package com.scdc.csiapp.invmain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;

/**
 * Created by Pantearz07 on 23/12/2558.
 */
public class VideoPlayerActivity extends Activity {
    VideoView video_player_view;
    DisplayMetrics dm;
    SurfaceView sur_View;
    MediaController media_Controller;
    Context mContext;
    private static String strSDCardPathName_Vid = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/Video/";
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT > 9) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_video_player);
        mContext = this;
        SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        cd = new ConnectionDetector(this);
        Intent intent = getIntent();
        String VideoPath = intent.getStringExtra("VideoPath"); // for String

        String strPath = strSDCardPathName_Vid + VideoPath;

        video_player_view = (VideoView) findViewById(R.id.video_player_view);
        media_Controller = new MediaController(this);
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
//                Log.i(TAG, "view online");
            if (cd.isNetworkAvailable()) {
                //C:\xampp\htdocs\mCSI\assets\csifiles\CR04_000001\pictures
                String filepath = "http://" + defaultIP + "/assets/csifiles/"
                        + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/video/"
                        + VideoPath;
//                    Log.i(TAG, "server file name: " + filepath);
                video_player_view.setMinimumWidth(width);
                video_player_view.setMinimumHeight(height);
                video_player_view.setMediaController(media_Controller);
                video_player_view.setVideoURI(Uri.parse(filepath));
                video_player_view.start();
                video_player_view.requestFocus();
            } else {
                video_player_view.setMinimumWidth(width);
                video_player_view.setMinimumHeight(height);
                video_player_view.setMediaController(media_Controller);
                video_player_view.setVideoURI(Uri.parse(strPath));
                video_player_view.start();
                video_player_view.requestFocus();
            }
        } else {
            video_player_view.setMinimumWidth(width);
            video_player_view.setMinimumHeight(height);
            video_player_view.setMediaController(media_Controller);
            video_player_view.setVideoURI(Uri.parse(strPath));
            video_player_view.start();
            video_player_view.requestFocus();
        }
    }


}
