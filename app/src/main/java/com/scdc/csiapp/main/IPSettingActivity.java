package com.scdc.csiapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;

/**
 * Created by Pantearz07 on 17/7/2559.
 */
public class IPSettingActivity extends AppCompatActivity {
    Button setip_btn;
    private EditText ipvalueEdt;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    String ipvalue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_ip);
        mContext = this;
        mManager = new PreferenceData(this);
        cd = new ConnectionDetector(getApplicationContext());
        networkConnectivity = cd.networkConnectivity();

        setip_btn = (Button) findViewById(R.id.setip_btn);
        ipvalueEdt = (EditText) findViewById(R.id.ipvalueEdt);
        setip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkConnectivity) {
                    Log.d("internet status", "connected to wifi");
                    settingNewIP();
                    //  loginnoconnect();
                } else {
                    Log.d("internet status", "no Internet Access");

                        Toast.makeText(getBaseContext(),
                                "กรุณาเชื่อมต่ออินเตอร์เน็ต",
                                Toast.LENGTH_SHORT).show();


                }
            }
        });

    }
    private void settingNewIP() {
        ipvalue = ipvalueEdt.getText().toString().trim();
        Log.d("ipvalue", ipvalue);

        boolean isSuccess = mManager.setIP(ipvalue);
        if (isSuccess) {

                Toast.makeText(getBaseContext(), "บันทึกค่า IP ใหม่เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                switchPageToMain();



        } else {
            Toast.makeText(getBaseContext(), "บันทึกค่า IP ไม่สำเร็จ", Toast.LENGTH_SHORT).show();
        }


    }
    protected void switchPageToMain() {
        Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);

        startActivity(gotoWelcomeActivity);
        finish();

    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        this.finish();
    }
}
