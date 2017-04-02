package com.scdc.csiapp.invmain;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import com.scdc.csiapp.main.DateDialog;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.SnackBarAlert;
import com.scdc.csiapp.main.TimeDialog;
import com.scdc.csiapp.tablemodel.TbSceneInvestigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
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
    String officialID;
    String[] updateDT;
    TextView edtUpdateDateTime2;
    EditText editTextPhone1;
    private String sAddrDetail, sDistrictName, sAmphurName, sProvinceName, provinceid, amphurid, districtid;
    Spinner spinnerAntecedent;
    AutoCompleteTextView autoCompleteSuffererStatus, autoCompleteAntecedent;
    private EditText editAddrDetail, editCircumstanceOfCaseDetail, edtVehicleDetail, editSuffererName, editTextSuffererPhone;
    private Button btnButtonSearchLatLong, btnButtonSearchMap, btnEditInvestigator;
    private Spinner spinnerDistrict, spinnerAmphur, spinnerProvince;
    private String selectedProvince, selectedAmphur, selectedDistrict;
    String[][] mProvinceArray;
    String[] mProvinceArray2;
    String[][] mAmphurArray;
    String[] mAmphurArray2;
    String[][] mDistrictArray;
    String[] mDistrictArray2;
    String[] Antecedent;

    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ
    private TextView editReceiveCaseDate, editReceiveCaseTime;
    private TextView editHappenCaseDate, editHappenCaseTime;
    private TextView editKnowCaseDate, editKnowCaseTime, valueLat, valueLong;
    // InvestDateTime ตรวจ
    private TextView editSceneInvestDate, editSceneInvestTime, txtSceneInvest;
    // ตรวจเสร็จ
    //private TextView editCompleteSceneDate, editCompleteSceneTime;
    ImageButton btn_property, ic_telphone1, ic_telphone2;
    private ImageButton btn_clear_txt_1, btn_clear_txt_2;

    private View viewReceiveCSI;
    Context context;
    String lat, lng;
    boolean oldAntecedent, oldProvince, oldAmphur = false;
    ViewGroup viewAddSceneInvestigation;
    protected static final int DIALOG_AddSceneInvestigation = 0;
    protected static final int DIALOG_SelectDataInvestigator = 1; // Dialog 1 ID
    List<TbSceneInvestigation> tbSceneInvestigations = null;
    ListView listViewAddSceneInvestDateTime, listViewInvestigator;
    List<ApiInvestigatorsInScene> apiInvestigatorsInScenes = null;
    String address, amphur, province, country, postalCode, knownName = "null";
    ArrayList<Integer> mMultiSelected;
    ArrayList<String> mSelectDataInvestigatorArrayNew, mSelectDataInvestigatorArrayOld;
    ArrayList<Boolean> mMultiChecked;
    String[][] mSelectDataInvestigatorArray = null;
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 10; //10 second

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

        edtUpdateDateTime2 = (TextView) viewReceiveCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText(getString(R.string.updatedata) + " "
                + getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate)
                + " เวลา " + getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime) + " น.");

        //โทรศัพท์ติดต่อ
        editTextPhone1 = (EditText) viewReceiveCSI.findViewById(R.id.editTextPhone);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel.equals("")) {
            editTextPhone1.setText("");
        } else {
            editTextPhone1.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel);
        }
        editTextPhone1.addTextChangedListener(new ReceiveTextWatcher(editTextPhone1));
        ic_telphone1 = (ImageButton) viewReceiveCSI.findViewById(R.id.ic_telphone1);
        ic_telphone1.setOnClickListener(new SummaryOnClickListener());
        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName.equals("")) {
            editAddrDetail.setText("");
        } else {
            editAddrDetail.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName);
        }
        editAddrDetail.addTextChangedListener(new ReceiveTextWatcher(editAddrDetail));


        //datetime
        String[] currentDT = getDateTime.updateDataDateTime();
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate.equals("0000-00-00")) {
            editReceiveCaseDate.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate = null;
        } else {
            editReceiveCaseDate.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseDate));
        }
        editReceiveCaseDate.setEnabled(false);

        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        editReceiveCaseTime.setEnabled(false);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime.equals("00:00:00")) {
            editReceiveCaseTime.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime = null;
        } else {
            editReceiveCaseTime.setText(getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReceivingCaseTime));
        }
        editReceiveCaseTime.setEnabled(false);
        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate.equals("0000-00-00")) {
            editHappenCaseDate.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate = null;
        } else {
            editHappenCaseDate.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate));
        }
        editHappenCaseDate.setOnClickListener(new SummaryOnClickListener());
        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime.equals("00:00:00")) {
            editHappenCaseTime.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime = null;
        } else {
            editHappenCaseTime.setText(getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime));
        }
        editHappenCaseTime.setOnClickListener(new SummaryOnClickListener());
        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate.equals("0000-00-00")) {
            editKnowCaseDate.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate = null;
        } else {
            editKnowCaseDate.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate));
        }
        editKnowCaseDate.setOnClickListener(new SummaryOnClickListener());
        editKnowCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseTime);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime.equals("00:00:00")) {
            editKnowCaseTime.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime = null;
        } else {
            editKnowCaseTime.setText(getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime));
        }
        editKnowCaseTime.setOnClickListener(new SummaryOnClickListener());

        btn_clear_txt_1 = (ImageButton) viewReceiveCSI.findViewById(R.id.btn_clear_txt_1);
        btn_clear_txt_2 = (ImageButton) viewReceiveCSI.findViewById(R.id.btn_clear_txt_2);
        btn_clear_txt_1.setOnClickListener(new SummaryOnClickListener());
        btn_clear_txt_2.setOnClickListener(new SummaryOnClickListener());
        //วันเวลาตรวจสถานที่เกิดเหตุ
        btn_property = (ImageButton) viewReceiveCSI.findViewById(R.id.btn_property);
        btn_property.setOnClickListener(new SummaryOnClickListener());

        if (CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations() == null) {
            tbSceneInvestigations = new ArrayList<>();
            Log.i(TAG, "getTbSceneInvestigations null");
        } else {
            tbSceneInvestigations = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations();
            Log.i(TAG, "getTbSceneInvestigations not null");
        }
        listViewAddSceneInvestDateTime = (ListView) viewReceiveCSI
                .findViewById(R.id.listViewAddSceneInvestDateTime);
        listViewAddSceneInvestDateTime.setOnTouchListener(new ListviewSetOnTouchListener());

        showListSceneInvestigation();
        if (CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes() == null) {
            apiInvestigatorsInScenes = new ArrayList<>();
            Log.i(TAG, "apiInvestigatorsInScenes null");
        } else {
            apiInvestigatorsInScenes = CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes();
            Log.i(TAG, "apiInvestigatorsInScenes not null");
        }
        listViewInvestigator = (ListView) viewReceiveCSI
                .findViewById(R.id.listViewInvestigator);
        listViewInvestigator.setOnTouchListener(new ListviewSetOnTouchListener());
        showListInvestigators();
        btnEditInvestigator = (Button) viewReceiveCSI.findViewById(R.id.btnEditInvestigator);
        btnEditInvestigator.setOnClickListener(new SummaryOnClickListener());
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
        //เเสดงค่าเดิม
        if (provinceid == null || provinceid.equals("") || provinceid.equals("null")) {
            spinnerProvince.setSelection(0);
        } else {
            for (int i = 0; i < mProvinceArray.length; i++) {
                if (provinceid.trim().equals(mProvinceArray[i][0])) {
                    spinnerProvince.setSelection(i);
                    sProvinceName = mProvinceArray[i][2].toString();
                    oldProvince = true;
                    break;
                }
            }
            setSelectAmphur(provinceid);
            Log.i(TAG, " show  provinceid " + provinceid);

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
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude = null;
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude = null;
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().Latitude = null;
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().Longitude = null;
        } else {
            valueLat.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude);
            lat = CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude;
        }
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude.equals("")) {
            valueLong.setText("");
        } else {
            valueLong.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude);
            lng = CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude;
        }


        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(new SummaryOnClickListener());
        btnButtonSearchLatLong = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchLatLong);
        btnButtonSearchLatLong.setOnClickListener(new SummaryOnClickListener());

        autoCompleteAntecedent = (AutoCompleteTextView) viewReceiveCSI.findViewById(R.id.autoCompleteAntecedent);
        Antecedent = getResources().getStringArray(R.array.antecedent);
        ArrayAdapter<String> adapterEnglish = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Antecedent);
        autoCompleteAntecedent.setAdapter(adapterEnglish);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename.equals("")) {
            autoCompleteAntecedent.setText("");
        } else {
            autoCompleteAntecedent.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename);
        }
        autoCompleteAntecedent.addTextChangedListener(new ReceiveTextWatcher(autoCompleteAntecedent));
        autoCompleteAntecedent.setOnTouchListener(new ReceiveOnTouchListener());
        editSuffererName = (EditText) viewReceiveCSI.findViewById(R.id.editSuffererName);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName.equals("")) {
            editSuffererName.setText("");
        } else {
            editSuffererName.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName);
        }
        editSuffererName.addTextChangedListener(new ReceiveTextWatcher(editSuffererName));

        autoCompleteSuffererStatus = (AutoCompleteTextView) viewReceiveCSI.findViewById(R.id.autoCompleteSuffererStatus);
        final String[] SuffererStatus = getResources().getStringArray(R.array.suffererStatus);
        ArrayAdapter<String> adapterSuffererStatus = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SuffererStatus);
        autoCompleteSuffererStatus.setAdapter(adapterSuffererStatus);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus.equals("")) {
            autoCompleteSuffererStatus.setText("");
        } else {
            autoCompleteSuffererStatus.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus);
        }
        autoCompleteSuffererStatus.addTextChangedListener(new ReceiveTextWatcher(autoCompleteSuffererStatus));
        autoCompleteSuffererStatus.setOnTouchListener(new ReceiveOnTouchListener());
        editTextSuffererPhone = (EditText) viewReceiveCSI.findViewById(R.id.editTextSuffererPhone);
        // Log.i(TAG, "SuffererPhoneNum " + CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum.equals("")) {
            editTextSuffererPhone.setText("");
        } else {
            editTextSuffererPhone.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum);
        }
        editTextSuffererPhone.addTextChangedListener(new ReceiveTextWatcher(editTextSuffererPhone));
        ic_telphone2 = (ImageButton) viewReceiveCSI.findViewById(R.id.ic_telphone2);
        ic_telphone2.setOnClickListener(new SummaryOnClickListener());

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail.equals("")) {
            editCircumstanceOfCaseDetail.setText("");
        } else {
            editCircumstanceOfCaseDetail.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail);
        }
        editCircumstanceOfCaseDetail.addTextChangedListener(new ReceiveTextWatcher(editCircumstanceOfCaseDetail));

        edtVehicleDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtVehicleDetail);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo.equals("")) {
            edtVehicleDetail.setText("");
        } else {
            edtVehicleDetail.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo);
        }
        edtVehicleDetail.addTextChangedListener(new ReceiveTextWatcher(edtVehicleDetail));

        fabBtnRec = (FloatingActionButton) viewReceiveCSI.findViewById(R.id.fabBtnRec);
        fabBtnRec.setOnClickListener(new SummaryOnClickListener());
        if (CSIDataTabFragment.mode == "view") {
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
            btn_clear_txt_1.setVisibility(View.GONE);
            btn_clear_txt_2.setVisibility(View.GONE);
            editAddrDetail.setEnabled(false);
            spinnerProvince.setEnabled(false);
            spinnerAmphur.setEnabled(false);
            spinnerDistrict.setEnabled(false);
            btnButtonSearchLatLong.setEnabled(false);
            btn_property.setEnabled(false);
            btn_property.setVisibility(View.GONE);
            editSuffererName.setEnabled(false);
            autoCompleteAntecedent.setEnabled(false);
            autoCompleteSuffererStatus.setEnabled(false);
            editTextSuffererPhone.setEnabled(false);
            editCircumstanceOfCaseDetail.setEnabled(false);
            edtVehicleDetail.setEnabled(false);
            btnEditInvestigator.setVisibility(View.GONE);
        } else {
            listViewAddSceneInvestDateTime.setOnItemClickListener(new ListviewOnClickListener());

        }
