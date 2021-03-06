package com.scdc.csiapp.invmain;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.scdc.csiapp.tablemodel.TbGatewayCriminal;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.scdc.csiapp.tablemodel.TbPhotoOfResultscene;
import com.scdc.csiapp.tablemodel.TbResultScene;
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

public class AddGatewayFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddGatewayFragment";
    private GridView horizontal_gridView_GC, horizontal_gridView_GC_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    DBHelper dbHelper;
    ResultTabFragment resultTabFragment;
    String sRSID, mode, typeid;
    int position = 0;
    TbResultScene tbResultScene;
    TbGatewayCriminal tbGatewayCriminal;
    AutoCompleteTextView editGatewayCriminalDetails;
    Button btn_clear_txt;
    List<ApiMultimedia> apiMultimediaList;
    List<TbMultimediaFile> tbPhotoList;
    ImageButton btnTakePhotoGC;
    String sPhotoID, timeStamp;
    public static final int REQUEST_CAMERA = 55;
    public static final int REQUEST_LOAD_IMAGE = 5;
    public static final int REQUEST_GALLERY = 555;
    String imageEncoded;
    List<String> imagesEncodedList;
    GetPathUri getPathUri;
    private String mCurrentPhotoPath;
    Uri uri;
    Context mContext;
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;
    Handler mHandler = new Handler();
    int INTERVAL = 1000 * 5; //20 second
    DisplayMetrics dm;
    int height = 0;
    int width = 0;

    public AddGatewayFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_gatewaycriminal, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มทางเข้าออกของคนร้าย");

        //call the main activity set tile method
        Bundle args = getArguments();
        sRSID = args.getString(ResultTabFragment.Bundle_ID);
        mode = args.getString(ResultTabFragment.Bundle_mode);
        typeid = args.getString(ResultTabFragment.Bundle_RSType);
        Log.i(TAG, "sRSID " + sRSID + " typeid " + typeid);
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Log.i(TAG, typeid + " GatewayCriminalsList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));
        cd = new ConnectionDetector(getActivity());
        mContext = view.getContext();
        getPathUri = new GetPathUri();
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);


        dbHelper = new DBHelper(getActivity());
        tbResultScene = new TbResultScene();
        tbGatewayCriminal = new TbGatewayCriminal();
        resultTabFragment = new ResultTabFragment();
        getDateTime = new GetDateTime();

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);
        btnTakePhotoGC = (ImageButton) view.findViewById(R.id.btnTakePhotoGC);
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        editGatewayCriminalDetails = (AutoCompleteTextView) view.findViewById(R.id.editGatewayCriminalDetails);
//        String[] mGateClueArray;
//        mGateClueArray = getResources().getStringArray(
//                R.array.gate_clue);
//        ArrayAdapter<String> adapterGateClue = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_dropdown_item_1line,
//                mGateClueArray);
//        editGatewayCriminalDetails.setThreshold(1);
//        editGatewayCriminalDetails.setAdapter(adapterGateClue);
        btn_clear_txt = (Button) view.findViewById(R.id.btn_clear_txt);
        editGatewayCriminalDetails.addTextChangedListener(new RSTextWatcher(editGatewayCriminalDetails));
        if (mode == "edit") {
            position = args.getInt(ResultTabFragment.Bundle_Index, -1);
            Log.i(TAG, "position " + position);
            tbGatewayCriminal = (TbGatewayCriminal) args.getSerializable(ResultTabFragment.Bundle_TB);
            editGatewayCriminalDetails.setText(tbGatewayCriminal.getRSDetail());
        }
        horizontal_gridView_GC = (GridView) view.findViewById(R.id.horizontal_gridView_GC);
        horizontal_gridView_GC_video = (GridView) view.findViewById(R.id.horizontal_gridView_GC_video);
        txtPhoto = (TextView) view.findViewById(R.id.txtPhoto);
        txtVideo = (TextView) view.findViewById(R.id.txtVideo);
        txtVideo.setVisibility(View.GONE);
        showAllPhoto();
        btnTakePhotoGC.setOnClickListener(new InsideOnClickListener());
        fabBtnDetails.setOnClickListener(new InsideOnClickListener());
        if (CSIDataTabFragment.mode == "view") {
            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtnDetails.setLayoutParams(p);
            fabBtnDetails.hide();
            editGatewayCriminalDetails.setEnabled(false);
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
                tbGatewayCriminal.RSID = sRSID;
                tbGatewayCriminal.RSTypeID = typeid;
                tbGatewayCriminal.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                ResultTabFragment.tbGatewayCriminals.add(tbGatewayCriminal);
                //add list
                CSIDataTabFragment.apiCaseScene.setTbGatewayCriminals(ResultTabFragment.tbGatewayCriminals);
                Log.i(TAG, typeid + " tbGatewayCriminals num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if (mode == "edit") {
                        CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().remove(position);
                        Log.i(TAG, typeid + " tbGatewayCriminals remove num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));

                    } else {
                        Log.i(TAG, typeid + " tbGatewayCriminals num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));

                    }
                    getActivity().onBackPressed();
                }

            }
            if (v == btn_clear_txt) {
                editGatewayCriminalDetails.setText("");
            }
            if (v == btnTakePhotoGC) {
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

    private class RSTextWatcher implements android.text.TextWatcher {
        private EditText mEditText;

        public RSTextWatcher(EditText editText) {
            mEditText = editText;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mEditText == editGatewayCriminalDetails) {
                btn_clear_txt.setVisibility(View.VISIBLE);
                btn_clear_txt.setOnClickListener(new InsideOnClickListener());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == editGatewayCriminalDetails.getEditableText()) {
                tbGatewayCriminal.RSDetail = editGatewayCriminalDetails.getText().toString();
                Log.i(TAG, "editGatewayCriminalDetails " + tbGatewayCriminal.RSDetail);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Result media " + String.valueOf(requestCode) + " " + String.valueOf(resultCode));


        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == getActivity().RESULT_OK) {
                try {

                    Log.i(TAG, "Photo save " + sPhotoID);
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
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพ"), REQUEST_GALLERY);

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
                    , "Take a picture with"), REQUEST_CAMERA);
        }
    }

    private void saveToMyAlbum(String imageEncoded) {
        try {
            createFolder();
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File datadest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String sPhotoID = "", sImageType = "";
            String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
            sPhotoID = "IMG_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5] + CurrentDate_ID[6];
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

            TbPhotoOfResultscene tbPhotoOfResultscene = new TbPhotoOfResultscene();
            tbPhotoOfResultscene.RSID = sRSID;
            tbPhotoOfResultscene.FileID = PhotoID;
            apiMultimedia.setTbPhotoOfResultscene(tbPhotoOfResultscene);

            apiMultimediaList.add(apiMultimedia);
            CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
            Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                Log.i(TAG, "PHOTO saved to Gallery!" + ResultTabFragment.strSDCardPathName + "Pictures/" + " : " + PhotoID + sImageType);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene() != null) {
                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().RSID.equals(sRSID)
                                    && CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().FileID.equals(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID)) {
                                apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                apiMultimedia.setTbPhotoOfResultscene(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene());
                                apiMultimediaList.add(apiMultimedia);
                                tbPhotoList.add(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());

                            }
                        }
                    }
                }
