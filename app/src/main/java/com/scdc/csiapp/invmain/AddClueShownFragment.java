package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.scdc.csiapp.tablemodel.TbClueShown;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.scdc.csiapp.tablemodel.TbPhotoOfResultscene;
import com.scdc.csiapp.tablemodel.TbResultScene;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.scdc.csiapp.invmain.ResultTabFragment.strSDCardPathName;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddClueShownFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddClueShownFragment";
    private GridView horizontal_gridView_ClueShown, horizontal_gridView_ClueShown_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    DBHelper dbHelper;
    ResultTabFragment resultTabFragment;
    String sRSID, mode, typeid;
    int position = 0;
    TbResultScene tbResultScene;
    TbClueShown tbClueShown;
    AutoCompleteTextView editClueShownPositionDetail;
    Button btn_clear_txt;
    List<ApiMultimedia> apiMultimediaList;
    List<TbMultimediaFile> tbPhotoList;
    ImageButton btnTakePhotoClueShown;
    String sPhotoID, timeStamp;
    public static final int REQUEST_CAMERA = 777;
    private String mCurrentPhotoPath;
    Uri uri;
    ConnectionDetector cd;
    private Context mContext;
    DisplayMetrics dm;
    Handler mHandler = new Handler();
    int INTERVAL = 1000 * 5; //20 second
    String defaultIP = "180.183.251.32/mcsi";

    public AddClueShownFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_clueshown, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มร่องรอยที่ปรากฏ");
        mContext = view.getContext();
        //call the main activity set tile method
        Bundle args = getArguments();
        sRSID = args.getString(ResultTabFragment.Bundle_ID);
        mode = args.getString(ResultTabFragment.Bundle_mode);
        typeid = args.getString(ResultTabFragment.Bundle_RSType);
        Log.i(TAG, "sRSID " + sRSID + " typeid " + typeid);
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Log.i(TAG, typeid + " tbClueShownsesList num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));
        cd = new ConnectionDetector(getActivity());
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        dbHelper = new DBHelper(getActivity());
        tbResultScene = new TbResultScene();
        tbClueShown = new TbClueShown();
        resultTabFragment = new ResultTabFragment();
        getDateTime = new GetDateTime();

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);
        btnTakePhotoClueShown = (ImageButton) view.findViewById(R.id.btnTakePhotoClueShown);
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        editClueShownPositionDetail = (AutoCompleteTextView) view.findViewById(R.id.editClueShownPositionDetail);
        String[] mGateClueArray;
        mGateClueArray = getResources().getStringArray(
                R.array.gate_clue);
        ArrayAdapter<String> adapterGateClue = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mGateClueArray);
        editClueShownPositionDetail.setThreshold(1);
        editClueShownPositionDetail.setAdapter(adapterGateClue);
        btn_clear_txt = (Button) view.findViewById(R.id.btn_clear_txt);
        editClueShownPositionDetail.addTextChangedListener(new RSTextWatcher(editClueShownPositionDetail));
        if (mode == "edit") {
            position = args.getInt(ResultTabFragment.Bundle_Index, -1);
            Log.i(TAG, "position " + position);
            tbClueShown = (TbClueShown) args.getSerializable(ResultTabFragment.Bundle_TB);
            editClueShownPositionDetail.setText(tbClueShown.getRSDetail());
        }
        horizontal_gridView_ClueShown = (GridView) view.findViewById(R.id.horizontal_gridView_ClueShown);
        horizontal_gridView_ClueShown_video = (GridView) view.findViewById(R.id.horizontal_gridView_ClueShown_video);
        txtPhoto = (TextView) view.findViewById(R.id.txtPhoto);
        txtVideo = (TextView) view.findViewById(R.id.txtVideo);
        txtVideo.setVisibility(View.GONE);
        showAllPhoto();
        btnTakePhotoClueShown.setOnClickListener(new InsideOnClickListener());
        fabBtnDetails.setOnClickListener(new InsideOnClickListener());

        return view;
    }

    private class InsideOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabBtnDetails) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                tbClueShown.RSID = sRSID;
                tbClueShown.RSTypeID = typeid;
                tbClueShown.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                ResultTabFragment.tbClueShowns.add(tbClueShown);
                CSIDataTabFragment.apiCaseScene.setTbClueShowns(ResultTabFragment.tbClueShowns);
                Log.i(TAG, typeid + " tbClueShowns num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if (mode == "edit") {
                        CSIDataTabFragment.apiCaseScene.getTbClueShowns().remove(position);
                        Log.i(TAG, typeid + " tbClueShowns remove num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));

                    } else {
                        Log.i(TAG, typeid + " tbClueShowns num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));

                    }
                    getActivity().onBackPressed();
                }

            }
            if (v == btn_clear_txt) {
                editClueShownPositionDetail.setText("");
            }
            if (v == btnTakePhotoClueShown) {
                File newfile;
                ResultTabFragment.createFolder();
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
            if (mEditText == editClueShownPositionDetail) {
                btn_clear_txt.setVisibility(View.VISIBLE);
                btn_clear_txt.setOnClickListener(new InsideOnClickListener());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == editClueShownPositionDetail.getEditableText()) {
                tbClueShown.RSDetail = editClueShownPositionDetail.getText().toString();
                Log.i(TAG, "editClueShownPositionDetail " + tbClueShown.RSDetail);
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

                    TbPhotoOfResultscene tbPhotoOfResultscene = new TbPhotoOfResultscene();
                    tbPhotoOfResultscene.RSID = sRSID;
                    tbPhotoOfResultscene.FileID = sPhotoID;
                    apiMultimedia.setTbPhotoOfResultscene(tbPhotoOfResultscene);

                    apiMultimediaList.add(apiMultimedia);
//                    CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);

                    CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
                    Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        Log.i(TAG, "PHOTO saved to Gallery! : " + sPhotoID + ".jpg");

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
//            Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            for (int i = 0; i < apiMultimediaList.size(); i++) {
                tbPhotoList.add(apiMultimediaList.get(i).getTbMultimediaFile());
            }
        }
        int photolength = 0;

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

            horizontal_gridView_ClueShown.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_ClueShown.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_ClueShown.setNumColumns(photolength);

            horizontal_gridView_ClueShown.setVisibility(View.VISIBLE);
            horizontal_gridView_ClueShown.setAdapter(new PhotoAdapter(getActivity()));
            registerForContextMenu(horizontal_gridView_ClueShown);
            // OnClick
            horizontal_gridView_ClueShown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", (Serializable) tbPhotoList);
                    bundle.putInt("position", position);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }
            });
        } else {
            horizontal_gridView_ClueShown.setVisibility(View.GONE);
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

