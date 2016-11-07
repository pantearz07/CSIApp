package com.scdc.csiapp.connecting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiInvestigatorsInScene;
import com.scdc.csiapp.apimodel.ApiListCaseScene;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.apimodel.ApiListOfficial;
import com.scdc.csiapp.apimodel.ApiListScheduleInvestigates;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.apimodel.ApiOfficial;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiScheduleGroup;
import com.scdc.csiapp.apimodel.ApiScheduleInvInGroup;
import com.scdc.csiapp.apimodel.ApiScheduleInvestigates;
import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseScene;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbClueShown;
import com.scdc.csiapp.tablemodel.TbComPosition;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbEvidenceType;
import com.scdc.csiapp.tablemodel.TbFindEvidence;
import com.scdc.csiapp.tablemodel.TbGatewayCriminal;
import com.scdc.csiapp.tablemodel.TbGeography;
import com.scdc.csiapp.tablemodel.TbInqPosition;
import com.scdc.csiapp.tablemodel.TbInvPosition;
import com.scdc.csiapp.tablemodel.TbInvestigatorsInScene;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.scdc.csiapp.tablemodel.TbNoticeCase;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPermission;
import com.scdc.csiapp.tablemodel.TbPhotoOfEvidence;
import com.scdc.csiapp.tablemodel.TbPhotoOfInside;
import com.scdc.csiapp.tablemodel.TbPhotoOfOutside;
import com.scdc.csiapp.tablemodel.TbPhotoOfPropertyless;
import com.scdc.csiapp.tablemodel.TbPhotoOfResultscene;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceCenter;
import com.scdc.csiapp.tablemodel.TbPolicePosition;
import com.scdc.csiapp.tablemodel.TbPoliceRank;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbPropertyLoss;
import com.scdc.csiapp.tablemodel.TbProvince;
import com.scdc.csiapp.tablemodel.TbRegistrationGCM;
import com.scdc.csiapp.tablemodel.TbResultScene;
import com.scdc.csiapp.tablemodel.TbResultSceneType;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbSceneFeatureInSide;
import com.scdc.csiapp.tablemodel.TbSceneFeatureOutside;
import com.scdc.csiapp.tablemodel.TbSceneInvestigation;
import com.scdc.csiapp.tablemodel.TbScheduleGroup;
import com.scdc.csiapp.tablemodel.TbScheduleInvInGroup;
import com.scdc.csiapp.tablemodel.TbScheduleInvestigates;
import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;
import com.scdc.csiapp.tablemodel.TbUsers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 15/9/2559.
 */
public class DBHelper extends SQLiteAssetHelper {
    SQLiteDatabase mDb;
    SQLiteQueryBuilder mQb;
    private static final String DB_NAME = "mCSI.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = "DEBUG-DBHelper";
    // สร้าง object เปล่าไว้ใช้เรียกชื่อฟิว
//
    TbAmphur oAmphur = new TbAmphur();
    public String TB_amphur = "amphur";
    public String COL_AMPHUR_ID = "AMPHUR_ID";
    public String COL_AMPHUR_CODE = "AMPHUR_CODE";
    public String COL_AMPHUR_NAME = "AMPHUR_NAME";
    public String COL_POSTCODE = "POSTCODE";
    public String COL_amphur_polygon = "amphur_polygon";
    //
    TbCaseScene oCaseScene = new TbCaseScene();
    public String TB_CaseScene = "casescene";
    public String COL_CaseReportID = "CaseReportID";
    public String COL_ReportNo = "ReportNo";
    public String COL_ReportStatus = "ReportStatus";
    public String COL_AssignmentDate = "AssignmentDate";
    public String COL_AssignmentTime = "AssignmentTime";
    public String COL_FeatureInsideDetail = "FeatureInsideDetail";
    public String COL_FullEvidencePerformed = "FullEvidencePerformed";
    public String COL_Annotation = "Annotation";
    public String COL_MaleCriminalNum = "MaleCriminalNum";
    public String COL_FemaleCriminalNum = "FemaleCriminalNum";
    public String COL_ConfineSufferer = "ConfineSufferer";
    public String COL_CriminalUsedWeapon = "CriminalUsedWeapon";
    public String COL_VehicleInfo = "VehicleInfo";
    //
    TbCaseSceneType oCaseSceneType = new TbCaseSceneType();
    public String TB_CaseSceneType = "casescenetype";
    public String COL_CaseTypeID = "CaseTypeID";
    public String COL_CaseTypeName = "CaseTypeName";
    public String COL_casetype_min = "casetype_min";
    public String COL_casetype_max = "casetype_max";
    public String COL_casetype_icon = "casetype_icon";
    public String COL_casetype_colormin = "casetype_colormin";
    public String COL_casetype_colormedium = "casetype_colormedium";
    public String COL_casetype_colorhigh = "casetype_colorhigh";
    public String COL_casetype_status = "casetype_status";

    //
    TbComPosition oComPosition = new TbComPosition();
    public String TB_composition = "composition";
    public String COL_ComPosID = "ComPosID";
    public String COL_ComPosName = "ComPosName";
    public String COL_ComPosAbbr = "ComPosAbbr";
    //
    TbDistrict oDistrict = new TbDistrict();
    public String TB_district = "district";
    public String COL_DISTRICT_ID = "DISTRICT_ID";
    public String COL_DISTRICT_CODE = "DISTRICT_CODE";
    public String COL_DISTRICT_NAME = "DISTRICT_NAME";
    //
    TbFindEvidence oFindEvidence = new TbFindEvidence();
    public String TB_findevidence = "findevidence";
    public String COL_FindEvidenceID = "FindEvidenceID";
    public String COL_EvidenceNumber = "EvidenceNumber";
    public String COL_FindEvidenceZone = "FindEvidenceZone";
    public String COL_FindEvidencecol = "FindEvidencecol";
    public String COL_Marking = "Marking";
    public String COL_Parceling = "Parceling";
    public String COL_EvidencePerformed = "EvidencePerformed";
    //
    TbGeography oGeography = new TbGeography();
    public String TB_geography = "geography";
    public String COL_GEO_ID = "GEO_ID";
    public String COL_GEO_NAME = "GEO_NAME";
    //
    TbInqPosition oInqPosition = new TbInqPosition();
    public String TB_inqposition = "inqposition";
    public String COL_InqPosID = "InqPosID";
    public String COL_InqPosName = "InqPosName";
    public String COL_InqPosAbbr = "InqPosAbbr";
    //
    TbInvestigatorsInScene oInvestigatorsInScene = new TbInvestigatorsInScene();
    public String TB_investigatorsinscene = "investigatorsinscene";
    public String COL_InvOfficialID = "InvOfficialID";
    public String COL_InvType = "InvType";
    //
    TbInvPosition oInvPosition = new TbInvPosition();
    public String TB_invposition = "invposition";
    public String COL_InvPosID = "InvPosID";
    public String COL_InvPosName = "InvPosName";
    public String COL_InvPosAbbr = "InvPosAbbr";
    //
    TbMultimediaFile oMultimediaFile = new TbMultimediaFile();
    public String TB_MultimediaFile = "multimediafile";
    public String COL_FileID = "FileID";
    public String COL_FileType = "FileType";
    public String COL_FilePath = "FilePath";
    public String COL_FileDescription = "FileDescription";
    public String COL_Timestamp = "Timestamp";
    //
    TbNoticeCase oNoticeCase = new TbNoticeCase();
    public String TB_NoticeCase = "noticecase";
    public String COL_NoticeCaseID = "NoticeCaseID";
    public String COL_Mobile_CaseID = "Mobile_CaseID";
    public String COL_InquiryOfficialID = "InquiryOfficialID";
    public String COL_InvestigatorOfficialID = "InvestigatorOfficialID";
    public String COL_CaseStatus = "CaseStatus";
    public String COL_CaseTel = "CaseTel";
    public String COL_ReceivingCaseDate = "ReceivingCaseDate";
    public String COL_ReceivingCaseTime = "ReceivingCaseTime";
    public String COL_HappenCaseDate = "HappenCaseDate";
    public String COL_HappenCaseTime = "HappenCaseTime";
    public String COL_KnowCaseDate = "KnowCaseDate";
    public String COL_KnowCaseTime = "KnowCaseTime";
    public String COL_SceneNoticeDate = "SceneNoticeDate";
    public String COL_SceneNoticeTime = "SceneNoticeTime";
    public String COL_CompleteSceneDate = "CompleteSceneDate";
    public String COL_CompleteSceneTime = "CompleteSceneTime";
    public String COL_LocaleName = "LocaleName";
    public String COL_Latitude = "Latitude";
    public String COL_Longitude = "Longitude";
    public String COL_SuffererPrename = "SuffererPrename";
    public String COL_SuffererName = "SuffererName";
    public String COL_SuffererStatus = "SuffererStatus";
    public String COL_SuffererPhoneNum = "SuffererPhoneNum";
    public String COL_CircumstanceOfCaseDetail = "CircumstanceOfCaseDetail";
    public String COL_LastUpdateDate = "LastUpdateDate";
    public String COL_LastUpdateTime = "LastUpdateTime";
    //
    TbOfficial oOfficial = new TbOfficial();
    public String TB_official = "official";
    public String COL_OfficialID = "OfficialID";
    public String COL_FirstName = "FirstName";
    public String COL_LastName = "LastName";
    public String COL_Alias = "Alias";
    public String COL_Rank = "Rank";
    public String COL_Position = "Position";
    public String COL_SubPossition = "SubPossition";
    public String COL_PhoneNumber = "PhoneNumber";
    public String COL_OfficialEmail = "OfficialEmail";
    public String COL_OfficialDisplayPic = "OfficialDisplayPic";
    public String COL_AccessType = "AccessType";
    //
    TbPermission oPermission = new TbPermission();
    public String TB_permission = "permission";
    public String COL_id_permission = "id_permission";
    public String COL_per_name = "per_name";
    public String COL_per_value = "per_value";
    //
    TbPhotoOfEvidence oPhotoOfEvidence = new TbPhotoOfEvidence();
    public String TB_photoofevidence = "photoofevidence";
    //
    TbPhotoOfInside oPhotoOfInside = new TbPhotoOfInside();
    public String TB_photoofinside = "photoofinside";
    //
    TbPhotoOfOutside oPhotoOfOutside = new TbPhotoOfOutside();
    public String TB_photoofoutside = "photoofoutside";
    //
    TbPhotoOfResultscene oPhotoOfResultscene = new TbPhotoOfResultscene();
    public String TB_photoofresultscene = "photoofresultscene";
    //
    TbPhotoOfPropertyless oPhotoOfPropertyless = new TbPhotoOfPropertyless();
    public String TB_photoofpropertyless = "photoofpropertyless";
    public String COL_PropertyLessID = "PropertyLessID";
    //
    TbPoliceAgency oPoliceAgency = new TbPoliceAgency();
    public String TB_policeagency = "policeagency";
    public String COL_PoliceAgencyID = "PoliceAgencyID";
    public String COL_PoliceAgencyName = "PoliceAgencyName";
    //
    TbPoliceCenter oPoliceCenter = new TbPoliceCenter();
    public String TB_policecenter = "policecenter";
    public String COL_PoliceCenterID = "PoliceCenterID";
    public String COL_PoliceName = "PoliceName";
    //
    TbPolicePosition oPolicePosition = new TbPolicePosition();
    public String TB_policeposition = "policeposition";
    public String COL_PolicePosID = "PolicePosID";
    //    public String COL_PoliceName = "PoliceName";
    public String COL_PoliceAbbr = "PoliceAbbr";
    //
    TbPoliceRank oPoliceRank = new TbPoliceRank();
    public String TB_policerank = "policerank";
    public String COL_RankID = "RankID";
    public String COL_RankName = "RankName";
    public String COL_RankAbbr = "RankAbbr";
    //
    TbPoliceStation oPoliceStation = new TbPoliceStation();
    public String TB_policestation = "policestation";
    public String COL_PoliceStationID = "PoliceStationID";
    public String COL_PoliceStationName = "PoliceStationName";
    //
    TbPropertyLoss oPropertyLoss = new TbPropertyLoss();
    public String TB_propertyloss = "propertyloss";
    public String COL_PropertyLossID = "PropertyLossID";
    public String COL_PropertyLossName = "PropertyLossName";
    public String COL_PropertyLossNumber = "PropertyLossNumber";
    public String COL_PropertyLossUnit = "PropertyLossUnit";
    public String COL_PropertyLossPosition = "PropertyLossPosition";
    public String COL_PropInsurance = "PropInsurance";
    //
    TbProvince oProvince = new TbProvince();
    public String TB_province = "province";
    public String COL_PROVINCE_ID = "PROVINCE_ID";
    public String COL_PROVINCE_CODE = "PROVINCE_CODE";
    public String COL_PROVINCE_NAME = "PROVINCE_NAME";
    public String COL_province_status = "province_status";
    //
    TbRegistrationGCM oRegistrationGCM = new TbRegistrationGCM();
    TbResultScene oResultScene = new TbResultScene();
    public String TB_resultscene = "resultscene";
    public String COL_RSID = "RSID";
    public String COL_RSDetail = "RSDetail";
    //
    TbResultSceneType oResultSceneType = new TbResultSceneType();
    public String TB_resultscenetype = "resultscenetype";
    public String COL_RSTypeID = "RSTypeID";
    public String COL_RSTypeNameEN = "RSTypeNameEN";
    public String COL_RSTypeNameTH = "RSTypeNameTH";
    //
    TbSCDCagency oScdCagency = new TbSCDCagency();
    public String TB_scdcagency = "scdcagency";
    public String COL_SCDCAgencyCode = "SCDCAgencyCode";
    public String COL_SCDCAgencyName = "SCDCAgencyName";
    //
    TbSCDCcenter oScdCcenter = new TbSCDCcenter();
    public String TB_scdccenter = "scdccenter";
    public String COL_SCDCCenterID = "SCDCCenterID";
    public String COL_SCDCCenterName = "SCDCCenterName";
    public String COL_SCDCCenterProvince = "SCDCCenterProvince";
    //
    TbSceneFeatureInSide oSceneFeatureInSide = new TbSceneFeatureInSide();
    public String TB_SceneFeatureInside = "scenefeatureinside";
    public String COL_FeatureInsideID = "FeatureInsideID";
    public String COL_FloorNo = "FloorNo";
    public String COL_CaveNo = "CaveNo";
    public String COL_FrontInside = "FrontInside";
    public String COL_LeftInside = "LeftInside";
    public String COL_RightInside = "RightInside";
    public String COL_BackInside = "BackInside";
    public String COL_CenterInside = "CenterInside";
    //
    TbSceneFeatureOutside oSceneFeatureOutside = new TbSceneFeatureOutside();
    public String TB_scenefeatureoutside = "scenefeatureoutside";
    public String COL_OutsideTypeName = "OutsideTypeName";
    public String COL_OutsideTypeDetail = "OutsideTypeDetail";
    public String COL_FloorNum = "FloorNum";
    public String COL_CaveNum = "CaveNum";
    public String COL_HaveFence = "HaveFence";
    public String COL_HaveMezzanine = "HaveMezzanine";
    public String COL_HaveRooftop = "HaveRooftop";
    public String COL_FrontSide = "FrontSide";
    public String COL_LeftSide = "LeftSide";
    public String COL_RightSide = "RightSide";
    public String COL_BackSide = "BackSide";
    public String COL_SceneZone = "SceneZone";
    //
    TbSceneInvestigation oSceneInvestigation = new TbSceneInvestigation();
    public String TB_sceneinvestigation = "sceneinvestigation";
    public String COL_SceneInvestID = "SceneInvestID";
    public String COL_SceneInvestDate = "SceneInvestDate";
    public String COL_SceneInvestTime = "SceneInvestTime";
    //
    TbScheduleGroup oScheduleGroup = new TbScheduleGroup();
    public String TB_schedulegroup = "schedulegroup";
    public String COL_ScheduleGroupID = "ScheduleGroupID";
    //
    TbScheduleInvestigates oScheduleInvestigates = new TbScheduleInvestigates();
    public String TB_scheduleinvestigates = "scheduleinvestigates";
    public String COL_ScheduleInvestigateID = "ScheduleInvestigateID";
    public String COL_ScheduleDate = "ScheduleDate";
    public String COL_ScheduleMonth = "ScheduleMonth";
    //
    TbSubcaseSceneType oSubcaseSceneType = new TbSubcaseSceneType();
    public String TB_subcasescenetype = "subcasescenetype";
    public String COL_SubCaseTypeID = "SubCaseTypeID";
    public String COL_SubCaseTypeName = "SubCaseTypeName";
    //
    TbEvidenceType oEvidenceType = new TbEvidenceType();
    public String TB_evidencetype = "evidencetype";
    public String COL_EvidenceTypeID = "EvidenceTypeID";
    public String COL_EvidenceTypeNameEN = "EvidenceTypeNameEN";
    public String COL_EvidenceTypeNameTH = "EvidenceTypeNameTH";
    //
    TbUsers oUsers = new TbUsers();
    public String TB_users = "users";
    public String COL_id_users = "id_users";
    public String COL_pass = "pass";
    public String COL_id_system = "id_system";
    public String COL_title = "title";
    public String COL_name = "name";
    public String COL_surname = "surname";
    public String COL_position = "position";
    public String COL_picture = "picture";
    public String COL_last_login = "last_login";


    public DBHelper(Context context) {
        //super(context, DB_NAME, null, DB_VERSION);
        super(context.getApplicationContext(), DB_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DB_VERSION);

    }

