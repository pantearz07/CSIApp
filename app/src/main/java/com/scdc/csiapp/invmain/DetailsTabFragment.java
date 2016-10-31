package com.scdc.csiapp.invmain;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.ActivityResultBus;
import com.scdc.csiapp.main.ActivityResultEvent;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.MainActivity;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.scdc.csiapp.tablemodel.TbPhotoOfOutside;
import com.scdc.csiapp.tablemodel.TbSceneFeatureInSide;
import com.scdc.csiapp.tablemodel.TbSceneFeatureOutside;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pantearz07 on 22/9/2558.
 */
public class DetailsTabFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    DBHelper dbHelper;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String[] updateDT, datetime;
    String officialID, reportID;
    AutoCompleteTextView autoCompleteTypeOutside;
    Spinner spnTypeReceive;
    Button btn_clear_txt_14, btn_clear_txt_15, btn_clear_txt_16, btn_clear_txt_17,
            btn_clear_txt_18, btn_clear_txt_19, btn_clear_txt_20, btn_clear_txt_21,
            btn_clear_txt_22, btn_clear_txt_23;
    CheckBox checkBoxHaveFence, checkBoxHaveMezzanine, checkBoxHaveRoofTop;
    EditText edtFloorNum, edtCaveNum, editDetailOutside,
            editOutsideAroundBack, editOutsideAroundLeft,
            editOutsideAroundRight, editOutsideAroundFront,
            editDetailInside, editFeatureAtTheScene;
    private GridView horizontal_gridView_Outside;
    String sOutsideTypeName, sFloorNum, sCaveNum, sOutsideTypeDetail,
            sFrontSide, sLeftSide, sRightSide, sBackSide, sSceneZone, sFeatureInsideDetail, sHaveFence, sHaveMezzanine, sHaveRoofTop;
    Boolean sHaveFenceBoolean, sHaveMezzanineBoolean,
            sHaveRoofTopBoolean;
    ImageButton btn_camera;
    //View linearLayoutAddFeatureInside;
    private ArrayList<HashMap<String, String>> featureInsideList;

    protected static final int DIALOG_AddFeatureInside = 0;
    ViewGroup viewByIdaddinside;
    TextView edtUpdateDateTime;
    public static final int REQUEST_CAMERA_OUTSIDE = 333;
    private String mCurrentPhotoPath;
    Uri uri;
    public static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    String sPhotoID, timeStamp;
    String arrDataPhoto[][], arrDataPhoto2[][], arrDataVideo[][];

    GridView horizontal_gridView_Inside_photo, horizontal_gridView_Inside_video;
    TextView txtPhoto, txtVideo;
    private View mViewAddFeatureInside;
    ImageButton btnShowHide1;
    private boolean viewGroupIsVisible = true;
    private static final String TAG = "DEBUG-DetailsTabFragment";
    private TbSceneFeatureOutside tbSceneFeatureOutside;
    private Snackbar snackbar;
    public static String Bundle_InsideID = "insideid";
    public static String Bundle_InsideTB = "tbSceneFeatureInSide";
    public static String Bundle_Inside_mode = "mode";
    public static String Bundle_Index = "position";
    public static List<TbSceneFeatureInSide> tbSceneFeatureInSideList = null;
    Button btnAddFeatureInside;
    ListView listViewAddFeatureInside;
    AddFeatureInsideFragment addFeatureInsideFragment;
    public static List<TbMultimediaFile> tbMultimediaFiles = null;
    List<TbMultimediaFile> tbPhotoList;
    List<TbPhotoOfOutside> tbPhotoOfOutsideList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewDetails = inflater.inflate(R.layout.details_tab_layout, null);
        mContext = viewDetails.getContext();
        rootLayout = (CoordinatorLayout) viewDetails.findViewById(R.id.rootLayout);
        mDbHelper = new SQLiteDBHelper(getActivity());
        dbHelper = new DBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        cd = new ConnectionDetector(getActivity());
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        ////
        reportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().getCaseReportID();
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();
        ////
        tbSceneFeatureOutside = new TbSceneFeatureOutside();
        if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide() == null) {
            tbSceneFeatureInSideList = new ArrayList<>();
            Log.i(TAG, "getTbSceneFeatureInSide null");
        } else {
            tbSceneFeatureInSideList = CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide();
            Log.i(TAG, "getTbSceneFeatureInSide not null");
        }

        addFeatureInsideFragment = new AddFeatureInsideFragment();
        mDbHelper = new SQLiteDBHelper(getActivity());
        updateDT = getDateTime.getDateTimeNow();
        datetime = getDateTime.getDateTimeCurrent();
        Log.i("page viewDetails", reportID);
        btn_clear_txt_14 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_14);
        btn_clear_txt_15 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_15);
        btn_clear_txt_16 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_16);
        btn_clear_txt_17 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_17);
        btn_clear_txt_18 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_18);
        btn_clear_txt_19 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_19);
        btn_clear_txt_20 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_20);
        btn_clear_txt_21 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_21);
        btn_clear_txt_22 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_22);
        btn_clear_txt_23 = (Button) viewDetails.findViewById(R.id.btn_clear_txt_23);

        mViewAddFeatureInside = viewDetails.findViewById(R.id.tableRowFeatureInsideLayout);
        mViewAddFeatureInside.setVisibility(View.VISIBLE);
        btnShowHide1 = (ImageButton) viewDetails
                .findViewById(R.id.btnShowHide1);
        btnShowHide1.setOnClickListener(new DetailsOnClickListener());

        edtUpdateDateTime = (TextView) viewDetails.findViewById(R.id.edtUpdateDateTime);
        edtUpdateDateTime.setText("อัพเดทข้อมูลเมื่อ " + getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate) + " เวลา " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime);

        // ลักษณะสถานที่เกิดเหตุ
        // ภายนอก
        autoCompleteTypeOutside = (AutoCompleteTextView) viewDetails
                .findViewById(R.id.autoCompleteTypeOutside);
        final String[] mOutsideTypeArray = getResources().getStringArray(
                R.array.type_outside);
        ArrayAdapter<String> adapterOutsideType = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_dropdown_item_1line,
                mOutsideTypeArray);
        autoCompleteTypeOutside.setThreshold(1);
        autoCompleteTypeOutside.setAdapter(adapterOutsideType);

        // มีชั้นเเบบ
        checkBoxHaveFence = (CheckBox) viewDetails
                .findViewById(R.id.checkBoxHaveFence);
        checkBoxHaveMezzanine = (CheckBox) viewDetails
                .findViewById(R.id.checkBoxHaveMezzanine);
        checkBoxHaveRoofTop = (CheckBox) viewDetails
                .findViewById(R.id.checkBoxHaveRoofTop);
        checkBoxHaveFence.setOnClickListener(new DetailsOnClickListener());
        checkBoxHaveMezzanine.setOnClickListener(new DetailsOnClickListener());
        checkBoxHaveRoofTop.setOnClickListener(new DetailsOnClickListener());


        // จำนวนชั้น
        edtFloorNum = (EditText) viewDetails.findViewById(R.id.edtFloorNum);
        // จำนวนคูหา
        edtCaveNum = (EditText) viewDetails.findViewById(R.id.edtCaveNum);
        editDetailOutside = (EditText) viewDetails.findViewById(R.id.editDetailOutside);
        editOutsideAroundBack = (EditText) viewDetails
                .findViewById(R.id.editOutsideAroundBack);
        editOutsideAroundLeft = (EditText) viewDetails
                .findViewById(R.id.editOutsideAroundLeft);
        editOutsideAroundRight = (EditText) viewDetails.findViewById(R.id.editOutsideAroundRight);
        editOutsideAroundFront = (EditText) viewDetails
                .findViewById(R.id.editOutsideAroundFront);
        // บริเวณที่เกิดเหตุ
        editFeatureAtTheScene = (EditText) viewDetails
                .findViewById(R.id.editFeatureAtTheScene);

        if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside() != null) {
            Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaseReportID);
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeName == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeName.equals("")) {
                autoCompleteTypeOutside.setText("");
            } else {
                autoCompleteTypeOutside.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeName);
            }
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveFence == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveFence.equals("")) {
                checkBoxHaveFence.setChecked(false);
            } else {
                if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveFence.equals("1")) {
                    checkBoxHaveFence.setChecked(true);
                } else {
                    checkBoxHaveFence.setChecked(false);
                }
            }
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine.equals("")) {
                checkBoxHaveMezzanine.setChecked(false);
            } else {
                if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine.equals("1")) {
                    checkBoxHaveMezzanine.setChecked(true);
                } else {
                    checkBoxHaveMezzanine.setChecked(false);
                }
            }
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveRooftop == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveRooftop.equals("")) {
                checkBoxHaveRoofTop.setChecked(false);
            } else {
                if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveRooftop.equals("1")) {
                    checkBoxHaveRoofTop.setChecked(true);
                } else {
                    checkBoxHaveRoofTop.setChecked(false);
                }
            }

            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FloorNum == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FloorNum.equals("")) {
                edtFloorNum.setText("");
            } else {
                edtFloorNum.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FloorNum);
            }
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaveNum == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaveNum.equals("")) {
                edtCaveNum.setText("");
            } else {
                edtCaveNum.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaveNum);
            }
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeDetail == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeDetail.equals("")) {
                editDetailOutside.setText("");
            } else {
                editDetailOutside.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeDetail);
            }
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().BackSide == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().BackSide.equals("")) {
                editOutsideAroundBack.setText("");
            } else {
                editOutsideAroundBack.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().BackSide);
            }

            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().LeftSide == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().LeftSide.equals("")) {
                editOutsideAroundLeft.setText("");
            } else {
                editOutsideAroundLeft.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().LeftSide);
            }
            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().RightSide == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().RightSide.equals("")) {
                editOutsideAroundRight.setText("");
            } else {
                editOutsideAroundRight.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().RightSide);
            }

            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FrontSide == null || CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FrontSide.equals("")) {
                editOutsideAroundFront.setText("");
            } else {
                editOutsideAroundFront.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FrontSide);
            }

            if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().SceneZone == null) {
                editFeatureAtTheScene.setText("");
            } else {
                editFeatureAtTheScene.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().SceneZone);
            }
        } else {
            CSIDataTabFragment.apiCaseScene.setTbSceneFeatureOutside(tbSceneFeatureOutside);

            CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
            Log.i(TAG, "new " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaseReportID);
        }
        autoCompleteTypeOutside.addTextChangedListener(new DetailTextWatcher(autoCompleteTypeOutside));
        edtFloorNum.addTextChangedListener(new DetailTextWatcher(edtFloorNum));
        edtCaveNum.addTextChangedListener(new DetailTextWatcher(edtCaveNum));
        editDetailOutside.addTextChangedListener(new DetailTextWatcher(editDetailOutside));
        editOutsideAroundBack.addTextChangedListener(new DetailTextWatcher(editOutsideAroundBack));
        editOutsideAroundLeft.addTextChangedListener(new DetailTextWatcher(editOutsideAroundLeft));
        editOutsideAroundRight.addTextChangedListener(new DetailTextWatcher(editOutsideAroundRight));
        editOutsideAroundFront.addTextChangedListener(new DetailTextWatcher(editOutsideAroundFront));
        editFeatureAtTheScene.addTextChangedListener(new DetailTextWatcher(editFeatureAtTheScene));

        horizontal_gridView_Outside = (GridView) viewDetails.findViewById(R.id.horizontal_gridView_Outside);

        showAllPhoto();
        btn_camera = (ImageButton) viewDetails.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new DetailsOnClickListener());


