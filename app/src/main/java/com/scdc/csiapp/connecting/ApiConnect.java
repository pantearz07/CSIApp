package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
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
public class ApiConnect {

    public static String urlMobileIP = "http://180.183.251.32/mcsi/C_mobile/";
    private String defaultIP = "180.183.251.32/mcsi";
    private String TAG = "DEBUG-ApiConnect";
    private static String strSDCardPathName_Pic = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/Pictures/";
    private static String strSDCardPathName_Vid = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/Video/";
    private static String strSDCardPathName_Voi = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/VoiceRecorder/";
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final MediaType MEDIA_TYPE_VIDEO = MediaType.parse("video/mp4");
    private static final MediaType MEDIA_TYPE_VOICE = MediaType.parse("audio/3gp");
    private Context mContext;
    private OkHttpClient okHttpClient = new OkHttpClient();
    // เตรียมไว้ใช้เวลาจะดึงค่าจาก SQLite
    DBHelper mDbHelper;
    private PreferenceData mManager;

    public ApiConnect(Context context) {

        mContext = context;
        mManager = new PreferenceData(mContext);
        updateApiConnect();
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
            if (response.isSuccessful()) {
                Log.d(TAG, "checkConnect success");
                Gson gson = new GsonBuilder().create();
                // check form ApiStatus ถ้าค่าที่ส่งกลับมาไม่ตรงกับ apistatus ให้ เเจ้งเออเร่อ
                try {
                    return gson.fromJson(response.body().string(), ApiStatus.class);
                } catch (JsonParseException e) {
                    Log.d(TAG, "checkConnect fail format");
                    ApiStatus apiStatus = new ApiStatus();
                    apiStatus.setStatus("fail");
//                    apiStatus.getData().setAction("checkConnect");
//                    apiStatus.getData().setReason("เชื่อมต่อไม่สำเร็จ");
                    return apiStatus;
                }
            } else {
                Log.d(TAG, "checkConnect fail");
                ApiStatus apiStatus = new ApiStatus();
                apiStatus.setStatus("fail");
//                apiStatus.getData().setAction("checkConnect");
//                apiStatus.getData().setReason("เชื่อมต่อไม่สำเร็จ");
                return apiStatus;
            }
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
                // ข้อมูลจาก SQLite
                mDbHelper = new DBHelper(WelcomeActivity.mContext);
                ApiListNoticeCase apiListNoticeCaseSQLite = mDbHelper.selectApiNoticeCase(WelcomeActivity.profile.getTbOfficial().OfficialID);
                // รวมข้อมูลเข้าเป็นก้อนเดียว โดยสนใจที่ข้อมูลจาก SQLite เป็นหลัก
                int ser_size = apiListNoticeCaseServer.getData().getResult().size();
                int sql_size = apiListNoticeCaseSQLite.getData().getResult().size();
                for (int i = 0; i < ser_size; i++) {
                    ApiNoticeCase temp_ser = apiListNoticeCaseServer.getData().getResult().get(i);
                    ApiNoticeCase temp_sql;
                    boolean flag_have = false;
                    for (int j = 0; j < sql_size; j++) {
                        temp_sql = apiListNoticeCaseSQLite.getData().getResult().get(j);
                        if (temp_ser.getTbNoticeCase().NoticeCaseID.equalsIgnoreCase(temp_sql.getTbNoticeCase().NoticeCaseID)) {
                            flag_have = true;
                            break;
                        }
                    }
                    if (flag_have == false) {
                        temp_ser.setMode("online");
                        apiListNoticeCaseSQLite.getData().getResult().add(temp_ser);
                    }
                }
                return apiListNoticeCaseSQLite;
            } else {
                Log.d(TAG, "Not Success " + response.code());
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
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                // ข้อมูลจากเซิร์ฟเวอร์
                ApiListCaseScene apiListCaseSceneServer = gson.fromJson(response.body().string(), ApiListCaseScene.class);
                // ข้อมูลจาก SQLite
                mDbHelper = new DBHelper(WelcomeActivity.mContext);
                ApiListCaseScene apiListCaseSceneSQLite = mDbHelper.selectApiCaseScene(WelcomeActivity.profile.getTbOfficial().OfficialID);
                // รวมข้อมูลเข้าเป็นก้อนเดียว โดยสนใจที่ข้อมูลจาก SQLite เป็นหลัก
                int ser_size = apiListCaseSceneServer.getData().getResult().size();
                int sql_size;
                if (apiListCaseSceneSQLite.getData() == null) {
                    sql_size = 0;
                } else {
                    sql_size = apiListCaseSceneSQLite.getData().getResult().size();
                }
                for (int i = 0; i < ser_size; i++) {
                    ApiCaseScene temp_ser = apiListCaseSceneServer.getData().getResult().get(i);
                    ApiCaseScene temp_sql;
                    boolean flag_have = false;
                    for (int j = 0; j < sql_size; j++) {
                        temp_sql = apiListCaseSceneSQLite.getData().getResult().get(j);
                        if (temp_ser.getTbCaseScene().CaseReportID.equalsIgnoreCase(temp_sql.getTbCaseScene().CaseReportID)) {
                            flag_have = true;
                            break;
                        }
                    }
                    if (flag_have == false) {
                        temp_ser.setMode("online");
                        apiListCaseSceneSQLite.getData().getResult().add(temp_ser);
                    }
                }
                return apiListCaseSceneSQLite;
            } else {
                Log.d(TAG, "Not Success " + response.code());
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
        Log.d(TAG, "Not User " + WelcomeActivity.profile.getTbUsers().id_users);
        Log.d(TAG, "Not Pass " + WelcomeActivity.profile.getTbUsers().pass);
        Log.d(TAG, "Not OffID " + WelcomeActivity.profile.getTbOfficial().OfficialID);
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "updateStatusCase")
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

