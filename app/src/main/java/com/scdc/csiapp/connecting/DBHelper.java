package com.scdc.csiapp.connecting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseScene;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbComPosition;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbFindEvidence;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
                Val.put(oResultSceneType.COL_RSTypeName, tbResultSceneTypes.get(i).RSTypeName);
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

                }
                Log.i(TAG, "show selectofficial" + arrData[2]);
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
//                apiNoticeCases = new ArrayList<>();
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
                    apiNoticeCases.add(apiNoticeCase);
                    Log.d("TEST", "--" + apiNoticeCase.getTbNoticeCase().NoticeCaseID);
                }
            }
            db.close();
//            Log.d("TEST", apiNoticeCases.get(0).getTbNoticeCase().NoticeCaseID + " " + apiNoticeCases.get(1).getTbNoticeCase().NoticeCaseID);
            // รวมข้อมูลที่ได้ทั้งหมดลง apiListNoticeCase ก่อนส่งกลับไปใช้
            Log.d(TAG, "apiNoticeCases:" + apiNoticeCases.size());
            for (int i = 0; i < apiNoticeCases.size(); i++) {
                Log.d("TEST", "////--" + apiNoticeCases.get(i).getTbNoticeCase().NoticeCaseID);
            }

            dataEntity.setResult(apiNoticeCases);
            apiListNoticeCase.setData(dataEntity);
            return apiListNoticeCase;
        } catch (Exception e) {
            Log.d(TAG, "Error in selectApiNoticeCase " + e.getMessage().toString());
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
}