// ภายใน
        editDetailInside = (EditText) viewDetails
                .findViewById(R.id.editDetailInside);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().FeatureInsideDetail == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().FeatureInsideDetail.equals("")) {
            editDetailInside.setText("");
        } else {
            editDetailInside.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().FeatureInsideDetail);
        }
        editDetailInside.addTextChangedListener(new DetailTextWatcher(editDetailInside));
        listViewAddFeatureInside = (ListView) viewDetails
                .findViewById(R.id.listViewAddFeatureInside);
        btnAddFeatureInside = (Button) viewDetails
                .findViewById(R.id.btnAddFeatureInside);

        listViewAddFeatureInside.setVisibility(View.GONE);
        listViewAddFeatureInside.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedFeatureInside();

        btnAddFeatureInside.setOnClickListener(new DetailsOnClickListener());

        fabBtnDetails = (FloatingActionButton) viewDetails.findViewById(R.id.fabBtnDetails);
        fabBtnDetails.setOnClickListener(new DetailsOnClickListener());
        if (CSIDataTabFragment.mode == "view") {

            autoCompleteTypeOutside.setEnabled(false);
            checkBoxHaveFence.setEnabled(false);
            checkBoxHaveMezzanine.setEnabled(false);
            checkBoxHaveRoofTop.setEnabled(false);
            editDetailInside.setEnabled(false);
            autoCompleteTypeOutside.setEnabled(false);
            edtFloorNum.setEnabled(false);
            edtCaveNum.setEnabled(false);
            editDetailOutside.setEnabled(false);
            editOutsideAroundBack.setEnabled(false);
            editOutsideAroundLeft.setEnabled(false);
            editOutsideAroundRight.setEnabled(false);
            editOutsideAroundFront.setEnabled(false);
            editFeatureAtTheScene.setEnabled(false);

            btn_camera.setVisibility(View.GONE);
            btnAddFeatureInside.setVisibility(View.GONE);

            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtnDetails.setLayoutParams(p);
            fabBtnDetails.hide();
        }
        return viewDetails;

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result media", String.valueOf(requestCode) + " " + String.valueOf(resultCode));


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

                    TbPhotoOfOutside tbPhotoOfOutside = new TbPhotoOfOutside();
                    tbPhotoOfOutside.CaseReportID = reportID;
                    tbPhotoOfOutside.FileID = sPhotoID;
                    apiMultimedia.setTbPhotoOfOutside(tbPhotoOfOutside);

                    apiMultimediaList.add(apiMultimedia);
                    CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);

                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        Log.i(TAG, "apiMultimediaList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                        Log.i(TAG, "PHOTO saved to Gallery!" + strSDCardPathName + "Pictures/" + " : " + sPhotoID + ".jpg");

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


    public void showAllPhoto() {
        // TODO Auto-generated method stub
        tbPhotoList = new ArrayList<>();
        tbPhotoList = dbHelper.SelectDataPhotoOfOutside(reportID, "photo");
        int photolength = 0;

//        arrDataPhoto = mDbHelper.SelectDataPhotoOfOutside(reportID, "photo");
        //Log.i("arrDataPhoto_Outside",arrDataPhoto[0][0]);
        if (tbPhotoList != null) {
            Log.i(TAG, "arrDataPhoto_Outside " + String.valueOf(tbPhotoList.size()));
//            photolength = arrDataPhoto.length;
            photolength = tbPhotoList.size();
            //int size=list.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 70;

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;

            int totalWidth = (int) (width * photolength * density);
            int singleItemWidth = (int) (width * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    totalWidth, singleItemWidth);

            horizontal_gridView_Outside.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_Outside.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_Outside.setNumColumns(photolength);

            horizontal_gridView_Outside.setVisibility(View.VISIBLE);
            horizontal_gridView_Outside.setAdapter(new PhotoAdapter(getActivity()));
            registerForContextMenu(horizontal_gridView_Outside);
            // OnClick
            horizontal_gridView_Outside.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    showViewPic(tbPhotoList.get(position).FilePath.toString());
                }
            });
        } else {
            horizontal_gridView_Outside.setVisibility(View.GONE);
            Log.i("Recieve_Outside", "Null!! ");

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
        Picasso.with(getActivity())
                .load(new File(strPath))
                .resize(50, 50)
                .centerCrop()
                .into(imageView);
//        Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
//        int width = bmpSelectedImage.getWidth();
//        int height = bmpSelectedImage.getHeight();
//        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
//        Bitmap resizedBitmap = Bitmap.createBitmap(bmpSelectedImage, 0, 0,
//                width, height, matrix, true);
//        imageView.setImageBitmap(resizedBitmap);
        dialog.show();
    }

    public class PhotoAdapter extends BaseAdapter {
        private Context context;


        public PhotoAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tbPhotoList.size();
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
            String root = Environment.getExternalStorageDirectory().toString();

            String strPath = root + "/CSIFiles/Pictures/"
                    + tbPhotoList.get(position).FilePath.toString();

            Log.i("list photo", "/CSIFiles/Pictures/" + tbPhotoList.get(position).FilePath.toString());
            // "file:///android_asset/DvpvklR.png"
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);
            Picasso.with(getActivity())
                    .load(new File(strPath))
                    .resize(50, 50)
                    .centerCrop()
                    .into(imageView);
