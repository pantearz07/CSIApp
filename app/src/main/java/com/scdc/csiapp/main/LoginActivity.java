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
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SyncDataLogin;
import com.scdc.csiapp.gcmservice.GcmRegisterService;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

import org.parceler.Parcels;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Created by Pantearz07 on 24/9/2558.
 */
public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    ImageButton settingip_btn;
    private EditText mUsername;
    private EditText mPassword;
    public static Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    ApiConnect api;
    public static final String KEY_CONNECT = "key_connect";
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

    private static String strSDCardPathName_temp = "/CSIFiles" + "/temp/";
    private static String strSDCardPathName_temps = "/CSIFiles" + "/temp/temps/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mContext = this;
        mManager = new PreferenceData(this);
        cd = new ConnectionDetector(getApplicationContext());
        mDbHelper = new DBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        getDateTime = new GetDateTime();
        BusProvider.getInstance().register(this);
        bindView();

        txt_username = mManager.getPreferenceData(mDbHelper.COL_id_users);

        loginButton.setEnabled(false);// ปิดการทำงานไว้จนกว่าจะตรวจว่าเชื่อมเซิร์ฟเวอร์ได้จริง

        mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์

        loginButton.setOnClickListener(new LoginOnClickListener());
        settingip_btn.setOnClickListener(new LoginOnClickListener());

    }

    private void bindView() {
        //เเสดงค่า IP ล่าสุด
        txt_ipvalue = (TextView) findViewById(R.id.txt_ipvalue);
        loginButton = (Button) findViewById(R.id.loginButton);
        settingip_btn = (ImageButton) findViewById(R.id.settingip_btn);
        mUsername = (EditText) findViewById(R.id.usernameEdt);
        mPassword = (EditText) findViewById(R.id.passwordEdt);
        mUsername.setText(txt_username);
        username = mUsername.getText().toString().trim().toLowerCase();
        password = mPassword.getText().toString().trim();

    }

    private class LoginOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (v == loginButton) {
                if (cd.isNetworkAvailable()) {
                    login();
                } else {
                    switchPageToMain();
                }
            }
            if (v == settingip_btn) {
                //*** ส่วนการทำงานปุ่มตั้งค่า IP ***ฝฝ
                // คำสั่งทั้งหมดในการทำงานของปุ่มตั่งค่า ทั้ง แสดง Dialog
                // บันทึกการเปลี่ยนแปลงผ่าน updateIP จาก ApiConnect
                // ปิดการแจ้งเตือนเก่า สั่งส่วนตรวจสอบให้ตรวจใหม่อีกครั้ง
                if (cd.isNetworkAvailable()) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(LoginActivity.this);
                    LayoutInflater inflater = getLayoutInflater();

                    View view = inflater.inflate(R.layout.ipsetting_dialog, null);
                    builder.setView(view);
                    final AutoCompleteTextView ipvalueEdt = (AutoCompleteTextView) view.findViewById(R.id.ipvalueEdt);
                    final String[] ip_list = getResources().getStringArray(
                            R.array.ip_list);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line,
                            ip_list);
                    ipvalueEdt.setThreshold(1);
                    ipvalueEdt.setAdapter(adapter);
                    ipvalueEdt.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {

                            ipvalueEdt.showDropDown();

                            return false;
                        }
                    });
                    builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ipvalueEdt.getText().equals("")) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.please_input_data),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                String ipvalue = ipvalueEdt.getText().toString();
                                Log.d(TAG, "ipvalue " + ipvalue);
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
        }
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

        // ซ่อน Keyborad หลังจากกด Login แล้ว
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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
            progressDialog.setCancelable(false);
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
                Log.i(TAG, "password :" + users.pass.toString());
                Toast.makeText(getApplicationContext(), apiLoginStatus.getData().getReason(), Toast.LENGTH_LONG).show();
                boolean isSuccess = mManager.registerUser(users, official);
                if (isSuccess) {
                    onLoginSuccess();
                } else {
                    Toast.makeText(getApplicationContext(), "บันทึก pref ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                }
                if (apiLoginStatus.getData().getResult().getUsers().getPicture() == null || apiLoginStatus.getData().getResult().getUsers().getPicture().equals("")) {

                } else {
                    DownloadDocFile downloadDocFile = new DownloadDocFile();
                    downloadDocFile.execute(apiLoginStatus.getData().getResult().getUsers().getPicture());
                }
                // ดึงข้อมูลอัพเดทจากเซิร์ฟเวอร์
                SyncDataLogin syncData = new SyncDataLogin();
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
            WelcomeActivity.api.setSorting("0");
            //switchPageToMain();
            registerReceiver();
            if (checkPlayServices()) {
                registerGcm();
            }
//            showNotification();
        }

    }

    public void showNotification() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // API 16 onwards
            Notification.Builder builder = new Notification.Builder(mContext);
            builder.setAutoCancel(true)
                    //.setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.app_logo)
                    .setContentTitle("CSI Report ยินดีต้อนรับ")
                    .setContentText("คุณได้ทำการเข้าสู่ระบบ CSI Report เรียบร้อยแล้ว")
                    .setAutoCancel(true)
                    .setWhen((new Date()).getTime())
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL);
//            Notification notification = builder.build();
//            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
//            mNotificationManager =
//                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            mNotificationManager.notify(1000, notification);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1001, builder.build());
        } else {

            Notification notification =
                    new NotificationCompat.Builder(this) // this is context
                            .setSmallIcon(R.mipmap.app_logo)
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
                loginButton.setEnabled(true);
            } else {
                loginButton.setEnabled(false);
                if (snackbar == null || !snackbar.isShown()) {
                    View rootView = findViewById(R.id.drawerLayoutLogin);
                    snackbar = Snackbar.make(rootView, getString(R.string.cannot_connect_server), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (cd.isNetworkAvailable()) {
                                        AlertDialog.Builder builder =
                                                new AlertDialog.Builder(LoginActivity.this);
                                        LayoutInflater inflater = getLayoutInflater();

                                        View view = inflater.inflate(R.layout.ipsetting_dialog, null);
                                        builder.setView(view);
                                        final AutoCompleteTextView ipvalueEdt = (AutoCompleteTextView) view.findViewById(R.id.ipvalueEdt);
                                        final String[] ip_list = getResources().getStringArray(
                                                R.array.ip_list);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line,
                                                ip_list);
                                        ipvalueEdt.setThreshold(1);
                                        ipvalueEdt.setAdapter(adapter);
                                        ipvalueEdt.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                                ipvalueEdt.showDropDown();

                                                return false;
                                            }
                                        });

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
            progressDialog.setCancelable(false);
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

    class DownloadDocFile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar

        }

        @Override
        protected String doInBackground(String... params) {
            int count;
            Log.i(TAG, "DownloadDocFile display file name " + String.valueOf(params[0]));
            File myDir;
            String fileoutput = "";
            try {
                myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), strSDCardPathName_temp);
