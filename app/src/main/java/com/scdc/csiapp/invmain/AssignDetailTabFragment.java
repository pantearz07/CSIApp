package com.scdc.csiapp.invmain;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiInvestigatorsInScene;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class AssignDetailTabFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // Google play services
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    boolean statusConnect = false;
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
    boolean oldAntecedent, oldProvince, oldAmphur, oldDistrict = false;
    String[] Antecedent;
    private EditText editAddrDetail, editCircumstanceOfCaseDetail, edtVehicleDetail, editSuffererName, editTextSuffererPhone;
    AutoCompleteTextView autoCompleteSuffererStatus, autoCompleteAntecedent;
    private Button btnButtonSearchMap, btnButtonSearchLatLong;
    String lat, lng;
    private ImageButton ic_telphone1, ic_telphone2;
    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ

    private TextView editReceiveCaseDate, editReceiveCaseTime;
    private TextView editHappenCaseDate, editHappenCaseTime;
    private TextView editKnowCaseDate, editKnowCaseTime, valueLat, valueLong;

    private static final String TAG = "DEBUG-AssignDetailTabFragment";
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 10; //10 second
    private View viewCSI;
    Context context;
    ListView listViewInvestigator;
    List<ApiInvestigatorsInScene> apiInvestigatorsInScenes = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        viewCSI = inflater.inflate(R.layout.emergency_tab_layout, null);
        context = viewCSI.getContext();
        rootLayout = (CoordinatorLayout) viewCSI.findViewById(R.id.rootLayoutReceive);
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
//            Log.d(TAG, "Create Google services");
        }

        updateDT = getDateTime.getDateTimeNow();
        String noticecaseid = AssignTabFragment.apiCaseScene.getTbNoticeCase().getNoticeCaseID();
//        Log.i(TAG, " NoticeCaseID " + noticecaseid);
        //Show เวลาล่าสุดที่อัพเดต
        edtUpdateDateTime2 = (TextView) viewCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText(getString(R.string.updatedata) + " " +
                getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate)
                + " เวลา " + AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime);
        //Show spinner สถานที่ตำรวจภูธร


        editTextPhone1 = (EditText) viewCSI.findViewById(R.id.editTextPhone);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTel == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTel.equals("")) {
            editTextPhone1.setText("");
        } else {
            editTextPhone1.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTel);
        }

        ic_telphone1 = (ImageButton) viewCSI.findViewById(R.id.ic_telphone_1);
        ic_telphone1.setOnClickListener(this);
        editAddrDetail = (EditText) viewCSI.findViewById(R.id.edtAddrDetail);
        //btn_clear_txt_1 = (Button) viewCSI.findViewById(R.id.btn_clear_txt_1);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().LocaleName == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().LocaleName.equals("")) {
            editAddrDetail.setText("");
        } else {
            editAddrDetail.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().LocaleName);
        }

