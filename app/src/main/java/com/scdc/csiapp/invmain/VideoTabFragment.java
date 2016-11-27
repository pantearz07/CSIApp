package com.scdc.csiapp.invmain;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.ActivityResultBus;
import com.scdc.csiapp.main.ActivityResultEvent;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 14/3/2559.
 */
public class VideoTabFragment extends Fragment {
    private static final String TAG = "DEBUG-VideoTabFragment";
    public static final int REQUEST_VIDEO = 888;
    public static final int REQUEST_LOAD_VIDEO = 8;
    public static final int REQUEST_GALLERY = 88;
    private String mCurrentVideoPath;
    String imageEncoded;
    List<String> imagesEncodedList;
    String caseReportID, sVideoID, timeStamp;
    DBHelper dbHelper;
    SQLiteDatabase mDb;
    GridView gViewVideo;
    Uri uri;
    private PreferenceData mManager;

    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    TextView txtVideoNum;
    GetDateTime getDateTime;
    public static List<TbMultimediaFile> tbMultimediaFileList = null;
    Context mContext;
    private static String strSDCardPathName_Vid = "/CSIFiles/";
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;
    GetPathUri getPathUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        dbHelper = new DBHelper(getActivity());
        View viewPhotosTab = inflater.inflate(R.layout.video_tab_layout, container, false);
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        cd = new ConnectionDetector(getActivity());
        rootLayout = (CoordinatorLayout) viewPhotosTab.findViewById(R.id.rootLayout);
        gViewVideo = (GridView) viewPhotosTab.findViewById(R.id.gridViewVideo);
        txtVideoNum = (TextView) viewPhotosTab.findViewById(R.id.txtVideoNum);
        showAllVideo();
        fabBtn = (FloatingActionButton) viewPhotosTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new VideoOnClickListener());
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        getPathUri = new GetPathUri();
        if (CSIDataTabFragment.mode == "view") {

            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtn.setLayoutParams(p);
            fabBtn.hide();
        }
        return viewPhotosTab;
    }

    public static void createFolder() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strSDCardPathName_Vid);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }

    }


    public class VideoAdapter extends BaseAdapter {
        private Context context;


        public VideoAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tbMultimediaFileList.size();
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
            if(tbMultimediaFileList.get(position).FileDescription == null ||
                    tbMultimediaFileList.get(position).FileDescription.length() == 0){
                textView.setVisibility(View.GONE);
            }else {
                textView.setText(tbMultimediaFileList.get(position).FileDescription.toString());
            }
            String strPath = strSDCardPathName_Vid
                    + tbMultimediaFileList.get(position).FilePath.toString();

            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);
            final File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), strPath);
            final String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/video/"
                    + tbMultimediaFileList.get(position).FilePath.toString();
            Bitmap bmThumbnail = null;

            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