    //sendNewNoticeCase
    public ApiStatusResult sendNewNoticeCase(TbNoticeCase tbNoticeCase) {
        Gson gson1 = new GsonBuilder().create();
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID)
                .add("NoticeCaseID", tbNoticeCase.getNoticeCaseID())
                .add("tbNoticeCase", gson1.toJson(tbNoticeCase))
                .build();
        Log.d(TAG, "tbNoticeCase toJson " + gson1.toJson(tbNoticeCase));
        Log.d(TAG, "NoticeCaseID " + tbNoticeCase.getNoticeCaseID());
        Log.d(TAG, "Not User " + WelcomeActivity.profile.getTbUsers().id_users);
        Log.d(TAG, "Not Pass " + WelcomeActivity.profile.getTbUsers().pass);
        Log.d(TAG, "Not OffID " + WelcomeActivity.profile.getTbOfficial().OfficialID);
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "saveNewNoticeCase")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "post data" + response.body().string());
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(response.body().string(), ApiStatusResult.class);
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
                // ข้อมูลจาก SQLite
                mDbHelper = new DBHelper(WelcomeActivity.mContext);
                ApiListOfficial apiListOfficialSQLite = mDbHelper.selectApiOfficial(AccessType);
                // รวมข้อมูลเข้าเป็นก้อนเดียว โดยสนใจที่ข้อมูลจาก SQLite เป็นหลัก
                int ser_size = apiListOfficialServer.getData().getResult().size();
                int sql_size;
                if (apiListOfficialSQLite.getData() == null) {
                    sql_size = 0;
                } else {
                    sql_size = apiListOfficialSQLite.getData().getResult().size();
                }
                for (int i = 0; i < ser_size; i++) {
                    ApiOfficial temp_ser = apiListOfficialServer.getData().getResult().get(i);
                    ApiOfficial temp_sql;
                    boolean flag_have = false;
                    for (int j = 0; j < sql_size; j++) {
                        temp_sql = apiListOfficialSQLite.getData().getResult().get(j);
                        if (temp_ser.getTbOfficial().OfficialID.equalsIgnoreCase(temp_sql.getTbOfficial().OfficialID)) {
                            flag_have = true;
                            break;
                        }
                    }
                    if (flag_have == false) {
                        temp_ser.setMode("online");
                        apiListOfficialSQLite.getData().getResult().add(temp_ser);
                    }
                }
                return apiListOfficialSQLite;
            } else {
                Log.d(TAG, "Not Success " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR in listOfficial : " + e.getMessage());

            return null;
        }
    }

    public ApiStatus saveDocFile(ApiCaseScene apiCaseScene) {
        Log.d(TAG, "CaseReportID " + apiCaseScene.getTbCaseScene().getCaseReportID());
        Log.d(TAG, "Not User " + WelcomeActivity.profile.getTbUsers().id_users);
        Log.d(TAG, "Not Pass " + WelcomeActivity.profile.getTbUsers().pass);
        Log.d(TAG, "Not OffID " + WelcomeActivity.profile.getTbOfficial().OfficialID);
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
            if (response.isSuccessful()) {
//                Log.d(TAG, "post data" + response.body().string());
                Gson gson = new GsonBuilder().create();
                try {
                    ApiStatus apiStatus = new ApiStatus();
                    apiStatus = gson.fromJson(response.body().string(), ApiStatus.class);
                    Log.d(TAG, apiStatus.getData().getReason());
                    return apiStatus;
                } catch (JsonParseException e) {
                    Log.d(TAG, "checkConnect fail format");
                    ApiStatus apiStatus = new ApiStatus();
                    apiStatus.setStatus("fail");
//                    apiStatus.getData().setAction("checkConnect");
//                    apiStatus.getData().setReason("เชื่อมต่อไม่สำเร็จ");
                    return apiStatus;
                }
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

    public ApiStatusData saveCaseReport(ApiCaseScene apiCaseScene) {
        Log.d(TAG, "CaseReportID " + apiCaseScene.getTbCaseScene().getCaseReportID());
        Log.d(TAG, "Not User " + WelcomeActivity.profile.getTbUsers().id_users);
        Log.d(TAG, "Not Pass " + WelcomeActivity.profile.getTbUsers().pass);
        Log.d(TAG, "Not OffID " + WelcomeActivity.profile.getTbOfficial().OfficialID);

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
        formBuilder1.addFormDataPart("apiMultimedia", gson1.toJson(apiCaseScene.getApiMultimedia()));
        Log.d(TAG, "getApiMultimedia size" + String.valueOf(apiCaseScene.getApiMultimedia().size()));
        for (int i = 0; i < apiCaseScene.getApiMultimedia().size(); i++) {
            if (apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("photo")) {
                File filePic = new File(strSDCardPathName_Pic, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                if (filePic.exists()) {
                    formBuilder1.addFormDataPart("filepic[" + String.valueOf(i) + "]", apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath,
                            RequestBody.create(MEDIA_TYPE_JPEG, filePic));
                }
            } else if (apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("diagram")) {
                File filePic = new File(strSDCardPathName_Pic, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                if (filePic.exists()) {
                    formBuilder1.addFormDataPart("filepic[" + String.valueOf(i) + "]", apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath,
                            RequestBody.create(MEDIA_TYPE_JPEG, filePic));
                }
            } else if (apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("video")) {
                File fileVid = new File(strSDCardPathName_Vid, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                if (fileVid.exists()) {
                    formBuilder1.addFormDataPart("filevid[" + String.valueOf(i) + "]", apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath,
                            RequestBody.create(MEDIA_TYPE_VIDEO, fileVid));
                }
            } else if (apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equalsIgnoreCase("voice")) {
                File fileVoi = new File(strSDCardPathName_Voi, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                if (fileVoi.exists()) {
                    formBuilder1.addFormDataPart("filevoi[" + String.valueOf(i) + "]", apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath,
                            RequestBody.create(MEDIA_TYPE_VOICE, fileVoi));
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
            if (response.isSuccessful()) {
//                Log.d(TAG, "post data" + response.body().string());
                Gson gson = new GsonBuilder().create();
                try {
                    ApiStatusData apiStatus = new ApiStatusData();
                    apiStatus = gson.fromJson(response.body().string(), ApiStatusData.class);
                    Log.d(TAG, apiStatus.getData().getReason());
                    return apiStatus;
                } catch (JsonParseException e) {
                    Log.d(TAG, "checkConnect fail format");
                    ApiStatusData apiStatus = new ApiStatusData();
                    apiStatus.setStatus("fail");
//                    apiStatus.getData().setAction("checkConnect");
//                    apiStatus.getData().setReason("เชื่อมต่อไม่สำเร็จ");
                    return apiStatus;
                }
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
    //** ใช้อัปเดทข้อมูล User , Official ผ่าน function editProfile
    // ส่งข้อมูลด้วย
    // ApiProfile
    // Username,Password ดึงเอาจาก User ของ ApiProfile

    public ApiStatus editProfile(ApiProfile apiProfile) {
        mDbHelper = new DBHelper(WelcomeActivity.mContext);
        String txt_pass = mManager.getPreferenceData(mDbHelper.COL_pass);

        Log.d(TAG, "Not User " + apiProfile.getTbUsers().id_users);
        Log.d(TAG, "Not Pass " + txt_pass);
        Log.d(TAG, "Not OffID " + apiProfile.getTbOfficial().OfficialID);
        MultipartBody.Builder formBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Username_old", apiProfile.getTbOfficial().id_users)
                .addFormDataPart("Password_old", txt_pass);//apiProfile.getTbUsers().pass
        Gson gson1 = new GsonBuilder().create();
        formBuilder.addFormDataPart("tbOfficial", gson1.toJson(apiProfile.getTbOfficial()));
        formBuilder.addFormDataPart("tbUsers", gson1.toJson(apiProfile.getTbUsers()));

        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(urlMobileIP + "editProfile")
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
//            Log.d(TAG, "post data" + response.body().string());
            if (response.isSuccessful()) {

                Gson gson = new GsonBuilder().create();
                try {
                    ApiStatus apiStatus = new ApiStatus();
                    apiStatus = gson.fromJson(response.body().string(), ApiStatus.class);
                    Log.d(TAG, "editProfile " + apiStatus.getData().getReason());
                    return apiStatus;
                } catch (JsonParseException e) {
                    Log.d(TAG, "checkConnect fail format");
                    ApiStatus apiStatus = new ApiStatus();
                    apiStatus.setStatus("fail");
//                    apiStatus.getData().setAction("checkConnect");
//                    apiStatus.getData().setReason("เชื่อมต่อไม่สำเร็จ");
                    return apiStatus;
                }
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
                int ser_size = apiListScheduleInvestigatesServer.getData().getResult().size();
//                for (int i = 0; i < ser_size; i++) {
//                    ApiScheduleInvestigates temp_ser = apiListScheduleInvestigatesServer.getData().getResult().get(i);
//                    temp_ser.setMode("online");
//                    apiListScheduleInvestigatesServer.getData().getResult().add(temp_ser);
//                }
// String x =gson.toJson(apiListScheduleInvestigatesServer);
//                Log.d(TAG, "toJson " + x);
                // ข้อมูลจาก SQLite
//                mDbHelper = new DBHelper(WelcomeActivity.mContext);
//                ApiListScheduleInvestigates apiListScheduleInvestigatesSQLite = mDbHelper.selectApiScheduleInvestigates(WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode);
//                // รวมข้อมูลเข้าเป็นก้อนเดียว โดยสนใจที่ข้อมูลจาก SQLite เป็นหลัก
//                int ser_size = apiListScheduleInvestigatesServer.getData().getResult().size();
//                int sql_size;
//                if (apiListScheduleInvestigatesSQLite.getData() == null) {
//                    sql_size = 0;
//                } else {
//                    sql_size = apiListScheduleInvestigatesSQLite.getData().getResult().size();
//                }
                for (int i = 0; i < ser_size; i++) {
                    String x = apiListScheduleInvestigatesServer.getData().getResult().get(i).getTbScheduleInvestigates().ScheduleInvestigateID;
                    Log.d(TAG, "ScheduleInvestigateID :" + x);
                    String w =gson.toJson(apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup());
                    Log.d(TAG, "toJson " + w);
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
//                for (int i = 0; i < ser_size; i++) {
//                    ApiScheduleInvestigates temp_ser = apiListScheduleInvestigatesServer.getData().getResult().get(i);
////                    String c = String.valueOf(apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().get(0).getApiScheduleInvInGroup().size());
////                    Log.d(TAG, "getApiScheduleInvInGroup " + c);
//
//                    ApiScheduleInvestigates temp_sql;
//                    boolean flag_have = false;
//                    for (int j = 0; j < sql_size; j++) {
//                        temp_sql = apiListScheduleInvestigatesSQLite.getData().getResult().get(j);
//                        if (temp_ser.getTbScheduleInvestigates().ScheduleInvestigateID.equalsIgnoreCase(temp_sql.getTbScheduleInvestigates().ScheduleInvestigateID)) {
////                            flag_have = true;
////                            break;
//                            Log.d(TAG, "true ScheduleInvestigateID :" + temp_sql.getTbScheduleInvestigates().ScheduleInvestigateID);
//                            for (int k = 0; k < apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().size(); k++) {
//                                ApiScheduleGroup temp_ser2 = apiListScheduleInvestigatesServer.getData().getResult().get(i).getApiScheduleGroup().get(k);
//                                ApiScheduleGroup temp_sql2;
//                                for (int l = 0; l < apiListScheduleInvestigatesSQLite.getData().getResult().get(j).getApiScheduleGroup().size(); l++) {
//                                    temp_sql2 = apiListScheduleInvestigatesSQLite.getData().getResult().get(j).getApiScheduleGroup().get(l);
//                                    if (temp_ser2.getTbScheduleGroup().ScheduleGroupID.equalsIgnoreCase(temp_sql2.getTbScheduleGroup().ScheduleGroupID)) {
//                                        Log.d(TAG, "true ScheduleGroupID :" + temp_sql2.getTbScheduleGroup().ScheduleGroupID);
//                                        for (int m = 0; m < temp_ser2.getApiScheduleInvInGroup().size(); m++) {
//                                            ApiScheduleInvInGroup temp_ser3 = temp_ser2.getApiScheduleInvInGroup().get(m);
//                                            ApiScheduleInvInGroup temp_sql3;
//                                            for (int n = 0; n < temp_sql2.getApiScheduleInvInGroup().size(); n++) {
//                                                temp_sql3 = temp_ser2.getApiScheduleInvInGroup().get(n);
//                                                if (temp_ser3.getTbScheduleInvInGroup().InvOfficialID.equalsIgnoreCase(temp_sql3.getTbScheduleInvInGroup().InvOfficialID)) {
//                                                    Log.d(TAG, "true InvOfficialID :" + temp_sql3.getTbScheduleInvInGroup().InvOfficialID);
//                                                    break;
//                                                } else {
//                                                    Log.d(TAG, "false InvOfficialID :" + temp_ser3.getTbScheduleInvInGroup().InvOfficialID);
//                                                    temp_ser3.setMode3("online");
//                                                    apiListScheduleInvestigatesSQLite.getData().getResult().get(j).getApiScheduleGroup().get(l).getApiScheduleInvInGroup().add(temp_ser3);
//
//                                                }
//                                            }
//                                        }
//                                    } else {
//                                        Log.d(TAG, "false ScheduleGroupID :" + temp_ser2.getTbScheduleGroup().ScheduleGroupID);
//                                        temp_ser2.setMode2("online");
//                                        apiListScheduleInvestigatesSQLite.getData().getResult().get(j).getApiScheduleGroup().add(temp_ser2);
//                                    }
//                                }
//
//                            }
//                        } else {
//                            // กรณีที่ไม่มี ScheduleInvestigateID ตารางเวรวันที่เท่ากัน
////                            flag_have = false;
////                            if (flag_have == false) {
//                            Log.d(TAG, "false ScheduleInvestigateID :" + temp_ser.getTbScheduleInvestigates().ScheduleInvestigateID);
//                            temp_ser.setMode("online");
//                            apiListScheduleInvestigatesSQLite.getData().getResult().add(temp_ser);
////                            }
//                        }
//                    }
//
//                }
                return apiListScheduleInvestigatesServer;
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
}
