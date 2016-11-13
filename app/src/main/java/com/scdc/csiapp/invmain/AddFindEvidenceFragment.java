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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.ActivityResultBus;
import com.scdc.csiapp.main.ActivityResultEvent;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbFindEvidence;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.scdc.csiapp.tablemodel.TbPhotoOfEvidence;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.scdc.csiapp.invmain.ResultTabFragment.createFolder;
import static com.scdc.csiapp.invmain.ResultTabFragment.strSDCardPathName;

/**
 * Created by Pantearz07 on 6/10/2559.
 */

public class AddFindEvidenceFragment extends Fragment {
    FloatingActionButton fabBtnDetails;
    private CoordinatorLayout rootLayout;
    private static final String TAG = "DEBUG-AddFindEvidenceFragment";
    EditText editEvidenceNumber, editFindEvidenceZone, editMarking, editParceling,
            editEvidencePerformed;
    Spinner spnEvidenceType;
    private GridView horizontal_gridView_EV_photo, horizontal_gridView_EV_video;
    private TextView txtPhoto, txtVideo;
    GetDateTime getDateTime;
    TbFindEvidence tbFindEvidence;
    ResultTabFragment resultTabFragment;
    CSIDataTabFragment csiDataTabFragment;
    String sEVID, mode, sSceneInvestID;
    //    String[] type_evidence;
    String[][] evidenceTypeArray;
    //    String[] evidenceTypeArray2;
    String evidenceTypeID;
    DBHelper dbHelper;
    boolean oldEvidenceType = false;
    int position = 0;
    Uri uri;
    String sPhotoID, timeStamp;
    ImageButton btnTakePhotoEV;
    List<ApiMultimedia> apiMultimediaList;
    public static final int REQUEST_CAMERA = 777;
    private String mCurrentPhotoPath;
    Context mContext;
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;
    List<TbMultimediaFile> tbPhotoList;
    Handler mHandler = new Handler();
    int INTERVAL = 1000 * 5; //20 second
    DisplayMetrics dm;
    public AddFindEvidenceFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_evidences, container, false);
        //call the main activity set tile method
        dbHelper = new DBHelper(getActivity());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มวัตถุพยาน");
        Log.i(TAG, "CaseReportID " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
        Bundle args = getArguments();
        sEVID = args.getString(ResultTabFragment.Bundle_ID);
        mode = args.getString(ResultTabFragment.Bundle_mode);
        cd = new ConnectionDetector(getActivity());
        mContext = view.getContext();
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);

        Log.i(TAG, "sEVID " + sEVID);
        tbFindEvidence = new TbFindEvidence();
        resultTabFragment = new ResultTabFragment();
        csiDataTabFragment = new CSIDataTabFragment();
        getDateTime = new GetDateTime();
        Log.i(TAG, "tbFindEvidence num1:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));

        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtnDetails = (FloatingActionButton) view.findViewById(R.id.fabBtnDetails);

        btnTakePhotoEV = (ImageButton) view.findViewById(R.id.btnTakePhotoEV);
        spnEvidenceType = (Spinner) view.findViewById(R.id.spinnerEvidenceType);
        evidenceTypeArray = dbHelper.SelectAllEvidenceType();
        if (evidenceTypeArray != null) {
            final String[] evidenceTypeArray2 = new String[evidenceTypeArray.length];
            for (int i = 0; i < evidenceTypeArray.length; i++) {
                evidenceTypeArray2[i] = evidenceTypeArray[i][2];
                Log.i(TAG + " show evidenceTypeArray2", evidenceTypeArray2[i].toString());
            }
            ArrayAdapter<String> adapterEvidenceType = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    evidenceTypeArray2);
            spnEvidenceType.setAdapter(adapterEvidenceType);
        } else {
            Log.i(TAG + " show evidenceTypeArray", "null");
        }
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        spnEvidenceType.setOnItemSelectedListener(new EvidenceOnItemSelectedListener());
        spnEvidenceType.setOnTouchListener(new EvidenceOnItemSelectedListener());

        editEvidenceNumber = (EditText) view.findViewById(R.id.editEvidenceNumber);
        editFindEvidenceZone = (EditText) view.findViewById(R.id.editFindEvidenceZone);
        editMarking = (EditText) view.findViewById(R.id.editMarking);
        editParceling = (EditText) view.findViewById(R.id.editParceling);
        editEvidencePerformed = (EditText) view.findViewById(R.id.editEvidencePerformed);
        editEvidenceNumber.addTextChangedListener(new InsideTextWatcher(editEvidenceNumber));
        editFindEvidenceZone.addTextChangedListener(new InsideTextWatcher(editFindEvidenceZone));
        editMarking.addTextChangedListener(new InsideTextWatcher(editMarking));
        editParceling.addTextChangedListener(new InsideTextWatcher(editParceling));
        editEvidencePerformed.addTextChangedListener(new InsideTextWatcher(editEvidencePerformed));
        if (mode == "new") {
            sSceneInvestID = args.getString(ResultTabFragment.Bundle_SceneInvestID);
            Log.i(TAG, "sSceneInvestID " + sSceneInvestID);
        }
        if (mode == "edit") {
            position = args.getInt(ResultTabFragment.Bundle_Index, -1);
            Log.i(TAG, "position " + position);
            tbFindEvidence = (TbFindEvidence) args.getSerializable(ResultTabFragment.Bundle_TB);
            sSceneInvestID = tbFindEvidence.getSceneInvestID();
            Log.i(TAG, "sSceneInvestID " + sSceneInvestID);
            editEvidenceNumber.setText(tbFindEvidence.getEvidenceNumber());
            editFindEvidenceZone.setText(tbFindEvidence.getFindEvidenceZone());
            editMarking.setText(tbFindEvidence.getMarking());
            editParceling.setText(tbFindEvidence.getParceling());

            if (tbFindEvidence.getEvidenceTypeID() != null) {
                evidenceTypeID = tbFindEvidence.EvidenceTypeID;
                Log.i(TAG, "getEvidenceTypeID" + tbFindEvidence.getEvidenceTypeID());
                for (int i = 0; i < evidenceTypeArray.length; i++) {
                    if (String.valueOf(evidenceTypeArray[i][0]).equals(tbFindEvidence.EvidenceTypeID)) {
                        spnEvidenceType.setSelection(i);
                        oldEvidenceType = true;
                        break;
                    }
                }
            }
        }
        horizontal_gridView_EV_photo = (GridView) view.findViewById(R.id.horizontal_gridView_EV);
        horizontal_gridView_EV_video = (GridView) view.findViewById(R.id.horizontal_gridView_EV_video);
        txtPhoto = (TextView) view.findViewById(R.id.txtPhoto);
        txtVideo = (TextView) view.findViewById(R.id.txtVideo);
        txtVideo.setVisibility(View.GONE);
        showAllPhoto();
        fabBtnDetails.setOnClickListener(new InsideOnClickListener());
        btnTakePhotoEV.setOnClickListener(new InsideOnClickListener());
        return view;
    }

    private class InsideOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabBtnDetails) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();


                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                tbFindEvidence.FindEvidenceID = sEVID;
                tbFindEvidence.SceneInvestID = sSceneInvestID;
                tbFindEvidence.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                ResultTabFragment.tbFindEvidences.add(tbFindEvidence);
                CSIDataTabFragment.apiCaseScene.setTbFindEvidences(ResultTabFragment.tbFindEvidences);
                Log.i(TAG, "tbFindEvidences num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));
                Log.i(TAG, "getEvidenceTypeID send" + tbFindEvidence.getEvidenceTypeID());
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if (mode == "edit") {
                        CSIDataTabFragment.apiCaseScene.getTbFindEvidences().remove(position);
                        Log.i(TAG, "tbFindEvidences remove num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));

                    } else {
                        Log.i(TAG, "tbFindEvidences num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));

                    }
                    getActivity().onBackPressed();
                }

            }
            if (v == btnTakePhotoEV) {
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
            if (s == editEvidenceNumber.getEditableText()) {
                tbFindEvidence.EvidenceNumber = editEvidenceNumber.getText().toString();
                Log.i(TAG, "EvidenceNumber " + tbFindEvidence.EvidenceNumber);
            } else if (s == editFindEvidenceZone.getEditableText()) {
                tbFindEvidence.FindEvidenceZone = editFindEvidenceZone.getText().toString();
                Log.i(TAG, "FindEvidenceZone " + tbFindEvidence.FindEvidenceZone);
            } else if (s == editMarking.getEditableText()) {
                tbFindEvidence.Marking = editMarking.getText().toString();
                Log.i(TAG, "Marking " + tbFindEvidence.Marking);
            } else if (s == editParceling.getEditableText()) {
                tbFindEvidence.Parceling = editParceling.getText().toString();
                Log.i(TAG, "Parceling " + tbFindEvidence.Parceling);
            } else if (s == editEvidencePerformed.getEditableText()) {
                tbFindEvidence.EvidencePerformed = editEvidencePerformed.getText().toString();
                Log.i(TAG, "EvidencePerformed " + tbFindEvidence.EvidencePerformed);
            }
        }
    }

    private class EvidenceOnItemSelectedListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view == spnEvidenceType) {
                oldEvidenceType = false;
            }
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            switch (parent.getId()) {
                case R.id.spinnerEvidenceType:
                    if (oldEvidenceType == false) {

                        tbFindEvidence.EvidenceTypeID = String.valueOf(evidenceTypeArray[i][0]);
                        Log.i(TAG, "EvidenceTypeID " + tbFindEvidence.EvidenceTypeID);

                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            switch (parent.getId()) {
                case R.id.spinnerEvidenceType:

                    tbFindEvidence.EvidenceTypeID = String.valueOf(evidenceTypeArray[0][0]);
                    Log.i(TAG, "EvidenceTypeID " + tbFindEvidence.EvidenceTypeID);

                    break;
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

                    TbPhotoOfEvidence tbPhotoOfEvidence = new TbPhotoOfEvidence();
                    tbPhotoOfEvidence.FindEvidenceID = sEVID;
                    tbPhotoOfEvidence.FileID = sPhotoID;
                    apiMultimedia.setTbPhotoOfEvidence(tbPhotoOfEvidence);

                    apiMultimediaList.add(apiMultimedia);
//                    CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);

                    CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
                    Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        Log.i(TAG, "PHOTO saved to Gallery!" + ResultTabFragment.strSDCardPathName + "Pictures/" + " : " + sPhotoID + ".jpg");

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
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence() != null) {
                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FindEvidenceID.equals(sEVID)
                                    && CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FileID.equals(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID)) {
                                apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                apiMultimedia.setTbPhotoOfEvidence(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence());
                                apiMultimediaList.add(apiMultimedia);
                                tbPhotoList.add(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());

                            }
                        }
                    }
                }
