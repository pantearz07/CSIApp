package com.scdc.csiapp.connecting;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.main.SettingFragment;
import com.scdc.csiapp.main.WelcomeActivity;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncDataAddress extends AsyncTask<Void, Void, Void> {
    protected ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(SettingFragment.mContext, "กำลังดำเนินการ", "อัพเดตข้อมูลพื้นฐานสำหรับที่อยู่จากเซิร์ฟเวอร์", true, false);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (WelcomeActivity.api != null) {
            ApiStatus apiStatus = WelcomeActivity.api.checkConnect();
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                WelcomeActivity.api.syncDataFromServer("amphur");
                WelcomeActivity.api.syncDataFromServer("district");
                WelcomeActivity.api.syncDataFromServer("geography");
                WelcomeActivity.api.syncDataFromServer("province");
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
        Toast.makeText(WelcomeActivity.mContext, R.string.save_complete,
                Toast.LENGTH_LONG).show();

        progressDialog.dismiss();
    }
}
