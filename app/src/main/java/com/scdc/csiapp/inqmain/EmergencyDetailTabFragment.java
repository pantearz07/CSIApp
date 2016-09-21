package com.scdc.csiapp.inqmain;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.DateDialog;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.TimeDialog;


public class EmergencyDetailTabFragment extends Fragment implements View.OnClickListener {
    FloatingActionButton fabBtnRec;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    EmergencyTabFragment emergencyTabFragment;
    DBHelper dbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Snackbar snackbar;
    GetDateTime getDateTime;
    String officialID, reportID;
    ArrayAdapter<String> adapterSubCaseType;
    String[] updateDT;
    private String message = "";
    String sReceivingCaseDate_New = "";
    String sHappenCaseDate_New = "";
    String sKnowCaseDate_New = "";
    TextView edtUpdateDateTime2;
    EditText editTextPhone1;
    private String sAddrDetail, sDistrictName, sAmphurName, sProvinceName, provinceid, amphurid, districtid;
    private Spinner spinnerDistrict, spinnerAmphur, spinnerProvince;
    private String selectedProvince, selectedAmphur, selectedDistrict;
    String[][] mProvinceArray;
    String[] mProvinceArray2;
    String[][] mAmphurArray;
    String[] mAmphurArray2;
    String[][] mDistrictArray;
    String[] mDistrictArray2;

    private EditText editAddrDetail, editCircumstanceOfCaseDetail, edtVehicleDetail;
    private Button btnButtonSearchMap, btnButtonSearchLatLong;
    String lat, lng;
    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ

    private TextView editReceiveCaseDate, editReceiveCaseTime;
    private TextView editHappenCaseDate, editHappenCaseTime;
    private TextView editKnowCaseDate, editKnowCaseTime, valueLat, valueLong;

    private static final String TAG = "DEBUG-EmergencyDetailTabFragment";

    View viewReceiveCSI;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        viewReceiveCSI = inflater.inflate(R.layout.emergency_tab_layout, null);
        context = viewReceiveCSI.getContext();
        rootLayout = (CoordinatorLayout) viewReceiveCSI.findViewById(R.id.rootLayoutReceive);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        dbHelper = new DBHelper(getActivity());
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        getDateTime = new GetDateTime();
        emergencyTabFragment = new EmergencyTabFragment();
        cd = new ConnectionDetector(getActivity());

        updateDT = getDateTime.getDateTimeNow();
        String noticecaseid = EmergencyTabFragment.tbNoticeCase.getNoticeCaseID();
        Log.i(TAG, " NoticeCaseID " + noticecaseid);
        //Show เวลาล่าสุดที่อัพเดต
        edtUpdateDateTime2 = (TextView) viewReceiveCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText("อัพเดทข้อมูลล่าสุดเมื่อวันที่ " +
                getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.LastUpdateDate)
                + " เวลา " + EmergencyTabFragment.tbNoticeCase.LastUpdateTime);
        //Show spinner สถานที่ตำรวจภูธร


        editTextPhone1 = (EditText) viewReceiveCSI.findViewById(R.id.editTextPhone);
        if (EmergencyTabFragment.tbNoticeCase.CaseTel != "") {
            editTextPhone1.setText(EmergencyTabFragment.tbNoticeCase.CaseTel);
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
                EmergencyTabFragment.tbNoticeCase.CaseTel = editTextPhone1.getText().toString();
            }
        });
        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);
        //btn_clear_txt_1 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_1);
        if (EmergencyTabFragment.tbNoticeCase.LocaleName != "") {
            editAddrDetail.setText(EmergencyTabFragment.tbNoticeCase.LocaleName);
        }
        editAddrDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // btn_clear_txt_1.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //btn_clear_txt_1.setVisibility(View.VISIBLE);
                //btn_clear_txt_1.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {
                EmergencyTabFragment.tbNoticeCase.LocaleName = editAddrDetail.getText().toString();
            }
        });

