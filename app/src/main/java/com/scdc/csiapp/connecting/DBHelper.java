package com.scdc.csiapp.connecting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiListCaseScene;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
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
    TbAmphur oAmphur = new TbAmphur();
    TbCaseScene oCaseScene = new TbCaseScene();
    TbCaseSceneType oCaseSceneType = new TbCaseSceneType();
    TbComPosition oComPosition = new TbComPosition();
    TbDistrict oDistrict = new TbDistrict();
    TbFindEvidence oFindEvidence = new TbFindEvidence();
    TbGeography oGeography = new TbGeography();
    TbInqPosition oInqPosition = new TbInqPosition();
    TbInvestigatorsInScene oInvestigatorsInScene = new TbInvestigatorsInScene();
    TbInvPosition oInvPosition = new TbInvPosition();
    TbMultimediaFile oMultimediaFile = new TbMultimediaFile();
    TbNoticeCase oNoticeCase = new TbNoticeCase();
    TbOfficial oOfficial = new TbOfficial();
    TbPermission oPermission = new TbPermission();
    TbPhotoOfEvidence oPhotoOfEvidence = new TbPhotoOfEvidence();
    TbPhotoOfInside oPhotoOfInside = new TbPhotoOfInside();
    TbPhotoOfOutside oPhotoOfOutside = new TbPhotoOfOutside();
    TbPhotoOfResultscene oPhotoOfResultscene = new TbPhotoOfResultscene();
    TbPoliceAgency oPoliceAgency = new TbPoliceAgency();
    TbPoliceCenter oPoliceCenter = new TbPoliceCenter();
    TbPolicePosition oPolicePosition = new TbPolicePosition();
    TbPoliceRank oPoliceRank = new TbPoliceRank();
    TbPoliceStation oPoliceStation = new TbPoliceStation();
    TbPropertyLoss oPropertyLoss = new TbPropertyLoss();
    TbProvince oProvince = new TbProvince();
    TbRegistrationGCM oRegistrationGCM = new TbRegistrationGCM();
    TbResultScene oResultScene = new TbResultScene();
    TbResultSceneType oResultSceneType = new TbResultSceneType();
    TbSCDCagency oScdCagency = new TbSCDCagency();
    TbSCDCcenter oScdCcenter = new TbSCDCcenter();
    TbSceneFeatureInSide oSceneFeatureInSide = new TbSceneFeatureInSide();
    TbSceneFeatureOutside oSceneFeatureOutside = new TbSceneFeatureOutside();
    TbSceneInvestigation oSceneInvestigation = new TbSceneInvestigation();
    TbScheduleGroup oScheduleGroup = new TbScheduleGroup();
    TbScheduleInvestigates oScheduleInvestigates = new TbScheduleInvestigates();
    TbSubcaseSceneType oSubcaseSceneType = new TbSubcaseSceneType();
    TbEvidenceType oEvidenceType = new TbEvidenceType();
    TbUsers oUsers = new TbUsers();

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
                Val.put(temp.COL_AMPHUR_ID, tbAmphurList.get(i).AMPHUR_ID);
                Val.put(temp.COL_AMPHUR_CODE, tbAmphurList.get(i).AMPHUR_CODE);
                Val.put(temp.COL_AMPHUR_NAME, tbAmphurList.get(i).AMPHUR_NAME);
                Val.put(temp.COL_POSTCODE, tbAmphurList.get(i).POSTCODE);
                Val.put(temp.COL_GEO_ID, tbAmphurList.get(i).GEO_ID);
                Val.put(temp.COL_PROVINCE_ID, tbAmphurList.get(i).PROVINCE_ID);
                Val.put(temp.COL_amphur_polygon, tbAmphurList.get(i).amphur_polygon);

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
                Val.put(temp.COL_CaseTypeID, tbCaseSceneTypes.get(i).CaseTypeID);
                Val.put(temp.COL_CaseTypeName, tbCaseSceneTypes.get(i).CaseTypeName);
                Val.put(temp.COL_casetype_min, tbCaseSceneTypes.get(i).casetype_min);
                Val.put(temp.COL_casetype_max, tbCaseSceneTypes.get(i).casetype_max);
                Val.put(temp.COL_casetype_icon, tbCaseSceneTypes.get(i).casetype_icon);
                Val.put(temp.COL_casetype_colormin, tbCaseSceneTypes.get(i).casetype_colormin);
                Val.put(temp.COL_casetype_colormedium, tbCaseSceneTypes.get(i).casetype_colormedium);
                Val.put(temp.COL_casetype_colorhigh, tbCaseSceneTypes.get(i).casetype_colorhigh);
                Val.put(temp.COL_casetype_status, tbCaseSceneTypes.get(i).casetype_status);

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
                Val.put(oComPosition.COL_ComPosID, tbComPositions.get(i).ComPosID);
                Val.put(oComPosition.COL_ComPosName, tbComPositions.get(i).ComPosName);
                Val.put(oComPosition.COL_ComPosAbbr, tbComPositions.get(i).ComPosAbbr);

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
                Val.put(temp.COL_DISTRICT_ID, tbDistricts.get(i).DISTRICT_ID);
                Val.put(temp.COL_DISTRICT_CODE, tbDistricts.get(i).DISTRICT_CODE);
                Val.put(temp.COL_DISTRICT_NAME, tbDistricts.get(i).DISTRICT_NAME);
                Val.put(temp.COL_AMPHUR_ID, tbDistricts.get(i).AMPHUR_ID);

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
                Val.put(oGeography.COL_GEO_ID, tbGeographies.get(i).GEO_ID);
                Val.put(oGeography.COL_GEO_NAME, tbGeographies.get(i).GEO_NAME);

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
                Val.put(oInqPosition.COL_InqPosID, tbInqPositions.get(i).InqPosID);
                Val.put(oInqPosition.COL_InqPosName, tbInqPositions.get(i).InqPosName);
                Val.put(oInqPosition.COL_InqPosAbbr, tbInqPositions.get(i).InqPosAbbr);

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
                Val.put(oInvPosition.COL_InvPosID, tbInvPositions.get(i).InvPosID);
                Val.put(oInvPosition.COL_InvPosName, tbInvPositions.get(i).InvPosName);
                Val.put(oInvPosition.COL_InvPosAbbr, tbInvPositions.get(i).InvPosAbbr);

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
                Val.put(temp.COL_OfficialID, tbOfficials.get(i).OfficialID);
                Val.put(temp.COL_FirstName, tbOfficials.get(i).FirstName);
                Val.put(temp.COL_LastName, tbOfficials.get(i).LastName);
                Val.put(temp.COL_Alias, tbOfficials.get(i).Alias);
                Val.put(temp.COL_Rank, tbOfficials.get(i).Rank);
                Val.put(temp.COL_Position, tbOfficials.get(i).Position);
                Val.put(temp.COL_SubPossition, tbOfficials.get(i).SubPossition);
                Val.put(temp.COL_PhoneNumber, tbOfficials.get(i).PhoneNumber);
                Val.put(temp.COL_OfficialEmail, tbOfficials.get(i).OfficialEmail);
                Val.put(temp.COL_OfficialDisplayPic, tbOfficials.get(i).OfficialDisplayPic);
                Val.put(temp.COL_AccessType, tbOfficials.get(i).AccessType);
                Val.put(temp.COL_SCDCAgencyCode, tbOfficials.get(i).SCDCAgencyCode);
                Val.put(temp.COL_PoliceStationID, tbOfficials.get(i).PoliceStationID);
                Val.put(temp.COL_id_users, tbOfficials.get(i).id_users);

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
                Val.put(oPermission.COL_id_permission, tbPermissions.get(i).id_permission);
                Val.put(oPermission.COL_per_name, tbPermissions.get(i).per_name);
                Val.put(oPermission.COL_per_value, tbPermissions.get(i).per_value);

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
                Val.put(temp.COL_PoliceAgencyID, tbPoliceAgencies.get(i).PoliceAgencyID);
                Val.put(temp.COL_PoliceCenterID, tbPoliceAgencies.get(i).PoliceCenterID);
                Val.put(temp.COL_PoliceAgencyName, tbPoliceAgencies.get(i).PoliceAgencyName);

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
                Val.put(oPoliceCenter.COL_PoliceCenterID, tbPoliceCenters.get(i).PoliceCenterID);
                Val.put(oPoliceCenter.COL_PoliceName, tbPoliceCenters.get(i).PoliceName);

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
                Val.put(oPolicePosition.COL_PolicePosID, tbPolicePositions.get(i).PolicePosID);
                Val.put(oPolicePosition.COL_PoliceName, tbPolicePositions.get(i).PoliceName);
                Val.put(oPolicePosition.COL_PoliceAbbr, tbPolicePositions.get(i).PoliceAbbr);

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
                Val.put(oPoliceRank.COL_RankID, tbPoliceRanks.get(i).RankID);
                Val.put(oPoliceRank.COL_RankName, tbPoliceRanks.get(i).RankName);
                Val.put(oPoliceRank.COL_RankAbbr, tbPoliceRanks.get(i).RankAbbr);

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
                Val.put(temp.COL_PoliceStationID, tbPoliceStations.get(i).PoliceStationID);
                Val.put(temp.COL_PoliceAgencyID, tbPoliceStations.get(i).PoliceAgencyID);
                Val.put(temp.COL_PoliceStationName, tbPoliceStations.get(i).PoliceStationName);

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
                Val.put(temp.COL_PROVINCE_ID, tbProvinces.get(i).PROVINCE_ID);
                Val.put(temp.COL_PROVINCE_CODE, tbProvinces.get(i).PROVINCE_CODE);
                Val.put(temp.COL_PROVINCE_NAME, tbProvinces.get(i).PROVINCE_NAME);
                Val.put(temp.COL_GEO_ID, tbProvinces.get(i).GEO_ID);
                Val.put(temp.COL_province_status, tbProvinces.get(i).province_status);
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
                Val.put(oResultSceneType.COL_RSTypeID, tbResultSceneTypes.get(i).RSTypeID);
                Val.put(oResultSceneType.COL_RSTypeNameEN, tbResultSceneTypes.get(i).RSTypeNameEN);
                Val.put(oResultSceneType.COL_RSTypeNameTH, tbResultSceneTypes.get(i).RSTypeNameTH);
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
                Val.put(temp.COL_SCDCAgencyCode, tbSCDCagencies.get(i).SCDCAgencyCode);
                Val.put(temp.COL_SCDCCenterID, tbSCDCagencies.get(i).SCDCCenterID);
                Val.put(temp.COL_SCDCAgencyName, tbSCDCagencies.get(i).SCDCAgencyName);
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
                Val.put(temp.COL_SCDCCenterID, tbSCDCcenters.get(i).SCDCCenterID);
                Val.put(temp.COL_SCDCCenterName, tbSCDCcenters.get(i).SCDCCenterName);
                Val.put(temp.COL_SCDCCenterProvince, tbSCDCcenters.get(i).SCDCCenterProvince);
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
                Val.put(temp.COL_SubCaseTypeID, tbSubcaseSceneTypes.get(i).SubCaseTypeID);
                Val.put(temp.COL_CaseTypeID, tbSubcaseSceneTypes.get(i).CaseTypeID);
                Val.put(temp.COL_SubCaseTypeName, tbSubcaseSceneTypes.get(i).SubCaseTypeName);

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
                Val.put(oEvidenceType.COL_EvidenceTypeID, tbEvidenceTypes.get(i).EvidenceTypeID);
                Val.put(oEvidenceType.COL_EvidenceTypeNameEN, tbEvidenceTypes.get(i).EvidenceTypeNameEN);
                Val.put(oEvidenceType.COL_EvidenceTypeNameTH, tbEvidenceTypes.get(i).EvidenceTypeNameTH);
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
                Val.put(temp.COL_NoticeCaseID, tbNoticeCases.get(i).NoticeCaseID);
                Val.put(temp.COL_Mobile_CaseID, tbNoticeCases.get(i).Mobile_CaseID);
                Val.put(temp.COL_InquiryOfficialID, tbNoticeCases.get(i).InquiryOfficialID);
                Val.put(temp.COL_InvestigatorOfficialID, tbNoticeCases.get(i).InvestigatorOfficialID);
                Val.put(temp.COL_SCDCAgencyCode, tbNoticeCases.get(i).SCDCAgencyCode);
                Val.put(temp.COL_CaseTypeID, tbNoticeCases.get(i).CaseTypeID);
                Val.put(temp.COL_SubCaseTypeID, tbNoticeCases.get(i).SubCaseTypeID);
                Val.put(temp.COL_CaseStatus, tbNoticeCases.get(i).CaseStatus);
                Val.put(temp.COL_PoliceStationID, tbNoticeCases.get(i).PoliceStationID);
                Val.put(temp.COL_CaseTel, tbNoticeCases.get(i).CaseTel);
                Val.put(temp.COL_ReceivingCaseDate, tbNoticeCases.get(i).ReceivingCaseDate);
                Val.put(temp.COL_ReceivingCaseTime, tbNoticeCases.get(i).ReceivingCaseTime);
                Val.put(temp.COL_HappenCaseDate, tbNoticeCases.get(i).HappenCaseDate);
                Val.put(temp.COL_HappenCaseTime, tbNoticeCases.get(i).HappenCaseTime);
                Val.put(temp.COL_KnowCaseDate, tbNoticeCases.get(i).KnowCaseDate);
                Val.put(temp.COL_KnowCaseTime, tbNoticeCases.get(i).KnowCaseTime);
                Val.put(temp.COL_SceneNoticeDate, tbNoticeCases.get(i).SceneNoticeDate);
                Val.put(temp.COL_SceneNoticeTime, tbNoticeCases.get(i).SceneNoticeTime);
                Val.put(temp.COL_CompleteSceneDate, tbNoticeCases.get(i).CompleteSceneDate);
                Val.put(temp.COL_CompleteSceneTime, tbNoticeCases.get(i).CompleteSceneTime);
                Val.put(temp.COL_LocaleName, tbNoticeCases.get(i).LocaleName);
                Val.put(temp.COL_DISTRICT_ID, tbNoticeCases.get(i).DISTRICT_ID);
                Val.put(temp.COL_AMPHUR_ID, tbNoticeCases.get(i).AMPHUR_ID);
                Val.put(temp.COL_PROVINCE_ID, tbNoticeCases.get(i).PROVINCE_ID);
                Val.put(temp.COL_Latitude, tbNoticeCases.get(i).Latitude);
                Val.put(temp.COL_Longitude, tbNoticeCases.get(i).Longitude);
                Val.put(temp.COL_SuffererPrename, tbNoticeCases.get(i).SuffererPrename);
                Val.put(temp.COL_SuffererName, tbNoticeCases.get(i).SuffererName);
                Val.put(temp.COL_SuffererStatus, tbNoticeCases.get(i).SuffererStatus);
                Val.put(temp.COL_SuffererPhoneNum, tbNoticeCases.get(i).SuffererPhoneNum);
                Val.put(temp.COL_CircumstanceOfCaseDetail, tbNoticeCases.get(i).CircumstanceOfCaseDetail);
                Val.put(temp.COL_LastUpdateDate, tbNoticeCases.get(i).LastUpdateDate);
                Val.put(temp.COL_LastUpdateTime, tbNoticeCases.get(i).LastUpdateTime);

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

            TbSubcaseSceneType temp = new TbSubcaseSceneType();
            String strSQL = "SELECT * FROM subcasescenetype WHERE " + temp.COL_CaseTypeID + " = '" + CaseTypeID + "'";
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

    public String[][] SelectCaseType() {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            TbCaseSceneType temp = new TbCaseSceneType();
            String strSQL = "SELECT * FROM " + temp.TB_CaseSceneType;
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
            Val.put(temp.COL_NoticeCaseID, tbNoticeCases.NoticeCaseID);
            Val.put(temp.COL_Mobile_CaseID, tbNoticeCases.Mobile_CaseID);
            Val.put(temp.COL_InquiryOfficialID, tbNoticeCases.InquiryOfficialID);
            Val.put(temp.COL_InvestigatorOfficialID, tbNoticeCases.InvestigatorOfficialID);
            Val.put(temp.COL_SCDCAgencyCode, tbNoticeCases.SCDCAgencyCode);
            Val.put(temp.COL_CaseTypeID, tbNoticeCases.CaseTypeID);
            Val.put(temp.COL_SubCaseTypeID, tbNoticeCases.SubCaseTypeID);
            Val.put(temp.COL_CaseStatus, tbNoticeCases.CaseStatus);
            Val.put(temp.COL_PoliceStationID, tbNoticeCases.PoliceStationID);
            Val.put(temp.COL_CaseTel, tbNoticeCases.CaseTel);
            Val.put(temp.COL_ReceivingCaseDate, tbNoticeCases.ReceivingCaseDate);
            Val.put(temp.COL_ReceivingCaseTime, tbNoticeCases.ReceivingCaseTime);
            Val.put(temp.COL_HappenCaseDate, tbNoticeCases.HappenCaseDate);
            Val.put(temp.COL_HappenCaseTime, tbNoticeCases.HappenCaseTime);
            Val.put(temp.COL_KnowCaseDate, tbNoticeCases.KnowCaseDate);
            Val.put(temp.COL_KnowCaseTime, tbNoticeCases.KnowCaseTime);
            Val.put(temp.COL_SceneNoticeDate, tbNoticeCases.SceneNoticeDate);
            Val.put(temp.COL_SceneNoticeTime, tbNoticeCases.SceneNoticeTime);
            Val.put(temp.COL_CompleteSceneDate, tbNoticeCases.CompleteSceneDate);
            Val.put(temp.COL_CompleteSceneTime, tbNoticeCases.CompleteSceneTime);
            Val.put(temp.COL_LocaleName, tbNoticeCases.LocaleName);
            Val.put(temp.COL_DISTRICT_ID, tbNoticeCases.DISTRICT_ID);
            Val.put(temp.COL_AMPHUR_ID, tbNoticeCases.AMPHUR_ID);
            Val.put(temp.COL_PROVINCE_ID, tbNoticeCases.PROVINCE_ID);
            Val.put(temp.COL_Latitude, tbNoticeCases.Latitude);
            Val.put(temp.COL_Longitude, tbNoticeCases.Longitude);
            Val.put(temp.COL_SuffererPrename, tbNoticeCases.SuffererPrename);
            Val.put(temp.COL_SuffererName, tbNoticeCases.SuffererName);
            Val.put(temp.COL_SuffererStatus, tbNoticeCases.SuffererStatus);
            Val.put(temp.COL_SuffererPhoneNum, tbNoticeCases.SuffererPhoneNum);
            Val.put(temp.COL_CircumstanceOfCaseDetail, tbNoticeCases.CircumstanceOfCaseDetail);
            Val.put(temp.COL_LastUpdateDate, tbNoticeCases.LastUpdateDate);
            Val.put(temp.COL_LastUpdateTime, tbNoticeCases.LastUpdateTime);

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

            TbNoticeCase temp = new TbNoticeCase();
            ContentValues Val = new ContentValues();
            Val.put(temp.COL_NoticeCaseID, tbNoticeCases.NoticeCaseID);

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

            TbCaseScene temp = new TbCaseScene();
            ContentValues Val = new ContentValues();
            Val.put(temp.COL_CaseReportID, tbCaseScene.CaseReportID);
            Val.put(temp.COL_NoticeCaseID, tbCaseScene.NoticeCaseID);
            Val.put(temp.COL_Mobile_CaseID, tbCaseScene.Mobile_CaseID);
            Val.put(temp.COL_SCDCAgencyCode, tbCaseScene.SCDCAgencyCode);
            Val.put(temp.COL_InvestigatorOfficialID, tbCaseScene.InvestigatorOfficialID);
            Val.put(temp.COL_CaseTypeID, tbCaseScene.CaseTypeID);
            Val.put(temp.COL_SubCaseTypeID, tbCaseScene.SubCaseTypeID);
            Val.put(temp.COL_ReportNo, tbCaseScene.ReportNo);
            Val.put(temp.COL_ReportStatus, tbCaseScene.ReportStatus);
            Val.put(temp.COL_PoliceStationID, tbCaseScene.PoliceStationID);
            Val.put(temp.COL_CaseTel, tbCaseScene.CaseTel);
            Val.put(temp.COL_AssignmentDate, tbCaseScene.AssignmentDate);
            Val.put(temp.COL_AssignmentTime, tbCaseScene.AssignmentTime);
            Val.put(temp.COL_ReceivingCaseDate, tbCaseScene.ReceivingCaseDate);
            Val.put(temp.COL_ReceivingCaseTime, tbCaseScene.ReceivingCaseTime);
            Val.put(temp.COL_HappenCaseDate, tbCaseScene.HappenCaseDate);
            Val.put(temp.COL_HappenCaseTime, tbCaseScene.HappenCaseTime);
            Val.put(temp.COL_KnowCaseDate, tbCaseScene.KnowCaseDate);
            Val.put(temp.COL_KnowCaseTime, tbCaseScene.KnowCaseTime);
            Val.put(temp.COL_CompleteSceneDate, tbCaseScene.CompleteSceneDate);
            Val.put(temp.COL_CompleteSceneTime, tbCaseScene.CompleteSceneTime);
            Val.put(temp.COL_LocaleName, tbCaseScene.LocaleName);
            Val.put(temp.COL_DISTRICT_ID, tbCaseScene.DISTRICT_ID);
            Val.put(temp.COL_AMPHUR_ID, tbCaseScene.AMPHUR_ID);
            Val.put(temp.COL_PROVINCE_ID, tbCaseScene.PROVINCE_ID);
            Val.put(temp.COL_Latitude, tbCaseScene.Latitude);
            Val.put(temp.COL_Longitude, tbCaseScene.Longitude);
            Val.put(temp.COL_FeatureInsideDetail, tbCaseScene.FeatureInsideDetail);
            Val.put(temp.COL_CircumstanceOfCaseDetail, tbCaseScene.CircumstanceOfCaseDetail);
            Val.put(temp.COL_FullEvidencePerformed, tbCaseScene.FullEvidencePerformed);
            Val.put(temp.COL_Annotation, tbCaseScene.Annotation);
            Val.put(temp.COL_MaleCriminalNum, tbCaseScene.MaleCriminalNum);
            Val.put(temp.COL_FemaleCriminalNum, tbCaseScene.FemaleCriminalNum);
            Val.put(temp.COL_ConfineSufferer, tbCaseScene.ConfineSufferer);
            Val.put(temp.COL_SuffererPrename, tbCaseScene.SuffererPrename);
            Val.put(temp.COL_SuffererName, tbCaseScene.SuffererName);
            Val.put(temp.COL_SuffererStatus, tbCaseScene.SuffererStatus);
            Val.put(temp.COL_SuffererPhoneNum, tbCaseScene.SuffererPhoneNum);
            Val.put(temp.COL_CriminalUsedWeapon, tbCaseScene.CriminalUsedWeapon);
            Val.put(temp.COL_VehicleInfo, tbCaseScene.VehicleInfo);
            Val.put(temp.COL_LastUpdateDate, tbCaseScene.LastUpdateDate);
            Val.put(temp.COL_LastUpdateTime, tbCaseScene.LastUpdateTime);

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
                TbCaseScene temp = new TbCaseScene();
                ContentValues Val = new ContentValues();
                Val.put(temp.COL_CaseReportID, apiCaseScene.getTbCaseScene().CaseReportID);
                Val.put(temp.COL_NoticeCaseID, apiCaseScene.getTbCaseScene().NoticeCaseID);
                Val.put(temp.COL_Mobile_CaseID, apiCaseScene.getTbCaseScene().Mobile_CaseID);
                Val.put(temp.COL_SCDCAgencyCode, apiCaseScene.getTbCaseScene().SCDCAgencyCode);
                Val.put(temp.COL_InvestigatorOfficialID, apiCaseScene.getTbCaseScene().InvestigatorOfficialID);
                Val.put(temp.COL_CaseTypeID, apiCaseScene.getTbCaseScene().CaseTypeID);
                Val.put(temp.COL_SubCaseTypeID, apiCaseScene.getTbCaseScene().SubCaseTypeID);
                Val.put(temp.COL_ReportNo, apiCaseScene.getTbCaseScene().ReportNo);
                Val.put(temp.COL_ReportStatus, apiCaseScene.getTbCaseScene().ReportStatus);
                Val.put(temp.COL_PoliceStationID, apiCaseScene.getTbCaseScene().PoliceStationID);
                Val.put(temp.COL_CaseTel, apiCaseScene.getTbCaseScene().CaseTel);
                Val.put(temp.COL_AssignmentDate, apiCaseScene.getTbCaseScene().AssignmentDate);
                Val.put(temp.COL_AssignmentTime, apiCaseScene.getTbCaseScene().AssignmentTime);
                Val.put(temp.COL_ReceivingCaseDate, apiCaseScene.getTbCaseScene().ReceivingCaseDate);
                Val.put(temp.COL_ReceivingCaseTime, apiCaseScene.getTbCaseScene().ReceivingCaseTime);
                Val.put(temp.COL_HappenCaseDate, apiCaseScene.getTbCaseScene().HappenCaseDate);
                Val.put(temp.COL_HappenCaseTime, apiCaseScene.getTbCaseScene().HappenCaseTime);
                Val.put(temp.COL_KnowCaseDate, apiCaseScene.getTbCaseScene().KnowCaseDate);
                Val.put(temp.COL_KnowCaseTime, apiCaseScene.getTbCaseScene().KnowCaseTime);
                Val.put(temp.COL_CompleteSceneDate, apiCaseScene.getTbCaseScene().CompleteSceneDate);
                Val.put(temp.COL_CompleteSceneTime, apiCaseScene.getTbCaseScene().CompleteSceneTime);
                Val.put(temp.COL_LocaleName, apiCaseScene.getTbCaseScene().LocaleName);
                Val.put(temp.COL_DISTRICT_ID, apiCaseScene.getTbCaseScene().DISTRICT_ID);
                Val.put(temp.COL_AMPHUR_ID, apiCaseScene.getTbCaseScene().AMPHUR_ID);
                Val.put(temp.COL_PROVINCE_ID, apiCaseScene.getTbCaseScene().PROVINCE_ID);
                Val.put(temp.COL_Latitude, apiCaseScene.getTbCaseScene().Latitude);
                Val.put(temp.COL_Longitude, apiCaseScene.getTbCaseScene().Longitude);
                Val.put(temp.COL_FeatureInsideDetail, apiCaseScene.getTbCaseScene().FeatureInsideDetail);
                Val.put(temp.COL_CircumstanceOfCaseDetail, apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail);
                Val.put(temp.COL_FullEvidencePerformed, apiCaseScene.getTbCaseScene().FullEvidencePerformed);
                Val.put(temp.COL_Annotation, apiCaseScene.getTbCaseScene().Annotation);
                Val.put(temp.COL_MaleCriminalNum, apiCaseScene.getTbCaseScene().MaleCriminalNum);
                Val.put(temp.COL_FemaleCriminalNum, apiCaseScene.getTbCaseScene().FemaleCriminalNum);
                Val.put(temp.COL_ConfineSufferer, apiCaseScene.getTbCaseScene().ConfineSufferer);
                Val.put(temp.COL_SuffererPrename, apiCaseScene.getTbCaseScene().SuffererPrename);
                Val.put(temp.COL_SuffererName, apiCaseScene.getTbCaseScene().SuffererName);
                Val.put(temp.COL_SuffererStatus, apiCaseScene.getTbCaseScene().SuffererStatus);
                Val.put(temp.COL_SuffererPhoneNum, apiCaseScene.getTbCaseScene().SuffererPhoneNum);
                Val.put(temp.COL_CriminalUsedWeapon, apiCaseScene.getTbCaseScene().CriminalUsedWeapon);
                Val.put(temp.COL_VehicleInfo, apiCaseScene.getTbCaseScene().VehicleInfo);
                Val.put(temp.COL_LastUpdateDate, apiCaseScene.getTbCaseScene().LastUpdateDate);
                Val.put(temp.COL_LastUpdateTime, apiCaseScene.getTbCaseScene().LastUpdateTime);
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
                TbNoticeCase temp = new TbNoticeCase();
                ContentValues Val = new ContentValues();
                Val.put(temp.COL_NoticeCaseID, apiCaseScene.getTbNoticeCase().NoticeCaseID);
                Val.put(temp.COL_Mobile_CaseID, apiCaseScene.getTbNoticeCase().Mobile_CaseID);
                Val.put(temp.COL_InquiryOfficialID, apiCaseScene.getTbNoticeCase().InquiryOfficialID);
                Val.put(temp.COL_InvestigatorOfficialID, apiCaseScene.getTbNoticeCase().InvestigatorOfficialID);
                Val.put(temp.COL_SCDCAgencyCode, apiCaseScene.getTbNoticeCase().SCDCAgencyCode);
                Val.put(temp.COL_CaseTypeID, apiCaseScene.getTbNoticeCase().CaseTypeID);
                Val.put(temp.COL_SubCaseTypeID, apiCaseScene.getTbNoticeCase().SubCaseTypeID);
                Val.put(temp.COL_CaseStatus, apiCaseScene.getTbNoticeCase().CaseStatus);
                Val.put(temp.COL_PoliceStationID, apiCaseScene.getTbNoticeCase().PoliceStationID);
                Val.put(temp.COL_CaseTel, apiCaseScene.getTbNoticeCase().CaseTel);
                Val.put(temp.COL_ReceivingCaseDate, apiCaseScene.getTbNoticeCase().ReceivingCaseDate);
                Val.put(temp.COL_ReceivingCaseTime, apiCaseScene.getTbNoticeCase().ReceivingCaseTime);
                Val.put(temp.COL_HappenCaseDate, apiCaseScene.getTbNoticeCase().HappenCaseDate);
                Val.put(temp.COL_HappenCaseTime, apiCaseScene.getTbNoticeCase().HappenCaseTime);
                Val.put(temp.COL_KnowCaseDate, apiCaseScene.getTbNoticeCase().KnowCaseDate);
                Val.put(temp.COL_KnowCaseTime, apiCaseScene.getTbNoticeCase().KnowCaseTime);
                Val.put(temp.COL_SceneNoticeDate, apiCaseScene.getTbNoticeCase().SceneNoticeDate);
                Val.put(temp.COL_SceneNoticeTime, apiCaseScene.getTbNoticeCase().SceneNoticeTime);
                Val.put(temp.COL_CompleteSceneDate, apiCaseScene.getTbNoticeCase().CompleteSceneDate);
                Val.put(temp.COL_CompleteSceneTime, apiCaseScene.getTbNoticeCase().CompleteSceneTime);
                Val.put(temp.COL_LocaleName, apiCaseScene.getTbNoticeCase().LocaleName);
                Val.put(temp.COL_DISTRICT_ID, apiCaseScene.getTbNoticeCase().DISTRICT_ID);
                Val.put(temp.COL_AMPHUR_ID, apiCaseScene.getTbNoticeCase().AMPHUR_ID);
                Val.put(temp.COL_PROVINCE_ID, apiCaseScene.getTbNoticeCase().PROVINCE_ID);
                Val.put(temp.COL_Latitude, apiCaseScene.getTbNoticeCase().Latitude);
                Val.put(temp.COL_Longitude, apiCaseScene.getTbNoticeCase().Longitude);
                Val.put(temp.COL_SuffererPrename, apiCaseScene.getTbNoticeCase().SuffererPrename);
                Val.put(temp.COL_SuffererName, apiCaseScene.getTbNoticeCase().SuffererName);
                Val.put(temp.COL_SuffererStatus, apiCaseScene.getTbNoticeCase().SuffererStatus);
                Val.put(temp.COL_SuffererPhoneNum, apiCaseScene.getTbNoticeCase().SuffererPhoneNum);
                Val.put(temp.COL_CircumstanceOfCaseDetail, apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail);
                Val.put(temp.COL_LastUpdateDate, apiCaseScene.getTbNoticeCase().LastUpdateDate);
                Val.put(temp.COL_LastUpdateTime, apiCaseScene.getTbNoticeCase().LastUpdateTime);
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
                        TbSceneInvestigation temp = new TbSceneInvestigation();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_SceneInvestID, apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbSceneInvestigations().get(i).CaseReportID);
                        Val.put(temp.COL_SceneInvestDate, apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestDate);
                        Val.put(temp.COL_SceneInvestTime, apiCaseScene.getTbSceneInvestigations().get(i).SceneInvestTime);
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
                    TbSceneFeatureOutside temp = new TbSceneFeatureOutside();
                    ContentValues Val = new ContentValues();
                    Val.put(temp.COL_CaseReportID, apiCaseScene.getTbSceneFeatureOutside().CaseReportID);
                    Val.put(temp.COL_OutsideTypeName, apiCaseScene.getTbSceneFeatureOutside().OutsideTypeName);
                    Val.put(temp.COL_OutsideTypeDetail, apiCaseScene.getTbSceneFeatureOutside().OutsideTypeDetail);
                    Val.put(temp.COL_FloorNum, apiCaseScene.getTbSceneFeatureOutside().FloorNum);
                    Val.put(temp.COL_CaveNum, apiCaseScene.getTbSceneFeatureOutside().CaveNum);
                    Val.put(temp.COL_HaveFence, apiCaseScene.getTbSceneFeatureOutside().HaveFence);
                    Val.put(temp.COL_HaveMezzanine, apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine);
                    Val.put(temp.COL_HaveRooftop, apiCaseScene.getTbSceneFeatureOutside().HaveRooftop);
                    Val.put(temp.COL_FrontSide, apiCaseScene.getTbSceneFeatureOutside().FrontSide);
                    Val.put(temp.COL_LeftSide, apiCaseScene.getTbSceneFeatureOutside().LeftSide);
                    Val.put(temp.COL_RightSide, apiCaseScene.getTbSceneFeatureOutside().RightSide);
                    Val.put(temp.COL_BackSide, apiCaseScene.getTbSceneFeatureOutside().BackSide);
                    Val.put(temp.COL_SceneZone, apiCaseScene.getTbSceneFeatureOutside().SceneZone);
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
                        TbSceneFeatureInSide temp = new TbSceneFeatureInSide();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_FeatureInsideID, apiCaseScene.getTbSceneFeatureInSide().get(i).FeatureInsideID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbSceneFeatureInSide().get(i).CaseReportID);
                        Val.put(temp.COL_FloorNo, apiCaseScene.getTbSceneFeatureInSide().get(i).FloorNo);
                        Val.put(temp.COL_CaveNo, apiCaseScene.getTbSceneFeatureInSide().get(i).CaveNo);
                        Val.put(temp.COL_FrontInside, apiCaseScene.getTbSceneFeatureInSide().get(i).FrontInside);
                        Val.put(temp.COL_LeftInside, apiCaseScene.getTbSceneFeatureInSide().get(i).LeftInside);
                        Val.put(temp.COL_RightInside, apiCaseScene.getTbSceneFeatureInSide().get(i).RightInside);
                        Val.put(temp.COL_BackInside, apiCaseScene.getTbSceneFeatureInSide().get(i).BackInside);
                        Val.put(temp.COL_CenterInside, apiCaseScene.getTbSceneFeatureInSide().get(i).CenterInside);
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
            if (apiCaseScene.getTbMultimediaFiles() != null) {
                for (int i = 0; i < apiCaseScene.getTbMultimediaFiles().size(); i++) {
                    strSQL = "SELECT * FROM multimediaFile "
                            + " WHERE FileID = '" + apiCaseScene.getTbMultimediaFiles().get(i).FileID + "' " +
                            "AND CaseReportID = '" + sCaseReportID + "'";
                    try (Cursor cursor = mDb.rawQuery(strSQL, null)) {
                        TbMultimediaFile temp = new TbMultimediaFile();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_FileID, apiCaseScene.getTbMultimediaFiles().get(i).FileID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbMultimediaFiles().get(i).CaseReportID);
                        Val.put(temp.COL_FileType, apiCaseScene.getTbMultimediaFiles().get(i).FileType);
                        Val.put(temp.COL_FilePath, apiCaseScene.getTbMultimediaFiles().get(i).FilePath);
                        Val.put(temp.COL_FileDescription, apiCaseScene.getTbMultimediaFiles().get(i).FileDescription);
                        Val.put(temp.COL_Timestamp, apiCaseScene.getTbMultimediaFiles().get(i).Timestamp);
                        if (cursor.getCount() == 0) { // กรณีไม่เคยมีข้อมูลนี้
                            db.insert("MultimediaFile", null, Val);
                            Log.d(TAG, "Sync Table MultimediaFile [" + i + "]: Insert ");
                        } else if (cursor.getCount() == 1) { // กรณีเคยมีข้อมูลแล้ว
                            db.update("MultimediaFile", Val, " FileID = ?", new String[]{String.valueOf(apiCaseScene.getTbMultimediaFiles().get(i).FileID)});
                            Log.d(TAG, "Sync Table MultimediaFile [" + i + "]: Update ");
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
                        TbFindEvidence temp = new TbFindEvidence();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_FindEvidenceID, apiCaseScene.getTbFindEvidences().get(i).FindEvidenceID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbFindEvidences().get(i).CaseReportID);
                        Val.put(temp.COL_SceneInvestID, apiCaseScene.getTbFindEvidences().get(i).SceneInvestID);
                        Val.put(temp.COL_EvidenceTypeID, apiCaseScene.getTbFindEvidences().get(i).EvidenceTypeID);
                        Val.put(temp.COL_EvidenceNumber, apiCaseScene.getTbFindEvidences().get(i).EvidenceNumber);
                        Val.put(temp.COL_FindEvidenceZone, apiCaseScene.getTbFindEvidences().get(i).FindEvidenceZone);
                        Val.put(temp.COL_FindEvidencecol, apiCaseScene.getTbFindEvidences().get(i).FindEvidencecol);
                        Val.put(temp.COL_Marking, apiCaseScene.getTbFindEvidences().get(i).Marking);
                        Val.put(temp.COL_Parceling, apiCaseScene.getTbFindEvidences().get(i).Parceling);
                        Val.put(temp.COL_EvidencePerformed, apiCaseScene.getTbFindEvidences().get(i).EvidencePerformed);
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
                        TbResultScene temp = new TbResultScene();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_RSID, apiCaseScene.getTbResultScenes().get(i).RSID);
                        Val.put(temp.COL_RSTypeID, apiCaseScene.getTbResultScenes().get(i).RSTypeID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbResultScenes().get(i).CaseReportID);
                        Val.put(temp.COL_RSDetail, apiCaseScene.getTbResultScenes().get(i).RSDetail);
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
                        TbResultScene temp = new TbResultScene();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_RSID, apiCaseScene.getTbGatewayCriminals().get(i).RSID);
                        Val.put(temp.COL_RSTypeID, apiCaseScene.getTbGatewayCriminals().get(i).RSTypeID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbGatewayCriminals().get(i).CaseReportID);
                        Val.put(temp.COL_RSDetail, apiCaseScene.getTbGatewayCriminals().get(i).RSDetail);
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
                        TbResultScene temp = new TbResultScene();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_RSID, apiCaseScene.getTbClueShowns().get(i).RSID);
                        Val.put(temp.COL_RSTypeID, apiCaseScene.getTbClueShowns().get(i).RSTypeID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbClueShowns().get(i).CaseReportID);
                        Val.put(temp.COL_RSDetail, apiCaseScene.getTbClueShowns().get(i).RSDetail);
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
                        TbPropertyLoss temp = new TbPropertyLoss();
                        ContentValues Val = new ContentValues();
                        Val.put(temp.COL_PropertyLossID, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossID);
                        Val.put(temp.COL_CaseReportID, apiCaseScene.getTbPropertyLosses().get(i).CaseReportID);
                        Val.put(temp.COL_PropertyLossName, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossName);
                        Val.put(temp.COL_PropertyLossNumber, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossNumber);
                        Val.put(temp.COL_PropertyLossUnit, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossUnit);
                        Val.put(temp.COL_PropertyLossPosition, apiCaseScene.getTbPropertyLosses().get(i).PropertyLossPosition);
                        Val.put(temp.COL_PropInsurance, apiCaseScene.getTbPropertyLosses().get(i).PropInsurance);
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
                    + " WHERE InquiryOfficialID = '" + OfficeID + "'";

            try (Cursor cursor = db.rawQuery(strSQL_main, null)) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    ApiNoticeCase apiNoticeCase = new ApiNoticeCase();

                    TbNoticeCase temp = new TbNoticeCase();
                    temp.NoticeCaseID = cursor.getString(cursor.getColumnIndex(temp.COL_NoticeCaseID));
                    temp.Mobile_CaseID = cursor.getString(cursor.getColumnIndex(temp.COL_Mobile_CaseID));
                    temp.InquiryOfficialID = cursor.getString(cursor.getColumnIndex(temp.COL_InquiryOfficialID));
                    temp.InvestigatorOfficialID = cursor.getString(cursor.getColumnIndex(temp.COL_InvestigatorOfficialID));
                    temp.SCDCAgencyCode = cursor.getString(cursor.getColumnIndex(temp.COL_SCDCAgencyCode));
                    temp.CaseTypeID = cursor.getString(cursor.getColumnIndex(temp.COL_CaseTypeID));
                    temp.SubCaseTypeID = cursor.getString(cursor.getColumnIndex(temp.COL_SubCaseTypeID));
                    temp.CaseStatus = cursor.getString(cursor.getColumnIndex(temp.COL_CaseStatus));
                    temp.PoliceStationID = cursor.getString(cursor.getColumnIndex(temp.COL_PoliceStationID));
                    temp.CaseTel = cursor.getString(cursor.getColumnIndex(temp.COL_CaseTel));
                    temp.ReceivingCaseDate = cursor.getString(cursor.getColumnIndex(temp.COL_ReceivingCaseDate));
                    temp.ReceivingCaseTime = cursor.getString(cursor.getColumnIndex(temp.COL_ReceivingCaseTime));
                    temp.HappenCaseDate = cursor.getString(cursor.getColumnIndex(temp.COL_HappenCaseDate));
                    temp.HappenCaseTime = cursor.getString(cursor.getColumnIndex(temp.COL_HappenCaseTime));
                    temp.KnowCaseDate = cursor.getString(cursor.getColumnIndex(temp.COL_KnowCaseDate));
                    temp.KnowCaseTime = cursor.getString(cursor.getColumnIndex(temp.COL_KnowCaseTime));
                    temp.SceneNoticeDate = cursor.getString(cursor.getColumnIndex(temp.COL_SceneNoticeDate));
                    temp.SceneNoticeTime = cursor.getString(cursor.getColumnIndex(temp.COL_SceneNoticeTime));
                    temp.CompleteSceneDate = cursor.getString(cursor.getColumnIndex(temp.COL_CompleteSceneDate));
                    temp.CompleteSceneTime = cursor.getString(cursor.getColumnIndex(temp.COL_CompleteSceneTime));
                    temp.LocaleName = cursor.getString(cursor.getColumnIndex(temp.COL_LocaleName));
                    temp.DISTRICT_ID = cursor.getString(cursor.getColumnIndex(temp.COL_DISTRICT_ID));
                    temp.AMPHUR_ID = cursor.getString(cursor.getColumnIndex(temp.COL_AMPHUR_ID));
                    temp.PROVINCE_ID = cursor.getString(cursor.getColumnIndex(temp.COL_PROVINCE_ID));
                    temp.Latitude = cursor.getString(cursor.getColumnIndex(temp.COL_Latitude));
                    temp.Longitude = cursor.getString(cursor.getColumnIndex(temp.COL_Longitude));
                    temp.SuffererPrename = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererPrename));
                    temp.SuffererName = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererName));
                    temp.SuffererStatus = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererStatus));
                    temp.SuffererPhoneNum = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererPhoneNum));
                    temp.CircumstanceOfCaseDetail = cursor.getString(cursor.getColumnIndex(temp.COL_CircumstanceOfCaseDetail));
                    temp.LastUpdateDate = cursor.getString(cursor.getColumnIndex(temp.COL_LastUpdateDate));
                    temp.LastUpdateTime = cursor.getString(cursor.getColumnIndex(temp.COL_LastUpdateTime));
                    apiNoticeCase.setTbNoticeCase(temp);

                    // Index tbOfficial ดึงจากตาราง official
                    strSQL = "SELECT * FROM official "
                            + " WHERE OfficialID = '" + temp.InvestigatorOfficialID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbOfficial temp2 = new TbOfficial();
                            temp2.OfficialID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_OfficialID));
                            temp2.FirstName = cursor2.getString(cursor2.getColumnIndex(temp2.COL_FirstName));
                            temp2.LastName = cursor2.getString(cursor2.getColumnIndex(temp2.COL_LastName));
                            temp2.Alias = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Alias));
                            temp2.Rank = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Rank));
                            temp2.Position = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Position));
                            temp2.SubPossition = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SubPossition));
                            temp2.PhoneNumber = cursor2.getString(cursor2.getColumnIndex(temp2.COL_PhoneNumber));
                            temp2.OfficialEmail = cursor2.getString(cursor2.getColumnIndex(temp2.COL_OfficialEmail));
                            temp2.OfficialDisplayPic = cursor2.getString(cursor2.getColumnIndex(temp2.COL_OfficialDisplayPic));
                            temp2.AccessType = cursor2.getString(cursor2.getColumnIndex(temp2.COL_AccessType));
                            temp2.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SCDCAgencyCode));
                            temp2.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_PoliceStationID));
                            temp2.id_users = cursor2.getString(cursor2.getColumnIndex(temp2.COL_id_users));
                            apiNoticeCase.setTbOfficial(temp2);
                        }
                    }

                    // Index tbCaseSceneType ดึงจากตาราง casescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM casescenetype "
                            + " WHERE CaseTypeID = '" + temp.CaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();

                            TbCaseSceneType temp3 = new TbCaseSceneType();
                            temp3.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp3.COL_CaseTypeID));
                            temp3.CaseTypeName = cursor2.getString(cursor2.getColumnIndex(temp3.COL_CaseTypeName));
                            temp3.casetype_min = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_min));
                            temp3.casetype_max = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_max));
                            temp3.casetype_icon = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_icon));
                            temp3.casetype_colormin = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_colormin));
                            temp3.casetype_colormedium = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_colormedium));
                            temp3.casetype_colorhigh = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_colorhigh));
                            temp3.casetype_status = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_status));
                            apiNoticeCase.setTbCaseSceneType(temp3);
                        }
                    }

                    // Index tbSubcaseSceneType ดึงจากตาราง subcasescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM subcasescenetype "
                            + " WHERE SubCaseTypeID = '" + temp.SubCaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbSubcaseSceneType temp4 = new TbSubcaseSceneType();
                            temp4.SubCaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp4.COL_SubCaseTypeID));
                            temp4.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp4.COL_CaseTypeID));
                            temp4.SubCaseTypeName = cursor2.getString(cursor2.getColumnIndex(temp4.COL_SubCaseTypeName));
                            apiNoticeCase.setTbSubcaseSceneType(temp4);
                        }
                    }

                    // Index tbPoliceStation ดึงจากตาราง policestation ใช้ค่าจาก Index tbNoticeCase
                    TbPoliceStation temp5 = new TbPoliceStation();
                    strSQL = "SELECT * FROM policestation "
                            + " WHERE PoliceStationID = '" + temp.PoliceStationID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp5.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(temp5.COL_PoliceStationID));
                            temp5.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(temp5.COL_PoliceAgencyID));
                            temp5.PoliceStationName = cursor2.getString(cursor2.getColumnIndex(temp5.COL_PoliceStationName));
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
                                temp6.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(temp6.COL_PoliceAgencyID));
                                temp6.PoliceCenterID = cursor2.getString(cursor2.getColumnIndex(temp6.COL_PoliceCenterID));
                                temp6.PoliceAgencyName = cursor2.getString(cursor2.getColumnIndex(temp6.COL_PoliceAgencyName));
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
                            temp7.DISTRICT_ID = cursor2.getString(cursor2.getColumnIndex(temp7.COL_DISTRICT_ID));
                            temp7.DISTRICT_CODE = cursor2.getString(cursor2.getColumnIndex(temp7.COL_DISTRICT_CODE));
                            temp7.DISTRICT_NAME = cursor2.getString(cursor2.getColumnIndex(temp7.COL_DISTRICT_NAME));
                            temp7.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(temp7.COL_AMPHUR_ID));
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
                            temp8.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(temp8.COL_PROVINCE_ID));
                            temp8.PROVINCE_CODE = cursor2.getString(cursor2.getColumnIndex(temp8.COL_PROVINCE_CODE));
                            temp8.PROVINCE_NAME = cursor2.getString(cursor2.getColumnIndex(temp8.COL_PROVINCE_NAME));
                            temp8.GEO_ID = cursor2.getString(cursor2.getColumnIndex(temp8.COL_GEO_ID));
                            temp8.province_status = cursor2.getString(cursor2.getColumnIndex(temp8.COL_province_status));
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
                            temp9.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(temp9.COL_AMPHUR_ID));
                            temp9.AMPHUR_CODE = cursor2.getString(cursor2.getColumnIndex(temp9.COL_AMPHUR_CODE));
                            temp9.AMPHUR_NAME = cursor2.getString(cursor2.getColumnIndex(temp9.COL_AMPHUR_NAME));
                            temp9.POSTCODE = cursor2.getString(cursor2.getColumnIndex(temp9.COL_POSTCODE));
                            temp9.GEO_ID = cursor2.getString(cursor2.getColumnIndex(temp9.COL_GEO_ID));
                            temp9.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(temp9.COL_PROVINCE_ID));
                            temp9.amphur_polygon = cursor2.getString(cursor2.getColumnIndex(temp9.COL_amphur_polygon));
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
                            temp10.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(temp10.COL_SCDCAgencyCode));
                            temp10.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(temp10.COL_SCDCCenterID));
                            temp10.SCDCAgencyName = cursor2.getString(cursor2.getColumnIndex(temp10.COL_SCDCAgencyName));
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
                                temp11.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(temp11.COL_SCDCCenterID));
                                temp11.SCDCCenterName = cursor2.getString(cursor2.getColumnIndex(temp11.COL_SCDCCenterName));
                                temp11.SCDCCenterProvince = cursor2.getString(cursor2.getColumnIndex(temp11.COL_SCDCCenterProvince));
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
                    + " WHERE InvestigatorOfficialID = '" + OfficeID + "'";

            try (Cursor cursor = db.rawQuery(strSQL_main, null)) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    ApiCaseScene apiCaseSceneCase = new ApiCaseScene();

                    TbCaseScene temp = new TbCaseScene();
                    temp.CaseReportID = cursor.getString(cursor.getColumnIndex(temp.COL_CaseReportID));
                    temp.NoticeCaseID = cursor.getString(cursor.getColumnIndex(temp.COL_NoticeCaseID));
                    temp.Mobile_CaseID = cursor.getString(cursor.getColumnIndex(temp.COL_Mobile_CaseID));
                    temp.SCDCAgencyCode = cursor.getString(cursor.getColumnIndex(temp.COL_SCDCAgencyCode));
                    temp.InvestigatorOfficialID = cursor.getString(cursor.getColumnIndex(temp.COL_InvestigatorOfficialID));
                    temp.CaseTypeID = cursor.getString(cursor.getColumnIndex(temp.COL_CaseTypeID));
                    temp.SubCaseTypeID = cursor.getString(cursor.getColumnIndex(temp.COL_SubCaseTypeID));
                    temp.ReportNo = cursor.getString(cursor.getColumnIndex(temp.COL_ReportNo));
                    temp.ReportStatus = cursor.getString(cursor.getColumnIndex(temp.COL_ReportStatus));
                    temp.PoliceStationID = cursor.getString(cursor.getColumnIndex(temp.COL_PoliceStationID));
                    temp.CaseTel = cursor.getString(cursor.getColumnIndex(temp.COL_CaseTel));
                    temp.AssignmentDate = cursor.getString(cursor.getColumnIndex(temp.COL_AssignmentDate));
                    temp.AssignmentTime = cursor.getString(cursor.getColumnIndex(temp.COL_AssignmentTime));
                    temp.ReceivingCaseDate = cursor.getString(cursor.getColumnIndex(temp.COL_ReceivingCaseDate));
                    temp.ReceivingCaseTime = cursor.getString(cursor.getColumnIndex(temp.COL_ReceivingCaseTime));
                    temp.HappenCaseDate = cursor.getString(cursor.getColumnIndex(temp.COL_HappenCaseDate));
                    temp.HappenCaseTime = cursor.getString(cursor.getColumnIndex(temp.COL_HappenCaseTime));
                    temp.KnowCaseDate = cursor.getString(cursor.getColumnIndex(temp.COL_KnowCaseDate));
                    temp.KnowCaseTime = cursor.getString(cursor.getColumnIndex(temp.COL_KnowCaseTime));
                    temp.CompleteSceneDate = cursor.getString(cursor.getColumnIndex(temp.COL_CompleteSceneDate));
                    temp.CompleteSceneTime = cursor.getString(cursor.getColumnIndex(temp.COL_CompleteSceneTime));
                    temp.LocaleName = cursor.getString(cursor.getColumnIndex(temp.COL_LocaleName));
                    temp.DISTRICT_ID = cursor.getString(cursor.getColumnIndex(temp.COL_DISTRICT_ID));
                    temp.AMPHUR_ID = cursor.getString(cursor.getColumnIndex(temp.COL_AMPHUR_ID));
                    temp.PROVINCE_ID = cursor.getString(cursor.getColumnIndex(temp.COL_PROVINCE_ID));
                    temp.Latitude = cursor.getString(cursor.getColumnIndex(temp.COL_Latitude));
                    temp.Longitude = cursor.getString(cursor.getColumnIndex(temp.COL_Longitude));
                    temp.FeatureInsideDetail = cursor.getString(cursor.getColumnIndex(temp.COL_FeatureInsideDetail));
                    temp.CircumstanceOfCaseDetail = cursor.getString(cursor.getColumnIndex(temp.COL_CircumstanceOfCaseDetail));
                    temp.FullEvidencePerformed = cursor.getString(cursor.getColumnIndex(temp.COL_FullEvidencePerformed));
                    temp.Annotation = cursor.getString(cursor.getColumnIndex(temp.COL_Annotation));
                    temp.MaleCriminalNum = cursor.getString(cursor.getColumnIndex(temp.COL_MaleCriminalNum));
                    temp.FemaleCriminalNum = cursor.getString(cursor.getColumnIndex(temp.COL_FemaleCriminalNum));
                    temp.ConfineSufferer = cursor.getString(cursor.getColumnIndex(temp.COL_ConfineSufferer));
                    temp.SuffererPrename = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererPrename));
                    temp.SuffererName = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererName));
                    temp.SuffererStatus = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererStatus));
                    temp.SuffererPhoneNum = cursor.getString(cursor.getColumnIndex(temp.COL_SuffererPhoneNum));
                    temp.CriminalUsedWeapon = cursor.getString(cursor.getColumnIndex(temp.COL_CriminalUsedWeapon));
                    temp.VehicleInfo = cursor.getString(cursor.getColumnIndex(temp.COL_VehicleInfo));
                    temp.LastUpdateDate = cursor.getString(cursor.getColumnIndex(temp.COL_LastUpdateDate));
                    temp.LastUpdateTime = cursor.getString(cursor.getColumnIndex(temp.COL_LastUpdateTime));
                    apiCaseSceneCase.setTbCaseScene(temp);
                    Log.d(TAG, "NoticeCaseID " + temp.NoticeCaseID.toString());
                    // Index TbCaseScene ดึงจากตาราง noticecase
                    strSQL = "SELECT * FROM noticecase "
                            + " WHERE NoticeCaseID = '" + temp.NoticeCaseID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbNoticeCase temp2 = new TbNoticeCase();
                            temp2.NoticeCaseID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_NoticeCaseID));
                            temp2.Mobile_CaseID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Mobile_CaseID));
                            temp2.InquiryOfficialID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_InquiryOfficialID));
                            temp2.InvestigatorOfficialID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_InvestigatorOfficialID));
                            temp2.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SCDCAgencyCode));
                            temp2.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_CaseTypeID));
                            temp2.SubCaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SubCaseTypeID));
                            temp2.CaseStatus = cursor2.getString(cursor2.getColumnIndex(temp2.COL_CaseStatus));
                            temp2.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_PoliceStationID));
                            temp2.CaseTel = cursor2.getString(cursor2.getColumnIndex(temp2.COL_CaseTel));
                            temp2.ReceivingCaseDate = cursor2.getString(cursor2.getColumnIndex(temp2.COL_ReceivingCaseDate));
                            temp2.ReceivingCaseTime = cursor2.getString(cursor2.getColumnIndex(temp2.COL_ReceivingCaseTime));
                            temp2.HappenCaseDate = cursor2.getString(cursor2.getColumnIndex(temp2.COL_HappenCaseDate));
                            temp2.HappenCaseTime = cursor2.getString(cursor2.getColumnIndex(temp2.COL_HappenCaseTime));
                            temp2.KnowCaseDate = cursor2.getString(cursor2.getColumnIndex(temp2.COL_KnowCaseDate));
                            temp2.KnowCaseTime = cursor2.getString(cursor2.getColumnIndex(temp2.COL_KnowCaseTime));
                            temp2.SceneNoticeDate = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SceneNoticeDate));
                            temp2.SceneNoticeTime = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SceneNoticeTime));
                            temp2.CompleteSceneDate = cursor2.getString(cursor2.getColumnIndex(temp2.COL_CompleteSceneDate));
                            temp2.CompleteSceneTime = cursor2.getString(cursor2.getColumnIndex(temp2.COL_CompleteSceneTime));
                            temp2.LocaleName = cursor2.getString(cursor2.getColumnIndex(temp2.COL_LocaleName));
                            temp2.DISTRICT_ID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_DISTRICT_ID));
                            temp2.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_AMPHUR_ID));
                            temp2.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_PROVINCE_ID));
                            temp2.Latitude = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Latitude));
                            temp2.Longitude = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Longitude));
                            temp2.SuffererPrename = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SuffererPrename));
                            temp2.SuffererName = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SuffererName));
                            temp2.SuffererStatus = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SuffererStatus));
                            temp2.SuffererPhoneNum = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SuffererPhoneNum));
                            temp2.CircumstanceOfCaseDetail = cursor2.getString(cursor2.getColumnIndex(temp2.COL_CircumstanceOfCaseDetail));
                            temp2.LastUpdateDate = cursor2.getString(cursor2.getColumnIndex(temp2.COL_LastUpdateDate));
                            temp2.LastUpdateTime = cursor2.getString(cursor2.getColumnIndex(temp2.COL_LastUpdateTime));
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
                            temp2.OfficialID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_OfficialID));
                            temp2.FirstName = cursor2.getString(cursor2.getColumnIndex(temp2.COL_FirstName));
                            temp2.LastName = cursor2.getString(cursor2.getColumnIndex(temp2.COL_LastName));
                            temp2.Alias = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Alias));
                            temp2.Rank = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Rank));
                            temp2.Position = cursor2.getString(cursor2.getColumnIndex(temp2.COL_Position));
                            temp2.SubPossition = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SubPossition));
                            temp2.PhoneNumber = cursor2.getString(cursor2.getColumnIndex(temp2.COL_PhoneNumber));
                            temp2.OfficialEmail = cursor2.getString(cursor2.getColumnIndex(temp2.COL_OfficialEmail));
                            temp2.OfficialDisplayPic = cursor2.getString(cursor2.getColumnIndex(temp2.COL_OfficialDisplayPic));
                            temp2.AccessType = cursor2.getString(cursor2.getColumnIndex(temp2.COL_AccessType));
                            temp2.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(temp2.COL_SCDCAgencyCode));
                            temp2.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(temp2.COL_PoliceStationID));
                            temp2.id_users = cursor2.getString(cursor2.getColumnIndex(temp2.COL_id_users));
                            apiCaseSceneCase.setTbOfficial(temp2);
                        }
                    }

                    // Index tbCaseSceneType ดึงจากตาราง casescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM casescenetype "
                            + " WHERE CaseTypeID = '" + temp.CaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();

                            TbCaseSceneType temp3 = new TbCaseSceneType();
                            temp3.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp3.COL_CaseTypeID));
                            temp3.CaseTypeName = cursor2.getString(cursor2.getColumnIndex(temp3.COL_CaseTypeName));
                            temp3.casetype_min = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_min));
                            temp3.casetype_max = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_max));
                            temp3.casetype_icon = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_icon));
                            temp3.casetype_colormin = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_colormin));
                            temp3.casetype_colormedium = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_colormedium));
                            temp3.casetype_colorhigh = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_colorhigh));
                            temp3.casetype_status = cursor2.getString(cursor2.getColumnIndex(temp3.COL_casetype_status));
                            apiCaseSceneCase.setTbCaseSceneType(temp3);
                        }
                    }

                    // Index tbSubcaseSceneType ดึงจากตาราง subcasescenetype ใช้ค่าจาก Index tbNoticeCase
                    strSQL = "SELECT * FROM subcasescenetype "
                            + " WHERE SubCaseTypeID = '" + temp.SubCaseTypeID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            TbSubcaseSceneType temp4 = new TbSubcaseSceneType();
                            temp4.SubCaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp4.COL_SubCaseTypeID));
                            temp4.CaseTypeID = cursor2.getString(cursor2.getColumnIndex(temp4.COL_CaseTypeID));
                            temp4.SubCaseTypeName = cursor2.getString(cursor2.getColumnIndex(temp4.COL_SubCaseTypeName));
                            apiCaseSceneCase.setTbSubcaseSceneType(temp4);
                        }
                    }

                    // Index tbPoliceStation ดึงจากตาราง policestation ใช้ค่าจาก Index tbNoticeCase
                    TbPoliceStation temp5 = new TbPoliceStation();
                    strSQL = "SELECT * FROM policestation "
                            + " WHERE PoliceStationID = '" + temp.PoliceStationID + "'";
                    try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                        if (cursor2.getCount() == 1) {
                            cursor2.moveToFirst();
                            temp5.PoliceStationID = cursor2.getString(cursor2.getColumnIndex(temp5.COL_PoliceStationID));
                            temp5.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(temp5.COL_PoliceAgencyID));
                            temp5.PoliceStationName = cursor2.getString(cursor2.getColumnIndex(temp5.COL_PoliceStationName));
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
                                temp6.PoliceAgencyID = cursor2.getString(cursor2.getColumnIndex(temp6.COL_PoliceAgencyID));
                                temp6.PoliceCenterID = cursor2.getString(cursor2.getColumnIndex(temp6.COL_PoliceCenterID));
                                temp6.PoliceAgencyName = cursor2.getString(cursor2.getColumnIndex(temp6.COL_PoliceAgencyName));
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
                            temp7.DISTRICT_ID = cursor2.getString(cursor2.getColumnIndex(temp7.COL_DISTRICT_ID));
                            temp7.DISTRICT_CODE = cursor2.getString(cursor2.getColumnIndex(temp7.COL_DISTRICT_CODE));
                            temp7.DISTRICT_NAME = cursor2.getString(cursor2.getColumnIndex(temp7.COL_DISTRICT_NAME));
                            temp7.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(temp7.COL_AMPHUR_ID));
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
                            temp8.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(temp8.COL_PROVINCE_ID));
                            temp8.PROVINCE_CODE = cursor2.getString(cursor2.getColumnIndex(temp8.COL_PROVINCE_CODE));
                            temp8.PROVINCE_NAME = cursor2.getString(cursor2.getColumnIndex(temp8.COL_PROVINCE_NAME));
                            temp8.GEO_ID = cursor2.getString(cursor2.getColumnIndex(temp8.COL_GEO_ID));
                            temp8.province_status = cursor2.getString(cursor2.getColumnIndex(temp8.COL_province_status));
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
                            temp9.AMPHUR_ID = cursor2.getString(cursor2.getColumnIndex(temp9.COL_AMPHUR_ID));
                            temp9.AMPHUR_CODE = cursor2.getString(cursor2.getColumnIndex(temp9.COL_AMPHUR_CODE));
                            temp9.AMPHUR_NAME = cursor2.getString(cursor2.getColumnIndex(temp9.COL_AMPHUR_NAME));
                            temp9.POSTCODE = cursor2.getString(cursor2.getColumnIndex(temp9.COL_POSTCODE));
                            temp9.GEO_ID = cursor2.getString(cursor2.getColumnIndex(temp9.COL_GEO_ID));
                            temp9.PROVINCE_ID = cursor2.getString(cursor2.getColumnIndex(temp9.COL_PROVINCE_ID));
                            temp9.amphur_polygon = cursor2.getString(cursor2.getColumnIndex(temp9.COL_amphur_polygon));
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
                            temp10.SCDCAgencyCode = cursor2.getString(cursor2.getColumnIndex(temp10.COL_SCDCAgencyCode));
                            temp10.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(temp10.COL_SCDCCenterID));
                            temp10.SCDCAgencyName = cursor2.getString(cursor2.getColumnIndex(temp10.COL_SCDCAgencyName));
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
                                temp11.SCDCCenterID = cursor2.getString(cursor2.getColumnIndex(temp11.COL_SCDCCenterID));
                                temp11.SCDCCenterName = cursor2.getString(cursor2.getColumnIndex(temp11.COL_SCDCCenterName));
                                temp11.SCDCCenterProvince = cursor2.getString(cursor2.getColumnIndex(temp11.COL_SCDCCenterProvince));
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
                                    temp12.SceneInvestID = cursor2.getString(cursor2.getColumnIndex(temp12.COL_SceneInvestID));
                                    temp12.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp12.COL_CaseReportID));
                                    temp12.SceneInvestDate = cursor2.getString(cursor2.getColumnIndex(temp12.COL_SceneInvestDate));
                                    temp12.SceneInvestTime = cursor2.getString(cursor2.getColumnIndex(temp12.COL_SceneInvestTime));
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
                                temp13.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp13.COL_CaseReportID));
                                temp13.OutsideTypeName = cursor2.getString(cursor2.getColumnIndex(temp13.COL_OutsideTypeName));
                                temp13.OutsideTypeDetail = cursor2.getString(cursor2.getColumnIndex(temp13.COL_OutsideTypeDetail));
                                temp13.FloorNum = cursor2.getString(cursor2.getColumnIndex(temp13.COL_FloorNum));
                                temp13.CaveNum = cursor2.getString(cursor2.getColumnIndex(temp13.COL_CaveNum));
                                temp13.HaveFence = cursor2.getString(cursor2.getColumnIndex(temp13.COL_HaveFence));
                                temp13.HaveMezzanine = cursor2.getString(cursor2.getColumnIndex(temp13.COL_HaveMezzanine));
                                temp13.HaveRooftop = cursor2.getString(cursor2.getColumnIndex(temp13.COL_HaveRooftop));
                                temp13.FrontSide = cursor2.getString(cursor2.getColumnIndex(temp13.COL_FrontSide));
                                temp13.LeftSide = cursor2.getString(cursor2.getColumnIndex(temp13.COL_LeftSide));
                                temp13.RightSide = cursor2.getString(cursor2.getColumnIndex(temp13.COL_RightSide));
                                temp13.BackSide = cursor2.getString(cursor2.getColumnIndex(temp13.COL_BackSide));
                                temp13.SceneZone = cursor2.getString(cursor2.getColumnIndex(temp13.COL_SceneZone));
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
                                    temp14.FeatureInsideID = cursor2.getString(cursor2.getColumnIndex(temp14.COL_FeatureInsideID));
                                    temp14.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp14.COL_CaseReportID));
                                    temp14.FloorNo = cursor2.getString(cursor2.getColumnIndex(temp14.COL_FloorNo));
                                    temp14.CaveNo = cursor2.getString(cursor2.getColumnIndex(temp14.COL_CaveNo));
                                    temp14.FrontInside = cursor2.getString(cursor2.getColumnIndex(temp14.COL_FrontInside));
                                    temp14.LeftInside = cursor2.getString(cursor2.getColumnIndex(temp14.COL_LeftInside));
                                    temp14.RightInside = cursor2.getString(cursor2.getColumnIndex(temp14.COL_RightInside));
                                    temp14.BackInside = cursor2.getString(cursor2.getColumnIndex(temp14.COL_BackInside));
                                    temp14.CenterInside = cursor2.getString(cursor2.getColumnIndex(temp14.COL_CenterInside));
                                    tbSceneFeatureInSides.add(temp14);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbSceneFeatureInSide(tbSceneFeatureInSides);

                    }
                    // Index tbMultimediaFiles ดึงจากตาราง multimediafile ใช้ค่าจาก Index tbCaseScene
                    if (temp.CaseReportID != null) {
                        strSQL = "SELECT * FROM multimediafile "
                                + " WHERE CaseReportID = '" + temp.CaseReportID + "'";
                        List<TbMultimediaFile> tbMultimediaFiles = null;
                        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
                            if (tbMultimediaFiles == null) {
                                tbMultimediaFiles = new ArrayList<>(cursor2.getCount());
                            }
                            if (cursor2.getCount() > 0) {
                                cursor2.moveToPosition(-1);
                                while (cursor2.moveToNext()) {
                                    TbMultimediaFile temp15 = new TbMultimediaFile();
                                    temp15.FileID = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FileID));
                                    temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp15.COL_CaseReportID));
                                    temp15.FileType = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FileType));
                                    temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FilePath));
                                    temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FileDescription));
                                    temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(temp15.COL_Timestamp));
                                    tbMultimediaFiles.add(temp15);
                                }
                            }
                        }
                        apiCaseSceneCase.setTbMultimediaFiles(tbMultimediaFiles);

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
                                    temp16.FindEvidenceID = cursor2.getString(cursor2.getColumnIndex(temp16.COL_FindEvidenceID));
                                    temp16.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp16.COL_CaseReportID));
                                    temp16.SceneInvestID = cursor2.getString(cursor2.getColumnIndex(temp16.COL_SceneInvestID));
                                    temp16.EvidenceTypeID = cursor2.getString(cursor2.getColumnIndex(temp16.COL_EvidenceTypeID));
                                    temp16.EvidenceNumber = cursor2.getString(cursor2.getColumnIndex(temp16.COL_EvidenceNumber));
                                    temp16.FindEvidenceZone = cursor2.getString(cursor2.getColumnIndex(temp16.COL_FindEvidenceZone));
                                    temp16.FindEvidencecol = cursor2.getString(cursor2.getColumnIndex(temp16.COL_FindEvidencecol));
                                    temp16.Marking = cursor2.getString(cursor2.getColumnIndex(temp16.COL_Marking));
                                    temp16.Parceling = cursor2.getString(cursor2.getColumnIndex(temp16.COL_Parceling));
                                    temp16.EvidencePerformed = cursor2.getString(cursor2.getColumnIndex(temp16.COL_EvidencePerformed));
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
                                    temp17.RSID = cursor2.getString(cursor2.getColumnIndex(temp17.COL_RSID));
                                    temp17.RSTypeID = cursor2.getString(cursor2.getColumnIndex(temp17.COL_RSTypeID));
                                    temp17.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp17.COL_CaseReportID));
                                    temp17.RSDetail = cursor2.getString(cursor2.getColumnIndex(temp17.COL_RSDetail));
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
                                    temp18.RSID = cursor2.getString(cursor2.getColumnIndex(temp18.COL_RSID));
                                    temp18.RSTypeID = cursor2.getString(cursor2.getColumnIndex(temp18.COL_RSTypeID));
                                    temp18.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp18.COL_CaseReportID));
                                    temp18.RSDetail = cursor2.getString(cursor2.getColumnIndex(temp18.COL_RSDetail));
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
                                    temp19.RSID = cursor2.getString(cursor2.getColumnIndex(temp19.COL_RSID));
                                    temp19.RSTypeID = cursor2.getString(cursor2.getColumnIndex(temp19.COL_RSTypeID));
                                    temp19.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp19.COL_CaseReportID));
                                    temp19.RSDetail = cursor2.getString(cursor2.getColumnIndex(temp19.COL_RSDetail));
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
                                    temp20.PropertyLossID = cursor2.getString(cursor2.getColumnIndex(temp20.COL_PropertyLossID));
                                    temp20.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp20.COL_CaseReportID));
                                    temp20.PropertyLossName = cursor2.getString(cursor2.getColumnIndex(temp20.COL_PropertyLossName));
                                    temp20.PropertyLossNumber = cursor2.getString(cursor2.getColumnIndex(temp20.COL_PropertyLossNumber));
                                    temp20.PropertyLossUnit = cursor2.getString(cursor2.getColumnIndex(temp20.COL_PropertyLossUnit));
                                    temp20.PropertyLossPosition = cursor2.getString(cursor2.getColumnIndex(temp20.COL_PropertyLossPosition));
                                    temp20.PropInsurance = cursor2.getString(cursor2.getColumnIndex(temp20.COL_PropInsurance));
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
            Log.d(TAG, "apiNoticeCases:" + apiCaseScenesCases.size());
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

    public TbNoticeCase selectNoticeScene(String noticeCaseID) {
        // TODO Auto-generated method stub
        TbNoticeCase TbNotice = new TbNoticeCase();
        Log.i(TAG, "selectNoticeScene " + noticeCaseID);
        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM noticecase "
                    + " WHERE NoticeCaseID = '" + noticeCaseID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        arrData[i] = cursor.getString(i);
                    }
                    // เชื่อมข้อมูลที่ดึงได้เข้ากับ Tb ของตารางนั้นๆ
                    TbNotice.NoticeCaseID = arrData[0];
                    TbNotice.Mobile_CaseID = arrData[1];
                    TbNotice.InquiryOfficialID = arrData[2];
                    TbNotice.InvestigatorOfficialID = arrData[3];
                    TbNotice.SCDCAgencyCode = arrData[4];
                    TbNotice.CaseTypeID = arrData[5];
                    TbNotice.SubCaseTypeID = arrData[6];
                    TbNotice.CaseStatus = arrData[7];
                    Log.i(TAG, "selectNoticeScene " + arrData[0] + arrData[1] + "/" + arrData[2] + "/" + arrData[3] + "/ " + arrData[4]);
                }
            }
            cursor.close();
            db.close();
            return TbNotice;

        } catch (Exception e) {
            Log.d(TAG, "Error in selectNoticeScene " + e.getMessage().toString());
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

    public String[][] SelectAllDistrict() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM district";
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

    public long DeleteSelectedFeatureInside(String sFeatureInsideID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(oSceneFeatureInSide.TB_SceneFeatureInSide,
                    oSceneFeatureInSide.COL_FeatureInsideID + " = ?",
                    new String[]{String.valueOf(sFeatureInsideID)});

            db.close();
            Log.i(TAG, "delete SceneFeatureInSide: " + String.valueOf(rows) + " " + sFeatureInsideID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeleteSelectedData(String tableName, String colName, String id) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(tableName,
                    colName + " = ?",
                    new String[]{String.valueOf(id)});

            db.close();
            Log.i(TAG, "delete " + tableName + ": " + String.valueOf(rows) + " " + id);
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
                    + oMultimediaFile.COL_FileType + " = '" + sFileType + "'" + " ORDER BY "
                    + oMultimediaFile.COL_FileID + " DESC";
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

                        Log.i(TAG,"show Photo "+sFileType+ arrData[i][0]);
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
    public List<TbMultimediaFile> selectedMediafiles(String sCaseReportID, String sFileType){
        SQLiteDatabase db;
        db = this.getReadableDatabase(); // Read Data
        String strSQL = "SELECT * FROM multimediafile WHERE "
                + oMultimediaFile.COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                + oMultimediaFile.COL_FileType + " = '" + sFileType + "'" + " ORDER BY "
                + oMultimediaFile.COL_FileID + " DESC";
        List<TbMultimediaFile> tbMultimediaFiles = null;
        try (Cursor cursor2 = db.rawQuery(strSQL, null)) {
            if (tbMultimediaFiles == null) {
                tbMultimediaFiles = new ArrayList<>(cursor2.getCount());
            }
            if (cursor2.getCount() > 0) {
                cursor2.moveToPosition(-1);
                while (cursor2.moveToNext()) {
                    TbMultimediaFile temp15 = new TbMultimediaFile();
                    temp15.FileID = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FileID));
                    temp15.CaseReportID = cursor2.getString(cursor2.getColumnIndex(temp15.COL_CaseReportID));
                    temp15.FileType = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FileType));
                    temp15.FilePath = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FilePath));
                    temp15.FileDescription = cursor2.getString(cursor2.getColumnIndex(temp15.COL_FileDescription));
                    temp15.Timestamp = cursor2.getString(cursor2.getColumnIndex(temp15.COL_Timestamp));
                    tbMultimediaFiles.add(temp15);
                }
            }
        }
        return tbMultimediaFiles;
    }
}