//                Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfEvidence(sEVID, "photo");
                for (int i = 0; i < apiMultimediaList.size(); i++) {
                    tbPhotoList.add(apiMultimediaList.get(i).getTbMultimediaFile());
                }
            }
        } else {
            apiMultimediaList = dbHelper.SelectDataPhotoOfEvidence(sEVID, "photo");
//            Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            for (int i = 0; i < apiMultimediaList.size(); i++) {
                tbPhotoList.add(apiMultimediaList.get(i).getTbMultimediaFile());
            }
        }
        int photolength = 0;
        if (apiMultimediaList != null) {
//            Log.i(TAG, "arrDataPhoto_Evidence " + String.valueOf(apiMultimediaList.size()));
            photolength = apiMultimediaList.size();
            // Calculated single Item Layout Width for each grid element ....
            int width = 70;

            float density = dm.density;

            int totalWidth = (int) (width * photolength * density);
            int singleItemWidth = (int) (width * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    totalWidth, singleItemWidth);

            horizontal_gridView_EV_photo.setLayoutParams(params);
            // horizontal_gridView_Outside.setHorizontalSpacing(2);
            horizontal_gridView_EV_photo.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            horizontal_gridView_EV_photo.setNumColumns(photolength);

            horizontal_gridView_EV_photo.setVisibility(View.VISIBLE);
            horizontal_gridView_EV_photo.setAdapter(new PhotoAdapter(getActivity()));
            registerForContextMenu(horizontal_gridView_EV_photo);
            // OnClick
            horizontal_gridView_EV_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            horizontal_gridView_EV_photo.setVisibility(View.GONE);
            Log.i(TAG, "Recieve_Evidence  Null!! ");

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