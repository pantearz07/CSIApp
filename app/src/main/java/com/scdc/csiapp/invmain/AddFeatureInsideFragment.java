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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.scdc.csiapp.tablemodel.TbPhotoOfInside;
import com.scdc.csiapp.tablemodel.TbSceneFeatureInSide;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddFeatureInsideFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddFeatureInsideFragment";
    EditText editFeatureInsideFloor, editFeatureInsideCave, editFeatureInsideClassBack, editFeatureInsideClassLeft, editFeatureInsideClassCenter,
            editFeatureInsideClassRight, editFeatureInsideClassFront;
    private GridView horizontal_gridView_Inside, horizontal_gridView_Inside_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    TbSceneFeatureInSide tbSceneFeatureInSide;
    DBHelper dbHelper;
    DetailsTabFragment detailsTabFragment;
    CSIDataTabFragment csiDataTabFragment;
    String sFeatureInsideID, mode;
    int position = 0;
    Button btnTakePhotoInside;
    public static final int REQUEST_CAMERA_OUTSIDE = 333;
    private String mCurrentPhotoPath;
    Uri uri;
    String sPhotoID, timeStamp;
    List<ApiMultimedia> apiMultimediaList;
    Context mContext;
    private static String strSDCardPathName_Pic = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/Pictures/";
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;

    //    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    public AddFeatureInsideFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_feature_inside, container, false);
        //call the main activity set tile method

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มลักษณะภายใน");
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Bundle args = getArguments();
        sFeatureInsideID = args.getString(DetailsTabFragment.Bundle_InsideID);
        mode = args.getString(DetailsTabFragment.Bundle_Inside_mode);
        Log.i(TAG, "sFeatureInsideID " + sFeatureInsideID);
        dbHelper = new DBHelper(getActivity());
        tbSceneFeatureInSide = new TbSceneFeatureInSide();
        detailsTabFragment = new DetailsTabFragment();
        csiDataTabFragment = new CSIDataTabFragment();
        getDateTime = new GetDateTime();
        cd = new ConnectionDetector(getActivity());
        mContext = view.getContext();
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);


        Log.i(TAG, "tbSceneFeatureInSideList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);
        btnTakePhotoInside = (Button) view.findViewById(R.id.btnTakePhotoInside);
        editFeatureInsideFloor = (EditText) view.findViewById(R.id.editFeatureInsideFloor);
        editFeatureInsideCave = (EditText) view.findViewById(R.id.editFeatureInsideCave);
        editFeatureInsideClassBack = (EditText) view.findViewById(R.id.editFeatureInsideClassBack);
        editFeatureInsideClassLeft = (EditText) view.findViewById(R.id.editFeatureInsideClassLeft);
        editFeatureInsideClassCenter = (EditText) view.findViewById(R.id.editFeatureInsideClassCenter);
        editFeatureInsideClassRight = (EditText) view.findViewById(R.id.editFeatureInsideClassRight);
        editFeatureInsideClassFront = (EditText) view.findViewById(R.id.editFeatureInsideClassFront);

        editFeatureInsideFloor.addTextChangedListener(new InsideTextWatcher(editFeatureInsideFloor));
        editFeatureInsideCave.addTextChangedListener(new InsideTextWatcher(editFeatureInsideCave));
        editFeatureInsideClassBack.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassBack));
        editFeatureInsideClassCenter.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassCenter));
        editFeatureInsideClassRight.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassRight));
        editFeatureInsideClassFront.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassFront));
        editFeatureInsideClassLeft.addTextChangedListener(new InsideTextWatcher(editFeatureInsideClassLeft));

        if (mode == "edit") {
            position = args.getInt(DetailsTabFragment.Bundle_Index, -1);
            Log.i(TAG, "position " + position);
            tbSceneFeatureInSide = (TbSceneFeatureInSide) args.getSerializable(DetailsTabFragment.Bundle_InsideTB);
            editFeatureInsideFloor.setText(tbSceneFeatureInSide.getFloorNo());
            editFeatureInsideCave.setText(tbSceneFeatureInSide.getCaveNo());
            editFeatureInsideClassBack.setText(tbSceneFeatureInSide.getBackInside());
            editFeatureInsideClassCenter.setText(tbSceneFeatureInSide.getCenterInside());
            editFeatureInsideClassRight.setText(tbSceneFeatureInSide.getRightInside());
            editFeatureInsideClassFront.setText(tbSceneFeatureInSide.getFrontInside());
            editFeatureInsideClassLeft.setText(tbSceneFeatureInSide.getLeftInside());
        }
        horizontal_gridView_Inside = (GridView) view.findViewById(R.id.horizontal_gridView_Inside);
        horizontal_gridView_Inside_video = (GridView) view.findViewById(R.id.horizontal_gridView_Inside_video);
        txtPhoto = (TextView) view.findViewById(R.id.txtPhoto);
        txtVideo = (TextView) view.findViewById(R.id.txtVideo);
        txtVideo.setVisibility(View.GONE);
        showAllPhoto();
        fabBtnDetails.setOnClickListener(new InsideOnClickListener());
        btnTakePhotoInside.setOnClickListener(new InsideOnClickListener());
        if (CSIDataTabFragment.mode == "view") {
            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtnDetails.setLayoutParams(p);
            fabBtnDetails.hide();
        }
        return view;
    }

    private class InsideOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabBtnDetails) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();


                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                tbSceneFeatureInSide.FeatureInsideID = sFeatureInsideID;
                tbSceneFeatureInSide.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                DetailsTabFragment.tbSceneFeatureInSideList.add(tbSceneFeatureInSide);
                CSIDataTabFragment.apiCaseScene.setTbSceneFeatureInSide(DetailsTabFragment.tbSceneFeatureInSideList);
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if (mode == "edit") {
                        CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().remove(position);
                        Log.i(TAG, "tbSceneFeatureInSideList remove num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));

                    } else {
                        Log.i(TAG, "tbSceneFeatureInSideList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));

                    }
                    getActivity().onBackPressed();
                }

            }
            if (v == btnTakePhotoInside) {
                File newfile;
                DetailsTabFragment.createFolder("Pictures");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sPhotoID = "IMG_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];

                String sPhotoPath = sPhotoID + ".jpg";
                newfile = new File(DetailsTabFragment.strSDCardPathName, "Pictures/" + sPhotoPath);
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
                            , "Take a picture with"), REQUEST_CAMERA_OUTSIDE);
                }
            }
        }
    }

    private class InsideTextWatcher implements android.text.TextWatcher {
        private EditText mEditText;

        public InsideTextWatcher(EditText editText) {
            mEditText = editText;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == editFeatureInsideFloor.getEditableText()) {
                tbSceneFeatureInSide.FloorNo = editFeatureInsideFloor.getText().toString();
                Log.i(TAG, "FloorNo " + tbSceneFeatureInSide.FloorNo);
            } else if (s == editFeatureInsideCave.getEditableText()) {
                tbSceneFeatureInSide.CaveNo = editFeatureInsideCave.getText().toString();
                Log.i(TAG, "CaveNo " + tbSceneFeatureInSide.CaveNo);
            } else if (s == editFeatureInsideClassBack.getEditableText()) {
                tbSceneFeatureInSide.BackInside = editFeatureInsideClassBack.getText().toString();
                Log.i(TAG, "BackInside " + tbSceneFeatureInSide.BackInside);
            } else if (s == editFeatureInsideClassLeft.getEditableText()) {
                tbSceneFeatureInSide.LeftInside = editFeatureInsideClassLeft.getText().toString();
                Log.i(TAG, "LeftInside " + tbSceneFeatureInSide.LeftInside);
            } else if (s == editFeatureInsideClassCenter.getEditableText()) {
                tbSceneFeatureInSide.CenterInside = editFeatureInsideClassCenter.getText().toString();
                Log.i(TAG, "CenterInside " + tbSceneFeatureInSide.CenterInside);
            } else if (s == editFeatureInsideClassRight.getEditableText()) {
                tbSceneFeatureInSide.RightInside = editFeatureInsideClassRight.getText().toString();
                Log.i(TAG, "RightInside " + tbSceneFeatureInSide.RightInside);
            } else if (s == editFeatureInsideClassFront.getEditableText()) {
                tbSceneFeatureInSide.FrontInside = editFeatureInsideClassFront.getText().toString();
                Log.i(TAG, "FrontInside " + tbSceneFeatureInSide.FrontInside);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Result media" + String.valueOf(requestCode) + " " + String.valueOf(resultCode));


        if (requestCode == REQUEST_CAMERA_OUTSIDE) {
            if (resultCode == getActivity().RESULT_OK) {
                try {

                    Log.i(TAG, "Photo save");
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

                    TbPhotoOfInside tbPhotoOfInside = new TbPhotoOfInside();
                    tbPhotoOfInside.FeatureInsideID = sFeatureInsideID;
                    tbPhotoOfInside.FileID = sPhotoID;
                    apiMultimedia.setTbPhotoOfInside(tbPhotoOfInside);

                    apiMultimediaList.add(apiMultimedia);
//                    CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);

                    CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        Log.i(TAG, "PHOTO saved to Gallery!" + DetailsTabFragment.strSDCardPathName + "Pictures/" + " : " + sPhotoID + ".jpg");

                    }
                    showAllPhoto();
//                    showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i(TAG, "media recording cancelled." + sPhotoID);
            } else {
                Log.i(TAG, "Failed to record media");
            }
        }
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

    public void showAllPhoto() {
        // TODO Auto-generated method stub
        apiMultimediaList = new ArrayList<>();
        apiMultimediaList = dbHelper.SelectDataPhotoOfInside(sFeatureInsideID, "photo");
        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
            Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            if (cd.isNetworkAvailable()) {
                ApiMultimedia apiMultimedia = new ApiMultimedia();

                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside() != null) {
                            apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                            apiMultimedia.setTbPhotoOfInside(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside());

                            apiMultimediaList.add(apiMultimedia);
                            //apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString());
                        }
                    }
                }
                Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfInside(sFeatureInsideID, "photo");

            }
        } else {
            apiMultimediaList = dbHelper.SelectDataPhotoOfInside(sFeatureInsideID, "photo");
            Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
        }
        int photolength = 0;
        if (apiMultimediaList != null) {
            Log.i(TAG, "arrDataPhoto_Outside " + String.valueOf(apiMultimediaList.size()));
            photolength = apiMultimediaList.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 70;

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;

            int totalWidth = (int) (width * photolength * density);
            int singleItemWidth = (int) (width * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    totalWidth, singleItemWidth);

            horizontal_gridView_Inside.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_Inside.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_Inside.setNumColumns(photolength);

            horizontal_gridView_Inside.setVisibility(View.VISIBLE);
            horizontal_gridView_Inside.setAdapter(new PhotoAdapter(getActivity()));
            registerForContextMenu(horizontal_gridView_Inside);
            // OnClick
            horizontal_gridView_Inside.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

//                    showViewPic(apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString());
                    Intent intent = new Intent(getActivity(), FullScreenPhoto.class);
                    Bundle extras = new Bundle();
                    extras.putString("photopath", apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

        } else {
            horizontal_gridView_Inside.setVisibility(View.GONE);
            Log.i(TAG, "Recieve_inside Null!! ");

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
            return apiMultimediaList.size();
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

            String strPath = strSDCardPathName_Pic
                    + apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString();

            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                if (cd.isNetworkAvailable()) {
                    //C:\xampp\htdocs\mCSI\assets\csifiles\CR04_000001\pictures
                    String filepath = "http://" + defaultIP + "/assets/csifiles/"
                            + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                            + apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString();
//                    Log.i(TAG, "server file name: " + filepath);
                    Picasso.with(getActivity())
                            .load(filepath)
                            .resize(50, 50)
                            .centerCrop()
                            .into(imageView);
                } else {
                    Picasso.with(getActivity())
                            .load(new File(strPath))
                            .resize(50, 50)
                            .centerCrop()
                            .into(imageView);
                }
            } else {
                Picasso.with(getActivity())
                        .load(new File(strPath))
                        .resize(50, 50)
                        .centerCrop()
                        .into(imageView);
            }

            return convertView;

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

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        showAllPhoto();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }
}