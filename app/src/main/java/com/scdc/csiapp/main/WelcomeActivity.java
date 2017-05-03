package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by Pantearz07 on 24/9/2558.
 */
public class WelcomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // Google play services
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    /*
     * สร้างตัวเชื่อม ApiConnect เพื่อให้แต่ละหน้าเรียกใช้ได้สะดวก
    */
    public static ApiConnect api;
    public static ApiLoginRequest login;

    //ข้อมูลของผู้ใช้งานที่ Login ไว้
    public static ApiProfile profile;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    //Context for other activity
    public static Context mContext;

    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    String officialID, username, password, accestype = "";
    boolean userlogin = false;
    String ipvalue;
    DBHelper dbHelper;
    boolean flag_firstRun = true;
    //private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // private boolean isReceiverRegistered;
    private static final String TAG = "DEBUG-WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        mManager = new PreferenceData(this);
        mContext = this;
        userlogin = mManager.getPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS);
        ipvalue = mManager.getPreferenceData(mManager.KEY_IP);
        dbHelper = new DBHelper(mContext);
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.SelectSubCaseType();

        // ทำการสร้างตัวเชื่อกับ Google services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
            Log.d(TAG, "Create Google services");
        }

        //*** สร้าง ApiConnect ***//
        api = new ApiConnect(mContext);

        //*** ตรวจสอบ Internet ในกรณีที่ไม่เคย Login เพราะต้องใช้ในการส่งค่าตรวจสอบสิทธิ์ ***//
        // ถ้าไม่ต่อจะเตะออกแอพก่อน เพื่อให้ไปต่อก่อนถึงเข้าได้
        // แต่ถ้าเคย Login อยู่แล้ว ไม่ต่อก็ยังเข้าใช้ได้ไม่เตะออก
        cd = new ConnectionDetector(getApplicationContext());
        if (userlogin == false) {
            if (cd.isNetworkAvailable() == false) {
                Toast.makeText(getBaseContext(),
                        getString(R.string.network_unavailable),
                        Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }

        // ตรวจสอบการทำงานของระบบ GPS
        // ถ้าไม่เปิดจะไม่ให้ทำงานต่อเลย จะหยุดที่ขั้นตอนนี้เท่านั้นช
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setMessage(mContext.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(mContext.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(myIntent);
                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    checkLoginAndGo();
                }
            });
            dialog.show();
        } else {
            // กรณีผ่านการตรวจสอบ GPS แล้วก็จะทำการเช็คว่าเคย Login แล้วหรือยัง
            // และส่งไปตามสิทธิ์ที่ได้ Login ไว้
            checkLoginAndGo();
        }
    }

    private void checkLoginAndGo() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                if (userlogin) {
                    // ถ้าเคย Login อยู่แล้วจะสร้าง ApiProfile จาก pref
                    createApiProfile();

                    officialID = profile.getTbOfficial().OfficialID;
                    username = profile.getTbUsers().id_users;
                    password = profile.getTbUsers().pass;
                    accestype = profile.getTbOfficial().AccessType;
                    String nameOfficial = profile.getTbOfficial().Rank + profile.getTbOfficial().FirstName + " " + profile.getTbOfficial().LastName;

                    Toast.makeText(getBaseContext(),
                            "สวัสดีค่ะ คุณ " + nameOfficial,
                            Toast.LENGTH_SHORT).show();

                    if (accestype.equals("investigator")) {
                        finish();
                        startActivity(new Intent(mContext, MainActivity.class));
                    } else if (accestype.equals("inquiryofficial")) {
                        finish();
                        startActivity(new Intent(mContext, InqMainActivity.class));
                    }
                } else {
                    finish();
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

            }
        }, 2000);
    }

    // ใช้สร้าง ApiProfile จาก pref
    private void createApiProfile() {
        if (profile == null) {
            profile = new ApiProfile();
        }
        // สร้าง TbUsers จาก pref โดยชื่ออิงจาก TbUsers เอง
        TbUsers users = new TbUsers();
        users.id_users = mManager.getPreferenceData(dbHelper.COL_id_users);
        users.id_permission = mManager.getPreferenceData(dbHelper.COL_id_permission);
        users.pass = mManager.getPreferenceData(dbHelper.COL_pass);
        users.id_system = mManager.getPreferenceData(dbHelper.COL_id_system);
        users.title = mManager.getPreferenceData(dbHelper.COL_title);
        users.name = mManager.getPreferenceData(dbHelper.COL_name);
        users.surname = mManager.getPreferenceData(dbHelper.COL_surname);
        users.position = mManager.getPreferenceData(dbHelper.COL_position);
        users.picture = mManager.getPreferenceData(dbHelper.COL_picture);
        users.last_login = mManager.getPreferenceData(dbHelper.COL_last_login);
        // สร้าง TbOfficial จาก pref โดยชื่ออิงจาก TbOfficial เอง
        TbOfficial official = new TbOfficial();
        official.OfficialID = mManager.getPreferenceData(dbHelper.COL_OfficialID);
        official.FirstName = mManager.getPreferenceData(dbHelper.COL_FirstName);
        official.LastName = mManager.getPreferenceData(dbHelper.COL_LastName);
        official.Alias = mManager.getPreferenceData(dbHelper.COL_Alias);
        official.Rank = mManager.getPreferenceData(dbHelper.COL_Rank);
        official.Position = mManager.getPreferenceData(dbHelper.COL_Position);
        official.SubPossition = mManager.getPreferenceData(dbHelper.COL_SubPossition);
        official.PhoneNumber = mManager.getPreferenceData(dbHelper.COL_PhoneNumber);
        official.OfficialEmail = mManager.getPreferenceData(dbHelper.COL_OfficialEmail);
        official.OfficialDisplayPic = mManager.getPreferenceData(dbHelper.COL_OfficialDisplayPic);
        official.AccessType = mManager.getPreferenceData(dbHelper.COL_AccessType);
        official.SCDCAgencyCode = mManager.getPreferenceData(dbHelper.COL_SCDCAgencyCode);
        official.PoliceStationID = mManager.getPreferenceData(dbHelper.COL_PoliceStationID);
        official.id_users = mManager.getPreferenceData(dbHelper.COL_id_users);
        // นำค่าที่สร้างไปใช้ในการสร้าง ApiProfile ต่อ
        profile.setTbOfficial(official);
        if (official.AccessType.equalsIgnoreCase("inquiryofficial")) {
            profile.setPoliceStationID(mManager.getPreferenceData(dbHelper.COL_PoliceStationID));
            profile.setSCDCAgencyCode(mManager.getPreferenceData(dbHelper.COL_SCDCAgencyCode));
        } else if (official.AccessType.equalsIgnoreCase("investigator")) {
            profile.setSCDCAgencyCode(mManager.getPreferenceData(dbHelper.COL_SCDCAgencyCode));
        }
        profile.setTbUsers(users);
    }

    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStartWelcome");
    }

    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.ad_title);
        dialog.setIcon(R.drawable.ic_noti);
        dialog.setCancelable(true);
        dialog.setMessage(R.string.ad_message);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });

        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        Log.d(TAG, "onResume");

        // ตรวจสอบการ Login จะไม่ทำงานครั้งแรกที่เข้า onResume
        // เพราะ onCreate -> onResume ครั้งแรกเสมอ 
        if (flag_firstRun) {
            flag_firstRun = false;
        } else {
            // กรณีผ่านกลับมาเปิดหน้านั้นให้ทำการตรวจ Login ใหม่
            checkLoginAndGo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        Log.d(TAG, "Call Location Services");
        LocationRequest request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(10)//อ่านค่าใหม่ทุก 10 เมตร
                .setFastestInterval(1000)//อ่านค่าแบบรวดเร็วภายใน 1 วินาที
                .setInterval(10000);//อ่านค่าเป็นช่วงๆ ทุก 10 วินาที
        FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);

        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, "get mLastLocation");


//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location " + location.getLatitude() + " " + location.getLatitude());
    }
}