//            Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
//            if (bmpSelectedImage != null) {
//                int width1 = bmpSelectedImage.getWidth();
//                int height1 = bmpSelectedImage.getHeight();
//                Log.i("size", width1 + " " + height1);
//                int width = width1 / 13;
//                int height = height1 / 13;
//                Log.i("resize", width + " " + height);
//                Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage,
//                        width, height, true);
//                imageView.setImageBitmap(resizedbitmap);
//            }

            return convertView;

        }
    }

    public class DetailsOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == fabBtnDetails) {
                Log.i(TAG, "fabBtnDetails");
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                if (CSIDataTabFragment.apiCaseScene.getTbCaseScene() != null) {
                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        if (snackbar == null || !snackbar.isShown()) {
                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_complete)
                                    + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID
                                    + "\n" + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate
                                    + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });
                            snackbar.show();
                        }
                    } else {
                        if (snackbar == null || !snackbar.isShown()) {
                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error) + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID.toString(), Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                        }
                                    });
                            snackbar.show();
                        }
                    }
                }

            }
            if (v == btnAddFeatureInside) {
                Log.i(TAG, "btnAddFeatureInside");
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                String sFeatureInsideID = "IN_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                Bundle i = new Bundle();
                i.putString(Bundle_InsideID, sFeatureInsideID);
                i.putString(Bundle_Inside_mode, "new");
                addFeatureInsideFragment.setArguments(i);
                MainActivity.setFragment(addFeatureInsideFragment, 1);

            }
            if (v == btnShowHide1) {
                if (viewGroupIsVisible) {
                    mViewAddFeatureInside.setVisibility(View.VISIBLE);
                    btnShowHide1.setImageResource(R.drawable.ic_maxlayout);
                } else {
                    mViewAddFeatureInside.setVisibility(View.GONE);
                    btnShowHide1.setImageResource(R.drawable.ic_minlayout);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if (v == btn_camera) {

                File newfile;
                createFolder("Pictures");

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sPhotoID = "IMG_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];

                String sPhotoPath = sPhotoID + ".jpg";
                newfile = new File(strSDCardPathName, "Pictures/" + sPhotoPath);
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
            if (v == checkBoxHaveFence) {
                if (checkBoxHaveFence.isChecked()) {
                    CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveFence = "1";
                    Log.i(TAG, "HaveFence " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveFence);
                } else {
                    CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveFence = "0";
                    Log.i(TAG, "HaveFence " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveFence);
                }
            }
            if (v == checkBoxHaveMezzanine) {
                if (checkBoxHaveMezzanine.isChecked()) {
                    CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine = "1";
                    Log.i(TAG, "HaveMezzanine " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine);
                } else {
                    CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine = "0";
                    Log.i(TAG, "HaveMezzanine " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveMezzanine);
                }
            }
            if (v == checkBoxHaveRoofTop) {
                if (checkBoxHaveRoofTop.isChecked()) {
                    CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveRooftop = "1";
                    Log.i(TAG, "HaveRooftop " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveRooftop);
                } else {
                    CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveRooftop = "0";
                    Log.i(TAG, "HaveRooftop " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().HaveRooftop);
                }
            }
            if (v == btn_clear_txt_14) {
                autoCompleteTypeOutside.setText("");
            }
            if (v == btn_clear_txt_15) {
                edtFloorNum.setText("");
            }
            if (v == btn_clear_txt_16) {
                edtCaveNum.setText("");
            }
            if (v == btn_clear_txt_17) {
                editDetailOutside.setText("");
            }
            if (v == btn_clear_txt_18) {
                editOutsideAroundBack.setText("");
            }
            if (v == btn_clear_txt_19) {
                editOutsideAroundLeft.setText("");
            }
            if (v == btn_clear_txt_20) {
                editOutsideAroundRight.setText("");
            }
            if (v == btn_clear_txt_21) {
                editOutsideAroundFront.setText("");
            }
            if (v == btn_clear_txt_22) {
                editDetailInside.setText("");
            }
            if (v == btn_clear_txt_23) {
                editFeatureAtTheScene.setText("");
            }
        }
    }

    public void ShowSelectedFeatureInside() {
        // TODO Auto-generated method stub
//        featureInsideList = mDbHelper.SelectAllFeatureInside(reportID);

        if (CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide() != null) {
            listViewAddFeatureInside.setAdapter(new FeatureInsideAdapter(
                    getActivity()));
            Log.i("featureInsideList", String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));
            setListViewHeightBasedOnItems(listViewAddFeatureInside);
            listViewAddFeatureInside.setVisibility(View.VISIBLE);
        } else {
            listViewAddFeatureInside.setVisibility(View.GONE);
        }

    }

    public class FeatureInsideAdapter extends BaseAdapter {
        private Context context;

        public FeatureInsideAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_featureinside_data,
                        null);

            }
            final String sFeatureInsideID = CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getFeatureInsideID();
            final String sCaseReportID = CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getCaseReportID();
            final TextView showFeatureInsideFloor = (TextView) convertView
                    .findViewById(R.id.showFeatureInsideFloor);
            final TextView showFeatureInsideCave = (TextView) convertView
                    .findViewById(R.id.showFeatureInsideCave);
            showFeatureInsideFloor.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getFloorNo());
            showFeatureInsideCave.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getCaveNo());
            final TextView editFeatureInsideClassBack = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassBack2);
            editFeatureInsideClassBack.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getBackInside());

            final TextView editFeatureInsideClassLeft = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassLeft2);
            editFeatureInsideClassLeft.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getLeftInside());

            final TextView editFeatureInsideClassCenter = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassCenter2);
            editFeatureInsideClassCenter.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getCenterInside());

            final TextView editFeatureInsideClassRight = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassRight2);
            editFeatureInsideClassRight.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getRightInside());
            final TextView editFeatureInsideClassFront = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassFront2);
            editFeatureInsideClassFront.setText(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position).getFrontInside());


            //horizontal_gridView_Inside_photo= (GridView)convertView.findViewById(R.id.horizontal_gridView_Inside_photo);
            //horizontal_gridView_Inside_video= (GridView)convertView.findViewById(R.id.horizontal_gridView_Inside_video);
            txtPhoto = (TextView) convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView) convertView.findViewById(R.id.txtVideo);
            txtVideo.setVisibility(View.GONE);
            List<ApiMultimedia> apiMultimediaList = dbHelper.SelectDataPhotoOfInside(sFeatureInsideID, "photo");

            if (apiMultimediaList != null) {
                Log.i(TAG, "apiMultimediaList Inside " + sFeatureInsideID + " " + String.valueOf(apiMultimediaList.size()));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(apiMultimediaList.size()) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i(TAG, "apiMultimediaList Inside " + sFeatureInsideID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, " sFeatureInsideID " + sFeatureInsideID);
                    Log.i(TAG, " sCaseReportID " + sCaseReportID);
                    final TbSceneFeatureInSide tbSceneFeatureInSide = CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().get(position);
                    Bundle i = new Bundle();
                    i.putString(Bundle_InsideID, sFeatureInsideID);
                    i.putString(Bundle_Inside_mode, "edit");
                    i.putInt(Bundle_Index, position);
                    i.putSerializable(Bundle_InsideTB, tbSceneFeatureInSide);
                    addFeatureInsideFragment.setArguments(i);
                    MainActivity.setFragment(addFeatureInsideFragment, 1);
                }
            });

            // imgDelete
            ImageButton imgDelete = (ImageButton) convertView
                    .findViewById(R.id.imgDelete);

            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    getActivity());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adb.setTitle("ลบข้อมูล");
                    adb.setMessage("ยืนยันการลบข้อมูล");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().remove(position);
                                    long flg = dbHelper.DeleteSelectedData("scenefeatureinside", "FeatureInsideID", sFeatureInsideID);
