package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiLoginStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scdc.csiapp.main.WelcomeActivity;

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


    public static String urlMobileIP ="http://192.168.0.89/mCSI/C_mobile/";
   // private String URL = "http://192.168.0.89/mCSI/C_mobile/";
    private String TAG = "DEBUG";
    private OkHttpClient okHttpClient = new OkHttpClient();
    public ApiConnect() {


    }
    public static void updateIP(){
        Context context = WelcomeActivity.mContext;
        SharedPreferences sp = context.getSharedPreferences(PreferenceData.PREF_IP,context.MODE_PRIVATE);

        String IP = sp.getString(PreferenceData.KEY_IP,"192.168.0.89");
        urlMobileIP = "http://"+IP+"/mCSI/C_mobile/";

        Log.d("urlMobileIP", urlMobileIP);

    }
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
}