// /show spinner
        spinnerProvince = (Spinner) viewCSI.findViewById(R.id.spinnerProvince);
        spinnerAmphur = (Spinner) viewCSI.findViewById(R.id.spinnerAmphur);
        spinnerDistrict = (Spinner) viewCSI.findViewById(R.id.spinnerDistrict);
        amphurid = AssignTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID;
        districtid = AssignTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID;
        provinceid = AssignTabFragment.apiCaseScene.getTbNoticeCase().PROVINCE_ID;

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
//            Log.i(TAG + " show mProvinceArray", "null");
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
            mAmphurArray = dbHelper.SelectAmphur(provinceid);
            if (mAmphurArray != null) {
                String[] mAmphurArray2 = new String[mAmphurArray.length];
                for (int i = 0; i < mAmphurArray.length; i++) {
                    mAmphurArray2[i] = mAmphurArray[i][2];
//                    Log.i(TAG + " show mAmphurArray2", mAmphurArray2[i].toString());
                }
                ArrayAdapter<String> adapterAmphur = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, mAmphurArray2);
                spinnerAmphur.setAdapter(adapterAmphur);
            } else {
                spinnerAmphur.setAdapter(null);
                selectedAmphur = null;
//                Log.i(TAG + " show mAmphurArray", String.valueOf(selectedAmphur));
            }
        }
        if (amphurid == null || amphurid.equals("") || amphurid.equals("null")) {
            spinnerAmphur.setSelection(0);
        } else {
            for (int i = 0; i < mAmphurArray.length; i++) {
                if (amphurid.trim().equals(mAmphurArray[i][0].toString())) {
                    spinnerAmphur.setSelection(i);
                    break;
                }
            }
            mDistrictArray = dbHelper.SelectDistrict(amphurid);
            if (mDistrictArray != null) {
                String[] mDistrictArray2 = new String[mDistrictArray.length];
                for (int i = 0; i < mDistrictArray.length; i++) {
                    mDistrictArray2[i] = mDistrictArray[i][2];
//                    Log.i(TAG + " show mDistrictArray2", mDistrictArray2[i].toString());
                }
                ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, mDistrictArray2);
                spinnerDistrict.setAdapter(adapterDistrict);
            } else {
                spinnerDistrict.setAdapter(null);
                selectedDistrict = null;
//                Log.i(TAG + " show selectedDistrict", String.valueOf(selectedDistrict));
            }
        }
        if (districtid == null || districtid.equals("") || districtid.equals("null")) {
            spinnerDistrict.setSelection(0);
        } else {
            for (int i = 0; i < mDistrictArray.length; i++) {
                if (districtid.trim().equals(mDistrictArray[i][0].toString())) {
                    spinnerDistrict.setSelection(i);
                    break;
                }
            }
        }


        //datetime
        editReceiveCaseDate = (TextView) viewCSI
                .findViewById(R.id.editReceiveCaseDate);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseDate == null
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseDate.equals("")
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseDate.equals("0000-00-00")) {
            editReceiveCaseDate.setText("");
        } else {
            editReceiveCaseDate.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseDate));
        }

        editReceiveCaseTime = (TextView) viewCSI
                .findViewById(R.id.editReceiveCaseTime);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseTime == null
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseTime.equals("")
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseTime.equals("00:00:00")) {
            editReceiveCaseTime.setText("");
        } else {
            editReceiveCaseTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().ReceivingCaseTime));
        }


        editHappenCaseDate = (TextView) viewCSI
                .findViewById(R.id.editHappenCaseDate);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseDate == null
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseDate.equals("")
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseDate.equals("0000-00-00")) {
            editHappenCaseDate.setText("");
        } else {
            editHappenCaseDate.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseDate));
        }

        editHappenCaseTime = (TextView) viewCSI
                .findViewById(R.id.editHappenCaseTime);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseTime == null
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseTime.equals("")
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseTime.equals("00:00:00")) {
            editHappenCaseTime.setText("");
        } else {
            editHappenCaseTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().HappenCaseTime));
        }

        editKnowCaseDate = (TextView) viewCSI
                .findViewById(R.id.editKnowCaseDate);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseDate == null
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseDate.equals("")
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseDate.equals("0000-00-00")) {
            editKnowCaseDate.setText("");
        } else {
            editKnowCaseDate.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseDate));
        }

        editKnowCaseTime = (TextView) viewCSI
                .findViewById(R.id.editKnowCaseTime);

        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseTime == null
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseTime.equals("")
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseTime.equals("00:00:00")) {
            editKnowCaseTime.setText("");
        } else {
            editKnowCaseTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().KnowCaseTime));
        }


        valueLat = (TextView) viewCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewCSI.findViewById(R.id.valueLong);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().Latitude == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().Latitude.equals("")) {
            valueLat.setText("");
        } else {
            valueLat.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().Latitude);
        }
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().Longitude == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().Longitude.equals("")) {
            valueLong.setText("");
        } else {
            valueLong.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().Longitude);

        }

        btnButtonSearchMap = (Button) viewCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(this);
        btnButtonSearchLatLong = (Button) viewCSI.findViewById(R.id.btnButtonSearchLatLong);
        btnButtonSearchLatLong.setOnClickListener(this);

        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);

        editCircumstanceOfCaseDetail = (EditText) viewCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail == null ||
                AssignTabFragment.apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail.equals("")) {
            editCircumstanceOfCaseDetail.setText("");
        } else {
            editCircumstanceOfCaseDetail.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().CircumstanceOfCaseDetail);
        }

        autoCompleteAntecedent = (AutoCompleteTextView) viewCSI.findViewById(R.id.autoCompleteAntecedent);
        Antecedent = getResources().getStringArray(R.array.antecedent);
        ArrayAdapter<String> adapterEnglish = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Antecedent);
        autoCompleteAntecedent.setAdapter(adapterEnglish);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPrename == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPrename.equals("")) {
            autoCompleteAntecedent.setText("");
        } else {
            autoCompleteAntecedent.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPrename);
        }

        editSuffererName = (EditText) viewCSI.findViewById(R.id.editSuffererName);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName == null ||
                AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName.equals("")) {
            editSuffererName.setText("");
        } else {
            editSuffererName.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName);
        }
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName == null ||
                AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName.equals("")) {
            editSuffererName.setText("");
        } else {
            editSuffererName.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererName);
        }

        autoCompleteSuffererStatus = (AutoCompleteTextView) viewCSI.findViewById(R.id.autoCompleteSuffererStatus);
        final String[] SuffererStatus = getResources().getStringArray(R.array.suffererStatus);
        ArrayAdapter<String> adapterSuffererStatus = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SuffererStatus);
        autoCompleteSuffererStatus.setAdapter(adapterSuffererStatus);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus.equals("")) {
            autoCompleteSuffererStatus.setText("");
        } else {
            autoCompleteSuffererStatus.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus);
        }

        editTextSuffererPhone = (EditText) viewCSI.findViewById(R.id.editTextSuffererPhone);
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum.equals("")) {
            editTextSuffererPhone.setText("");
        } else {
            editTextSuffererPhone.setText(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum);

        }
        ic_telphone2 = (ImageButton) viewCSI.findViewById(R.id.ic_telphone_2);
        ic_telphone2.setOnClickListener(this);
        TextView txtInvestigatorList = (TextView) viewCSI.findViewById(R.id.txtInvestigatorList);
        txtInvestigatorList.setVisibility(View.VISIBLE);
        listViewInvestigator = (ListView) viewCSI
                .findViewById(R.id.listViewInvestigator);
        if (AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes() == null) {
            apiInvestigatorsInScenes = new ArrayList<>();
//            Log.i(TAG, "apiInvestigatorsInScenes null");
        } else {
            apiInvestigatorsInScenes = AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes();
//            Log.i(TAG, "apiInvestigatorsInScenes not null");
            listViewInvestigator.setVisibility(View.VISIBLE);

        }

        listViewInvestigator.setOnTouchListener(new ListviewSetOnTouchListener());
        showListInvestigators();

        fabBtnRec = (FloatingActionButton) viewCSI.findViewById(R.id.fabBtnRec);
        if (AssignTabFragment.mode == "view") {

            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtnRec.setLayoutParams(p);
            fabBtnRec.hide();
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
            btnButtonSearchLatLong.setVisibility(View.GONE);
            autoCompleteAntecedent.setEnabled(false);
            editSuffererName.setEnabled(false);
            autoCompleteSuffererStatus.setEnabled(false);
            editTextSuffererPhone.setEnabled(false);
            editCircumstanceOfCaseDetail.setEnabled(false);
        }

