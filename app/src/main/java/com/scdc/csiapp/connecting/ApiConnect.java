package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiGCMRequest;
import com.scdc.csiapp.apimodel.ApiListCaseScene;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.apimodel.ApiListOfficial;
import com.scdc.csiapp.apimodel.ApiLoginRequest;
import com.scdc.csiapp.apimodel.ApiLoginStatus;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.apimodel.ApiOfficial;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Pantearz07 on 12/9/2559.
 */
public class ApiConnect {

    private String urlMobileIP = "http://180.183.251.32/mcsi/C_mobile/";
    private String defaultIP = "180.183.251.32/mcsi";
    private String TAG = "DEBUG-ApiConnect";
    private Context mContext;
    private OkHttpClient okHttpClient = new OkHttpClient();
    // เตรียมไว้ใช้เวลาจะดึงค่าจาก SQLite
    DBHelper mDbHelper;

    public ApiConnect(Context context) {

        mContext = context;
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
        RequestBody formBody = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID)
                .add("NoticeCaseID", tbNoticeCase.getNoticeCaseID())
                .add("Mobile_CaseID", tbNoticeCase.getMobile_CaseID())
                .add("InquiryOfficialID", tbNoticeCase.getInquiryOfficialID())
                .add("InvestigatorOfficialID", "")
                .add("SCDCAgencyCode", tbNoticeCase.getSCDCAgencyCode())
                .add("CaseTypeID", tbNoticeCase.getCaseTypeID())
                .add("SubCaseTypeID", tbNoticeCase.getSubCaseTypeID())
                .add("CaseStatus", tbNoticeCase.getCaseStatus())
                .add("PoliceStationID", tbNoticeCase.getPoliceStationID())
                .add("CaseTel", tbNoticeCase.getCaseTel())
                .add("ReceivingCaseDate", tbNoticeCase.getReceivingCaseDate())
                .add("ReceivingCaseTime", tbNoticeCase.getReceivingCaseTime())
                .add("HappenCaseDate", tbNoticeCase.getHappenCaseDate())
                .add("HappenCaseTime", tbNoticeCase.getHappenCaseTime())
                .add("KnowCaseDate", tbNoticeCase.getKnowCaseDate())
                .add("KnowCaseTime", tbNoticeCase.getKnowCaseTime())
                .add("SceneNoticeDate", "")
                .add("SceneNoticeTime", "")
                .add("CompleteSceneDate", "")
                .add("CompleteSceneTime", "")
                .add("LocaleName", tbNoticeCase.getLocaleName())
                .add("DISTRICT_ID", tbNoticeCase.getDISTRICT_ID())
                .add("AMPHUR_ID", tbNoticeCase.getAMPHUR_ID())
                .add("PROVINCE_ID", tbNoticeCase.getPROVINCE_ID())
                .add("Latitude", tbNoticeCase.getLatitude())
                .add("Longitude", tbNoticeCase.getLongitude())
                .add("SuffererPrename", tbNoticeCase.getSuffererPrename())
                .add("SuffererName", tbNoticeCase.getSuffererName())
                .add("SuffererStatus", tbNoticeCase.getSuffererStatus())
                .add("SuffererPhoneNum", tbNoticeCase.getSuffererPhoneNum())
                .add("CircumstanceOfCaseDetail", tbNoticeCase.getCircumstanceOfCaseDetail())
                .add("LastUpdateDate", tbNoticeCase.getLastUpdateDate())
                .add("LastUpdateTime", tbNoticeCase.getLastUpdateTime())
                .build();
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

