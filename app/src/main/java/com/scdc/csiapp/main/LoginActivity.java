package com.scdc.csiapp.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiGCMRequest;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiLoginStatus;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SyncData;
import com.scdc.csiapp.gcmservice.GcmRegisterService;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

import java.util.Date;

/**
 * Created by Pantearz07 on 24/9/2558.
 */
public class LoginActivity extends AppCompatActivity {

    Button loginButton, settingip_btn;
    private EditText mUsername;
    private EditText mPassword;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;

    String username;
    String password;
    String txt_username, txt_password = "";
    String officialid;
    // กำหนดค่าเวลา และตัว Handler สำหรับตรวจการเชื่อมกับเซิร์ฟเวอร์ทุก 10 วินาที
    private final static int INTERVAL = 1000 * 10; //10 second
    Handler mHandler = new Handler();
    Snackbar snackbar;
    private static final String TAG = "DEBUG-LoginActivity";
    // connect sqlite
    SQLiteDatabase mDb;
    DBHelper mDbHelper;

    GetDateTime getDateTime;
    TextView txt_ipvalue;
    private static NotificationManager mNotificationManager;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mContext = this;
        mManager = new PreferenceData(this);

        txt_username = mManager.getPreferenceData(mManager.KEY_USERNAME);

        cd = new ConnectionDetector(getApplicationContext());

        mDbHelper = new DBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        getDateTime = new GetDateTime();

        loginButton = (Button) findViewById(R.id.loginButton);
        settingip_btn = (Button) findViewById(R.id.settingip_btn);

        mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์

        //เเสดงค่า IP ล่าสุด
        txt_ipvalue = (TextView) findViewById(R.id.txt_ipvalue);
        txt_ipvalue.setText(getString(R.string.current_ip_server) + WelcomeActivity.api.getDefaultIP());

