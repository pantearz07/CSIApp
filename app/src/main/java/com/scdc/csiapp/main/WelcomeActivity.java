package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;

/**
 * Created by Pantearz07 on 24/9/2558.
 */
public class WelcomeActivity extends AppCompatActivity {
    /*
         * สร้างตัวเชื่อม ApiConnect เพื่อให้แต่ละหน้าเรียกใช้ได้สะดวก
          */
    public static ApiConnect api = new ApiConnect();
    public static ApiLoginRequest login;

    //Context for other activity
    public static Context mContext;

    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    String accestype="";
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

        cd = new ConnectionDetector(getApplicationContext());
        networkConnectivity = cd.networkConnectivity();
        isConnectingToInternet = cd.isConnectingToInternet();
        userlogin = mManager.getPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS);
        ipvalue = mManager.getPreferenceData(mManager.KEY_IP);
        Toast.makeText(getBaseContext(),
                "ค่า ip ล่าสุด"+ipvalue,
                Toast.LENGTH_SHORT).show();

        ApiConnect.updateIP();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                /*if (isConnectingToInternet == 1) {
                    Log.d("internet status", "connected to wifi");
                } else if (isConnectingToInternet == 2) {
                    Log.d("internet status", "data plan");
                } else {
                    Log.d("internet status", "no Internet Access");
                }*/

                if (userlogin) {
                    SharedPreferences sp = getSharedPreferences(PreferenceData.KEY_PREFS,MODE_PRIVATE);

                    String nameOfficial = sp.getString(PreferenceData.KEY_NAME,"");
                    Toast.makeText(getBaseContext(),
                            "สวัสดีค่ะ คุณ "+nameOfficial,
                            Toast.LENGTH_SHORT).show();

                    if (networkConnectivity) {
                        Log.d("internet status", "connected");
                       /* registerReceiver();

                        if (checkPlayServices()) {
                            registerGcm();
                        }*/
                    } else {
                        Log.d("internet status", "no Internet Access");
                    }
                    Toast.makeText(getBaseContext(),
                            "คุณได้มีการล๊อคอินแล้ว",
                            Toast.LENGTH_SHORT).show();
                    if(accestype.equals("investigator")) {
                        switchPageToMain();
                    }else if(accestype.equals("inquiryofficial")) {
                        switchPageToMainInq();

                    }


                } else {
                    switchPageLogin();
                }

            }
        }, 2000);
    }

    protected void switchPageToMain() {
        Intent gotoMainActivity = new Intent(mContext, MainActivity.class);

        startActivity(gotoMainActivity);
        finish();
    }
    protected void switchPageToMainInq() {
        Intent gotoInqMainActivity = new Intent(mContext, InqMainActivity.class);

        startActivity(gotoInqMainActivity);
        finish();
    }
    protected void switchPageLogin() {


        Intent gotoLoginPage = new Intent(this, LoginActivity.class);

        startActivity(gotoLoginPage);
        finish();
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
        dialog.setMessage("Do you want to exit?");
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
        //registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkConnectivity) {
            Log.i("networkConnectivity", "connect!! ");
           // unregisterReceiver();
        }else {
            Log.i("networkConnectivity", "no connect!! ");
        }
    }
/*
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
        }
    };

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }*/
}
