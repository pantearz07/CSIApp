package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiGCMRequest;
import com.scdc.csiapp.apimodel.ApiListCaseScene;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.apimodel.ApiListOfficial;
import com.scdc.csiapp.apimodel.ApiListScheduleInvestigates;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiLoginStatus;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.apimodel.ApiOfficial;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.apimodel.ApiStatusData;
import com.scdc.csiapp.apimodel.ApiStatusResult;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.syncmodel.SyncAmphur;
import com.scdc.csiapp.syncmodel.SyncCaseSceneType;
import com.scdc.csiapp.syncmodel.SyncComPosition;
import com.scdc.csiapp.syncmodel.SyncDistrict;
import com.scdc.csiapp.syncmodel.SyncEvidenceType;
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
import com.scdc.csiapp.syncmodel.SyncResultSceneType;
import com.scdc.csiapp.syncmodel.SyncSCDCagency;
import com.scdc.csiapp.syncmodel.SyncSCDCcenter;
import com.scdc.csiapp.syncmodel.SyncSubCaseSceneType;
import com.scdc.csiapp.tablemodel.TbNoticeCase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Pantearz07 on 12/9/2559.
 */
public class ApiConnect implements Parcelable {

    public static String urlMobileIP = "http://180.183.251.32/mcsi/C_mobile/";
    private String defaultIP = "180.183.251.32/mcsi";
    private String TAG = "DEBUG-ApiConnect";
    private static String strSDCardPathName_temp = "/CSIFiles/temp/";
    private static String strSDCardPathName = "/CSIFiles/";
    private static final MediaType MEDIA_TYPE_IMG = MediaType.parse("image/*");
    private static final MediaType MEDIA_TYPE_VIDEO = MediaType.parse("video/*");
    private static final MediaType MEDIA_TYPE_VOICE = MediaType.parse("audio/*");
    private Context mContext;
    GetDateTime getDateTime;
    private OkHttpClient okHttpClient = new OkHttpClient();
    // เตรียมไว้ใช้เวลาจะดึงค่าจาก SQLite
    DBHelper mDbHelper;
    private PreferenceData mManager;

    public ApiConnect(Context context) {

        mContext = context;
        mManager = new PreferenceData(mContext);
        updateApiConnect();
        getDateTime = new GetDateTime();
    }

    // ใช้ดึงค่า IP ปัจจุบันที่เชื่อมต่ออยู่ตอนนั้น
    public String getDefaultIP() {
        return defaultIP;
    }

    // ตรวจสอบการเชื่อมต่อกับระบบผ่าน function checkConnect
    public ApiStatus checkConnect() {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "checkConnect")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ApiStatus apiStatus = new ApiStatus();

            if (response.isSuccessful()) {
                Log.d(TAG, "checkConnect success");
                Gson gson = new GsonBuilder().create();
                // check form ApiStatus ถ้าค่าที่ส่งกลับมาไม่ตรงกับ apistatus ให้ เเจ้งเออเร่อ
                try {
                    return gson.fromJson(response.body().string(), ApiStatus.class);
                } catch (JsonParseException e) {
                    Log.d(TAG, "checkConnect fail format");
                    apiStatus.setStatus("fail");
                }
            } else {
                Log.d(TAG, "checkConnect fail");
                apiStatus.setStatus("fail");
            }
            response.close();
            return apiStatus;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in checkConnect : " + e.getMessage());
            ApiStatus apiStatus = new ApiStatus();
            apiStatus.setStatus("fail");
            return apiStatus;
        }