        mUsername = (EditText) findViewById(R.id.usernameEdt);
        mPassword = (EditText) findViewById(R.id.passwordEdt);
        mUsername.setText(txt_username);
        username = mUsername.getText().toString().trim().toLowerCase();
        password = mPassword.getText().toString().trim();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isNetworkAvailable()) {
                    login();
                } else {
                    switchPageToMain();
                }
            }
        });

        //*** ส่วนการทำงานปุ่มตั้งค่า IP ***ฝฝ
        // คำสั่งทั้งหมดในการทำงานของปุ่มตั่งค่า ทั้ง แสดง Dialog
        // บันทึกการเปลี่ยนแปลงผ่าน updateIP จาก ApiConnect
        // ปิดการแจ้งเตือนเก่า สั่งส่วนตรวจสอบให้ตรวจใหม่อีกครั้ง
        settingip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isNetworkAvailable()) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(LoginActivity.this);
                    LayoutInflater inflater = getLayoutInflater();

                    View view = inflater.inflate(R.layout.ipsetting_dialog, null);
                    builder.setView(view);
                    final EditText ipvalueEdt = (EditText) view.findViewById(R.id.ipvalueEdt);

                    builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ipvalueEdt.getText().equals("")) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.please_input_data),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                String ipvalue = ipvalueEdt.getText().toString();
                                WelcomeActivity.api.updateIP(ipvalue);

                                if (snackbar != null) {
                                    snackbar.dismiss();//ปิดการแจ้งเตือนเก่าออกให้หมดก่อนตรวจใหม่
                                }
                                mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
                                mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่
                                txt_ipvalue.setText(getString(R.string.current_ip_server) + WelcomeActivity.api.getDefaultIP());

                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.save_complete),
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();

                } else {
                    Toast.makeText(getBaseContext(),
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void switchPageToMain() {
        Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
        finish();
        startActivity(gotoWelcomeActivity);

    }

    public void login() {
        if (!validate()) {
            loginButton.setEnabled(true);
            mManager.setPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS, false);
            return;
        }

        loginButton.setEnabled(false);
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        ApiLoginRequest login = new ApiLoginRequest(username, password);
        Connect connect = new Connect();
        connect.execute(login);
    }

    class Connect extends AsyncTask<ApiLoginRequest, Void, ApiLoginStatus> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*
            * สร้าง dialog popup ขึ้นมาแสดงตอนกำลัง login
            */
            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.authenticating));
            progressDialog.show();
        }

        @Override
        protected ApiLoginStatus doInBackground(ApiLoginRequest... params) {
            return WelcomeActivity.api.login(params[0]);

        }

        @Override
        protected void onPostExecute(ApiLoginStatus apiLoginStatus) {
            super.onPostExecute(apiLoginStatus);
            if (apiLoginStatus.getStatus().equalsIgnoreCase("success")) {
                //*** การทำงานทั้งหมดเพื่อสร้าง ApiProfile ***//
                // นำค่าที่ได้รับมาสร้างเป็น ApiProfile ไว้ที่ WelcomeActivity ให้หน้าอื่นเรียกใช้ได้ง่ายๆ
                if (WelcomeActivity.profile == null) {
                    WelcomeActivity.profile = new ApiProfile();
                }
                // สร้าง TbUsers จาก ApiLoginStatus
                TbUsers users = new TbUsers();
                users.id_users = apiLoginStatus.getData().getResult().getUsers().getId_users();
                users.id_permission = apiLoginStatus.getData().getResult().getUsers().getId_permission();
                users.pass = apiLoginStatus.getData().getResult().getUsers().getPass();
                users.id_system = apiLoginStatus.getData().getResult().getUsers().getId_system();
                users.title = apiLoginStatus.getData().getResult().getUsers().getTitle();
                users.name = apiLoginStatus.getData().getResult().getUsers().getName();
                users.surname = apiLoginStatus.getData().getResult().getUsers().getSurname();
                users.position = apiLoginStatus.getData().getResult().getUsers().getPosition();
                users.picture = apiLoginStatus.getData().getResult().getUsers().getPicture();
                users.last_login = apiLoginStatus.getData().getResult().getUsers().getLast_login();
                // สร้าง TbOfficial จาก ApiLoginStatus
                TbOfficial official = new TbOfficial();
                official.OfficialID = apiLoginStatus.getData().getResult().getOfficial().getOfficialID();
                official.FirstName = apiLoginStatus.getData().getResult().getOfficial().getFirstName();
                official.LastName = apiLoginStatus.getData().getResult().getOfficial().getLastName();
                official.Alias = apiLoginStatus.getData().getResult().getOfficial().getAlias();
                official.Rank = apiLoginStatus.getData().getResult().getOfficial().getRank();
                official.Position = apiLoginStatus.getData().getResult().getOfficial().getPosition();
                official.SubPossition = apiLoginStatus.getData().getResult().getOfficial().getSubPossition();
                official.PhoneNumber = apiLoginStatus.getData().getResult().getOfficial().getPhoneNumber();
                official.OfficialEmail = apiLoginStatus.getData().getResult().getOfficial().getOfficialEmail();
                official.OfficialDisplayPic = apiLoginStatus.getData().getResult().getOfficial().getOfficialDisplayPic();
                official.AccessType = apiLoginStatus.getData().getResult().getOfficial().getAccessType();
                official.SCDCAgencyCode = apiLoginStatus.getData().getResult().getOfficial().getSCDCAgencyCode();
                official.PoliceStationID = apiLoginStatus.getData().getResult().getOfficial().getPoliceStationID();
                official.id_users = apiLoginStatus.getData().getResult().getOfficial().getId_users();
                // นำค่าที่สร้างไปใช้ในการสร้าง ApiProfile ต่อ
                WelcomeActivity.profile.setTbOfficial(official);
                if (official.AccessType.equalsIgnoreCase("inquiryofficial")) {
                    WelcomeActivity.profile.setPoliceStationID(apiLoginStatus.getData().getResult().getOfficial().getPoliceStationID());
                } else if (official.AccessType.equalsIgnoreCase("investigator")) {
                    WelcomeActivity.profile.setSCDCAgencyCode(apiLoginStatus.getData().getResult().getOfficial().getSCDCAgencyCode());
                }
                WelcomeActivity.profile.setTbUsers(users);

                Toast.makeText(getApplicationContext(), apiLoginStatus.getData().getReason(), Toast.LENGTH_LONG).show();
                boolean isSuccess = mManager.registerUser(users, official);
                if (isSuccess) {
                    onLoginSuccess();
                } else {
                    Toast.makeText(getApplicationContext(), "บันทึก pref ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                }

                // ดึงข้อมูลอัพเดทจากเซิร์ฟเวอร์
                SyncData syncData = new SyncData();
                syncData.execute();
            } else {
                loginButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "ผิดพลาด", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        if (username.isEmpty()) {
            mUsername.setError(getString(R.string.please_input_username));
            valid = false;
        } else {
            mUsername.setError(null);
        }

        if (password.isEmpty()) {
            mPassword.setError(getString(R.string.please_input_password));
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        boolean isLoginstatus = mManager.setPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS, true);
        if (isLoginstatus) {
            // new SaveOfficialDataToSQLiteTask().execute(strOfficialID);
            //switchPageToMain();
            registerReceiver();
            if (checkPlayServices()) {
                registerGcm();
            }

        }

    }

    //อันเก่า
    private void _Login() {
        username = mUsername.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        // Toast.makeText(mContext, "Username: " + username + " Password: " + password + " \nสถานะ: " + selectedaccesstype[0], Toast.LENGTH_SHORT).show();
        //new SimpleTask().execute(username, password, selectedaccesstype[0]);


    }

    public void showNotification() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // API 16 onwards
            Notification.Builder builder = new Notification.Builder(mContext);
            builder.setAutoCancel(false)
                    //.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_csi)
                    .setContentTitle("CSI Report ยินดีต้อนรับ")
                    .setContentText("คุณได้ทำการเข้าสู่ระบบ CSI Report เรียบร้อยแล้ว")
                    .setAutoCancel(true)
                    .setWhen((new Date()).getTime())
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL);
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
            mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(1000, notification);
        } else {

            Notification notification =
                    new NotificationCompat.Builder(this) // this is context
                            .setSmallIcon(R.drawable.logo_csi)
                            .setContentTitle("CSI Report ยินดีต้อนรับ")
                            .setContentText("คุณได้ทำการเข้าสู่ระบบ CSI Report เรียบร้อยแล้ว")
                            .setAutoCancel(true)
                            .build();
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1000, notification);

        }
    }

    //*** การทำงานในส่วนตรวจการเชื่อมกับเซิร์ฟเวอร์ ***//
    // mHandlerTaskcheckConnect เป็น Runnable คำสั่งต่างๆ ให้ Handler ไว้ควบคุม
    // ConnectApiCheckConnect คลาส AsyncTask ที่เอาไว้เชื่อมกับ ApiConnect
    Runnable mHandlerTaskcheckConnect = new Runnable() {
        @Override
        public void run() {
            ConnectApiCheckConnect connectApi = new ConnectApiCheckConnect();
            connectApi.execute();
            mHandler.postDelayed(mHandlerTaskcheckConnect, INTERVAL);
        }
    };

    class ConnectApiCheckConnect extends AsyncTask<ApiStatus, Boolean, ApiStatus> {

        @Override
        protected ApiStatus doInBackground(ApiStatus... apiStatuses) {
            return WelcomeActivity.api.checkConnect();
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            if (apiStatus != null && apiStatus.getStatus().equalsIgnoreCase("success")) {
                mHandler.removeCallbacks(mHandlerTaskcheckConnect);
            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    View rootView = findViewById(R.id.drawerLayoutLogin);
                    snackbar = Snackbar.make(rootView, getString(R.string.cannot_connect_server), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
                }
            }
        }
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

                String username = WelcomeActivity.profile.getTbUsers().id_users;
                String password = WelcomeActivity.profile.getTbUsers().pass;
                String officialid = WelcomeActivity.profile.getTbOfficial().OfficialID;

                ApiGCMRequest gcmRequest = new ApiGCMRequest();
                gcmRequest.setUsername(username);
                gcmRequest.setPassword(password);
                gcmRequest.setRegisOfficialID(officialid);
                gcmRequest.setRegistration_id(tokenvalue);
                LoginActivity.GCM gcmconnect = new GCM();
                gcmconnect.execute(gcmRequest);
            }
        }
    };

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

    class GCM extends AsyncTask<ApiGCMRequest, Void, ApiStatus> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*
            * สร้าง dialog popup ขึ้นมาแสดงตอนกำลัง login เข้าระบบเป็นเวลา 3 วินาที
            */
            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.send_token));
            progressDialog.show();
        }

        @Override
        protected ApiStatus doInBackground(ApiGCMRequest... params) {
            return WelcomeActivity.api.saveGCM(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            progressDialog.dismiss();
            Log.d(TAG, apiStatus.getStatus());
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());
                switchPageToMain();

            } else {
                Toast.makeText(getApplication(), apiStatus.getData().getReason(), Toast.LENGTH_LONG).show();
            }
        }
    }
}