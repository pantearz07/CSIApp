package com.scdc.csiapp.invmain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.GetDateTime;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Pantearz07 on 23/12/2558.
 */
public class VideoPlayerActivity extends Activity {
    private static final String TAG = "DEBUG-VideoPlayerActivity";
    VideoView video_player_view;
    DisplayMetrics dm;
    SurfaceView sur_View;
    MediaController media_Controller;
    Context mContext;
    private static String strSDCardPathName_Vid = "/CSIFiles/";
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;
    TextView nofile, txtNum;
    FrameLayout video_frame;
    DBHelper dbHelper;
    ImageButton btnClose, btnMenu;
    File curfile;
    String filepath, VideoPath, fileid, descVideo;
    int selectedposition = 0;
    int totalnum = 0;
    ViewGroup viewDescPhoto;
    protected static final int DIALOG_AddDescPhoto = 0;

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
        dbHelper = new DBHelper(this);
        SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        cd = new ConnectionDetector(this);
        Intent intent = getIntent();
        VideoPath = intent.getStringExtra("VideoPath"); // for String
        fileid = intent.getStringExtra("fileid");
        descVideo = intent.getStringExtra("desc");
        selectedposition = intent.getIntExtra("position", 0);
        totalnum = intent.getIntExtra("totalnum", 0);
        String strPath = strSDCardPathName_Vid + VideoPath;
        nofile = (TextView) findViewById(R.id.nofile);
        video_frame = (FrameLayout) findViewById(R.id.video_frame);
        video_player_view = (VideoView) findViewById(R.id.video_player_view);
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        txtNum = (TextView) findViewById(R.id.txtNum);
        txtNum.setText((selectedposition + 1) + " จาก " + String.valueOf(totalnum));
        media_Controller = new MediaController(this);
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strPath);
        filepath = "http://" + defaultIP + "/assets/csifiles/"
                + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/video/"
                + VideoPath;
        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
            if (cd.isNetworkAvailable()) {

                video_player_view.setMinimumWidth(width);
                video_player_view.setMinimumHeight(height);
                video_player_view.setMediaController(media_Controller);
                video_player_view.setVideoURI(Uri.parse(filepath));
                video_player_view.start();
                video_player_view.requestFocus();
            } else {
                if (curfile.exists()) {
                    video_player_view.setMinimumWidth(width);
                    video_player_view.setMinimumHeight(height);
                    video_player_view.setMediaController(media_Controller);
                    video_player_view.setVideoURI(Uri.parse(curfile.getAbsolutePath()));
                    video_player_view.start();
                    video_player_view.requestFocus();
                } else {
                    video_player_view.setVisibility(View.GONE);
                    nofile.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (curfile.exists()) {
                video_player_view.setMinimumWidth(width);
                video_player_view.setMinimumHeight(height);
                video_player_view.setMediaController(media_Controller);
                video_player_view.setVideoURI(Uri.parse(curfile.getAbsolutePath()));
                video_player_view.start();
                video_player_view.requestFocus();
            } else {
                if (cd.isNetworkAvailable()) {
                    video_player_view.setMinimumWidth(width);
                    video_player_view.setMinimumHeight(height);
                    video_player_view.setMediaController(media_Controller);
                    video_player_view.setVideoURI(Uri.parse(filepath));
                    video_player_view.start();
                    video_player_view.requestFocus();
                } else {
                    video_player_view.setVisibility(View.GONE);
                    nofile.setVisibility(View.VISIBLE);
                }
            }
        }
        btnClose.setOnClickListener(new MenuOnClickListener());
        btnMenu.setOnClickListener(new MenuOnClickListener());
    }

    private class MenuOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == btnClose) {
                VideoPlayerActivity.this.finish();
            }
            if (view == btnMenu) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(VideoPlayerActivity.this, btnMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.photo_menu, popup.getMenu());
                Menu popupMenu = popup.getMenu();