//        return null;
    }

    // ส่วนการทำงาน Update ผ่านในคลาส เพื่อดึงค่า IP ล่าสุด และอัพเดทลิงค์การเชื่อม
    private void updateApiConnect() {
        SharedPreferences sp = mContext.getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        urlMobileIP = "http://" + defaultIP + "/C_mobile/";
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
            ApiLoginStatus apiLoginStatus = new ApiLoginStatus();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                try {
                    return gson.fromJson(response.body().string(), ApiLoginStatus.class);
                } catch (JsonSyntaxException e) {
                    Log.d(TAG, "login fail format");
                    apiLoginStatus.setStatus("fail");
                }
            } else {
                Log.d(TAG, "Not Success - code in login : response error");
                apiLoginStatus.setStatus("fail");
            }
            response.close();
            return apiLoginStatus;
        } catch (IOException e) {
//            e.printStackTrace();
            Log.d(TAG, "ERROR in login : error");
            ApiLoginStatus apiLoginStatus = new ApiLoginStatus();
            apiLoginStatus.setStatus("fail");
            return apiLoginStatus;
        }
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
                        if (data_amphur != null) {
                            dbHelper.syncAmphur(data_amphur.getData());
                        }
                        break;
                    case "casescenetype":
                        SyncCaseSceneType data_casescenetype = gson.fromJson(response.body().string(), SyncCaseSceneType.class);
                        if (data_casescenetype != null) {
                            dbHelper.syncCaseSceneType(data_casescenetype.getData());
                        }
                        break;
                    case "composition":
                        SyncComPosition data_composition = gson.fromJson(response.body().string(), SyncComPosition.class);
                        if (data_composition != null) {
                            dbHelper.syncComPosition(data_composition.getData());
                        }
                        break;
                    case "district":
                        SyncDistrict data_district = gson.fromJson(response.body().string(), SyncDistrict.class);
                        if (data_district != null) {
                            dbHelper.syncDistrict(data_district.getData());
                        }
                        break;
                    case "geography":
                        SyncGeography data_geography = gson.fromJson(response.body().string(), SyncGeography.class);
                        if (data_geography != null) {
                            dbHelper.syncGeography(data_geography.getData());
                        }
                        break;
                    case "inqposition":
                        SyncInqPosition data_inqposition = gson.fromJson(response.body().string(), SyncInqPosition.class);
                        if (data_inqposition != null) {
                            dbHelper.syncInqPosition(data_inqposition.getData());
                        }
                        break;
                    case "invposition":
                        SyncInvPosition data_invposition = gson.fromJson(response.body().string(), SyncInvPosition.class);
                        if (data_invposition != null) {
                            dbHelper.syncInvPosition(data_invposition.getData());
                        }
                        break;
                    case "official":
                        SyncOfficial data_official = gson.fromJson(response.body().string(), SyncOfficial.class);
                        if (data_official != null) {
                            dbHelper.syncOfficial(data_official.getData());
                        }
                        break;
                    case "permission":
                        SyncPermission data_permission = gson.fromJson(response.body().string(), SyncPermission.class);
                        if (data_permission != null) {
                            dbHelper.syncPermission(data_permission.getData());
                        }
                        break;
                    case "policeagency":
                        SyncPoliceAgency data_policeagency = gson.fromJson(response.body().string(), SyncPoliceAgency.class);
                        if (data_policeagency != null) {
                            dbHelper.syncPoliceAgency(data_policeagency.getData());
                        }
                        break;
                    case "policecenter":
                        SyncPoliceCenter data_policecenter = gson.fromJson(response.body().string(), SyncPoliceCenter.class);
                        if (data_policecenter != null) {
                            dbHelper.syncPoliceCenter(data_policecenter.getData());
                        }
                        break;
                    case "policeposition":
                        SyncPolicePosition data_policeposition = gson.fromJson(response.body().string(), SyncPolicePosition.class);
                        if (data_policeposition != null) {
                            dbHelper.syncPolicePosition(data_policeposition.getData());
                        }
                        break;
                    case "policerank":
                        SyncPoliceRank data_policerank = gson.fromJson(response.body().string(), SyncPoliceRank.class);
                        if (data_policerank != null) {
                            dbHelper.syncPoliceRank(data_policerank.getData());
                        }
                        break;
                    case "policestation":
                        SyncPoliceStation data_policestation = gson.fromJson(response.body().string(), SyncPoliceStation.class);
                        if (data_policestation != null) {
                            dbHelper.syncPoliceStation(data_policestation.getData());
                        }
                        break;
                    case "province":
                        SyncProvince data_province = gson.fromJson(response.body().string(), SyncProvince.class);
                        if (data_province != null) {
                            dbHelper.syncProvince(data_province.getData());
                        }
                        break;
                    case "resultscenetype":
                        SyncResultSceneType data_resultscenetype = gson.fromJson(response.body().string(), SyncResultSceneType.class);
                        if (data_resultscenetype != null) {
                            dbHelper.syncResultSceneType(data_resultscenetype.getData());
                        }
                        break;
                    case "scdcagency":
                        SyncSCDCagency data_scdcagency = gson.fromJson(response.body().string(), SyncSCDCagency.class);
                        if (data_scdcagency != null) {
                            dbHelper.syncSCDCAgency(data_scdcagency.getData());
                        }
                        break;
                    case "scdccenter":
                        SyncSCDCcenter data_scdccenter = gson.fromJson(response.body().string(), SyncSCDCcenter.class);
                        if (data_scdccenter != null) {
                            dbHelper.syncSCDCCenter(data_scdccenter.getData());
                        }
                        break;
                    case "subcasescenetype":
                        SyncSubCaseSceneType data_subcasescenetype = gson.fromJson(response.body().string(), SyncSubCaseSceneType.class);
                        if (data_subcasescenetype != null) {
                            dbHelper.syncSubCaseSceneType(data_subcasescenetype.getData());
                        }
                        break;
                    case "evidencetype":
                        SyncEvidenceType data_evidencetype = gson.fromJson(response.body().string(), SyncEvidenceType.class);
                        if (data_evidencetype != null) {
                            dbHelper.syncEvidenceType(data_evidencetype.getData());
                        }
                        break;
                }
                response.close();
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

    public ApiListNoticeCase listNoticecase() {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID)
                .build();
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "listNoticecase")
                .post(formBody)
                .build();
        try {

            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "post data " + response.body().string());
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                // ข้อมูลจากเซิร์ฟเวอร์
                ApiListNoticeCase apiListNoticeCaseServer = gson.fromJson(response.body().string(), ApiListNoticeCase.class);
                if (apiListNoticeCaseServer.getStatus().equals("fail")) {
                    Log.d(TAG, "post data " + apiListNoticeCaseServer.getData().getReason());
                    response.close();
                    return null;
                } else {
                    // ข้อมูลจาก SQLite
                    mDbHelper = new DBHelper(mContext);
                    // รวมข้อมูลเข้าเป็นก้อนเดียว โดยสนใจที่ข้อมูลจาก SQLite เป็นหลัก
                    int ser_size = apiListNoticeCaseServer.getData().getResult().size();
//                int sql_size = apiListNoticeCaseSQLite.getData().getResult().size();
                    List<ApiNoticeCase> newListNoticeCase = new ArrayList<>();
                    int sql_size;
                    ApiListNoticeCase apiListNoticeCaseSQLite = mDbHelper.selectApiNoticeCase(WelcomeActivity.profile.getTbOfficial().OfficialID);
                    if (apiListNoticeCaseSQLite.getData() == null) {
                        sql_size = 0;
                    } else {
                        sql_size = apiListNoticeCaseSQLite.getData().getResult().size();
                    }
                    Log.i(TAG, "sql_size: " + sql_size + " ser_size: " + ser_size);
                    for (int i = 0; i < ser_size; i++) {
                        ApiNoticeCase temp_ser = apiListNoticeCaseServer.getData().getResult().get(i);
                        ApiNoticeCase temp_sql;
                        boolean flag_have = false;
                        for (int j = 0; j < sql_size; j++) {
                            temp_sql = apiListNoticeCaseSQLite.getData().getResult().get(j);
                            if (temp_ser.getTbNoticeCase().NoticeCaseID.equalsIgnoreCase(temp_sql.getTbNoticeCase().NoticeCaseID)) {
                                flag_have = true;
                                if (temp_ser.getTbNoticeCase().LastUpdateDate != null && temp_sql.getTbNoticeCase().LastUpdateDate != null
                                        && temp_ser.getTbNoticeCase().LastUpdateTime != null && temp_sql.getTbNoticeCase().LastUpdateTime != null) {
                                    int checkDateTime = 0;
                                    String LastUpdateDateTime_start = temp_ser.getTbNoticeCase().LastUpdateDate + " " + temp_ser.getTbNoticeCase().LastUpdateTime;
                                    String LastUpdateDateTime_end = temp_sql.getTbNoticeCase().LastUpdateDate + " " + temp_sql.getTbNoticeCase().LastUpdateTime;
                                    checkDateTime = getDateTime.CheckDates(LastUpdateDateTime_start, LastUpdateDateTime_end);
                                    Log.i(TAG, "CheckDates start " + LastUpdateDateTime_start + "end " + LastUpdateDateTime_end + " checkDateTime: " + String.valueOf(checkDateTime));
                                    if (checkDateTime == 1) {
                                        boolean isSuccess = mDbHelper.saveNoticeCase(temp_ser.getTbNoticeCase());
                                        if (isSuccess) {
                                            //add list in object
                                            temp_ser.getMode().equals("offline");
                                            newListNoticeCase.add(temp_ser);
                                            Log.d(TAG, "update from server to mobile saveNoticeCase ");
                                            break;
                                        }
                                    } else if (checkDateTime == 2) {
                                        Log.d(TAG, "update from mobile to server saveNoticeCase ");
                                        //sendNewNoticeCase
                                        ApiStatusResult apiStatusResult = sendNewNoticeCase(temp_sql.getTbNoticeCase());
                                        if (apiStatusResult.getStatus().equalsIgnoreCase("success")) {
                                            temp_sql.setMode("offline");
                                            newListNoticeCase.add(temp_sql);
                                            Log.d(TAG, "update from mobile to server saveNoticeCase : success");
                                        } else {
                                            Log.d(TAG, "update from mobile to server saveNoticeCase : fail");
                                        }
                                        break;
                                    } else {
                                        Log.d(TAG, "no update saveNoticeCase ");
                                        temp_sql.setMode("offline");
                                        newListNoticeCase.add(temp_sql);
                                        break;
                                    }

                                }
                            }
                        }
                        if (flag_have == false) {
                            temp_ser.setMode("online");
                            newListNoticeCase.add(temp_ser);
                        }
                    }
                    response.close();
                    apiListNoticeCaseSQLite.getData().setResult(newListNoticeCase);
                    return apiListNoticeCaseSQLite;
                }
            } else {
                Log.d(TAG, "Not Success listNoticecase" + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in listNoticecase : " + e.getMessage());

            return null;
        }
    }

    public ApiListCaseScene listCasescene() {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID)
                .build();


        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "listCasescene")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "post data" + response.body().string());
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                mDbHelper = new DBHelper(mContext);
                // ข้อมูลจากเซิร์ฟเวอร์
                ApiListCaseScene apiListCaseSceneServer = gson.fromJson(response.body().string(), ApiListCaseScene.class);
                if (apiListCaseSceneServer.getStatus().equals("fail")) {
                    Log.d(TAG, "post data " + apiListCaseSceneServer.getData().getReason());
                    response.close();
                    return null;
                } else {
                    // ข้อมูลจาก SQLite
                    //check deletecase
                    ApiListCaseScene apiListCaseSceneSQLite_old = mDbHelper.selectApiCaseScene(WelcomeActivity.profile.getTbOfficial().OfficialID);
                    int ser_size_check = apiListCaseSceneServer.getData().getResult().size();
                    Log.i(TAG, "ser_size_check " + apiListCaseSceneServer.getData().getResult().size());
                    int sql_size_old;
                    if (apiListCaseSceneSQLite_old.getData() == null) {
                        sql_size_old = 0;
                    } else {
                        sql_size_old = apiListCaseSceneSQLite_old.getData().getResult().size();
                    }
//                    for (int i = 0; i < sql_size_check; i++) {
//                        ApiCaseScene temp_sql_check = apiListCaseSceneSQLite_check.getData().getResult().get(i);
//                        ApiCaseScene temp_ser_check;
//                        for (int j = 0; j < ser_size_check; j++) {
//                            temp_ser_check = apiListCaseSceneServer.getData().getResult().get(j);
//                            if (temp_sql_check.getTbCaseScene().CaseReportID.equalsIgnoreCase(temp_ser_check.getTbCaseScene().CaseReportID)) {
//                                Log.i(TAG, "check list CaseScene มีบนserver "+temp_sql_check.getTbCaseScene().CaseReportID);
//                            } else {
//                                boolean isSuccess3 = mDbHelper.DeleteMultimedia(temp_sql_check);
//                                boolean isSuccess2 = mDbHelper.DeleteAllCaseScene(temp_sql_check.getTbCaseScene().CaseReportID);
//                                boolean isSuccess1 = mDbHelper.DeleteNoticeCase(temp_sql_check.getTbNoticeCase().Mobile_CaseID);
//                                if (isSuccess1) {
//                                    Log.i(TAG, "check list CaseScene ไม่มีบนserver ลบออกจาก sqlite "+temp_sql_check.getTbCaseScene().CaseReportID);
//                                }
//                            }
//                        }
//                    }
                    // ทำการ อัพเดทข้อมูลระหว่าง Server และ SQLite  และ ลบข้อมูลออกจาก SQLite กรณีที่ Server ไม่มีข้อมูลแล้ว
                    for (int i = 0; i < sql_size_old; i++) {
                        ApiCaseScene temp_sql_old = apiListCaseSceneSQLite_old.getData().getResult().get(i);
                        ApiCaseScene temp_ser_check;
                        boolean flag_have = false;
                        for (int j = 0; j < ser_size_check; j++) {
                            temp_ser_check = apiListCaseSceneServer.getData().getResult().get(j);
                            //เช็คว่า ข้อมูลใน SQLite มีตรงกับ server มั้ย
                            if (temp_sql_old.getTbCaseScene().CaseReportID.equalsIgnoreCase(temp_ser_check.getTbCaseScene().CaseReportID)) {
                                //มีคดีที่ตรงกัน

                                if (temp_ser_check.getTbCaseScene().LastUpdateDate != null && temp_sql_old.getTbCaseScene().LastUpdateDate != null
                                        && temp_ser_check.getTbCaseScene().LastUpdateTime != null && temp_sql_old.getTbCaseScene().LastUpdateTime != null) {
                                    //เช็ควันเวลา LastUpdate เปรียบเทียบว่าฝั่งไหนใหม่หรือเก่ากว่า
                                    int checkDateTime = 0;
                                    String LastUpdateDateTime_start = temp_ser_check.getTbCaseScene().LastUpdateDate + " " + temp_ser_check.getTbCaseScene().LastUpdateTime;
                                    String LastUpdateDateTime_end = temp_sql_old.getTbCaseScene().LastUpdateDate + " " + temp_sql_old.getTbCaseScene().LastUpdateTime;
                                    checkDateTime = getDateTime.CheckDates(LastUpdateDateTime_start, LastUpdateDateTime_end);
                                    Log.i(TAG, temp_ser_check.getTbCaseScene().CaseReportID + "CheckDates start ser:  " + LastUpdateDateTime_start + "- end sql: " + LastUpdateDateTime_end + " checkDateTime: " + String.valueOf(checkDateTime));
                                    // -end- เช็ควันเวลา LastUpdate เปรียบเทียบว่าฝั่งไหนใหม่หรือเก่ากว่า
                                    if (checkDateTime == 1) {
                                        // ถ้า วันที่ฝั่ง server ใหม่กว่า Sqlite
                                        flag_have = true;
                                        if (mDbHelper.DeleteMultimedia(temp_sql_old)) {
                                            boolean isSuccess = mDbHelper.updateAlldataCase(temp_ser_check);
                                            if (isSuccess) {
                                                Log.d(TAG, "update from server to mobile updateAlldataCase ");
                                                break;
                                            }
                                        } else {
                                            Log.d(TAG, "DeleteMultimedia in Case error");
                                            break;
                                        }
                                        // -end- ถ้า วันที่ฝั่ง server ใหม่กว่า Sqlite
                                    } else if (checkDateTime == 2) {
                                        // ถ้า วันที่ฝั่ง server เก่ากว่า Sqlite
                                        flag_have = true;
                                        Log.d(TAG, "update from mobile to server updateAlldataCase ");
                                        //อัพเดทข้อมูลจาก SQLite ไปยัง server
                                        ApiStatusData apiStatusData = saveCaseReport(temp_sql_old);
                                        if (apiStatusData.getStatus().equalsIgnoreCase("success")) {
                                            Log.d(TAG, "update from mobile to server saveCaseReport : success");
                                        } else {
                                            Log.d(TAG, "update from mobile to server saveCaseReport : fail");
                                        }
                                        break;
                                        // -end- ถ้า วันที่ฝั่ง server เก่ากว่า Sqlite
                                    } else {
                                        // ถ้า วันที่ฝั่ง server เท่ากันกับ Sqlite
                                        flag_have = true;
                                        Log.d(TAG, "no update updateAlldataCase");
                                        break;
                                    }
                                }
                            }
                        }
                        //ตรวจ flag_have หลังจากวนทั้งหมดแล้ว ถึงจะลบได้
                        if (flag_have == false) {
                            //คดีที่อยู่ในมือถือ ไม่มีบน server  เลยต้องลบออกจาก sqlite
                            //Delete Data in SQLite
                            long checkcase = mDbHelper.CheckCaseScene(temp_sql_old.getTbCaseScene().CaseReportID);
                            if (checkcase == 1) {
                                boolean isSuccess3 = mDbHelper.DeleteMultimedia(temp_sql_old);
                                boolean isSuccess2 = mDbHelper.DeleteAllCaseScene(temp_sql_old.getTbCaseScene().CaseReportID);
                                boolean isSuccess1 = mDbHelper.DeleteNoticeCase(temp_sql_old.getTbNoticeCase().Mobile_CaseID);
                                if (isSuccess1) {
                                    Log.i(TAG, "check list CaseScene ไม่มีบนserver ลบออกจาก sqlite " + temp_sql_old.getTbCaseScene().CaseReportID);
                                }
                            }
                        }
                        
                    }
                    // รวมข้อมูลเข้าเป็นก้อนเดียว โดยสนใจที่ข้อมูลจาก Server เป็นหลัก
                    List<ApiCaseScene> newListCaseScene = new ArrayList<>();
                    ApiListCaseScene apiListCaseSceneSQLite = mDbHelper.selectApiCaseScene(WelcomeActivity.profile.getTbOfficial().OfficialID);
                    Log.d(TAG, "apiListCaseSceneSQLite new " + String.valueOf(apiListCaseSceneSQLite.getData().getResult().size()));
//                    int ser_size = apiListCaseSceneServer.getData().getResult().size();
                    int sql_size;
                    if (apiListCaseSceneSQLite.getData() == null) {
                        sql_size = 0;
                    } else {
                        sql_size = apiListCaseSceneSQLite.getData().getResult().size();
                    }
                    for (int i = 0; i < ser_size_check; i++) {
                        ApiCaseScene temp_ser = apiListCaseSceneServer.getData().getResult().get(i);
                        ApiCaseScene temp_sql;
                        if (sql_size == 0) {
                            Log.i(TAG, "online" + temp_ser.getTbCaseScene().CaseReportID);
                            temp_ser.setMode("online");
                            newListCaseScene.add(temp_ser);
                        } else {
                            for (int j = 0; j < sql_size; j++) {
                                temp_sql = apiListCaseSceneSQLite.getData().getResult().get(j);
                                if (temp_ser.getTbCaseScene().CaseReportID.equalsIgnoreCase(temp_sql.getTbCaseScene().CaseReportID)) {
                                    Log.i(TAG, "offline" + temp_sql.getTbCaseScene().CaseReportID);
                                    temp_sql.setMode("offline");
                                    newListCaseScene.add(temp_sql);
                                    break;
                                } else {
                                    Log.i(TAG, "online" + temp_ser.getTbCaseScene().CaseReportID);
                                    temp_ser.setMode("online");
                                    newListCaseScene.add(temp_ser);
                                    break;
                                }
                            }
                        }
                    }
//                    for (int i = 0; i < ser_size; i++) {
//                        ApiCaseScene temp_ser = apiListCaseSceneServer.getData().getResult().get(i);
//                        ApiCaseScene temp_sql;
//                        boolean flag_have = false;
//                        for (int j = 0; j < sql_size; j++) {
//                            temp_sql = apiListCaseSceneSQLite.getData().getResult().get(j);
//                            if (temp_sql.getTbCaseScene().CaseReportID.equalsIgnoreCase(temp_ser.getTbCaseScene().CaseReportID)) {
//                                flag_have = true;
//                                //mobile have casescene same server
//                                if (temp_ser.getTbCaseScene().LastUpdateDate != null && temp_sql.getTbCaseScene().LastUpdateDate != null
//                                        && temp_ser.getTbCaseScene().LastUpdateTime != null && temp_sql.getTbCaseScene().LastUpdateTime != null) {
//                                    int checkDateTime = 0;
//                                    String LastUpdateDateTime_start = temp_ser.getTbCaseScene().LastUpdateDate + " " + temp_ser.getTbCaseScene().LastUpdateTime;
//                                    String LastUpdateDateTime_end = temp_sql.getTbCaseScene().LastUpdateDate + " " + temp_sql.getTbCaseScene().LastUpdateTime;
//                                    checkDateTime = getDateTime.CheckDates(LastUpdateDateTime_start, LastUpdateDateTime_end);
//                                    Log.i(TAG, "CheckDates start " + LastUpdateDateTime_start + "end " + LastUpdateDateTime_end + " checkDateTime: " + String.valueOf(checkDateTime));
//
//                                    if (checkDateTime == 1) {
//                                        if (mDbHelper.DeleteMultimedia(temp_sql)) {
//                                            boolean isSuccess = mDbHelper.updateAlldataCase(temp_ser);
//                                            if (isSuccess) {
////                                                apiListCaseSceneSQLite.getData().getResult().set(j, temp_ser);
//                                                temp_ser.setMode("offline");
//                                                newListCaseScene.add(temp_ser);
//                                                Log.d(TAG, "update from server to mobile updateAlldataCase ");
//                                                break;
//                                            }
//                                        } else {
//                                            Log.d(TAG, "DeleteMultimedia in Case error");
//                                            break;
//                                        }
//                                    } else if (checkDateTime == 2) {
//                                        Log.d(TAG, "update from mobile to server updateAlldataCase ");
//                                        temp_sql.setMode("offline");
//                                        ApiStatusData apiStatusData = saveCaseReport(temp_sql);
//                                        if (apiStatusData.getStatus().equalsIgnoreCase("success")) {
//                                            newListCaseScene.add(temp_sql);
//                                            Log.d(TAG, "update from mobile to server saveCaseReport : success");
//                                        } else {
//                                            Log.d(TAG, "update from mobile to server saveCaseReport : fail");
//                                        }
//                                        break;
//                                    } else {
//                                        temp_sql.setMode("offline");
//                                        newListCaseScene.add(temp_sql);
//                                        Log.d(TAG, "no update updateAlldataCase");
//                                        break;
//                                    }
//                                }
//                            } else {
//                                Log.d(TAG, " mobile not have Casescene " + temp_ser.getTbCaseScene().CaseReportID.toString());
//                            }
//                        }
//                        if (flag_have == false) {
//                            temp_ser.setMode("online");
////                            apiListCaseSceneSQLite.getData().getResult().add(temp_ser);
//                            newListCaseScene.add(temp_ser);
//                        }
//                    }
                    response.close();
                    Log.d(TAG, "newListCaseScene " + String.valueOf(newListCaseScene.size()));
                    apiListCaseSceneSQLite.getData().setResult(newListCaseScene);
                    return apiListCaseSceneSQLite;
                }
            } else {
                Log.d(TAG, "Not Success listCasescene" + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in listCasescene : " + e.getMessage());

            return null;
        }
    }

    public ApiStatus saveGCM(ApiGCMRequest data) {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", data.getUsername())
                .add("Password", data.getPassword())
                .add("Registration_id", data.getRegistration_id())
                .add("RegisOfficialID", data.getRegisOfficialID())
                .build();
//        Log.d(TAG, "Not User " + data.getUsername());
//        Log.d(TAG, "Not Pass " + data.getPassword());
//        Log.d(TAG, "Not Token " + data.getRegistration_id());
//        Log.d(TAG, "Not OffID " + data.getRegisOfficialID());
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "getRegistrationGCM")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ApiStatus apiStatus = new ApiStatus();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                try {
                    return gson.fromJson(response.body().string(), ApiStatus.class);
                } catch (JsonSyntaxException e) {
                    Log.d(TAG, "getRegistrationGCM fail format");
                    apiStatus.setStatus("fail");
                }
            } else {
                Log.d(TAG, "Not Success getRegistrationGCM" + response.code());
                apiStatus.setStatus("fail");
            }
            response.close();
            return apiStatus;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in getRegistrationGCM : " + e.getMessage());
            ApiStatus apiStatus = new ApiStatus();
            apiStatus.setStatus("fail");
            return apiStatus;
        }
    }

    public ApiStatus updateStatusCase(ApiCaseScene apiCaseScene) {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID)
                .add("NoticeCaseID", apiCaseScene.getTbNoticeCase().getNoticeCaseID())
                .add("CaseStatus", apiCaseScene.getTbNoticeCase().getCaseStatus())
                .add("SceneNoticeDate", apiCaseScene.getTbNoticeCase().getSceneNoticeDate())
                .add("SceneNoticeTime", apiCaseScene.getTbNoticeCase().getSceneNoticeTime())
                .add("LastUpdateDate", apiCaseScene.getTbNoticeCase().getLastUpdateDate())
                .add("LastUpdateTime", apiCaseScene.getTbNoticeCase().getLastUpdateTime())
                .add("CaseReportID", apiCaseScene.getTbCaseScene().getCaseReportID())
                .add("ReportStatus", apiCaseScene.getTbCaseScene().getReportStatus())
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "updateStatusCase")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ApiStatus apiStatus = new ApiStatus();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                try {
                    return gson.fromJson(response.body().string(), ApiStatus.class);
                } catch (JsonSyntaxException e) {
                    Log.d(TAG, "updateStatusCase fail format");
                    apiStatus.setStatus("fail");
                }
            } else {
                Log.d(TAG, "Not Success updateStatusCase" + response.code());
                apiStatus.setStatus("fail");
            }
            response.close();
            return apiStatus;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in updateStatusCase : " + e.getMessage());
            ApiStatus apiStatus = new ApiStatus();
            apiStatus.setStatus("fail");
            return apiStatus;
        }
    }

    //sendNewNoticeCase
    public ApiStatusResult sendNewNoticeCase(TbNoticeCase tbNoticeCase) {
        Gson gson1 = new GsonBuilder().create();
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID)
                .add("tbNoticeCase", gson1.toJson(tbNoticeCase))
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "saveNewNoticeCase")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "post data" + response.body().string());
            ApiStatusResult apiStatusResult = new ApiStatusResult();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                try {
                    return gson.fromJson(response.body().string(), ApiStatusResult.class);
                } catch (JsonParseException e) {
                    Log.d(TAG, "saveNewNoticeCase fail format");
                    apiStatusResult.setStatus("fail");
                }

            } else {
                Log.d(TAG, "Not Success saveNewNoticeCase" + response.code());
                apiStatusResult.setStatus("fail");
            }
            response.close();
            return apiStatusResult;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in saveNewNoticeCase : " + e.getMessage());
            ApiStatusResult apiStatusResult = new ApiStatusResult();
            apiStatusResult.setStatus("fail");
            return apiStatusResult;
        }
    }

    public ApiListOfficial listOfficial(String AccessType) {
        RequestBody formBody = new FormBody.Builder()
                .add("AccessType", AccessType)
                .build();


        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "listOfficial")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                // ข้อมูลจากเซิร์ฟเวอร์
                ApiListOfficial apiListOfficialServer = gson.fromJson(response.body().string(), ApiListOfficial.class);

                mDbHelper = new DBHelper(mContext);
                int ser_size = apiListOfficialServer.getData().getResult().size();
                for (int i = 0; i < ser_size; i++) {
                    ApiOfficial temp_ser = apiListOfficialServer.getData().getResult().get(i);
                    // update หรือ add รายชื่อ จาก server
                    mDbHelper.saveOfficial(temp_ser.getTbOfficial());
                }
                ApiListOfficial apiListOfficialSQLite = mDbHelper.selectApiOfficial(AccessType);
                response.close();
                return apiListOfficialSQLite;
            } else {
                Log.d(TAG, "Not Success listOfficial" + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in listOfficial : " + e.getMessage());

            return null;
        }
    }

    public ApiStatusResult saveDocFile(ApiCaseScene apiCaseScene) {

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("CaseReportID", apiCaseScene.getTbCaseScene().CaseReportID);

        RequestBody formBody = formBuilder.build();
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "downloadDocFile")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "post data" + response.body().string());
            ApiStatusResult apiStatusResult = new ApiStatusResult();

            if (response.isSuccessful()) {
//                Log.d(TAG, "post data" + response.body().string());
                Gson gson = new GsonBuilder().create();
                try {
                    apiStatusResult = gson.fromJson(response.body().string(), ApiStatusResult.class);
                    Log.d(TAG, "downloadDocFile" + apiStatusResult.getData().getReason());
                    Log.d(TAG, "downloadDocFile" + apiStatusResult.getData().getResult());
                } catch (JsonParseException e) {
                    Log.d(TAG, "downloadDocFile fail format");
                    apiStatusResult.setStatus("fail");
                }

            } else {
                Log.d(TAG, "Not Success downloadDocFile" + response.code());
                apiStatusResult.setStatus("fail");
            }
            response.close();
            return apiStatusResult;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in downloadDocFile : " + e.getMessage());
            ApiStatusResult apiStatusResult = new ApiStatusResult();
            apiStatusResult.setStatus("fail");
            return apiStatusResult;
        }
    }

    public ApiStatusData saveCaseReport(ApiCaseScene apiCaseScene) {
        MultipartBody.Builder formBuilder1 = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .addFormDataPart("Password", WelcomeActivity.profile.getTbUsers().pass)
                .addFormDataPart("CaseReportID", apiCaseScene.getTbCaseScene().CaseReportID)
                .addFormDataPart("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID);
        Gson gson1 = new GsonBuilder().create();
        formBuilder1.addFormDataPart("tbCaseScene", gson1.toJson(apiCaseScene.getTbCaseScene()));
        formBuilder1.addFormDataPart("tbNoticeCase", gson1.toJson(apiCaseScene.getTbNoticeCase()));
        formBuilder1.addFormDataPart("tbSceneInvestigations", gson1.toJson(apiCaseScene.getTbSceneInvestigations()));
        formBuilder1.addFormDataPart("tbSceneFeatureOutside", gson1.toJson(apiCaseScene.getTbSceneFeatureOutside()));
        formBuilder1.addFormDataPart("tbSceneFeatureInSide", gson1.toJson(apiCaseScene.getTbSceneFeatureInSide()));
        formBuilder1.addFormDataPart("tbFindEvidences", gson1.toJson(apiCaseScene.getTbFindEvidences()));
        formBuilder1.addFormDataPart("tbResultScenes", gson1.toJson(apiCaseScene.getTbResultScenes()));
        formBuilder1.addFormDataPart("tbGatewayCriminals", gson1.toJson(apiCaseScene.getTbGatewayCriminals()));
        formBuilder1.addFormDataPart("tbClueShowns", gson1.toJson(apiCaseScene.getTbClueShowns()));
        formBuilder1.addFormDataPart("tbPropertyLosses", gson1.toJson(apiCaseScene.getTbPropertyLosses()));
        formBuilder1.addFormDataPart("apiInvestigatorsInScenes", gson1.toJson(apiCaseScene.getApiInvestigatorsInScenes()));
        formBuilder1.addFormDataPart("apiMultimedia", gson1.toJson(apiCaseScene.getApiMultimedia()));
//        Log.d(TAG, "getApiMultimedia size" + String.valueOf(apiCaseScene.getApiMultimedia().size()));
//        Log.d(TAG, "apiMultimedia" + gson1.toJson(apiCaseScene.getApiMultimedia()));
        if (apiCaseScene.getApiMultimedia() != null) {
            for (int i = 0; i < apiCaseScene.getApiMultimedia().size(); i++) {

                if (apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("photo") ||
                        apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("diagram")) {
                    File filePic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strSDCardPathName + apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                    if (filePic.exists()) {
                        formBuilder1.addFormDataPart("filepic[" + String.valueOf(i) + "]", apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath,
                                RequestBody.create(MEDIA_TYPE_IMG, filePic));
                    }
                }
                if (apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("video")) {
                    File fileVid = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strSDCardPathName + apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                    if (fileVid.exists()) {
                        formBuilder1.addFormDataPart("filevid[" + String.valueOf(i) + "]", apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath,
                                RequestBody.create(MEDIA_TYPE_VIDEO, fileVid));
                    }
                }
                if (apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("voice")) {
                    File fileVoi = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strSDCardPathName + apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                    if (fileVoi.exists()) {
                        formBuilder1.addFormDataPart("filevoi[" + String.valueOf(i) + "]", apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath,
                                RequestBody.create(MEDIA_TYPE_VOICE, fileVoi));
                    }
                }
            }
        }
        RequestBody formBody1 = formBuilder1.build();

        Request request1 = new Request.Builder()
                .url(urlMobileIP + "saveCaseReport")
                .post(formBody1)
                .build();

        try {
            Response response = okHttpClient.newCall(request1).execute();
            ApiStatusData apiStatus = new ApiStatusData();
//            Log.d(TAG, "post data" + response.body().string());
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                try {
                    apiStatus = gson.fromJson(response.body().string(), ApiStatusData.class);
                    Log.d(TAG, apiStatus.getData().getReason());
                    return apiStatus;
                } catch (JsonParseException e) {
                    Log.d(TAG, "saveCaseReport fail format");
                    apiStatus.setStatus("fail");
                }

            } else {
                Log.d(TAG, "saveCaseReport Not Success " + response.code());
                apiStatus.setStatus("fail");
            }
            response.close();
            return apiStatus;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in saveCaseReport : " + e.getMessage());
            ApiStatusData apiStatus = new ApiStatusData();
            apiStatus.setStatus("fail");
            return apiStatus;
        }
    }
    //** ใช้อัปเดทข้อมูล User , Official ผ่าน function editProfile
    // ส่งข้อมูลด้วย
    // ApiProfile
    // Username,Password ดึงเอาจาก User ของ ApiProfile

    public ApiStatusResult editProfile(ApiProfile apiProfile) {
        mDbHelper = new DBHelper(mContext);
        String txt_Username_old = mManager.getPreferenceData(mDbHelper.COL_id_users);
        String txt_pass = mManager.getPreferenceData(mDbHelper.COL_pass);

        MultipartBody.Builder formBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Username_old", txt_Username_old)
                .addFormDataPart("Password_old", txt_pass);//apiProfile.getTbUsers().pass
        Gson gson1 = new GsonBuilder().create();
        formBuilder.addFormDataPart("tbOfficial", gson1.toJson(apiProfile.getTbOfficial()));
        formBuilder.addFormDataPart("tbUsers", gson1.toJson(apiProfile.getTbUsers()));
        if (apiProfile.getTbOfficial().OfficialDisplayPic == null || apiProfile.getTbOfficial().OfficialDisplayPic.equals("")) {
            Log.d(TAG, "editProfile check OfficialDisplayPic null");
//            formBuilder.addFormDataPart("filedisplay", null);
        } else {
            Log.d(TAG, "editProfile check OfficialDisplayPic not null");
//            final String MEDIA_TYPE = MimeTypeMap.getFileExtensionFromUrl(strSDCardPathName_temp + apiProfile.getTbOfficial().OfficialDisplayPic);
            File filePic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    strSDCardPathName_temp + apiProfile.getTbOfficial().OfficialDisplayPic);
            Log.d(TAG, " filename: " + strSDCardPathName_temp + apiProfile.getTbOfficial().OfficialDisplayPic);
            if (filePic.exists()) {
//                Log.d(TAG, "MEDIA_TYPE " + MEDIA_TYPE + " filename: " + apiProfile.getTbOfficial().OfficialDisplayPic);
                formBuilder.addFormDataPart("filedisplay", apiProfile.getTbOfficial().OfficialDisplayPic,
//                        RequestBody.create(MediaType.parse("image/"+MEDIA_TYPE), filePic));
                        RequestBody.create(MEDIA_TYPE_IMG, filePic));
            }
        }
        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(urlMobileIP + "editProfile")
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            ApiStatusResult apiStatusResult = new ApiStatusResult();
//            Log.d(TAG, "post data" + response.body().string());

            if (response.isSuccessful()) {

                Gson gson = new GsonBuilder().create();
                try {
                    apiStatusResult = gson.fromJson(response.body().string(), ApiStatusResult.class);
                    Log.d(TAG, "editProfile Reason " + apiStatusResult.getData().getReason());
                    Log.d(TAG, "editProfile Result " + apiStatusResult.getData().getResult());
                    return apiStatusResult;
                } catch (JsonParseException e) {
                    Log.d(TAG, "editProfile fail format");
                    apiStatusResult.setStatus("fail");
                }

            } else {
                Log.d(TAG, "Not Success editProfile" + response.code());
                apiStatusResult.setStatus("fail");
            }
            response.close();
            return apiStatusResult;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in editProfile : " + e.getMessage());
            ApiStatusResult apiStatusResult = new ApiStatusResult();
            apiStatusResult.setStatus("fail");
            return apiStatusResult;
        }
    }

    public ApiStatus checkUsername(String Username_new) {
        mDbHelper = new DBHelper(mContext);
        String Username_old = mManager.getPreferenceData(mDbHelper.COL_id_users);
        String txt_pass = mManager.getPreferenceData(mDbHelper.COL_pass);
        Log.d(TAG, "txt_pass: " + txt_pass);
        MultipartBody.Builder formBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Username_old", Username_old)
                .addFormDataPart("Username_new", Username_new)
                .addFormDataPart("Password_old", txt_pass);//apiProfile.getTbUsers().pass

        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(urlMobileIP + "checkUsername")
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            ApiStatus apiStatus = new ApiStatus();
//            Log.d(TAG, "post data" + response.body().string());

            if (response.isSuccessful()) {

                Gson gson = new GsonBuilder().create();
                try {
                    apiStatus = gson.fromJson(response.body().string(), ApiStatus.class);
                    Log.d(TAG, "checkUsername Reason " + apiStatus.getData().getReason());
                    return apiStatus;
                } catch (JsonParseException e) {
                    Log.d(TAG, "checkUsername fail format");
                    apiStatus.setStatus("fail");
                }

            } else {
                Log.d(TAG, "Not Success checkUsername " + response.code());
                apiStatus.setStatus("fail");
            }
            response.close();
            return apiStatus;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in checkUsername : " + e.getMessage());
            ApiStatus apiStatus = new ApiStatus();
            apiStatus.setStatus("fail");
            return apiStatus;
        }
    }

    public ApiStatus deleteNoticeCase(String Mobile_CaseID) {
        mDbHelper = new DBHelper(mContext);
        String Username = mManager.getPreferenceData(mDbHelper.COL_id_users);
        String Password = mManager.getPreferenceData(mDbHelper.COL_pass);

        MultipartBody.Builder formBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Username", Username)
                .addFormDataPart("Password", Password)
                .addFormDataPart("Mobile_CaseID", Mobile_CaseID);

        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(urlMobileIP + "deleteNoticeCase")
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            ApiStatus apiStatus = new ApiStatus();
//            Log.d(TAG, "post data" + response.body().string());
            if (response.isSuccessful()) {

                Gson gson = new GsonBuilder().create();
                try {
                    apiStatus = gson.fromJson(response.body().string(), ApiStatus.class);
                    Log.d(TAG, "deleteNoticeCase Reason " + apiStatus.getData().getReason());
                    return apiStatus;
                } catch (JsonParseException e) {
                    Log.d(TAG, "deleteNoticeCase fail format");
                    apiStatus.setStatus("fail");
                }
            } else {
                Log.d(TAG, "Not Success deleteNoticeCase " + response.code());
                apiStatus.setStatus("fail");
            }
            response.close();
            return apiStatus;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in deleteNoticeCase : " + e.getMessage());
            ApiStatus apiStatus = new ApiStatus();
            apiStatus.setStatus("fail");
            return apiStatus;
        }
    }

    public ApiListScheduleInvestigates listScheduleInvestigates() {
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID)
                .add("SCDCAgencyCode", WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode)
                .build();


        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "listScheduleInvestigates")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