    public ApiStatus saveCaseReport(ApiCaseScene apiCaseScene) {
        Log.d(TAG, "CaseReportID " + apiCaseScene.getTbCaseScene().getCaseReportID());
        Log.d(TAG, "Not User " + WelcomeActivity.profile.getTbUsers().id_users);
        Log.d(TAG, "Not Pass " + WelcomeActivity.profile.getTbUsers().pass);
        Log.d(TAG, "Not OffID " + WelcomeActivity.profile.getTbOfficial().OfficialID);
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("Username", WelcomeActivity.profile.getTbUsers().id_users)
                .add("Password", WelcomeActivity.profile.getTbUsers().pass)
                .add("OfficeID", WelcomeActivity.profile.getTbOfficial().OfficialID);
        //tbNoticeCase
        formBuilder.add("NoticeCaseID", apiCaseScene.getTbNoticeCase().getNoticeCaseID())
                .add("InquiryOfficialID", apiCaseScene.getTbNoticeCase().getInquiryOfficialID())
                .add("CaseStatus", apiCaseScene.getTbNoticeCase().getCaseStatus())
                .add("SceneNoticeDate", apiCaseScene.getTbNoticeCase().getSceneNoticeDate())
                .add("SceneNoticeTime", apiCaseScene.getTbNoticeCase().getSceneNoticeTime());
        if (apiCaseScene.getTbCaseScene().getMobile_CaseID() == null || apiCaseScene.getTbCaseScene().getMobile_CaseID().length() == 0) {
            formBuilder.add("Mobile_CaseID", "");
        } else {
            formBuilder.add("Mobile_CaseID", apiCaseScene.getTbCaseScene().getMobile_CaseID());

        }
        //getTbCaseScene
        formBuilder.add("CaseReportID", apiCaseScene.getTbCaseScene().getCaseReportID())
                .add("NoticeCaseID", apiCaseScene.getTbCaseScene().getNoticeCaseID())
                .add("SCDCAgencyCode", apiCaseScene.getTbCaseScene().getSCDCAgencyCode())
                .add("InvestigatorOfficialID", apiCaseScene.getTbCaseScene().getInvestigatorOfficialID())
                .add("CaseTypeID", apiCaseScene.getTbCaseScene().getCaseTypeID())
                .add("SubCaseTypeID", apiCaseScene.getTbCaseScene().getSubCaseTypeID())
                .add("ReportNo", apiCaseScene.getTbCaseScene().getReportNo())
                .add("ReportStatus", apiCaseScene.getTbCaseScene().getReportStatus())
                .add("PoliceStationID", apiCaseScene.getTbCaseScene().getPoliceStationID())
                .add("CaseTel", apiCaseScene.getTbCaseScene().getCaseTel())
                .add("AssignmentDate", apiCaseScene.getTbCaseScene().getAssignmentDate())
                .add("AssignmentTime", apiCaseScene.getTbCaseScene().getAssignmentTime())
                .add("ReceivingCaseDate", apiCaseScene.getTbCaseScene().getReceivingCaseDate())
                .add("ReceivingCaseTime", apiCaseScene.getTbCaseScene().getReceivingCaseTime())
                .add("HappenCaseDate", apiCaseScene.getTbCaseScene().getHappenCaseDate())
                .add("HappenCaseTime", apiCaseScene.getTbCaseScene().getHappenCaseTime())
                .add("KnowCaseDate", apiCaseScene.getTbCaseScene().getKnowCaseDate())
                .add("KnowCaseTime", apiCaseScene.getTbCaseScene().getKnowCaseTime())

                .add("LocaleName", apiCaseScene.getTbCaseScene().getLocaleName())
                .add("DISTRICT_ID", apiCaseScene.getTbCaseScene().getDISTRICT_ID())
                .add("AMPHUR_ID", apiCaseScene.getTbCaseScene().getAMPHUR_ID())
                .add("PROVINCE_ID", apiCaseScene.getTbCaseScene().getPROVINCE_ID())
                .add("Latitude", apiCaseScene.getTbCaseScene().getLatitude())
                .add("Longitude", apiCaseScene.getTbCaseScene().getLongitude())
                .add("FeatureInsideDetail", apiCaseScene.getTbCaseScene().getFeatureInsideDetail())
                .add("CircumstanceOfCaseDetail", apiCaseScene.getTbCaseScene().getCircumstanceOfCaseDetail())
                .add("FullEvidencePerformed", apiCaseScene.getTbCaseScene().getFullEvidencePerformed())
                .add("Annotation", apiCaseScene.getTbCaseScene().getAnnotation())
                .add("MaleCriminalNum", apiCaseScene.getTbCaseScene().getMaleCriminalNum())
                .add("FemaleCriminalNum", apiCaseScene.getTbCaseScene().getFemaleCriminalNum())
                .add("ConfineSufferer", apiCaseScene.getTbCaseScene().getConfineSufferer())
                .add("SuffererPrename", apiCaseScene.getTbCaseScene().getSuffererPrename())
                .add("SuffererName", apiCaseScene.getTbCaseScene().getSuffererName())
                .add("SuffererStatus", apiCaseScene.getTbCaseScene().getSuffererStatus())
                .add("SuffererPhoneNum", apiCaseScene.getTbCaseScene().getSuffererPhoneNum())
                .add("CriminalUsedWeapon", apiCaseScene.getTbCaseScene().getCriminalUsedWeapon())
                .add("VehicleInfo", apiCaseScene.getTbCaseScene().getVehicleInfo())
                .add("LastUpdateDate", apiCaseScene.getTbCaseScene().getLastUpdateDate())
                .add("LastUpdateTime", apiCaseScene.getTbCaseScene().getLastUpdateTime());

        if(apiCaseScene.getTbCaseScene().getCompleteSceneDate() == null || apiCaseScene.getTbCaseScene().getCompleteSceneTime() == null){
            formBuilder.add("CompleteSceneDate", "")
                    .add("CompleteSceneTime", "");
        }else{
            formBuilder.add("CompleteSceneDate", apiCaseScene.getTbCaseScene().getCompleteSceneDate())
                    .add("CompleteSceneTime", apiCaseScene.getTbCaseScene().getCompleteSceneTime());
        }
//tbSceneFeatureOutside
        formBuilder.add("OutsideTypeName", apiCaseScene.getTbSceneFeatureOutside().getOutsideTypeName())
                .add("OutsideTypeDetail", apiCaseScene.getTbSceneFeatureOutside().getOutsideTypeDetail())
                .add("FloorNum", apiCaseScene.getTbSceneFeatureOutside().getFloorNum())
                .add("CaveNum", apiCaseScene.getTbSceneFeatureOutside().getCaveNum())
                .add("HaveFence", apiCaseScene.getTbSceneFeatureOutside().getHaveFence())
                .add("HaveMezzanine", apiCaseScene.getTbSceneFeatureOutside().getHaveMezzanine())
                .add("HaveRooftop", apiCaseScene.getTbSceneFeatureOutside().getHaveRooftop())
                .add("FrontSide", apiCaseScene.getTbSceneFeatureOutside().getFrontSide())
                .add("LeftSide", apiCaseScene.getTbSceneFeatureOutside().getLeftSide())
                .add("RightSide", apiCaseScene.getTbSceneFeatureOutside().getRightSide())
                .add("BackSide", apiCaseScene.getTbSceneFeatureOutside().getBackSide())
                .add("SceneZone", apiCaseScene.getTbSceneFeatureOutside().getSceneZone());


//        for (int i = 0; i < apiCaseScene.getTbSceneInvestigations().size(); i++) {
//            formBuilder.add("tbSceneInvestigations[" + i + "]", String.valueOf(apiCaseScene.getTbSceneInvestigations().get(i)));
//        }
        RequestBody formBody = formBuilder.build();
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(urlMobileIP + "saveCaseReport")
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
