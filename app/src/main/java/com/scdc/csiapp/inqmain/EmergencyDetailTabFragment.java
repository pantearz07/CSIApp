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


public class EmergencyDetailTabFragment extends Fragment {
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
    private String sAddrDetail,
            sDistrict, sAmphur, sProvinceName, sLatitude,
            sLongitude;
    private Spinner spinnerDistrict, spinnerAmphur, spinnerProvince;
    private EditText editAddrDetail, editCircumstanceOfCaseDetail, edtVehicleDetail;
    private Button btnButtonSearchMap, btnButtonSearchLatLong;

    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ

    private TextView editReceiveCaseDate, editReceiveCaseTime, editScheduleInvestDate;
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

// /layout 3 address
        spinnerProvince = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerProvince);
        spinnerAmphur = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAmphur);
        spinnerDistrict = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerDistrict);
        final String[] selectedProvince = new String[1];
        final String[] selectedAmphur = new String[1];
        final String[] selectedDistrict = new String[1];
        final String mProvinceArray[][] = dbHelper.SelectAllProvince();
        if (mProvinceArray != null) {
            String[] mProvinceArray2 = new String[mProvinceArray.length];
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
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedProvince[0] = mProvinceArray[position][0];
                Log.i(TAG + " show selectedProvince", selectedProvince[0]);
                EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = selectedProvince[0];
                //ดึงค่า amphur
                final String mAmphurArray[][] = dbHelper.SelectAmphur(selectedProvince[0]);
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
                    selectedAmphur[0] = null;
                    Log.i(TAG + " show mAmphurArray", String.valueOf(selectedAmphur[0]));
                }

                spinnerAmphur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedAmphur[0] = mAmphurArray[position][0];
                        Log.i(TAG + " show selectedAmphur", selectedAmphur[0]);

                        EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur[0];
                        //ดึงค่า District

                        final String mDistrictArray[][] = dbHelper.SelectDistrict(selectedAmphur[0]);
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
                            selectedDistrict[0] = null;
                            Log.i(TAG + " show selectedDistrict", String.valueOf(selectedDistrict[0]));
                        }

                        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedDistrict[0] = mDistrictArray[position][0];
                                Log.i(TAG + " show selectedDistrict", selectedDistrict[0]);
                                EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict[0];
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                selectedDistrict[0] = mDistrictArray[0][0];
                                Log.i(TAG + " show selectedDistrict", selectedDistrict[0]);
                                EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict[0];
                            }
                        });
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedAmphur[0] = mAmphurArray[0][0];
                        Log.i(TAG + " show selectedAmphur", selectedAmphur[0]);
                        EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur[0];
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedProvince[0] = mProvinceArray[0][0];
                Log.i(TAG + " show selectedProvince", selectedProvince[0]);
                EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = selectedProvince[0];

            }
        });
        //datetime
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        editReceiveCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateDialog dialogReceiveCaseDate = new DateDialog(view);
                dialogReceiveCaseDate.show(getActivity().getFragmentManager(), "Date Picker");


            }

        });
        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        editReceiveCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeDialog dialogReceiveCaseTime = new TimeDialog(view);
                dialogReceiveCaseTime.show(getActivity().getFragmentManager(), "Time Picker");

            }

        });
        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);
        editHappenCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateDialog dialogHappenCaseDate = new DateDialog(view);
                dialogHappenCaseDate.show(getActivity().getFragmentManager(), "Date Picker");


            }

        });
        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);
        editHappenCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeDialog dialogHappenCaseTime = new TimeDialog(view);
                dialogHappenCaseTime.show(getActivity().getFragmentManager(), "Time Picker");

            }


        });

        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);
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
        editKnowCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickKnowCaseTime", editKnowCaseTime.getText().toString());
                TimeDialog dialogKnowCaseTime = new TimeDialog(view);
                dialogKnowCaseTime.show(getActivity().getFragmentManager(), "Time Picker");


            }

        });

        Log.d(TAG, "Go to Google map " + EmergencyTabFragment.tbNoticeCase.Latitude + " " + EmergencyTabFragment.tbNoticeCase.Latitude);
        final String lat = EmergencyTabFragment.tbNoticeCase.Latitude;
        final String lng = EmergencyTabFragment.tbNoticeCase.Longitude;
        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Go to Google map " + lat + " " + lng);
                // Searches for 'Main Street' near San Francisco
//                Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=101+main+street");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
        btnButtonSearchLatLong = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchLatLong);
        // btnButtonSearchMap.setOnClickListener(new ReceiveOnClickListener());
        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);
//        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            Log.d(TAG, "get mLastLocation");
//
//
////            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        }
        EmergencyTabFragment.tbNoticeCase.Latitude = "0";
        EmergencyTabFragment.tbNoticeCase.Longitude = "0";
        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
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
        spinnerAmphur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmergencyTabFragment.tbNoticeCase.SuffererPrename = String.valueOf(spinnerAntecedent.getSelectedItem());
                Log.i(TAG, "spinnerAntecedent " + EmergencyTabFragment.tbNoticeCase.SuffererPrename);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                EmergencyTabFragment.tbNoticeCase.SuffererPrename = String.valueOf(spinnerAntecedent.getSelectedItem());

                Log.i(TAG, "spinnerAntecedent " + EmergencyTabFragment.tbNoticeCase.SuffererPrename);
            }
        });
        final EditText editSuffererName = (EditText) viewReceiveCSI.findViewById(R.id.editSuffererName);
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

        final EditText editTextSuffererPhone = (EditText) viewReceiveCSI.findViewById(R.id.editTextSuffererPhone);
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

}