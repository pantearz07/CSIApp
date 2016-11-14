package com.scdc.csiapp.inqmain;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.DateDialog;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.SnackBarAlert;
import com.scdc.csiapp.main.TimeDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class EmergencyDetailTabFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // Google play services
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

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
    EditText editTextPhone1, editSuffererName;
    private String sAddrDetail, sDistrictName, sAmphurName, sProvinceName, provinceid, amphurid, districtid;
    private Spinner spinnerDistrict, spinnerAmphur, spinnerProvince;
    private String selectedProvince, selectedAmphur, selectedDistrict;
    String[][] mProvinceArray;
    String[] mProvinceArray2;
    String[][] mAmphurArray;
    String[] mAmphurArray2;
    String[][] mDistrictArray;
    String[] mDistrictArray2;

    private EditText editAddrDetail, editCircumstanceOfCaseDetail, editTextSuffererPhone;
    private ImageButton ic_telphone1, ic_telphone2;
    private Button btnButtonSearchMap, btnButtonSearchLatLong;
    String lat, lng;
    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ

    private TextView editReceiveCaseDate, editReceiveCaseTime;
    private TextView editHappenCaseDate, editHappenCaseTime;
    private TextView editKnowCaseDate, editKnowCaseTime, valueLat, valueLong;

    private static final String TAG = "DEBUG-EmergencyDetailTabFragment";

    View viewReceiveCSI;
    Context context;
    AutoCompleteTextView autoCompleteSuffererStatus, autoCompleteAntecedent;
    String[] Antecedent;
    boolean oldAntecedent, oldProvince, oldAmphur, oldDistrict = false;
    String address, amphur, province, country, postalCode, knownName = "null";

    boolean statusConnect = false;
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 10; //10 second

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

        // ทำการสร้างตัวเชื่อกับ Google services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
            Log.d(TAG, "Create Google services");
        }
        editTextSuffererPhone = (EditText) viewReceiveCSI.findViewById(R.id.editTextSuffererPhone);
        updateDT = getDateTime.getDateTimeNow();
        String noticecaseid = EmergencyTabFragment.tbNoticeCase.getNoticeCaseID();
        Log.i(TAG, " NoticeCaseID " + noticecaseid);
        //Show เวลาล่าสุดที่อัพเดต
        edtUpdateDateTime2 = (TextView) viewReceiveCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText(getString(R.string.updatedata) + " " +
                getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.LastUpdateDate)
                + " เวลา " + EmergencyTabFragment.tbNoticeCase.LastUpdateTime);
        //Show spinner สถานที่ตำรวจภูธร


        editTextPhone1 = (EditText) viewReceiveCSI.findViewById(R.id.editTextPhone);

        if (EmergencyTabFragment.tbNoticeCase.CaseTel == null || EmergencyTabFragment.tbNoticeCase.CaseTel.equals("")) {
            editTextPhone1.setText("");
        } else {
            editTextPhone1.setText(EmergencyTabFragment.tbNoticeCase.CaseTel);
        }
        editTextPhone1.addTextChangedListener(new EmerTextWatcher(editTextPhone1));
        ic_telphone1 = (ImageButton) viewReceiveCSI.findViewById(R.id.ic_telphone_1);
        ic_telphone1.setOnClickListener(this);

        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);
        //btn_clear_txt_1 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_1);
        if (EmergencyTabFragment.tbNoticeCase.LocaleName != "") {
            editAddrDetail.setText(EmergencyTabFragment.tbNoticeCase.LocaleName);
        }
        if (EmergencyTabFragment.tbNoticeCase.LocaleName == null || EmergencyTabFragment.tbNoticeCase.LocaleName.equals("")) {
            editAddrDetail.setText("");
        } else {
            editAddrDetail.setText(EmergencyTabFragment.tbNoticeCase.LocaleName);
        }
        editAddrDetail.addTextChangedListener(new EmerTextWatcher(editAddrDetail));

