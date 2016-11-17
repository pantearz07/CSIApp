package com.scdc.csiapp.invmain;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.scdc.csiapp.invmain.ResultTabFragment.createFolder;
import static com.scdc.csiapp.invmain.ResultTabFragment.strSDCardPathName;

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
    ImageButton btnTakePhotoInside;
    public static final int REQUEST_CAMERA_OUTSIDE = 22;
    public static final int REQUEST_LOAD_IMAGE = 2;
    public static final int REQUEST_GALLERY = 222;
    String imageEncoded;
    List<String> imagesEncodedList;
    private String mCurrentPhotoPath;
    Uri uri;
    String sPhotoID, timeStamp;
    List<ApiMultimedia> apiMultimediaList;
    List<TbMultimediaFile> tbPhotoList;
    Context mContext;
    private static String strSDCardPathName_Pic = "/CSIFiles/";
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;
    Handler mHandler = new Handler();
    int INTERVAL = 1000 * 5; //20 second
    DisplayMetrics dm;

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
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        Log.i(TAG, "tbSceneFeatureInSideList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);
        btnTakePhotoInside = (ImageButton) view.findViewById(R.id.btnTakePhotoInside);
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

    private void updateData() {
        final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
    }

    private class InsideOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabBtnDetails) {
                updateData();
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
                String title = getString(R.string.importphoto);
                CharSequence[] itemlist = {getString(R.string.camera),
                        getString(R.string.gallery)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_camera_add);
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
                    takePhoto();
                    break;
                case 1:// Choose Existing Photo
                    // Do Pick Photo task here
                    pickPhoto();
                    break;
                default:
                    break;
            }
        }
    }

    private void pickPhoto() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "เลือกรูปภาพ");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        getActivity().startActivityForResult(chooserIntent, REQUEST_GALLERY);
    }

    private void takePhoto() {
        File newfile;
        createFolder();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
        sPhotoID = "IMG_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
        timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];

        String sPhotoPath = strSDCardPathName + sPhotoID + ".jpg";
        newfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), sPhotoPath);
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

    private void saveToMyAlbum(String imageEncoded) {
        try {
            createFolder();
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File datadest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String sPhotoID = "", sImageType = "";
            String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
            sPhotoID = "IMG_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
            timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];
            sImageType = imageEncoded.substring(imageEncoded.lastIndexOf("."));
            Log.i(TAG, "sPhotoID " + sPhotoID + " sImageType " + sImageType);
            if (sd.canWrite()) {
//                        String sourceImagePath = "/path/to/source/file.jpg";
                String destinationImagePath = strSDCardPathName + sPhotoID + sImageType;
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
                        saveToListDB(sPhotoID, sImageType);

                    }
                    src.close();
                    dst.close();
                    Log.i(TAG, "save new Photo from gallery " + destinationImagePath);
                } else {
                    Log.i(TAG, "Photo from gallery error ");
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Photo from gallery error " + e.getMessage());
        }
    }

    // save ข้อมูลรูปภาพไว้ใน list table และ sqlite
    private void saveToListDB(String PhotoID, String sImageType) {
        try {
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            ApiMultimedia apiMultimedia = new ApiMultimedia();
            TbMultimediaFile tbMultimediaFile = new TbMultimediaFile();
            tbMultimediaFile.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
            tbMultimediaFile.FileID = PhotoID;
            tbMultimediaFile.FileDescription = "";
            tbMultimediaFile.FileType = "photo";
            tbMultimediaFile.FilePath = PhotoID + sImageType;
            tbMultimediaFile.Timestamp = timeStamp;
            apiMultimedia.setTbMultimediaFile(tbMultimediaFile);

            TbPhotoOfInside tbPhotoOfInside = new TbPhotoOfInside();
            tbPhotoOfInside.FeatureInsideID = sFeatureInsideID;
            tbPhotoOfInside.FileID = PhotoID;
            apiMultimedia.setTbPhotoOfInside(tbPhotoOfInside);

            apiMultimediaList.add(apiMultimedia);
//                    CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);

            CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                Log.i(TAG, "PHOTO saved to Gallery! : " + PhotoID + sImageType);

            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    saveToListDB(sPhotoID, ".jpg");
                    showAllPhoto();
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

        // รับข้อมูลจากอัลบั้มภาพ และบันทึกภาพลงแอพ
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == getActivity().RESULT_OK && null != data) {
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imagesEncodedList = new ArrayList<String>();
                    if (data.getData() != null) {
                        Uri mImageUri = data.getData();
                        // Get the cursor getFilepath
                        imageEncoded = getFilepath(filePathColumn, mImageUri);
                        Log.i(TAG, "REQUEST_GALLERY " + imageEncoded);
                        saveToMyAlbum(imageEncoded);
                    } else { //ถ้าเลือกหลายรูป
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayUri.add(uri);
                                // Get the cursor getFilepath
                                imageEncoded = getFilepath(filePathColumn, uri);
                                Log.v(TAG, "REQUEST_GALLERY [" + i + "] " + imageEncoded);

                                if (imageEncoded != null) {
                                    imagesEncodedList.add(imageEncoded);
                                    saveToMyAlbum(imageEncoded);
                                }
                            }
                            Log.v(TAG, "Selected Images mArrayUri :" + mArrayUri.size() + " imagesEncodedList :" + imagesEncodedList.size());
                        }
                    }

                    showAllPhoto();
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
        // แสดงรายการรูป หลังจากกดลบรูป ในหน้า SlideShowDialogfragment
        if (requestCode == REQUEST_LOAD_IMAGE) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    showAllPhoto();
                    Log.i(TAG, "RESULT_OK");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // After Cancel code.
                Log.i(TAG, "Cancel REQUEST_LOAD_IMAGE");
            } else {
                Log.i(TAG, "Failed to REQUEST_LOAD_IMAGE");
            }

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
        tbPhotoList = new ArrayList<>();
        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
