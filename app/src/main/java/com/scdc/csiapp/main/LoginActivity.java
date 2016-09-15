package com.scdc.csiapp.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiLoginStatus;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;

import java.util.Date;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;

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
    Boolean networkConnectivity = false;
    String username;
    String password;
    String txt_username = "";

    // กำหนดค่าเวลา และตัว Handler สำหรับตรวจการเชื่อมกับเซิร์ฟเวอร์ทุก 10 วินาที
    private final static int INTERVAL = 1000 * 10; //10 second
    Handler mHandler = new Handler();
    Snackbar snackbar;

    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;

    GetDateTime getDateTime;
    TextView txt_ipvalue;
    private static NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mContext = this;
        mManager = new PreferenceData(this);

        txt_username = mManager.getPreferenceData(mManager.KEY_USERNAME);

        cd = new ConnectionDetector(getApplicationContext());
        networkConnectivity = cd.isNetworkAvailable();

        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        getDateTime = new GetDateTime();

        loginButton = (Button) findViewById(R.id.loginButton);
        settingip_btn = (Button) findViewById(R.id.settingip_btn);

        mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์

        //เเสดงค่า IP ล่าสุด
        txt_ipvalue = (TextView) findViewById(R.id.txt_ipvalue);
        SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, MODE_PRIVATE);
        String IP = sp.getString(PreferenceData.KEY_IP, "192.168.0.89");
        txt_ipvalue.setText("ค่า IP ล่าสุด: " + IP);

        mUsername = (EditText) findViewById(R.id.usernameEdt);
        mPassword = (EditText) findViewById(R.id.passwordEdt);
        mUsername.setText(txt_username);
        username = mUsername.getText().toString().trim().toLowerCase();

        password = mPassword.getText().toString().trim();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isNetworkAvailable()) {
                    Log.d("internet status", "connected to wifi");
                    login();

                } else {
                    Log.d("internet status", "no Internet Access");
                    if (mManager.checkLoginValidate(username, password)) {
                        Toast.makeText(getBaseContext(),
                                "คุณได้มีการล๊อคอินแล้ว\nแต่ไม่ได้เชื่อมต่ออินเตอร์เน็ต",
                                Toast.LENGTH_SHORT).show();
                        //showNotification();
                        switchPageToMain();

                    } else {
                        // loginnoconnect();


                    }
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

    private void loginnoconnect() {
        username = mUsername.getText().toString().trim().toLowerCase();
        password = mPassword.getText().toString().trim();
        //new checklogin().execute(username, password);
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
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
            * สร้าง dialog popup ขึ้นมาแสดงตอนกำลัง login เข้าระบบเป็นเวลา 3 วินาที
            */
            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected ApiLoginStatus doInBackground(ApiLoginRequest... params) {
            return WelcomeActivity.api.login(params[0]);

        }

        @Override
        protected void onPostExecute(ApiLoginStatus apiLoginStatus) {
            super.onPostExecute(apiLoginStatus);
            progressDialog.dismiss();
            if (apiLoginStatus.getStatus().equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(), apiLoginStatus.getData().getReason(), Toast.LENGTH_LONG).show();
                boolean isSuccess = mManager.registerUser(apiLoginStatus.getData().getResult().getUsers().getId_users(),
                        apiLoginStatus.getData().getResult().getUsers().getPass(),
                        apiLoginStatus.getData().getResult().getOfficial().get(0).getOfficialID(),
                        apiLoginStatus.getData().getResult().getOfficial().get(0).getAccessType(),
                        apiLoginStatus.getData().getResult().getOfficial().get(0).getRank() + " " +
                                apiLoginStatus.getData().getResult().getOfficial().get(0).getFirstName() + " " +
                                apiLoginStatus.getData().getResult().getOfficial().get(0).getLastName(),
                        apiLoginStatus.getData().getResult().getOfficial().get(0).getSCDCAgencyCode());
                if (isSuccess) {
                    onLoginSuccess();
                } else {
                    Toast.makeText(getApplicationContext(), "บันทึก pref ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                }


            } else {
                loginButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "ผิดพลาด", Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean validate() {
        boolean valid = true;
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        if (username.isEmpty()) {
            mUsername.setError("enter a valid email address");
            valid = false;
        } else {
            mUsername.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Log.d("Login status", "Login Success");
        boolean isLoginstatus = mManager.setPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS, true);
        if (isLoginstatus) {
            // new SaveOfficialDataToSQLiteTask().execute(strOfficialID);
            switchPageToMain();

        }

    }

    public void onLoginFailed() {
        Log.d("Login status", "Login failed");

        loginButton.setEnabled(true);
        boolean isLoginstatus = mManager.setPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS, false);
        if (isLoginstatus) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        }
    }


    //อันเก่า
    private void _Login() {
        username = mUsername.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        // Toast.makeText(mContext, "Username: " + username + " Password: " + password + " \nสถานะ: " + selectedaccesstype[0], Toast.LENGTH_SHORT).show();
        //new SimpleTask().execute(username, password, selectedaccesstype[0]);


    }

    private class checklogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String[] checkUser = mDbHelper.checkUser(params[0], params[1], params[2]);
            String status = null;
            if (checkUser != null) {

                Log.i("checkuser", params[0] + params[1] + params[2]);
//                boolean isSuccess = mManager.registerUser(params[0], params[1], checkUser[0], params[2]);
//                if (isSuccess) {
//                    boolean isLoginstatus = mManager.setPreferenceDataBoolean(mManager.KEY_USER_LOGGEDIN_STATUS, true);
//                    if (isLoginstatus) {
//
//                        status = "have";
//                    }
//                }
            } else {
                status = "no";
                Log.i("checkuser ", status + " " + params[0] + params[1] + params[2]);
            }
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            if (status == "have") {

                Toast.makeText(getBaseContext(),
                        "คุณได้มีการล๊อคอินแล้ว\nแต่ไม่ได้เชื่อมต่ออินเตอร์เน็ต",
                        Toast.LENGTH_SHORT).show();
                showNotification();
                switchPageToMain();
            } else if (status == "no") {
                Toast.makeText(getBaseContext(), "กรุณาเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
            }
        }
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
        protected ApiStatus doInBackground(ApiStatus... booleen) {
            return WelcomeActivity.api.checkConnect();
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            if (apiStatus != null && apiStatus.getStatus().equalsIgnoreCase("success")) {
                mHandler.removeCallbacks(mHandlerTaskcheckConnect);
            } else {
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