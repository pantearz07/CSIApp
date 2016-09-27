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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class AssignDetailTabFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // Google play services
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    FloatingActionButton fabBtnRec;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
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

    private static final String TAG = "DEBUG-AssignDetailTabFragment";

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

        cd = new ConnectionDetector(getActivity());

        // ทำการสร้างตัวเชื่อกับ Google services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
            Log.d(TAG, "Create Google services");
        }

        updateDT = getDateTime.getDateTimeNow();
        String noticecaseid = AssignTabFragment.apiCaseScene.getTbNoticeCase().getNoticeCaseID();
        Log.i(TAG, " NoticeCaseID " + noticecaseid);
        //Show เวลาล่าสุดที่อัพเดต
        edtUpdateDateTime2 = (TextView) viewReceiveCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText("อัพเดทข้อมูลล่าสุดเมื่อวันที่ " +
                getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate)
                + " เวลา " + AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime);
        //Show spinner สถานที่ตำรวจภูธร


        editTextPhone1 = (EditText) viewReceiveCSI.findViewById(R.id.editTextPhone);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTel != "") {
            editTextPhone1.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTel);
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
                AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTel = editTextPhone1.getText().toString();
            }
        });
        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);
        //btn_clear_txt_1 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_1);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().LocaleName != "") {
            editAddrDetail.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().LocaleName);
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
                AssignTabFragment.apiCaseScene.getTbNoticeCase().LocaleName = editAddrDetail.getText().toString();
            }
        });

// /show spinner
        spinnerProvince = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerProvince);
        spinnerAmphur = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAmphur);
        spinnerDistrict = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerDistrict);
        amphurid = AssignTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID;
        districtid = AssignTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID;
        provinceid = AssignTabFragment.apiCaseScene.getTbNoticeCase().PoliceStationID;

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
//        if (provinceid == null || provinceid.equals("") || provinceid.equals("null")) {
//            spinnerProvince.setSelection(0);
//        } else {
//            for (int i = 0; i < mProvinceArray.length; i++) {
//                if (provinceid.trim().equals(mProvinceArray[i][0])) {
//                    spinnerProvince.setSelection(i);
//                    break;
//                }
//            }
//        }
//        if (amphurid == null || amphurid.equals("") || amphurid.equals("null")) {
//            spinnerProvince.setSelection(0);
//        } else {
//            for (int i = 0; i < mProvinceArray.length; i++) {
//                if (amphurid.trim().equals(mAmphurArray[i][0].toString())) {
//                    spinnerAmphur.setSelection(i);
//                    break;
//                }
//            }
//        }
//        if (districtid == null || districtid.equals("") || districtid.equals("null")) {
//            spinnerProvince.setSelection(0);
//        } else {
//            for (int i = 0; i < mDistrictArray.length; i++) {
//                if (districtid.trim().equals(mDistrictArray[i][0].toString())) {
//                    spinnerDistrict.setSelection(i);
//                    break;
//                }
//            }
//        }
        spinnerDistrict.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spinnerProvince.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spinnerAmphur.setOnItemSelectedListener(new EmerOnItemSelectedListener());


        //datetime
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseDate == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseDate.equals("")) {
            editReceiveCaseDate.setText("");
        } else {
            editReceiveCaseDate.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseDate));
        }

        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseTime == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseTime.equals("")) {
            editReceiveCaseTime.setText("");
        } else {
            editReceiveCaseTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseTime));
        }


        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseDate == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseDate.equals("")) {
            editHappenCaseDate.setText("");
        } else {
            editHappenCaseDate.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseDate));
        }

        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseTime == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseTime.equals("")) {
            editHappenCaseTime.setText("");
        } else {
            editHappenCaseTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseTime));
        }

        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseDate == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseDate.equals("")) {
            editKnowCaseDate.setText("");
        } else {
            editKnowCaseDate.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseDate));
        }

        editKnowCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseTime);

        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseTime == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseTime.equals("")) {
            editKnowCaseTime.setText("");
        } else {
            editKnowCaseTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseTime));
        }


        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);
        valueLat.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().Latitude);
        valueLong.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().Longitude);
