package com.scdc.csiapp.invmain;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.DateDialog;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.TimeDialog;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class ReceiveDataTabFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // Google play services
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Snackbar snackbar;
    private static final String TAG = "DEBUG-ReceiveDataTabFragment";
    FloatingActionButton fabBtnRec;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    DBHelper dbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    GetDateTime getDateTime;
    String officialID, reportID;
    String[] updateDT;
    private String message = "";
    TextView spnLocatePolice;
    TextView edtUpdateDateTime2;
    TextView editTextPhone1;
    private String sAddrDetail, sDistrictName, sAmphurName, sProvinceName, provinceid, amphurid, districtid;
    Spinner spinnerAntecedent;
    AutoCompleteTextView autoCompleteSuffererStatus;
    private EditText editAddrDetail, editCircumstanceOfCaseDetail, edtVehicleDetail, editSuffererName, editTextSuffererPhone;
    private Button btnButtonSearchLatLong, btnButtonSearchMap;
    private Spinner spinnerDistrict, spinnerAmphur, spinnerProvince;
    private String selectedProvince, selectedAmphur, selectedDistrict;
    String[][] mProvinceArray;
    String[] mProvinceArray2;
    String[][] mAmphurArray;
    String[] mAmphurArray2;
    String[][] mDistrictArray;
    String[] mDistrictArray2;
    String[] Antecedent;
    static String sInquiryOfficialID, sProvinceID, sAmphurID = "";
    // layout Sufferer 3
    String sReceivingCaseDate_New = "";
    String sHappenCaseDate_New = "";
    String sKnowCaseDate_New = "";
    String sSceneInvestDate_New = "";
    private ListView listViewSufferer, listViewInvestigator;
    private View linearLayoutAddSufferer, linearLayoutInvestigator;
    private Button btnAddSufferer, btnAddInvestigator, btnShowInvestigator, btnSaveSchedule, btnEditInvestigator;
    private ArrayList<HashMap<String, String>> suffererList;
    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ
    private String sReceivingCaseDate, sReceivingCaseTime, sHappenCaseDate,
            sHappenCaseTime, sKnowCaseDate, sKnowCaseTime, sSceneInvestDate,
            sSceneInvestTime, sCompleteSceneDate, sCompleteSceneTime, sCircumstanceOfCaseDetail;
    private TextView editReceiveCaseDate, editReceiveCaseTime, editScheduleInvestDate;
    private TextView editHappenCaseDate, editHappenCaseTime;
    private TextView editKnowCaseDate, editKnowCaseTime, valueLat, valueLong;
    // InvestDateTime ตรวจ
    private TextView editSceneInvestDate, editSceneInvestTime;
    // ตรวจเสร็จ
    //private TextView editCompleteSceneDate, editCompleteSceneTime;


    ArrayList<Integer> mMultiSelected;
    ArrayList<Boolean> mMultiChecked;
    String[][] mSelectDataInvestigatorArray = null;
    private ArrayList<HashMap<String, String>> InvestigatorList;
    protected static final int DIALOG_SelectDataInvestigator = 1; // Dialog 1 ID
    protected static final int DIALOG_AddSufferer = 0; // Dialog 2 ID
    ViewGroup viewByIdaddsufferer;
    View viewReceiveCSI;
    Context context;
    ArrayAdapter<String> adapterSelectDataInspector, adapterPoliceStation;
    protected static String selectScheduleID = null;
    String lat, lng;
    boolean oldAntecedent, oldProvince, oldAmphur, oldDistrict = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        viewReceiveCSI = inflater.inflate(R.layout.receive_tab_layout, null);

        context = viewReceiveCSI.getContext();

        rootLayout = (CoordinatorLayout) viewReceiveCSI.findViewById(R.id.rootLayoutReceive);
        dbHelper = new DBHelper(getActivity());
        mDb = dbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(getActivity());
        updateDT = getDateTime.updateDataDateTime();


        // ทำการสร้างตัวเชื่อกับ Google services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
            Log.d(TAG, "Create Google services");
        }

        Log.i("viewReceiveCSI", reportID);
        // new showScheduleInvestOfCase().execute(reportID);
        edtUpdateDateTime2 = (TextView) viewReceiveCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText("อัพเดทข้อมูลเมื่อ " + getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate) + " เวลา " + CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime);

        //โทรศัพท์ติดต่อ
        editTextPhone1 = (TextView) viewReceiveCSI.findViewById(R.id.editTextPhone);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel.equals("")) {
            editTextPhone1.setText("");
        } else {
            editTextPhone1.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel);
        }
        editTextPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel = editTextPhone1.getText().toString();
            }
        });

        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName.equals("")) {
            editAddrDetail.setText("");
        } else {
            editAddrDetail.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName);

        }
        editAddrDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName = editAddrDetail.getText().toString();

            }
        });


        //datetime
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate.equals("")) {
            editReceiveCaseDate.setText("");
        } else {
            editReceiveCaseDate.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate));
        }
        editReceiveCaseDate.setEnabled(false);

        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        editReceiveCaseTime.setEnabled(false);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime.equals("")) {
            editReceiveCaseTime.setText("");
        } else {
            editReceiveCaseTime.setText(getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime));
        }
        editReceiveCaseTime.setEnabled(false);
        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate.equals("")) {
            editHappenCaseDate.setText("");
        } else {
            editHappenCaseDate.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate));
        }
        editHappenCaseDate.setOnClickListener(new SummaryOnClickListener());
        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime.equals("")) {
            editHappenCaseTime.setText("");
        } else {
            editHappenCaseTime.setText(getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime));
        }
        editHappenCaseTime.setOnClickListener(new SummaryOnClickListener());
        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate.equals("")) {
            editKnowCaseDate.setText("");
        } else {
            editKnowCaseDate.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate));
        }
        editKnowCaseDate.setOnClickListener(new SummaryOnClickListener());
        editKnowCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseTime);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime.equals("")) {
            editKnowCaseTime.setText("");
        } else {
            editKnowCaseTime.setText(getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime));
        }
        editKnowCaseTime.setOnClickListener(new SummaryOnClickListener());
        //วันเวลาตรวจสถานที่เกิดเหตุ
        editSceneInvestDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editSceneInvestDate);