// /show spinner
        spinnerProvince = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerProvince);
        spinnerAmphur = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAmphur);
        spinnerDistrict = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerDistrict);
        amphurid = EmergencyTabFragment.tbNoticeCase.AMPHUR_ID;
        districtid = EmergencyTabFragment.tbNoticeCase.DISTRICT_ID;
        provinceid = EmergencyTabFragment.tbNoticeCase.PoliceStationID;

        mProvinceArray = dbHelper.SelectAllProvince();
        if (mProvinceArray != null) {
            final String[] mProvinceArray2 = new String[mProvinceArray.length];
            for (int i = 0; i < mProvinceArray.length; i++) {
                mProvinceArray2[i] = mProvinceArray[i][2];
                Log.i(TAG + " show mProvinceArray2", mProvinceArray2[i].toString());
            }
            ArrayAdapter<String> adapterProvince = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mProvinceArray2);
            spinnerProvince.setAdapter(adapterProvince);
        } else {
            Log.i(TAG + " show mProvinceArray", "null");
        }
        mAmphurArray = dbHelper.SelectAllAumphur();
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
        mDistrictArray = dbHelper.SelectAllDistrict();
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
        //เเสดงค่าเดิม
        if (provinceid == null || provinceid.equals("") || provinceid.equals("null")) {
            spinnerProvince.setSelection(0);
        } else {
            for (int i = 0; i < mProvinceArray.length; i++) {
                if (provinceid.trim().equals(mProvinceArray[i][0])) {
                    spinnerProvince.setSelection(i);
                    break;
                }
            }
        }
        if (amphurid == null || amphurid.equals("") || amphurid.equals("null")) {
            spinnerProvince.setSelection(0);
        } else {
            for (int i = 0; i < mProvinceArray.length; i++) {
                if (amphurid.trim().equals(mAmphurArray[i][0].toString())) {
                    spinnerAmphur.setSelection(i);
                    break;
                }
            }
        }
        if (districtid == null || districtid.equals("") || districtid.equals("null")) {
            spinnerProvince.setSelection(0);
        } else {
            for (int i = 0; i < mDistrictArray.length; i++) {
                if (districtid.trim().equals(mDistrictArray[i][0].toString())) {
                    spinnerDistrict.setSelection(i);
                    break;
                }
            }
        }
        spinnerDistrict.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spinnerProvince.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spinnerAmphur.setOnItemSelectedListener(new EmerOnItemSelectedListener());


        //datetime
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        if (EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate != "") {
            editReceiveCaseDate.setText(EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate);
        }
        editReceiveCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateDialog dialogReceiveCaseDate = new DateDialog(view);
                dialogReceiveCaseDate.show(getActivity().getFragmentManager(), "Date Picker");
            }

        });
        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        if (EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime != "") {
            editReceiveCaseTime.setText(EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime);
        }
        editReceiveCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeDialog dialogReceiveCaseTime = new TimeDialog(view);
                dialogReceiveCaseTime.show(getActivity().getFragmentManager(), "Time Picker");

            }

        });
        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);
        if (EmergencyTabFragment.tbNoticeCase.HappenCaseDate != "") {
            editHappenCaseDate.setText(EmergencyTabFragment.tbNoticeCase.HappenCaseDate);
        }
        editHappenCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateDialog dialogHappenCaseDate = new DateDialog(view);
                dialogHappenCaseDate.show(getActivity().getFragmentManager(), "Date Picker");


            }

        });
        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);

        if (EmergencyTabFragment.tbNoticeCase.HappenCaseDate != "") {
            editHappenCaseDate.setText(EmergencyTabFragment.tbNoticeCase.HappenCaseDate);
        }
        editHappenCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeDialog dialogHappenCaseTime = new TimeDialog(view);
                dialogHappenCaseTime.show(getActivity().getFragmentManager(), "Time Picker");

            }


        });

        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);

        if (EmergencyTabFragment.tbNoticeCase.KnowCaseDate != "") {
            editKnowCaseDate.setText(EmergencyTabFragment.tbNoticeCase.KnowCaseDate);
        }
        editKnowCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Click KnowCaseTime", editKnowCaseDate.getText().toString());
                DateDialog dialogKnowCaseDate = new DateDialog(view);
                dialogKnowCaseDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });
        editKnowCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseTime);
        if (EmergencyTabFragment.tbNoticeCase.KnowCaseTime != "") {
            editKnowCaseTime.setText(EmergencyTabFragment.tbNoticeCase.KnowCaseTime);
        }
        editKnowCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickKnowCaseTime", editKnowCaseTime.getText().toString());
                TimeDialog dialogKnowCaseTime = new TimeDialog(view);
                dialogKnowCaseTime.show(getActivity().getFragmentManager(), "Time Picker");


            }

        });


        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);
        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(this);
        btnButtonSearchLatLong = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchLatLong);
        btnButtonSearchMap.setOnClickListener(this);

