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
    private AutoCompleteTextView autoCompleteDistrict, autoCompleteAmphur, autoCompleteProvince2;
    private EditText editAddrDetail, edtReportNo, editCircumstanceOfCaseDetail, edtVehicleDetail;
    private Button btnButtonSearchLatLong, btnButtonSearchMap;
    private Spinner spinnerDistrict, spinnerAmphur, spinnerProvince;

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
    String lat,lng;
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
        //Form

        edtReportNo = (EditText) viewReceiveCSI.findViewById(R.id.edtReportNo);
        // EmergencyTabFragment.tbNoticeCase.ReportNo = editTextPhone1.getText().toString();
        editTextPhone1 = (TextView) viewReceiveCSI.findViewById(R.id.editTextPhone);
//        if (EmergencyTabFragment.tbNoticeCase.CaseTel != "") {
//            editTextPhone1.setText(EmergencyTabFragment.tbNoticeCase.CaseTel);
//        }
        editTextPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //EmergencyTabFragment.tbNoticeCase.CaseTel = editTextPhone1.getText().toString();
            }
        });

        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);

        editAddrDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //     EmergencyTabFragment.tbNoticeCase.LocaleName = editAddrDetail.getText().toString();

            }
        });


        //datetime
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        editReceiveCaseDate.setEnabled(false);
       /* editReceiveCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickReceiveCaseDate", "null");
                DateDialog dialogReceiveCaseDate = new DateDialog(view);
                dialogReceiveCaseDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });*/
        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        editReceiveCaseTime.setEnabled(false);
        /*editReceiveCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickReceiveCaseTime", "null");
                TimeDialog dialogReceiveCaseTime = new TimeDialog(view);
                dialogReceiveCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
            }

        });*/
        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);

        editHappenCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickHappenCaseDate", "null");
                DateDialog dialogHappenCaseDate = new DateDialog(view);
                dialogHappenCaseDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });
        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);
        editHappenCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickHappenCaseTime", "null");
                TimeDialog dialogHappenCaseTime = new TimeDialog(view);
                dialogHappenCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
            }

        });
        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);
        editKnowCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Click KnowCaseTime", "null");
                DateDialog dialogKnowCaseDate = new DateDialog(view);
                dialogKnowCaseDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });
        editKnowCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseTime);
        editKnowCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickKnowCaseTime", "null");
                TimeDialog dialogKnowCaseTime = new TimeDialog(view);
                dialogKnowCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
            }

        });
        editSceneInvestDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editSceneInvestDate);
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
                // EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = selectedProvince[0];
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

                        // EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur[0];
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
                                //  EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict[0];
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                selectedDistrict[0] = mDistrictArray[0][0];
                                Log.i(TAG + " show selectedDistrict", selectedDistrict[0]);
                                //  EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict[0];
                            }
                        });
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedAmphur[0] = mAmphurArray[0][0];
                        Log.i(TAG + " show selectedAmphur", selectedAmphur[0]);
                        //  EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur[0];
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedProvince[0] = mProvinceArray[0][0];
                Log.i(TAG + " show selectedProvince", selectedProvince[0]);
                //  EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = selectedProvince[0];

            }
        });

        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);
        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(new SummaryOnClickListener());
        btnButtonSearchLatLong = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchLatLong);
        btnButtonSearchLatLong.setOnClickListener(new SummaryOnClickListener());

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);

        edtVehicleDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtVehicleDetail);


        fabBtnRec = (FloatingActionButton) viewReceiveCSI.findViewById(R.id.fabBtnRec);
        fabBtnRec.setOnClickListener(new SummaryOnClickListener());
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
//
//                if (EmergencyTabFragment.tbNoticeCase != null) {
//                    boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
//                    if (isSuccess) {
                if (snackbar == null || !snackbar.isShown()) {
                    snackbar = Snackbar.make(rootLayout, getString(R.string.save_complete) + " " + dateTimeCurrent[2]+" "+dateTimeCurrent[1]+" "+dateTimeCurrent[0], Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
                }
//                    }
//                }
            }
            if(v == btnButtonSearchMap){

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
            if(v == btnButtonSearchLatLong){
                lat = String.valueOf(mLastLocation.getLatitude());
                lng = String.valueOf(mLastLocation.getLongitude());
                Log.d(TAG, "Go to Google map " + lat + " " + lng);
                valueLat.setText(lat);
                valueLong.setText(lng);
               // EmergencyTabFragment.tbNoticeCase.Latitude = valueLat.getText().toString();
               // EmergencyTabFragment.tbNoticeCase.Longitude = valueLong.getText().toString();

            }
        }
    }
}