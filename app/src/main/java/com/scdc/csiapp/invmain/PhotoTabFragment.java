package com.scdc.csiapp.invmain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.ActivityResultBus;
import com.scdc.csiapp.main.ActivityResultEvent;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 14/3/2559.
 */
public class PhotoTabFragment extends Fragment {
    private static final String TAG = "DEBUG-PhotoTabFragment";
    public static final int REQUEST_CAMERA = 111;
    private String mCurrentPhotoPath;
    TextView txtPhotoNum;
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    String sPhotoID, timeStamp;
    DBHelper dbHelper;
    private GridView gViewPhoto;
    Uri uri;
    private PreferenceData mManager;
    String officialID, caseReportID;
    String arrDataPhoto[][];
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    GetDateTime getDateTime;

    public static List<TbMultimediaFile> tbMultimediaFileList = null;
    Context mContext;
    private static String strSDCardPathName_Pic = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/Pictures/";
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        dbHelper = new DBHelper(getActivity());
        View viewPhotosTab = inflater.inflate(R.layout.photo_tab_layout, container, false);
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        getDateTime = new GetDateTime();
        cd = new ConnectionDetector(getActivity());
        rootLayout = (CoordinatorLayout) viewPhotosTab.findViewById(R.id.rootLayout);
        gViewPhoto = (GridView) viewPhotosTab.findViewById(R.id.gridViewPhoto);
        txtPhotoNum = (TextView) viewPhotosTab.findViewById(R.id.txtPhotoNum);
        mContext = viewPhotosTab.getContext();
        showAllPhoto();
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);

        fabBtn = (FloatingActionButton) viewPhotosTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new PhotoOnClickListener());
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

    public static void createFolder(String pathType) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
                Log.i("mkdir", Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
            } else {
                Log.i("folder.exists", Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");

            }
        } catch (Exception ex) {
        }

    }


    public class PhotoAdapter extends BaseAdapter {
        private Context context;

        public PhotoAdapter(Context c) {
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
            textView.setVisibility(View.GONE);
 /*           textView.setText(lis[position][3].toString() + "\n"
                    + lis[position][4].toString());
  */
//            String root = Environment.getExternalStorageDirectory().toString();
            String strPath = strSDCardPathName_Pic + tbMultimediaFileList.get(position).FilePath.toString();
            Log.i("strPath ", strPath);
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);
            final File curfile = new File(strPath);
            final String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                    + tbMultimediaFileList.get(position).FilePath.toString();

            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                if (cd.isNetworkAvailable()) {
                    Picasso.with(getActivity())
                            .load(filepath)
                            .resize(50, 50)
                            .centerCrop()
                            .placeholder(R.drawable.ic_imagefile)
                            .error(R.drawable.ic_imagefile)
                            .into(imageView);
                } else {

                    if (curfile.exists()) {
                        Picasso.with(getActivity())
                                .load(curfile)
                                .resize(50, 50)
                                .centerCrop()
                                .placeholder(R.drawable.ic_imagefile)
                                .error(R.drawable.ic_imagefile)
                                .into(imageView);
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_imagefile));
                    }
                }
            } else {

                if (curfile.exists()) {
                    Picasso.with(getActivity())
                            .load(curfile)
                            .resize(50, 50)
                            .placeholder(R.drawable.ic_imagefile)
                            .error(R.drawable.ic_imagefile)
                            .centerCrop()
                            .into(imageView);
                } else {
                    if (cd.isNetworkAvailable()) {
                        Picasso.with(getActivity())
                                .load(filepath)
                                .resize(50, 50)
                                .placeholder(R.drawable.ic_imagefile)
                                .error(R.drawable.ic_imagefile)
                                .centerCrop()
                                .into(imageView);
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_imagefile));
                    }
                }

            }
            return convertView;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result media", String.valueOf(requestCode) + " " + String.valueOf(resultCode));


        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    Log.i(TAG, "Photo save " + sPhotoID);
                    List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
                    ApiMultimedia apiMultimedia = new ApiMultimedia();
                    TbMultimediaFile tbMultimediaFile = new TbMultimediaFile();
                    tbMultimediaFile.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                    tbMultimediaFile.FileID = sPhotoID;
                    tbMultimediaFile.FileDescription = "";
                    tbMultimediaFile.FileType = "photo";
                    tbMultimediaFile.FilePath = sPhotoID + ".jpg";
                    tbMultimediaFile.Timestamp = timeStamp;
                    apiMultimedia.setTbMultimediaFile(tbMultimediaFile);

                    apiMultimediaList.add(apiMultimedia);
                    CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
