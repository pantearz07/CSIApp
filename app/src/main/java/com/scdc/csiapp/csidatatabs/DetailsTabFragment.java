package com.scdc.csiapp.csidatatabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import  com.scdc.csiapp.connecting.ConnectionDetector;
import  com.scdc.csiapp.connecting.PreferenceData;
import  com.scdc.csiapp.connecting.SQLiteDBHelper;
import  com.scdc.csiapp.main.ActivityResultBus;
import  com.scdc.csiapp.main.ActivityResultEvent;
import  com.scdc.csiapp.main.GetDateTime;
import  com.scdc.csiapp.R;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pantearz07 on 22/9/2558.
 */
public class DetailsTabFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String[] updateDT,datetime;
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
            sFrontSide, sLeftSide, sRightSide, sBackSide, sSceneZone,sFeatureInsideDetail, sHaveFence, sHaveMezzanine, sHaveRoofTop;
    Boolean sHaveFenceBoolean, sHaveMezzanineBoolean,
            sHaveRoofTopBoolean;
    Button btnAddFeatureInside;
            ImageButton btn_camera;
    ListView listViewAddFeatureInside;
    //View linearLayoutAddFeatureInside;
    private ArrayList<HashMap<String, String>> featureInsideList;

    protected static final int DIALOG_AddFeatureInside = 0;
    ViewGroup viewByIdaddinside;
    TextView edtUpdateDateTime;
    public static final int REQUEST_CAMERA_OUTSIDE = 333;
    private String mCurrentPhotoPath;
    Uri uri;
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
    String sPhotoID;
    String arrDataPhoto[][],arrDataPhoto2[][],arrDataVideo[][];

    GridView horizontal_gridView_Inside_photo,horizontal_gridView_Inside_video;
    TextView txtPhoto,txtVideo;
    private View mViewAddFeatureInside;
    ImageButton btnShowHide1;
    private boolean viewGroupIsVisible = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewDetails = inflater.inflate(R.layout.details_tab_layout, null);
        mContext = viewDetails.getContext();
        rootLayout = (CoordinatorLayout) viewDetails.findViewById(R.id.rootLayout);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.networkConnectivity();
        isConnectingToInternet = cd.isConnectingToInternet();
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
// ลักษณะสถานที่เกิดเหตุ
        // ภายนอก
        autoCompleteTypeOutside = (AutoCompleteTextView) viewDetails
                .findViewById(R.id.autoCompleteTypeOutside);
        String[] mOutsideTypeArray = getResources().getStringArray(
                R.array.type_outside);
        ArrayAdapter<String> adapterOutsideType = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_dropdown_item_1line,
                mOutsideTypeArray);
        autoCompleteTypeOutside.setThreshold(1);
        autoCompleteTypeOutside.setAdapter(adapterOutsideType);

        autoCompleteTypeOutside.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_14.setVisibility(View.VISIBLE);
                btn_clear_txt_14.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // มีชั้นเเบบ
        checkBoxHaveFence = (CheckBox) viewDetails
                .findViewById(R.id.checkBoxHaveFence);
        checkBoxHaveMezzanine = (CheckBox) viewDetails
                .findViewById(R.id.checkBoxHaveMezzanine);
        checkBoxHaveRoofTop = (CheckBox) viewDetails
                .findViewById(R.id.checkBoxHaveRoofTop);


        // จำนวนชั้น
        edtFloorNum = (EditText) viewDetails.findViewById(R.id.edtFloorNum);
        edtFloorNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_15.setVisibility(View.VISIBLE);
                btn_clear_txt_15.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // จำนวนคูหา
        edtCaveNum = (EditText) viewDetails.findViewById(R.id.edtCaveNum);
        edtCaveNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_16.setVisibility(View.VISIBLE);
                btn_clear_txt_16.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editDetailOutside = (EditText) viewDetails
                .findViewById(R.id.editDetailOutside);

        editDetailOutside.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_17.setVisibility(View.VISIBLE);
                btn_clear_txt_17.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editOutsideAroundBack = (EditText) viewDetails
                .findViewById(R.id.editOutsideAroundBack);
        editOutsideAroundBack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_18.setVisibility(View.VISIBLE);
                btn_clear_txt_18.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editOutsideAroundLeft = (EditText) viewDetails
                .findViewById(R.id.editOutsideAroundLeft);
        editOutsideAroundLeft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_19.setVisibility(View.VISIBLE);
                btn_clear_txt_19.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editOutsideAroundRight = (EditText) viewDetails
                .findViewById(R.id.editOutsideAroundRight);
        editOutsideAroundRight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_20.setVisibility(View.VISIBLE);
                btn_clear_txt_20.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editOutsideAroundFront = (EditText) viewDetails
                .findViewById(R.id.editOutsideAroundFront);
        editOutsideAroundFront.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_21.setVisibility(View.VISIBLE);
                btn_clear_txt_21.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // ภายใน
        editDetailInside = (EditText) viewDetails
                .findViewById(R.id.editDetailInside);
        editDetailInside.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_22.setVisibility(View.VISIBLE);
                btn_clear_txt_22.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        horizontal_gridView_Outside= (GridView) viewDetails.findViewById(R.id.horizontal_gridView_Outside);
        showAllPhoto();
        btn_camera = (ImageButton) viewDetails.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String timeStamp = "";
                File newfile;
                createFolder("Pictures");

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 timeStamp = updateDT[0]+" "+updateDT[1];

                sPhotoID = "IMG_" + datetime[2]+""+datetime[1]+""+datetime[0]+"_"+ datetime[3]+""+datetime[4]+""+datetime[5];
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

                    String sPhotoDescription = "";
                    new saveDataMedia().execute(reportID, sPhotoID, sPhotoPath, sPhotoDescription, timeStamp, "photo");

                    Log.i("show", "PHOTO saved to Gallery!" + strSDCardPathName + "Pictures/" + " : " + sPhotoPath);
                }

            }
        });

        listViewAddFeatureInside = (ListView) viewDetails
                .findViewById(R.id.listViewAddFeatureInside);
        btnAddFeatureInside = (Button) viewDetails
                .findViewById(R.id.btnAddFeatureInside);

        listViewAddFeatureInside.setVisibility(View.GONE);
        listViewAddFeatureInside.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedFeatureInside(reportID);

        btnAddFeatureInside.setOnClickListener(new DetailsOnClickListener());


        // บริเวณที่เกิดเหตุ
        editFeatureAtTheScene = (EditText) viewDetails
                .findViewById(R.id.editFeatureAtTheScene);
        editFeatureAtTheScene.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_23.setVisibility(View.VISIBLE);
                btn_clear_txt_23.setOnClickListener(new DetailsOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        fabBtnDetails = (FloatingActionButton) viewDetails.findViewById(R.id.fabBtnDetails);
        fabBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 saveAllDetailsData();
            }
        });
        return viewDetails;

    }
    public static void createFolder(String pathType) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
                Log.i("mkdir", Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
            }else {
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

                    Log.i("REQUEST_Photo", "Photo save");
                    showAllPhoto();
                    //showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_Photo", "media recording cancelled." + sPhotoID);
                File photosfile = new File(mCurrentPhotoPath);
                if (photosfile.exists()) {
                    photosfile.delete();
                    long saveStatus = mDbHelper.DeleteMediaFile(reportID, sPhotoID);
                    if (saveStatus <= 0) {
                        Log.i("deletephoto", "Cannot delete!! ");

                    } else {
                        long saveStatus2 = mDbHelper.DeletePhotoOfOutside(reportID, sPhotoID);
                        if (saveStatus2 <= 0) {
                            Log.i("deletephoto", "Cannot delete!! ");

                        } else {
                            Log.i("deletephoto", "ok");
                            showAllPhoto();
                        }
                    }
                }
            } else {
                Log.i("REQUEST_Photo", "Failed to record media");
            }
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

                long saveStatus2 = mDbHelper.saveDataPhotoOfOutside(params[0], params[1]);
                if (saveStatus2 <= 0) {
                    arrData = "error";
                    Log.i("saveData " + params[5], "Error!! ");
                }else{
                    arrData = "save";
                    Log.i("saveData " + params[5], params[2]);
                }
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
    public void showAllPhoto() {
        // TODO Auto-generated method stub
        int photolength=0;
        arrDataPhoto = mDbHelper.SelectDataPhotoOfOutside(reportID,"photo");
        //Log.i("arrDataPhoto_Outside",arrDataPhoto[0][0]);
        if (arrDataPhoto != null) {
            Log.i("arrDataPhoto_Outside", String.valueOf(arrDataPhoto.length));
            photolength = arrDataPhoto.length;
            //int size=list.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 70 ;

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
            horizontal_gridView_Outside.setAdapter(new PhotoAdapter(getActivity(), arrDataPhoto));
            registerForContextMenu(horizontal_gridView_Outside);
            // OnClick
            horizontal_gridView_Outside.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    showViewPic(arrDataPhoto[position][3].toString());
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
    public class PhotoAdapter extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public PhotoAdapter(Context c, String[][] li) {
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
            String root = Environment.getExternalStorageDirectory().toString();

            String strPath = root + "/CSIFiles/Pictures/"
                    + lis[position][3].toString();

            Log.i("list photo", "/CSIFiles/Pictures/" + lis[position][3].toString());
           // "file:///android_asset/DvpvklR.png"
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);
            String imgPath = "file:///CSIFiles/Pictures/"
                    + lis[position][3].toString();
            //Picasso.with(getActivity()).load(f).into(imageView);
           /* Picasso.with(getContext())
                    .load("file:///1234.jpg")
                    // .resize(50, 50)
                    //.centerCrop()
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView);
 */
            Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
            if(bmpSelectedImage != null) {
                int width1 = bmpSelectedImage.getWidth();
                int height1 = bmpSelectedImage.getHeight();
                Log.i("size", width1 + " " + height1);
                int width = width1 / 13;
                int height = height1 / 13;
                Log.i("resize", width + " " + height);
                Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage,
                        width, height, true);
                imageView.setImageBitmap(resizedbitmap);
            }

            // imageView.setImageBitmap(bmpSelectedImage);

            return convertView;

        }
    }

    private boolean saveAllDetailsData() {
        sOutsideTypeName = "";
        sOutsideTypeDetail = "";
        sFloorNum = "";
        sCaveNum = "";
        int FloorNum = 0, CaveNum = 0;

        sFrontSide = "";
        sLeftSide = "";
        sRightSide = "";
        sBackSide = "";
        sSceneZone = "";
        if (autoCompleteTypeOutside.getText().toString().length() != 0) {
            sOutsideTypeName = autoCompleteTypeOutside.getText().toString();
        }else{
            sOutsideTypeName="";
        }

        if (editDetailOutside.getText().toString().length() != 0) {
            sOutsideTypeDetail = editDetailOutside.getText().toString();
        }else{
            sOutsideTypeDetail="";
        }
		if (edtFloorNum.getText().toString().length() != 0) {
			sFloorNum = edtFloorNum.getText().toString();
			FloorNum = Integer.parseInt(sFloorNum);
		}else{
            sFloorNum="0";
        }
        if (edtCaveNum.getText().toString().length() != 0) {
            sCaveNum = edtCaveNum.getText().toString();
            CaveNum = Integer.parseInt(sCaveNum);
        }else{
            sCaveNum="0";
        }

        Log.i("sFloorNum sCaveNum", sFloorNum+" "+sCaveNum);

        sFeatureInsideDetail = "";
        if (editDetailInside.getText().toString().length() != 0) {
            sFeatureInsideDetail = editDetailInside.getText().toString();
        }else{
            sFeatureInsideDetail="";
        }
        sHaveFenceBoolean = checkBoxHaveFence.isChecked();
        Log.i("HaveFence detailscase", String.valueOf(sHaveFenceBoolean));
        if (sHaveFenceBoolean != true) {
            sHaveFence = "0";
        } else {
            sHaveFence = "1";
        }
        sHaveMezzanineBoolean = checkBoxHaveMezzanine.isChecked();
        Log.i("Mezzanine detailscase", String.valueOf(sHaveMezzanineBoolean));
        if (sHaveMezzanineBoolean != true) {
            sHaveMezzanine = "0";
        } else {
            sHaveMezzanine = "1";
        }

        sHaveRoofTopBoolean = checkBoxHaveRoofTop.isChecked();
        if (sHaveRoofTopBoolean != true) {
            sHaveRoofTop = "0"; Log.i("RoofTop detailscase", String.valueOf(sHaveRoofTop));
        } else {
            sHaveRoofTop = "1"; Log.i("RoofTop detailscase", String.valueOf(sHaveRoofTop));
        }
        if (editOutsideAroundFront.getText().toString().length() != 0) {
            sFrontSide = editOutsideAroundFront.getText().toString();
        }else{
            sFrontSide="";
        }

        if (editOutsideAroundLeft.getText().toString().length() != 0) {
            sLeftSide = editOutsideAroundLeft.getText().toString();
        }else{
            sLeftSide="";
        }

        if (editOutsideAroundRight.getText().toString().length() != 0) {
            sRightSide = editOutsideAroundRight.getText().toString();
        }else{
            sRightSide="";
        }

        if (editOutsideAroundBack.getText().toString().length() != 0) {
            sBackSide = editOutsideAroundBack.getText().toString();
        }else{
            sBackSide="";
        }

        if (editFeatureAtTheScene.getText().toString().length() != 0) {
            sSceneZone = editFeatureAtTheScene.getText().toString();
        }else{
            sSceneZone="";
        }


        new saveDetailsData().execute(reportID,
                sOutsideTypeName, sOutsideTypeDetail, sFloorNum, sCaveNum,
                sHaveFence, sHaveMezzanine, sHaveRoofTop, sFrontSide,
                sLeftSide, sRightSide, sBackSide,sSceneZone);
        new saveDetailsData2().execute(reportID, sFeatureInsideDetail,updateDT[0],updateDT[1]);
return true;

    }
    class saveDetailsData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";

            long saveDataFeatureOutside = mDbHelper.saveDataFeatureOutside(params[0], params[1], params[2],
                    params[3], params[4], params[5], params[6], params[7],
                    params[8], params[9], params[10], params[11], params[12]);

            if (saveDataFeatureOutside <= 0) {
                Log.i("saveDataFeatureOutside", "error detailscase");
                arrData = "error";
            } else {
                Log.i("saveDataFeatureOutside", "save detailscase");
                arrData = "save";
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            String message="";
            if (arrData == "save") {
                message = "บันทึกข้อมูลเรียบร้อยแล้ว";

            } else {
                message = "เกิดข้อผิดพลาด";

            }
            Log.i("save  FeatureOutside","detailscase "+ message);
           Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

        }


    }
    class showData extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }


        @Override
        protected String[] doInBackground(String... params) {
            String[] arrData = {""};
            arrData = mDbHelper.SelectDataFeatureOutside(params[0]);


            return arrData;
        }

        protected void onPostExecute(String[] arrDataFeatureOutside) {


                if (arrDataFeatureOutside != null) {
                    Log.i("SelectFeatureOutside", "detailscase "+ arrDataFeatureOutside[1]);
                    if (arrDataFeatureOutside[1].length() != 0) {
                        autoCompleteTypeOutside.setText(arrDataFeatureOutside[1]);
                    }
                    if (arrDataFeatureOutside[2].length() != 0) {
                        editDetailOutside.setText(arrDataFeatureOutside[2]);
                    }

                    if (arrDataFeatureOutside[3].length() != 0) {

                        if(arrDataFeatureOutside[3].equals(0)){
                            edtFloorNum.setText("");
                        }else {
                            edtFloorNum.setText(arrDataFeatureOutside[3]);
                        }
                    }

                    if (arrDataFeatureOutside[4].length() != 0) {
                        if(arrDataFeatureOutside[4].equals(0)){
                            edtCaveNum.setText("");
                        }else {
                            edtCaveNum.setText(arrDataFeatureOutside[4]);
                        }
                    }
                    Log.i("arrDataFeatureOutside", "detailscase "+ arrDataFeatureOutside[0]+"/"+arrDataFeatureOutside[1]+"/"+arrDataFeatureOutside[5]+"/"
                            + arrDataFeatureOutside[6]+"/" + arrDataFeatureOutside[7]);
                    if (Integer.parseInt(arrDataFeatureOutside[5]) == 1) {

                        checkBoxHaveFence.setChecked(true);
                    }
                    if (Integer.parseInt(arrDataFeatureOutside[6]) == 1) {
                        checkBoxHaveMezzanine.setChecked(true);
                    }
                    if (Integer.parseInt(arrDataFeatureOutside[7]) == 1) {
                        checkBoxHaveRoofTop.setChecked(true);
                    }

                    if (arrDataFeatureOutside[8].length() != 0) {
                        editOutsideAroundFront.setText(arrDataFeatureOutside[8]);
                    }
                    if (arrDataFeatureOutside[9].length() != 0) {
                        editOutsideAroundLeft.setText(arrDataFeatureOutside[9]);
                    }

                    if (arrDataFeatureOutside[10].length() != 0) {
                        editOutsideAroundRight.setText(arrDataFeatureOutside[10]);
                    }
                    if (arrDataFeatureOutside[11].length() != 0) {
                        editOutsideAroundBack.setText(arrDataFeatureOutside[11]);
                    }
                    if (arrDataFeatureOutside[12].length() != 0) {
                        editFeatureAtTheScene.setText(arrDataFeatureOutside[12]);
                    }

                } else {
                    Log.i("Recieve FeatureOutside", "Null!! detailscase ");
                }
        }
    }
    class saveDetailsData2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";

            long updateCaseSceneDetails = mDbHelper.updateCaseSceneDetails(params[0], params[1], params[2],params[3]);

            if (updateCaseSceneDetails <= 0) {
                Log.i("updateCaseSceneDetails2", "error detailscase");
                arrData = "error";
            } else {
                Log.i("updateCaseSceneDetails2", "save detailscase" );
                arrData = "save";
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            String message="";
            if (arrData == "save") {
                message = "บันทึกข้อมูลเรียบร้อยแล้ว";

            } else {
                message = "เกิดข้อผิดพลาด";

            }
            Log.i("updateCaseSceneDetails2", "detailscase "+ message);
           //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

        }


    }
    class showData2 extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }


        @Override
        protected String[] doInBackground(String... params) {
            String[] arrData = {""};
            arrData = mDbHelper.SelectDataCaseScene(params[0]);


            return arrData;
        }

        protected void onPostExecute(String[] arrData) {
            if (arrData != null) {

                if (arrData[7]!=null) {
                    edtUpdateDateTime.setText("อัพเดทข้อมูลล่าสุดเมื่อวันที่ " + getDateTime.changeDateFormatToCalendar(arrData[7]) + " เวลา " + arrData[8]);
                   /* Toast.makeText(getActivity()
                                    .getApplicationContext(),
                            "อัพเดทข้อมูลล่าสุดเมื่อวันที่ " + arrData[7] + " เวลา " + arrData[8],
                            Toast.LENGTH_LONG).show();*/
                    Log.i("update data when", "อัพเดทข้อมูลล่าสุดเมื่อวันที่ " + getDateTime.changeDateFormatToCalendar(arrData[7]) + " เวลา " + arrData[8]);
                }
                if (arrData[2]!=null) {
                    editDetailInside.setText(arrData[2]);
                }

            }
        }
    }
    public class DetailsOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(v == btnAddFeatureInside){
                Log.i("Investigator1", "showlist");
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                String sFeatureInsideID = "IN_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0]+"_"+CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];

                Intent showActivity = new Intent(getActivity(), FeatureInsideActivity.class);
                showActivity.putExtra("featureid", sFeatureInsideID);
                showActivity.putExtra("type", "new");
                startActivity(showActivity);
/*
                viewByIdaddinside = (ViewGroup) v.findViewById(R.id.layout_addinside_dialog);

                createdDialog(DIALOG_AddFeatureInside).show();*/
            }
            if (v == btn_clear_txt_14) {
                autoCompleteTypeOutside.setText("");
            }

            if(v==btn_clear_txt_15){
                edtFloorNum.setText("");
            }
            if(v==btn_clear_txt_16){
                edtCaveNum.setText("");
            }

            if(v==btn_clear_txt_17){
                editDetailOutside.setText("");
            }
            if(v==btn_clear_txt_18){
                editOutsideAroundBack.setText("");
            }
            if(v==btn_clear_txt_19){
                editOutsideAroundLeft.setText("");
            }
            if(v==btn_clear_txt_20){
                editOutsideAroundRight.setText("");
            }
            if(v==btn_clear_txt_21){
                editOutsideAroundFront.setText("");
            }
            if(v==btn_clear_txt_22){
                editDetailInside.setText("");
            }
            if(v==btn_clear_txt_23){
                editFeatureAtTheScene.setText("");
            }
            if(v == btnShowHide1){
                if (viewGroupIsVisible) {
                    mViewAddFeatureInside.setVisibility(View.VISIBLE);
                    btnShowHide1.setImageResource(R.drawable.ic_maxlayout);
                } else {
                    mViewAddFeatureInside.setVisibility(View.GONE);
                    btnShowHide1.setImageResource(R.drawable.ic_minlayout);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }

        }
    }
    protected Dialog createdDialog(int id){
        Dialog dialog = null;
        final AlertDialog.Builder otherInsideDialog,FeatureInsideDialog;


        switch (id) {
            case DIALOG_AddFeatureInside:
                Log.i("DIALOG_AddFeatureInside", "AddFeatureInside");
                otherInsideDialog = new AlertDialog.Builder(getActivity());
                FeatureInsideDialog = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflaterDialogFeatureInside = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View Viewlayout = inflaterDialogFeatureInside.inflate(
                        R.layout.add_inside_dialog,viewByIdaddinside);
                otherInsideDialog.setIcon(android.R.drawable.btn_star_big_on);
                otherInsideDialog.setTitle("เพิ่มลักษณะภายใน");
                otherInsideDialog.setView(Viewlayout);

                final EditText editFeatureInsideFloor = (EditText) Viewlayout
                        .findViewById(R.id.editFeatureInsideFloor);
                final TextView editNoti1 = (TextView) Viewlayout
                        .findViewById(R.id.editNoti1);
                editFeatureInsideFloor
                        .addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s,
                                                      int start, int before, int count) {
                                // TODO Auto-generated method stub
                                editNoti1.setVisibility(View.GONE);

                            }

                            @Override
                            public void beforeTextChanged(CharSequence s,
                                                          int start, int count, int after) {
                                // TODO Auto-generated method stub
                                editNoti1.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // TODO Auto-generated method stub
                                if (s.toString().length() != 0) {
                                    editNoti1.setVisibility(View.GONE);
                                    // buttonPOS.setEnabled(true);
                                } else {
                                    editNoti1.setVisibility(View.VISIBLE);
                                    // buttonPOS.setEnabled(false);
                                }
                            }
                        });
                final EditText editFeatureInsideCave = (EditText) Viewlayout
                        .findViewById(R.id.editFeatureInsideCave);
                final TextView editNoti2 = (TextView) Viewlayout
                        .findViewById(R.id.editNoti2);
                editFeatureInsideCave.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        // TODO Auto-generated method stub
                        editNoti2.setVisibility(View.GONE);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        // TODO Auto-generated method stub
                        editNoti2.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        if (s.toString().length() != 0) {
                            editNoti2.setVisibility(View.GONE);

                        } else {
                            editNoti2.setVisibility(View.VISIBLE);
                        }
                    }
                });
                final EditText editFeatureInsideClassBack = (EditText) Viewlayout
                        .findViewById(R.id.editFeatureInsideClassBack);
                final EditText editFeatureInsideClassLeft = (EditText) Viewlayout
                        .findViewById(R.id.editFeatureInsideClassLeft);
                final EditText editFeatureInsideClassCenter = (EditText) Viewlayout
                        .findViewById(R.id.editFeatureInsideClassCenter);
                final EditText editFeatureInsideClassRight = (EditText) Viewlayout
                        .findViewById(R.id.editFeatureInsideClassRight);
                final EditText editFeatureInsideClassFront = (EditText) Viewlayout
                        .findViewById(R.id.editFeatureInsideClassFront);

                // Button OK

                otherInsideDialog.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {

                                if (editFeatureInsideFloor.getText().toString()
                                        .equals("")
                                        || editFeatureInsideCave.getText()
                                        .toString().equals("")) {
                                    FeatureInsideDialog
                                            .setIcon(android.R.drawable.btn_star_big_on);
                                    FeatureInsideDialog
                                            .setTitle("กรุณากรอกข้อมูลชั้นและคูหา!");
                                    FeatureInsideDialog.setPositiveButton("OK",
                                            null);
                                    FeatureInsideDialog.show();

                                } else {

									/*	*/
                                    String sFloorNo, sCaveNo, sFrontInside, sLeftInside, sRightInside, sBackInside, sCenterInside;

                                    sFloorNo = "";
                                    sCaveNo = "";
                                    sFrontInside = "";
                                    sLeftInside = "";
                                    sRightInside = "";
                                    sBackInside = "";
                                    sCenterInside = "";
                                    if (editFeatureInsideFloor.getText()
                                            .toString().length() != 0) {
                                        sFloorNo = editFeatureInsideFloor
                                                .getText().toString();
                                    }

                                    if (editFeatureInsideCave.getText()
                                            .toString().length() != 0) {
                                        sCaveNo = editFeatureInsideCave
                                                .getText().toString();
                                    }
                                    if (editFeatureInsideClassBack.getText()
                                            .toString().length() != 0) {
                                        sBackInside = editFeatureInsideClassBack
                                                .getText().toString();
                                    }
                                    if (editFeatureInsideClassLeft.getText()
                                            .toString().length() != 0) {
                                        sLeftInside = editFeatureInsideClassLeft
                                                .getText().toString();
                                    }
                                    if (editFeatureInsideClassCenter.getText()
                                            .toString().length() != 0) {
                                        sCenterInside = editFeatureInsideClassCenter
                                                .getText().toString();
                                    }
                                    if (editFeatureInsideClassRight.getText()
                                            .toString().length() != 0) {
                                        sRightInside = editFeatureInsideClassRight
                                                .getText().toString();
                                    }
                                    if (editFeatureInsideClassFront.getText()
                                            .toString().length() != 0) {
                                        sFrontInside = editFeatureInsideClassFront
                                                .getText().toString();
                                    }
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    Toast.makeText(context,
                                            "เพิ่มข้อมูลเรียบร้อยเเล้ว",
                                            Toast.LENGTH_LONG).show();
                                    saveDataAddFeatureInside(reportID,
                                            sFloorNo, sCaveNo, sFrontInside,
                                            sLeftInside, sRightInside,
                                            sBackInside, sCenterInside);
                                    dialog.dismiss();
                                }
                            }

                        })

                        // Button Cancel
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                dialog = otherInsideDialog.create();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }
    public void saveDataAddFeatureInside(String reportID, String sFloorNo,
                                         String sCaveNo, String sFrontInside, String sLeftInside,
                                         String sRightInside, String sBackInside, String sCenterInside) {
        // TODO Auto-generated method stub
        String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
        String sFeatureInsideID = "IN_" + CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
        int floorno = 0, caveno = 0;
        if (sFloorNo.length() != 0) {
            floorno = Integer.parseInt(sFloorNo);
        }
        if (sCaveNo.length() != 0) {

            caveno = Integer.parseInt(sCaveNo);
        }
        long saveStatus = mDbHelper.saveDataFeatureInside(reportID,
                sFeatureInsideID, floorno, caveno, sFrontInside, sLeftInside,
                sRightInside, sBackInside, sCenterInside);
        if (saveStatus <= 0) {
            Log.i("saveDataFeatureInside", "Error!! ");
        } else {
            Log.i("saveDataFeatureInside", "OK!! ");

            ShowSelectedFeatureInside(reportID);
        }
        Log.i("saveData FeatureInside", sFeatureInsideID + " " + sFrontInside
                + " " + sLeftInside + " " + sRightInside+ " " + sBackInside+ " " + sCenterInside);
    }

    public void ShowSelectedFeatureInside(String reportID) {
        // TODO Auto-generated method stub
        featureInsideList = mDbHelper.SelectAllFeatureInside(reportID);


        if (featureInsideList != null) {
            listViewAddFeatureInside.setAdapter(new FeatureInsideAdapter(
                getActivity()));
            Log.i("featureInsideList",String.valueOf(featureInsideList.size()));
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
            return featureInsideList.size();
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
            final String sFeatureInsideID = featureInsideList.get(position)
                    .get("FeatureInsideID");
            final TextView showFeatureInsideFloor = (TextView) convertView
                    .findViewById(R.id.showFeatureInsideFloor);
            final TextView showFeatureInsideCave = (TextView) convertView
                    .findViewById(R.id.showFeatureInsideCave);
            showFeatureInsideFloor.setText(featureInsideList.get(position).get(
                    "FloorNo"));
            showFeatureInsideCave.setText(featureInsideList.get(position).get(
                    "CaveNo"));
            final TextView editFeatureInsideClassBack = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassBack2);
            editFeatureInsideClassBack.setText(featureInsideList.get(position)
                    .get("BackInside"));

            final TextView editFeatureInsideClassLeft = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassLeft2);
            editFeatureInsideClassLeft.setText(featureInsideList.get(position)
                    .get("LeftInside"));

            final TextView editFeatureInsideClassCenter = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassCenter2);
            editFeatureInsideClassCenter.setText(featureInsideList
                    .get(position).get("CenterInside"));

            final TextView editFeatureInsideClassRight = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassRight2);
            editFeatureInsideClassRight.setText(featureInsideList.get(position)
                    .get("RightInside"));
            final TextView editFeatureInsideClassFront = (TextView) convertView
                    .findViewById(R.id.txtFeatureInsideClassFront2);
            editFeatureInsideClassFront.setText(featureInsideList.get(position).get(
                    "FrontInside"));
            /*final EditText showFeatureInsideFloor = (EditText) convertView
                    .findViewById(R.id.showFeatureInsideFloor);
            showFeatureInsideFloor.setText(featureInsideList.get(position).get(
                    "FloorNo"));
            Log.i("FloorNo", featureInsideList.get(position).get(
                    "FloorNo"));

            final EditText showFeatureInsideCave = (EditText) convertView
                    .findViewById(R.id.showFeatureInsideCave);
            showFeatureInsideCave.setText(featureInsideList.get(position).get(
                    "CaveNo"));
            Log.i("CaveNo", featureInsideList.get(position).get(
                    "CaveNo"));

            final EditText editFeatureInsideClassBack = (EditText) convertView
                    .findViewById(R.id.editFeatureInsideClassBack);
            editFeatureInsideClassBack.setText(featureInsideList.get(position)
                    .get("BackInside"));

            final EditText editFeatureInsideClassLeft = (EditText) convertView
                    .findViewById(R.id.editFeatureInsideClassLeft);
            editFeatureInsideClassLeft.setText(featureInsideList.get(position)
                    .get("LeftInside"));

            final EditText editFeatureInsideClassCenter = (EditText) convertView
                    .findViewById(R.id.editFeatureInsideClassCenter);
            editFeatureInsideClassCenter.setText(featureInsideList
                    .get(position).get("CenterInside"));

            final EditText editFeatureInsideClassRight = (EditText) convertView
                    .findViewById(R.id.editFeatureInsideClassRight);
            editFeatureInsideClassRight.setText(featureInsideList.get(position)
                    .get("RightInside"));
             final EditText editFeatureInsideClassFront = (EditText) convertView
                    .findViewById(R.id.editFeatureInsideClassFront);
            editFeatureInsideClassFront.setText(featureInsideList.get(position).get(
                    "FrontInside"));
            Log.i("FrontInside", featureInsideList.get(position).get(
                            "FrontInside"));*/

            //horizontal_gridView_Inside_photo= (GridView)convertView.findViewById(R.id.horizontal_gridView_Inside_photo);
            //horizontal_gridView_Inside_video= (GridView)convertView.findViewById(R.id.horizontal_gridView_Inside_video);
            txtPhoto = (TextView)convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView)convertView.findViewById(R.id.txtVideo);

            arrDataPhoto2 = mDbHelper.SelectDataPhotoOfInside(reportID,sFeatureInsideID, "photo");

            if (arrDataPhoto2 != null) {
                Log.i("arrDataPhoto_Inside", sFeatureInsideID + " " + String.valueOf(arrDataPhoto2.length));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(arrDataPhoto2.length) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i("Recieve_inside", sFeatureInsideID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent showActivity = new Intent(getActivity(), FeatureInsideActivity.class);
                    showActivity.putExtra("featureid", sFeatureInsideID);
                    showActivity.putExtra("type", "update");
                    startActivity(showActivity);
                }
            });
            /*
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adbEdit.setTitle("แก้ไขข้อมูล");
                    adbEdit.setMessage("ยืนยันการแก้ไขข้อมูล [ ลักษณะภายในชั้นที่ "
                            + featureInsideList.get(position).get("FloorNo")
                            + "]");
                    adbEdit.setNegativeButton("Cancel", null);
                    adbEdit.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long saveStatus = mDbHelper.updateDataSelectedFeatureInside(
                                            sFeatureInsideID,
                                            Integer.parseInt(showFeatureInsideFloor
                                                    .getText().toString()),
                                            Integer.parseInt(showFeatureInsideCave
                                                    .getText().toString()),
                                            editFeatureInsideClassBack
                                                    .getText().toString(),
                                            editFeatureInsideClassLeft
                                                    .getText().toString(),
                                            editFeatureInsideClassCenter
                                                    .getText().toString(),
                                            editFeatureInsideClassRight
                                                    .getText().toString(),
                                            editFeatureInsideClassFront
                                                    .getText().toString());
                                    if (saveStatus <= 0) {
                                        Log.i("update FeatureInside",
                                                "Error!! ");
                                    } else {
                                        Log.i("update FeatureInside",
                                                "ok!! ");
                                        Toast.makeText(getActivity(),
                                                "แก้ไขเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                            });
                    adbEdit.show();
                }
            });*/
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

                                    long flg = mDbHelper
                                            .DeleteSelectedFeatureInside(sFeatureInsideID);
                                    if (flg > 0) {

                                            long saveStatus2 = mDbHelper.DeletePhotoOfAllInside(sFeatureInsideID);
                                            if (saveStatus2 <= 0) {
                                                Log.i("DeletePhotoOf inside", "Cannot delete!! ");

                                            } else {
                                                Toast.makeText(getActivity(),
                                                        "ลบข้อมูลเรียบรอยแล้ว",
                                                        Toast.LENGTH_LONG).show();
                                                ShowSelectedFeatureInside(reportID);
                                            }


                                    } else {
                                        Log.i("Delete FeatureInside",
                                                "Delete Data Failed.");
                                    }

                                }

                            });
                    adb.show();
                }
            });

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
            Log.i("inside",String.valueOf(numberOfItems));
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
            int totalHeight = totalItemsHeight + totalDividersHeight ;
             // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            //params.height = (int) (totalItemsHeight-(totalItemsHeight/1.5));
            params.height =totalHeight;
            Log.i("inside totalHeight",String.valueOf(totalHeight));
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
        Log.i("Check", "onStart detailscase "+reportID);

        new showData().execute(reportID);
        new showData2().execute(reportID);
        ShowSelectedFeatureInside(reportID);
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStop", "onStop detailscase");
        saveAllDetailsData();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "onResume detailscase");
      //  saveAllDetailsData();
        new showData().execute(reportID);
        new showData2().execute(reportID);
        ShowSelectedFeatureInside(reportID);
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
        Log.i("onPause", "onPause detailscase");
        saveAllDetailsData();
    }