//                Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfResultscene(sRSID, "photo");
                for (int i = 0; i < apiMultimediaList.size(); i++) {
                    tbPhotoList.add(apiMultimediaList.get(i).getTbMultimediaFile());
                }
            }
        } else {
            apiMultimediaList = dbHelper.SelectDataPhotoOfResultscene(sRSID, "photo");
            Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            for (int i = 0; i < apiMultimediaList.size(); i++) {
                tbPhotoList.add(apiMultimediaList.get(i).getTbMultimediaFile());
            }
        }
        int photolength = 0;

//        arrDataPhoto = mDbHelper.SelectDataPhotoOfOutside(reportID, "photo");
        //Log.i("arrDataPhoto_Outside",arrDataPhoto[0][0]);
        if (apiMultimediaList != null) {
//            Log.i(TAG, "arrDataPhoto_Resultscene " + String.valueOf(apiMultimediaList.size()));
            photolength = apiMultimediaList.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 70;

            float density = dm.density;

            int totalWidth = (int) (width * photolength * density);
            int singleItemWidth = (int) (width * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    totalWidth, singleItemWidth);

            horizontal_gridView_GC.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_GC.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_GC.setNumColumns(photolength);

            horizontal_gridView_GC.setVisibility(View.VISIBLE);
            horizontal_gridView_GC.setAdapter(new PhotoAdapter(getActivity()));
            registerForContextMenu(horizontal_gridView_GC);
            // OnClick
            horizontal_gridView_GC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", (Serializable) tbPhotoList);
                    bundle.putInt("position", position);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    newFragment.setTargetFragment(AddGatewayFragment.this, REQUEST_LOAD_IMAGE);
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }
            });
        } else {
            horizontal_gridView_GC.setVisibility(View.GONE);
            Log.i(TAG, "Recieve_Resultscene  Null!! ");

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
            String strPath = strSDCardPathName
                    + apiMultimediaList.get(position).getTbMultimediaFile().FilePath.toString();

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

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        showAllPhoto();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume detailscase");
        showAllPhoto();
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
        showAllPhoto();
        mHandler.removeCallbacks(mHandlerReload);
        mHandlerReload.run();
    }

    Runnable mHandlerReload = new Runnable() {
        @Override
        public void run() {
            showAllPhoto();
            INTERVAL = 1000 * 30;
            mHandler.postDelayed(mHandlerReload, INTERVAL);
        }
    };
}

