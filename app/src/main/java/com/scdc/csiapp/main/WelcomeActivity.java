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
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

/**
 * Created by Pantearz07 on 24/9/2558.
 */
public class WelcomeActivity extends AppCompatActivity {
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
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        mManager = new PreferenceData(this);
        mContext = this;
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
        users.id_users = mManager.getPreferenceData(TbUsers.COL_id_users);
        users.id_permission = mManager.getPreferenceData(TbUsers.COL_id_permission);
        users.pass = mManager.getPreferenceData(TbUsers.COL_pass);
        users.id_system = mManager.getPreferenceData(TbUsers.COL_id_system);
        users.title = mManager.getPreferenceData(TbUsers.COL_title);
        users.name = mManager.getPreferenceData(TbUsers.COL_name);
        users.surname = mManager.getPreferenceData(TbUsers.COL_surname);
        users.position = mManager.getPreferenceData(TbUsers.COL_position);
        users.picture = mManager.getPreferenceData(TbUsers.COL_picture);
        users.last_login = mManager.getPreferenceData(TbUsers.COL_last_login);
        // สร้าง TbOfficial จาก pref โดยชื่ออิงจาก TbOfficial เอง
        TbOfficial official = new TbOfficial();
        official.OfficialID = mManager.getPreferenceData(TbOfficial.COL_OfficialID);
        official.FirstName = mManager.getPreferenceData(TbOfficial.COL_FirstName);
        official.LastName = mManager.getPreferenceData(TbOfficial.COL_LastName);
        official.Alias = mManager.getPreferenceData(TbOfficial.COL_Alias);
        official.Rank = mManager.getPreferenceData(TbOfficial.COL_Rank);
        official.Position = mManager.getPreferenceData(TbOfficial.COL_Position);
        official.SubPossition = mManager.getPreferenceData(TbOfficial.COL_SubPossition);
        official.PhoneNumber = mManager.getPreferenceData(TbOfficial.COL_PhoneNumber);
        official.OfficialEmail = mManager.getPreferenceData(TbOfficial.COL_OfficialEmail);
        official.OfficialDisplayPic = mManager.getPreferenceData(TbOfficial.COL_OfficialDisplayPic);
        official.AccessType = mManager.getPreferenceData(TbOfficial.COL_AccessType);
        official.SCDCAgencyCode = mManager.getPreferenceData(TbOfficial.COL_SCDCAgencyCode);
        official.PoliceStationID = mManager.getPreferenceData(TbOfficial.COL_PoliceStationID);
        official.id_users = mManager.getPreferenceData(TbOfficial.COL_id_users);
        // นำค่าที่สร้างไปใช้ในการสร้าง ApiProfile ต่อ
        profile.setTbOfficial(official);
        if (official.AccessType.equalsIgnoreCase("inquiryofficial")) {
            profile.setPoliceStationID(mManager.getPreferenceData(TbOfficial.COL_PoliceStationID));
        } else if (official.AccessType.equalsIgnoreCase("investigator")) {
            profile.setSCDCAgencyCode(mManager.getPreferenceData(TbOfficial.COL_SCDCAgencyCode));
        }
        profile.setTbUsers(users);
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

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