//                myDir = new File(strSDCardPathName_temp);
                myDir.mkdirs();
                //http://localhost/mCSI/assets/csifiles/CR04_000002/docs/
                String defaultIP = "180.183.251.32/mcsi";
                SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
                defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
                String filepath = "http://" + defaultIP + "/assets/img/users/" + params[0];
                Log.i(TAG, "display file name: " + filepath);

                URL url = new URL(filepath);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d(TAG, "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                File filePic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), strSDCardPathName_temp + params[0]);
                if (filePic.exists()) {
                    filePic.delete();
                }
                filePic.createNewFile();

                // Get File Name from URL
//                fileoutput = strSDCardPathName_temp + params[0];
                Log.i(TAG, "filePic : " + filePic.getPath());
                OutputStream output = new FileOutputStream(filePic);
                //OutputStream output = new FileOutputStream("/sdcard/Download/"+fileName+".doc");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                if (total > 0) {
//                    Toast.makeText(LoginActivity.this, getString(R.string.save_photo_success), Toast.LENGTH_SHORT).show();
                }
                output.flush();
                output.close();
                input.close();
                return fileoutput;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return "error";
            }


        }

    }

    protected void onPostExecute(final String arrData) {
        Log.i(TAG, "DownloadDocFile display" + String.valueOf(arrData));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        api = Parcels.unwrap(savedInstanceState.getParcelable(KEY_CONNECT));
        if (WelcomeActivity.api == null) {
            WelcomeActivity.api = new ApiConnect(this);
            WelcomeActivity.api = api;
        } else {
            WelcomeActivity.api = api;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (api == null) {
            api = new ApiConnect(this);
            api = WelcomeActivity.api;
        } else {
            api = WelcomeActivity.api;
        }
        outState.putParcelable(KEY_CONNECT, Parcels.wrap(api));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        txt_ipvalue.setText(getString(R.string.current_ip_server) + WelcomeActivity.api.getDefaultIP());

    }

}