//            Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            if (cd.isNetworkAvailable()) {
                ApiMultimedia apiMultimedia = new ApiMultimedia();

                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside() != null) {
                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FeatureInsideID.equals(sFeatureInsideID)
                                    && CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside().FileID.equals(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID)) {
                                apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                apiMultimedia.setTbPhotoOfInside(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfInside());
                                tbPhotoList.add(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                apiMultimediaList.add(apiMultimedia);
                                //apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString());
                            }
                        }
                    }
                }
//                Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfInside(sFeatureInsideID, "photo");
                for (int i = 0; i < apiMultimediaList.size(); i++) {
                    tbPhotoList.add(apiMultimediaList.get(i).getTbMultimediaFile());
                }
            }
        } else {
            apiMultimediaList = dbHelper.SelectDataPhotoOfInside(sFeatureInsideID, "photo");
            Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            for (int i = 0; i < apiMultimediaList.size(); i++) {
                tbPhotoList.add(apiMultimediaList.get(i).getTbMultimediaFile());
            }
        }
        int photolength = 0;
        if (apiMultimediaList != null) {
//            Log.i(TAG, "arrDataPhoto_inside " + String.valueOf(apiMultimediaList.size()));
            photolength = apiMultimediaList.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 70;

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

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", (Serializable) tbPhotoList);
                    bundle.putInt("position", position);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    newFragment.setTargetFragment(AddFeatureInsideFragment.this, REQUEST_LOAD_IMAGE);
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
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
            Log.i(TAG, "strPath " + strPath);
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);
            final File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strPath);
            final String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/pictures/"
                    + apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString();
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                if (cd.isNetworkAvailable()) {
                    Picasso.with(getActivity())
                            .load(filepath)
                            .resize(100, 100)
                            .centerCrop()
                            .placeholder(R.drawable.ic_imagefile)
                            .error(R.drawable.ic_imagefile)
                            .into(imageView);
                } else {

                    if (curfile.exists()) {
                        Picasso.with(getActivity())
                                .load(curfile)
                                .resize(100, 100)
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
                            .resize(100, 100)
                            .placeholder(R.drawable.ic_imagefile)
                            .error(R.drawable.ic_imagefile)
                            .centerCrop()
                            .into(imageView);
                } else {
                    if (cd.isNetworkAvailable()) {
                        Picasso.with(getActivity())
                                .load(filepath)
                                .resize(100, 100)
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        showAllPhoto();

    }
}