//                    CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);
                    Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        Log.i(TAG, "PHOTO saved to Gallery!" + ResultTabFragment.strSDCardPathName + "Pictures/" + " : " + sPhotoID + ".jpg");

                    }
                    showAllPhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_Photo", "media recording cancelled." + sPhotoID);

            } else {
                Log.i("REQUEST_Photo", "Failed to record media");
            }
        }
    }

    public void showAllPhoto() {
        // TODO Auto-generated method stub
        tbMultimediaFileList = new ArrayList<>();

        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
            Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            if (cd.isNetworkAvailable()) {
                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equals("photo")) {
                            tbMultimediaFileList.add(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                        }
                    }
                }
                Log.i(TAG, "tbMultimediaFileList " + String.valueOf(tbMultimediaFileList.size()));
            } else {
                tbMultimediaFileList = dbHelper.selectedMediafiles(caseReportID, "photo");

            }
        } else {
            tbMultimediaFileList = dbHelper.selectedMediafiles(caseReportID, "photo");
            Log.i(TAG, "tbMultimediaFileList offline " + String.valueOf(tbMultimediaFileList.size()));
        }
        if (tbMultimediaFileList != null) {
            Log.i("tbMultimediaFileList", String.valueOf(tbMultimediaFileList.size()));
            txtPhotoNum.setText(String.valueOf(tbMultimediaFileList.size()));
            gViewPhoto.setVisibility(View.VISIBLE);
            gViewPhoto.setAdapter(new PhotoAdapter(getActivity()));
            registerForContextMenu(gViewPhoto);
            // OnClick
            gViewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

//                    showViewPic(tbMultimediaFileList.get(position).FilePath.toString());
                    Intent intent = new Intent(getActivity(), FullScreenPhoto.class);
                    Bundle extras = new Bundle();
                    extras.putString("photopath", tbMultimediaFileList.get(position).FilePath.toString());
                    extras.putString("fileid", tbMultimediaFileList.get(position).FileID.toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });


        } else {
            gViewPhoto.setVisibility(View.GONE);
            Log.i("Recieve", "Null!! ");

        }
    }

    public void showViewPic(String sPicPath) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity(),
                R.style.FullHeightDialog);
        dialog.setContentView(R.layout.view_pic_dialog);
        String root = Environment.getExternalStorageDirectory().toString();
        String strPath = root + "/CSIFiles/Pictures/" + sPicPath;

        // Image Resource
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgPhoto);

        Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
        int width = bmpSelectedImage.getWidth();
        int height = bmpSelectedImage.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap resizedBitmap = Bitmap.createBitmap(bmpSelectedImage, 0, 0,
                width, height, matrix, true);
        imageView.setImageBitmap(resizedBitmap);
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllPhoto();
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
        Log.i("onResume photo", "resume");
        showAllPhoto();
    }

    private class PhotoOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == fabBtn) {
                File newfile;
                createFolder("Pictures");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sPhotoID = "IMG_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];

                String sPhotoPath = sPhotoID + ".jpg";
                newfile = new File(ResultTabFragment.strSDCardPathName, "Pictures/" + sPhotoPath);
                if (newfile.exists())
                    newfile.delete();
                try {
                    newfile.createNewFile();
                    mCurrentPhotoPath = newfile.getAbsolutePath();
                } catch (IOException e) {
                }
                if (newfile != null) {
                    uri = Uri.fromFile(newfile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    getActivity().startActivityForResult(Intent.createChooser(cameraIntent
                            , "Take a picture with"), REQUEST_CAMERA);
                }
            }
        }
    }
}