    //*** การทำงานต่างๆในส่วนการดึงข้อมูลใหม่จากเซิร์ฟเวอร์  ***//
    // คำสั่งการอัปเดทตารางต่าง
    // พวกนี้เป็นตารางพื้นฐานที่ต้อง sync
    public boolean syncAmphur(List<TbAmphur> tbAmphurList) {
        if (tbAmphurList.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbAmphurList.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbAmphurList.get(i).AMPHUR_ID;
                strSQL = "SELECT * FROM amphur WHERE "
                        + "AMPHUR_ID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbAmphur temp = new TbAmphur();
                ContentValues Val = new ContentValues();
                Val.put(COL_AMPHUR_ID, tbAmphurList.get(i).AMPHUR_ID);
                Val.put(COL_AMPHUR_CODE, tbAmphurList.get(i).AMPHUR_CODE);
                Val.put(COL_AMPHUR_NAME, tbAmphurList.get(i).AMPHUR_NAME);
                Val.put(COL_POSTCODE, tbAmphurList.get(i).POSTCODE);
                Val.put(COL_GEO_ID, tbAmphurList.get(i).GEO_ID);
                Val.put(COL_PROVINCE_ID, tbAmphurList.get(i).PROVINCE_ID);
                Val.put(COL_amphur_polygon, tbAmphurList.get(i).amphur_polygon);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("amphur", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("amphur", Val, " AMPHUR_ID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table amphur: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncAmphur " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncCaseSceneType(List<TbCaseSceneType> tbCaseSceneTypes) {
        if (tbCaseSceneTypes.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbCaseSceneTypes.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbCaseSceneTypes.get(i).CaseTypeID;
                strSQL = "SELECT * FROM casescenetype WHERE "
                        + "CaseTypeID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbCaseSceneType temp = new TbCaseSceneType();
                ContentValues Val = new ContentValues();
                Val.put(COL_CaseTypeID, tbCaseSceneTypes.get(i).CaseTypeID);
                Val.put(COL_CaseTypeName, tbCaseSceneTypes.get(i).CaseTypeName);
                Val.put(COL_casetype_min, tbCaseSceneTypes.get(i).casetype_min);
                Val.put(COL_casetype_max, tbCaseSceneTypes.get(i).casetype_max);
                Val.put(COL_casetype_icon, tbCaseSceneTypes.get(i).casetype_icon);
                Val.put(COL_casetype_colormin, tbCaseSceneTypes.get(i).casetype_colormin);
                Val.put(COL_casetype_colormedium, tbCaseSceneTypes.get(i).casetype_colormedium);
                Val.put(COL_casetype_colorhigh, tbCaseSceneTypes.get(i).casetype_colorhigh);
                Val.put(COL_casetype_status, tbCaseSceneTypes.get(i).casetype_status);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("casescenetype", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("casescenetype", Val, " CaseTypeID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table casescenetype: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncCaseSceneType " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncComPosition(List<TbComPosition> tbComPositions) {
        if (tbComPositions.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbComPositions.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbComPositions.get(i).ComPosID;
                strSQL = "SELECT * FROM composition WHERE "
                        + "ComPosID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_ComPosID, tbComPositions.get(i).ComPosID);
                Val.put(COL_ComPosName, tbComPositions.get(i).ComPosName);
                Val.put(COL_ComPosAbbr, tbComPositions.get(i).ComPosAbbr);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("composition", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("composition", Val, " ComPosID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table composition: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncComPosition " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncDistrict(List<TbDistrict> tbDistricts) {
        if (tbDistricts.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbDistricts.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbDistricts.get(i).DISTRICT_ID;
                strSQL = "SELECT * FROM district WHERE "
                        + "DISTRICT_ID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbDistrict temp = new TbDistrict();
                ContentValues Val = new ContentValues();
                Val.put(COL_DISTRICT_ID, tbDistricts.get(i).DISTRICT_ID);
                Val.put(COL_DISTRICT_CODE, tbDistricts.get(i).DISTRICT_CODE);
                Val.put(COL_DISTRICT_NAME, tbDistricts.get(i).DISTRICT_NAME);
                Val.put(COL_AMPHUR_ID, tbDistricts.get(i).AMPHUR_ID);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("district", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("district", Val, " DISTRICT_ID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table district: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncDistrict " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncGeography(List<TbGeography> tbGeographies) {
        if (tbGeographies.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbGeographies.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbGeographies.get(i).GEO_ID;
                strSQL = "SELECT * FROM geography WHERE "
                        + "GEO_ID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_GEO_ID, tbGeographies.get(i).GEO_ID);
                Val.put(COL_GEO_NAME, tbGeographies.get(i).GEO_NAME);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("geography", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("geography", Val, " GEO_ID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table geography: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncGeography " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncInqPosition(List<TbInqPosition> tbInqPositions) {
        if (tbInqPositions.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbInqPositions.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbInqPositions.get(i).InqPosID;
                strSQL = "SELECT * FROM inqposition WHERE "
                        + "InqPosID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_InqPosID, tbInqPositions.get(i).InqPosID);
                Val.put(COL_InqPosName, tbInqPositions.get(i).InqPosName);
                Val.put(COL_InqPosAbbr, tbInqPositions.get(i).InqPosAbbr);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("inqposition", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("inqposition", Val, " InqPosID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table inqposition: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncInqPosition " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncInvPosition(List<TbInvPosition> tbInvPositions) {
        if (tbInvPositions.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbInvPositions.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbInvPositions.get(i).InvPosID;
                strSQL = "SELECT * FROM invposition WHERE "
                        + "InvPosID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_InvPosID, tbInvPositions.get(i).InvPosID);
                Val.put(COL_InvPosName, tbInvPositions.get(i).InvPosName);
                Val.put(COL_InvPosAbbr, tbInvPositions.get(i).InvPosAbbr);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("invposition", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("invposition", Val, " InvPosID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table invposition: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncInvPosition " + e.getMessage().toString());
            return false;
        }
    }

    // ต้อง sync เพราะต้องใช้ในการอ้างอิงกับแต่คดี
    public boolean syncOfficial(List<TbOfficial> tbOfficials) {
        if (tbOfficials.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbOfficials.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbOfficials.get(i).OfficialID;
                strSQL = "SELECT * FROM official WHERE "
                        + "OfficialID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbOfficial temp = new TbOfficial();
                ContentValues Val = new ContentValues();
                Val.put(COL_OfficialID, tbOfficials.get(i).OfficialID);
                Val.put(COL_FirstName, tbOfficials.get(i).FirstName);
                Val.put(COL_LastName, tbOfficials.get(i).LastName);
                Val.put(COL_Alias, tbOfficials.get(i).Alias);
                Val.put(COL_Rank, tbOfficials.get(i).Rank);
                Val.put(COL_Position, tbOfficials.get(i).Position);
                Val.put(COL_SubPossition, tbOfficials.get(i).SubPossition);
                Val.put(COL_PhoneNumber, tbOfficials.get(i).PhoneNumber);
                Val.put(COL_OfficialEmail, tbOfficials.get(i).OfficialEmail);
                Val.put(COL_OfficialDisplayPic, tbOfficials.get(i).OfficialDisplayPic);
                Val.put(COL_AccessType, tbOfficials.get(i).AccessType);
                Val.put(COL_SCDCAgencyCode, tbOfficials.get(i).SCDCAgencyCode);
                Val.put(COL_PoliceStationID, tbOfficials.get(i).PoliceStationID);
                Val.put(COL_id_users, tbOfficials.get(i).id_users);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("official", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("official", Val, " OfficialID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table official: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncOfficial " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncPermission(List<TbPermission> tbPermissions) {
        if (tbPermissions.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbPermissions.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbPermissions.get(i).id_permission;
                strSQL = "SELECT * FROM permission WHERE "
                        + "id_permission = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_id_permission, tbPermissions.get(i).id_permission);
                Val.put(COL_per_name, tbPermissions.get(i).per_name);
                Val.put(COL_per_value, tbPermissions.get(i).per_value);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("permission", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("permission", Val, " id_permission = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table permission: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncPermission " + e.getMessage().toString());
            return false;
        }
    }


    public boolean syncPoliceAgency(List<TbPoliceAgency> tbPoliceAgencies) {
        if (tbPoliceAgencies.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbPoliceAgencies.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbPoliceAgencies.get(i).PoliceAgencyID;
                strSQL = "SELECT * FROM policeagency WHERE "
                        + "PoliceAgencyID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbPoliceAgency temp = new TbPoliceAgency();
                ContentValues Val = new ContentValues();
                Val.put(COL_PoliceAgencyID, tbPoliceAgencies.get(i).PoliceAgencyID);
                Val.put(COL_PoliceCenterID, tbPoliceAgencies.get(i).PoliceCenterID);
                Val.put(COL_PoliceAgencyName, tbPoliceAgencies.get(i).PoliceAgencyName);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("policeagency", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("policeagency", Val, " PoliceAgencyID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table policeagency: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncPoliceAgency " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncPoliceCenter(List<TbPoliceCenter> tbPoliceCenters) {
        if (tbPoliceCenters.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbPoliceCenters.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbPoliceCenters.get(i).PoliceCenterID;
                strSQL = "SELECT * FROM policecenter WHERE "
                        + "PoliceCenterID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_PoliceCenterID, tbPoliceCenters.get(i).PoliceCenterID);
                Val.put(COL_PoliceName, tbPoliceCenters.get(i).PoliceName);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("policecenter", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("policecenter", Val, " PoliceCenterID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table policecenter: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncPoliceCenter " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncPolicePosition(List<TbPolicePosition> tbPolicePositions) {
        if (tbPolicePositions.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbPolicePositions.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbPolicePositions.get(i).PolicePosID;
                strSQL = "SELECT * FROM policeposition WHERE "
                        + "PolicePosID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_PolicePosID, tbPolicePositions.get(i).PolicePosID);
                Val.put(COL_PoliceName, tbPolicePositions.get(i).PoliceName);
                Val.put(COL_PoliceAbbr, tbPolicePositions.get(i).PoliceAbbr);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("policeposition", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("policeposition", Val, " PolicePosID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table policeposition: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncPolicePosition " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncPoliceRank(List<TbPoliceRank> tbPoliceRanks) {
        if (tbPoliceRanks.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbPoliceRanks.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbPoliceRanks.get(i).RankID;
                strSQL = "SELECT * FROM policerank WHERE "
                        + "RankID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_RankID, tbPoliceRanks.get(i).RankID);
                Val.put(COL_RankName, tbPoliceRanks.get(i).RankName);
                Val.put(COL_RankAbbr, tbPoliceRanks.get(i).RankAbbr);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("policerank", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("policerank", Val, " RankID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table policerank: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncPoliceRank " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncPoliceStation(List<TbPoliceStation> tbPoliceStations) {
        if (tbPoliceStations.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbPoliceStations.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbPoliceStations.get(i).PoliceStationID;
                strSQL = "SELECT * FROM policestation WHERE "
                        + "PoliceStationID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbPoliceStation temp = new TbPoliceStation();
                ContentValues Val = new ContentValues();
                Val.put(COL_PoliceStationID, tbPoliceStations.get(i).PoliceStationID);
                Val.put(COL_PoliceAgencyID, tbPoliceStations.get(i).PoliceAgencyID);
                Val.put(COL_PoliceStationName, tbPoliceStations.get(i).PoliceStationName);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("policestation", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("policestation", Val, " PoliceStationID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table policestation: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncPoliceStation " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncProvince(List<TbProvince> tbProvinces) {
        if (tbProvinces.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbProvinces.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbProvinces.get(i).PROVINCE_ID;
                strSQL = "SELECT * FROM province WHERE "
                        + "PROVINCE_ID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbProvince temp = new TbProvince();
                ContentValues Val = new ContentValues();
                Val.put(COL_PROVINCE_ID, tbProvinces.get(i).PROVINCE_ID);
                Val.put(COL_PROVINCE_CODE, tbProvinces.get(i).PROVINCE_CODE);
                Val.put(COL_PROVINCE_NAME, tbProvinces.get(i).PROVINCE_NAME);
                Val.put(COL_GEO_ID, tbProvinces.get(i).GEO_ID);
                Val.put(COL_province_status, tbProvinces.get(i).province_status);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("province", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("province", Val, " PROVINCE_ID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table province: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncProvince " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncResultSceneType(List<TbResultSceneType> tbResultSceneTypes) {
        if (tbResultSceneTypes.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbResultSceneTypes.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbResultSceneTypes.get(i).RSTypeID;
                strSQL = "SELECT * FROM resultscenetype WHERE "
                        + "RSTypeID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_RSTypeID, tbResultSceneTypes.get(i).RSTypeID);
                Val.put(COL_RSTypeNameEN, tbResultSceneTypes.get(i).RSTypeNameEN);
                Val.put(COL_RSTypeNameTH, tbResultSceneTypes.get(i).RSTypeNameTH);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("resultscenetype", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("resultscenetype", Val, " RSTypeID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table resultscenetype: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncResultSceneType " + e.getMessage().toString());
            return false;
        }

    }

    public boolean syncSCDCAgency(List<TbSCDCagency> tbSCDCagencies) {
        if (tbSCDCagencies.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbSCDCagencies.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbSCDCagencies.get(i).SCDCAgencyCode;
                strSQL = "SELECT * FROM scdcagency WHERE "
                        + "SCDCAgencyCode = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbSCDCagency temp = new TbSCDCagency();
                ContentValues Val = new ContentValues();
                Val.put(COL_SCDCAgencyCode, tbSCDCagencies.get(i).SCDCAgencyCode);
                Val.put(COL_SCDCCenterID, tbSCDCagencies.get(i).SCDCCenterID);
                Val.put(COL_SCDCAgencyName, tbSCDCagencies.get(i).SCDCAgencyName);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("scdcagency", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("scdcagency", Val, " SCDCAgencyCode = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table scdcagency: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncSCDCAgency " + e.getMessage().toString());
            return false;
        }

    }

    public boolean syncSCDCCenter(List<TbSCDCcenter> tbSCDCcenters) {
        if (tbSCDCcenters.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbSCDCcenters.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbSCDCcenters.get(i).SCDCCenterID;
                strSQL = "SELECT * FROM scdccenter WHERE "
                        + "SCDCCenterID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbSCDCcenter temp = new TbSCDCcenter();
                ContentValues Val = new ContentValues();
                Val.put(COL_SCDCCenterID, tbSCDCcenters.get(i).SCDCCenterID);
                Val.put(COL_SCDCCenterName, tbSCDCcenters.get(i).SCDCCenterName);
                Val.put(COL_SCDCCenterProvince, tbSCDCcenters.get(i).SCDCCenterProvince);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("scdccenter", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("scdccenter", Val, " SCDCCenterID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table scdccenter: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncSCDCCenter " + e.getMessage().toString());
            return false;
        }

    }

    public boolean syncSubCaseSceneType(List<TbSubcaseSceneType> tbSubcaseSceneTypes) {
        if (tbSubcaseSceneTypes.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbSubcaseSceneTypes.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbSubcaseSceneTypes.get(i).SubCaseTypeID;
                strSQL = "SELECT * FROM subcasescenetype WHERE "
                        + "SubCaseTypeID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbSubcaseSceneType temp = new TbSubcaseSceneType();
                ContentValues Val = new ContentValues();
                Val.put(COL_SubCaseTypeID, tbSubcaseSceneTypes.get(i).SubCaseTypeID);
                Val.put(COL_CaseTypeID, tbSubcaseSceneTypes.get(i).CaseTypeID);
                Val.put(COL_SubCaseTypeName, tbSubcaseSceneTypes.get(i).SubCaseTypeName);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("subcasescenetype", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("subcasescenetype", Val, " SubCaseTypeID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table subcasescenetype: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncSubCaseSceneType " + e.getMessage().toString());
            return false;
        }
    }

    public boolean syncEvidenceType(List<TbEvidenceType> tbEvidenceTypes) {
        if (tbEvidenceTypes.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbEvidenceTypes.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbEvidenceTypes.get(i).EvidenceTypeID;
                strSQL = "SELECT * FROM evidencetype WHERE "
                        + "EvidenceTypeID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                ContentValues Val = new ContentValues();
                Val.put(COL_EvidenceTypeID, tbEvidenceTypes.get(i).EvidenceTypeID);
                Val.put(COL_EvidenceTypeNameEN, tbEvidenceTypes.get(i).EvidenceTypeNameEN);
                Val.put(COL_EvidenceTypeNameTH, tbEvidenceTypes.get(i).EvidenceTypeNameTH);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("evidencetype", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("evidencetype", Val, " EvidenceTypeID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table evidencetype: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncEvidenceType " + e.getMessage().toString());
            return false;
        }

    }

    public boolean syncNoticeCase(List<TbNoticeCase> tbNoticeCases) {
        if (tbNoticeCases.size() == 0) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = tbNoticeCases.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = tbNoticeCases.get(i).NoticeCaseID;
                strSQL = "SELECT * FROM noticecase WHERE "
                        + "NoticeCaseID = '" + PRIMARY_KEY + "'";
                Cursor cursor = mDb.rawQuery(strSQL, null);

                TbNoticeCase temp = new TbNoticeCase();
                ContentValues Val = new ContentValues();
                Val.put(COL_NoticeCaseID, tbNoticeCases.get(i).NoticeCaseID);
                Val.put(COL_Mobile_CaseID, tbNoticeCases.get(i).Mobile_CaseID);
                Val.put(COL_InquiryOfficialID, tbNoticeCases.get(i).InquiryOfficialID);
                Val.put(COL_InvestigatorOfficialID, tbNoticeCases.get(i).InvestigatorOfficialID);
                Val.put(COL_SCDCAgencyCode, tbNoticeCases.get(i).SCDCAgencyCode);
                Val.put(COL_CaseTypeID, tbNoticeCases.get(i).CaseTypeID);
                Val.put(COL_SubCaseTypeID, tbNoticeCases.get(i).SubCaseTypeID);
                Val.put(COL_CaseStatus, tbNoticeCases.get(i).CaseStatus);
                Val.put(COL_PoliceStationID, tbNoticeCases.get(i).PoliceStationID);
                Val.put(COL_CaseTel, tbNoticeCases.get(i).CaseTel);
                Val.put(COL_ReceivingCaseDate, tbNoticeCases.get(i).ReceivingCaseDate);
                Val.put(COL_ReceivingCaseTime, tbNoticeCases.get(i).ReceivingCaseTime);
                Val.put(COL_HappenCaseDate, tbNoticeCases.get(i).HappenCaseDate);
                Val.put(COL_HappenCaseTime, tbNoticeCases.get(i).HappenCaseTime);
                Val.put(COL_KnowCaseDate, tbNoticeCases.get(i).KnowCaseDate);
                Val.put(COL_KnowCaseTime, tbNoticeCases.get(i).KnowCaseTime);
                Val.put(COL_SceneNoticeDate, tbNoticeCases.get(i).SceneNoticeDate);
                Val.put(COL_SceneNoticeTime, tbNoticeCases.get(i).SceneNoticeTime);
                Val.put(COL_CompleteSceneDate, tbNoticeCases.get(i).CompleteSceneDate);
                Val.put(COL_CompleteSceneTime, tbNoticeCases.get(i).CompleteSceneTime);
                Val.put(COL_LocaleName, tbNoticeCases.get(i).LocaleName);
                Val.put(COL_DISTRICT_ID, tbNoticeCases.get(i).DISTRICT_ID);
                Val.put(COL_AMPHUR_ID, tbNoticeCases.get(i).AMPHUR_ID);
                Val.put(COL_PROVINCE_ID, tbNoticeCases.get(i).PROVINCE_ID);
                Val.put(COL_Latitude, tbNoticeCases.get(i).Latitude);
                Val.put(COL_Longitude, tbNoticeCases.get(i).Longitude);
                Val.put(COL_SuffererPrename, tbNoticeCases.get(i).SuffererPrename);
                Val.put(COL_SuffererName, tbNoticeCases.get(i).SuffererName);
                Val.put(COL_SuffererStatus, tbNoticeCases.get(i).SuffererStatus);
                Val.put(COL_SuffererPhoneNum, tbNoticeCases.get(i).SuffererPhoneNum);
                Val.put(COL_CircumstanceOfCaseDetail, tbNoticeCases.get(i).CircumstanceOfCaseDetail);
                Val.put(COL_LastUpdateDate, tbNoticeCases.get(i).LastUpdateDate);
                Val.put(COL_LastUpdateTime, tbNoticeCases.get(i).LastUpdateTime);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("noticecase", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    // ต้องตรวจสอบก่อนว่าข้อมูลที่ได้มานั้นใหม่กว่าที่อยู่ใน SQLite จริงๆ
                    db.update("noticecase", Val, " NoticeCaseID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table syncNoticeCase: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncNoticeCase " + e.getMessage().toString());
            return false;
        }
    }

    public String[][] SelectSubCaseType() {
        // TODO Auto-generated method stub
        Log.i("show", "SelectSubCaseType");
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM subcasescenetype";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            Log.i("show", e.getMessage().toString());
            return null;
        }
    }

    public String[][] SelectSubCaseTypeByCaseType(String CaseTypeID) {
        // TODO Auto-generated method stub
        Log.i("show", "SelectSubCaseType");
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM subcasescenetype WHERE " + COL_CaseTypeID + " = '" + CaseTypeID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            Log.i("show", e.getMessage().toString());
            return null;
        }
    }

    public String SelectCaseTypeID(String subCaseTypeID) {
        // TODO Auto-generated method stub

        try {
            String arrData = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT casescenetype.CaseTypeID FROM casescenetype,subcasescenetype WHERE " +
                    "subcasescenetype.SubCaseTypeID = '" + subCaseTypeID + "' " +
                    "AND casescenetype.CaseTypeID = subcasescenetype.CaseTypeID LIMIT 1";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
//                    arrData = new String[cursor.getColumnCount()];

                    arrData = cursor.getString(0);// CaseTypeID

                }
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectCaseTypeBySubCaseType(String subCaseTypeID) {
        // TODO Auto-generated method stub
        Log.i("show", "SelectSubCaseType");
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data


            String strSQL = "SELECT casescenetype.CaseTypeID,casescenetype.CaseTypeName FROM casescenetype,subcasescenetype WHERE " +
                    "subcasescenetype.SubCaseTypeID = '" + subCaseTypeID + "' " +
                    "AND casescenetype.CaseTypeID = subcasescenetype.CaseTypeID";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);

                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            Log.i("show", e.getMessage().toString());
            return null;
        }
    }

    public String[][] SelectCaseType() {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TB_CaseSceneType + " ORDER BY CaseTypeID ASC LIMIT 5 OFFSET 1";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);
                        arrData[i][6] = cursor.getString(6);
                        arrData[i][7] = cursor.getString(7);

                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectInvOfficial(String InvestigatorOfficialID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM official WHERE OfficialID = '" + InvestigatorOfficialID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("Select Official", String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                    arrData[4] = cursor.getString(4);
                    arrData[5] = cursor.getString(5);
                    arrData[6] = cursor.getString(6);
                    arrData[7] = cursor.getString(7);
                }
                Log.i(TAG, "show selectofficial " + arrData[1] + " " + arrData[2]);
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[] SelectPoliceStation(String policestationid) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM policestation WHERE PoliceStationID = '" + policestationid + "'";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("selectPoliceStation", String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);

                }
                Log.i(TAG, "show selectPoliceStation" + arrData[2]);
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public boolean saveNoticeCase(TbNoticeCase tbNoticeCases) {
        if (tbNoticeCases == null) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            PRIMARY_KEY = tbNoticeCases.NoticeCaseID;
            strSQL = "SELECT * FROM noticecase WHERE "
                    + "NoticeCaseID = '" + PRIMARY_KEY + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);

            TbNoticeCase temp = new TbNoticeCase();
            ContentValues Val = new ContentValues();
            Val.put(COL_NoticeCaseID, tbNoticeCases.NoticeCaseID);
            Val.put(COL_Mobile_CaseID, tbNoticeCases.Mobile_CaseID);
            Val.put(COL_InquiryOfficialID, tbNoticeCases.InquiryOfficialID);
            Val.put(COL_InvestigatorOfficialID, tbNoticeCases.InvestigatorOfficialID);
            Val.put(COL_SCDCAgencyCode, tbNoticeCases.SCDCAgencyCode);
            Val.put(COL_CaseTypeID, tbNoticeCases.CaseTypeID);
            Val.put(COL_SubCaseTypeID, tbNoticeCases.SubCaseTypeID);
            Val.put(COL_CaseStatus, tbNoticeCases.CaseStatus);
            Val.put(COL_PoliceStationID, tbNoticeCases.PoliceStationID);
            Val.put(COL_CaseTel, tbNoticeCases.CaseTel);
            Val.put(COL_ReceivingCaseDate, tbNoticeCases.ReceivingCaseDate);
            Val.put(COL_ReceivingCaseTime, tbNoticeCases.ReceivingCaseTime);
            Val.put(COL_HappenCaseDate, tbNoticeCases.HappenCaseDate);
            Val.put(COL_HappenCaseTime, tbNoticeCases.HappenCaseTime);
            Val.put(COL_KnowCaseDate, tbNoticeCases.KnowCaseDate);
            Val.put(COL_KnowCaseTime, tbNoticeCases.KnowCaseTime);
            Val.put(COL_SceneNoticeDate, tbNoticeCases.SceneNoticeDate);
            Val.put(COL_SceneNoticeTime, tbNoticeCases.SceneNoticeTime);
            Val.put(COL_CompleteSceneDate, tbNoticeCases.CompleteSceneDate);
            Val.put(COL_CompleteSceneTime, tbNoticeCases.CompleteSceneTime);
            Val.put(COL_LocaleName, tbNoticeCases.LocaleName);
            Val.put(COL_DISTRICT_ID, tbNoticeCases.DISTRICT_ID);
            Val.put(COL_AMPHUR_ID, tbNoticeCases.AMPHUR_ID);
            Val.put(COL_PROVINCE_ID, tbNoticeCases.PROVINCE_ID);
            Val.put(COL_Latitude, tbNoticeCases.Latitude);
            Val.put(COL_Longitude, tbNoticeCases.Longitude);
            Val.put(COL_SuffererPrename, tbNoticeCases.SuffererPrename);
            Val.put(COL_SuffererName, tbNoticeCases.SuffererName);
            Val.put(COL_SuffererStatus, tbNoticeCases.SuffererStatus);
            Val.put(COL_SuffererPhoneNum, tbNoticeCases.SuffererPhoneNum);
            Val.put(COL_CircumstanceOfCaseDetail, tbNoticeCases.CircumstanceOfCaseDetail);
            Val.put(COL_LastUpdateDate, tbNoticeCases.LastUpdateDate);
            Val.put(COL_LastUpdateTime, tbNoticeCases.LastUpdateTime);

            if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                db.insert("noticecase", null, Val);
                insert++;
            } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                db.update("noticecase", Val, " NoticeCaseID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                update++;
            }
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table noticecase: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in saveNoticeCase " + e.getMessage().toString());
            return false;
        }
    }

    public boolean updateNoticeCaseID(TbNoticeCase tbNoticeCases) {
        if (tbNoticeCases == null) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            String PRIMARY_KEY;
            db.beginTransaction();
            PRIMARY_KEY = tbNoticeCases.Mobile_CaseID;

            ContentValues Val = new ContentValues();
            Val.put(COL_NoticeCaseID, tbNoticeCases.NoticeCaseID);

            db.update("noticecase", Val, " Mobile_CaseID = ?", new String[]{String.valueOf(PRIMARY_KEY)});

            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Update Table noticecase: NoticeCaseID " + tbNoticeCases.NoticeCaseID);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in saveNoticeCase " + e.getMessage().toString());
            return false;
        }
    }

    public boolean DeleteNoticeCase(String Mobile_CaseID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            db.beginTransaction();
            db.delete("noticecase", " Mobile_CaseID = ?", new String[]{String.valueOf(Mobile_CaseID)});

            Log.d(TAG, "DeleteNoticeCase " + Mobile_CaseID);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;

        } catch (Exception e) {
            Log.d(TAG, "Error in DeleteNoticeCase " + e.getMessage().toString());
            return false;
        }
    }

    public boolean saveCaseScene(TbCaseScene tbCaseScene) {
        if (tbCaseScene == null) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            PRIMARY_KEY = tbCaseScene.CaseReportID;
            strSQL = "SELECT * FROM casescene WHERE "
                    + "CaseReportID = '" + PRIMARY_KEY + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, tbCaseScene.CaseReportID);
            Val.put(COL_NoticeCaseID, tbCaseScene.NoticeCaseID);
            Val.put(COL_Mobile_CaseID, tbCaseScene.Mobile_CaseID);
            Val.put(COL_SCDCAgencyCode, tbCaseScene.SCDCAgencyCode);
            Val.put(COL_InvestigatorOfficialID, tbCaseScene.InvestigatorOfficialID);
            Val.put(COL_CaseTypeID, tbCaseScene.CaseTypeID);
            Val.put(COL_SubCaseTypeID, tbCaseScene.SubCaseTypeID);
            Val.put(COL_ReportNo, tbCaseScene.ReportNo);
            Val.put(COL_ReportStatus, tbCaseScene.ReportStatus);
            Val.put(COL_PoliceStationID, tbCaseScene.PoliceStationID);
            Val.put(COL_CaseTel, tbCaseScene.CaseTel);
            Val.put(COL_AssignmentDate, tbCaseScene.AssignmentDate);
            Val.put(COL_AssignmentTime, tbCaseScene.AssignmentTime);
            Val.put(COL_ReceivingCaseDate, tbCaseScene.ReceivingCaseDate);
            Val.put(COL_ReceivingCaseTime, tbCaseScene.ReceivingCaseTime);
            Val.put(COL_HappenCaseDate, tbCaseScene.HappenCaseDate);
            Val.put(COL_HappenCaseTime, tbCaseScene.HappenCaseTime);
            Val.put(COL_KnowCaseDate, tbCaseScene.KnowCaseDate);
            Val.put(COL_KnowCaseTime, tbCaseScene.KnowCaseTime);
            Val.put(COL_CompleteSceneDate, tbCaseScene.CompleteSceneDate);
            Val.put(COL_CompleteSceneTime, tbCaseScene.CompleteSceneTime);
            Val.put(COL_LocaleName, tbCaseScene.LocaleName);
            Val.put(COL_DISTRICT_ID, tbCaseScene.DISTRICT_ID);
            Val.put(COL_AMPHUR_ID, tbCaseScene.AMPHUR_ID);
            Val.put(COL_PROVINCE_ID, tbCaseScene.PROVINCE_ID);
            Val.put(COL_Latitude, tbCaseScene.Latitude);
            Val.put(COL_Longitude, tbCaseScene.Longitude);
            Val.put(COL_FeatureInsideDetail, tbCaseScene.FeatureInsideDetail);
            Val.put(COL_CircumstanceOfCaseDetail, tbCaseScene.CircumstanceOfCaseDetail);
            Val.put(COL_FullEvidencePerformed, tbCaseScene.FullEvidencePerformed);
            Val.put(COL_Annotation, tbCaseScene.Annotation);
            Val.put(COL_MaleCriminalNum, tbCaseScene.MaleCriminalNum);
            Val.put(COL_FemaleCriminalNum, tbCaseScene.FemaleCriminalNum);
            Val.put(COL_ConfineSufferer, tbCaseScene.ConfineSufferer);
            Val.put(COL_SuffererPrename, tbCaseScene.SuffererPrename);
            Val.put(COL_SuffererName, tbCaseScene.SuffererName);
            Val.put(COL_SuffererStatus, tbCaseScene.SuffererStatus);
            Val.put(COL_SuffererPhoneNum, tbCaseScene.SuffererPhoneNum);
            Val.put(COL_CriminalUsedWeapon, tbCaseScene.CriminalUsedWeapon);
            Val.put(COL_VehicleInfo, tbCaseScene.VehicleInfo);
            Val.put(COL_LastUpdateDate, tbCaseScene.LastUpdateDate);
            Val.put(COL_LastUpdateTime, tbCaseScene.LastUpdateTime);

            if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                db.insert("casescene", null, Val);
                insert++;
            } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                db.update("casescene", Val, " CaseReportID = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                update++;
            }
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            Log.d(TAG, "Sync Table casescene: Insert " + insert + ", Update " + update);
            Log.d(TAG, "Sync Table casescene: " + tbCaseScene.HappenCaseDate + " " + tbCaseScene.HappenCaseTime);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in savecasescene " + e.getMessage().toString());
            return false;
        }
    }

    public boolean updateProfile(ApiProfile apiProfile, String Username_old) {
        if (apiProfile == null) {
            return false;
        }
        if (Username_old == null) {
            return false;
        }
        String username_old = Username_old;
        Log.d(TAG, "Not Username_old " + username_old + " new " + apiProfile.getTbUsers().id_users);
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            String sOfficialID, id_users;
            String strSQL;
            db.beginTransaction();
            sOfficialID = apiProfile.getTbOfficial().OfficialID;
//            id_users = apiProfile.getTbUsers().id_users;
            //บันทึกข้อมูลลง users
            strSQL = "SELECT * FROM users WHERE "
                    + "id_users = '" + username_old + "'";
            try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                ContentValues Val = new ContentValues();
                Val.put(COL_id_users, apiProfile.getTbUsers().id_users);
                Val.put(COL_id_permission, apiProfile.getTbUsers().id_permission);
                Val.put(COL_pass, apiProfile.getTbUsers().pass);
                Val.put(COL_id_system, apiProfile.getTbUsers().id_system);
                Val.put(COL_title, apiProfile.getTbUsers().title);
                Val.put(COL_name, apiProfile.getTbUsers().name);
                Val.put(COL_surname, apiProfile.getTbUsers().surname);
                Val.put(COL_position, apiProfile.getTbUsers().position);
                Val.put(COL_picture, apiProfile.getTbUsers().picture);
                Val.put(COL_last_login, apiProfile.getTbUsers().last_login);
                Log.d(TAG, "users   name" + apiProfile.getTbUsers().name);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("users", null, Val);
                    Log.d(TAG, "Sync Table users: Insert ");
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("users", Val, " id_users = ?", new String[]{String.valueOf(username_old)});
                    Log.d(TAG, "Sync Table users: Update ");
                }
            }
            //บันทึกข้อมูลลง official
            strSQL = "SELECT * FROM official WHERE "
                    + "OfficialID = '" + sOfficialID + "'";
            try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                ContentValues Val = new ContentValues();
                Val.put(COL_OfficialID, apiProfile.getTbOfficial().OfficialID);
                Val.put(COL_FirstName, apiProfile.getTbOfficial().FirstName);
                Val.put(COL_LastName, apiProfile.getTbOfficial().LastName);
                Val.put(COL_Alias, apiProfile.getTbOfficial().Alias);
                Val.put(COL_Rank, apiProfile.getTbOfficial().Rank);
                Val.put(COL_Position, apiProfile.getTbOfficial().Position);
                Val.put(COL_SubPossition, apiProfile.getTbOfficial().SubPossition);
                Val.put(COL_PhoneNumber, apiProfile.getTbOfficial().PhoneNumber);
                Val.put(COL_OfficialEmail, apiProfile.getTbOfficial().OfficialEmail);
                Val.put(COL_OfficialDisplayPic, apiProfile.getTbOfficial().OfficialDisplayPic);
                Val.put(COL_AccessType, apiProfile.getTbOfficial().AccessType);
                Val.put(COL_SCDCAgencyCode, apiProfile.getTbOfficial().SCDCAgencyCode);
                Val.put(COL_PoliceStationID, apiProfile.getTbOfficial().PoliceStationID);
                Val.put(COL_id_users, apiProfile.getTbOfficial().id_users);
                Log.d(TAG, "official FirstName" + apiProfile.getTbOfficial().FirstName);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("official", null, Val);
                    Log.d(TAG, "Sync Table official: Insert ");
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("official", Val, " OfficialID = ?", new String[]{String.valueOf(sOfficialID)});
                    Log.d(TAG, "Sync Table official: Update ");
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in updateProfile " + e.getMessage().toString());
            return false;
        }
    }

    public boolean updateDisplayProfile(ApiProfile apiProfile) {
        if (apiProfile == null) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            String sOfficialID, id_users;
            String strSQL;
            db.beginTransaction();
            sOfficialID = apiProfile.getTbOfficial().OfficialID;
            id_users = apiProfile.getTbUsers().id_users;
            //บันทึกข้อมูลลง official
            strSQL = "SELECT * FROM official WHERE "
                    + "OfficialID = '" + sOfficialID + "'";
            try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                ContentValues Val = new ContentValues();

                Val.put(COL_OfficialDisplayPic, apiProfile.getTbOfficial().OfficialDisplayPic);

                Log.d(TAG, "official FirstName" + apiProfile.getTbOfficial().FirstName);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("official", null, Val);
                    Log.d(TAG, "Sync Table official: Insert ");
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("official", Val, " OfficialID = ?", new String[]{String.valueOf(sOfficialID)});
                    Log.d(TAG, "Sync Table official: Update ");
                }
            }
            //บันทึกข้อมูลลง users
            strSQL = "SELECT * FROM users WHERE "
                    + "id_users = '" + id_users + "'";
            try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                ContentValues Val = new ContentValues();
                Val.put(COL_picture, apiProfile.getTbUsers().picture);
                Log.d(TAG, "users   name" + apiProfile.getTbUsers().name);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("users", null, Val);
                    Log.d(TAG, "Sync Table users: Insert ");
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("users", Val, " id_users = ?", new String[]{String.valueOf(id_users)});
                    Log.d(TAG, "Sync Table users: Update ");
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in updateProfile " + e.getMessage().toString());
            return false;
        }
    }

    public boolean updateAlldataCase(ApiCaseScene apiCaseScene) {
        if (apiCaseScene == null) {
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            String sCaseReportID;
            String strSQL;
            db.beginTransaction();
            sCaseReportID = apiCaseScene.getTbCaseScene().CaseReportID;
            //บันทึกข้อมูลลง TbCaseScene
            strSQL = "SELECT * FROM casescene WHERE "
                    + "CaseReportID = '" + sCaseReportID + "'";
            try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                ContentValues Val = new ContentValues();
                Val.put(COL_CaseReportID, apiCaseScene.getTbCaseScene().CaseReportID);
                Val.put(COL_NoticeCaseID, apiCaseScene.getTbCaseScene().NoticeCaseID);
                Val.put(COL_Mobile_CaseID, apiCaseScene.getTbCaseScene().Mobile_CaseID);
                Val.put(COL_SCDCAgencyCode, apiCaseScene.getTbCaseScene().SCDCAgencyCode);
                Val.put(COL_InvestigatorOfficialID, apiCaseScene.getTbCaseScene().InvestigatorOfficialID);
                Val.put(COL_CaseTypeID, apiCaseScene.getTbCaseScene().CaseTypeID);
                Val.put(COL_SubCaseTypeID, apiCaseScene.getTbCaseScene().SubCaseTypeID);
                Val.put(COL_ReportNo, apiCaseScene.getTbCaseScene().ReportNo);
                Val.put(COL_ReportStatus, apiCaseScene.getTbCaseScene().ReportStatus);
                Val.put(COL_PoliceStationID, apiCaseScene.getTbCaseScene().PoliceStationID);
                Val.put(COL_CaseTel, apiCaseScene.getTbCaseScene().CaseTel);
                Val.put(COL_AssignmentDate, apiCaseScene.getTbCaseScene().AssignmentDate);
                Val.put(COL_AssignmentTime, apiCaseScene.getTbCaseScene().AssignmentTime);
                Val.put(COL_ReceivingCaseDate, apiCaseScene.getTbCaseScene().ReceivingCaseDate);
                Val.put(COL_ReceivingCaseTime, apiCaseScene.getTbCaseScene().ReceivingCaseTime);
                Val.put(COL_HappenCaseDate, apiCaseScene.getTbCaseScene().HappenCaseDate);
                Val.put(COL_HappenCaseTime, apiCaseScene.getTbCaseScene().HappenCaseTime);
                Val.put(COL_KnowCaseDate, apiCaseScene.getTbCaseScene().KnowCaseDate);
                Val.put(COL_KnowCaseTime, apiCaseScene.getTbCaseScene().KnowCaseTime);
                Val.put(COL_CompleteSceneDate, apiCaseScene.getTbCaseScene().CompleteSceneDate);
                Val.put(COL_CompleteSceneTime, apiCaseScene.getTbCaseScene().CompleteSceneTime);
                Val.put(COL_LocaleName, apiCaseScene.getTbCaseScene().LocaleName);
                Val.put(COL_DISTRICT_ID, apiCaseScene.getTbCaseScene().DISTRICT_ID);
                Val.put(COL_AMPHUR_ID, apiCaseScene.getTbCaseScene().AMPHUR_ID);
                Val.put(COL_PROVINCE_ID, apiCaseScene.getTbCaseScene().PROVINCE_ID);
                Val.put(COL_Latitude, apiCaseScene.getTbCaseScene().Latitude);
                Val.put(COL_Longitude, apiCaseScene.getTbCaseScene().Longitude);
                Val.put(COL_FeatureInsideDetail, apiCaseScene.getTbCaseScene().FeatureInsideDetail);
                Val.put(COL_CircumstanceOfCaseDetail, apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail);
                Val.put(COL_FullEvidencePerformed, apiCaseScene.getTbCaseScene().FullEvidencePerformed);
                Val.put(COL_Annotation, apiCaseScene.getTbCaseScene().Annotation);
                Val.put(COL_MaleCriminalNum, apiCaseScene.getTbCaseScene().MaleCriminalNum);
                Val.put(COL_FemaleCriminalNum, apiCaseScene.getTbCaseScene().FemaleCriminalNum);
                Val.put(COL_ConfineSufferer, apiCaseScene.getTbCaseScene().ConfineSufferer);
                Val.put(COL_SuffererPrename, apiCaseScene.getTbCaseScene().SuffererPrename);
                Val.put(COL_SuffererName, apiCaseScene.getTbCaseScene().SuffererName);
                Val.put(COL_SuffererStatus, apiCaseScene.getTbCaseScene().SuffererStatus);
                Val.put(COL_SuffererPhoneNum, apiCaseScene.getTbCaseScene().SuffererPhoneNum);
                Val.put(COL_CriminalUsedWeapon, apiCaseScene.getTbCaseScene().CriminalUsedWeapon);
                Val.put(COL_VehicleInfo, apiCaseScene.getTbCaseScene().VehicleInfo);
                Val.put(COL_LastUpdateDate, apiCaseScene.getTbCaseScene().LastUpdateDate);
                Val.put(COL_LastUpdateTime, apiCaseScene.getTbCaseScene().LastUpdateTime);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("casescene", null, Val);
                    Log.d(TAG, "Sync Table casescene: Insert ");
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("casescene", Val, " CaseReportID = ?", new String[]{String.valueOf(sCaseReportID)});
                    Log.d(TAG, "Sync Table casescene: Update ");
                }
            }
            //บันทึกข้อมูลลง TbNoticeCase
            strSQL = "SELECT * FROM noticecase WHERE "
                    + "NoticeCaseID = '" + apiCaseScene.getTbCaseScene().NoticeCaseID + "'";
            try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                ContentValues Val = new ContentValues();
                Val.put(COL_NoticeCaseID, apiCaseScene.getTbNoticeCase().NoticeCaseID);
                Val.put(COL_Mobile_CaseID, apiCaseScene.getTbNoticeCase().Mobile_CaseID);
                Val.put(COL_InquiryOfficialID, apiCaseScene.getTbNoticeCase().InquiryOfficialID);
                Val.put(COL_InvestigatorOfficialID, apiCaseScene.getTbNoticeCase().InvestigatorOfficialID);
                Val.put(COL_SCDCAgencyCode, apiCaseScene.getTbNoticeCase().SCDCAgencyCode);
                Val.put(COL_CaseTypeID, apiCaseScene.getTbNoticeCase().CaseTypeID);
                Val.put(COL_SubCaseTypeID, apiCaseScene.getTbNoticeCase().SubCaseTypeID);
                Val.put(COL_CaseStatus, apiCaseScene.getTbNoticeCase().CaseStatus);
                Val.put(COL_PoliceStationID, apiCaseScene.getTbNoticeCase().PoliceStationID);
                Val.put(COL_CaseTel, apiCaseScene.getTbNoticeCase().CaseTel);
                Val.put(COL_ReceivingCaseDate, apiCaseScene.getTbNoticeCase().ReceivingCaseDate);
                Val.put(COL_ReceivingCaseTime, apiCaseScene.getTbNoticeCase().ReceivingCaseTime);
                Val.put(COL_HappenCaseDate, apiCaseScene.getTbNoticeCase().HappenCaseDate);
                Val.put(COL_HappenCaseTime, apiCaseScene.getTbNoticeCase().HappenCaseTime);
                Val.put(COL_KnowCaseDate, apiCaseScene.getTbNoticeCase().KnowCaseDate);
                Val.put(COL_KnowCaseTime, apiCaseScene.getTbNoticeCase().KnowCaseTime);
                Val.put(COL_SceneNoticeDate, apiCaseScene.getTbNoticeCase().SceneNoticeDate);
                Val.put(COL_SceneNoticeTime, apiCaseScene.getTbNoticeCase().SceneNoticeTime);
                Val.put(COL_CompleteSceneDate, apiCaseScene.getTbNoticeCase().CompleteSceneDate);
                Val.put(COL_CompleteSceneTime, apiCaseScene.getTbNoticeCase().CompleteSceneTime);
                Val.put(COL_LocaleName, apiCaseScene.getTbNoticeCase().LocaleName);
                Val.put(COL_DISTRICT_ID, apiCaseScene.getTbNoticeCase().DISTRICT_ID);
                Val.put(COL_AMPHUR_ID, apiCaseScene.getTbNoticeCase().AMPHUR_ID);
                Val.put(COL_PROVINCE_ID, apiCaseScene.getTbNoticeCase().PROVINCE_ID);
                Val.put(COL_Latitude, apiCaseScene.getTbNoticeCase().Latitude);
                Val.put(COL_Longitude, apiCaseScene.getTbNoticeCase().Longitude);
                Val.put(COL_SuffererPrename, apiCaseScene.getTbNoticeCase().SuffererPrename);
                Val.put(COL_SuffererName, apiCaseScene.getTbNoticeCase().SuffererName);
                Val.put(COL_SuffererStatus, apiCaseScene.getTbNoticeCase().SuffererStatus);
                Val.put(COL_SuffererPhoneNum, apiCaseScene.getTbNoticeCase().SuffererPhoneNum);
                Val.put(COL_CircumstanceOfCaseDetail, apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail);
                Val.put(COL_LastUpdateDate, apiCaseScene.getTbNoticeCase().LastUpdateDate);
                Val.put(COL_LastUpdateTime, apiCaseScene.getTbNoticeCase().LastUpdateTime);
                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("noticecase", null, Val);
                    Log.d(TAG, "Sync Table noticecase: Insert ");
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    db.update("noticecase", Val, " NoticeCaseID = ?", new String[]{String.valueOf(apiCaseScene.getTbCaseScene().NoticeCaseID)});
                    Log.d(TAG, "Sync Table noticecase: Update ");
                }
            }
// บันทึกข้อมูลลง tbSceneInvestigations
            if (apiCaseScene.getTbSceneInvestigations() != null) {
                for (int i = 0; i < apiCaseScene.getTbSceneInvestigations().size(); i++) {
                    strSQL = "SELECT * FROM sceneinvestigation "
                            + " WHERE SceneInvestID = '" + apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_SceneInvestID, apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestID);
                        Val.put(COL_CaseReportID, apiCaseScene.getTbSceneInvestigations().get(i).CaseReportID);
                        Val.put(COL_SceneInvestDate, apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestDate);
                        Val.put(COL_SceneInvestTime, apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestTime);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("sceneinvestigation", null, Val);
                            Log.d(TAG, "Sync Table sceneinvestigation [" + i + "]: Insert " + apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestID);
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("sceneinvestigation", Val, " SceneInvestID = ?", new String[]{String.valueOf(apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestID)});
                            Log.d(TAG, "Sync Table sceneinvestigation [" + i + "]: Update " + apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestID);
                        }
                    }
                }
            }
            if (apiCaseScene.getTbSceneFeatureOutside() != null) {
                //บันทึกข้อมูลลง Tbscenefeatureoutside
                strSQL = "SELECT * FROM scenefeatureoutside "
                        + " WHERE CaseReportID = '" + sCaseReportID + "'";
                try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                    ContentValues Val = new ContentValues();
                    Val.put(COL_CaseReportID, apiCaseScene.getTbSceneFeatureOutside().CaseReportID);
                    Val.put(COL_OutsideTypeName, apiCaseScene.getTbSceneFeatureOutside().OutsideTypeName);
                    Val.put(COL_OutsideTypeDetail, apiCaseScene.getTbSceneFeatureOutside().OutsideTypeDetail);
                    Val.put(COL_FloorNum, apiCaseScene.getTbSceneFeatureOutside().FloorNum);
                    Val.put(COL_CaveNum, apiCaseScene.getTbSceneFeatureOutside().CaveNum);
                    Val.put(COL_HaveFence, apiCaseScene.getTbSceneFeatureOutside().HaveFence);
                    Val.put(COL_HaveMezzanine, apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine);
                    Val.put(COL_HaveRooftop, apiCaseScene.getTbSceneFeatureOutside().HaveRooftop);
                    Val.put(COL_FrontSide, apiCaseScene.getTbSceneFeatureOutside().FrontSide);
                    Val.put(COL_LeftSide, apiCaseScene.getTbSceneFeatureOutside().LeftSide);
                    Val.put(COL_RightSide, apiCaseScene.getTbSceneFeatureOutside().RightSide);
                    Val.put(COL_BackSide, apiCaseScene.getTbSceneFeatureOutside().BackSide);
                    Val.put(COL_SceneZone, apiCaseScene.getTbSceneFeatureOutside().SceneZone);
                    if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                        db.insert("scenefeatureoutside", null, Val);
                        Log.d(TAG, "Sync Table scenefeatureoutside: Insert ");
                    } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                        db.update("scenefeatureoutside", Val, " CaseReportID = ?", new String[]{String.valueOf(sCaseReportID)});
                        Log.d(TAG, "Sync Table scenefeatureoutside: Update ");
                    }
                }
            }
            // บันทึกข้อมูลลง tbSceneFeatureInSide
            if (apiCaseScene.getTbSceneFeatureInSide() != null) {
                for (int i = 0; i < apiCaseScene.getTbSceneFeatureInSide().size(); i++) {
                    strSQL = "SELECT * FROM scenefeatureinside "
                            + " WHERE FeatureInsideID = '" + apiCaseScene.getTbSceneFeatureInSide().get(i).FeatureInsideID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_FeatureInsideID, apiCaseScene.getTbSceneFeatureInSide().get(i).FeatureInsideID);
                        Val.put(COL_CaseReportID, apiCaseScene.getTbSceneFeatureInSide().get(i).CaseReportID);
                        Val.put(COL_FloorNo, apiCaseScene.getTbSceneFeatureInSide().get(i).FloorNo);
                        Val.put(COL_CaveNo, apiCaseScene.getTbSceneFeatureInSide().get(i).CaveNo);
                        Val.put(COL_FrontInside, apiCaseScene.getTbSceneFeatureInSide().get(i).FrontInside);
                        Val.put(COL_LeftInside, apiCaseScene.getTbSceneFeatureInSide().get(i).LeftInside);
                        Val.put(COL_RightInside, apiCaseScene.getTbSceneFeatureInSide().get(i).RightInside);
                        Val.put(COL_BackInside, apiCaseScene.getTbSceneFeatureInSide().get(i).BackInside);
                        Val.put(COL_CenterInside, apiCaseScene.getTbSceneFeatureInSide().get(i).CenterInside);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("scenefeatureinside", null, Val);
                            Log.d(TAG, "Sync Table scenefeatureinside [" + i + "]: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("scenefeatureinside", Val, " FeatureInsideID = ?", new String[]{String.valueOf(apiCaseScene.getTbSceneFeatureInSide().get(i).FeatureInsideID)});
                            Log.d(TAG, "Sync Table scenefeatureinside [" + i + "]: Update ");
                        }
                    }
                }
            }
            // บันทึกข้อมูลลง tbMultimediaFiles
            if (apiCaseScene.getApiMultimedia() != null) {
                for (int i = 0; i < apiCaseScene.getApiMultimedia().size(); i++) {
                    strSQL = "SELECT * FROM multimediaFile "
                            + " WHERE FileID = '" + apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    Log.d(TAG, "บันทึกข้อมูลลง tbMultimediaFiles " + strSQL);
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_FileID, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID);
                        Val.put(COL_CaseReportID, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID);
                        Val.put(COL_FileType, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType);
                        Val.put(COL_FilePath, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                        Val.put(COL_FileDescription, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileDescription);
                        Val.put(COL_Timestamp, apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().Timestamp);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("multimediaFile", null, Val);
                            Log.d(TAG, "Sync Table MultimediaFile [" + i + "]: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("multimediaFile", Val, " FileID = ?", new String[]{String.valueOf(apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID)});
                            Log.d(TAG, "Sync Table MultimediaFile [" + i + "]: Update " + apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID);
                        }

                        if (apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfOutside() != null) {
                            strSQL = "SELECT * FROM " + TB_photoofoutside
                                    + " WHERE " + COL_FileID + " = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfOutside().FileID + "' "
                                    + "AND CaseReportID = '" + sCaseReportID + "'";
                            try (Cursor cursor2 = mDb.rawQuery(strSQL, null)) {
                                ContentValues Val2 = new ContentValues();
                                Val2.put(COL_FileID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfOutside().FileID);
                                Val2.put(COL_CaseReportID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfOutside().CaseReportID);

                                if (cursor2.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                                    db.insert(TB_photoofoutside, null, Val2);
                                    Log.d(TAG, "Sync Table PhotoOfOutside [" + i + "]: Insert ");
                                } else if (cursor2.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                                    db.update(TB_photoofoutside, Val2, " FileID = ?", new String[]{String.valueOf(apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfOutside().FileID)});
                                    Log.d(TAG, "Sync Table PhotoOfOutside [" + i + "]: Update " + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfOutside().FileID);
                                }
                            }
                        }
                        if (apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside() != null) {
                            strSQL = "SELECT * FROM " + TB_photoofinside
                                    + " WHERE " + COL_FileID + " = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FileID + "' "
                                    + "AND FeatureInsideID = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FeatureInsideID + "'";
                            try (Cursor cursor2 = mDb.rawQuery(strSQL, null)) {

                                ContentValues Val3 = new ContentValues();
                                Val3.put(COL_FileID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FileID);
                                Val3.put(COL_FeatureInsideID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FeatureInsideID);

                                if (cursor2.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                                    db.insert(TB_photoofinside, null, Val3);
                                    Log.d(TAG, "Sync Table PhotoOfInside [" + i + "]: Insert ");
                                } else if (cursor2.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                                    db.update(TB_photoofinside, Val3, " FileID = ?", new String[]{String.valueOf(apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FileID)});
                                    Log.d(TAG, "Sync Table PhotoOfInside [" + i + "]: Update " + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FileID);
                                }
                            }
                        }
                        if (apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence() != null) {
                            strSQL = "SELECT * FROM " + TB_photoofevidence
                                    + " WHERE " + COL_FileID + " = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FileID + "' "
                                    + "AND FindEvidenceID = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FindEvidenceID + "'";
                            try (Cursor cursor2 = mDb.rawQuery(strSQL, null)) {
                                ContentValues Val4 = new ContentValues();
                                Val4.put(COL_FileID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FileID);
                                Val4.put(COL_FindEvidenceID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FindEvidenceID);

                                if (cursor2.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                                    db.insert(TB_photoofevidence, null, Val4);
                                    Log.d(TAG, "Sync Table PhotoOfEvidence [" + i + "]: Insert ");
                                } else if (cursor2.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                                    db.update(TB_photoofevidence, Val4, " FileID = ?", new String[]{String.valueOf(apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FileID)});
                                    Log.d(TAG, "Sync Table PhotoOfEvidence [" + i + "]: Update " + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FileID);
                                }
                            }
                        }
                        if (apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene() != null) {
                            strSQL = "SELECT * FROM " + TB_photoofresultscene
                                    + " WHERE " + COL_FileID + " = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().FileID + "' "
                                    + "AND RSID = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().RSID + "'";
                            try (Cursor cursor2 = mDb.rawQuery(strSQL, null)) {
                                ContentValues Val5 = new ContentValues();
                                Val5.put(COL_FileID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().FileID);
                                Val5.put(COL_RSID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().RSID);

                                if (cursor2.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                                    db.insert(TB_photoofresultscene, null, Val5);
                                    Log.d(TAG, "Sync Table TB_PhotoOfResultscene [" + i + "]: Insert ");
                                } else if (cursor2.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                                    db.update(TB_photoofresultscene, Val5, " FileID = ?", new String[]{String.valueOf(apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().FileID)});
                                    Log.d(TAG, "Sync Table TB_PhotoOfResultscene [" + i + "]: Update " + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().FileID);
                                }
                            }
                        }
                        if (apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless() != null) {
                            strSQL = "SELECT * FROM " + TB_photoofpropertyless
                                    + " WHERE " + COL_FileID + " = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless().FileID + "' "
                                    + "AND PropertyLessID = '" + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless().PropertyLessID + "'";
                            try (Cursor cursor2 = mDb.rawQuery(strSQL, null)) {
                                ContentValues Val6 = new ContentValues();
                                Val6.put(COL_FileID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless().FileID);
                                Val6.put(COL_PropertyLessID, apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless().PropertyLessID);

                                if (cursor2.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                                    db.insert(TB_photoofpropertyless, null, Val6);
                                    Log.d(TAG, "Sync Table TB_PhotoOfPropertyless [" + i + "]: Insert ");
                                } else if (cursor2.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                                    db.update(TB_photoofpropertyless, Val6, " FileID = ?", new String[]{String.valueOf(apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless().FileID)});
                                    Log.d(TAG, "Sync Table TB_PhotoOfPropertyless [" + i + "]: Update " + apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless().FileID);
                                }
                            }
                        }
                    }
                }
            }
            // บันทึกข้อมูลลง tbFindEvidences
            if (apiCaseScene.getTbFindEvidences() != null) {
                for (int i = 0; i < apiCaseScene.getTbFindEvidences().size(); i++) {
                    strSQL = "SELECT * FROM findevidence "
                            + " WHERE FindEvidenceID = '" + apiCaseScene.getTbFindEvidences().get(i).FindEvidenceID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_FindEvidenceID, apiCaseScene.getTbFindEvidences().get(i).FindEvidenceID);
                        Val.put(COL_EvidenceTypeID, apiCaseScene.getTbFindEvidences().get(i).EvidenceTypeID);
                        Val.put(COL_CaseReportID, apiCaseScene.getTbFindEvidences().get(i).CaseReportID);
                        Val.put(COL_SceneInvestID, apiCaseScene.getTbFindEvidences().get(i).SceneInvestID);
                        Val.put(COL_EvidenceNumber, apiCaseScene.getTbFindEvidences().get(i).EvidenceNumber);
                        Val.put(COL_FindEvidenceZone, apiCaseScene.getTbFindEvidences().get(i).FindEvidenceZone);
                        Val.put(COL_FindEvidencecol, apiCaseScene.getTbFindEvidences().get(i).FindEvidencecol);
                        Val.put(COL_Marking, apiCaseScene.getTbFindEvidences().get(i).Marking);
                        Val.put(COL_Parceling, apiCaseScene.getTbFindEvidences().get(i).Parceling);
                        Val.put(COL_EvidencePerformed, apiCaseScene.getTbFindEvidences().get(i).EvidencePerformed);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("FindEvidence", null, Val);
                            Log.d(TAG, "Sync Table FindEvidence [" + i + "]: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("FindEvidence", Val, " FindEvidenceID = ?", new String[]{String.valueOf(apiCaseScene.getTbFindEvidences().get(i).FindEvidenceID)});
                            Log.d(TAG, "Sync Table FindEvidence [" + i + "]: Update ");
                        }
                    }
                }
            }
            // บันทึกข้อมูลลง tbResultScenes
            if (apiCaseScene.getTbResultScenes() != null) {
                for (int i = 0; i < apiCaseScene.getTbResultScenes().size(); i++) {
                    strSQL = "SELECT * FROM resultscene "
                            + " WHERE RSID = '" + apiCaseScene.getTbResultScenes().get(i).RSID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_RSID, apiCaseScene.getTbResultScenes().get(i).RSID);
                        Val.put(COL_RSTypeID, apiCaseScene.getTbResultScenes().get(i).RSTypeID);
                        Val.put(COL_CaseReportID, apiCaseScene.getTbResultScenes().get(i).CaseReportID);
                        Val.put(COL_RSDetail, apiCaseScene.getTbResultScenes().get(i).RSDetail);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("resultscene", null, Val);
                            Log.d(TAG, "Sync Table resultscene [" + i + "]: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("resultscene", Val, " RSID = ?", new String[]{String.valueOf(apiCaseScene.getTbResultScenes().get(i).RSID)});
                            Log.d(TAG, "Sync Table resultscene [" + i + "]: Update ");
                        }
                    }
                }
            }
            // บันทึกข้อมูลลง tbGatewayCriminals
            if (apiCaseScene.getTbGatewayCriminals() != null) {
                for (int i = 0; i < apiCaseScene.getTbGatewayCriminals().size(); i++) {
                    strSQL = "SELECT * FROM resultscene "
                            + " WHERE RSID = '" + apiCaseScene.getTbGatewayCriminals().get(i).RSID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_RSID, apiCaseScene.getTbGatewayCriminals().get(i).RSID);
                        Val.put(COL_RSTypeID, apiCaseScene.getTbGatewayCriminals().get(i).RSTypeID);
                        Val.put(COL_CaseReportID, apiCaseScene.getTbGatewayCriminals().get(i).CaseReportID);
                        Val.put(COL_RSDetail, apiCaseScene.getTbGatewayCriminals().get(i).RSDetail);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("resultscene", null, Val);
                            Log.d(TAG, "Sync Table resultscene [" + i + "] getTbGatewayCriminals: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("resultscene", Val, " RSID = ?", new String[]{String.valueOf(apiCaseScene.getTbGatewayCriminals().get(i).RSID)});
                            Log.d(TAG, "Sync Table resultscene [" + i + "] getTbGatewayCriminals: Update ");
                        }
                    }
                }
            }
            // บันทึกข้อมูลลง tbClueShowns
            if (apiCaseScene.getTbClueShowns() != null) {
                for (int i = 0; i < apiCaseScene.getTbClueShowns().size(); i++) {
                    strSQL = "SELECT * FROM resultscene "
                            + " WHERE RSID = '" + apiCaseScene.getTbClueShowns().get(i).RSID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_RSID, apiCaseScene.getTbClueShowns().get(i).RSID);
                        Val.put(COL_RSTypeID, apiCaseScene.getTbClueShowns().get(i).RSTypeID);
                        Val.put(COL_CaseReportID, apiCaseScene.getTbClueShowns().get(i).CaseReportID);
                        Val.put(COL_RSDetail, apiCaseScene.getTbClueShowns().get(i).RSDetail);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("resultscene", null, Val);
                            Log.d(TAG, "Sync Table resultscene [" + i + "] getTbClueShowns: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("resultscene", Val, " RSID = ?", new String[]{String.valueOf(apiCaseScene.getTbClueShowns().get(i).RSID)});
                            Log.d(TAG, "Sync Table resultscene [" + i + "] getTbClueShowns: Update ");
                        }
                    }
                }
            }
            // บันทึกข้อมูลลง tbPropertyLosses
            if (apiCaseScene.getTbPropertyLosses() != null) {
                for (int i = 0; i < apiCaseScene.getTbPropertyLosses().size(); i++) {
                    strSQL = "SELECT * FROM propertyloss "
                            + " WHERE PropertyLossID = '" + apiCaseScene.getTbPropertyLosses().get(i).PropertyLossID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        ContentValues Val = new ContentValues();
                        Val.put(COL_PropertyLossID, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossID);
                        Val.put(COL_CaseReportID, apiCaseScene.getTbPropertyLosses().get(i).CaseReportID);
                        Val.put(COL_PropertyLossName, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossName);
                        Val.put(COL_PropertyLossNumber, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossNumber);
                        Val.put(COL_PropertyLossUnit, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossUnit);
                        Val.put(COL_PropertyLossPosition, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossPosition);
                        Val.put(COL_PropInsurance, apiCaseScene.getTbPropertyLosses().get(i).PropInsurance);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("propertyloss", null, Val);
                            Log.d(TAG, "Sync Table propertyloss [" + i + "]: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("propertyloss", Val, " PropertyLossID = ?", new String[]{String.valueOf(apiCaseScene.getTbPropertyLosses().get(i).PropertyLossID)});
                            Log.d(TAG, "Sync Table propertyloss [" + i + "]: Update ");
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in updateAlldataCase " + e.getMessage().toString());
            return false;
        }

    }

    public ApiListNoticeCase selectApiNoticeCase(String OfficeID) {
        Log.d(TAG, "OfficeID:" + OfficeID);
        ApiListNoticeCase apiListNoticeCase = new ApiListNoticeCase();
        apiListNoticeCase.setStatus("success");
        ApiListNoticeCase.DataEntity dataEntity = new ApiListNoticeCase().new DataEntity();
        dataEntity.setAction("SQLite selectApiNoticeCase");
        dataEntity.setReason("โหลดรายการคดีสำเร็จ");

        List<ApiNoticeCase> apiNoticeCases = new ArrayList<>();

        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL, strSQL_main;

            strSQL_main = "SELECT * FROM noticecase "
                    + " WHERE InquiryOfficialID = '" + OfficeID + "'" +
                    " ORDER BY ReceivingCaseDate DESC, ReceivingCaseTime DESC," +
                    " LastUpdateDate DESC, LastUpdateTime DESC";
            try (Cursor cursor = db.rawQuery(strSQL_main, null)) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    ApiNoticeCase apiNoticeCase = new ApiNoticeCase();

                    TbNoticeCase temp = new TbNoticeCase();
                    temp.NoticeCaseID = cursor.getString(cursor.getColumnIndex(COL_NoticeCaseID));
                    temp.Mobile_CaseID = cursor.getString(cursor.getColumnIndex(COL_Mobile_CaseID));
                    temp.InquiryOfficialID = cursor.getString(cursor.getColumnIndex(COL_InquiryOfficialID));
                    temp.InvestigatorOfficialID = cursor.getString(cursor.getColumnIndex(COL_InvestigatorOfficialID));
                    temp.SCDCAgencyCode = cursor.getString(cursor.getColumnIndex(COL_SCDCAgencyCode));
                    temp.CaseTypeID = cursor.getString(cursor.getColumnIndex(COL_CaseTypeID));
                    temp.SubCaseTypeID = cursor.getString(cursor.getColumnIndex(COL_SubCaseTypeID));
                    temp.CaseStatus = cursor.getString(cursor.getColumnIndex(COL_CaseStatus));
                    temp.PoliceStationID = cursor.getString(cursor.getColumnIndex(COL_PoliceStationID));
                    temp.CaseTel = cursor.getString(cursor.getColumnIndex(COL_CaseTel));
                    temp.ReceivingCaseDate = cursor.getString(cursor.getColumnIndex(COL_ReceivingCaseDate));
                    temp.ReceivingCaseTime = cursor.getString(cursor.getColumnIndex(COL_ReceivingCaseTime));
                    temp.HappenCaseDate = cursor.getString(cursor.getColumnIndex(COL_HappenCaseDate));
                    temp.HappenCaseTime = cursor.getString(cursor.getColumnIndex(COL_HappenCaseTime));
                    temp.KnowCaseDate = cursor.getString(cursor.getColumnIndex(COL_KnowCaseDate));
                    temp.KnowCaseTime = cursor.getString(cursor.getColumnIndex(COL_KnowCaseTime));
                    temp.SceneNoticeDate = cursor.getString(cursor.getColumnIndex(COL_SceneNoticeDate));
                    temp.SceneNoticeTime = cursor.getString(cursor.getColumnIndex(COL_SceneNoticeTime));
                    temp.CompleteSceneDate = cursor.getString(cursor.getColumnIndex(COL_CompleteSceneDate));
                    temp.CompleteSceneTime = cursor.getString(cursor.getColumnIndex(COL_CompleteSceneTime));
                    temp.LocaleName = cursor.getString(cursor.getColumnIndex(COL_LocaleName));
                    temp.DISTRICT_ID = cursor.getString(cursor.getColumnIndex(COL_DISTRICT_ID));
                    temp.AMPHUR_ID = cursor.getString(cursor.getColumnIndex(COL_AMPHUR_ID));
                    temp.PROVINCE_ID = cursor.getString(cursor.getColumnIndex(COL_PROVINCE_ID));
                    temp.Latitude = cursor.getString(cursor.getColumnIndex(COL_Latitude));
                    temp.Longitude = cursor.getString(cursor.getColumnIndex(COL_Longitude));
                    temp.SuffererPrename = cursor.getString(cursor.getColumnIndex(COL_SuffererPrename));
                    temp.SuffererName = cursor.getString(cursor.getColumnIndex(COL_SuffererName));
                    temp.SuffererStatus = cursor.getString(cursor.getColumnIndex(COL_SuffererStatus));
                    temp.SuffererPhoneNum = cursor.getString(cursor.getColumnIndex(COL_SuffererPhoneNum));
                    temp.CircumstanceOfCaseDetail = cursor.getString(cursor.getColumnIndex(COL_CircumstanceOfCaseDetail));
                    temp.LastUpdateDate = cursor.getString(cursor.getColumnIndex(COL_LastUpdateDate));
                    temp.LastUpdateTime = cursor.getString(cursor.getColumnIndex(COL_LastUpdateTime));
                    apiNoticeCase.setTbNoticeCase(temp);

                    // Index tbOfficial ดึงจากตาราง official
                    strSQL = "SELECT * FROM official "
                            + " WHERE OfficialID = '" + temp.InvestigatorOfficialID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbOfficial temp2 = new TbOfficial();
                            temp2.OfficialID = cursor2.getString(cursor2.getColumnIndex(COL_OfficialID));
                            temp2.FirstName = cursor2.getString(cursor2.getColumnIndex(COL_FirstName));
                            temp2.LastName = cursor2.getString(cursor2.getColumnIndex(COL_LastName));
                            temp2.Alias = cursor2.getString(cursor2.getColumnIndex(COL_Alias));
                            temp2.Rank = cursor2.getString(cursor2.getColumnIndex(COL_Rank));
                            temp2.Position = cursor2.getString(cursor2.getColumnIndex(COL_Position));
                            temp2.SubPossition = cursor2.getString(cursor2.getColumnIndex(COL_SubPossition));
                            temp2.PhoneNumber = cursor2.getString(cursor2.getColumnIndex(COL_PhoneNumber));
                            temp2.OfficialEmail = cursor2.getString(cursor2.getColumnIndex(COL_OfficialEmail));
                            temp2.OfficialDisplayPic = cursor2.getString(cursor2.getColumnIndex(COL_OfficialDisplayPic));
                            temp2.AccessType = cursor2.getString(cursor2.getColumnIndex(COL_AccessType));
                            temp2.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyCode));
                            temp2.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationID));
                            temp2.id_users = cursor2.getString(cursor2.getColumnIndex(COL_id_users));
                            apiNoticeCase.setTbOfficial(temp2);
                        }
                    }


                    // Index tbSubcaseSceneType ดึงจากตาราง subcasescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM subcasescenetype "
                            + " WHERE SubCaseTypeID = '" + temp.SubCaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbSubcaseSceneType temp4 = new TbSubcaseSceneType();
                            temp4.SubCaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_SubCaseTypeID));
                            temp4.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_CaseTypeID));
                            temp4.SubCaseTypeName = cursor2.getString(cursor2.getColumnIndex(COL_SubCaseTypeName));
                            apiNoticeCase.setTbSubcaseSceneType(temp4);
                        }
                    }
// Index tbCaseSceneType ดึงจากตาราง casescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM casescenetype "
                            + " WHERE CaseTypeID = '" + apiNoticeCase.getTbSubcaseSceneType().CaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();

                            TbCaseSceneType temp3 = new TbCaseSceneType();
                            temp3.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_CaseTypeID));
                            temp3.CaseTypeName = cursor2.getString(cursor2.getColumnIndex(COL_CaseTypeName));
                            temp3.casetype_min = cursor2.getString(cursor2.getColumnIndex(COL_casetype_min));
                            temp3.casetype_max = cursor2.getString(cursor2.getColumnIndex(COL_casetype_max));
                            temp3.casetype_icon = cursor2.getString(cursor2.getColumnIndex(COL_casetype_icon));
                            temp3.casetype_colormin = cursor2.getString(cursor2.getColumnIndex(COL_casetype_colormin));
                            temp3.casetype_colormedium = cursor2.getString(cursor2.getColumnIndex(COL_casetype_colormedium));
                            temp3.casetype_colorhigh = cursor2.getString(cursor2.getColumnIndex(COL_casetype_colorhigh));
                            temp3.casetype_status = cursor2.getString(cursor2.getColumnIndex(COL_casetype_status));
                            apiNoticeCase.setTbCaseSceneType(temp3);
                        }
                    }

                    // Index tbPoliceStation ดึงจากตาราง policestation ใช้ค่าจาก Index tbNoticeCase
                    TbPoliceStation temp5 = new TbPoliceStation();
                    strSQL = "SELECT * FROM policestation "
                            + " WHERE PoliceStationID = '" + temp.PoliceStationID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp5.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationID));
                            temp5.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceAgencyID));
                            temp5.PoliceStationName = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationName));
                            apiNoticeCase.setTbPoliceStation(temp5);
                        }
                    }

                    // Index tbPoliceAgency ดึงจากตาราง policeagency ใช้ค่าจาก Index tbPoliceStation
                    if (temp5.PoliceStationID != null) {
                        strSQL = "SELECT * FROM policeagency "
                                + " WHERE PoliceAgencyID = '" + temp5.PoliceStationID + "'";
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (cursor2.getCount() == 1) {
                                cursor2.moveToFirst();
                                TbPoliceAgency temp6 = new TbPoliceAgency();
                                temp6.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceAgencyID));
                                temp6.PoliceCenterID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceCenterID));
                                temp6.PoliceAgencyName = cursor2.getString(cursor2.getColumnIndex(COL_PoliceAgencyName));
                                apiNoticeCase.setTbPoliceAgency(temp6);
                            }
                        }
                    }

                    // Index tbDistrict ดึงจากตาราง district ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM district "
                            + " WHERE DISTRICT_ID = '" + temp.DISTRICT_ID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbDistrict temp7 = new TbDistrict();
                            temp7.DISTRICT_ID = cursor2.getString(cursor2.getColumnIndex(COL_DISTRICT_ID));
                            temp7.DISTRICT_CODE = cursor2.getString(cursor2.getColumnIndex(COL_DISTRICT_CODE));
                            temp7.DISTRICT_NAME = cursor2.getString(cursor2.getColumnIndex(COL_DISTRICT_NAME));
                            temp7.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_ID));
                            apiNoticeCase.setTbDistrict(temp7);
                        }
                    }

                    // Index tbProvince ดึงจากตาราง province ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM province "
                            + " WHERE PROVINCE_ID = '" + temp.PROVINCE_ID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbProvince temp8 = new TbProvince();
                            temp8.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_ID));
                            temp8.PROVINCE_CODE = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_CODE));
                            temp8.PROVINCE_NAME = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_NAME));
                            temp8.GEO_ID = cursor2.getString(cursor2.getColumnIndex(COL_GEO_ID));
                            temp8.province_status = cursor2.getString(cursor2.getColumnIndex(COL_province_status));
                            apiNoticeCase.setTbProvince(temp8);
                        }
                    }

                    // Index tbAmphur ดึงจากตาราง amphur ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM amphur "
                            + " WHERE AMPHUR_ID = '" + temp.AMPHUR_ID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbAmphur temp9 = new TbAmphur();
                            temp9.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_ID));
                            temp9.AMPHUR_CODE = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_CODE));
                            temp9.AMPHUR_NAME = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_NAME));
                            temp9.POSTCODE = cursor2.getString(cursor2.getColumnIndex(COL_POSTCODE));
                            temp9.GEO_ID = cursor2.getString(cursor2.getColumnIndex(COL_GEO_ID));
                            temp9.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_ID));
                            temp9.amphur_polygon = cursor2.getString(cursor2.getColumnIndex(COL_amphur_polygon));
                            apiNoticeCase.setTbAmphur(temp9);
                        }
                    }

                    // Index tbSCDCagency ดึงจากตาราง scdcagency ใช้ค่าจาก Index tbNoticeCase
                    TbSCDCagency temp10 = new TbSCDCagency();
                    strSQL = "SELECT * FROM scdcagency "
                            + " WHERE SCDCAgencyCode = '" + temp.SCDCAgencyCode + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp10.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyCode));
                            temp10.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterID));
                            temp10.SCDCAgencyName = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyName));
                            apiNoticeCase.setTbSCDCagency(temp10);
                        }
                    }

                    // Index tbSCDCcenter ดึงจากตาราง scdccenter ใช้ค่าจาก Index tbSCDCagency
                    if (temp10.SCDCCenterID != null) {
                        strSQL = "SELECT * FROM scdccenter "
                                + " WHERE SCDCCenterID = '" + temp10.SCDCCenterID + "'";
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (cursor2.getCount() == 1) {
                                cursor2.moveToFirst();
                                TbSCDCcenter temp11 = new TbSCDCcenter();
                                temp11.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterID));
                                temp11.SCDCCenterName = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterName));
                                temp11.SCDCCenterProvince = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterProvince));
                                apiNoticeCase.setTbSCDCcenter(temp11);
                            }
                        }
                    }

                    //ส่งค่าทั้งหมดเข้า apiNoticeCases
                    apiNoticeCase.setMode("offline");
                    apiNoticeCases.add(apiNoticeCase);
                }
            }
            db.close();
            // รวมข้อมูลที่ได้ทั้งหมดลง apiListNoticeCase ก่อนส่งกลับไปใช้
            dataEntity.setResult(apiNoticeCases);
            apiListNoticeCase.setData(dataEntity);
            return apiListNoticeCase;
        } catch (Exception e) {
            Log.d(TAG, "Error in selectApiNoticeCase " + e.getMessage().toString());
            return null;
        }
    }

    public ApiListOfficial selectApiOfficial(String AccessType) {
        Log.d(TAG, "AccessType:" + AccessType);
        ApiListOfficial apiListOfficial = new ApiListOfficial();
        apiListOfficial.setStatus("success");
        ApiListOfficial.DataEntity dataEntity = new ApiListOfficial().new DataEntity();
        dataEntity.setAction("SQLite selectApiOfficial");
        dataEntity.setReason("โหลดรายการสำเร็จ");

        List<ApiOfficial> apiOfficialList = new ArrayList<>();

        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL, strSQL_main;

            strSQL_main = "SELECT * FROM official "
                    + " WHERE AccessType = '" + AccessType + "'";

            try (Cursor cursor = db.rawQuery(strSQL_main, null)) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    ApiOfficial apiOfficial = new ApiOfficial();

                    TbOfficial temp2 = new TbOfficial();
                    temp2.OfficialID = cursor.getString(cursor.getColumnIndex(COL_OfficialID));
                    temp2.FirstName = cursor.getString(cursor.getColumnIndex(COL_FirstName));
                    temp2.LastName = cursor.getString(cursor.getColumnIndex(COL_LastName));
                    temp2.Alias = cursor.getString(cursor.getColumnIndex(COL_Alias));
                    temp2.Rank = cursor.getString(cursor.getColumnIndex(COL_Rank));
                    temp2.Position = cursor.getString(cursor.getColumnIndex(COL_Position));
                    temp2.SubPossition = cursor.getString(cursor.getColumnIndex(COL_SubPossition));
                    temp2.PhoneNumber = cursor.getString(cursor.getColumnIndex(COL_PhoneNumber));
                    temp2.OfficialEmail = cursor.getString(cursor.getColumnIndex(COL_OfficialEmail));
                    temp2.OfficialDisplayPic = cursor.getString(cursor.getColumnIndex(COL_OfficialDisplayPic));
                    temp2.AccessType = cursor.getString(cursor.getColumnIndex(COL_AccessType));
                    temp2.SCDCAgencyCode = cursor.getString(cursor.getColumnIndex(COL_SCDCAgencyCode));
                    temp2.PoliceStationID = cursor.getString(cursor.getColumnIndex(COL_PoliceStationID));
                    temp2.id_users = cursor.getString(cursor.getColumnIndex(COL_id_users));
                    apiOfficial.setTbOfficial(temp2);
                    // Index tbPoliceStation ดึงจากตาราง policestation ใช้ค่าจาก Index tbNoticeCase
                    TbPoliceStation temp5 = new TbPoliceStation();
                    strSQL = "SELECT * FROM policestation "
                            + " WHERE PoliceStationID = '" + temp2.PoliceStationID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp5.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationID));
                            temp5.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceAgencyID));
                            temp5.PoliceStationName = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationName));
                            apiOfficial.setTbPoliceStation(temp5);
                        } else {
                            apiOfficial.setTbPoliceStation(null);
                        }
                    }

                    // Index tbSCDCagency ดึงจากตาราง scdcagency ใช้ค่าจาก Index tbNoticeCase
                    TbSCDCagency temp10 = new TbSCDCagency();
                    strSQL = "SELECT * FROM scdcagency "
                            + " WHERE SCDCAgencyCode = '" + temp2.SCDCAgencyCode + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp10.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyCode));
                            temp10.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterID));
                            temp10.SCDCAgencyName = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyName));
                            apiOfficial.setTbSCDCagency(temp10);
                        } else {
                            apiOfficial.setTbSCDCagency(null);
                        }
                    }
                    //ส่งค่าทั้งหมดเข้า apiOfficial
                    apiOfficial.setMode("offline");
                    apiOfficialList.add(apiOfficial);
                    Log.d("TEST", "--" + apiOfficial.getTbOfficial().OfficialID);
                }
            }
            db.close();
            // รวมข้อมูลที่ได้ทั้งหมดลง apiOfficial ก่อนส่งกลับไปใช้
            Log.d(TAG, "apiOfficialList:" + apiOfficialList.size());
            for (int i = 0; i < apiOfficialList.size(); i++) {
                Log.d("TEST", "////--" + apiOfficialList.get(i).getTbOfficial().OfficialID);
            }

            dataEntity.setResult(apiOfficialList);
            apiListOfficial.setData(dataEntity);
            return apiListOfficial;
        } catch (Exception e) {
            Log.d(TAG, "Error in selectApiOfficial " + e.getMessage().toString());
            return null;
        }
    }

    public ApiListScheduleInvestigates selectApiScheduleInvestigates(String SCDCAgencyCode) {
        Log.d(TAG, "SCDCAgencyCode:" + SCDCAgencyCode);
        ApiListScheduleInvestigates apiListScheduleInvestigates = new ApiListScheduleInvestigates();
        apiListScheduleInvestigates.setStatus("success");
        ApiListScheduleInvestigates.DataEntity dataEntity = new ApiListScheduleInvestigates().new DataEntity();
        dataEntity.setAction("SQLite selectApiScheduleInvestigates");
        dataEntity.setReason("โหลดรายการคดีสำเร็จ");

        List<ApiScheduleInvestigates> apiScheduleInvestigatesList = new ArrayList<>();

        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL, strSQL_main;
            strSQL_main = "SELECT * FROM scheduleinvestigates "
                    + " WHERE SCDCAgencyCode = '" + SCDCAgencyCode + "'";
//                    + " ORDER BY ScheduleDate ASC";
            try (Cursor cursor = db.rawQuery(strSQL_main, null)) {

                Log.d(TAG, "strSQL_main" + strSQL_main + " " + String.valueOf(cursor.getCount()));
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    ApiScheduleInvestigates apiScheduleInvestigates = new ApiScheduleInvestigates();

                    TbScheduleInvestigates tbScheduleInvestigates = new TbScheduleInvestigates();
                    tbScheduleInvestigates.ScheduleInvestigateID = cursor.getString(cursor.getColumnIndex(COL_ScheduleInvestigateID));
                    tbScheduleInvestigates.ScheduleDate = cursor.getString(cursor.getColumnIndex(COL_ScheduleDate));
                    tbScheduleInvestigates.ScheduleMonth = cursor.getString(cursor.getColumnIndex(COL_ScheduleMonth));
                    tbScheduleInvestigates.SCDCAgencyCode = cursor.getString(cursor.getColumnIndex(COL_SCDCAgencyCode));
                    apiScheduleInvestigates.setTbScheduleInvestigates(tbScheduleInvestigates);
                    Log.d(TAG, "tbScheduleInvestigates.ScheduleInvestigateID" + tbScheduleInvestigates.ScheduleInvestigateID);

                    Log.d(TAG, "apiScheduleInvestigates" + apiScheduleInvestigates.getTbScheduleInvestigates().ScheduleInvestigateID);
                    if (tbScheduleInvestigates.SCDCAgencyCode != null) {
                        // Index tbOfficial ดึงจากตาราง official
                        strSQL = "SELECT * FROM scdcagency "
                                + " WHERE SCDCAgencyCode = '" + tbScheduleInvestigates.SCDCAgencyCode + "'";
                        try (Cursor cursor4 = db.rawQuery(strSQL, null)) {
                            if (cursor4.getCount() == 1) {
                                cursor4.moveToFirst();
                                TbSCDCagency tbSCDCagency = new TbSCDCagency();
                                tbSCDCagency.SCDCAgencyCode = cursor4.getString(cursor4.getColumnIndex(COL_SCDCAgencyCode));
                                tbSCDCagency.SCDCCenterID = cursor4.getString(cursor4.getColumnIndex(COL_SCDCCenterID));
                                tbSCDCagency.SCDCAgencyName = cursor4.getString(cursor4.getColumnIndex(COL_SCDCAgencyName));
                                apiScheduleInvestigates.setTbSCDCagency(tbSCDCagency);
//                                Log.d(TAG, "SCDCAgencyName" + apiScheduleInvestigates.getTbSCDCagency().SCDCAgencyName);
                            }
                        }
                    }
                    if (tbScheduleInvestigates.ScheduleInvestigateID != null) {
                        List<ApiScheduleGroup> apiScheduleGroupList = new ArrayList<>();
                        strSQL = "SELECT * FROM schedulegroup "
                                + " WHERE ScheduleInvestigateID = '" + tbScheduleInvestigates.ScheduleInvestigateID + "'"
                                + " ORDER BY ScheduleGroupID ASC";
                        Log.d(TAG, "schedulegroup / " + strSQL);
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (cursor2.getCount() > 0) {
                                Log.d(TAG, "schedulegroup  " + strSQL);
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    ApiScheduleGroup apiScheduleGroup = new ApiScheduleGroup();
                                    TbScheduleGroup tbScheduleGroup = new TbScheduleGroup();
                                    tbScheduleGroup.ScheduleGroupID = cursor2.getString(cursor2.getColumnIndex(COL_ScheduleGroupID));
                                    tbScheduleGroup.ScheduleInvestigateID = cursor2.getString(cursor2.getColumnIndex(COL_ScheduleInvestigateID));
                                    apiScheduleGroup.setTbScheduleGroup(tbScheduleGroup);
//                                    Log.d(TAG, "ScheduleGroupID " + apiScheduleGroup.getTbScheduleGroup().ScheduleGroupID);

                                    List<ApiScheduleInvInGroup> apiScheduleInvInGroupList = new ArrayList<>();
                                    String strSQL2 = "SELECT * FROM scheduleinvingroup "
                                            + " WHERE ScheduleGroupID = '" + tbScheduleGroup.ScheduleGroupID + "'"
                                            + " ORDER BY InvOfficialID DESC";
//                                    Log.d(TAG, "scheduleinvingroup  " + strSQL2);
                                    try (Cursor cursor3 = db.rawQuery(strSQL2, null)) {
                                        Log.d(TAG, "scheduleinvingroup  " + String.valueOf(cursor3.getCount()));

                                        if (cursor3.getCount() > 0) {
                                            cursor3.moveToPosition(-1);
                                            while (cursor3.moveToNext()) {
                                                ApiScheduleInvInGroup apiScheduleInvInGroup = new ApiScheduleInvInGroup();

                                                TbScheduleInvInGroup tbScheduleInvInGroup = new TbScheduleInvInGroup();
                                                tbScheduleInvInGroup.ScheduleGroupID = cursor3.getString(cursor3.getColumnIndex(COL_ScheduleGroupID));
                                                tbScheduleInvInGroup.InvOfficialID = cursor3.getString(cursor3.getColumnIndex(COL_InvOfficialID));
                                                apiScheduleInvInGroup.setTbScheduleInvInGroup(tbScheduleInvInGroup);

//                                                Log.d(TAG, "InvOfficialID " + apiScheduleInvInGroup.getTbScheduleInvInGroup().InvOfficialID);
                                                if (tbScheduleInvInGroup.InvOfficialID != null) {
                                                    // Index tbOfficial ดึงจากตาราง official
                                                    strSQL = "SELECT * FROM official "
                                                            + " WHERE OfficialID = '" + tbScheduleInvInGroup.InvOfficialID + "'";
                                                    try (Cursor cursor4 = db.rawQuery(strSQL, null)) {
                                                        if (cursor4.getCount() == 1) {
                                                            cursor4.moveToFirst();
                                                            TbOfficial tbOfficial = new TbOfficial();
                                                            tbOfficial.OfficialID = cursor4.getString(cursor4.getColumnIndex(COL_OfficialID));
                                                            tbOfficial.FirstName = cursor4.getString(cursor4.getColumnIndex(COL_FirstName));
                                                            tbOfficial.LastName = cursor4.getString(cursor4.getColumnIndex(COL_LastName));
                                                            tbOfficial.Alias = cursor4.getString(cursor4.getColumnIndex(COL_Alias));
                                                            tbOfficial.Rank = cursor4.getString(cursor4.getColumnIndex(COL_Rank));
                                                            tbOfficial.Position = cursor4.getString(cursor4.getColumnIndex(COL_Position));
                                                            tbOfficial.SubPossition = cursor4.getString(cursor4.getColumnIndex(COL_SubPossition));
                                                            tbOfficial.PhoneNumber = cursor4.getString(cursor4.getColumnIndex(COL_PhoneNumber));
                                                            tbOfficial.OfficialEmail = cursor4.getString(cursor4.getColumnIndex(COL_OfficialEmail));
                                                            tbOfficial.OfficialDisplayPic = cursor4.getString(cursor4.getColumnIndex(COL_OfficialDisplayPic));
                                                            tbOfficial.AccessType = cursor4.getString(cursor4.getColumnIndex(COL_AccessType));
                                                            tbOfficial.SCDCAgencyCode = cursor4.getString(cursor4.getColumnIndex(COL_SCDCAgencyCode));
                                                            tbOfficial.PoliceStationID = cursor4.getString(cursor4.getColumnIndex(COL_PoliceStationID));
                                                            tbOfficial.id_users = cursor4.getString(cursor4.getColumnIndex(COL_id_users));
                                                            apiScheduleInvInGroup.setTbOfficial(tbOfficial);
                                                        }
                                                    }
                                                }
                                                apiScheduleInvInGroupList.add(apiScheduleInvInGroup);
                                            }
                                        }
                                    }
                                    apiScheduleGroup.setApiScheduleInvInGroup(apiScheduleInvInGroupList);
                                    apiScheduleGroupList.add(apiScheduleGroup);
                                    apiScheduleInvestigates.setApiScheduleGroup(apiScheduleGroupList);

                                }
                            }
                        }
                    }
                    apiScheduleInvestigates.setMode("offline");
                    apiScheduleInvestigatesList.add(apiScheduleInvestigates);
                }
            }
            db.close();