//                popupMenu.findItem(R.id.descphoto).setVisible(false);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        //noinspection SimplifiableIfStatement
                        if (id == R.id.savephoto) {
                            if (curfile.exists()) {
                                Toast.makeText(VideoPlayerActivity.this, R.string.got_video, Toast.LENGTH_SHORT).show();
                            } else {
                                saveVideo();
                            }
                        }
                        if (id == R.id.descphoto) {
                            Log.i(TAG, "  descphoto ");
                            viewDescPhoto = (ViewGroup) findViewById(R.id.layout_media_dialog);
                            createdDialog(DIALOG_AddDescPhoto).show();
                        }
                        if (id == R.id.deletephoto) {
                            if (CSIDataTabFragment.mode.equals("view")) {
                                Toast.makeText(mContext.getApplicationContext(),
                                        getString(R.string.status_change_mode),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                deleteVideo();
                            }
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        }
    }

    protected int deletefile(String fileid) {
        int flag = 0;
        long flg1 = dbHelper.DeleteSelectedData("multimediafile", "FileID", fileid);
        if (flg1 > 0) {
            flag++;
        }

        Log.i(TAG, "  delete file name flag " + String.valueOf(flag));
        return flag;
    }

    private void saveVideo() {
        if (cd.isNetworkAvailable()) {
            int count;
            File myDir;
            String fileoutput = "";
            try {
                myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strSDCardPathName_Vid);
                myDir.mkdirs();
                Log.i(TAG, "display file name: " + filepath);

                URL url = new URL(filepath);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d(TAG, "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                // Get File Name from URL
                fileoutput = strSDCardPathName_Vid + VideoPath;
                File filePic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), fileoutput);
                if (filePic.exists()) {
                    filePic.delete();
                }
                filePic.createNewFile();
                OutputStream output = new FileOutputStream(filePic);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                Log.i(TAG, "DownloadFile display" + filePic.getPath());
                if (total > 0) {
                    Toast.makeText(VideoPlayerActivity.this, getString(R.string.save_video_success), Toast.LENGTH_SHORT).show();
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                Toast.makeText(VideoPlayerActivity.this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(VideoPlayerActivity.this, getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteVideo() {
        if (curfile.exists()) {
            Log.i(TAG, "  delete file name " + fileid);
            int flag = 0;
            flag = deletefile(fileid);
            if (flag > 0) {
                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID.equals(fileid)) {
                        CSIDataTabFragment.apiCaseScene.getApiMultimedia().remove(i);
                        Log.i(TAG, "delete file name " + fileid);
                        curfile.delete();
                        saveToDB();
                        Toast.makeText(VideoPlayerActivity.this, getString(R.string.delete_video_success), Toast.LENGTH_SHORT).show();
                        Intent _result = new Intent();
                        setResult(Activity.RESULT_OK, _result);
                        VideoPlayerActivity.this.finish();
                    }
                }

            } else {
                Toast.makeText(VideoPlayerActivity.this.getApplicationContext(),
                        getString(R.string.delete_error),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(VideoPlayerActivity.this, getString(R.string.no_video), Toast.LENGTH_SHORT).show();
        }
    }

    protected Dialog createdDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder;
        switch (id) {

            case DIALOG_AddDescPhoto:
                builder = new AlertDialog.Builder(this);
                final LayoutInflater inflaterDialogDescPhoto = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View ViewlayoutAddDescPhoto = inflaterDialogDescPhoto
                        .inflate(
                                R.layout.add_media_dialog,
                                viewDescPhoto);
                builder.setIcon(R.drawable.ic_edit);
                builder.setTitle(R.string.edit_descphoto);
                builder.setView(ViewlayoutAddDescPhoto);
                TextView editMediaName = (TextView) ViewlayoutAddDescPhoto
                        .findViewById(R.id.editMediaName);
                final EditText editMediaDescription = (EditText) ViewlayoutAddDescPhoto
                        .findViewById(R.id.editMediaDescription);
                editMediaName.setText(fileid + ".mp4");
                if (descVideo == null || descVideo.equals("")) {
                    editMediaDescription.setText("");
                } else {
                    editMediaDescription.setText(descVideo);
                }
                builder.setPositiveButton(R.string.save,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID.equals(fileid)) {
                                        CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i)
                                                .getTbMultimediaFile().FileDescription = editMediaDescription.getText().toString();

                                        Toast.makeText(mContext, getString(R.string.save_complete), Toast.LENGTH_SHORT).show();
//                                        Log.v(TAG, "Click FileDescription " + i + " " + CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i)
//                                                .getTbMultimediaFile().FileDescription + " tbMultimediaFiles " + tbMultimediaFiles.get(currentphoto).FileDescription.toString());
                                        saveToDB();
                                    }
                                }
                                dialog.dismiss();
                            }
                        })
                        // Button Cancel
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                dialog = builder.create();

                break;
            default:
                dialog = null;
        }
        return dialog;
    }

    // save ข้อมูลรูปภาพไว้ใน list table และ sqlite
    private void saveToDB() {
        try {
            updateData();
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                Log.i(TAG, "apiMultimediaList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        GetDateTime getDateTime = new GetDateTime();
        final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

    }
}
