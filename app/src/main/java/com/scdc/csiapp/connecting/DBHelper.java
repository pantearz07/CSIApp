package com.scdc.csiapp.connecting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbGeography;
import com.scdc.csiapp.tablemodel.TbInqPosition;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceCenter;

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
            Log.d(TAG, "Sync Table casescenetype: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncCaseSceneType " + e.getMessage().toString());
            return false;
        }
    }

    private boolean syncComPosition() {
        // ไม่มี Tb
        return false;
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
            Log.d(TAG, "Sync Table inqposition: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncInqPosition " + e.getMessage().toString());
            return false;
        }
    }

    private boolean syncInvPosition() {
        // ไม่มี Tb
        return false;
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
            Log.d(TAG, "Sync Table official: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncOfficial " + e.getMessage().toString());
            return false;
        }
    }

    private boolean syncPermission() {
        // ไม่มี Tb
        return false;
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
            Log.d(TAG, "Sync Table policecenter: Insert " + insert + ", Update " + update);
            db.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error in syncPoliceCenter " + e.getMessage().toString());
            return false;
        }
    }

    private boolean syncPolicePosition() {
        return false;
    }

    private boolean syncPoliceRank() {
        return false;
    }

    private boolean syncPoliceStation() {
        return false;
    }

    private boolean syncProvince() {
        return false;
    }

    private boolean syncSCDCAgency() {
        return false;
    }

    private boolean syncSCDCCenter() {
        return false;
    }

    private boolean syncSubCaseSceneType() {
        return false;
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
