package com.scdc.csiapp.connecting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Pantearz07 on 15/9/2559.
 */
public class DBHelper extends SQLiteAssetHelper {
    SQLiteDatabase mDb;
    SQLiteQueryBuilder mQb;
    private static final String DB_NAME = "csi_db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        //super(context, DB_NAME, null, DB_VERSION);
        super(context, DB_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DB_VERSION);

    }
}
