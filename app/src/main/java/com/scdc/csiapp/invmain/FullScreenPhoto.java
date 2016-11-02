package com.scdc.csiapp.invmain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Pantearz07 on 1/11/2559.
 */

public class FullScreenPhoto extends Activity {
    DisplayMetrics dm;
    ConnectionDetector cd;
    ImageView imgPhoto;
    private static String strSDCardPathName_Pic = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/Pictures/";
    String defaultIP = "180.183.251.32/mcsi";
    private static final String TAG = "DEBUG-FullScreenPhoto";
    private Context mContext;
    ImageButton btnClose;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        setContentView(R.layout.view_pic_dialog);
        SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        cd = new ConnectionDetector(mContext);
        Intent intent = getIntent();
        String photopath = intent.getStringExtra("photopath"); // for String
        Log.i(TAG, "file name: " + photopath);
        String strPath = strSDCardPathName_Pic + photopath;
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenPhoto.this.finish();
            }
        });
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
//                Log.i(TAG, "view online");
            if (cd.isNetworkAvailable()) {
                //C:\xampp\htdocs\mCSI\assets\csifiles\CR04_000001\pictures
                String filepath = "http://" + defaultIP + "/assets/csifiles/"
                        + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                        + photopath;
//                    Log.i(TAG, "server file name: " + filepath);
                Picasso.with(mContext)
                        .load(filepath)
                        .centerInside()
//                        .rotate(90)
                        .resize(width, height)
                        .into(imgPhoto);
//                Bitmap bmpSelectedImage = BitmapFactory.decodeFile(filepath);
//                int width1 = bmpSelectedImage.getWidth();
//                int height1 = bmpSelectedImage.getHeight();
//                Matrix matrix = new Matrix();
//                matrix.postRotate(90);
//                Bitmap resizedBitmap = Bitmap.createBitmap(bmpSelectedImage, 0, 0,
//                        width1, height1, matrix, true);
//                imageView.setImageBitmap(resizedBitmap);
            } else {
                Picasso.with(mContext)
                        .load(new File(strPath))
                        .centerInside()
//                        .rotate(90)
                        .resize(width, height)
                        .into(imgPhoto);
            }
        } else {
            Picasso.with(mContext)
                    .load(new File(strPath))
                    .centerInside()
//                    .rotate(90)
                    .resize(width, height)
                    .into(imgPhoto);
        }
    }


}


