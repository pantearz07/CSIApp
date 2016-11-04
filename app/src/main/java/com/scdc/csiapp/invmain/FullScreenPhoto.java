package com.scdc.csiapp.invmain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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
    ImageButton btnClose, btnMenu;
    TextView nofile;
    File curfile;
    String filepath, photopath, fileid;
    DBHelper dbHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        dbHelper = new DBHelper(this);
        setContentView(R.layout.view_pic_dialog);
        SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        cd = new ConnectionDetector(mContext);
        Intent intent = getIntent();
        photopath = intent.getStringExtra("photopath"); // for String
        fileid = intent.getStringExtra("fileid"); // for String
        Log.i(TAG, "fileid " + fileid);
        Log.i(TAG, "file name: " + photopath);
        String strPath = strSDCardPathName_Pic + photopath;
        curfile = new File(strPath);
        filepath = "http://" + defaultIP + "/assets/csifiles/"
                + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                + photopath.toString();
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        nofile = (TextView) findViewById(R.id.nofile);
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
            if (cd.isNetworkAvailable()) {
                Picasso.with(mContext)
                        .load(filepath)
                        .centerInside()
                        .resize(width, height)
                        .into(imgPhoto);
            } else {
                if (curfile.exists()) {
                    Picasso.with(mContext)
                            .load(curfile)
                            .centerInside()
                            .resize(width, height)
                            .into(imgPhoto);
                } else {
                    imgPhoto.setVisibility(View.GONE);
                    nofile.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (curfile.exists()) {
                Picasso.with(mContext)
                        .load(curfile)
                        .centerInside()
                        .resize(width, height)
                        .into(imgPhoto);
            } else {
                if (cd.isNetworkAvailable()) {
                    Picasso.with(mContext)
                            .load(filepath)
                            .centerInside()
                            .resize(width, height)
                            .into(imgPhoto);
                } else {
                    imgPhoto.setVisibility(View.GONE);
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
            if(view == btnClose){
                FullScreenPhoto.this.finish();
            }
            if (view == btnMenu) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(FullScreenPhoto.this, btnMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.photo_menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        //noinspection SimplifiableIfStatement
                        if (id == R.id.savephoto) {
                            if (curfile.exists()) {
                                Toast.makeText(FullScreenPhoto.this, R.string.got_photo, Toast.LENGTH_SHORT).show();
                            } else {
                                if (cd.isNetworkAvailable()) {
                                    int count;
                                    File myDir;
                                    String fileoutput = "";
                                    try {
                                        myDir = new File(strSDCardPathName_Pic);
                                        myDir.mkdirs();
                                        Log.i(TAG, "display file name: " + filepath);

                                        URL url = new URL(filepath);
                                        URLConnection conexion = url.openConnection();
                                        conexion.connect();

                                        int lenghtOfFile = conexion.getContentLength();
                                        Log.d(TAG, "Lenght of file: " + lenghtOfFile);

                                        InputStream input = new BufferedInputStream(url.openStream());

                                        // Get File Name from URL
                                        fileoutput = strSDCardPathName_Pic + photopath;
                                        Log.i(TAG, "fileoutput : " + fileoutput);
                                        OutputStream output = new FileOutputStream(fileoutput);

                                        byte data[] = new byte[1024];
                                        long total = 0;
                                        while ((count = input.read(data)) != -1) {
                                            total += count;
                                            output.write(data, 0, count);
                                        }
                                        Log.i(TAG, "DownloadFile display" + fileoutput);
                                        if(total > 0){
                                            Toast.makeText(FullScreenPhoto.this, getString(R.string.save_photo_success), Toast.LENGTH_SHORT).show();
                                        }
                                        output.flush();
                                        output.close();
                                        input.close();
                                    } catch (Exception e) {
                                        Log.e("Error: ", e.getMessage());
                                        Toast.makeText(FullScreenPhoto.this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(FullScreenPhoto.this, getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (id == R.id.deletephoto) {
                            if (curfile.exists()) {
                                Log.i(TAG, "  delete file name " + fileid);
                                int flag = 0;
                                flag = deletefile(fileid);
                                if (flag > 0) {
                                    Toast.makeText(FullScreenPhoto.this, getString(R.string.delete_photo_success), Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID.equals(fileid)) {
                                            CSIDataTabFragment.apiCaseScene.getApiMultimedia().remove(i);
                                            Log.i(TAG, "delete file name " + fileid);
                                            curfile.delete();
                                            FullScreenPhoto.this.finish();
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(FullScreenPhoto.this, getString(R.string.no_photo), Toast.LENGTH_SHORT).show();
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
        long flg2 = dbHelper.DeleteSelectedData("photoofinside", "FileID", fileid);
        long flg3 = dbHelper.DeleteSelectedData("photoofoutside", "FileID", fileid);
        long flg4 = dbHelper.DeleteSelectedData("photoofevidence", "FileID", fileid);
        long flg5 = dbHelper.DeleteSelectedData("photoofpropertyless", "FileID", fileid);
        long flg6 = dbHelper.DeleteSelectedData("photoofresultscene", "FileID", fileid);
        if (flg1 > 0) {
            flag++;
        }
        if (flg2 > 0) {
            flag++;
        }
        if (flg3 > 0) {
            flag++;
        }
        if (flg4 > 0) {
            flag++;
        }
        if (flg5 > 0) {
            flag++;
        }
        if (flg6 > 0) {
            flag++;
        }
        Log.i(TAG, "  delete file name flag " + String.valueOf(flag));
        return flag;
    }
}