//            // รวมข้อมูลที่ได้ทั้งหมดลง apiScheduleInvestigatesList ก่อนส่งกลับไปใช้
//            Log.d(TAG, "apiScheduleInvestigatesList:" + apiScheduleInvestigatesList.size());
//            for (int i = 0; i < apiScheduleInvestigatesList.size(); i++) {
//                Log.d(TAG, "////--" + apiScheduleInvestigatesList.get(i).getTbScheduleInvestigates().ScheduleInvestigateID);
//            }
            dataEntity.setResult(apiScheduleInvestigatesList);
            apiListScheduleInvestigates.setData(dataEntity);
            return apiListScheduleInvestigates;

        } catch (Exception e) {
            Log.d(TAG, "Error in selectApiScheduleInvestigates " + e.getMessage().toString());
            return null;
        }
    }

    public boolean syncApiScheduleInvestigates(List<ApiScheduleInvestigates> apiScheduleInvestigates) {
        if (apiScheduleInvestigates.size() == 0) {
            Log.d(TAG, "apiScheduleInvestigates =  0");
            return false;
        }
        try {
            mDb = this.getReadableDatabase();
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            int size = apiScheduleInvestigates.size();
            int insert = 0;
            int update = 0;
            String PRIMARY_KEY;
            String strSQL;
            db.beginTransaction();
            Log.d(TAG, "apiScheduleInvestigates =  " + String.valueOf(size));
            for (int i = 0; i < size; i++) {
                PRIMARY_KEY = apiScheduleInvestigates.get(i).getTbScheduleInvestigates().ScheduleInvestigateID;
                strSQL = "SELECT * FROM scheduleinvestigates WHERE "
                        + "ScheduleInvestigateID = '" + PRIMARY_KEY + "'" +
                        " ORDER BY ScheduleDate ASC";
                Cursor cursor = mDb.rawQuery(strSQL, null);
                Log.d(TAG, strSQL + " / scheduleinvestigates getCount=  " + String.valueOf(cursor.getCount()));
                ContentValues Val = new ContentValues();
                Val.put(COL_ScheduleInvestigateID, apiScheduleInvestigates.get(i).getTbScheduleInvestigates().ScheduleInvestigateID);
                Val.put(COL_ScheduleDate, apiScheduleInvestigates.get(i).getTbScheduleInvestigates().ScheduleDate);
                Val.put(COL_ScheduleMonth, apiScheduleInvestigates.get(i).getTbScheduleInvestigates().ScheduleMonth);
                Val.put(COL_SCDCAgencyCode, apiScheduleInvestigates.get(i).getTbScheduleInvestigates().SCDCAgencyCode);

                if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                    db.insert("scheduleinvestigates", null, Val);
                    insert++;
                } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                    // ต้องตรวจสอบก่อนว่าข้อมูลที่ได้มานั้นใหม่กว่าที่อยู่ใน SQLite จริงๆ
                    db.update("scheduleinvestigates", Val, " SCDCAgencyCode = ?", new String[]{String.valueOf(PRIMARY_KEY)});
                    update++;
                }
//                Log.d(TAG, "Sync Table scheduleinvestigates: Insert " + insert + ", Update " + update);
                cursor.close();
                if (apiScheduleInvestigates.get(i).getApiScheduleGroup() != null) {
                    int sizeApiScheduleGroup = apiScheduleInvestigates.get(i).getApiScheduleGroup().size();
                    int insert2 = 0;
                    int update2 = 0;
                    for (int j = 0; j < sizeApiScheduleGroup; j++) {
//
                        String sScheduleInvestigateID = apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getTbScheduleGroup().ScheduleInvestigateID;
                        strSQL = "SELECT * FROM schedulegroup "
                                + " WHERE ScheduleInvestigateID = '" + sScheduleInvestigateID + "'"
                                + " ORDER BY ScheduleGroupID ASC";
                        Cursor cursor2 = mDb.rawQuery(strSQL, null);
                        ContentValues Val2 = new ContentValues();
                        Val2.put(COL_ScheduleGroupID, apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getTbScheduleGroup().ScheduleGroupID);
                        Val2.put(COL_ScheduleInvestigateID, apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getTbScheduleGroup().ScheduleInvestigateID);
                        if (cursor2.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("schedulegroup", null, Val2);
                            insert2++;
                        } else if (cursor2.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            // ต้องตรวจสอบก่อนว่าข้อมูลที่ได้มานั้นใหม่กว่าที่อยู่ใน SQLite จริงๆ
                            db.update("schedulegroup", Val2, " ScheduleInvestigateID = ?", new String[]{sScheduleInvestigateID});
                            update2++;
                        }
//                        Log.d(TAG, "Sync Table schedulegroup: Insert " + insert2 + ", Update " + update2);
                        cursor2.close();
                        if (apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup() != null) {
                            int sizeApiScheduleInvInGroup = apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().size();
                            int insert3 = 0;
                            int update3 = 0;
//                            Log.d(TAG, apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getTbScheduleGroup().ScheduleGroupID
//                                    + " sizeApiScheduleInvInGroup" + String.valueOf(sizeApiScheduleInvInGroup));
                            for (int k = 0; k < sizeApiScheduleInvInGroup; k++) {
                                String sScheduleGroupID = apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().ScheduleGroupID;
                                String sInvOfficialID = apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().InvOfficialID;
                                strSQL = "SELECT * FROM scheduleinvingroup "
                                        + " WHERE InvOfficialID = '" + sInvOfficialID + "' " +
                                        "AND ScheduleGroupID = '" + sScheduleGroupID + "'";
                                Cursor cursor3 = mDb.rawQuery(strSQL, null);
                                ContentValues Val3 = new ContentValues();
                                Val3.put(COL_ScheduleGroupID, sScheduleGroupID);
                                Val3.put(COL_InvOfficialID, sInvOfficialID);
//                                Log.d(TAG, apiScheduleInvestigates.get(i).getApiScheduleGroup().get(j).getApiScheduleInvInGroup().get(k).getTbScheduleInvInGroup().InvOfficialID);
                                if (cursor3.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                                    db.insert("scheduleinvingroup", null, Val3);
                                    Log.d(TAG, "insert Table scheduleinvingroup: " + sInvOfficialID);
                                    insert3++;
                                } else if (cursor3.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                                    // ต้องตรวจสอบก่อนว่าข้อมูลที่ได้มานั้นใหม่กว่าที่อยู่ใน SQLite จริงๆ
                                    db.update("scheduleinvingroup", Val3, " InvOfficialID = ? AND ScheduleGroupID = ?", new String[]{sInvOfficialID, sScheduleGroupID});
//                                    Log.d(TAG, "update Table scheduleinvingroup: sInvOfficialID " + sInvOfficialID +" ScheduleGroupID "+sScheduleGroupID);
                                    update3++;
                                }
//                                Log.d(TAG, "Sync Table scheduleinvingroup: Insert " + insert3 + ", Update " + update3);
                                cursor3.close();
                            }
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncApiScheduleInvestigates " + e.getMessage().toString());
            return false;
        }
    }

    public ApiListCaseScene selectApiCaseScene(String OfficeID) {
        Log.d(TAG, "OfficeID:" + OfficeID);
        ApiListCaseScene apiListCaseScene = new ApiListCaseScene();
        apiListCaseScene.setStatus("success");
        ApiListCaseScene.DataEntity dataEntity = new ApiListCaseScene().new DataEntity();
        dataEntity.setAction("SQLite selectApiCaseScene");
        dataEntity.setReason("โหลดรายการคดีสำเร็จ");

        List<ApiCaseScene> apiCaseScenesCases = new ArrayList<>();

        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL, strSQL_main;

            strSQL_main = "SELECT * FROM casescene "
                    + " WHERE InvestigatorOfficialID = '" + OfficeID + "'" +
                    " ORDER BY ReceivingCaseDate DESC, ReceivingCaseTime DESC," +
                    " LastUpdateDate DESC, LastUpdateTime DESC";

            try (Cursor cursor = db.rawQuery(strSQL_main, null)) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    ApiCaseScene apiCaseSceneCase = new ApiCaseScene();

                    TbCaseScene temp = new TbCaseScene();
                    temp.CaseReportID = cursor.getString(cursor.getColumnIndex(COL_CaseReportID));
                    temp.NoticeCaseID = cursor.getString(cursor.getColumnIndex(COL_NoticeCaseID));
                    temp.Mobile_CaseID = cursor.getString(cursor.getColumnIndex(COL_Mobile_CaseID));
                    temp.SCDCAgencyCode = cursor.getString(cursor.getColumnIndex(COL_SCDCAgencyCode));
                    temp.InvestigatorOfficialID = cursor.getString(cursor.getColumnIndex(COL_InvestigatorOfficialID));
                    temp.CaseTypeID = cursor.getString(cursor.getColumnIndex(COL_CaseTypeID));
                    temp.SubCaseTypeID = cursor.getString(cursor.getColumnIndex(COL_SubCaseTypeID));
                    temp.ReportNo = cursor.getString(cursor.getColumnIndex(COL_ReportNo));
                    temp.ReportStatus = cursor.getString(cursor.getColumnIndex(COL_ReportStatus));
                    temp.PoliceStationID = cursor.getString(cursor.getColumnIndex(COL_PoliceStationID));
                    temp.CaseTel = cursor.getString(cursor.getColumnIndex(COL_CaseTel));
                    temp.AssignmentDate = cursor.getString(cursor.getColumnIndex(COL_AssignmentDate));
                    temp.AssignmentTime = cursor.getString(cursor.getColumnIndex(COL_AssignmentTime));
                    temp.ReceivingCaseDate = cursor.getString(cursor.getColumnIndex(COL_ReceivingCaseDate));
                    temp.ReceivingCaseTime = cursor.getString(cursor.getColumnIndex(COL_ReceivingCaseTime));
                    temp.HappenCaseDate = cursor.getString(cursor.getColumnIndex(COL_HappenCaseDate));
                    temp.HappenCaseTime = cursor.getString(cursor.getColumnIndex(COL_HappenCaseTime));
                    temp.KnowCaseDate = cursor.getString(cursor.getColumnIndex(COL_KnowCaseDate));
                    temp.KnowCaseTime = cursor.getString(cursor.getColumnIndex(COL_KnowCaseTime));
                    temp.CompleteSceneDate = cursor.getString(cursor.getColumnIndex(COL_CompleteSceneDate));
                    temp.CompleteSceneTime = cursor.getString(cursor.getColumnIndex(COL_CompleteSceneTime));
                    temp.LocaleName = cursor.getString(cursor.getColumnIndex(COL_LocaleName));
                    temp.DISTRICT_ID = cursor.getString(cursor.getColumnIndex(COL_DISTRICT_ID));
                    temp.AMPHUR_ID = cursor.getString(cursor.getColumnIndex(COL_AMPHUR_ID));
                    temp.PROVINCE_ID = cursor.getString(cursor.getColumnIndex(COL_PROVINCE_ID));
                    temp.Latitude = cursor.getString(cursor.getColumnIndex(COL_Latitude));
                    temp.Longitude = cursor.getString(cursor.getColumnIndex(COL_Longitude));
                    temp.FeatureInsideDetail = cursor.getString(cursor.getColumnIndex(COL_FeatureInsideDetail));
                    temp.CircumstanceOfCaseDetail = cursor.getString(cursor.getColumnIndex(COL_CircumstanceOfCaseDetail));
                    temp.FullEvidencePerformed = cursor.getString(cursor.getColumnIndex(COL_FullEvidencePerformed));
                    temp.Annotation = cursor.getString(cursor.getColumnIndex(COL_Annotation));
                    temp.MaleCriminalNum = cursor.getString(cursor.getColumnIndex(COL_MaleCriminalNum));
                    temp.FemaleCriminalNum = cursor.getString(cursor.getColumnIndex(COL_FemaleCriminalNum));
                    temp.ConfineSufferer = cursor.getString(cursor.getColumnIndex(COL_ConfineSufferer));
                    temp.SuffererPrename = cursor.getString(cursor.getColumnIndex(COL_SuffererPrename));
                    temp.SuffererName = cursor.getString(cursor.getColumnIndex(COL_SuffererName));
                    temp.SuffererStatus = cursor.getString(cursor.getColumnIndex(COL_SuffererStatus));
                    temp.SuffererPhoneNum = cursor.getString(cursor.getColumnIndex(COL_SuffererPhoneNum));
                    temp.CriminalUsedWeapon = cursor.getString(cursor.getColumnIndex(COL_CriminalUsedWeapon));
                    temp.VehicleInfo = cursor.getString(cursor.getColumnIndex(COL_VehicleInfo));
                    temp.LastUpdateDate = cursor.getString(cursor.getColumnIndex(COL_LastUpdateDate));
                    temp.LastUpdateTime = cursor.getString(cursor.getColumnIndex(COL_LastUpdateTime));
                    apiCaseSceneCase.setTbCaseScene(temp);
                    Log.d(TAG, "NoticeCaseID " + temp.NoticeCaseID.toString());
                    // Index TbCaseScene ดึงจากตาราง noticecase
                    strSQL = "SELECT * FROM noticecase "
                            + " WHERE NoticeCaseID = '" + temp.NoticeCaseID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbNoticeCase temp2 = new TbNoticeCase();
                            temp2.NoticeCaseID = cursor2.getString(cursor2.getColumnIndex(COL_NoticeCaseID));
                            temp2.Mobile_CaseID = cursor2.getString(cursor2.getColumnIndex(COL_Mobile_CaseID));
                            temp2.InquiryOfficialID = cursor2.getString(cursor2.getColumnIndex(COL_InquiryOfficialID));
                            temp2.InvestigatorOfficialID = cursor2.getString(cursor2.getColumnIndex(COL_InvestigatorOfficialID));
                            temp2.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyCode));
                            temp2.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_CaseTypeID));
                            temp2.SubCaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_SubCaseTypeID));
                            temp2.CaseStatus = cursor2.getString(cursor2.getColumnIndex(COL_CaseStatus));
                            temp2.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationID));
                            temp2.CaseTel = cursor2.getString(cursor2.getColumnIndex(COL_CaseTel));
                            temp2.ReceivingCaseDate = cursor2.getString(cursor2.getColumnIndex(COL_ReceivingCaseDate));
                            temp2.ReceivingCaseTime = cursor2.getString(cursor2.getColumnIndex(COL_ReceivingCaseTime));
                            temp2.HappenCaseDate = cursor2.getString(cursor2.getColumnIndex(COL_HappenCaseDate));
                            temp2.HappenCaseTime = cursor2.getString(cursor2.getColumnIndex(COL_HappenCaseTime));
                            temp2.KnowCaseDate = cursor2.getString(cursor2.getColumnIndex(COL_KnowCaseDate));
                            temp2.KnowCaseTime = cursor2.getString(cursor2.getColumnIndex(COL_KnowCaseTime));
                            temp2.SceneNoticeDate = cursor2.getString(cursor2.getColumnIndex(COL_SceneNoticeDate));
                            temp2.SceneNoticeTime = cursor2.getString(cursor2.getColumnIndex(COL_SceneNoticeTime));
                            temp2.CompleteSceneDate = cursor2.getString(cursor2.getColumnIndex(COL_CompleteSceneDate));
                            temp2.CompleteSceneTime = cursor2.getString(cursor2.getColumnIndex(COL_CompleteSceneTime));
                            temp2.LocaleName = cursor2.getString(cursor2.getColumnIndex(COL_LocaleName));
                            temp2.DISTRICT_ID = cursor2.getString(cursor2.getColumnIndex(COL_DISTRICT_ID));
                            temp2.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_ID));
                            temp2.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_ID));
                            temp2.Latitude = cursor2.getString(cursor2.getColumnIndex(COL_Latitude));
                            temp2.Longitude = cursor2.getString(cursor2.getColumnIndex(COL_Longitude));
                            temp2.SuffererPrename = cursor2.getString(cursor2.getColumnIndex(COL_SuffererPrename));
                            temp2.SuffererName = cursor2.getString(cursor2.getColumnIndex(COL_SuffererName));
                            temp2.SuffererStatus = cursor2.getString(cursor2.getColumnIndex(COL_SuffererStatus));
                            temp2.SuffererPhoneNum = cursor2.getString(cursor2.getColumnIndex(COL_SuffererPhoneNum));
                            temp2.CircumstanceOfCaseDetail = cursor2.getString(cursor2.getColumnIndex(COL_CircumstanceOfCaseDetail));
                            temp2.LastUpdateDate = cursor2.getString(cursor2.getColumnIndex(COL_LastUpdateDate));
                            temp2.LastUpdateTime = cursor2.getString(cursor2.getColumnIndex(COL_LastUpdateTime));
                            apiCaseSceneCase.setTbNoticeCase(temp2);
                        }
                    }

                    // Index tbOfficial ดึงจากตาราง official
                    strSQL = "SELECT * FROM official "
                            + " WHERE OfficialID = '" + apiCaseSceneCase.getTbNoticeCase().InquiryOfficialID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbOfficial temp2 = new TbOfficial();
                            temp2.OfficialID = cursor2.getString(cursor2.getColumnIndex(COL_OfficialID));
                            temp2.FirstName = cursor2.getString(cursor2.getColumnIndex(COL_FirstName));
                            temp2.LastName = cursor2.getString(cursor2.getColumnIndex(COL_LastName));
                            temp2.Alias = cursor2.getString(cursor2.getColumnIndex(COL_Alias));
                            temp2.Rank = cursor2.getString(cursor2.getColumnIndex(COL_Rank));
                            temp2.Position = cursor2.getString(cursor2.getColumnIndex(COL_Position));
                            temp2.SubPossition = cursor2.getString(cursor2.getColumnIndex(COL_SubPossition));
                            temp2.PhoneNumber = cursor2.getString(cursor2.getColumnIndex(COL_PhoneNumber));
                            temp2.OfficialEmail = cursor2.getString(cursor2.getColumnIndex(COL_OfficialEmail));
                            temp2.OfficialDisplayPic = cursor2.getString(cursor2.getColumnIndex(COL_OfficialDisplayPic));
                            temp2.AccessType = cursor2.getString(cursor2.getColumnIndex(COL_AccessType));
                            temp2.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyCode));
                            temp2.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationID));
                            temp2.id_users = cursor2.getString(cursor2.getColumnIndex(COL_id_users));
                            apiCaseSceneCase.setTbOfficial(temp2);
                        }
                    }

                    // Index tbSubcaseSceneType ดึงจากตาราง subcasescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM subcasescenetype "
                            + " WHERE SubCaseTypeID = '" + temp.SubCaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbSubcaseSceneType temp4 = new TbSubcaseSceneType();
                            temp4.SubCaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_SubCaseTypeID));
                            temp4.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_CaseTypeID));
                            temp4.SubCaseTypeName = cursor2.getString(cursor2.getColumnIndex(COL_SubCaseTypeName));
                            apiCaseSceneCase.setTbSubcaseSceneType(temp4);
                        }
                    }
                    // Index tbCaseSceneType ดึงจากตาราง casescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM casescenetype "
                            + " WHERE CaseTypeID = '" + apiCaseSceneCase.getTbSubcaseSceneType().CaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();

                            TbCaseSceneType temp3 = new TbCaseSceneType();
                            temp3.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(COL_CaseTypeID));
                            temp3.CaseTypeName = cursor2.getString(cursor2.getColumnIndex(COL_CaseTypeName));
                            temp3.casetype_min = cursor2.getString(cursor2.getColumnIndex(COL_casetype_min));
                            temp3.casetype_max = cursor2.getString(cursor2.getColumnIndex(COL_casetype_max));
                            temp3.casetype_icon = cursor2.getString(cursor2.getColumnIndex(COL_casetype_icon));
                            temp3.casetype_colormin = cursor2.getString(cursor2.getColumnIndex(COL_casetype_colormin));
                            temp3.casetype_colormedium = cursor2.getString(cursor2.getColumnIndex(COL_casetype_colormedium));
                            temp3.casetype_colorhigh = cursor2.getString(cursor2.getColumnIndex(COL_casetype_colorhigh));
                            temp3.casetype_status = cursor2.getString(cursor2.getColumnIndex(COL_casetype_status));
                            apiCaseSceneCase.setTbCaseSceneType(temp3);
                        }
                    }
                    // Index tbPoliceStation ดึงจากตาราง policestation ใช้ค่าจาก Index tbNoticeCase
                    TbPoliceStation temp5 = new TbPoliceStation();
                    strSQL = "SELECT * FROM policestation "
                            + " WHERE PoliceStationID = '" + temp.PoliceStationID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp5.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationID));
                            temp5.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceAgencyID));
                            temp5.PoliceStationName = cursor2.getString(cursor2.getColumnIndex(COL_PoliceStationName));
                            apiCaseSceneCase.setTbPoliceStation(temp5);
                        }
                    }

                    // Index tbPoliceAgency ดึงจากตาราง policeagency ใช้ค่าจาก Index tbPoliceStation
                    if (temp5.PoliceStationID != null) {
                        strSQL = "SELECT * FROM policeagency "
                                + " WHERE PoliceAgencyID = '" + temp5.PoliceStationID + "'";
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (cursor2.getCount() == 1) {
                                cursor2.moveToFirst();
                                TbPoliceAgency temp6 = new TbPoliceAgency();
                                temp6.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceAgencyID));
                                temp6.PoliceCenterID = cursor2.getString(cursor2.getColumnIndex(COL_PoliceCenterID));
                                temp6.PoliceAgencyName = cursor2.getString(cursor2.getColumnIndex(COL_PoliceAgencyName));
                                apiCaseSceneCase.setTbPoliceAgency(temp6);
                            }
                        }
                    }

                    // Index tbDistrict ดึงจากตาราง district ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM district "
                            + " WHERE DISTRICT_ID = '" + temp.DISTRICT_ID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbDistrict temp7 = new TbDistrict();
                            temp7.DISTRICT_ID = cursor2.getString(cursor2.getColumnIndex(COL_DISTRICT_ID));
                            temp7.DISTRICT_CODE = cursor2.getString(cursor2.getColumnIndex(COL_DISTRICT_CODE));
                            temp7.DISTRICT_NAME = cursor2.getString(cursor2.getColumnIndex(COL_DISTRICT_NAME));
                            temp7.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_ID));
                            apiCaseSceneCase.setTbDistrict(temp7);
                        }
                    }

                    // Index tbProvince ดึงจากตาราง province ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM province "
                            + " WHERE PROVINCE_ID = '" + temp.PROVINCE_ID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbProvince temp8 = new TbProvince();
                            temp8.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_ID));
                            temp8.PROVINCE_CODE = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_CODE));
                            temp8.PROVINCE_NAME = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_NAME));
                            temp8.GEO_ID = cursor2.getString(cursor2.getColumnIndex(COL_GEO_ID));
                            temp8.province_status = cursor2.getString(cursor2.getColumnIndex(COL_province_status));
                            apiCaseSceneCase.setTbProvince(temp8);
                        }
                    }

                    // Index tbAmphur ดึงจากตาราง amphur ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM amphur "
                            + " WHERE AMPHUR_ID = '" + temp.AMPHUR_ID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbAmphur temp9 = new TbAmphur();
                            temp9.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_ID));
                            temp9.AMPHUR_CODE = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_CODE));
                            temp9.AMPHUR_NAME = cursor2.getString(cursor2.getColumnIndex(COL_AMPHUR_NAME));
                            temp9.POSTCODE = cursor2.getString(cursor2.getColumnIndex(COL_POSTCODE));
                            temp9.GEO_ID = cursor2.getString(cursor2.getColumnIndex(COL_GEO_ID));
                            temp9.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(COL_PROVINCE_ID));
                            temp9.amphur_polygon = cursor2.getString(cursor2.getColumnIndex(COL_amphur_polygon));
                            apiCaseSceneCase.setTbAmphur(temp9);
                        }
                    }

                    // Index tbSCDCagency ดึงจากตาราง scdcagency ใช้ค่าจาก Index tbNoticeCase
                    TbSCDCagency temp10 = new TbSCDCagency();
                    strSQL = "SELECT * FROM scdcagency "
                            + " WHERE SCDCAgencyCode = '" + temp.SCDCAgencyCode + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp10.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyCode));
                            temp10.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterID));
                            temp10.SCDCAgencyName = cursor2.getString(cursor2.getColumnIndex(COL_SCDCAgencyName));
                            apiCaseSceneCase.setTbSCDCagency(temp10);
                        }
                    }

                    // Index tbSCDCcenter ดึงจากตาราง scdccenter ใช้ค่าจาก Index tbSCDCagency
                    if (temp10.SCDCCenterID != null) {
                        strSQL = "SELECT * FROM scdccenter "
                                + " WHERE SCDCCenterID = '" + temp10.SCDCCenterID + "'";
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (cursor2.getCount() == 1) {
                                cursor2.moveToFirst();
                                TbSCDCcenter temp11 = new TbSCDCcenter();
                                temp11.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterID));
                                temp11.SCDCCenterName = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterName));
                                temp11.SCDCCenterProvince = cursor2.getString(cursor2.getColumnIndex(COL_SCDCCenterProvince));
                                apiCaseSceneCase.setTbSCDCcenter(temp11);
                            }
                        }
                    }
                    // Index tbSceneInvestigations ดึงจากตาราง sceneinvestigation ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM sceneinvestigation "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        List<TbSceneInvestigation> tbSceneInvestigations = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbSceneInvestigations == null) {
                                tbSceneInvestigations = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbSceneInvestigation temp12 = new TbSceneInvestigation();
                                    temp12.SceneInvestID = cursor2.getString(cursor2.getColumnIndex(COL_SceneInvestID));
                                    temp12.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp12.SceneInvestDate = cursor2.getString(cursor2.getColumnIndex(COL_SceneInvestDate));
                                    temp12.SceneInvestTime = cursor2.getString(cursor2.getColumnIndex(COL_SceneInvestTime));
                                    tbSceneInvestigations.add(temp12);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbSceneInvestigations(tbSceneInvestigations);

                    }
                    // Index tbSceneFeatureOutside ดึงจากตาราง scenefeatureoutside ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM scenefeatureoutside "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (cursor2.getCount() == 1) {
                                cursor2.moveToFirst();
                                TbSceneFeatureOutside temp13 = new TbSceneFeatureOutside();
                                temp13.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                temp13.OutsideTypeName = cursor2.getString(cursor2.getColumnIndex(COL_OutsideTypeName));
                                temp13.OutsideTypeDetail = cursor2.getString(cursor2.getColumnIndex(COL_OutsideTypeDetail));
                                temp13.FloorNum = cursor2.getString(cursor2.getColumnIndex(COL_FloorNum));
                                temp13.CaveNum = cursor2.getString(cursor2.getColumnIndex(COL_CaveNum));
                                temp13.HaveFence = cursor2.getString(cursor2.getColumnIndex(COL_HaveFence));
                                temp13.HaveMezzanine = cursor2.getString(cursor2.getColumnIndex(COL_HaveMezzanine));
                                temp13.HaveRooftop = cursor2.getString(cursor2.getColumnIndex(COL_HaveRooftop));
                                temp13.FrontSide = cursor2.getString(cursor2.getColumnIndex(COL_FrontSide));
                                temp13.LeftSide = cursor2.getString(cursor2.getColumnIndex(COL_LeftSide));
                                temp13.RightSide = cursor2.getString(cursor2.getColumnIndex(COL_RightSide));
                                temp13.BackSide = cursor2.getString(cursor2.getColumnIndex(COL_BackSide));
                                temp13.SceneZone = cursor2.getString(cursor2.getColumnIndex(COL_SceneZone));
                                apiCaseSceneCase.setTbSceneFeatureOutside(temp13);
                            }
                        }
                    }
                    // Index tbSceneFeatureInSide ดึงจากตาราง scenefeatureinside ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM scenefeatureinside "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        List<TbSceneFeatureInSide> tbSceneFeatureInSides = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbSceneFeatureInSides == null) {
                                tbSceneFeatureInSides = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbSceneFeatureInSide temp14 = new TbSceneFeatureInSide();
                                    temp14.FeatureInsideID = cursor2.getString(cursor2.getColumnIndex(COL_FeatureInsideID));
                                    temp14.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp14.FloorNo = cursor2.getString(cursor2.getColumnIndex(COL_FloorNo));
                                    temp14.CaveNo = cursor2.getString(cursor2.getColumnIndex(COL_CaveNo));
                                    temp14.FrontInside = cursor2.getString(cursor2.getColumnIndex(COL_FrontInside));
                                    temp14.LeftInside = cursor2.getString(cursor2.getColumnIndex(COL_LeftInside));
                                    temp14.RightInside = cursor2.getString(cursor2.getColumnIndex(COL_RightInside));
                                    temp14.BackInside = cursor2.getString(cursor2.getColumnIndex(COL_BackInside));
                                    temp14.CenterInside = cursor2.getString(cursor2.getColumnIndex(COL_CenterInside));
                                    tbSceneFeatureInSides.add(temp14);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbSceneFeatureInSide(tbSceneFeatureInSides);

                    }
                    //ApiInvestigatorsInScene
                    if (temp.CaseReportID != null) {
                        List<ApiInvestigatorsInScene> apiInvestigatorsInScenes = new ArrayList<>();
                        strSQL = "SELECT * FROM investigatorsinscene "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        Log.i(TAG, strSQL);
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {

                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    ApiInvestigatorsInScene apiInvestigatorsInScene = new ApiInvestigatorsInScene();

                                    TbInvestigatorsInScene tbInvestigatorsInScene = new TbInvestigatorsInScene();
                                    tbInvestigatorsInScene.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    tbInvestigatorsInScene.InvOfficialID = cursor2.getString(cursor2.getColumnIndex(COL_InvOfficialID));
                                    tbInvestigatorsInScene.InvType = cursor2.getString(cursor2.getColumnIndex(COL_InvType));
                                    apiInvestigatorsInScene.setTbInvestigatorsInScene(tbInvestigatorsInScene);
                                    if (tbInvestigatorsInScene.InvOfficialID != null) {
                                        strSQL = "SELECT * FROM " + TB_photoofoutside
                                                + " WHERE " + COL_FileID + " = '" + tbInvestigatorsInScene.InvOfficialID + "'";

                                        try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                            if (cursor3.getCount() == 1) {
                                                cursor3.moveToFirst();
                                                TbOfficial tbOfficial = new TbOfficial();
                                                tbOfficial.OfficialID = cursor3.getString(cursor3.getColumnIndex(COL_OfficialID));
                                                tbOfficial.FirstName = cursor3.getString(cursor3.getColumnIndex(COL_FirstName));
                                                tbOfficial.LastName = cursor3.getString(cursor3.getColumnIndex(COL_LastName));
                                                tbOfficial.Alias = cursor3.getString(cursor3.getColumnIndex(COL_Alias));
                                                tbOfficial.Rank = cursor3.getString(cursor3.getColumnIndex(COL_Rank));
                                                tbOfficial.Position = cursor3.getString(cursor3.getColumnIndex(COL_Position));
                                                tbOfficial.SubPossition = cursor3.getString(cursor3.getColumnIndex(COL_SubPossition));
                                                tbOfficial.PhoneNumber = cursor3.getString(cursor3.getColumnIndex(COL_PhoneNumber));
                                                tbOfficial.OfficialEmail = cursor3.getString(cursor3.getColumnIndex(COL_OfficialEmail));
                                                tbOfficial.OfficialDisplayPic = cursor3.getString(cursor3.getColumnIndex(COL_OfficialDisplayPic));
                                                tbOfficial.AccessType = cursor3.getString(cursor3.getColumnIndex(COL_AccessType));
                                                tbOfficial.SCDCAgencyCode = cursor3.getString(cursor3.getColumnIndex(COL_SCDCAgencyCode));
                                                tbOfficial.PoliceStationID = cursor3.getString(cursor3.getColumnIndex(COL_PoliceStationID));
                                                tbOfficial.id_users = cursor3.getString(cursor3.getColumnIndex(COL_id_users));
                                                apiInvestigatorsInScene.setTbOfficial(tbOfficial);

                                                Log.i(TAG, "tbOfficial " + tbOfficial.FirstName);
                                            } else {
                                                apiInvestigatorsInScene.setTbOfficial(null);
                                            }
                                        }
                                    }
                                    apiInvestigatorsInScenes.add(apiInvestigatorsInScene);
                                }
                            }
                        }
                        apiCaseSceneCase.setApiInvestigatorsInScenes(apiInvestigatorsInScenes);
                        Log.i(TAG, "apiInvestigatorsInScenes :" + String.valueOf(apiCaseSceneCase.getApiInvestigatorsInScenes().size()));
                        for (int i = 0; i < apiCaseSceneCase.getApiInvestigatorsInScenes().size(); i++) {
                            Log.i(TAG, apiCaseSceneCase.getApiInvestigatorsInScenes().get(i).getTbInvestigatorsInScene().InvOfficialID);
                        }
                    }
                    //  ApiMultimedia
                    if (temp.CaseReportID != null) {
                        List<ApiMultimedia> apiMultimediaList = new ArrayList<>();

                        strSQL = "SELECT * FROM multimediafile "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        Log.i(TAG, strSQL);
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {

                            Log.i(TAG, "multimediafile " + String.valueOf(cursor2.getCount()));
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    ApiMultimedia apiMultimedia = new ApiMultimedia();

                                    TbMultimediaFile temp15 = new TbMultimediaFile();
                                    temp15.FileID = cursor2.getString(cursor2.getColumnIndex(COL_FileID));
                                    temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp15.FileType = cursor2.getString(cursor2.getColumnIndex(COL_FileType));
                                    temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(COL_FilePath));
                                    temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(COL_FileDescription));
                                    temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(COL_Timestamp));
                                    apiMultimedia.setTbMultimediaFile(temp15);
                                    Log.i(TAG, "multimediafile " + String.valueOf(apiMultimedia.getTbMultimediaFile().FileID));
                                    if (temp15.FileID != null) {
                                        strSQL = "SELECT * FROM " + TB_photoofoutside
                                                + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                                        try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                            if (cursor3.getCount() == 1) {
                                                cursor3.moveToFirst();
                                                TbPhotoOfOutside tbPhotoOfOutside = new TbPhotoOfOutside();
                                                tbPhotoOfOutside.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                                tbPhotoOfOutside.CaseReportID = cursor3.getString(cursor3.getColumnIndex(COL_CaseReportID));
                                                apiMultimedia.setTbPhotoOfOutside(tbPhotoOfOutside);

                                                Log.i(TAG, "tbPhotoOfOutside " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfOutside().FileID));
                                            } else {
                                                apiMultimedia.setTbPhotoOfOutside(null);
                                            }
                                        }
                                    }
                                    if (temp15.FileID != null) {
                                        strSQL = "SELECT * FROM " + TB_photoofinside
                                                + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                                        try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                            if (cursor3.getCount() == 1) {
                                                cursor3.moveToFirst();
                                                TbPhotoOfInside tbPhotoOfInside = new TbPhotoOfInside();
                                                tbPhotoOfInside.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                                tbPhotoOfInside.FeatureInsideID = cursor3.getString(cursor3.getColumnIndex(COL_FeatureInsideID));
                                                apiMultimedia.setTbPhotoOfInside(tbPhotoOfInside);
                                                Log.i(TAG, "tbPhotoOfInside " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfInside().FileID));
                                            } else {
                                                apiMultimedia.setTbPhotoOfInside(null);
                                            }
                                        }

                                    }
                                    if (temp15.FileID != null) {
                                        strSQL = "SELECT * FROM " + TB_photoofevidence
                                                + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                                        try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                            if (cursor3.getCount() == 1) {
                                                cursor3.moveToFirst();
                                                TbPhotoOfEvidence tbPhotoOfEvidence = new TbPhotoOfEvidence();
                                                tbPhotoOfEvidence.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                                tbPhotoOfEvidence.FindEvidenceID = cursor3.getString(cursor3.getColumnIndex(COL_FindEvidenceID));
                                                apiMultimedia.setTbPhotoOfEvidence(tbPhotoOfEvidence);
                                                Log.i(TAG, "tbPhotoOfEvidence " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfEvidence().FileID));
                                            } else {
                                                apiMultimedia.setTbPhotoOfEvidence(null);
                                            }
                                        }

                                    }
                                    if (temp15.FileID != null) {
                                        strSQL = "SELECT * FROM " + TB_photoofresultscene
                                                + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                                        try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                            if (cursor3.getCount() == 1) {
                                                cursor3.moveToFirst();
                                                TbPhotoOfResultscene tbPhotoOfResultscene = new TbPhotoOfResultscene();
                                                tbPhotoOfResultscene.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                                tbPhotoOfResultscene.RSID = cursor3.getString(cursor3.getColumnIndex(COL_RSID));
                                                apiMultimedia.setTbPhotoOfResultscene(tbPhotoOfResultscene);
                                                Log.i(TAG, "tbPhotoOfResultscene " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfResultscene().FileID));
                                            } else {
                                                apiMultimedia.setTbPhotoOfResultscene(null);
                                            }
                                        }

                                    }
                                    if (temp15.FileID != null) {
                                        strSQL = "SELECT * FROM " + TB_photoofpropertyless
                                                + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";
                                        try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                            if (cursor3.getCount() == 1) {
                                                cursor3.moveToFirst();
                                                TbPhotoOfPropertyless tbPhotoOfPropertyless = new TbPhotoOfPropertyless();
                                                tbPhotoOfPropertyless.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                                tbPhotoOfPropertyless.PropertyLessID = cursor3.getString(cursor3.getColumnIndex(COL_PropertyLessID));
                                                apiMultimedia.setTbPhotoOfPropertyless(tbPhotoOfPropertyless);
                                                Log.i(TAG, "tbPhotoOfPropertyless " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfPropertyless().FileID));
//
                                            } else {
                                                apiMultimedia.setTbPhotoOfPropertyless(null);
                                            }
                                        }
                                    }
                                    apiMultimediaList.add(apiMultimedia);

                                }
                            }

                        }
                        apiCaseSceneCase.setApiMultimedia(apiMultimediaList);
                        Log.i(TAG, "apiMultimediaList :" + String.valueOf(apiCaseSceneCase.getApiMultimedia().size()));
                        for (int i = 0; i < apiCaseSceneCase.getApiMultimedia().size(); i++) {
                            Log.i(TAG, apiCaseSceneCase.getApiMultimedia().get(i).getTbMultimediaFile().FilePath);
                        }
                    }

                    // Index tbFindEvidences ดึงจากตาราง findevidence ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM findevidence "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        List<TbFindEvidence> tbFindEvidences = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbFindEvidences == null) {
                                tbFindEvidences = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbFindEvidence temp16 = new TbFindEvidence();
                                    temp16.FindEvidenceID = cursor2.getString(cursor2.getColumnIndex(COL_FindEvidenceID));
                                    temp16.EvidenceTypeID = cursor2.getString(cursor2.getColumnIndex(COL_EvidenceTypeID));
                                    temp16.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp16.SceneInvestID = cursor2.getString(cursor2.getColumnIndex(COL_SceneInvestID));
                                    temp16.EvidenceNumber = cursor2.getString(cursor2.getColumnIndex(COL_EvidenceNumber));
                                    temp16.FindEvidenceZone = cursor2.getString(cursor2.getColumnIndex(COL_FindEvidenceZone));
                                    temp16.FindEvidencecol = cursor2.getString(cursor2.getColumnIndex(COL_FindEvidencecol));
                                    temp16.Marking = cursor2.getString(cursor2.getColumnIndex(COL_Marking));
                                    temp16.Parceling = cursor2.getString(cursor2.getColumnIndex(COL_Parceling));
                                    temp16.EvidencePerformed = cursor2.getString(cursor2.getColumnIndex(COL_EvidencePerformed));
                                    tbFindEvidences.add(temp16);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbFindEvidences(tbFindEvidences);

                    }
                    // Index tbResultScenes ดึงจากตาราง resultscene ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM resultscene "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        List<TbResultScene> tbResultScenes = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbResultScenes == null) {
                                tbResultScenes = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbResultScene temp17 = new TbResultScene();
                                    temp17.RSID = cursor2.getString(cursor2.getColumnIndex(COL_RSID));
                                    temp17.RSTypeID = cursor2.getString(cursor2.getColumnIndex(COL_RSTypeID));
                                    temp17.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp17.RSDetail = cursor2.getString(cursor2.getColumnIndex(COL_RSDetail));
                                    tbResultScenes.add(temp17);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbResultScenes(tbResultScenes);

                    }
                    // Index tbGatewayCriminals ดึงจากตาราง resultscene ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM resultscene "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'"
                                + " AND RSTypeID = 'GC'";
                        List<TbGatewayCriminal> tbGatewayCriminals = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbGatewayCriminals == null) {
                                tbGatewayCriminals = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbGatewayCriminal temp18 = new TbGatewayCriminal();
                                    temp18.RSID = cursor2.getString(cursor2.getColumnIndex(COL_RSID));
                                    temp18.RSTypeID = cursor2.getString(cursor2.getColumnIndex(COL_RSTypeID));
                                    temp18.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp18.RSDetail = cursor2.getString(cursor2.getColumnIndex(COL_RSDetail));
                                    tbGatewayCriminals.add(temp18);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbGatewayCriminals(tbGatewayCriminals);

                    }
                    // Index tbClueShowns ดึงจากตาราง resultscene ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM resultscene "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'"
                                + " AND RSTypeID = 'CS'";
                        List<TbClueShown> tbClueShowns = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbClueShowns == null) {
                                tbClueShowns = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbClueShown temp19 = new TbClueShown();
                                    temp19.RSID = cursor2.getString(cursor2.getColumnIndex(COL_RSID));
                                    temp19.RSTypeID = cursor2.getString(cursor2.getColumnIndex(COL_RSTypeID));
                                    temp19.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp19.RSDetail = cursor2.getString(cursor2.getColumnIndex(COL_RSDetail));
                                    tbClueShowns.add(temp19);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbClueShowns(tbClueShowns);

                    }
                    // Index tbPropertyLosses ดึงจากตาราง propertyloss ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM propertyloss "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        List<TbPropertyLoss> tbPropertyLosses = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbPropertyLosses == null) {
                                tbPropertyLosses = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbPropertyLoss temp20 = new TbPropertyLoss();
                                    temp20.PropertyLossID = cursor2.getString(cursor2.getColumnIndex(COL_PropertyLossID));
                                    temp20.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                                    temp20.PropertyLossName = cursor2.getString(cursor2.getColumnIndex(COL_PropertyLossName));
                                    temp20.PropertyLossNumber = cursor2.getString(cursor2.getColumnIndex(COL_PropertyLossNumber));
                                    temp20.PropertyLossUnit = cursor2.getString(cursor2.getColumnIndex(COL_PropertyLossUnit));
                                    temp20.PropertyLossPosition = cursor2.getString(cursor2.getColumnIndex(COL_PropertyLossPosition));
                                    temp20.PropInsurance = cursor2.getString(cursor2.getColumnIndex(COL_PropInsurance));
                                    tbPropertyLosses.add(temp20);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbPropertyLosses(tbPropertyLosses);

                    }
                    //ส่งค่าทั้งหมดเข้า apiNoticeCases
                    apiCaseSceneCase.setMode("offline");
                    apiCaseScenesCases.add(apiCaseSceneCase);
                    Log.d("TEST", "--" + apiCaseSceneCase.getTbNoticeCase().NoticeCaseID);
                }
            }
            db.close();
