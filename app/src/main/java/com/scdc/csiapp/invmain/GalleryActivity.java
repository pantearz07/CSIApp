package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 11/11/2559.
 */

public class GalleryActivity extends AppCompatActivity {
    private String TAG = GalleryActivity.class.getSimpleName();
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
    ImageView imgDisplay;
    LinearLayout layoutDescPhoto;
    private Toolbar toolbar;
    boolean fullscreen = true;
    String defaultIP = "180.183.251.32/mcsi";
    int currentphoto = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
        mContext = this;
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_fullscreen_view2);
        SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        cd = new ConnectionDetector(mContext);
        initView();
        initData();
        settingDisplayMetrics();
        tbMultimediaFiles = new ArrayList<>();
        Intent intent = getIntent();
        tbMultimediaFiles = (List<TbMultimediaFile>) intent.getSerializableExtra("images"); // for String
        selectedPosition = intent.getIntExtra("position", 0); // for String

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + tbMultimediaFiles.size());
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
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

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
        currentphoto = position;
        Log.i(TAG, "  setCurrentItem " + currentphoto);
    }

    private void displayMetaInfo(int position) {
        if (tbMultimediaFiles.get(position).FileDescription == null || tbMultimediaFiles.get(position).FileDescription.equals("")) {
            layoutDescPhoto.setVisibility(View.GONE);
        } else {
            descPhoto.setText(tbMultimediaFiles.get(position).FileDescription);
        }
        toolbar.setTitle((position + 1) + " จาก " + tbMultimediaFiles.size());

    }

    private void settingDisplayMetrics() {
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        width = dm.widthPixels;
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        descPhoto = (TextView) findViewById(R.id.descPhoto);
        layoutDescPhoto = (LinearLayout) findViewById(R.id.layoutDescPhoto);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    private class MyViewPagerAdapter extends PagerAdapter {
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
            View view = layoutInflater.inflate(R.layout.layout_fullscreen_image, container, false);
            imgDisplay = (ImageView) view.findViewById(R.id.imgDisplay);
            TextView nofile = (TextView) view.findViewById(R.id.nofile);
            TbMultimediaFile tbMultimediaFile = tbMultimediaFiles.get(position);
            String strPath = strSDCardPathName_Pic + tbMultimediaFile.FilePath;
            String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                    + tbMultimediaFile.FilePath.toString();
            File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strPath);
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                if (cd.isNetworkAvailable()) {
                    Picasso.with(context)
                            .load(filepath)
                            .centerInside()
                            .resize(width, height)
                            .into(imgDisplay);
                } else {
                    if (curfile.exists()) {
                        Picasso.with(context)
                                .load(curfile)
                                .centerInside()
                                .resize(width, height)
                                .into(imgDisplay);
                    } else {
                        imgDisplay.setVisibility(View.GONE);
                        nofile.setVisibility(View.VISIBLE);
                    }
                }

            } else {
                if (curfile.exists()) {
                    Picasso.with(context)
                            .load(curfile)
                            .centerInside()
                            .resize(width, height)
                            .into(imgDisplay);
                } else {
                    if (cd.isNetworkAvailable()) {
                        Picasso.with(context)
                                .load(filepath)
                                .centerInside()
                                .resize(width, height)
                                .into(imgDisplay);
                    } else {
                        imgDisplay.setVisibility(View.GONE);
                        nofile.setVisibility(View.VISIBLE);
                    }
                }
            }
//            imgDisplay.setOnClickListener(new PhotoOnclick());
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

    private void initData() {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GalleryActivity.this.finish();
                }
            });
        }
    }
}