//        lat = AssignTabFragment.apiCaseScene.getTbNoticeCase().Latitude;
//        lng = AssignTabFragment.apiCaseScene.getTbNoticeCase().Longitude;

        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(this);
        btnButtonSearchLatLong = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchLatLong);
        btnButtonSearchLatLong.setOnClickListener(this);

        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            Log.d(TAG, "get mLastLocation");
//
//
////            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        }

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail != "") {
            editCircumstanceOfCaseDetail.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail);
        }
        editCircumstanceOfCaseDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AssignTabFragment.apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                AssignTabFragment.apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();
            }
        });
        final Spinner spinnerAntecedent = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAntecedent);

        final String[] Antecedent = getResources().getStringArray(R.array.antecedent);
        ArrayAdapter<String> adapterEnglish = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Antecedent);
        spinnerAntecedent.setAdapter(adapterEnglish);

        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPrename != null) {
            for (int i = 0; i < Antecedent.length; i++) {
                if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPrename.trim().equals(Antecedent[i].toString())) {
                    spinnerAntecedent.setSelection(i);
                    break;
                }
            }
        }

        final EditText editSuffererName = (EditText) viewReceiveCSI.findViewById(R.id.editSuffererName);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName != "") {
            editSuffererName.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName);
        }


        AutoCompleteTextView autoCompleteSuffererStatus = (AutoCompleteTextView) viewReceiveCSI.findViewById(R.id.autoCompleteSuffererStatus);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus != "") {
            autoCompleteSuffererStatus.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus);
        }
        AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus = autoCompleteSuffererStatus.getText().toString();

        final EditText editTextSuffererPhone = (EditText) viewReceiveCSI.findViewById(R.id.editTextSuffererPhone);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum != "") {
            editTextSuffererPhone.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum);
        }


        fabBtnRec = (FloatingActionButton) viewReceiveCSI.findViewById(R.id.fabBtnRec);
        if (AssignTabFragment.mode == "view") {
            fabBtnRec.setVisibility(View.GONE);
            fabBtnRec.setEnabled(false);
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
        }

        fabBtnRec.setOnClickListener(this);

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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabBtnRec:

                break;
            case R.id.btnButtonSearchMap:
                lat = AssignTabFragment.apiCaseScene.getTbNoticeCase().Latitude;
                lng = AssignTabFragment.apiCaseScene.getTbNoticeCase().Longitude;
                if (lat != null || lng != null) {
                    Log.d(TAG, "Go to Google map " + lat + " " + lng);

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    //Searches for 'Locale name' province amphur district
                    Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + editAddrDetail.getText().toString() + "+" + sDistrictName + "+" + sAmphurName + "+" + sProvinceName);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                break;
            case R.id.btnButtonSearchLatLong:

                break;
        }
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

    public class EmerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {

                case R.id.spinnerDistrict:
                    selectedDistrict = mDistrictArray[pos][0];
                    sDistrictName = mDistrictArray[pos][2].toString();
                    Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                    Log.i(TAG, AssignTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID);
                    break;
                case R.id.spinnerAmphur:
                    selectedAmphur = mAmphurArray[pos][0];
                    sAmphurName = mAmphurArray[pos][2].toString();
                    Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);
                    Log.i(TAG, AssignTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID);
                    //   AssignTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID = selectedAmphur[0];
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

                    Log.i(TAG, AssignTabFragment.apiCaseScene.getTbNoticeCase().PROVINCE_ID);
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
            switch (parent.getId()) {
                case R.id.spinnerDistrict:
                    selectedDistrict = mDistrictArray[0][0];
                    sDistrictName = mDistrictArray[0][2].toString();
                    Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);

                    break;
                case R.id.spinnerAmphur:
                    selectedAmphur = mAmphurArray[0][0];
                    sAmphurName = mAmphurArray[0][2].toString();
                    Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);

                    break;
                case R.id.spinnerProvince:
                    selectedProvince = mProvinceArray[0][0];
                    sProvinceName = mProvinceArray[0][2].toString();
                    Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);

                    break;
            }
        }
    }
}