//        btnEditInvestigator.setVisibility(View.GONE);
        return viewReceiveCSI;
    }

    // ดึงค่าอำเภอมาเเสดง ตาม จังหวัดปัจจุบัน
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

    // สัมผัส listview
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

    //กำนดความสูงของการแสดงlistview ทั้งหมด
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

    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        Log.i("Check", "onStart recieve");
        showListSceneInvestigation();
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause receive");
        hiddenKeyboard();
//        if (CSIDataTabFragment.mode != "view") {
//            savedata();
//        }
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
    public void onResume() {
        super.onResume();
        showListSceneInvestigation();
    }

    //อ่านค่า พิกัดสถานที่
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

    //เช็คค่าพิกัดปัจจุงบัน แล้วเดึงข้อมูลสถานที่ปัจจุบัน จังหวัด อำเภอ ตำบล เอามาค้นสถ้างรายการspinner ปตามจังหวัดปัจจุบัน
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation.set(location);
        Log.d(TAG, "Location " + location.getLatitude() + " " + location.getLatitude());
        if (cd.isNetworkAvailable()) {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
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
                            CSIDataTabFragment.apiCaseScene.getTbCaseScene().PROVINCE_ID = provinceid;
                            Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().PROVINCE_ID);
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
                            CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID = amphurid;
                            Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID);
                        }
                    } else {
                        spinnerAmphur.setSelection(0);
                    }
                }
                if (districtid == null || districtid.equals("") || districtid.equals("null")) {

                    if (knownName != null || knownName != "null" || knownName != "Unnamed Road") {
                        knownName = knownName.replace("ตำบล", "");
//                        Log.i(TAG, "have knownName" + knownName);
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
                            CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID = districtid;
                            Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID);
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

    //----end //เช็คค่าพิกัดปัจจุงบัน แล้วเดึงข้อมูลสถานที่ปัจจุบัน จังหวัด อำเภอ ตำบล เอามาค้นสถ้างรายการspinner ปตามจังหวัดปัจจุบัน
    private void savedata() {
        final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
        if (editHappenCaseDate.getText().toString() == null || editHappenCaseDate.getText().toString().equals("")) {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate = null;
        } else {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseDate = getDateTime.changeDateFormatToDB(editHappenCaseDate.getText().toString());
        }
        if (editHappenCaseTime.getText().toString() == null || editHappenCaseTime.getText().toString().equals("")) {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime = null;
        } else {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().HappenCaseTime = editHappenCaseTime.getText().toString();
        }
        if (editKnowCaseDate.getText().toString() == null || editKnowCaseDate.getText().toString().equals("")) {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate = null;
        } else {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseDate = getDateTime.changeDateFormatToDB(editKnowCaseDate.getText().toString());
        }
        if (editKnowCaseTime.getText().toString() == null || editKnowCaseTime.getText().toString().equals("")) {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime = null;
        } else {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().KnowCaseTime = editKnowCaseTime.getText().toString();
        }

        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene() != null) {
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                if (snackbar == null || !snackbar.isShown()) {
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                            getString(R.string.save_complete)
                                    + "\n" + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate
                                    + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime);
                    snackBarAlert.createSnacbar();
                }
            } else {
                if (snackbar == null || !snackbar.isShown()) {

                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                            getString(R.string.save_error)
                                    + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID.toString());
                    snackBarAlert.createSnacbar();
                }
            }
        }
    }

    //event onclick ของ ปุ่มต่างๆ
    private class SummaryOnClickListener implements View.OnClickListener {
        public void onClick(View v) {

            if (v == fabBtnRec) {
                hiddenKeyboard();
                savedata();
            }
            if (v == btnButtonSearchMap) {
                hiddenKeyboard();
                if (cd.isNetworkAvailable()) {
                    if (lat != null || lng != null) {
                        Log.d(TAG, "Go to Google map " + lat + " " + lng);

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } else {
                        //Searches for 'Locale name' province amphur district

                        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q="
                                + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName
                                + "+" + sDistrictName + "+" + sAmphurName + "+" + sProvinceName);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT).show();
                }
            }
            if (v == btnButtonSearchLatLong) {
                hiddenKeyboard();
                if (cd.isNetworkAvailable()) {
                    lat = String.valueOf(mLastLocation.getLatitude());
//                lat = "16.438052";
                    lng = String.valueOf(mLastLocation.getLongitude());
//                lng = "102.799998";
                    Log.d(TAG, "Go to Google map " + lat + " " + lng);
                    valueLat.setText(lat);
                    valueLong.setText(lng);
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().Latitude = lat.toString();
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().Longitude = lng.toString();

                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().Latitude = lat.toString();
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().Longitude = lng.toString();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT).show();
                }
            }
            if (v == btnEditInvestigator) {
                Log.i("Investigator1", "showlist");
                createdDialog(DIALOG_SelectDataInvestigator).show();
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
            if (v == btn_property) {
                viewAddSceneInvestigation = (ViewGroup) v.findViewById(R.id.layout_addsceneinvestigation_dialog);
                createdDialog(DIALOG_AddSceneInvestigation).show();
                Log.i(TAG, "tbSceneInvestigations num1:" + String.valueOf(tbSceneInvestigations.size()));

            }
            if (v == editSceneInvestDate) {
                Log.i("Click SceneInvestDate", "null");
                DateDialog dialogSceneInvestDate = new DateDialog(v);
                dialogSceneInvestDate.show(getActivity().getFragmentManager(), "Date Picker");
            }
            if (v == editSceneInvestTime) {
                Log.i("ClickSceneInvestTime", "null");
                TimeDialog dialogSceneInvestTime = new TimeDialog(v);
                dialogSceneInvestTime.show(getActivity().getFragmentManager(), "Time Picker");
            }
            if (v == ic_telphone1) {
                hiddenKeyboard();
                calling(editTextPhone1.getText().toString());
            }
            if (v == ic_telphone2) {
                hiddenKeyboard();
                calling(editTextSuffererPhone.getText().toString());
            }
            if (v == btn_clear_txt_1) {
                hiddenKeyboard();
                editHappenCaseDate.setText("");
                editHappenCaseTime.setText("");
            }
            if (v == btn_clear_txt_2) {
                hiddenKeyboard();
                editKnowCaseDate.setText("");
                editKnowCaseTime.setText("");
            }
        }
    }

    //สร้าง dialog เพิ่มวันที่เวลาตรวจสถานที่เกิดเหตุ
    protected Dialog createdDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder;
        switch (id) {

            case DIALOG_AddSceneInvestigation:
                builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflaterDialogSceneInvestigation = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View ViewlayoutAddSceneInvestigation = inflaterDialogSceneInvestigation
                        .inflate(
                                R.layout.add_sceneinvestigation_dialog,
                                viewAddSceneInvestigation);
                builder.setIcon(R.drawable.ic_property);
                builder.setTitle("เพิ่มวันเวลาออกตรวจสถานที่เกิดเหตุ");
                builder.setView(ViewlayoutAddSceneInvestigation);
                editSceneInvestDate = (TextView) ViewlayoutAddSceneInvestigation
                        .findViewById(R.id.editSceneInvestDate);
                editSceneInvestTime = (TextView) ViewlayoutAddSceneInvestigation
                        .findViewById(R.id.editSceneInvestTime);
                //datetime
                String[] currentDT = getDateTime.updateDataDateTime();
                editSceneInvestDate.setText(currentDT[0]);
                editSceneInvestTime.setText(currentDT[1]);

                editSceneInvestDate.setOnClickListener(new SummaryOnClickListener());
                editSceneInvestTime.setOnClickListener(new SummaryOnClickListener());


                builder.setPositiveButton(R.string.save,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.i(TAG, "Click SceneInvestDate " + editSceneInvestDate.getText().toString());
                                Log.i(TAG, "Click SceneInvestTime " + editSceneInvestTime.getText().toString());

                                TbSceneInvestigation tbSceneInvestigation = new TbSceneInvestigation();
                                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                                final String saveDataTime = dateTimeCurrent[2] + dateTimeCurrent[1] + dateTimeCurrent[0] + "_" + dateTimeCurrent[3] + dateTimeCurrent[4] + dateTimeCurrent[5];

                                tbSceneInvestigation.SceneInvestID = "SI_" + saveDataTime;
                                tbSceneInvestigation.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                                tbSceneInvestigation.SceneInvestDate = getDateTime.changeDateFormatToDB(editSceneInvestDate.getText().toString());
                                tbSceneInvestigation.SceneInvestTime = editSceneInvestTime.getText().toString();

                                tbSceneInvestigations.add(tbSceneInvestigation);
                                CSIDataTabFragment.apiCaseScene.setTbSceneInvestigations(tbSceneInvestigations);
                                Log.i(TAG, "tbSceneInvestigations num2:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().size()));
                                showListSceneInvestigation();
                                dialog.dismiss();
                            }
                        })
                        // Button Cancel
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                dialog = builder.create();

                break;
            case DIALOG_SelectDataInvestigator:
                Log.i("Investigator2", "showlist");
                mSelectDataInvestigatorArray = dbHelper
                        .SelectDataInvestigator("investigator2");
                mMultiChecked = new ArrayList<Boolean>();
                mSelectDataInvestigatorArrayOld = new ArrayList<String>();

                if (mSelectDataInvestigatorArray != null) {
                    final String[] mSelectDataInvestigatorArray2 = new String[mSelectDataInvestigatorArray.length];
                    for (int i = 0; i < mSelectDataInvestigatorArray.length; i++) {
                        mSelectDataInvestigatorArray2[i] = mSelectDataInvestigatorArray[i][1] + " " + mSelectDataInvestigatorArray[i][2] + " " + mSelectDataInvestigatorArray[i][3];
                        Log.i("SelectInvestigator",
                                mSelectDataInvestigatorArray2[i].toString());
                        long checkInv = dbHelper.CheckInvestigatorInCase(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID, mSelectDataInvestigatorArray[i][0]);
                        if (checkInv == 1) {
                            mMultiChecked.add(true);
                            mSelectDataInvestigatorArrayOld.add(mSelectDataInvestigatorArray[i][0]);
                            Log.i("mSelectDataInvestigatorArrayOld", mSelectDataInvestigatorArray[i][0]);
                        } else if (checkInv == 0) {
                            mMultiChecked.add(false);
                        }
                    }

                    final boolean[] CheckedInvestigator = new boolean[mMultiChecked.size()];

                    for (int i = 0; i < mMultiChecked.size(); i++) {
                        CheckedInvestigator[i] = ((Boolean) mMultiChecked.get(i)).booleanValue();
                        Log.i("CheckedInvestigator",
                                String.valueOf(CheckedInvestigator[i]));
                    }
                    mMultiSelected = new ArrayList<Integer>();
                    mSelectDataInvestigatorArrayNew = new ArrayList<String>();
                    builder = new AlertDialog.Builder(getActivity());

                    builder.setMultiChoiceItems(mSelectDataInvestigatorArray2, CheckedInvestigator, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            CheckedInvestigator[which] = isChecked;
                            if (isChecked) {
                                mMultiSelected.add(which);

                            } else if (mMultiSelected.contains(which)) {
                                mMultiSelected.remove(Integer.valueOf(which));
                            }
                        }
                    })
                            .setTitle("เลือกรายชื่อเจ้าหน้าที่")
                            .setCancelable(false)
                            .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.i("DIALOG_ Investigator", "null");

                                    for (int i = 0; i < CheckedInvestigator.length; i++) {
                                        boolean checked = CheckedInvestigator[i];
                                        if (checked) {
                                            mSelectDataInvestigatorArrayNew.add(mSelectDataInvestigatorArray[i][0]);
//                                            Toast.makeText(getActivity().getApplicationContext(), mSelectDataInvestigatorArray[i][0] + "\n", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    String sCaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                                    if (mSelectDataInvestigatorArrayOld != null) {
                                        if (dbHelper.deleteInvIncase(sCaseReportID, mSelectDataInvestigatorArrayOld)) {
                                            Log.i(TAG, "success deleteInvIncase");
                                        } else {
                                            Log.i(TAG, "cannot DeleteInvestigatorInscene");
                                        }
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "ไม่มีผู้ช่วยตรวจ", Toast.LENGTH_SHORT).show();
                                    }
                                    if (mSelectDataInvestigatorArrayNew != null) {
                                        if (dbHelper.saveInvIncase(sCaseReportID, mSelectDataInvestigatorArrayNew)) {
                                            Log.i(TAG, "success saveInvIncase");
                                            List<ApiInvestigatorsInScene> apiInvestigatorsInScenes = new ArrayList<>();
                                            apiInvestigatorsInScenes = dbHelper.selectApiInvestigatorsInScene(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
                                            CSIDataTabFragment.apiCaseScene.setApiInvestigatorsInScenes(apiInvestigatorsInScenes);
                                            Log.i(TAG, "size apiInvestigatorsInScenes " + String.valueOf(apiInvestigatorsInScenes.size()));

                                            Toast.makeText(getActivity().getApplicationContext(), "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();
                                            showListInvestigators();
                                        } else {
                                            Log.i(TAG, "cannot saveInvIncase");
                                        }
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "คุณยังไม่ได้เลือกรายชื่อผู้ตรวจ", Toast.LENGTH_SHORT).show();
                                    }


                                    dialog.dismiss();
                                    showListInvestigators();
                                }
                            })
                            .setNegativeButton("ยกเลิก", null);

                    dialog = builder.create();
                } else {
                    Log.i("Investigator", "null");
                }


                break;
            default:
                dialog = null;
        }
        return dialog;
    }

    //แสดงรายการ วันที่ตรวจสถานที่เกิดเหตุ
    private void showListSceneInvestigation() {
        if (CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations() != null) {
            listViewAddSceneInvestDateTime.setAdapter(new SceneInvestigationAdapter(getActivity()));
            setListViewHeightBasedOnItems(listViewAddSceneInvestDateTime);
            listViewAddSceneInvestDateTime.setVisibility(View.VISIBLE);
        } else {
            listViewAddSceneInvestDateTime.setVisibility(View.GONE);
        }

    }

    //SceneInvestigationAdapter ข้อความวันที่ตรวจสถานที่เกิดเหตุ ที่เเสดงใน listview
    public class SceneInvestigationAdapter extends BaseAdapter {
        private Context context;

        public SceneInvestigationAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;
        }

        @Override
        public int getCount() {
            return CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().size();
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
            final String sSceneInvestID = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(i).getSceneInvestID();
            final String sSceneInvestDate = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(i).getSceneInvestDate();
            final String sSceneInvestTime = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(i).getSceneInvestTime();
//            Log.i(TAG, " SceneInvestigation :" + sSceneInvestID);
            final String sceneInvestinfo = "ครั้งที่ " + String.valueOf(i + 1) + " วันที่ " + getDateTime.changeDateFormatToCalendar(sSceneInvestDate) + " เวลา " + sSceneInvestTime + " น.";
            final TextView txtSceneInvest = (TextView) view.findViewById(R.id.txtSceneInvest);
            txtSceneInvest.setText(sceneInvestinfo);
            return view;
        }
    }

    //แสดงรายชื่อผู้ตรวจสถานที่เกิดเหตุ
    private void showListInvestigators() {

        if (CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes() != null) {
            listViewInvestigator.setAdapter(new InvestigatorsAdapter(getActivity()));
            setListViewHeightBasedOnItems(listViewInvestigator);
            listViewInvestigator.setVisibility(View.VISIBLE);
        } else {
            listViewInvestigator.setVisibility(View.GONE);
        }
    }

    //InvestigatorsAdapter แสดงรายชื่อผู้ตรวจสถานที่เกิดเหตุ
    public class InvestigatorsAdapter extends BaseAdapter {
        private Context context;

        public InvestigatorsAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;
        }

        @Override
        public int getCount() {
            return CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes().size();
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
            final String sInvOfficialID = CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbInvestigatorsInScene().getInvOfficialID();
//            Log.i(TAG, " show " + sInvOfficialID);
            final String sRank = CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getRank();
            final String sFirstName = CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getFirstName();
            final String sLastName = CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getLastName();
            final String sPosition = CSIDataTabFragment.apiCaseScene.getApiInvestigatorsInScenes().get(i).getTbOfficial().getPosition();
            final TextView txtSceneInvest = (TextView) view.findViewById(R.id.txtSceneInvest);
            txtSceneInvest.setText(String.valueOf(i + 1) + ") " + sRank + " " + sFirstName + " " + sLastName + " " + sPosition);
            return view;
        }
    }

    //event เมื่อเลือก spinner
    public class RecOnItemSelectedListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (view == spinnerProvince) {
                oldProvince = false;
            }

            return false;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            //check which spinner triggered the listener

            switch (parent.getId()) {

                case R.id.spinnerDistrict:
                    if (districtid == null || districtid.equals("") || districtid.equals("null")) {
                        selectedDistrict = mDistrictArray[pos][0];
                        sDistrictName = mDistrictArray[pos][2].toString();
                        Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID = selectedDistrict;
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID = selectedDistrict;
                        Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID);
                    } else {
                        for (int i = 0; i < mDistrictArray.length; i++) {
                            if (districtid.trim().equals(mDistrictArray[i][0].toString())) {
                                sDistrictName = mDistrictArray[i][2].toString();
                                selectedDistrict = mDistrictArray[i][0];
                                spinnerDistrict.setSelection(i);
                                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID = selectedDistrict;
                                CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID = selectedDistrict;
                                Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID);
                                break;
                            }
                        }
                    }
                    break;
                case R.id.spinnerAmphur:

                    //ดึงค่า District
                    if (amphurid == null || amphurid.equals("") || amphurid.equals("null")) {
                        selectedAmphur = mAmphurArray[pos][0];
                        sAmphurName = mAmphurArray[pos][2].toString();
//                        Log.i(TAG, " show selectedAmphur" + selectedAmphur + " " + sAmphurName);
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID = selectedAmphur;
                        Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID);
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID = selectedAmphur;
                    } else {
                        for (int i = 0; i < mAmphurArray.length; i++) {
                            if (amphurid.trim().equals(mAmphurArray[i][0].toString())) {
                                spinnerAmphur.setSelection(i);
                                selectedAmphur = mAmphurArray[i][0];
                                sAmphurName = mAmphurArray[i][2].toString();
//                                Log.i(TAG, " show selectedAmphur" + selectedAmphur + " " + sAmphurName);
                                CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID = selectedAmphur;
                                Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID);
                                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID = selectedAmphur;
                                oldAmphur = true;
                                break;
                            }
                        }
                    }
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
//                        Log.i(TAG, " show selectedDistrict " + String.valueOf(selectedDistrict));
                    }
