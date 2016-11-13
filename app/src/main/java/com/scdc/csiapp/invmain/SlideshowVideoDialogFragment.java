package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 10/11/2559.
 */

public class SlideshowVideoDialogFragment extends DialogFragment {
    private String TAG = SlideshowVideoDialogFragment.class.getSimpleName();
    ConnectionDetector cd;
    DBHelper dbHelper;
    private Context mContext;
    private ViewPager viewPager;
    List<TbMultimediaFile> tbMultimediaFiles;
    private int selectedPosition = 0;
    private TextView descPhoto;
    DisplayMetrics dm;
    private static String strSDCardPathName_Pic = "/CSIFiles/";
    private MyViewPagerAdapter myViewPagerAdapter;
    int height = 0;
    int width = 0;
    View v;
    LinearLayout layoutDescPhoto;
    private Toolbar toolbar;
    boolean fullscreen = true;
    String defaultIP = "180.183.251.32/mcsi";
    int currentphoto = 0;

    static SlideshowVideoDialogFragment newInstance() {
        SlideshowVideoDialogFragment f = new SlideshowVideoDialogFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_fullscreen_view, container, false);

        mContext = getActivity();
        dbHelper = new DBHelper(mContext);
        cd = new ConnectionDetector(mContext);
        SharedPreferences sp = mContext.getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        initView();

        tbMultimediaFiles = new ArrayList<>();
        tbMultimediaFiles = (List<TbMultimediaFile>) getArguments().getSerializable("video");
        selectedPosition = getArguments().getInt("position");
        initData();
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        width = dm.widthPixels;
        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "video size: " + tbMultimediaFiles.size());

        myViewPagerAdapter = new MyViewPagerAdapter(mContext);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        setCurrentItem(selectedPosition);
        return v;

    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
        currentphoto = position;
        Log.i(TAG, "  setCurrentItem " + currentphoto);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        layoutDescPhoto.setVisibility(View.GONE);
//        if (tbMultimediaFiles.get(position).FileDescription != null) {
//            descPhoto.setText(tbMultimediaFiles.get(position).FileDescription);
//        } else {
//            layoutDescPhoto.setVisibility(View.GONE);
//        }
        toolbar.setTitle((position + 1) + " จาก " + tbMultimediaFiles.size());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme_ActionBar_Transparent);
        setHasOptionsMenu(true);
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Context context;

        public MyViewPagerAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;
        }

        @Override
        public int getCount() {
            return tbMultimediaFiles.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.layout_fullscreen_video, container, false);
            VideoView video_player_view;
            MediaController media_Controller;
            FrameLayout video_frame;
            video_frame = (FrameLayout) view.findViewById(R.id.video_frame);
            video_player_view = (VideoView) view.findViewById(R.id.video_player_view);
            TextView nofile = (TextView) view.findViewById(R.id.nofile);

            TbMultimediaFile tbMultimediaFile = tbMultimediaFiles.get(position);
            media_Controller = new MediaController(video_player_view.getContext());
            media_Controller.setMediaPlayer(video_player_view);
            String strPath = strSDCardPathName_Pic + tbMultimediaFile.FilePath;
            String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/video/"
                    + tbMultimediaFile.FilePath.toString();
            File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strPath);
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
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initView() {
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        descPhoto = (TextView) v.findViewById(R.id.descPhoto);
        layoutDescPhoto = (LinearLayout) v.findViewById(R.id.layoutDescPhoto);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
    }

    private void initData() {
        if (toolbar != null) {

            toolbar.setNavigationIcon(R.drawable.ic_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    TbMultimediaFile tbMultimediaFile = tbMultimediaFiles.get(currentphoto);
                    String strPath = strSDCardPathName_Pic + tbMultimediaFile.FilePath;
                    File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strPath);
                    String filepath = "http://" + defaultIP + "/assets/csifiles/"
                            + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/video/"
                            + tbMultimediaFile.FilePath.toString();
                    String fileid = tbMultimediaFile.FileID.toString();
                    //noinspection SimplifiableIfStatement
                    if (id == R.id.savephoto) {
                        if (curfile.exists()) {
                            Toast.makeText(mContext, R.string.got_video, Toast.LENGTH_SHORT).show();
                        } else {
                            if (cd.isNetworkAvailable()) {
                                int count;
                                File myDir;
                                try {
                                    myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strSDCardPathName_Pic);
                                    myDir.mkdirs();
                                    Log.i(TAG, "display file name: " + filepath);

                                    URL url = new URL(filepath);
                                    URLConnection conexion = url.openConnection();
                                    conexion.connect();

                                    int lenghtOfFile = conexion.getContentLength();
                                    Log.d(TAG, "Lenght of file: " + lenghtOfFile);

                                    InputStream input = new BufferedInputStream(url.openStream());
                                    // Get File Name from URL
                                    File filePic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strPath);
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
                                        Toast.makeText(mContext, getString(R.string.save_video_success), Toast.LENGTH_SHORT).show();
                                    }
                                    output.flush();
                                    output.close();
                                    input.close();
                                } catch (Exception e) {
                                    Log.e("Error: ", e.getMessage());
                                    Toast.makeText(mContext, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (id == R.id.deletephoto) {
                        if (curfile.exists()) {
                            Log.i(TAG, "  delete file name " + fileid);
                            int flag = 0;
                            flag = deletefile(fileid);
                            if (flag > 0) {
                                Toast.makeText(mContext, getString(R.string.delete_video_success), Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID.equals(fileid)) {
                                        CSIDataTabFragment.apiCaseScene.getApiMultimedia().remove(i);
                                        curfile.delete();
                                        getDialog().dismiss();
                                    }
                                }
                            } else {
                                Toast.makeText(mContext.getApplicationContext(),
                                        getString(R.string.delete_error),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_video), Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }
            });
            toolbar.inflateMenu(R.menu.photo_menu);
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

}
