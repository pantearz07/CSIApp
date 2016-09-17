package com.scdc.csiapp.connecting;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseScene;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbGeography;
import com.scdc.csiapp.tablemodel.TbInqPosition;
import com.scdc.csiapp.tablemodel.TbInvPosition;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceCenter;
import com.scdc.csiapp.tablemodel.TbPoliceRank;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbProvince;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;

/**
 * Created by Pantearz07 on 15/9/2559.
 */
public class DBHelper extends SQLiteAssetHelper {
    SQLiteDatabase mDb;
    SQLiteQueryBuilder mQb;
    private static final String DB_NAME = "mCSI.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        //super(context, DB_NAME, null, DB_VERSION);
        super(context, DB_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DB_VERSION);

    }

    //*** การทำงานต่างๆในส่วนการดึงข้อมูลใหม่จากเซิร์ฟเวอร์  ***//
    private void updateDB() {

    }

    public String[][] SelectSubCaseType() {
        // TODO Auto-generated method stub
        Log.i("show", "SelectSubCaseType");
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM "+ TbSubcaseSceneType.TB_SubcaseSceneType;
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

    public String[] SelectAllCaseType() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbCaseScene.TB_CaseScene;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
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

    public String[][] SelectPoliceStation() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbPoliceStation.TB_PoliceStation;
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("selectPoliceStation", String.valueOf(cursor.getCount()));
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
                        Log.i("selectPoliceStation", arrData[i][3]);
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

    public String[] SelectPoliceAgency() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbPoliceAgency.TB_PoliceAgency;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
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

    public String[] SelectPoliceCenter() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbPoliceCenter.TB_PoliceCenter;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
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

    public String[] SelectPoliceRank() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbPoliceRank.TB_PoliceRank;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
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

    public String[] SelectInvPosition() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbInvPosition.TB_InvPosition;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
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

    public String[] SelectInqPosition() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbInqPosition.TB_InqPosition;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
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

            String strSQL = "SELECT * FROM " + TbDistrict.TB_District;
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
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show SelectAllDistrict", arrData[i][0]);
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

    public String[][] SelectAllAmphur() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbAmphur.TB_Amphur;
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
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show SelectAllAmphur", arrData[i][0]);
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

    public String[][] SelectAllProvince() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbProvince.TB_Province;
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
                        arrData[i][3] = cursor.getString(3);
                        Log.i("show SelectAllProvince", arrData[i][0]);
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

    public String[][] SelectAllGeography() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbGeography.TB_Geography;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);

                        Log.i("show SelectAllGeography", arrData[i][0]);
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

    public String[][] SelectAllSCDCAgency() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbSCDCagency.TB_SCDCagency;
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("SelectAllSCDCAgency", String.valueOf(cursor.getCount()));
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
                        Log.i("SelectAllSCDCAgency", arrData[i][3]);
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

    public String[][] SelectAllSCDCCenter() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TbSCDCcenter.TB_SCDCcenter;
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("SelectAllSCDCCenter", String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);

                        Log.i("SelectAllSCDCCenter", arrData[i][2]);
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
