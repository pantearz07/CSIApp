package com.scdc.csiapp.connecting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbComPosition;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbGeography;
import com.scdc.csiapp.tablemodel.TbInqPosition;
import com.scdc.csiapp.tablemodel.TbInvPosition;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPermission;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceCenter;
import com.scdc.csiapp.tablemodel.TbPolicePosition;
import com.scdc.csiapp.tablemodel.TbPoliceRank;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbProvince;
import com.scdc.csiapp.tablemodel.TbResultSceneType;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;

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

                ContentValues Val = new ContentValues();
                Val.put(TbAmphur.COL_AMPHUR_ID, tbAmphurList.get(i).AMPHUR_ID);
                Val.put(TbAmphur.COL_AMPHUR_CODE, tbAmphurList.get(i).AMPHUR_CODE);
                Val.put(TbAmphur.COL_AMPHUR_NAME, tbAmphurList.get(i).AMPHUR_NAME);
                Val.put(TbAmphur.COL_POSTCODE, tbAmphurList.get(i).POSTCODE);
                Val.put(TbAmphur.COL_GEO_ID, tbAmphurList.get(i).GEO_ID);
                Val.put(TbAmphur.COL_PROVINCE_ID, tbAmphurList.get(i).PROVINCE_ID);
                Val.put(TbAmphur.COL_amphur_polygon, tbAmphurList.get(i).amphur_polygon);

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

                ContentValues Val = new ContentValues();
                Val.put(TbCaseSceneType.COL_CaseTypeID, tbCaseSceneTypes.get(i).CaseTypeID);
                Val.put(TbCaseSceneType.COL_CaseTypeName, tbCaseSceneTypes.get(i).CaseTypeName);
                Val.put(TbCaseSceneType.COL_casetype_min, tbCaseSceneTypes.get(i).casetype_min);
                Val.put(TbCaseSceneType.COL_casetype_max, tbCaseSceneTypes.get(i).casetype_max);
                Val.put(TbCaseSceneType.COL_casetype_icon, tbCaseSceneTypes.get(i).casetype_icon);
                Val.put(TbCaseSceneType.COL_casetype_colormin, tbCaseSceneTypes.get(i).casetype_colormin);
                Val.put(TbCaseSceneType.COL_casetype_colormedium, tbCaseSceneTypes.get(i).casetype_colormedium);
                Val.put(TbCaseSceneType.COL_casetype_colorhigh, tbCaseSceneTypes.get(i).casetype_colorhigh);
                Val.put(TbCaseSceneType.COL_casetype_status, tbCaseSceneTypes.get(i).casetype_status);

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
                Val.put(TbComPosition.COL_ComPosID, tbComPositions.get(i).ComPosID);
                Val.put(TbComPosition.COL_ComPosName, tbComPositions.get(i).ComPosName);
                Val.put(TbComPosition.COL_ComPosAbbr, tbComPositions.get(i).ComPosAbbr);

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

                ContentValues Val = new ContentValues();
                Val.put(TbDistrict.COL_DISTRICT_ID, tbDistricts.get(i).DISTRICT_ID);
                Val.put(TbDistrict.COL_DISTRICT_CODE, tbDistricts.get(i).DISTRICT_CODE);
                Val.put(TbDistrict.COL_DISTRICT_NAME, tbDistricts.get(i).DISTRICT_NAME);
                Val.put(TbDistrict.COL_AMPHUR_ID, tbDistricts.get(i).COL_AMPHUR_ID);

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
                Val.put(TbGeography.COL_GEO_ID, tbGeographies.get(i).GEO_ID);
                Val.put(TbGeography.COL_GEO_NAME, tbGeographies.get(i).GEO_NAME);

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
                Val.put(TbInqPosition.COL_InqPosID, tbInqPositions.get(i).InqPosID);
                Val.put(TbInqPosition.COL_InqPosName, tbInqPositions.get(i).InqPosName);
                Val.put(TbInqPosition.COL_InqPosAbbr, tbInqPositions.get(i).InqPosAbbr);

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
                Val.put(TbInvPosition.COL_InvPosID, tbInvPositions.get(i).InvPosID);
                Val.put(TbInvPosition.COL_InvPosName, tbInvPositions.get(i).InvPosName);
                Val.put(TbInvPosition.COL_InvPosAbbr, tbInvPositions.get(i).InvPosAbbr);

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

                ContentValues Val = new ContentValues();
                Val.put(TbOfficial.COL_OfficialID, tbOfficials.get(i).OfficialID);
                Val.put(TbOfficial.COL_FirstName, tbOfficials.get(i).FirstName);
                Val.put(TbOfficial.COL_LastName, tbOfficials.get(i).LastName);
                Val.put(TbOfficial.COL_Alias, tbOfficials.get(i).Alias);
                Val.put(TbOfficial.COL_Rank, tbOfficials.get(i).Rank);
                Val.put(TbOfficial.COL_Position, tbOfficials.get(i).Position);
                Val.put(TbOfficial.COL_SubPossition, tbOfficials.get(i).SubPossition);
                Val.put(TbOfficial.COL_PhoneNumber, tbOfficials.get(i).PhoneNumber);
                Val.put(TbOfficial.COL_OfficialEmail, tbOfficials.get(i).OfficialEmail);
                Val.put(TbOfficial.COL_OfficialDisplayPic, tbOfficials.get(i).OfficialDisplayPic);
                Val.put(TbOfficial.COL_AccessType, tbOfficials.get(i).AccessType);
                Val.put(TbOfficial.COL_SCDCAgencyCode, tbOfficials.get(i).SCDCAgencyCode);
                Val.put(TbOfficial.COL_PoliceStationID, tbOfficials.get(i).PoliceStationID);
                Val.put(TbOfficial.COL_id_users, tbOfficials.get(i).id_users);

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
                Val.put(TbPermission.COL_id_permission, tbPermissions.get(i).id_permission);
                Val.put(TbPermission.COL_per_name, tbPermissions.get(i).per_name);
                Val.put(TbPermission.COL_per_value, tbPermissions.get(i).per_value);

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

                ContentValues Val = new ContentValues();
                Val.put(TbPoliceAgency.COL_PoliceAgencyID, tbPoliceAgencies.get(i).PoliceAgencyID);
                Val.put(TbPoliceAgency.COL_PoliceCenterID, tbPoliceAgencies.get(i).PoliceCenterID);
                Val.put(TbPoliceAgency.COL_PoliceAgencyName, tbPoliceAgencies.get(i).PoliceAgencyName);

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
                Val.put(TbPoliceCenter.COL_PoliceCenterID, tbPoliceCenters.get(i).PoliceCenterID);
                Val.put(TbPoliceCenter.COL_PoliceName, tbPoliceCenters.get(i).PoliceName);

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
                Val.put(TbPolicePosition.COL_PolicePosID, tbPolicePositions.get(i).PolicePosID);
                Val.put(TbPolicePosition.COL_PoliceName, tbPolicePositions.get(i).PoliceName);
                Val.put(TbPolicePosition.COL_PoliceAbbr, tbPolicePositions.get(i).PoliceAbbr);

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
                Val.put(TbPoliceRank.COL_RankID, tbPoliceRanks.get(i).RankID);
                Val.put(TbPoliceRank.COL_RankName, tbPoliceRanks.get(i).RankName);
                Val.put(TbPoliceRank.COL_RankAbbr, tbPoliceRanks.get(i).RankAbbr);

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

                ContentValues Val = new ContentValues();
                Val.put(TbPoliceStation.COL_PoliceStationID, tbPoliceStations.get(i).PoliceStationID);
                Val.put(TbPoliceStation.COL_PoliceAgencyID, tbPoliceStations.get(i).PoliceAgencyID);
                Val.put(TbPoliceStation.COL_PoliceStationName, tbPoliceStations.get(i).PoliceStationName);

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

                ContentValues Val = new ContentValues();
                Val.put(TbProvince.COL_PROVINCE_ID, tbProvinces.get(i).PROVINCE_ID);
                Val.put(TbProvince.COL_PROVINCE_CODE, tbProvinces.get(i).PROVINCE_CODE);
                Val.put(TbProvince.COL_PROVINCE_NAME, tbProvinces.get(i).PROVINCE_NAME);
                Val.put(TbProvince.COL_GEO_ID, tbProvinces.get(i).GEO_ID);
                Val.put(TbProvince.COL_province_status, tbProvinces.get(i).province_status);
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
                Val.put(TbResultSceneType.COL_RSTypeID, tbResultSceneTypes.get(i).RSTypeID);
                Val.put(TbResultSceneType.COL_RSTypeName, tbResultSceneTypes.get(i).RSTypeName);
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

                ContentValues Val = new ContentValues();
                Val.put(TbSCDCagency.COL_SCDCAgencyCode, tbSCDCagencies.get(i).SCDCAgencyCode);
                Val.put(TbSCDCagency.COL_SCDCCenterID, tbSCDCagencies.get(i).SCDCCenterID);
                Val.put(TbSCDCagency.COL_SCDCAgencyName, tbSCDCagencies.get(i).SCDCAgencyName);
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

                ContentValues Val = new ContentValues();
                Val.put(TbSCDCcenter.COL_SCDCCenterID, tbSCDCcenters.get(i).SCDCCenterID);
                Val.put(TbSCDCcenter.COL_SCDCCenterName, tbSCDCcenters.get(i).SCDCCenterName);
                Val.put(TbSCDCcenter.COL_SCDCCenterProvince, tbSCDCcenters.get(i).SCDCCenterProvince);
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

                ContentValues Val = new ContentValues();
                Val.put(TbSubcaseSceneType.COL_SubCaseTypeID, tbSubcaseSceneTypes.get(i).SubCaseTypeID);
                Val.put(TbSubcaseSceneType.COL_CaseTypeID, tbSubcaseSceneTypes.get(i).CaseTypeID);
                Val.put(TbSubcaseSceneType.COL_SubCaseTypeName, tbSubcaseSceneTypes.get(i).SubCaseTypeName);

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

                        Log.i("show SelectSubCaseType", arrData[i][0]);
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
}
