package com.scdc.csiapp.connecting;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.main.WelcomeActivity;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncDataLogin extends AsyncTask<Void, Void, Void> {
//    protected ProgressDialog progressDialog;
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        progressDialog = ProgressDialog.show(LoginActivity.mContext, "กำลังดำเนินการ", "อัพเดตข้อมูลทั้งหมดจากเซิร์ฟเวอร์", true, false);
//    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (WelcomeActivity.api != null) {
            ApiStatus apiStatus = WelcomeActivity.api.checkConnect();
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                WelcomeActivity.api.syncDataFromServer("amphur");
                WelcomeActivity.api.syncDataFromServer("casescenetype");
                WelcomeActivity.api.syncDataFromServer("composition");
                WelcomeActivity.api.syncDataFromServer("district");
                WelcomeActivity.api.syncDataFromServer("geography");
                WelcomeActivity.api.syncDataFromServer("inqposition");
                WelcomeActivity.api.syncDataFromServer("invposition");
                WelcomeActivity.api.syncDataFromServer("official");
                WelcomeActivity.api.syncDataFromServer("permission");
                WelcomeActivity.api.syncDataFromServer("policeagency");
                WelcomeActivity.api.syncDataFromServer("policecenter");
                WelcomeActivity.api.syncDataFromServer("policeposition");
                WelcomeActivity.api.syncDataFromServer("policerank");
                WelcomeActivity.api.syncDataFromServer("policestation");
                WelcomeActivity.api.syncDataFromServer("province");
                WelcomeActivity.api.syncDataFromServer("resultscenetype");
                WelcomeActivity.api.syncDataFromServer("scdcagency");
                WelcomeActivity.api.syncDataFromServer("scdccenter");
                WelcomeActivity.api.syncDataFromServer("subcasescenetype");
                WelcomeActivity.api.syncDataFromServer("evidencetype");
            } else {
                Log.d("TEST", "Connect error!");
            }
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voice) {
        super.onPostExecute(voice);
        Log.d("TEST", "END");

        // ถ้าส่งมาจากหน้า loginactivity จะ error
        Toast.makeText(WelcomeActivity.mContext, R.string.save_complete,
                Toast.LENGTH_LONG).show();

//        progressDialog.dismiss();
    }
}
