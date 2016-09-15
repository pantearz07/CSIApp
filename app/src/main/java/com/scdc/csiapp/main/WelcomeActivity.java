package com.scdc.csiapp.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiGCMRequest;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.gcmservice.GcmRegisterService;

/**
 * Created by Pantearz07 on 24/9/2558.
 */
public class WelcomeActivity extends AppCompatActivity {
    /*
         * สร้างตัวเชื่อม ApiConnect เพื่อให้แต่ละหน้าเรียกใช้ได้สะดวก
          */
    public static ApiConnect api;
    public static ApiLoginRequest login;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    //Context for other activity
    public static Context mContext;

    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    String officialID,username,password,accestype = "";
    boolean userlogin = false;
    String ipvalue;
    //private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // private boolean isReceiverRegistered;
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        mManager = new PreferenceData(this);
        mContext = this;
        accestype = mManager.getPreferenceData(mManager.KEY_ACCESSTYPE);
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        username = mManager.getPreferenceData(mManager.KEY_USERNAME);
        password = mManager.getPreferenceData(mManager.KEY_PASSWORD);
        userlogin = mManager.getPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS);
        ipvalue = mManager.getPreferenceData(mManager.KEY_IP);

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
                    SharedPreferences sp = getSharedPreferences(PreferenceData.KEY_PREFS, MODE_PRIVATE);

                    String nameOfficial = sp.getString(PreferenceData.KEY_NAME, "");
                    Toast.makeText(getBaseContext(),
                            "สวัสดีค่ะ คุณ " + nameOfficial,
                            Toast.LENGTH_SHORT).show();
                    cd = new ConnectionDetector(getApplicationContext());
                    if (cd.isNetworkAvailable()) {
                        Log.d("internet status", "connected");
//                        registerReceiver();
//
//                        if (checkPlayServices()) {
//                            registerGcm();
//                        }
                    } else {
                        Log.d("internet status", "no Internet Access");
                    }
                    Toast.makeText(getBaseContext(),
                            "คุณได้มีการล๊อคอินแล้ว",
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


    protected void onStart() {
        super.onStart();
        Log.i("Check", "onStartWelcome");


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
    protected void onResume() {
        super.onResume();
       // registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void registerGcm() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GcmRegisterService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences.getBoolean(GcmRegisterService.SENT_TOKEN_TO_SERVER, false);
            // TODO Do something here
            if (sentToken) {
                SharedPreferences shared = getApplicationContext().getSharedPreferences(GcmRegisterService.PREFS_TOKEN,
                        Context.MODE_PRIVATE);
                String tokenvalue = shared.getString("tokenKey", "not found!");
                Log.i(TAG, "Token value: " + tokenvalue);
                ApiGCMRequest gcmRequest = new ApiGCMRequest();
                gcmRequest.setUsername(username);
                gcmRequest.setPassword(password);
                gcmRequest.setRegisOfficialID(officialID);
                gcmRequest.setRegistration_id(tokenvalue);
                Connect connect = new Connect();
                connect.execute(gcmRequest);
            }
        }
    };
    class Connect extends AsyncTask<ApiGCMRequest, Void, ApiStatus> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ApiStatus doInBackground(ApiGCMRequest... params) {
            return WelcomeActivity.api.saveGCM(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());
                //  Toast.makeText(getApplication(), apiStatus.getData().getReason(), Toast.LENGTH_LONG).show();
            } else {
                //       Toast.makeText(getApplication(), apiStatus.getData().getReason(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