//        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SceneInvestDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime.equals("")) {
//            editSceneInvestDate.setText("");
//        } else {
//            editSceneInvestDate.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SceneInvestDate);
//        }
        editSceneInvestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Click SceneInvestDate", "null");
                DateDialog dialogSceneInvestDate = new DateDialog(view);
                dialogSceneInvestDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });
        editSceneInvestTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editSceneInvestTime);
        editSceneInvestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickSceneInvestTime", "null");
                TimeDialog dialogSceneInvestTime = new TimeDialog(view);
                dialogSceneInvestTime.show(getActivity().getFragmentManager(), "Time Picker");
            }

        });


// /show spinner
        spinnerProvince = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerProvince);
        spinnerAmphur = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAmphur);
        spinnerDistrict = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerDistrict);

        amphurid = CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID;
        districtid = CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID;
        provinceid = CSIDataTabFragment.apiCaseScene.getTbCaseScene().PROVINCE_ID;

        mProvinceArray = dbHelper.SelectAllProvince();
        if (mProvinceArray != null) {
            final String[] mProvinceArray2 = new String[mProvinceArray.length];
            for (int i = 0; i < mProvinceArray.length; i++) {
                mProvinceArray2[i] = mProvinceArray[i][2];
                // Log.i(TAG + " show mProvinceArray2", mProvinceArray2[i].toString());
            }
            ArrayAdapter<String> adapterProvince = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mProvinceArray2);
            spinnerProvince.setAdapter(adapterProvince);
        } else {
            Log.i(TAG + " show mProvinceArray", "null");
        }