//                Log.d(TAG, "post data " + response.body().string());
                Gson gson = new GsonBuilder().create();
                // ข้อมูลจากเซิร์ฟเวอร์
                ApiListScheduleInvestigates apiListScheduleInvestigatesServer = gson.fromJson(response.body().string(), ApiListScheduleInvestigates.class);
                if (apiListScheduleInvestigatesServer.getStatus().equals("fail")) {
                    Log.d(TAG, "post data " + apiListScheduleInvestigatesServer.getData().getReason());
                    response.close();
                    return null;
                } else {
                    mDbHelper = new DBHelper(mContext);
                    int ser_size = apiListScheduleInvestigatesServer.getData().getResult().size();

                    for (int i = 0; i < ser_size; i++) {
                        String x = apiListScheduleInvestigatesServer.getData().getResult().get(i).getTbScheduleInvestigates().ScheduleInvestigateID;
                        Log.d(TAG, "ScheduleInvestigateID :" + x);
                        String w = gson.toJson(apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup());
//                    Log.d(TAG, "toJson " + w);
                        for (int j = 0; j < apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().size(); j++) {

                            String y = apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().get(j).getTbScheduleGroup().ScheduleGroupID;
                            Log.d(TAG, "ScheduleGroupID :" + y);
                            int sizeScheduleInvInGroup = apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().size();
                            Log.d(TAG, "sizeScheduleInvInGroup" + String.valueOf(sizeScheduleInvInGroup));
                            for (int k = 0; k < sizeScheduleInvInGroup; k++) {
                                String InvOfficialID = apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().InvOfficialID;
                                Log.d(TAG, "InvOfficialID :" + InvOfficialID);
                                String AccessType = apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbOfficial().AccessType;
                                Log.d(TAG, "AccessType :" + AccessType);
                            }
                        }
                    }
                    response.close();
                    return apiListScheduleInvestigatesServer;
                }
            } else {
                Log.d(TAG, "Not Success listScheduleInvestigates" + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in listScheduleInvestigates : " + e.getMessage());

            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected ApiConnect(Parcel in) {
    }

    public static final Parcelable.Creator<ApiConnect> CREATOR = new Parcelable.Creator<ApiConnect>() {
        @Override
        public ApiConnect createFromParcel(Parcel source) {
            return new ApiConnect(source);
        }

        @Override
        public ApiConnect[] newArray(int size) {
            return new ApiConnect[size];
        }
    };
}