//        fabBtnRec.setOnClickListener(this);

        return viewCSI;
    }


    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
//        Log.i("Check", "onStart recieve");
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        Log.i("onPause", "onPause receive");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
//        Log.i("onStop", "onStop receive");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.i("onDestroyView", "onDestroyView receive");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnButtonSearchMap:
                if (cd.isNetworkAvailable()) {
                    lat = AssignTabFragment.apiCaseScene.getTbNoticeCase().Latitude;
                    lng = AssignTabFragment.apiCaseScene.getTbNoticeCase().Longitude;
                    if (lat != null || lng != null) {
//                        Log.d(TAG, "Go to Google map " + lat + " " + lng);

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
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ic_telphone_1:
                calling(AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTel);
                break;
            case R.id.ic_telphone_2:
                calling(AssignTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum);
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
                callIntent.setData(Uri.parse("tel:" + sPhonenumber.replace(" ", "").trim()));
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

    private void showListInvestigators() {
        if (AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes() != null) {
            listViewInvestigator.setAdapter(new InvestigatorsAdapter(getActivity()));
            setListViewHeightBasedOnItems(listViewInvestigator);
            listViewInvestigator.setVisibility(View.VISIBLE);
        } else {
            listViewInvestigator.setVisibility(View.GONE);
        }
    }

    public class InvestigatorsAdapter extends BaseAdapter {
        private Context context;

        public InvestigatorsAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;
        }

        @Override
        public int getCount() {
            return AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null) {
                view = inflater.inflate(R.layout.list_sceneinvestigation, null);
            }
            final String sInvOfficialID = AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbInvestigatorsInScene().getInvOfficialID();
            Log.i(TAG, " show " + sInvOfficialID);
            final String sRank = AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getRank();
            final String sFirstName = AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getFirstName();
            final String sLastName = AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getLastName();
            final String sPosition = AssignTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getPosition();
            final TextView txtSceneInvest = (TextView) view.findViewById(R.id.txtSceneInvest);
            txtSceneInvest.setText(String.valueOf(i + 1) + ") " + sRank + " " + sFirstName + " " + sLastName + " " + sPosition);
            return view;
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();
//            Log.i("inside", String.valueOf(numberOfItems));
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
//                Log.i("inside", String.valueOf(item.getMeasuredHeight()));
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
//            Log.i("inside totalHeight", String.valueOf(totalHeight));
            //  Log.i("inside getDividerHeight", String.valueOf(totalItemsHeight) + " " + String.valueOf(totalItemsHeight - (totalItemsHeight / 1.5)));
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
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


}