//        mAmphurArray = dbHelper.SelectAllAumphur();
//        if (mAmphurArray != null) {
//            String[] mAmphurArray2 = new String[mAmphurArray.length];
//            for (int i = 0; i < mAmphurArray.length; i++) {
//                mAmphurArray2[i] = mAmphurArray[i][2];
//                // Log.i(TAG + " show mAmphurArray2", mAmphurArray2[i].toString());
//            }
//            ArrayAdapter<String> adapterAmphur = new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_dropdown_item_1line, mAmphurArray2);
//            spinnerAmphur.setAdapter(adapterAmphur);
//        } else {
//            spinnerAmphur.setAdapter(null);
//            selectedAmphur = null;
//            Log.i(TAG + " show mAmphurArray", String.valueOf(selectedAmphur));
//        }
//        mDistrictArray = dbHelper.SelectAllDistrict();
//        if (mDistrictArray != null) {
//            String[] mDistrictArray2 = new String[mDistrictArray.length];
//            for (int i = 0; i < mDistrictArray.length; i++) {
//                mDistrictArray2[i] = mDistrictArray[i][2];
//                //Log.i(TAG + " show mDistrictArray2", mDistrictArray2[i].toString());
//            }
//            ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_dropdown_item_1line, mDistrictArray2);
//            spinnerDistrict.setAdapter(adapterDistrict);
//        } else {
//            spinnerDistrict.setAdapter(null);
//            selectedDistrict = null;
//            Log.i(TAG + " show selectedDistrict", String.valueOf(selectedDistrict));
//        }
        //เเสดงค่าเดิม


        if (provinceid == null || provinceid.equals("") || provinceid.equals("null")) {
            spinnerProvince.setSelection(0);
        } else {
            for (int i = 0; i < mProvinceArray.length; i++) {
                if (provinceid.trim().equals(mProvinceArray[i][0])) {
                    spinnerProvince.setSelection(i);
                    oldProvince = true;
                    break;
                }
            }
            mAmphurArray = dbHelper.SelectAmphur(provinceid);
            if (mAmphurArray != null) {
                String[] mAmphurArray2 = new String[mAmphurArray.length];
                for (int i = 0; i < mAmphurArray.length; i++) {
                    mAmphurArray2[i] = mAmphurArray[i][2];
                    Log.i(TAG + " show mAmphurArray2", mAmphurArray2[i].toString());
                }
                ArrayAdapter<String> adapterAmphur = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, mAmphurArray2);
                spinnerAmphur.setAdapter(adapterAmphur);
            } else {
                spinnerAmphur.setAdapter(null);
                selectedAmphur = null;
                Log.i(TAG + " show mAmphurArray", String.valueOf(selectedAmphur));
            }
        }
        if (amphurid == null || amphurid.equals("") || amphurid.equals("null")) {
            spinnerAmphur.setSelection(0);
        } else {
            for (int i = 0; i < mAmphurArray.length; i++) {
                if (amphurid.trim().equals(mAmphurArray[i][0].toString())) {
                    spinnerAmphur.setSelection(i);
                    //oldAmphur = true;
                    break;
                }
            }
            mDistrictArray = dbHelper.SelectDistrict(amphurid);
            if (mDistrictArray != null) {
                String[] mDistrictArray2 = new String[mDistrictArray.length];
                for (int i = 0; i < mDistrictArray.length; i++) {
                    mDistrictArray2[i] = mDistrictArray[i][2];
                    Log.i(TAG + " show mDistrictArray2", mDistrictArray2[i].toString());
                }
                ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, mDistrictArray2);
                spinnerDistrict.setAdapter(adapterDistrict);
            } else {
                spinnerDistrict.setAdapter(null);
                selectedDistrict = null;
                Log.i(TAG + " show selectedDistrict", String.valueOf(selectedDistrict));
            }
        }
        if (districtid == null || districtid.equals("") || districtid.equals("null")) {
            spinnerDistrict.setSelection(0);
        } else {
            for (int i = 0; i < mDistrictArray.length; i++) {
                if (districtid.trim().equals(mDistrictArray[i][0].toString())) {
                    spinnerDistrict.setSelection(i);
                    //oldDistrict = true;
                    break;
                }
            }

        }
        spinnerDistrict.setOnItemSelectedListener(new RecOnItemSelectedListener());
        spinnerProvince.setOnItemSelectedListener(new RecOnItemSelectedListener());
        spinnerAmphur.setOnItemSelectedListener(new RecOnItemSelectedListener());
        spinnerDistrict.setOnTouchListener(new RecOnItemSelectedListener());
        spinnerProvince.setOnTouchListener(new RecOnItemSelectedListener());
        spinnerAmphur.setOnTouchListener(new RecOnItemSelectedListener());

        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);

        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude.equals("")) {
            valueLat.setText("");
        } else {
            valueLat.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude);
        }
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude.equals("")) {
            valueLong.setText("");
        } else {
            valueLong.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude);

        }


        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(new SummaryOnClickListener());
        btnButtonSearchLatLong = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchLatLong);
        btnButtonSearchLatLong.setOnClickListener(new SummaryOnClickListener());

        spinnerAntecedent = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAntecedent);

        Antecedent = getResources().getStringArray(R.array.antecedent);
        ArrayAdapter<String> adapterEnglish = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Antecedent);
        spinnerAntecedent.setAdapter(adapterEnglish);
        spinnerAntecedent.setOnItemSelectedListener(new RecOnItemSelectedListener());
        spinnerAntecedent.setOnTouchListener(new RecOnItemSelectedListener());
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename != null) {
            for (int i = 0; i < Antecedent.length; i++) {
                if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename.trim().equals(Antecedent[i].toString())) {
                    spinnerAntecedent.setSelection(i);
                    oldAntecedent = true;
                    break;
                }
            }
        }

        editSuffererName = (EditText) viewReceiveCSI.findViewById(R.id.editSuffererName);
        //Log.i(TAG, "SuffererName " + CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererName);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName.equals("")) {
            editSuffererName.setText("");
        } else {
            editSuffererName.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName);


        }
        editSuffererName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName = editSuffererName.getText().toString();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererName = editSuffererName.getText().toString();

                Log.i(TAG, "SuffererName " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName);
            }
        });

        autoCompleteSuffererStatus = (AutoCompleteTextView) viewReceiveCSI.findViewById(R.id.autoCompleteSuffererStatus);
        // Log.i(TAG, "SuffererStatus " + CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus);
        final String[] SuffererStatus = getResources().getStringArray(R.array.suffererStatus);
        ArrayAdapter<String> adapterSuffererStatus = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SuffererStatus);
        autoCompleteSuffererStatus.setAdapter(adapterSuffererStatus);

        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus.equals("")) {
            autoCompleteSuffererStatus.setText("");
        } else {
            autoCompleteSuffererStatus.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus);
        }
        autoCompleteSuffererStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus = autoCompleteSuffererStatus.getText().toString();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus = autoCompleteSuffererStatus.getText().toString();

                Log.i(TAG, "SuffererStatus " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus);
            }
        });
        editTextSuffererPhone = (EditText) viewReceiveCSI.findViewById(R.id.editTextSuffererPhone);
        // Log.i(TAG, "SuffererPhoneNum " + CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum.equals("")) {
            editTextSuffererPhone.setText("");
        } else {
            editTextSuffererPhone.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum);
        }
        editTextSuffererPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum = editTextSuffererPhone.getText().toString();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum = editTextSuffererPhone.getText().toString();

                Log.i(TAG, "SuffererPhoneNum " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum);
            }
        });

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail.equals("")) {
            editCircumstanceOfCaseDetail.setText("");

        } else {
            editCircumstanceOfCaseDetail.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail);

        }
        editCircumstanceOfCaseDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();
            }
        });
        edtVehicleDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtVehicleDetail);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo.equals("")) {
            edtVehicleDetail.setText("");

        } else {
            edtVehicleDetail.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo);

        }
        edtVehicleDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo = edtVehicleDetail.getText().toString();
            }
        });

        fabBtnRec = (FloatingActionButton) viewReceiveCSI.findViewById(R.id.fabBtnRec);
        fabBtnRec.setOnClickListener(new SummaryOnClickListener());
        if (CSIDataTabFragment.mode == "view") {
            fabBtnRec.setEnabled(false);
            if (fabBtnRec != null || fabBtnRec.isShown()) {
                fabBtnRec.setVisibility(View.GONE);
            }

            editTextPhone1.setEnabled(false);
            editReceiveCaseDate.setEnabled(false);
            editReceiveCaseTime.setEnabled(false);
            editHappenCaseDate.setEnabled(false);
            editHappenCaseTime.setEnabled(false);
            editKnowCaseDate.setEnabled(false);
            editKnowCaseTime.setEnabled(false);
            editAddrDetail.setEnabled(false);
            spinnerProvince.setEnabled(false);
            spinnerAmphur.setEnabled(false);
            spinnerDistrict.setEnabled(false);
            btnButtonSearchLatLong.setEnabled(false);

            spinnerAntecedent.setEnabled(false);
            editSuffererName.setEnabled(false);
            autoCompleteSuffererStatus.setEnabled(false);
            editTextSuffererPhone.setEnabled(false);
            editCircumstanceOfCaseDetail.setEnabled(false);
            edtVehicleDetail.setEnabled(false);
        }
        return viewReceiveCSI;
    }


    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        Log.i("Check", "onStart recieve");

    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause receive");


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.i("onStop", "onStop receive");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", "onDestroyView receive");
        //saveAllReceiveData();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        Log.d(TAG, "Call Location Services");
        LocationRequest request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(10)//อ่านค่าใหม่ทุก 10 เมตร
                .setFastestInterval(1000)//อ่านค่าแบบรวดเร็วภายใน 1 วินาที
                .setInterval(10000);//อ่านค่าเป็นช่วงๆ ทุก 10 วินาที
        FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);

        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, "get mLastLocation");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation.set(location);
        Log.d(TAG, "Location " + location.getLatitude() + " " + location.getLatitude());
    }

    private class SummaryOnClickListener implements View.OnClickListener {
        public void onClick(View v) {

            if (v == fabBtnRec) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate = getDateTime.changeDateFormatToDB(editHappenCaseDate.getText().toString());
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime = editHappenCaseTime.getText().toString();
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate = getDateTime.changeDateFormatToDB(editKnowCaseDate.getText().toString());
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime = editKnowCaseTime.getText().toString();

                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];


                if (CSIDataTabFragment.apiCaseScene.getTbCaseScene() != null) {
                    boolean isSuccess = dbHelper.saveCaseScene(CSIDataTabFragment.apiCaseScene.getTbCaseScene());
                    if (isSuccess) {
                        boolean isSuccess1 = dbHelper.saveNoticeCase(CSIDataTabFragment.apiCaseScene.getTbNoticeCase());
                        if (isSuccess1) {
                            if (snackbar == null || !snackbar.isShown()) {
                                snackbar = Snackbar.make(rootLayout, getString(R.string.save_complete) + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID, Snackbar.LENGTH_INDEFINITE)
                                        .setAction(getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                snackbar.show();
                            }
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
            if (v == btnButtonSearchMap) {

                if (lat != null || lng != null) {
                    Log.d(TAG, "Go to Google map " + lat + " " + lng);

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    //Searches for 'Locale name' province amphur district

                    Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + sAddrDetail + "+" + sDistrictName + "+" + sAmphurName + "+" + sProvinceName);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
            if (v == btnButtonSearchLatLong) {
                lat = String.valueOf(mLastLocation.getLatitude());
                lng = String.valueOf(mLastLocation.getLongitude());
                Log.d(TAG, "Go to Google map " + lat + " " + lng);
                valueLat.setText(lat);
                valueLong.setText(lng);
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude = lat.toString();
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude = lng.toString();

                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().Latitude = lat.toString();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().Longitude = lng.toString();
            }
            if (v == editHappenCaseDate) {
                Log.i("ClickHappenCaseDate", "null");
                DateDialog dialogHappenCaseDate = new DateDialog(v);
                dialogHappenCaseDate.show(getActivity().getFragmentManager(), "Date Picker");

            }
            if (v == editHappenCaseTime) {
                Log.i("ClickHappenCaseTime", "null");
                TimeDialog dialogHappenCaseTime = new TimeDialog(v);
                dialogHappenCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
            }
            if (v == editKnowCaseDate) {
                Log.i("Click KnowCaseDate", "null");
                DateDialog dialogKnowCaseDate = new DateDialog(v);
                dialogKnowCaseDate.show(getActivity().getFragmentManager(), "Date Picker");
            }
            if (v == editKnowCaseTime) {
                Log.i("Click KnowCaseTime", "null");
                TimeDialog dialogKnowCaseTime = new TimeDialog(v);
                dialogKnowCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
            }

        }
    }

    public class RecOnItemSelectedListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view == spinnerAntecedent) {
                oldAntecedent = false;
            }
            if (view == spinnerProvince) {
                oldProvince = false;
            }
            if (view == spinnerAmphur) {
                oldAmphur = false;
            }
            if (view == spinnerDistrict) {
               // oldDistrict = false;
            }
            return false;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            //check which spinner triggered the listener

            switch (parent.getId()) {

                case R.id.spinnerDistrict:
                   // if (oldDistrict == false) {
                        selectedDistrict = mDistrictArray[pos][0];
                        sDistrictName = mDistrictArray[pos][2].toString();
                        Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID = selectedDistrict;
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID = selectedDistrict;
                        Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID);
                  // }
                    break;
                case R.id.spinnerAmphur:
//                    if (oldAmphur == false) {
                        selectedAmphur = mAmphurArray[pos][0];
                        sAmphurName = mAmphurArray[pos][2].toString();
                        Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID = selectedAmphur;
                        Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID);
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID = selectedAmphur;
                        //ดึงค่า District

                        mDistrictArray = dbHelper.SelectDistrict(selectedAmphur);
                        if (mDistrictArray != null) {
                            String[] mDistrictArray2 = new String[mDistrictArray.length];
                            for (int i = 0; i < mDistrictArray.length; i++) {
                                mDistrictArray2[i] = mDistrictArray[i][2];
                                // Log.i(TAG + " show mDistrictArray2", mDistrictArray2[i].toString());
                            }
                            ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, mDistrictArray2);
                            spinnerDistrict.setAdapter(adapterDistrict);
                        } else {
                            spinnerDistrict.setAdapter(null);
                            selectedDistrict = null;
                            Log.i(TAG + " show selectedDistrict", String.valueOf(selectedDistrict));
                        }
//                    }
                    break;
                case R.id.spinnerProvince:
                    if (oldProvince == false) {
                        selectedProvince = mProvinceArray[pos][0];
                        sProvinceName = mProvinceArray[pos][2].toString();
                        Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().PROVINCE_ID = selectedProvince;
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().PROVINCE_ID = selectedProvince;
                        Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().PROVINCE_ID);
                        //provinceid = selectedProvince[0];
                        //ดึงค่า amphur
                        mAmphurArray = dbHelper.SelectAmphur(selectedProvince);
                        if (mAmphurArray != null) {
                            String[] mAmphurArray2 = new String[mAmphurArray.length];
                            for (int i = 0; i < mAmphurArray.length; i++) {
                                mAmphurArray2[i] = mAmphurArray[i][2];
                                //  Log.i(TAG + " show mAmphurArray2", mAmphurArray2[i].toString());
                            }
                            ArrayAdapter<String> adapterAmphur = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, mAmphurArray2);
                            spinnerAmphur.setAdapter(adapterAmphur);
                        } else {
                            spinnerAmphur.setAdapter(null);
                            selectedAmphur = null;
                            Log.i(TAG + " show mAmphurArray", String.valueOf(selectedAmphur));
                        }
                    }
                    break;

                case R.id.spinnerAntecedent:
                    if (oldAntecedent == false) {
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename = String.valueOf(Antecedent[pos]);
                        Log.i(TAG, "spinnerAntecedent " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename);
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererPrename = String.valueOf(Antecedent[pos]);
                    }
                    break;
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
            switch (parent.getId()) {
                case R.id.spinnerDistrict:
                    selectedDistrict = mDistrictArray[0][0];
                    sDistrictName = mDistrictArray[0][2].toString();
                    Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID = selectedDistrict;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID = selectedDistrict;
                    break;
                case R.id.spinnerAmphur:
                    selectedAmphur = mAmphurArray[0][0];
                    sAmphurName = mAmphurArray[0][2].toString();
                    Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID = selectedAmphur;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID = selectedAmphur;
                    break;
                case R.id.spinnerProvince:
                    selectedProvince = mProvinceArray[0][0];
                    sProvinceName = mProvinceArray[0][2].toString();
                    Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().PROVINCE_ID = selectedProvince;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().PROVINCE_ID = selectedProvince;
                    break;
                case R.id.spinnerAntecedent:
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename = String.valueOf(Antecedent[0]);
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererPrename = String.valueOf(Antecedent[0]);
                    Log.i(TAG, "spinnerAntecedent " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename);
                    break;
            }
        }


    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable(CSIDataTabFragment.Bundle_Key,CSIDataTabFragment.apiCaseScene);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        CSIDataTabFragment.apiCaseScene = (ApiCaseScene) savedInstanceState.getSerializable(CSIDataTabFragment.Bundle_Key);
//    }
}