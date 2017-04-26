package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

/**
 * Created by Pantearz07 on 18/10/2558.
 */
public class PreferenceData {
    public static final String KEY_PREFS = "prefs_user";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_OFFICIALID = "officialid";
    public static final String KEY_ACCESSTYPE = "accesstype";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCDCAGENCYCODE = "scdcagencycode";

    // public static final String PREF_SCDCCENTER = "center";
    public static final String KEY_USER_LOGGEDIN_STATUS = "logged_in_status";
    public static final String PREF_REPORTID = "reportid";
    public static final String PREF_CASEID = "caseid";
    public static final String PREF_LASTLOGIN = "lastlogin";
    public static final String ARG_UPDATE_DATA_INVESTIGATOR = "updatedate";
    public static final String ARG_UPDATE_DATA_INSPECTOR = "updatedate";
    public static final String ARG_UPDATE_DATA_HISTORY = "updatehistory";
    public static final String ARG_UPDATE_DATA_RECEIVINGCASE = "updatedate";
    public static final String ARG_UPDATE_DATA_DRAFTCASE = "updatedate";
    public static final String PREF_REPORTSTATUS = "status";

    public static final String PREF_SORTING = "wSorting";
    public static final String KEY_SORTING = "wSortingValue";

    //setting
    public static final String PREF_IP = "pref_ip";
    public static final String KEY_IP = "IPValue";

    TbOfficial tbOfficial = new TbOfficial();
    TbUsers tbUsers = new TbUsers();
    DBHelper dbHelper;
// SharedPreferences mPrefs; mPrefs = getSharedPreferences("pref_ip", MODE_PRIVATE);

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    public PreferenceData(Context context) {
        dbHelper = new DBHelper(context);
        mPrefs = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }


    public boolean checkLoginValidate(String username, String password) {
        String realUsername = mPrefs.getString(KEY_USERNAME, "");
        String realPassword = mPrefs.getString(KEY_PASSWORD, "");

        if ((!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) &&
                username.equals(realUsername) &&
                password.equals(realPassword)) {
            return true;
        }
        return false;
    }

    public boolean registerUser(TbUsers users, TbOfficial official) {
        if (users == null || official == null) {
            return false;
        }

        // pref ของ TbUser
        mEditor.putString(dbHelper.COL_id_users, users.id_users);
        mEditor.putString(dbHelper.COL_id_permission, users.id_permission);
        mEditor.putString(dbHelper.COL_pass, users.pass);
        mEditor.putString(dbHelper.COL_id_system, users.id_system);
        mEditor.putString(dbHelper.COL_title, users.title);
        mEditor.putString(dbHelper.COL_name, users.name);
        mEditor.putString(dbHelper.COL_surname, users.surname);
        mEditor.putString(dbHelper.COL_position, users.position);
        mEditor.putString(dbHelper.COL_picture, users.picture);
        mEditor.putString(dbHelper.COL_last_login, users.last_login);
        // pref ของ TbOfficial
        mEditor.putString(dbHelper.COL_OfficialID, official.OfficialID);
        mEditor.putString(dbHelper.COL_FirstName, official.FirstName);
        mEditor.putString(dbHelper.COL_LastName, official.LastName);
        mEditor.putString(dbHelper.COL_Alias, official.Alias);
        mEditor.putString(dbHelper.COL_Rank, official.Rank);
        mEditor.putString(dbHelper.COL_Position, official.Position);
        mEditor.putString(dbHelper.COL_SubPossition, official.SubPossition);
        mEditor.putString(dbHelper.COL_PhoneNumber, official.PhoneNumber);
        mEditor.putString(dbHelper.COL_OfficialEmail, official.OfficialEmail);
        mEditor.putString(dbHelper.COL_OfficialDisplayPic, official.OfficialDisplayPic);
        mEditor.putString(dbHelper.COL_AccessType, official.AccessType);
        mEditor.putString(dbHelper.COL_SCDCAgencyCode, official.SCDCAgencyCode);
        mEditor.putString(dbHelper.COL_PoliceStationID, official.PoliceStationID);
        mEditor.putString(dbHelper.COL_id_users, official.id_users);

        return mEditor.commit();
    }

    public boolean setIP(String urlip) {

        if (TextUtils.isEmpty(urlip)) {
            return false;
        }

        mEditor.putString(KEY_IP, urlip);

        mEditor.putString(PREF_IP, "true");

        return mEditor.commit();
    }

    public Boolean clearLoggedInOfficial() {

        mEditor.remove(KEY_OFFICIALID);
        mEditor.remove(KEY_USERNAME);
        mEditor.remove(KEY_PASSWORD);
        mEditor.remove(KEY_ACCESSTYPE);
        mEditor.remove(KEY_USER_LOGGEDIN_STATUS);
        mEditor.commit();
        return null;
    }

    public boolean setPreferenceData(String valuePrefName, String value) {
        mEditor.putString(valuePrefName, value);
        return mEditor.commit();
    }

    public String getPreferenceData(String valuePrefName) {
        return mPrefs.getString(valuePrefName, "");
    }

    public boolean setPreferenceDataBoolean(String valuePrefName, boolean value) {
        mEditor.putBoolean(valuePrefName, value);
        return mEditor.commit();
    }

    public boolean getPreferenceDataBoolean(String valuePrefName) {
        return mPrefs.getBoolean(valuePrefName, false);
    }

}