//                                    long flg = dbHelper
//                                            .DeleteSelectedFeatureInside(sFeatureInsideID);
                                    if (flg > 0) {
                                        Log.i(TAG, "scenefeatureinside " + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneFeatureInSide().size()));
                                        ShowSelectedFeatureInside();
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.delete_complete)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedFeatureInside();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
//                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllInside(sFeatureInsideID);
//                                        if (saveStatus2 <= 0) {
//                                            Log.i("DeletePhotoOf inside", "Cannot delete!! ");
//
//                                        } else {
//                                            Toast.makeText(getActivity(),
//                                                    "ลบข้อมูลเรียบรอยแล้ว",
//                                                    Toast.LENGTH_LONG).show();
//                                            ShowSelectedFeatureInside(reportID);
//                                        }


                                    } else {
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedFeatureInside();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    }

                                }

                            });
                    adb.show();
                }
            });
            if (CSIDataTabFragment.mode == "view") {
                imgDelete.setVisibility(View.GONE);
                imgEdit.setVisibility(View.GONE);
            }
            return convertView;

        }

    }

    public class ListviewSetOnTouchListener implements ListView.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle ListView touch events.
            v.onTouchEvent(event);
            return true;
        }
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();
            Log.i("inside", String.valueOf(numberOfItems));
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                Log.i("inside", String.valueOf(item.getMeasuredHeight()));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.

            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            int totalHeight = totalItemsHeight + totalDividersHeight;
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            //params.height = (int) (totalItemsHeight-(totalItemsHeight/1.5));
            params.height = totalHeight;
            Log.i("inside totalHeight", String.valueOf(totalHeight));
            //  Log.i("inside getDividerHeight", String.valueOf(totalItemsHeight) + " " + String.valueOf(totalItemsHeight - (totalItemsHeight / 1.5)));
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart detailscase ");
        showAllPhoto();
        ShowSelectedFeatureInside();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop detailscase");

        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume detailscase");

