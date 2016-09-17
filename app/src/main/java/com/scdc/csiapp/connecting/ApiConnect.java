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
    public boolean editProfile(ApiProfile profile){
        return false;
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