//            Log.d("TEST", apiNoticeCases.get(0).getTbNoticeCase().NoticeCaseID + " " + apiNoticeCases.get(1).getTbNoticeCase().NoticeCaseID);
            // รวมข้อมูลที่ได้ทั้งหมดลง apiListNoticeCase ก่อนส่งกลับไปใช้
            Log.d(TAG, "apiCaseScenesCases:" + apiCaseScenesCases.size());
            for (int i = 0; i < apiCaseScenesCases.size(); i++) {
                Log.d("TEST", "////--" + apiCaseScenesCases.get(i).getTbNoticeCase().NoticeCaseID);
            }

            dataEntity.setResult(apiCaseScenesCases);
            apiListCaseScene.setData(dataEntity);
            return apiListCaseScene;
        } catch (Exception e) {
            Log.d(TAG, "Error in selectApiCaseScene " + e.getMessage().toString());
            return null;
        }
    }


    public String[][] SelectAllProvince() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM province ORDER BY PROVINCE_NAME ASC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            arrData[i][j] = cursor.getString(j);
                        }
                        // Log.i(TAG, "show SelectAllProvince " + arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAllAumphur() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM amphur";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            arrData[i][j] = cursor.getString(j);
                        }
                        // Log.i(TAG, "show SelectAllProvince " + arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAmphur(String provinceid) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM amphur WHERE PROVINCE_ID = '" + provinceid + "'";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i(TAG, provinceid + " amphur " + String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {

                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);


                        i++;
                    } while (cursor.moveToNext());
                }
                //  Log.i(TAG, "show amphur" + arrData[2]);
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectDistrict(String AMPHUR_ID) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM district WHERE AMPHUR_ID = " + AMPHUR_ID;
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i(TAG, String.valueOf(AMPHUR_ID) + " district " + String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        i++;
                    } while (cursor.moveToNext());
                }
                //   Log.i(TAG, "show district" + arrData[2]);
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAllSCDCCenter() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM scdccenter";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i(TAG, "SelectAllSCDCCenter " + String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);

                        Log.i(TAG, "SelectAllSCDCCenter " + arrData[i][2]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[] SelectSCDCCenterID(String SCDCAgencyCode) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT c.SCDCCenterID " +
                    "FROM scdccenter AS c,scdcagency WHERE scdcagency.SCDCCenterID = c.SCDCCenterID AND" +
                    " scdcagency.SCDCAgencyCode = '" + SCDCAgencyCode + "' ORDER BY c.SCDCCenterID ASC LIMIT 1";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i(TAG, SCDCAgencyCode + " SelectAllSCDCCenter" + strSQL + " " + String.valueOf(cursor.getCount()));


            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                }
                Log.i("show SubCaseTypeName", arrData[0]);
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectSCDCAgency(String SCDCCenterID) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM scdcagency WHERE SCDCCenterID = '" + SCDCCenterID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i(TAG, "SelectSCDCAgency " + String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);

                        Log.i(TAG, "SelectSCDCAgency " + arrData[i][2]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }


    public long DeleteSelectedData(String tableName, String colName, String id) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            db.beginTransaction();
            long rows = db.delete(tableName,
                    colName + " = ?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();
            Log.i(TAG, "delete " + tableName + ": " + String.valueOf(rows) + " " + id + " " + String.valueOf(rows));
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectAllEvidenceType() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM evidencetype ORDER BY EvidenceTypeID ASC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            arrData[i][j] = cursor.getString(j);
                        }
                        // Log.i(TAG, "show SelectAllEvidenceType " + arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectDataMultimediaFile(String sCaseReportID,
                                               String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM multimediafile WHERE "
                    + oMultimediaFile.CaseReportID + " = '" + sCaseReportID + "' AND "
                    + COL_FileType + " = '" + sFileType + "'" + " ORDER BY "
                    + COL_FileID + " DESC";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i(TAG, "show Photo " + sFileType + arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();

            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public List<TbMultimediaFile> selectedMediafiles(String sCaseReportID, String sFileType) {
        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT * FROM multimediafile WHERE "
                    + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + COL_FileType + " = '" + sFileType + "'" + " ORDER BY "
                    + COL_FileID + " DESC";
            Log.i(TAG, "multimediafile " + strSQL);
            List<TbMultimediaFile> tbMultimediaFiles = null;
            try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                if (tbMultimediaFiles == null) {
                    tbMultimediaFiles = new ArrayList<>(cursor2.getCount());
                }
                if (cursor2.getCount() > 0) {
                    cursor2.moveToPosition(-1);
                    while (cursor2.moveToNext()) {
                        TbMultimediaFile temp15 = new TbMultimediaFile();
                        temp15.FileID = cursor2.getString(cursor2.getColumnIndex(COL_FileID));
                        temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                        temp15.FileType = cursor2.getString(cursor2.getColumnIndex(COL_FileType));
                        temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(COL_FilePath));
                        temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(COL_FileDescription));
                        temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(COL_Timestamp));
                        tbMultimediaFiles.add(temp15);
                    }
                }
            }
            Log.i(TAG, "multimediafile " + String.valueOf(tbMultimediaFiles.size()));
            db.close();
            return tbMultimediaFiles;
        } catch (Exception e) {
            return null;
        }
    }

    public List<TbMultimediaFile> SelectDataPhotoOfOutside(String sCaseReportID,
                                                           String sFileType) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " +
                    TB_MultimediaFile + "." + COL_FileID + " , " +
                    TB_MultimediaFile + "." + COL_CaseReportID + " , " +
                    TB_MultimediaFile + "." + COL_FileType + " , " +
                    TB_MultimediaFile + "." + COL_FilePath + " , " +
                    TB_MultimediaFile + "." + COL_FileDescription + " , " +
                    TB_MultimediaFile + "." + COL_Timestamp +
                    " FROM "
                    + TB_MultimediaFile + "," + TB_photoofoutside + " WHERE "
                    + TB_MultimediaFile + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TB_photoofoutside + "." + COL_CaseReportID + " = " + TB_MultimediaFile + "." + COL_CaseReportID + " AND "
                    + TB_MultimediaFile + "." + COL_FileID + " = " + TB_photoofoutside + "." + COL_FileID + " AND "
                    + TB_MultimediaFile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TB_MultimediaFile + "." + COL_FileID + " DESC";
            Log.i("show", strSQL);
            List<TbMultimediaFile> tbMultimediaFiles = null;
            try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                if (tbMultimediaFiles == null) {
                    tbMultimediaFiles = new ArrayList<>(cursor2.getCount());
                }
                if (cursor2.getCount() > 0) {
                    cursor2.moveToPosition(-1);
                    while (cursor2.moveToNext()) {
                        TbMultimediaFile temp15 = new TbMultimediaFile();
                        temp15.FileID = cursor2.getString(cursor2.getColumnIndex(COL_FileID));
                        temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                        temp15.FileType = cursor2.getString(cursor2.getColumnIndex(COL_FileType));
                        temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(COL_FilePath));
                        temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(COL_FileDescription));
                        temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(COL_Timestamp));
                        tbMultimediaFiles.add(temp15);
                    }
                }
            }
            db.close();
            return tbMultimediaFiles;

        } catch (Exception e) {
            return null;
        }
    }

    public List<ApiMultimedia> SelectDataPhotoOfInside(String sFeatureInsideID,
                                                       String sFileType) {
        List<ApiMultimedia> apiMultimediaList = new ArrayList<>();

        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " +
                    TB_MultimediaFile + "." + COL_FileID + " , " +
                    TB_MultimediaFile + "." + COL_CaseReportID + " , " +
                    TB_MultimediaFile + "." + COL_FileType + " , " +
                    TB_MultimediaFile + "." + COL_FilePath + " , " +
                    TB_MultimediaFile + "." + COL_FileDescription + " , " +
                    TB_MultimediaFile + "." + COL_Timestamp +
                    " FROM "
                    + TB_MultimediaFile + "," + TB_photoofinside + " WHERE "
                    + TB_photoofinside + "." + COL_FeatureInsideID + " = '" + sFeatureInsideID + "' AND "
                    + TB_photoofinside + "." + COL_FileID + " = " + TB_MultimediaFile + "." + COL_FileID + " AND "
                    + TB_MultimediaFile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TB_MultimediaFile + "." + COL_FileID + " DESC";
            Log.i(TAG, "show SelectDataPhotoOfInside" + strSQL);
            try (Cursor cursor2 = db.rawQuery(strSQL, null)) {

                if (cursor2.getCount() > 0) {
                    cursor2.moveToPosition(-1);
                    while (cursor2.moveToNext()) {
                        ApiMultimedia apiMultimedia = new ApiMultimedia();

                        TbMultimediaFile temp15 = new TbMultimediaFile();
                        temp15.FileID = cursor2.getString(cursor2.getColumnIndex(COL_FileID));
                        temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                        temp15.FileType = cursor2.getString(cursor2.getColumnIndex(COL_FileType));
                        temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(COL_FilePath));
                        temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(COL_FileDescription));
                        temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(COL_Timestamp));
                        apiMultimedia.setTbMultimediaFile(temp15);
                        if (temp15.FileID != null) {
                            strSQL = "SELECT * FROM " + TB_photoofinside
                                    + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                            try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                if (cursor3.getCount() == 1) {
                                    cursor3.moveToFirst();
                                    TbPhotoOfInside tbPhotoOfInside = new TbPhotoOfInside();
                                    tbPhotoOfInside.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                    tbPhotoOfInside.FeatureInsideID = cursor3.getString(cursor3.getColumnIndex(COL_FeatureInsideID));
                                    apiMultimedia.setTbPhotoOfInside(tbPhotoOfInside);
                                    Log.i(TAG, "tbPhotoOfInside " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfInside().FileID));
                                } else {
                                    apiMultimedia.setTbPhotoOfInside(null);
                                }
                            }
                        }
                        apiMultimediaList.add(apiMultimedia);
                    }

                }
            }

            db.close();
            return apiMultimediaList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<ApiMultimedia> SelectDataPhotoOfPropertyLoss(String sPLID,
                                                             String sFileType) {
        List<ApiMultimedia> apiMultimediaList = new ArrayList<>();

        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " +
                    TB_MultimediaFile + "." + COL_FileID + " , " +
                    TB_MultimediaFile + "." + COL_CaseReportID + " , " +
                    TB_MultimediaFile + "." + COL_FileType + " , " +
                    TB_MultimediaFile + "." + COL_FilePath + " , " +
                    TB_MultimediaFile + "." + COL_FileDescription + " , " +
                    TB_MultimediaFile + "." + COL_Timestamp +
                    " FROM "
                    + TB_MultimediaFile + "," + TB_photoofpropertyless + " WHERE "
                    + TB_photoofpropertyless + "." + COL_PropertyLessID + " = '" + sPLID + "' AND "
                    + TB_photoofpropertyless + "." + COL_FileID + " = " + TB_MultimediaFile + "." + COL_FileID + " AND "
                    + TB_MultimediaFile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TB_MultimediaFile + "." + COL_FileID + " DESC";
            Log.i(TAG, "SelectDataPhotoOfPropertyLoss " + strSQL);
            try (Cursor cursor2 = db.rawQuery(strSQL, null)) {

                if (cursor2.getCount() > 0) {
                    cursor2.moveToPosition(-1);
                    while (cursor2.moveToNext()) {
                        ApiMultimedia apiMultimedia = new ApiMultimedia();

                        TbMultimediaFile temp15 = new TbMultimediaFile();
                        temp15.FileID = cursor2.getString(cursor2.getColumnIndex(COL_FileID));
                        temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                        temp15.FileType = cursor2.getString(cursor2.getColumnIndex(COL_FileType));
                        temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(COL_FilePath));
                        temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(COL_FileDescription));
                        temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(COL_Timestamp));
                        apiMultimedia.setTbMultimediaFile(temp15);
                        if (temp15.FileID != null) {
                            strSQL = "SELECT * FROM " + TB_photoofpropertyless
                                    + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                            try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                if (cursor3.getCount() == 1) {
                                    cursor3.moveToFirst();
                                    TbPhotoOfPropertyless tbPhotoOfPropertyless = new TbPhotoOfPropertyless();
                                    tbPhotoOfPropertyless.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                    tbPhotoOfPropertyless.PropertyLessID = cursor3.getString(cursor3.getColumnIndex(COL_PropertyLessID));
                                    apiMultimedia.setTbPhotoOfPropertyless(tbPhotoOfPropertyless);
                                    Log.i(TAG, "tbPhotoOfPropertyless " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfPropertyless().FileID));
                                } else {
                                    apiMultimedia.setTbPhotoOfPropertyless(null);
                                }
                            }
                        }
                        apiMultimediaList.add(apiMultimedia);
                    }

                }
            }

            db.close();
            return apiMultimediaList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<ApiMultimedia> SelectDataPhotoOfEvidence(String sEVID,
                                                         String sFileType) {
        List<ApiMultimedia> apiMultimediaList = new ArrayList<>();

        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " +
                    TB_MultimediaFile + "." + COL_FileID + " , " +
                    TB_MultimediaFile + "." + COL_CaseReportID + " , " +
                    TB_MultimediaFile + "." + COL_FileType + " , " +
                    TB_MultimediaFile + "." + COL_FilePath + " , " +
                    TB_MultimediaFile + "." + COL_FileDescription + " , " +
                    TB_MultimediaFile + "." + COL_Timestamp +
                    " FROM "
                    + TB_MultimediaFile + "," + TB_photoofevidence + " WHERE "
                    + TB_photoofevidence + "." + COL_FindEvidenceID + " = '" + sEVID + "' AND "
                    + TB_photoofevidence + "." + COL_FileID + " = " + TB_MultimediaFile + "." + COL_FileID + " AND "
                    + TB_MultimediaFile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TB_MultimediaFile + "." + COL_FileID + " DESC";
            Log.i("show", strSQL);
            try (Cursor cursor2 = db.rawQuery(strSQL, null)) {

                if (cursor2.getCount() > 0) {
                    cursor2.moveToPosition(-1);
                    while (cursor2.moveToNext()) {
                        ApiMultimedia apiMultimedia = new ApiMultimedia();

                        TbMultimediaFile temp15 = new TbMultimediaFile();
                        temp15.FileID = cursor2.getString(cursor2.getColumnIndex(COL_FileID));
                        temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                        temp15.FileType = cursor2.getString(cursor2.getColumnIndex(COL_FileType));
                        temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(COL_FilePath));
                        temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(COL_FileDescription));
                        temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(COL_Timestamp));
                        apiMultimedia.setTbMultimediaFile(temp15);
                        if (temp15.FileID != null) {
                            strSQL = "SELECT * FROM " + TB_photoofevidence
                                    + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                            try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                if (cursor3.getCount() == 1) {
                                    cursor3.moveToFirst();
                                    TbPhotoOfEvidence tbPhotoOfEvidence = new TbPhotoOfEvidence();
                                    tbPhotoOfEvidence.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                    tbPhotoOfEvidence.FindEvidenceID = cursor3.getString(cursor3.getColumnIndex(COL_FindEvidenceID));
                                    apiMultimedia.setTbPhotoOfEvidence(tbPhotoOfEvidence);
                                    Log.i(TAG, "tbPhotoOfEvidence " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfEvidence().FileID));
                                } else {
                                    apiMultimedia.setTbPhotoOfEvidence(null);
                                }
                            }
                        }
                        apiMultimediaList.add(apiMultimedia);
                    }

                }
            }

            db.close();
            return apiMultimediaList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<ApiMultimedia> SelectDataPhotoOfResultscene(String sRSID,
                                                            String sFileType) {
        List<ApiMultimedia> apiMultimediaList = new ArrayList<>();

        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " +
                    TB_MultimediaFile + "." + COL_FileID + " , " +
                    TB_MultimediaFile + "." + COL_CaseReportID + " , " +
                    TB_MultimediaFile + "." + COL_FileType + " , " +
                    TB_MultimediaFile + "." + COL_FilePath + " , " +
                    TB_MultimediaFile + "." + COL_FileDescription + " , " +
                    TB_MultimediaFile + "." + COL_Timestamp +
                    " FROM "
                    + TB_MultimediaFile + "," + TB_photoofresultscene + " WHERE "
                    + TB_photoofresultscene + "." + COL_RSID + " = '" + sRSID + "' AND "
                    + TB_photoofresultscene + "." + COL_FileID + " = " + TB_MultimediaFile + "." + COL_FileID + " AND "
                    + TB_MultimediaFile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TB_MultimediaFile + "." + COL_FileID + " DESC";
            Log.i("show", strSQL);
            try (Cursor cursor2 = db.rawQuery(strSQL, null)) {

                if (cursor2.getCount() > 0) {
                    cursor2.moveToPosition(-1);
                    while (cursor2.moveToNext()) {
                        ApiMultimedia apiMultimedia = new ApiMultimedia();

                        TbMultimediaFile temp15 = new TbMultimediaFile();
                        temp15.FileID = cursor2.getString(cursor2.getColumnIndex(COL_FileID));
                        temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(COL_CaseReportID));
                        temp15.FileType = cursor2.getString(cursor2.getColumnIndex(COL_FileType));
                        temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(COL_FilePath));
                        temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(COL_FileDescription));
                        temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(COL_Timestamp));
                        apiMultimedia.setTbMultimediaFile(temp15);
                        if (temp15.FileID != null) {
                            strSQL = "SELECT * FROM " + TB_photoofresultscene
                                    + " WHERE " + COL_FileID + " = '" + temp15.FileID + "'";

                            try (Cursor cursor3 = db.rawQuery(strSQL, null)) {

                                if (cursor3.getCount() == 1) {
                                    cursor3.moveToFirst();
                                    TbPhotoOfResultscene tbPhotoOfResultscene = new TbPhotoOfResultscene();
                                    tbPhotoOfResultscene.FileID = cursor3.getString(cursor3.getColumnIndex(COL_FileID));
                                    tbPhotoOfResultscene.RSID = cursor3.getString(cursor3.getColumnIndex(COL_RSID));
                                    apiMultimedia.setTbPhotoOfResultscene(tbPhotoOfResultscene);
                                    Log.i(TAG, "tbPhotoOfResultscene " + temp15.FileID + " " + String.valueOf(apiMultimedia.getTbPhotoOfResultscene().FileID));
                                } else {
                                    apiMultimedia.setTbPhotoOfEvidence(null);
                                }
                            }
                        }
                        apiMultimediaList.add(apiMultimedia);
                    }

                }
            }

            db.close();
            Log.i(TAG, "apiMultimediaList resultscene :" + String.valueOf(apiMultimediaList.size()));
            for (int i = 0; i < apiMultimediaList.size(); i++) {
                Log.i(TAG, apiMultimediaList.get(i).getTbMultimediaFile().FileID);
            }
            return apiMultimediaList;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeleteMediaFile(String sCaseReportID,
                                String sFileID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TB_MultimediaFile, COL_CaseReportID
                    + " =? AND " + COL_FileID + " =?", new String[]{
                    sCaseReportID, sFileID});

            db.close();
            Log.i("DeleteMultimediafile", String.valueOf(rows)
                    + sCaseReportID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectAllRank() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM policerank ORDER BY RankID ASC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];
                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAllinqposition() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM inqposition ORDER BY InqPosID ASC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];
                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAllinvposition() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM invposition ORDER BY InvPosID ASC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];
                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String getPoliceStionName(String PoliceStationID) {
        // TODO Auto-generated method stub

        try {
            String arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM policestation WHERE PoliceStationID = '" + PoliceStationID + "' LIMIT 1";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i(TAG, PoliceStationID + "   " + String.valueOf(cursor.getCount()));
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                arrData = cursor.getString(cursor.getColumnIndex("PoliceStationName"));
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String getSCDCAgencyName(String SCDCAgencyCode) {
        // TODO Auto-generated method stub

        try {
            String arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM scdcagency WHERE SCDCAgencyCode = '" + SCDCAgencyCode + "' LIMIT 1";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i(TAG, SCDCAgencyCode + "   " + String.valueOf(cursor.getCount()));
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                arrData = cursor.getString(cursor.getColumnIndex("SCDCAgencyName"));
            }
            cursor.close();

            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }
}