//                Log.i(TAG, "view online");
                if (cd.isNetworkAvailable()) {

//                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath,
//                            MediaStore.Video.Thumbnails.MICRO_KIND);
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_videofile));

                } else {
                    if (curfile.exists()) {
                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(curfile.getPath(),
                                MediaStore.Video.Thumbnails.MICRO_KIND);
                        imageView.setImageBitmap(bmThumbnail);
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_videofile));
                    }
                }
            } else {
                if (curfile.exists()) {
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(curfile.getPath(),
                            MediaStore.Video.Thumbnails.MICRO_KIND);
                    imageView.setImageBitmap(bmThumbnail);
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_videofile));
                }

            }


            return convertView;

        }
    }

    public void showAllVideo() {
        // TODO Auto-generated method stub
        tbMultimediaFileList = new ArrayList<>();
        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
            Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            if (cd.isNetworkAvailable()) {
                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equals("video")) {
                            tbMultimediaFileList.add(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                        }
                    }
                }
                Log.i(TAG, "tbMultimediaFileList " + String.valueOf(tbMultimediaFileList.size()));
            } else {
                tbMultimediaFileList = dbHelper.selectedMediafiles(caseReportID, "video");

            }
        } else {
            tbMultimediaFileList = dbHelper.selectedMediafiles(caseReportID, "video");

            Log.i(TAG, "tbMultimediaFileList video offline " + String.valueOf(tbMultimediaFileList.size()));
        }
        if (tbMultimediaFileList != null) {
            txtVideoNum.setText(String.valueOf(tbMultimediaFileList.size()));
            gViewVideo.setVisibility(View.VISIBLE);
            gViewVideo.setAdapter(new VideoAdapter(getActivity()));
            registerForContextMenu(gViewVideo);
            // OnClick
            gViewVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent VideoPlayActivity = new Intent(getActivity(),
                            VideoPlayerActivity.class);
                    VideoPlayActivity.putExtra("VideoPath",
                            tbMultimediaFileList.get(position).FilePath.toString());
                    VideoPlayActivity.putExtra("fileid",
                            tbMultimediaFileList.get(position).FileID.toString());
                    VideoPlayActivity.putExtra("desc",
                            tbMultimediaFileList.get(position).FileDescription.toString());
                    VideoPlayActivity.putExtra("position", position);
                    VideoPlayActivity.putExtra("totalnum", tbMultimediaFileList.size());
                    getActivity().startActivityForResult(VideoPlayActivity, REQUEST_LOAD_VIDEO);

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
                    saveToListDB(sVideoID, ".mp4");
                    Log.i(TAG, "VIDEO save " + sVideoID);

                    showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_VIDEO", "media recording cancelled." + sVideoID);

            } else {
                Log.i("REQUEST_VIDEO", "Failed to record media");
            }
        }
        // รับข้อมูลจากอัลบั้มภาพ และบันทึกภาพลงแอพ
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == getActivity().RESULT_OK && null != data) {
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imagesEncodedList = new ArrayList<String>();
                    if (data.getData() != null) {
                        Uri mImageUri = data.getData();
                        // Get the cursor getFilepath
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            imageEncoded = getPathUri.getPath(getActivity(), mImageUri);
                        } else {
                            imageEncoded = getFilepath(filePathColumn, mImageUri);
                        }
                        Log.i(TAG, "REQUEST_GALLERY " + imageEncoded);
                        saveToMyAlbum(imageEncoded);
                    } else { //ถ้าเลือกหลายรูป
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    //Multiple images
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    // Get the cursor getFilepath
                                    imageEncoded = getPathUri.getPath(getActivity(), uri);
//                                    Log.v(TAG, "REQUEST_GALLERY [" + i + "] " + imageEncoded);

                                    if (imageEncoded != null) {
                                        imagesEncodedList.add(imageEncoded);
                                        saveToMyAlbum(imageEncoded);
                                    }
                                }
                                Log.v(TAG, "REQUEST_GALLERY Selected Images   :" + " imagesEncodedList :" + imagesEncodedList.size());
                            }
                        } else {
                            Toast.makeText(getActivity(), "ไม่สามารถเลือกหลายรูปได้", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    showAllVideo();
                    Log.i(TAG, "REQUEST_GALLERY");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // After Cancel code.
                Log.i(TAG, "Cancel REQUEST_GALLERY");
            } else {
                Log.i(TAG, "Failed to REQUEST_GALLERY");
            }

        }
        if (requestCode == REQUEST_LOAD_VIDEO) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    showAllVideo();
                    Log.i(TAG, "RESULT_OK");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // After Cancel code.
                Log.i(TAG, "Cancel REQUEST_LOAD_VIDEO");
            } else {
                Log.i(TAG, "Failed to REQUEST_LOAD_VIDEO");
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
    }

    private class VideoOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabBtn) {
                String title = getString(R.string.importvideo);
                CharSequence[] itemlist = {getString(R.string.video),
                        getString(R.string.gallery)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_video);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterfaceOnClickListener());
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();
            }
        }
    }

    private class DialogInterfaceOnClickListener implements DialogInterface.OnClickListener {


        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case 0:// Take Photo
                    // Do Take Photo task here
                    takeVideo();
                    break;
                case 1:// Choose Existing Photo
                    // Do Pick Photo task here
                    pickVideo();
                    break;
                default:
                    break;
            }
        }
    }

    private void takeVideo() {
        File newfile;
        createFolder();
        Intent intentvideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
        sVideoID = "VID_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
        timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];

        String sVideoPath = strSDCardPathName_Vid + sVideoID + ".mp4";
        newfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), sVideoPath);
        if (newfile.exists())
            newfile.delete();
        try {
            newfile.createNewFile();
            mCurrentVideoPath = newfile.getAbsolutePath();
        } catch (IOException e) {
        }
        if (newfile != null) {
            uri = Uri.fromFile(newfile);
            intentvideo.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            getActivity().startActivityForResult(intentvideo, REQUEST_VIDEO);
            String sVideoDescription = "";
            Log.i("show", "Video saved to Gallery! Video/" + " : " + sVideoPath);
        }
    }

    private void pickVideo() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "เลือกวิดีโอ"), REQUEST_GALLERY);

    }

    private void saveToMyAlbum(String imageEncoded) {
        try {
            createFolder();
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            File datadest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            String sVideoID = "", sFileType = "";
            String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
            sVideoID = "VID_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5] + CurrentDate_ID[6];
            timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];
            sFileType = imageEncoded.substring(imageEncoded.lastIndexOf("."));
            Log.i(TAG, "sVideoID " + sVideoID + " sImageType " + sFileType);
            if (sd.canWrite()) {
//                        String sourceImagePath = "/path/to/source/file.jpg";
                String destinationImagePath = strSDCardPathName_Vid + sVideoID + sFileType;
                File source = new File(imageEncoded);

                File destination = new File(datadest, destinationImagePath);
                if (source.exists()) {
                    Log.i(TAG, "source ");
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();

                    try {
                        dst.transferFrom(src, 0, src.size());
                        Log.i(TAG, "transferFrom ");
                    } catch (IOException e) {
                        Log.i(TAG, "transferFrom " + e.getMessage());
                    }
                    if (destination.exists()) {
//                                source.delete();
                        Log.i(TAG, "source.delete ");
                        saveToListDB(sVideoID, sFileType);

                    }
                    src.close();
                    dst.close();
                    Log.i(TAG, "save new Video from gallery " + destinationImagePath);
                } else {
                    Log.i(TAG, "Video from gallery error ");
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Video from gallery error " + e.getMessage());
        }
    }

    // save ข้อมูลรูปภาพไว้ใน list table และ sqlite
    private void saveToListDB(String videoID, String sFileType) {
        try {
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            ApiMultimedia apiMultimedia = new ApiMultimedia();
            TbMultimediaFile tbMultimediaFile = new TbMultimediaFile();
            tbMultimediaFile.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
            tbMultimediaFile.FileID = videoID;
            tbMultimediaFile.FileDescription = "";
            tbMultimediaFile.FileType = "video";
            tbMultimediaFile.FilePath = videoID + sFileType;
            tbMultimediaFile.Timestamp = timeStamp;
            apiMultimedia.setTbMultimediaFile(tbMultimediaFile);

            apiMultimediaList.add(apiMultimedia);
            CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
            Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                Log.i(TAG, "video saved to Gallery! Video/" + " : " + videoID + sFileType);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilepath(String[] filePathColumn, Uri uri) {
        // Get the cursor
        Cursor cursor = getActivity().getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imageEncoded = cursor.getString(columnIndex);

        cursor.close();
        return imageEncoded;
    }
}
