package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scdc.csiapp.apimodel.ApiGCMRequest;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiLoginStatus;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.syncmodel.SyncAmphur;
import com.scdc.csiapp.syncmodel.SyncCaseSceneType;
import com.scdc.csiapp.syncmodel.SyncComPosition;
import com.scdc.csiapp.syncmodel.SyncDistrict;
import com.scdc.csiapp.syncmodel.SyncGeography;
import com.scdc.csiapp.syncmodel.SyncInqPosition;
import com.scdc.csiapp.syncmodel.SyncInvPosition;
import com.scdc.csiapp.syncmodel.SyncOfficial;
import com.scdc.csiapp.syncmodel.SyncPermission;
import com.scdc.csiapp.syncmodel.SyncPoliceAgency;
import com.scdc.csiapp.syncmodel.SyncPoliceCenter;
import com.scdc.csiapp.syncmodel.SyncPolicePosition;
import com.scdc.csiapp.syncmodel.SyncPoliceRank;
import com.scdc.csiapp.syncmodel.SyncPoliceStation;
import com.scdc.csiapp.syncmodel.SyncProvince;
import com.scdc.csiapp.syncmodel.SyncSCDCAgency;
import com.scdc.csiapp.syncmodel.SyncSCDCCenter;
import com.scdc.csiapp.syncmodel.SyncSubCaseSceneType;
import com.scdc.csiapp.tablemodel.SyncResultSceneType;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Pantearz07 on 12/9/2559.
 */
public class ApiConnect {

    private String urlMobileIP = "http://192.168.0.100/mCSI/C_mobile/";
    private String defaultIP = "192.168.0.100";
    // private String URL = "http://192.168.0.89/mCSI/C_mobile/";
    private String TAG = "DEBUG-ApiConnect";
    private Context mContext;
    private OkHttpClient okHttpClient = new OkHttpClient();

    public ApiConnect(Context context) {
        mContext = context;
        updateApiConnect();
    }

