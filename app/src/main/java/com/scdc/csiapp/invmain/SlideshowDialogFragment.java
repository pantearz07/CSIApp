package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.squareup.picasso.Picasso;

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

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
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
    ImageView imgDisplay;
    LinearLayout layoutDescPhoto;
    private Toolbar toolbar;
    boolean fullscreen = true;
    String defaultIP = "180.183.251.32/mcsi";
    int currentphoto = 0;
    ImageButton btnMenu;
    ImageButton btnClose;

    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
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
        tbMultimediaFiles = (List<TbMultimediaFile>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");
        initData();
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        width = dm.widthPixels;
        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + tbMultimediaFiles.size());

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
        if (tbMultimediaFiles.get(position).FileDescription == null || tbMultimediaFiles.get(position).FileDescription.equals("")) {
            layoutDescPhoto.setVisibility(View.GONE);
        } else {
            descPhoto.setText(tbMultimediaFiles.get(position).FileDescription);
        }
        toolbar.setTitle((position + 1) + " จาก " + tbMultimediaFiles.size());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 20) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.AppThemeDarkActionBar);
        } else {
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme_ActionBar_Transparent);
        }
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
            imgDisplay.setOnClickListener(new PhotoOnclick());

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
                    File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strPath);
                    String filepath = "http://" + defaultIP + "/assets/csifiles/"
                            + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                            + tbMultimediaFile.FilePath.toString();
                    String fileid = tbMultimediaFile.FileID.toString();
                    //noinspection SimplifiableIfStatement
                    if (id == R.id.savephoto) {
                        if (curfile.exists()) {
                            Toast.makeText(mContext, R.string.got_photo, Toast.LENGTH_SHORT).show();
                        } else {
                            if (cd.isNetworkAvailable()) {
                                int count;
                                File myDir;
                                try {
                                    myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strSDCardPathName_Pic);
                                    myDir.mkdirs();
                                    Log.i(TAG, "display file name: " + filepath);

                                    URL url = new URL(filepath);
                                    URLConnection conexion = url.openConnection();
                                    conexion.connect();

                                    int lenghtOfFile = conexion.getContentLength();
                                    Log.d(TAG, "Lenght of file: " + lenghtOfFile);

                                    InputStream input = new BufferedInputStream(url.openStream());
//                        File filePic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strSDCardPathName_Pic + photopath);
                                    if (curfile.exists()) {
                                        curfile.delete();
                                    }
                                    curfile.createNewFile();
                                    // Get File Name from URL
                                    OutputStream output = new FileOutputStream(curfile);

                                    byte data[] = new byte[1024];
                                    long total = 0;
                                    while ((count = input.read(data)) != -1) {
                                        total += count;
                                        output.write(data, 0, count);
                                    }
                                    Log.i(TAG, "DownloadFile display" + curfile.getPath());
                                    if (total > 0) {
                                        Toast.makeText(mContext, getString(R.string.save_photo_success), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mContext, getString(R.string.delete_photo_success), Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, getString(R.string.no_photo), Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }
            });
            toolbar.inflateMenu(R.menu.photo_menu);
        }
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        getActivity().getMenuInflater().inflate(R.menu.photo_menu, menu);
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        Log.i(TAG, "  delete file name " + currentphoto);
//
//        return super.onOptionsItemSelected(item);
//    }

    protected int deletefile(String fileid) {
        int flag = 0;
        long flg2 = dbHelper.DeleteSelectedData("photoofinside", "FileID", fileid);
        long flg3 = dbHelper.DeleteSelectedData("photoofoutside", "FileID", fileid);
        long flg4 = dbHelper.DeleteSelectedData("photoofevidence", "FileID", fileid);
        long flg5 = dbHelper.DeleteSelectedData("photoofpropertyless", "FileID", fileid);
        long flg6 = dbHelper.DeleteSelectedData("photoofresultscene", "FileID", fileid);
        long flg1 = dbHelper.DeleteSelectedData("multimediafile", "FileID", fileid);


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
        if (flg1 > 0) {
            flag++;
        }
        Log.i(TAG, "  delete file name flag " + String.valueOf(flag));
        return flag;
    }

    private class PhotoOnclick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            if (view == imgDisplay) {
                if (fullscreen) {
                    fullscreen = false;
                    toolbar.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT < 16) {
                        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    } else {
                        View decorView = getDialog().getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                    }

                } else {
                    fullscreen = true;
                    toolbar.setVisibility(View.VISIBLE);

                    if (Build.VERSION.SDK_INT < 16) {
                        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    } else {
                        View decorView = getDialog().getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                }
//            }
//            if (view == btnMenu) {
//                Log.i(TAG, "  btnMenu ");
//            }
//            if (view == btnClose) {
//                Log.i(TAG, "  btnClose ");
//            }
        }
    }
}