//                    }
                    break;
                case R.id.spinnerProvince:
                    if (oldProvince == false) {
                        selectedProvince = mProvinceArray[pos][0];
                        sProvinceName = mProvinceArray[pos][2].toString();
//                        Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);
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


            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
            switch (parent.getId()) {
                case R.id.spinnerDistrict:
                    selectedDistrict = mDistrictArray[0][0];
                    sDistrictName = mDistrictArray[0][2].toString();
//                    Log.i(TAG + " show selectedDistrict", selectedDistrict + " " + sDistrictName);
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().DISTRICT_ID = selectedDistrict;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().DISTRICT_ID = selectedDistrict;
                    break;
                case R.id.spinnerAmphur:
                    selectedAmphur = mAmphurArray[0][0];
                    sAmphurName = mAmphurArray[0][2].toString();
//                    Log.i(TAG + " show selectedAmphur", selectedAmphur + " " + sAmphurName);
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().AMPHUR_ID = selectedAmphur;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().AMPHUR_ID = selectedAmphur;
                    break;
                case R.id.spinnerProvince:
                    selectedProvince = mProvinceArray[0][0];
                    sProvinceName = mProvinceArray[0][2].toString();
//                    Log.i(TAG + " show selectedProvince", selectedProvince + " " + sProvinceName);
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().PROVINCE_ID = selectedProvince;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().PROVINCE_ID = selectedProvince;
                    break;

            }
        }


    }

    // ฟังชัน โทรออก
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

    //event ขณะพิมพ์ข้อความในedittext
    private class ReceiveTextWatcher implements TextWatcher {
        private EditText mEditText;

        public ReceiveTextWatcher(EditText editText) {
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
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel = editTextPhone1.getText().toString();
                Log.i(TAG, "CaseTel " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTel);
            } else if (s == editAddrDetail.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName = editAddrDetail.getText().toString();
//                Log.i(TAG, "LocaleName " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LocaleName);
            } else if (s == editSuffererName.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName = editSuffererName.getText().toString();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererName = editSuffererName.getText().toString();
//                Log.i(TAG, "SuffererName " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererName);
            } else if (s == autoCompleteSuffererStatus.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus = autoCompleteSuffererStatus.getText().toString();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererStatus = autoCompleteSuffererStatus.getText().toString();
//                Log.i(TAG, "SuffererStatus " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererStatus);
            } else if (s == editTextSuffererPhone.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum = editTextSuffererPhone.getText().toString();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SuffererPhoneNum = editTextSuffererPhone.getText().toString();
                Log.i(TAG, "SuffererPhoneNum " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPhoneNum);
            } else if (s == editCircumstanceOfCaseDetail.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();
//                Log.i(TAG, "CircumstanceOfCaseDetail " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CircumstanceOfCaseDetail);
            } else if (s == edtVehicleDetail.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo = edtVehicleDetail.getText().toString();
//                Log.i(TAG, "VehicleInfo " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().VehicleInfo);
            } else if (s == autoCompleteAntecedent.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().SuffererPrename = autoCompleteAntecedent.getText().toString();
//                Log.i(TAG, "CircumstanceOfCaseDetail " + EmergencyTabFragment.tbNoticeCase.CircumstanceOfCaseDetail);
            }
        }
    }

    //ซ่อน keyboard
    public void hiddenKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //event เมื่อเลือกแก้ไข วันที่ตรวจสถานที่เกิดเหตุ
    private class ListviewOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

            switch (parent.getId()) {
                case R.id.listViewAddSceneInvestDateTime:
                    Dialog dialog = null;
                    AlertDialog.Builder builder;
                    final String sSceneInvestDate = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(position).getSceneInvestDate();
                    final String sSceneInvestTime = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(position).getSceneInvestTime();
                    builder = new AlertDialog.Builder(getActivity());
                    final LayoutInflater inflaterDialogSceneInvestigation = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    ViewGroup viewEditSceneInvestigation = (ViewGroup) view.findViewById(R.id.layout_addsceneinvestigation_dialog);

                    final View ViewlayoutAddSceneInvestigation = inflaterDialogSceneInvestigation
                            .inflate(
                                    R.layout.add_sceneinvestigation_dialog,
                                    viewEditSceneInvestigation);
                    builder.setIcon(R.drawable.ic_property);
                    builder.setTitle("แก้ไขวันเวลาออกตรวจสถานที่เกิดเหตุ");
                    builder.setView(ViewlayoutAddSceneInvestigation);
                    editSceneInvestDate = (TextView) ViewlayoutAddSceneInvestigation
                            .findViewById(R.id.editSceneInvestDate);
                    editSceneInvestDate.setOnClickListener(new SummaryOnClickListener());
                    editSceneInvestTime = (TextView) ViewlayoutAddSceneInvestigation
                            .findViewById(R.id.editSceneInvestTime);
                    editSceneInvestTime.setOnClickListener(new SummaryOnClickListener());
                    if (sSceneInvestDate == null || sSceneInvestDate.equals("")
                            || sSceneInvestDate.equals("0000-00-00")) {
                        editSceneInvestDate.setText("");
                    } else {
                        editSceneInvestDate.setText(getDateTime.changeDateFormatToCalendar(sSceneInvestDate));
                    }
                    if (sSceneInvestTime == null || sSceneInvestTime.equals("")
                            || sSceneInvestTime.equals("00:00:00")) {
                        editSceneInvestTime.setText("");
                    } else {
                        editSceneInvestTime.setText(getDateTime.changeTimeFormatToDB(sSceneInvestTime));
                    }
                    builder.setPositiveButton(R.string.edit,
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
//                                    Log.i(TAG, "Click SceneInvestDate " + editSceneInvestDate.getText().toString());
//                                    Log.i(TAG, "Click SceneInvestTime " + editSceneInvestTime.getText().toString());
                                    CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(position)
                                            .setSceneInvestDate(getDateTime.changeDateFormatToDB(editSceneInvestDate.getText().toString()));
                                    CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(position)
                                            .setSceneInvestTime(editSceneInvestTime.getText().toString());

                                    showListSceneInvestigation();
                                    dialog.dismiss();
                                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT, "แก้ไขสำเร็จ");
                                    snackBarAlert.createSnacbar();
                                }
                            })
                            .setNeutralButton(R.string.delete,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().remove(position);
                                            showListSceneInvestigation();
                                            dialog.dismiss();
                                            SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT, "ลบรายการสำเร็จ");
                                            snackBarAlert.createSnacbar();

                                        }
                                    })

                            // Button Cancel
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                    dialog = builder.create();
                    builder.show();

                    break;
            }
        }
    }

    private class ReceiveOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view == autoCompleteAntecedent) {
                autoCompleteAntecedent.showDropDown();
            }
            if (view == autoCompleteSuffererStatus) {
                autoCompleteSuffererStatus.showDropDown();
            }
            return false;
        }
    }
}