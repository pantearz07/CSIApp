package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.ActivityResultBus;
import com.scdc.csiapp.main.ActivityResultEvent;
import com.scdc.csiapp.main.GetDateTime;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pantearz07 on 14/3/2559.
 */
public class VideoTabFragment extends Fragment {
    public static final int REQUEST_VIDEO = 222;
    private String mCurrentVideoPath;
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    String sVideoID;
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    GridView gViewVideo;
    Uri uri;
    private PreferenceData mManager;
    String officialID, reportID;
    String arrDataVideo[][];
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    TextView txtVideoNum;
GetDateTime getDateTime;
    String[] updateDT, datetime;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        getDateTime = new GetDateTime();

        updateDT = getDateTime.getDateTimeNow();
        datetime = getDateTime.getDateTimeCurrent();
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        View viewPhotosTab = inflater.inflate(R.layout.video_tab_layout, container, false);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        rootLayout = (CoordinatorLayout) viewPhotosTab.findViewById(R.id.rootLayout);
        gViewVideo = (GridView) viewPhotosTab.findViewById(R.id.gridViewVideo);
        txtVideoNum = (TextView) viewPhotosTab.findViewById(R.id.txtVideoNum);
        showAllVideo();
        fabBtn = (FloatingActionButton) viewPhotosTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String timeStamp = "";
                File newfile;
                createFolder("Videoes");
                Intent intentvideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                sVideoID = "VID_"+ datetime[2]+""+datetime[1]+""+datetime[0]+"_"+ datetime[3]+""+datetime[4]+""+datetime[5];
                String sVideoPath = sVideoID + ".mp4";
                newfile = new File(strSDCardPathName, "Videoes/" + sVideoPath);
                if (newfile.exists())
                    newfile.delete();
                try {
                    newfile.createNewFile();
                    mCurrentVideoPath = newfile.getAbsolutePath();
                    Log.i("mCurrentVideoPath",mCurrentVideoPath);
                } catch (IOException e) {
                }

                if (newfile != null) {
                    Uri videoUri = Uri.fromFile(newfile);

                    intentvideo.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                    getActivity().startActivityForResult(intentvideo, REQUEST_VIDEO);
                    String sVideoDescription = "";
                    new saveDataMedia().execute(reportID, sVideoID, sVideoPath, sVideoDescription, timeStamp, "video");
                    Log.i("show", "Video saved to Gallery!" + strSDCardPathName + "Videoes/" + " : " + sVideoPath);

                }
            }
        });


        return viewPhotosTab;
    }

    public static void createFolder(String pathType) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }

    }

    class saveDataMedia extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";


            long saveStatus = mDbHelper.saveDataMultimediaifile(params[0], params[1], params[5], params[2],
                    params[3], params[4]);
            if (saveStatus <= 0) {
                arrData = "error";
                Log.i("saveData " + params[5], "Error!! ");
            } else {
                arrData = "save";
                Log.i("saveData " + params[5], params[2]);


            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            if (arrData == "save") {
                Log.i("saveData", "save");
                //showAllPhoto();
                //showAllVideo();

            } else {
                Log.i("saveData", "error");

            }
        }
    }

    public class VideoAdapter extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public VideoAdapter(Context c, String[][] li) {
            // TODO Auto-generated method stub
            context = c;
            lis = li;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return lis.length;
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.show_photos, null);
            }

            TextView textView = (TextView) convertView
                    .findViewById(R.id.txtDescPhoto);
            textView.setVisibility(View.GONE);
           /* textView.setText(lis[position][3].toString() + "\n"
                    + lis[position][4].toString());
*/
            String root = Environment.getExternalStorageDirectory().toString();
            String strPath = root + "/CSIFiles/Videoes/"
                    + lis[position][3].toString();

            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(strPath,
                    MediaStore.Video.Thumbnails.MINI_KIND);

            imageView.setImageBitmap(bmThumbnail);
            return convertView;

        }
    }

    public void showAllVideo() {
        // TODO Auto-generated method stub
        arrDataVideo = mDbHelper.SelectDataMultimediaFile(reportID, "video");
        if (arrDataVideo != null) {
            txtVideoNum.setText(String.valueOf(arrDataVideo.length));
            gViewVideo.setVisibility(View.VISIBLE);
            gViewVideo.setAdapter(new VideoAdapter(getActivity(), arrDataVideo));
            registerForContextMenu(gViewVideo);
            // OnClick
            gViewVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent VideoPlayActivity = new Intent(getActivity(),
                            VideoPlayerActivity.class);
                    VideoPlayActivity.putExtra("VideoPath",
                            arrDataVideo[position][3].toString());
                    startActivity(VideoPlayActivity);

                }
            });
        } else {
            gViewVideo.setVisibility(View.GONE);
            Log.i("Recieve", "Null!! ");

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result media", String.valueOf(requestCode) + " " + String.valueOf(resultCode));
        if (requestCode == REQUEST_VIDEO) {
            if (resultCode == getActivity().RESULT_OK) {
                try {

                    Log.i("REQUEST_VIDEO", "VIDEO save");
                    showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_VIDEO", "media recording cancelled." + sVideoID);
                File videosfile = new File(mCurrentVideoPath);
                if (videosfile.exists()) {
                    videosfile.delete();
                    long saveStatus = mDbHelper.DeleteMediaFile(reportID, sVideoID);
                    if (saveStatus <= 0) {
                        Log.i("delete video", "Cannot delete!! ");

                    } else {
                        Log.i("delete video", "ok");
                        showAllVideo();
                    }
                }
            } else {
                Log.i("REQUEST_VIDEO", "Failed to record media");
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume Video", "resume");
        //  showAllVideo();
    }
}