//        ShowSelectedFeatureInside(reportID);
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
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i(TAG, "onPause detailscase");
    }

    private class DetailTextWatcher implements TextWatcher {
        private EditText mEditText;

        public DetailTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText == autoCompleteTypeOutside) {
                btn_clear_txt_14.setVisibility(View.VISIBLE);
                btn_clear_txt_14.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == edtFloorNum) {
                btn_clear_txt_15.setVisibility(View.VISIBLE);
                btn_clear_txt_15.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == edtCaveNum) {
                btn_clear_txt_16.setVisibility(View.VISIBLE);
                btn_clear_txt_16.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == editDetailOutside) {
                btn_clear_txt_17.setVisibility(View.VISIBLE);
                btn_clear_txt_17.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == editOutsideAroundBack) {
                btn_clear_txt_18.setVisibility(View.VISIBLE);
                btn_clear_txt_18.setOnClickListener(new DetailsOnClickListener());
            }

            if (mEditText == editOutsideAroundLeft) {
                btn_clear_txt_19.setVisibility(View.VISIBLE);
                btn_clear_txt_19.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == editOutsideAroundRight) {
                btn_clear_txt_20.setVisibility(View.VISIBLE);
                btn_clear_txt_20.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == editOutsideAroundFront) {
                btn_clear_txt_21.setVisibility(View.VISIBLE);
                btn_clear_txt_21.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == editFeatureAtTheScene) {
                btn_clear_txt_23.setVisibility(View.VISIBLE);
                btn_clear_txt_23.setOnClickListener(new DetailsOnClickListener());
            }
            if (mEditText == editDetailInside) {
                btn_clear_txt_22.setVisibility(View.VISIBLE);
                btn_clear_txt_22.setOnClickListener(new DetailsOnClickListener());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s == autoCompleteTypeOutside.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeName = autoCompleteTypeOutside.getText().toString();
                Log.i(TAG, "OutsideTypeName " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeName);
            } else if (s == edtFloorNum.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FloorNum = edtFloorNum.getText().toString();
                Log.i(TAG, "FloorNum " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FloorNum);
            } else if (s == edtCaveNum.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaveNum = edtCaveNum.getText().toString();
                Log.i(TAG, "CaveNum " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().CaveNum);
            } else if (s == editDetailOutside.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeDetail = editDetailOutside.getText().toString();
                Log.i(TAG, "OutsideTypeDetail " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().OutsideTypeDetail);
            } else if (s == editOutsideAroundBack.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().BackSide = editOutsideAroundBack.getText().toString();
                Log.i(TAG, "BackSide " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().BackSide);
            } else if (s == editOutsideAroundLeft.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().LeftSide = editOutsideAroundLeft.getText().toString();
                Log.i(TAG, "LeftSide " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().getLeftSide());
            } else if (s == editOutsideAroundRight.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().RightSide = editOutsideAroundRight.getText().toString();
                Log.i(TAG, "RightSide " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().RightSide);
            } else if (s == editOutsideAroundFront.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FrontSide = editOutsideAroundFront.getText().toString();
                Log.i(TAG, "FrontSide " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().FrontSide);
            } else if (s == editFeatureAtTheScene.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().SceneZone = editFeatureAtTheScene.getText().toString();
                Log.i(TAG, "SceneZone " + CSIDataTabFragment.apiCaseScene.getTbSceneFeatureOutside().SceneZone);
            } else if (s == editDetailInside.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().FeatureInsideDetail = editDetailInside.getText().toString();
                Log.i(TAG, "FeatureInsideDetail " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().FeatureInsideDetail);
            }
        }

    }
}
