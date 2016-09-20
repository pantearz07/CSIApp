package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SyncData;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

import static com.google.android.gms.location.LocationServices.*;

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
        users.id_users = mManager.getPreferenceData(users.COL_id_users);
        users.id_permission = mManager.getPreferenceData(users.COL_id_permission);
        users.pass = mManager.getPreferenceData(users.COL_pass);
        users.id_system = mManager.getPreferenceData(users.COL_id_system);
        users.title = mManager.getPreferenceData(users.COL_title);
        users.name = mManager.getPreferenceData(users.COL_name);
        users.surname = mManager.getPreferenceData(users.COL_surname);
        users.position = mManager.getPreferenceData(users.COL_position);
        users.picture = mManager.getPreferenceData(users.COL_picture);
        users.last_login = mManager.getPreferenceData(users.COL_last_login);
        // สร้าง TbOfficial จาก pref โดยชื่ออิงจาก TbOfficial เอง
        TbOfficial official = new TbOfficial();
        official.OfficialID = mManager.getPreferenceData(official.COL_OfficialID);
        official.FirstName = mManager.getPreferenceData(official.COL_FirstName);
        official.LastName = mManager.getPreferenceData(official.COL_LastName);
        official.Alias = mManager.getPreferenceData(official.COL_Alias);
        official.Rank = mManager.getPreferenceData(official.COL_Rank);
        official.Position = mManager.getPreferenceData(official.COL_Position);
        official.SubPossition = mManager.getPreferenceData(official.COL_SubPossition);
        official.PhoneNumber = mManager.getPreferenceData(official.COL_PhoneNumber);
        official.OfficialEmail = mManager.getPreferenceData(official.COL_OfficialEmail);
        official.OfficialDisplayPic = mManager.getPreferenceData(official.COL_OfficialDisplayPic);
        official.AccessType = mManager.getPreferenceData(official.COL_AccessType);
        official.SCDCAgencyCode = mManager.getPreferenceData(official.COL_SCDCAgencyCode);
        official.PoliceStationID = mManager.getPreferenceData(official.COL_PoliceStationID);
        official.id_users = mManager.getPreferenceData(official.COL_id_users);
        // นำค่าที่สร้างไปใช้ในการสร้าง ApiProfile ต่อ
        profile.setTbOfficial(official);
        if (official.AccessType.equalsIgnoreCase("inquiryofficial")) {
            profile.setPoliceStationID(mManager.getPreferenceData(official.COL_PoliceStationID));
        } else if (official.AccessType.equalsIgnoreCase("investigator")) {
            profile.setSCDCAgencyCode(mManager.getPreferenceData(official.COL_SCDCAgencyCode));
        }
        profile.setTbUsers(users);
    }


    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStartWelcome");
    }

    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit");
        dialog.setIcon(R.drawable.ic_noti);
        dialog.setCancelable(true);
        dialog.setMessage(R.string.ad_message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