    // ตรวจสอบการเชื่อมต่อกับระบบผ่าน function checkConnect
    public ApiStatus checkConnect() {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "checkConnect")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "checkConnect success");
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(response.body().string(), ApiStatus.class);
            } else {
                Log.d(TAG, "checkConnect fail");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in checkConnect : " + e.getMessage());
        }
        return null;
    }

    // ส่วนการทำงาน Update ผ่านในคลาส เพื่อดึงค่า IP ล่าสุด และอัพเดทลิงค์การเชื่อม
    private void updateApiConnect() {
        SharedPreferences sp = mContext.getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        urlMobileIP = "http://" + defaultIP + "/mCSI/C_mobile/";
        Log.d(TAG, "update Api Connect " + urlMobileIP);
    }

    // ใช้ในการสั่งเปลี่ยน IP และจะบันทึกลง Preferences ให้เอง
    public boolean updateIP(String ip) {
        SharedPreferences sp = mContext.getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PreferenceData.KEY_IP, ip);
        editor.commit();
        Log.d(TAG, "updateIP Api Connect " + ip);
        updateApiConnect();
        return true;
    }

    // ตรวจสอบการ Login และข้อมูลผู้ใช้งาน User , Official ผ่าน function login
    public ApiLoginStatus login(ApiLoginRequest dataLogin) {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", dataLogin.getUsername())
                .add("Password", dataLogin.getPassword())
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "login")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(response.body().string(), ApiLoginStatus.class);
            } else {
                Log.d(TAG, "Not Success - code in login : " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in login : " + e.getMessage());
            return null;
        }
    }

    //** ใช้อัปเดทข้อมูล User , Official ผ่าน function editProfile
    // ส่งข้อมูลด้วย
    // ApiProfile
    // Username,Password ดึงเอาจาก User ของ ApiProfile
    public boolean editProfile(ApiProfile profile) {
        return false;
    }

    // ใช้การสำหรับ sync ข้อมูลจากเซิร์ฟเวอร์ และทำการร่วมกับ DBHelper
    public boolean syncDataFromServer(String table_name) {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("Table", table_name)
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "syncData")
                .post(formBody)
                .build();

        DBHelper dbHelper = new DBHelper(mContext);
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                switch (table_name) {
                    case "amphur":
                        SyncAmphur data_amphur = gson.fromJson(response.body().string(), SyncAmphur.class);
                        dbHelper.syncAmphur(data_amphur.getData());
                        break;
                    case "casescenetype":
                        SyncCaseSceneType data_casescenetype = gson.fromJson(response.body().string(), SyncCaseSceneType.class);
                        dbHelper.syncCaseSceneType(data_casescenetype.getData());
                        break;
                    case "composition":
                        SyncComPosition data_composition = gson.fromJson(response.body().string(), SyncComPosition.class);
                        dbHelper.syncComPosition(data_composition.getData());
                        break;
                    case "district":
                        SyncDistrict data_district = gson.fromJson(response.body().string(), SyncDistrict.class);
                        dbHelper.syncDistrict(data_district.getData());
                        break;
                    case "geography":
                        SyncGeography data_geography = gson.fromJson(response.body().string(), SyncGeography.class);
                        dbHelper.syncGeography(data_geography.getData());
                        break;
                    case "inqposition":
                        SyncInqPosition data_inqposition = gson.fromJson(response.body().string(), SyncInqPosition.class);
                        dbHelper.syncInqPosition(data_inqposition.getData());
                        break;
                    case "invposition":
                        SyncInvPosition data_invposition = gson.fromJson(response.body().string(), SyncInvPosition.class);
                        dbHelper.syncInvPosition(data_invposition.getData());
                        break;
                    case "official":
                        SyncOfficial data_official = gson.fromJson(response.body().string(), SyncOfficial.class);
                        dbHelper.syncOfficial(data_official.getData());
                        break;
                    case "permission":
                        SyncPermission data_permission = gson.fromJson(response.body().string(), SyncPermission.class);
                        dbHelper.syncPermission(data_permission.getData());
                        break;
                    case "policeagency":
                        SyncPoliceAgency data_policeagency = gson.fromJson(response.body().string(), SyncPoliceAgency.class);
                        dbHelper.syncPoliceAgency(data_policeagency.getData());
                        break;
                    case "policecenter":
                        SyncPoliceCenter data_policecenter = gson.fromJson(response.body().string(), SyncPoliceCenter.class);
                        dbHelper.syncPoliceCenter(data_policecenter.getData());
                        break;
                    case "policeposition":
                        SyncPolicePosition data_policeposition = gson.fromJson(response.body().string(), SyncPolicePosition.class);
                        dbHelper.syncPolicePosition(data_policeposition.getData());
                        break;
                    case "policerank":
                        SyncPoliceRank data_policerank = gson.fromJson(response.body().string(), SyncPoliceRank.class);
                        dbHelper.syncPoliceRank(data_policerank.getData());
                        break;
                    case "policestation":
                        SyncPoliceStation data_policestation = gson.fromJson(response.body().string(), SyncPoliceStation.class);
                        dbHelper.syncPoliceStation(data_policestation.getData());
                        break;
                    case "province":
                        SyncProvince data_province = gson.fromJson(response.body().string(), SyncProvince.class);
                        dbHelper.syncProvince(data_province.getData());
                        break;
                    case "resultscenetype":
                        SyncResultSceneType data_resultscenetype = gson.fromJson(response.body().string(), SyncResultSceneType.class);
                        dbHelper.syncResultSceneType(data_resultscenetype.getData());
                        break;
                    case "scdcagency":
                        SyncSCDCAgency data_scdcagency = gson.fromJson(response.body().string(), SyncSCDCAgency.class);
                        dbHelper.syncSCDCAgency(data_scdcagency.getData());
                        break;
                    case "scdccenter":
                        SyncSCDCCenter data_scdccenter = gson.fromJson(response.body().string(), SyncSCDCCenter.class);
                        dbHelper.syncSCDCCenter(data_scdccenter.getData());
                        break;
                    case "subcasescenetype":
                        SyncSubCaseSceneType data_subcasescenetype = gson.fromJson(response.body().string(), SyncSubCaseSceneType.class);
                        dbHelper.syncSubCaseSceneType(data_subcasescenetype.getData());
                        break;
                }
                return true;
            } else {
                Log.d(TAG, "Not Success " + response.code());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in syncDataFromServer : " + e.getMessage());

            return false;
        }
    }

    public ApiStatus saveGCM(ApiGCMRequest data) {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", data.getUsername())
                .add("Password", data.getPassword())
                .add("Registration_id", data.getRegistration_id())
                .add("RegisOfficialID", data.getRegisOfficialID())
                .build();
        Log.d(TAG, "Not User " + data.getUsername());
        Log.d(TAG, "Not Pass " + data.getPassword());
        Log.d(TAG, "Not Token " + data.getRegistration_id());
        Log.d(TAG, "Not OffID " + data.getRegisOfficialID());
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "getRegistrationGCM")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(response.body().string(), ApiStatus.class);
            } else {
                Log.d(TAG, "Not Success " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in login : " + e.getMessage());

            return null;
        }
    }
}