/*
    public void showAllPhoto2(String sFeatureInsideID) {
        // TODO Auto-generated method stub
        int photolength=0;
        arrDataPhoto2 = mDbHelper.SelectDataPhotoOfInside(reportID,sFeatureInsideID, "photo");

        if (arrDataPhoto2 != null) {
            Log.i("arrDataPhoto_Inside", sFeatureInsideID+" "+String.valueOf(arrDataPhoto2.length));
            txtPhoto.setText("รูปภาพ  (" + String.valueOf(arrDataPhoto2.length)+")");
            photolength = arrDataPhoto2.length;
            //int size=list.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 70 ;

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;

            int totalWidth = (int) (width * photolength * density);
            int singleItemWidth = (int) (width * density);
            Log.i("inside gridlayout", String.valueOf(totalWidth)+" "+String.valueOf(singleItemWidth));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    totalWidth, singleItemWidth);

            horizontal_gridView_Inside_photo.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_Inside_photo.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_Inside_photo.setNumColumns(photolength);

            horizontal_gridView_Inside_photo.setVisibility(View.VISIBLE);
            horizontal_gridView_Inside_photo.setAdapter(new PhotoAdapter2(getActivity(), arrDataPhoto2));
            registerForContextMenu(horizontal_gridView_Inside_photo);
            // OnClick
            horizontal_gridView_Inside_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Log.i("showViewPic2_inside",arrDataPhoto2[position][3].toString());
                    showViewPic2(arrDataPhoto2[position][3].toString());
                }
            });
        } else {
            horizontal_gridView_Inside_photo.setVisibility(View.GONE);
            txtPhoto.setText("รูปภาพ (0)");

            Log.i("Recieve_inside", sFeatureInsideID+" Null!! ");

        }
    }
    public void showViewPic2(String sPicPath) {
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
    public class PhotoAdapter2 extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public PhotoAdapter2(Context c, String[][] li) {
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
            String root = Environment.getExternalStorageDirectory().toString();

            String strPath = root + "/CSIFiles/Pictures/"
                    + lis[position][3].toString();

            Log.i("list photo", "/CSIFiles/Pictures/" + lis[position][3].toString());

            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

            textView.setText(lis[position][3].toString() + "\n"
                    + lis[position][4].toString());
            Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);

            int width1 = bmpSelectedImage.getWidth();
            int height1 = bmpSelectedImage.getHeight();
            Log.i("size", width1 + " " + height1);
            int width = width1 / 13;
            int height = height1 / 13;
            Log.i("resize", width + " " + height);
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage,
                    width, height, true);
            imageView.setImageBitmap(resizedbitmap);


            // imageView.setImageBitmap(bmpSelectedImage);

            return convertView;

        }
    }

*/
}