//        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            Log.d(TAG, "get mLastLocation");
//
//
////            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        }

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        if (EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail != "") {
            editCircumstanceOfCaseDetail.setText(EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail);
        }
        editCircumstanceOfCaseDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();
            }
        });
        final Spinner spinnerAntecedent = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAntecedent);

        final String[] Antecedent = getResources().getStringArray(R.array.antecedent);
        ArrayAdapter<String> adapterEnglish = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Antecedent);
        spinnerAntecedent.setAdapter(adapterEnglish);
        spinnerAntecedent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmergencyTabFragment.tbNoticeCase.SuffererPrename = String.valueOf(Antecedent[position]);
                Log.i(TAG, "spinnerAntecedent " + EmergencyTabFragment.tbNoticeCase.SuffererPrename);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                EmergencyTabFragment.tbNoticeCase.SuffererPrename = String.valueOf(Antecedent[0]);

                Log.i(TAG, "spinnerAntecedent " + EmergencyTabFragment.tbNoticeCase.SuffererPrename);
            }
        });
        if (EmergencyTabFragment.tbNoticeCase.SuffererPrename != null) {
            for (int i = 0; i < Antecedent.length; i++) {
                if (EmergencyTabFragment.tbNoticeCase.SuffererPrename.trim().equals(Antecedent[i].toString())) {
                    spinnerAntecedent.setSelection(i);
                    break;
                }
            }
        }

        final EditText editSuffererName = (EditText) viewReceiveCSI.findViewById(R.id.editSuffererName);
        if (EmergencyTabFragment.tbNoticeCase.SuffererName != "") {
            editSuffererName.setText(EmergencyTabFragment.tbNoticeCase.SuffererName);
        }
        editSuffererName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                EmergencyTabFragment.tbNoticeCase.SuffererName = editSuffererName.getText().toString();
            }
        });

        AutoCompleteTextView autoCompleteSuffererStatus = (AutoCompleteTextView) viewReceiveCSI.findViewById(R.id.autoCompleteSuffererStatus);
        if (EmergencyTabFragment.tbNoticeCase.SuffererStatus != "") {
            autoCompleteSuffererStatus.setText(EmergencyTabFragment.tbNoticeCase.SuffererStatus);
        }
        EmergencyTabFragment.tbNoticeCase.SuffererStatus = autoCompleteSuffererStatus.getText().toString();

        final EditText editTextSuffererPhone = (EditText) viewReceiveCSI.findViewById(R.id.editTextSuffererPhone);
        if (EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum != "") {
            editTextSuffererPhone.setText(EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum);
        }
        editTextSuffererPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum = editTextSuffererPhone.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum = editTextSuffererPhone.getText().toString();
            }
        });

        fabBtnRec = (FloatingActionButton) viewReceiveCSI.findViewById(R.id.fabBtnRec);
        if (emergencyTabFragment.mode == "view") {
            fabBtnRec.setVisibility(View.GONE);
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
            spinnerProvince.setEnabled(false);
            spinnerAntecedent.setEnabled(false);
            editSuffererName.setEnabled(false);
            autoCompleteSuffererStatus.setEnabled(false);
            editTextSuffererPhone.setEnabled(false);
            editCircumstanceOfCaseDetail.setEnabled(false);
        }
        fabBtnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                EmergencyTabFragment.tbNoticeCase.HappenCaseDate = editHappenCaseDate.getText().toString();
                EmergencyTabFragment.tbNoticeCase.HappenCaseTime = editHappenCaseTime.getText().toString();
                EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate = editReceiveCaseDate.getText().toString();
                EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime = editReceiveCaseTime.getText().toString();
                EmergencyTabFragment.tbNoticeCase.KnowCaseDate = editKnowCaseDate.getText().toString();
                EmergencyTabFragment.tbNoticeCase.KnowCaseTime = editKnowCaseTime.getText().toString();
                Log.i(TAG, "spinnerKnowCaseTime" + EmergencyTabFragment.tbNoticeCase.KnowCaseTime);
                EmergencyTabFragment.tbNoticeCase.LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                EmergencyTabFragment.tbNoticeCase.LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                if (EmergencyTabFragment.tbNoticeCase != null) {
                    boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
                    if (isSuccess) {
                        if (snackbar == null || !snackbar.isShown()) {
                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_complete) + " " + EmergencyTabFragment.tbNoticeCase.LastUpdateDate, Snackbar.LENGTH_INDEFINITE)
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
        });


        return viewReceiveCSI;
    }

    public void onStart() {
        super.onStart();
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
        Log.i("onStop", "onStop receive");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", "onDestroyView receive");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnButtonSearchMap:
                if (lat != null || lng != null) {
                    Log.d(TAG, "Go to Google map " + lat + " " + lng);

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    //Searches for 'Locale name' province amphur district
                    Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + EmergencyTabFragment.tbNoticeCase.LocaleName + "+" + sDistrictName + "+" + sAmphurName + "+" + sProvinceName);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                break;
            case R.id.btnButtonSearchLatLong:
                Log.d(TAG, "Go to Google map " + lat + " " + lng);
                EmergencyTabFragment.tbNoticeCase.Latitude = lat;
                EmergencyTabFragment.tbNoticeCase.Longitude = lng;
                break;
        }
    }

    public class EmerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {

                case R.id.spinnerDistrict:
                    selectedDistrict = mDistrictArray[pos][0];
                    sDistrictName = mDistrictArray[pos][2].toString();
                    Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                    EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict;
                    Log.i(TAG ,EmergencyTabFragment.tbNoticeCase.DISTRICT_ID);
                    break;
                case R.id.spinnerAmphur:
                    selectedAmphur = mAmphurArray[pos][0];
                    sAmphurName = mAmphurArray[pos][2].toString();
                    Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);
                    EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur;
                    Log.i(TAG ,EmergencyTabFragment.tbNoticeCase.AMPHUR_ID);
                    //   EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur[0];
                    //ดึงค่า District

                    mDistrictArray = dbHelper.SelectDistrict(selectedAmphur);
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
                    spinnerDistrict.setOnItemSelectedListener(new EmerOnItemSelectedListener());
                    break;
                case R.id.spinnerProvince:
                    selectedProvince = mProvinceArray[pos][0];
                    sProvinceName = mProvinceArray[pos][2].toString();
                    Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);
                    EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = selectedProvince;
                    Log.i(TAG ,EmergencyTabFragment.tbNoticeCase.PROVINCE_ID);
                    //provinceid = selectedProvince[0];
                    //ดึงค่า amphur
                    mAmphurArray = dbHelper.SelectAmphur(selectedProvince);
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
                    spinnerAmphur.setOnItemSelectedListener(new EmerOnItemSelectedListener());
                    break;
                case R.id.spinnerAntecedent:

                    break;
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
}