// /show spinner
        spinnerProvince = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerProvince);
        spinnerAmphur = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerAmphur);
        spinnerDistrict = (Spinner) viewReceiveCSI.findViewById(R.id.spinnerDistrict);
        amphurid = EmergencyTabFragment.tbNoticeCase.AMPHUR_ID;
        districtid = EmergencyTabFragment.tbNoticeCase.DISTRICT_ID;
        provinceid = EmergencyTabFragment.tbNoticeCase.PROVINCE_ID;

        mProvinceArray = dbHelper.SelectAllProvince();
        if (mProvinceArray != null) {
            final String[] mProvinceArray2 = new String[mProvinceArray.length];
            for (int i = 0; i < mProvinceArray.length; i++) {
                mProvinceArray2[i] = mProvinceArray[i][2];
//                Log.i(TAG + " show mProvinceArray2", mProvinceArray2[i].toString());
            }
            ArrayAdapter<String> adapterProvince = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mProvinceArray2);
            spinnerProvince.setAdapter(adapterProvince);
        } else {
            Log.i(TAG + " show mProvinceArray", "null");
        }
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
            setSelectAmphur(provinceid);
//            Log.i(TAG, " show  provinceid " + provinceid);
        }
        spinnerDistrict.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spinnerProvince.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spinnerAmphur.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spinnerDistrict.setOnTouchListener(new EmerOnItemSelectedListener());
        spinnerProvince.setOnTouchListener(new EmerOnItemSelectedListener());
        spinnerAmphur.setOnTouchListener(new EmerOnItemSelectedListener());


        //datetime
        String[] currentDT = getDateTime.updateDataDateTime();
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        if (EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate == null || EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate.equals("")
                || EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate.equals("0000-00-00")) {
            if (EmergencyTabFragment.mode == "view") {
                editReceiveCaseDate.setText("");
            } else {
                editReceiveCaseDate.setText(currentDT[0]);
            }
        } else {
            editReceiveCaseDate.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate));
        }
        editReceiveCaseDate.setOnClickListener(this);
        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        if (EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime == null || EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime.equals("")
                || EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime.equals("00:00:00")) {
            if (EmergencyTabFragment.mode == "view") {
                editReceiveCaseTime.setText("");

            } else {
                editReceiveCaseTime.setText(currentDT[1]);
            }
        } else {
            editReceiveCaseTime.setText(getDateTime.changeTimeFormatToDB(EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime));
        }


        editReceiveCaseTime.setOnClickListener(this);
        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);
        if (EmergencyTabFragment.tbNoticeCase.HappenCaseDate == null || EmergencyTabFragment.tbNoticeCase.HappenCaseDate.equals("")
                || EmergencyTabFragment.tbNoticeCase.HappenCaseDate.equals("0000-00-00")) {
            if (EmergencyTabFragment.mode == "view") {
                editHappenCaseDate.setText("");
            } else {
                editHappenCaseDate.setText(currentDT[0]);
            }
        } else {
            editHappenCaseDate.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.HappenCaseDate));
        }
        editHappenCaseDate.setOnClickListener(this);
        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);
        if (EmergencyTabFragment.tbNoticeCase.HappenCaseTime == null || EmergencyTabFragment.tbNoticeCase.HappenCaseTime.equals("")
                || EmergencyTabFragment.tbNoticeCase.HappenCaseTime.equals("00:00:00")) {
            if (EmergencyTabFragment.mode == "view") {
                editHappenCaseTime.setText("");
            } else {
                editHappenCaseTime.setText(currentDT[1]);
            }
        } else {
            editHappenCaseTime.setText(getDateTime.changeTimeFormatToDB(EmergencyTabFragment.tbNoticeCase.HappenCaseTime));
        }
        editHappenCaseTime.setOnClickListener(this);

        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);
        if (EmergencyTabFragment.tbNoticeCase.KnowCaseDate == null || EmergencyTabFragment.tbNoticeCase.KnowCaseDate.equals("")
                || EmergencyTabFragment.tbNoticeCase.KnowCaseDate.equals("0000-00-00")) {
            if (EmergencyTabFragment.mode == "view") {
                editKnowCaseDate.setText("");
            } else {
                editKnowCaseDate.setText(currentDT[0]);
            }
        } else {
            editKnowCaseDate.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.KnowCaseDate));
        }

        editKnowCaseDate.setOnClickListener(this);
        editKnowCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseTime);

        if (EmergencyTabFragment.tbNoticeCase.KnowCaseTime == null || EmergencyTabFragment.tbNoticeCase.KnowCaseTime.equals("")
                || EmergencyTabFragment.tbNoticeCase.KnowCaseTime.equals("00:00:00")) {
            if (EmergencyTabFragment.mode == "view") {
                editKnowCaseTime.setText("");
            } else {
                editKnowCaseTime.setText(currentDT[1]);
            }
        } else {
            editKnowCaseTime.setText(getDateTime.changeTimeFormatToDB(EmergencyTabFragment.tbNoticeCase.KnowCaseTime));
        }

        editKnowCaseTime.setOnClickListener(this);

        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);
        if (EmergencyTabFragment.tbNoticeCase.Latitude == null || EmergencyTabFragment.tbNoticeCase.Latitude.equals("")) {
            valueLat.setText("");
        } else {
            valueLat.setText(EmergencyTabFragment.tbNoticeCase.Latitude);
            lat = EmergencyTabFragment.tbNoticeCase.Latitude;
        }
        if (EmergencyTabFragment.tbNoticeCase.Longitude == null || EmergencyTabFragment.tbNoticeCase.Longitude.equals("")) {
            valueLong.setText("");
        } else {
            valueLong.setText(EmergencyTabFragment.tbNoticeCase.Longitude);
            lng = EmergencyTabFragment.tbNoticeCase.Longitude;
        }

        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(this);
        btnButtonSearchLatLong = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchLatLong);
        btnButtonSearchLatLong.setOnClickListener(this);

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        if (EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail == null || EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail.equals("")) {
            editCircumstanceOfCaseDetail.setText("");

        } else {
            editCircumstanceOfCaseDetail.setText(EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail);

        }
        editCircumstanceOfCaseDetail.addTextChangedListener(new EmerTextWatcher(editCircumstanceOfCaseDetail));

        autoCompleteAntecedent = (AutoCompleteTextView) viewReceiveCSI.findViewById(R.id.autoCompleteAntecedent);
        Antecedent = getResources().getStringArray(R.array.antecedent);
        ArrayAdapter<String> adapterEnglish = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Antecedent);
        autoCompleteAntecedent.setAdapter(adapterEnglish);
        if (EmergencyTabFragment.tbNoticeCase.SuffererPrename == null || EmergencyTabFragment.tbNoticeCase.SuffererPrename.equals("")) {
            autoCompleteAntecedent.setText("");
        } else {
            autoCompleteAntecedent.setText(EmergencyTabFragment.tbNoticeCase.SuffererPrename);
        }
        autoCompleteAntecedent.addTextChangedListener(new EmerTextWatcher(autoCompleteAntecedent));

        editSuffererName = (EditText) viewReceiveCSI.findViewById(R.id.editSuffererName);
        if (EmergencyTabFragment.tbNoticeCase.SuffererName == null || EmergencyTabFragment.tbNoticeCase.SuffererName.equals("")) {
            editSuffererName.setText("");
        } else {
            editSuffererName.setText(EmergencyTabFragment.tbNoticeCase.SuffererName);
        }
        editSuffererName.addTextChangedListener(new EmerTextWatcher(editSuffererName));

        autoCompleteSuffererStatus = (AutoCompleteTextView) viewReceiveCSI.findViewById(R.id.autoCompleteSuffererStatus);
        final String[] SuffererStatus = getResources().getStringArray(R.array.suffererStatus);
        ArrayAdapter<String> adapterSuffererStatus = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SuffererStatus);
        autoCompleteSuffererStatus.setAdapter(adapterSuffererStatus);
        if (EmergencyTabFragment.tbNoticeCase.SuffererStatus == null || EmergencyTabFragment.tbNoticeCase.SuffererStatus.equals("")) {
            autoCompleteSuffererStatus.setText("");
        } else {
            autoCompleteSuffererStatus.setText(EmergencyTabFragment.tbNoticeCase.SuffererStatus);
        }
        autoCompleteSuffererStatus.addTextChangedListener(new EmerTextWatcher(autoCompleteSuffererStatus));


        if (EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum == null || EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum.equals("")) {
            editTextSuffererPhone.setText("");
        } else {
            editTextSuffererPhone.setText(EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum);
        }
        editTextSuffererPhone.addTextChangedListener(new EmerTextWatcher(editTextSuffererPhone));
        ic_telphone2 = (ImageButton) viewReceiveCSI.findViewById(R.id.ic_telphone_2);
        ic_telphone2.setOnClickListener(this);

        fabBtnRec = (FloatingActionButton) viewReceiveCSI.findViewById(R.id.fabBtnRec);
        if (emergencyTabFragment.mode == "view") {
            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtnRec.setLayoutParams(p);
            fabBtnRec.hide();
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

            autoCompleteAntecedent.setEnabled(false);
            editSuffererName.setEnabled(false);
            autoCompleteSuffererStatus.setEnabled(false);
            editTextSuffererPhone.setEnabled(false);
            editCircumstanceOfCaseDetail.setEnabled(false);
        }

        fabBtnRec.setOnClickListener(this);

        return viewReceiveCSI;
    }

    private void setSelectAmphur(String provinceid) {
        mAmphurArray = dbHelper.SelectAmphur(provinceid);
        if (mAmphurArray != null) {
            String[] mAmphurArray2 = new String[mAmphurArray.length];
            for (int i = 0; i < mAmphurArray.length; i++) {
                mAmphurArray2[i] = mAmphurArray[i][2];
//                Log.i(TAG + " show mAmphurArray2", mAmphurArray2[i].toString());
            }
            ArrayAdapter<String> adapterAmphur = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, mAmphurArray2);
            spinnerAmphur.setAdapter(adapterAmphur);
        } else {
            spinnerAmphur.setAdapter(null);
            selectedAmphur = null;
//            Log.i(TAG + " show mAmphurArray", String.valueOf(selectedAmphur));
        }
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
                hiddenKeyboard();
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                if (editHappenCaseDate.getText().toString() == null || editHappenCaseDate.getText().toString().equals("")) {
                    EmergencyTabFragment.tbNoticeCase.HappenCaseDate = "";
                } else {
                    EmergencyTabFragment.tbNoticeCase.HappenCaseDate = getDateTime.changeDateFormatToDB(editHappenCaseDate.getText().toString());
                }
                if (editHappenCaseTime.getText().toString() == null || editHappenCaseTime.getText().toString().equals("")) {
                    EmergencyTabFragment.tbNoticeCase.HappenCaseTime = "";
                } else {
                    EmergencyTabFragment.tbNoticeCase.HappenCaseTime = editHappenCaseTime.getText().toString();
                }
                if (editReceiveCaseDate.getText().toString() == null || editReceiveCaseDate.getText().toString().equals("")) {
                    EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate = "";
                } else {
                    EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate = getDateTime.changeDateFormatToDB(editReceiveCaseDate.getText().toString());
                }
                if (editReceiveCaseTime.getText().toString() == null || editReceiveCaseTime.getText().toString().equals("")) {
                    EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime = "";
                } else {
                    EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime = editReceiveCaseTime.getText().toString();
                }
                if (editKnowCaseDate.getText().toString() == null || editKnowCaseDate.getText().toString().equals("")) {
                    EmergencyTabFragment.tbNoticeCase.KnowCaseDate = "";
                } else {
                    EmergencyTabFragment.tbNoticeCase.KnowCaseDate = getDateTime.changeDateFormatToDB(editKnowCaseDate.getText().toString());
                }
                if (editKnowCaseTime.getText().toString() == null || editKnowCaseTime.getText().toString().equals("")) {
                    EmergencyTabFragment.tbNoticeCase.KnowCaseTime = "";
                } else {
                    EmergencyTabFragment.tbNoticeCase.KnowCaseTime = editKnowCaseTime.getText().toString();
                }


                EmergencyTabFragment.tbNoticeCase.LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                EmergencyTabFragment.tbNoticeCase.LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                if (EmergencyTabFragment.tbNoticeCase != null) {
                    boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
                    if (isSuccess) {
                        if (snackbar == null || !snackbar.isShown()) {
                            SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                                    getString(R.string.save_complete)
                                            + " " +EmergencyTabFragment.tbNoticeCase.LastUpdateDate.toString()
                                            + " " +EmergencyTabFragment.tbNoticeCase.LastUpdateTime.toString());
                            snackBarAlert.createSnacbar();
                        }
                    }
                }
                break;
            case R.id.btnButtonSearchMap:
                hiddenKeyboard();
                if (cd.isNetworkAvailable()) {
                    if (lat != null || lng != null) {
//                        Log.d(TAG, "Go to Google map " + lat + " " + lng);

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } else {
                        //Searches for 'Locale name' province amphur district
                        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q="
                                + EmergencyTabFragment.tbNoticeCase.LocaleName + "+" + sDistrictName + "+" + sAmphurName + "+" + sProvinceName);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);

                    }
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnButtonSearchLatLong:
                hiddenKeyboard();
                if (cd.isNetworkAvailable()) {
                    lat = String.valueOf(mLastLocation.getLatitude());
                    lng = String.valueOf(mLastLocation.getLongitude());
//                    Log.d(TAG, "Go to Google map " + lat + " " + lng);
                    valueLat.setText(lat);
                    valueLong.setText(lng);
                    EmergencyTabFragment.tbNoticeCase.Latitude = lat;
                    EmergencyTabFragment.tbNoticeCase.Longitude = lng;
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editReceiveCaseDate:
                Log.i("Click ReceiveCaseDate", "null");
                DateDialog dialogReceiveCaseDate = new DateDialog(view);
                dialogReceiveCaseDate.show(getActivity().getFragmentManager(), "Date Picker");
                break;
            case R.id.editReceiveCaseTime:
                Log.i("ClickReceiveCaseTime", "null");
                TimeDialog dialogReceiveCaseTime = new TimeDialog(view);
                dialogReceiveCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
                break;
            case R.id.editHappenCaseDate:
                Log.i("ClickHappenCaseDate", "null");
                DateDialog dialogHappenCaseDate = new DateDialog(view);
                dialogHappenCaseDate.show(getActivity().getFragmentManager(), "Date Picker");
                break;
            case R.id.editHappenCaseTime:
                Log.i("ClickHappenCaseTime", "null");
                TimeDialog dialogHappenCaseTime = new TimeDialog(view);
                dialogHappenCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
                break;
            case R.id.editKnowCaseDate:
                Log.i("Click KnowCaseDate", "null");
                DateDialog dialogKnowCaseDate = new DateDialog(view);
                dialogKnowCaseDate.show(getActivity().getFragmentManager(), "Date Picker");
                break;
            case R.id.editKnowCaseTime:
                Log.i("Click KnowCaseTime", "null");
                TimeDialog dialogKnowCaseTime = new TimeDialog(view);
                dialogKnowCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
                break;
            case R.id.ic_telphone_1:
                hiddenKeyboard();
                calling(EmergencyTabFragment.tbNoticeCase.CaseTel);
                break;
            case R.id.ic_telphone_2:
                hiddenKeyboard();
                calling(EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum);
                break;
        }
    }
    // Intent action_call for telephone
    private void calling(String sPhonenumber) {
        if (sPhonenumber == null || sPhonenumber.equals("")) {

        } else {
            try {
                Log.i(TAG, "Calling a Phone Number " + sPhonenumber);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:=" + sPhonenumber.replace(" ", "").trim()));
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            } catch (ActivityNotFoundException activityException) {
                Log.e("Calling a Phone Number", "Call failed", activityException);
            }
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
            Log.d(TAG, "get mLastLocation " + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());

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
        Log.d(TAG, "Location " + location.getLatitude() + " " + location.getLongitude());
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        if (cd.isNetworkAvailable()) {
            List<Address> addresses = null;
            try {
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (addresses != null && addresses.size() > 0) {


                    address = addresses.get(0).getAddressLine(0);
                    // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    amphur = addresses.get(0).getLocality();
                    province = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    postalCode = addresses.get(0).getPostalCode();
                    knownName = addresses.get(0).getFeatureName();

                    Log.d(TAG, "get mLastLocation address" + address);
                    Log.d(TAG, "get mLastLocation amphur " + amphur);
                    Log.d(TAG, "get mLastLocation province " + province);
                    Log.d(TAG, "get mLastLocation country " + country);
                    Log.d(TAG, "get mLastLocation postalCode " + postalCode);
                    Log.d(TAG, "get mLastLocation knownName " + knownName);

                }
                if (provinceid == null || provinceid.equals("") || provinceid.equals("null")) {
                    if (province != null || province != "null") {
                        for (int i = 0; i < mProvinceArray.length; i++) {
                            if (province.trim().equals(mProvinceArray[i][2])) {
                                spinnerProvince.setSelection(i);
                                provinceid = mProvinceArray[i][0];

                                oldProvince = false;
                                break;
                            }
                        }
//                        Log.i(TAG, " show province " + province + " provinceid " + provinceid);

                        if (provinceid != null) {
                            setSelectAmphur(provinceid);

                            EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = provinceid;
                            Log.i(TAG, EmergencyTabFragment.tbNoticeCase.PROVINCE_ID);
                        }
                    } else {
                        spinnerProvince.setSelection(0);
                    }
                }
                if (amphurid == null || amphurid.equals("") || amphurid.equals("null")) {

                    if (amphur != null || amphur != "null") {
                        amphur = amphur.replace("อำเภอ", "");
//                        Log.i(TAG, "have amphur" + amphur);
                        for (int i = 0; i < mAmphurArray.length; i++) {
                            if (amphur.trim().equals(mAmphurArray[i][2].toString())) {
                                spinnerAmphur.setSelection(i);
                                amphurid = mAmphurArray[i][0];
                                sAmphurName = mAmphurArray[i][2].toString();

                                break;
                            }
                        }
//                        Log.i(TAG, "have amphur from location " + amphurid + " " + sAmphurName);

                        if (amphurid != null) {
                            EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = amphurid;
                            Log.i(TAG, EmergencyTabFragment.tbNoticeCase.AMPHUR_ID);
                        }
                    } else {
                        spinnerAmphur.setSelection(0);
                    }
                }
                if (districtid == null || districtid.equals("") || districtid.equals("null")) {

                    if (knownName != null || knownName != "null" || knownName != "Unnamed Road") {
                        knownName = knownName.replace("ตำบล", "");
                        Log.i(TAG, "have knownName" + knownName.trim());
                        for (int i = 0; i < mDistrictArray.length; i++) {
                            if (knownName.trim().equals(mDistrictArray[i][2].toString())) {
                                spinnerDistrict.setSelection(i);
                                districtid = mDistrictArray[i][0];
                                sDistrictName = mDistrictArray[i][2].toString();
                                break;
                            }
                        }
//                        Log.i(TAG, "have knownName from location " + districtid + " " + sDistrictName);

                        if (districtid != null) {
                            EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = districtid;
                            Log.i(TAG, EmergencyTabFragment.tbNoticeCase.DISTRICT_ID);
                        }
                    } else {
                        spinnerDistrict.setSelection(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "get mLastLocation error" + e.getMessage());
            }
        }
    }

    public class EmerOnItemSelectedListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (view == spinnerProvince) {
                oldProvince = false;
            }

            return false;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {

                case R.id.spinnerDistrict:
                    if (districtid == null || districtid.equals("") || districtid.equals("null")) {
                        selectedDistrict = mDistrictArray[pos][0];
                        sDistrictName = mDistrictArray[pos][2].toString();
                        Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                        EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict;
                        Log.i(TAG, EmergencyTabFragment.tbNoticeCase.DISTRICT_ID);
                    } else {
                        for (int i = 0; i < mDistrictArray.length; i++) {
                            if (districtid.trim().equals(mDistrictArray[i][0].toString())) {
                                sDistrictName = mDistrictArray[i][2].toString();
                                selectedDistrict = mDistrictArray[i][0];
                                spinnerDistrict.setSelection(i);
                                EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict;
                                EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict;
//                                Log.i(TAG, EmergencyTabFragment.tbNoticeCase.DISTRICT_ID);
                                break;
                            }
                        }
                    }
                    break;
                case R.id.spinnerAmphur:
                    if (amphurid == null || amphurid.equals("") || amphurid.equals("null")) {

                        selectedAmphur = mAmphurArray[pos][0];
                        sAmphurName = mAmphurArray[pos][2].toString();
//                        Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);
                        EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur;
//                        Log.i(TAG, EmergencyTabFragment.tbNoticeCase.AMPHUR_ID);

                    } else {
                        for (int i = 0; i < mAmphurArray.length; i++) {
                            if (amphurid.trim().equals(mAmphurArray[i][0].toString())) {
                                spinnerAmphur.setSelection(i);
                                selectedAmphur = mAmphurArray[i][0];
                                sAmphurName = mAmphurArray[i][2].toString();
//                                Log.i(TAG, " show selectedAmphur" + selectedAmphur + " " + sAmphurName);
                                EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur;
//                                Log.i(TAG, EmergencyTabFragment.tbNoticeCase.AMPHUR_ID);
                                break;
                            }
                        }
                    }
                    //ดึงค่า District
                    mDistrictArray = dbHelper.SelectDistrict(selectedAmphur);
                    if (mDistrictArray != null) {
                        String[] mDistrictArray2 = new String[mDistrictArray.length];
                        for (int i = 0; i < mDistrictArray.length; i++) {
                            mDistrictArray2[i] = mDistrictArray[i][2];
                        }
                        ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_dropdown_item_1line, mDistrictArray2);
                        spinnerDistrict.setAdapter(adapterDistrict);
                    } else {
                        spinnerDistrict.setAdapter(null);
                        selectedDistrict = null;
                    }

                    break;
                case R.id.spinnerProvince:
                    if (oldProvince == false) {
                        selectedProvince = mProvinceArray[pos][0];
                        sProvinceName = mProvinceArray[pos][2].toString();
//                        Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);
                        EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = selectedProvince;
//                        Log.i(TAG, EmergencyTabFragment.tbNoticeCase.PROVINCE_ID);
                        //provinceid = selectedProvince[0];
                        //ดึงค่า amphur
                        mAmphurArray = dbHelper.SelectAmphur(selectedProvince);
                        if (mAmphurArray != null) {
                            String[] mAmphurArray2 = new String[mAmphurArray.length];
                            for (int i = 0; i < mAmphurArray.length; i++) {
                                mAmphurArray2[i] = mAmphurArray[i][2];
//                                Log.i(TAG + " show mAmphurArray2", mAmphurArray2[i].toString());
                            }
                            ArrayAdapter<String> adapterAmphur = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, mAmphurArray2);
                            spinnerAmphur.setAdapter(adapterAmphur);
                        } else {
                            spinnerAmphur.setAdapter(null);
                            selectedAmphur = null;
//                            Log.i(TAG + " show mAmphurArray", String.valueOf(selectedAmphur));
                        }

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
//                    Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                    EmergencyTabFragment.tbNoticeCase.DISTRICT_ID = selectedDistrict;
                    break;
                case R.id.spinnerAmphur:
                    selectedAmphur = mAmphurArray[0][0];
                    sAmphurName = mAmphurArray[0][2].toString();
//                    Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);
                    EmergencyTabFragment.tbNoticeCase.AMPHUR_ID = selectedAmphur;
                    break;
                case R.id.spinnerProvince:
                    selectedProvince = mProvinceArray[0][0];
                    sProvinceName = mProvinceArray[0][2].toString();
//                    Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);
                    EmergencyTabFragment.tbNoticeCase.PROVINCE_ID = selectedProvince;
                    break;
            }
        }
    }

    private class EmerTextWatcher implements TextWatcher {
        private EditText mEditText;

        public EmerTextWatcher(EditText editText) {

            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == editTextPhone1.getEditableText()) {
                EmergencyTabFragment.tbNoticeCase.CaseTel = editTextPhone1.getText().toString();
                ic_telphone1.setEnabled(true);
                Log.i(TAG, "CaseTel " + EmergencyTabFragment.tbNoticeCase.CaseTel);
            } else if (s == editAddrDetail.getEditableText()) {
                EmergencyTabFragment.tbNoticeCase.LocaleName = editAddrDetail.getText().toString();
//                Log.i(TAG, "LocaleName " + EmergencyTabFragment.tbNoticeCase.LocaleName);
            } else if (s == editSuffererName.getEditableText()) {
                EmergencyTabFragment.tbNoticeCase.SuffererName = editSuffererName.getText().toString();
//                Log.i(TAG, "SuffererName " + EmergencyTabFragment.tbNoticeCase.SuffererName);
            } else if (s == autoCompleteSuffererStatus.getEditableText()) {
                EmergencyTabFragment.tbNoticeCase.SuffererStatus = autoCompleteSuffererStatus.getText().toString();
//                Log.i(TAG, "SuffererStatus " + EmergencyTabFragment.tbNoticeCase.SuffererStatus);
            } else if (s == editTextSuffererPhone.getEditableText()) {
                EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum = editTextSuffererPhone.getText().toString();
                ic_telphone2.setEnabled(true);
                Log.i(TAG, "SuffererPhoneNum " + EmergencyTabFragment.tbNoticeCase.SuffererPhoneNum);
            } else if (s == editCircumstanceOfCaseDetail.getEditableText()) {
                EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();
//                Log.i(TAG, "CircumstanceOfCaseDetail " + EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail);
            } else if (s == autoCompleteAntecedent.getEditableText()) {
                EmergencyTabFragment.tbNoticeCase.SuffererPrename = autoCompleteAntecedent.getText().toString();
//                Log.i(TAG, "CircumstanceOfCaseDetail " + EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail);
            }
        }
    }

    public void hiddenKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}