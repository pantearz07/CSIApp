package com.scdc.csiapp.connecting;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Pantearz07 on 18/10/2558.
 */
public class PreferenceData {
    public static final String KEY_PREFS = "prefs_user";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_OFFICIALID = "officialid";
    public static final String PREF_ACCESSTYPE = "accesstype";

   // public static final String PREF_SCDCCENTER = "center";
    public static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";
    public static final String PREF_REPORTID = "reportid";
    public static final String PREF_CASEID = "caseid";
    public static final String PREF_LASTLOGIN = "lastlogin";
    public static final String ARG_UPDATE_DATA_INVESTIGATOR = "updatedate";
    public static final String ARG_UPDATE_DATA_INSPECTOR = "updatedate";
    public static final String ARG_UPDATE_DATA_HISTORY = "updatehistory";
    public static final String ARG_UPDATE_DATA_RECEIVINGCASE= "updatedate";
    public static final String ARG_UPDATE_DATA_DRAFTCASE= "updatedate";
    public static final String PREF_REPORTSTATUS= "status";
    //setting
    public static final String PREF_IP= "pref_ip";
    public static final String KEY_IP= "IPValue";


// SharedPreferences mPrefs; mPrefs = getSharedPreferences("pref_ip", MODE_PRIVATE);

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    public PreferenceData(Context context) {
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

    public boolean registerUser(String username, String password, String officialID, String accessType) {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;
        }

        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.putString(KEY_OFFICIALID, officialID);
        mEditor.putString(PREF_ACCESSTYPE, accessType);
        mEditor.putString(PREF_USER_LOGGEDIN_STATUS, "true");

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
        mEditor.remove(PREF_ACCESSTYPE);
        mEditor.remove(PREF_USER_LOGGEDIN_STATUS);
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
