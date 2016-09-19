package com.scdc.csiapp.inqmain;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.DateDialog;
import com.scdc.csiapp.main.TimeDialog;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.MapActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class EmergencyDetailTabFragment extends Fragment {
    FloatingActionButton fabBtnRec;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;

    GetDateTime getDateTime;
    String officialID, reportID;
    ArrayAdapter<String> adapterSubCaseType;
    String[] updateDT;
    private String message = "";
    String sReceivingCaseDate_New ="";
    String sHappenCaseDate_New ="";
    String sKnowCaseDate_New ="";
    TextView  edtUpdateDateTime2;
    EditText editTextPhone1;
    private String sAddrDetail, sHouseNo, sVillageNo, sVillageName, sLane,
            sRoad, sDistrict, sAmphur, sProvinceName, sPostalCode, sLatitude,
            sLongitude;
    private AutoCompleteTextView autoCompleteDistrict, autoCompleteAmphur, autoCompleteProvince2;
    private EditText editAddrDetail, editHouseNo, editVillageNo,
            editVillageName, editLane, editRoad, editCircumstanceOfCaseDetail, edtVehicleDetail;
    private Button btnButtonSearchMap;
    static String sInquiryOfficialID,sProvinceID, sAmphurID = "";
    // layout Sufferer 3

    private ListView listViewSufferer, listViewInvestigator;
    private View linearLayoutAddSufferer, linearLayoutInvestigator;
    private Button btnAddSufferer, btnShowInvestigator ;
    private ArrayList<HashMap<String, String>> suffererList;
    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ
    private String sReceivingCaseDate, sReceivingCaseTime, sHappenCaseDate,
            sHappenCaseTime, sKnowCaseDate, sKnowCaseTime,
            sCircumstanceOfCaseDetail;
    private TextView editReceiveCaseDate, editReceiveCaseTime,editScheduleInvestDate;
    private TextView editHappenCaseDate, editHappenCaseTime;
    private TextView editKnowCaseDate, editKnowCaseTime, valueLat, valueLong;
    // InvestDateTime ตรวจ
   //private TextView editSceneInvestDate, editSceneInvestTime;
    // ตรวจเสร็จ
    //private TextView editCompleteSceneDate, editCompleteSceneTime;

    //TextView edtInvestDateTime, edtUpdateDateTime;
    //private String selectedInspector = null;
    private String selectedLocatePolice = null;
    Spinner spnLocatePolice;
    String[] mTypePoliceStationArray2;
    String[][] mTypePoliceStationArray = null;
    //String[][] mSelectDataInspectorArray = null;
    //String[] mSelectDataInspectorArray2, mSelectDataInspectorID;
    //String[] mSelectDataInspectorRank;

    private static final String TAG = "DEBUG-EmergencyDetailTabFragment";
    String SelectedPoliceStationID= "";
    String mSelected;
    ArrayList<Integer> mMultiSelected;
    ArrayList<Boolean> mMultiChecked;
    String[][] mSelectDataInvestigatorArray = null;
    private ArrayList<HashMap<String, String>> InvestigatorList;
    protected static final int DIALOG_SelectDataInvestigator = 1; // Dialog 1 ID
    protected static final int DIALOG_AddSufferer = 0; // Dialog 2 ID
    ViewGroup viewByIdaddsufferer;
    View viewReceiveCSI;
    Context context;
    Button btn_clear_txt_1, btn_clear_txt_2, btn_clear_txt_3, btn_clear_txt_4, btn_clear_txt_5,
            btn_clear_txt_6, btn_clear_txt_7, btn_clear_txt_8, btn_clear_txt_9, btn_clear_txt_10, btn_clear_txt_11,btn_clear_txt_12;
    ArrayAdapter<String> adapterSelectDataInspector, adapterPoliceStation;
    protected static String selectScheduleID = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        viewReceiveCSI = inflater.inflate(R.layout.emergency_tab_layout, null);

        context = viewReceiveCSI.getContext();

        rootLayout = (CoordinatorLayout) viewReceiveCSI.findViewById(R.id.rootLayoutReceive);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
       // reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(getActivity());

        updateDT = getDateTime.getDateTimeNow();
      //  Log.i("viewReceiveCSI", reportID);
        //new showScheduleInvestOfCase().execute(reportID);
        edtUpdateDateTime2 = (TextView) viewReceiveCSI.findViewById(R.id.edtUpdateDateTime2);
        //Form
        String noticecaseid = EmergencyTabFragment.NoticeCaseID;
        Log.i(TAG,  "noticecaseid " + noticecaseid);



        spnLocatePolice = (Spinner) viewReceiveCSI.findViewById(R.id.spnLocatePolice);
        mTypePoliceStationArray = mDbHelper
                .SelectPoliceStation();
        if (mTypePoliceStationArray != null) {
            mTypePoliceStationArray2 = new String[mTypePoliceStationArray.length];
            for (int i = 0; i < mTypePoliceStationArray.length; i++) {
                mTypePoliceStationArray2[i] = mTypePoliceStationArray[i][3];
                Log.i("PoliceStationArray2",
                        mTypePoliceStationArray2[i].toString());
            }
            adapterPoliceStation = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_dropdown_item_1line,
                    mTypePoliceStationArray2);
            //set the view for the Drop down list
            adapterPoliceStation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spnLocatePolice.setAdapter(adapterPoliceStation);
            spnLocatePolice.setOnItemSelectedListener(new ReceiveOnItemSelectedListener());

            String SelectedLocatePolicecurrent  = String
                    .valueOf(spnLocatePolice
                            .getSelectedItem());
            if (mTypePoliceStationArray2 == null) {
                Log.i("selectedLocatePolice ", "null");

            }else {
                for (int i = 0; i < mTypePoliceStationArray2.length; i++) {

                    if (SelectedLocatePolicecurrent == mTypePoliceStationArray2[i]) {
                        selectedLocatePolice = mTypePoliceStationArray[i][3];
                        Log.i("selectedLocatePolice", selectedLocatePolice);
                    }
                }
                // new SaveInspectorReceivingCase().execute(reportID, SelectedInspectorID, "accept", "");
                Log.i("selectedLocatePolice ", "not null");
            }


            Log.i("selectedLocatePolice ", selectedLocatePolice);
        } else {
            Log.i("selectedLocatePolice", "null");
        }

        editTextPhone1 = (EditText) viewReceiveCSI.findViewById(R.id.editTextPhone1);

        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);
        btn_clear_txt_1 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_1);

        editAddrDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_clear_txt_1.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_1.setVisibility(View.VISIBLE);
                btn_clear_txt_1.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //datetime
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        editReceiveCaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickReceiveCaseDate", "null");
                DateDialog dialogReceiveCaseDate = new DateDialog(view);
                dialogReceiveCaseDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });
        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        editReceiveCaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickReceiveCaseTime", "null");
                TimeDialog dialogReceiveCaseTime = new TimeDialog(view);
                dialogReceiveCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
            }

        });
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



// /layout 3 address
        editHouseNo = (EditText) viewReceiveCSI.findViewById(R.id.edtHouseNo);
        btn_clear_txt_2 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_2);
        editHouseNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (btn_clear_txt_1.getText() == "") {
                    btn_clear_txt_1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_2.setVisibility(View.VISIBLE);
                btn_clear_txt_2.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editVillageNo = (EditText) viewReceiveCSI.findViewById(R.id.edtVillageNo);
        btn_clear_txt_3 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_3);
        editVillageNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_3.setVisibility(View.VISIBLE);
                btn_clear_txt_3.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editVillageName = (EditText) viewReceiveCSI
                .findViewById(R.id.edtVillageName);
        btn_clear_txt_4 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_4);
        editVillageName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_4.setVisibility(View.VISIBLE);
                btn_clear_txt_4.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editLane = (EditText) viewReceiveCSI.findViewById(R.id.edtLane);
        btn_clear_txt_5 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_5);
        editLane.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_5.setVisibility(View.VISIBLE);
                btn_clear_txt_5.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editRoad = (EditText) viewReceiveCSI.findViewById(R.id.edtRoad);
        btn_clear_txt_6 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_6);
        editRoad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_6.setVisibility(View.VISIBLE);
                btn_clear_txt_6.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        autoCompleteDistrict = (AutoCompleteTextView) viewReceiveCSI
                .findViewById(R.id.edtDistrict);
        btn_clear_txt_9 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_9);
        autoCompleteDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_9.setVisibility(View.VISIBLE);
                btn_clear_txt_9.setOnClickListener(new ReceiveOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        autoCompleteAmphur = (AutoCompleteTextView) viewReceiveCSI
                .findViewById(R.id.edtAmphur);
        btn_clear_txt_8 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_8);

        autoCompleteProvince2 = (AutoCompleteTextView) viewReceiveCSI
                .findViewById(R.id.edtProvinceAddr);
        btn_clear_txt_7 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_7);

        final String mProvince2Array[][] = mDbHelper.SelectAllProvince();
        if (mProvince2Array != null) {
            String[] mProvince2Array2 = new String[mProvince2Array.length];
            for (int i = 0; i < mProvince2Array.length; i++) {
                mProvince2Array2[i] = mProvince2Array[i][2];
                Log.i("show mProvince2Array2", mProvince2Array2[i].toString());
            }
            ArrayAdapter<String> adapterProvince2 = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mProvince2Array2);
            autoCompleteProvince2.setThreshold(1);
            autoCompleteProvince2.setAdapter(adapterProvince2);
        } else {
            Log.i("show mProvince2Array2", "null");
        }
        autoCompleteProvince2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                btn_clear_txt_7.setVisibility(View.VISIBLE);
                btn_clear_txt_7.setOnClickListener(new ReceiveOnClickListener());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString() != "") {
                    Log.i("s on amphur", s.toString());
                    final String[] myDataProvinceID = mDbHelper
                            .SelectProvinceID(s.toString());
                    if (myDataProvinceID != null) {
                        sProvinceID = myDataProvinceID[0];
                        Log.i("show sProvinceID1", sProvinceID);
                        autoCompleteAmphur.requestFocus();
                        final String mTypeAmphurArray[][] = mDbHelper
                                .SelectAllAmphurByProvinceID(sProvinceID);
                        if (mTypeAmphurArray != null) {
                            String[] mTypeAmphurArray2 = new String[mTypeAmphurArray.length];
                            for (int i = 0; i < mTypeAmphurArray.length; i++) {
                                mTypeAmphurArray2[i] = mTypeAmphurArray[i][2];
                                Log.i("show mTypeAmphurArray2",
                                        mTypeAmphurArray2[i].toString());
                            }
                            ArrayAdapter<String> adapterAmphur = new ArrayAdapter<String>(
                                    getActivity(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    mTypeAmphurArray2);
                            autoCompleteAmphur.setThreshold(1);
                            autoCompleteAmphur.setAdapter(adapterAmphur);
                        } else {
                            Log.i("show mTypeAmphurArray2", "null");
                        }
                    } else {
                        // Log.i("show sProvinceID", sProvinceID);
                    }
                } else {
                    Log.i("s on amphur", s.toString());
                }
            }
        });

        autoCompleteAmphur.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                btn_clear_txt_8.setVisibility(View.VISIBLE);
                btn_clear_txt_8.setOnClickListener(new ReceiveOnClickListener());


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                final String[] myDataAmphurID = mDbHelper.SelectAmphurID(s
                        .toString());
                if (myDataAmphurID != null) {
                    sAmphurID = myDataAmphurID[0];
                    Log.i("show sAmphurID1", sAmphurID);
                    autoCompleteDistrict.requestFocus();
                    final String mTypeDistrictArray[][] = mDbHelper
                            .SelectAllDistrictByAmphurID(sAmphurID);
                    if (mTypeDistrictArray != null) {
                        String[] mTypeDistrictArray2 = new String[mTypeDistrictArray.length];
                        for (int i = 0; i < mTypeDistrictArray.length; i++) {
                            mTypeDistrictArray2[i] = mTypeDistrictArray[i][2];
                            Log.i("TypeDistrict", mTypeDistrictArray2[i].toString());
                        }
                        ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_dropdown_item_1line,
                                mTypeDistrictArray2);
                        autoCompleteDistrict.setThreshold(1);
                        autoCompleteDistrict.setAdapter(adapterDistrict);
                    } else {
                        Log.i("DistrictArray", "null");
                    }
                }
            }
        });
        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(new ReceiveOnClickListener());
        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        btn_clear_txt_10 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_10);

        edtVehicleDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtVehicleDetail);
        btn_clear_txt_11 = (Button) viewReceiveCSI.findViewById(R.id.btn_clear_txt_11);

        listViewSufferer = (ListView) viewReceiveCSI
                .findViewById(R.id.listViewSufferer);
        linearLayoutAddSufferer = viewReceiveCSI
                .findViewById(R.id.linearLayoutAddSufferer);
        linearLayoutAddSufferer.setVisibility(View.GONE);

        listViewSufferer.setOnTouchListener(new ListviewSetOnTouchListener());
       //
        // ShowListSufferer(reportID);
        btnAddSufferer = (Button) viewReceiveCSI.findViewById(R.id.btnAddSufferer);
        btnAddSufferer.setOnClickListener(new ReceiveOnClickListener());

        /*listViewInvestigator = (ListView) viewReceiveCSI
                .findViewById(R.id.listViewInvestigator);
        linearLayoutInvestigator = viewReceiveCSI
                .findViewById(R.id.linearLayoutInvestigator);*/
        //linearLayoutInvestigator.setVisibility(View.GONE);

       // listViewInvestigator.setOnTouchListener(new ListviewSetOnTouchListener());
        // ผู้ตรวจสถานที่เกิดเหตุ

       // ShowListInvestigatorInCase(reportID);

        //btnAddInvestigator = (Button) viewReceiveCSI.findViewById(R.id.btnAddInvestigator);
       // btnAddInvestigator.setOnClickListener(new ReceiveOnClickListener());

       /* btnShowInvestigator = (Button) viewReceiveCSI.findViewById(R.id.btnShowInvestigator);
        btnShowInvestigator.setOnClickListener(new ReceiveOnClickListener());
        editScheduleInvestDate = (TextView)viewReceiveCSI.findViewById(R.id.editScheduleInvestDate);
        editScheduleInvestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("editScheduleInvestDate", "null");
                DateDialog dialogScheduleInvestDate = new DateDialog(view);
                dialogScheduleInvestDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });*/
       // String UpdateDataDate[] = getDateTime.updateDataDateTime();
       // editScheduleInvestDate.setText(UpdateDataDate[0]);

        fabBtnRec = (FloatingActionButton) viewReceiveCSI.findViewById(R.id.fabBtnRec);
        fabBtnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    saveAllReceiveData();


            }
        });


        return viewReceiveCSI;
    }

    private boolean saveAllReceiveData() {

        String sCaseTel = "";

        //  Log.i("SelectedInspectorID", SelectedInspectorID);
        // new SaveInspectorReceivingCase().execute(reportID, SelectedInspectorID);

        // การรับเเจ้ง SelectedTypeReceiveName
        //sStationName   SelectedPoliceStationID
        sCaseTel = editTextPhone1.getText().toString();
        // Address
        sAddrDetail = "";
        if (editAddrDetail.getText().toString().length() != 0) {
            sAddrDetail = editAddrDetail.getText().toString();
        }
        sHouseNo = "";
        if (editHouseNo.getText().toString().length() != 0) {
            sHouseNo = editHouseNo.getText().toString();
        }
        sVillageNo = "";
        if (editVillageNo.getText().toString().length() != 0) {
            sVillageNo = editVillageNo.getText().toString();

        }
        sVillageName = "";
        if (editVillageName.getText().toString().length() != 0) {
            sVillageName = editVillageName.getText().toString();
        }
        sLane = "";
        if (editLane.getText().toString().length() != 0) {
            sLane = editLane.getText().toString();
        }
        sRoad = "";
        if (editRoad.getText().toString().length() != 0) {
            sRoad = editRoad.getText().toString();
        }
        sDistrict = "";
        if (autoCompleteDistrict.getText().toString().length() != 0) {
            sDistrict = autoCompleteDistrict.getText().toString();
        }
        sAmphur = "";
        if (autoCompleteAmphur.getText().toString().length() != 0) {
            sAmphur = autoCompleteAmphur.getText().toString();
        }
        sProvinceName = "";
        if (autoCompleteProvince2.getText().toString().length() != 0) {
            sProvinceName = autoCompleteProvince2.getText().toString();
        }

        sPostalCode = "";

        sReceivingCaseDate = editReceiveCaseDate.getText().toString();
        sReceivingCaseDate_New =  getDateTime.changeDateFormatToDB(sReceivingCaseDate);

        sReceivingCaseTime = editReceiveCaseTime.getText().toString();

        sHappenCaseDate = editHappenCaseDate.getText().toString();
        sHappenCaseDate_New =  getDateTime.changeDateFormatToDB(sHappenCaseDate);

        sHappenCaseTime = editHappenCaseTime.getText().toString();

        sKnowCaseDate = editKnowCaseDate.getText().toString();
        sKnowCaseDate_New =  getDateTime.changeDateFormatToDB(sKnowCaseDate);

        sKnowCaseTime = editKnowCaseTime.getText().toString();

           sCircumstanceOfCaseDetail = "";
        if (editCircumstanceOfCaseDetail.getText().toString().length() != 0) {
            sCircumstanceOfCaseDetail = editCircumstanceOfCaseDetail.getText().toString();
        }
        // edtVehicleDetail.getText().toString();
        SelectedPoliceStationID = String
                .valueOf(spnLocatePolice
                        .getSelectedItem());
//        new SaveLocatePolice().execute(reportID, SelectedPoliceStationID);
        Log.i("SaveLocatePolice", SelectedPoliceStationID);


        Log.i("CaseDate", sReceivingCaseDate_New+" "+sHappenCaseDate_New+" "+sKnowCaseDate_New);


        new saveData().execute(reportID,
                "",
                sCaseTel,
                SelectedPoliceStationID,
                sAddrDetail,
                sHouseNo, sVillageNo, sVillageName, sLane, sRoad,
                sDistrict, sAmphur, sProvinceName, sPostalCode,
                sReceivingCaseDate_New, sReceivingCaseTime, sHappenCaseDate_New,
                sHappenCaseTime, sKnowCaseDate_New, sKnowCaseTime,

                sCircumstanceOfCaseDetail,
                updateDT[0],
                updateDT[1]);


        return true;

    }

    private void ShowListInvestigatorSchedule(String sScheduleInvestDate, String reportID) {
        // TODO Auto-generated method stub

        InvestigatorList = mDbHelper.SelectInvestigatorInCase(sScheduleInvestDate,reportID);
        if (InvestigatorList.size() != 0) {
            Log.i("Investigator", "showlist");
            linearLayoutInvestigator.setVisibility(View.VISIBLE);
        } else {
            Log.i("Investigator", "no");
            linearLayoutInvestigator.setVisibility(View.GONE);
        }
        listViewInvestigator.setAdapter(new InvestigatorInCaseAdapter(getActivity()));
       // ShowListInvestigatorSchedule(reportID);

    }

    @SuppressLint("InflateParams")
    public class InvestigatorInCaseAdapter extends BaseAdapter {
        private Context context;

        public InvestigatorInCaseAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {

            // TODO Auto-generated method stub
            return InvestigatorList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_investigatorincase,
                        null);

            }

            // Colrank
            TextView txtCode = (TextView) convertView
                    .findViewById(R.id.txtInvestigatorRank);
            txtCode.setText(InvestigatorList.get(position).get("Rank"));

            // txtFName
            TextView txtInvestigatorFName = (TextView) convertView
                    .findViewById(R.id.txtInvestigatorFName);
            TextView txtInvestigatorLName = (TextView) convertView
                    .findViewById(R.id.txtInvestigatorLName);
            txtInvestigatorFName.setText(InvestigatorList.get(position).get(
                    "FirstName"));
            txtInvestigatorLName.setText(InvestigatorList.get(position).get("LastName"));
            return convertView;

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

    public class ReceiveOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
           /* if (v == btnAddInvestigator) {
                Log.i("Investigator1", "showlist");
                createdDialog(DIALOG_SelectDataInvestigator).show();
            }
            if (v == btnShowInvestigator) {

                String sScheduleInvestDate = editScheduleInvestDate.getText().toString();
                Log.i("editScheduleInvestDate", sScheduleInvestDate);

                ShowListInvestigatorSchedule(sScheduleInvestDate, null);
            }
            */

            if (v == btnAddSufferer) {
                Log.i("AddSufferer1", "showlist");
                viewByIdaddsufferer = (ViewGroup) v.findViewById(R.id.layout_addsufferer_dialog);

                createdDialog(DIALOG_AddSufferer).show();
            }
            if (v == btn_clear_txt_1) {
                editAddrDetail.setText("");
            }
            if (v == btn_clear_txt_2) {
                editHouseNo.setText("");
            }
            if (v == btn_clear_txt_3) {
                editVillageNo.setText("");
            }
            if (v == btn_clear_txt_4) {
                editVillageName.setText("");
            }
            if (v == btn_clear_txt_5) {
                editLane.setText("");
            }
            if (v == btn_clear_txt_6) {
                editRoad.setText("");
            }
            if (v == btn_clear_txt_7) {
                autoCompleteProvince2.setText("");
            }
            if (v == btn_clear_txt_8) {
                autoCompleteAmphur.setText("");
            }
            if (v == btn_clear_txt_9) {
                autoCompleteDistrict.setText("");
            }
            if (v == btn_clear_txt_10) {
                editCircumstanceOfCaseDetail.setText("");
            }
            if (v == btn_clear_txt_11) {
                edtVehicleDetail.setText("");
            }
           /* if (v == btn_clear_txt_12) {
                autoSelectedInspector.setText("");
            }*/
            if (v == btnButtonSearchMap) {
                String sLocaleName = "";
                String sHouseNo = "";
                String sVillageNo = "";
                String sVillageName = "";
                String sLane = "";
                String sRoad = "";
                String sDistrict = "";
                String sAmphur = "";
                String sProvince = "";

                if (editAddrDetail.getText().toString().length() != 0) {
                    sLocaleName = editAddrDetail.getText().toString();
                }
                if (editHouseNo.getText().toString().length() != 0) {
                    sHouseNo = editHouseNo.getText().toString();
                }
                if (editVillageNo.getText().toString().length() != 0) {
                    sVillageNo = editVillageNo.getText().toString();
                }
                if (editVillageName.getText().toString().length() != 0) {
                    sVillageName = editVillageName.getText().toString();
                }
                if (editLane.getText().toString().length() != 0) {
                    sLane = editLane.getText().toString();
                }
                if (editRoad.getText().toString().length() != 0) {
                    sRoad = editRoad.getText().toString();
                }
                if (autoCompleteDistrict.getText().toString().length() != 0) {
                    sDistrict = autoCompleteDistrict.getText().toString();
                }
                if (autoCompleteAmphur.getText().toString().length() != 0) {
                    sAmphur = autoCompleteAmphur.getText().toString();
                }
                if (autoCompleteProvince2.getText().toString().length() != 0) {
                    sProvince = autoCompleteProvince2.getText().toString();
                }
                if (valueLat.getText().toString().length() != 0) {
                    sLatitude = valueLat.getText().toString();
                }
                if (valueLong.getText().toString().length() != 0) {
                    sLongitude = valueLong.getText().toString();
                }
                Intent showMapActivity = new Intent(getActivity(), MapActivity.class);
                showMapActivity.putExtra("reportid", reportID);
                showMapActivity.putExtra("LocaleName", sLocaleName);
                showMapActivity.putExtra("houseNo", sHouseNo);
                showMapActivity.putExtra("villageNo", sVillageNo);
                showMapActivity.putExtra("villageName", sVillageName);
                showMapActivity.putExtra("lane", sLane);
                showMapActivity.putExtra("road", sRoad);
                showMapActivity.putExtra("District", sDistrict);
                showMapActivity.putExtra("Amphur", sAmphur);
                showMapActivity.putExtra("province", sProvince);
                showMapActivity.putExtra("Latitude", sLatitude);
                showMapActivity.putExtra("Longitude", sLongitude);

                startActivity(showMapActivity);

            }

        }
    }

    protected Dialog createdDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder;
        switch (id) {

            case DIALOG_AddSufferer:
                Log.i("AddSufferer2", "showlist");
                builder = new AlertDialog.Builder(getActivity());

                final LayoutInflater inflaterDialogSufferer = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View ViewlayoutSufferer = inflaterDialogSufferer
                        .inflate(
                                R.layout.add_sufferer_dialog,
                                viewByIdaddsufferer);
                builder.setIcon(android.R.drawable.btn_star_big_on);
                builder.setTitle("เพิ่มข้อมูลผู้เสียหาย");
                builder.setView(ViewlayoutSufferer);

                final Spinner spinnerAntecedent = (Spinner) ViewlayoutSufferer
                        .findViewById(R.id.spinnerAntecedent);
                final EditText editSuffererFirstName = (EditText) ViewlayoutSufferer
                        .findViewById(R.id.editSuffererFirstName);

                final EditText editSuffererLastName = (EditText) ViewlayoutSufferer
                        .findViewById(R.id.editSuffererLastName);

                final EditText editSuffererAge = (EditText) ViewlayoutSufferer
                        .findViewById(R.id.editSuffererAge);
                final EditText editTextSuffererPhone1 = (EditText) ViewlayoutSufferer
                        .findViewById(R.id.editTextSuffererPhone1);
                final EditText editTextSuffererMobile1 = (EditText) ViewlayoutSufferer
                        .findViewById(R.id.editTextSuffererMobile1);

                final AutoCompleteTextView autoCompleteSuffererStatus = (AutoCompleteTextView) ViewlayoutSufferer
                        .findViewById(R.id.autoCompleteSuffererStatus);
                String[] mSuffererStatusArray;
                mSuffererStatusArray = getResources().getStringArray(
                        R.array.suffererStatus);
                ArrayAdapter<String> adapterSuffererStatus = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        mSuffererStatusArray);

                autoCompleteSuffererStatus.setThreshold(1);
                autoCompleteSuffererStatus.setAdapter(adapterSuffererStatus);


                builder.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String sSuffererAntecedent, sSuffererFirstName, sSuffererLastName, sSuffererAge, sSuffererTel1, sSuffererTel2, sSuffererStatus = "";

                                sSuffererAntecedent = String
                                        .valueOf(spinnerAntecedent
                                                .getSelectedItem());

                                sSuffererFirstName = editSuffererFirstName
                                        .getText().toString();
                                sSuffererLastName = editSuffererLastName
                                        .getText().toString();
                                sSuffererAge = editSuffererAge.getText()
                                        .toString();
                                sSuffererTel1 = editTextSuffererPhone1
                                        .getText().toString();
                                sSuffererTel2 = editTextSuffererMobile1
                                        .getText().toString();
                                sSuffererStatus = autoCompleteSuffererStatus
                                        .getText().toString();
                                Log.i("Sufferer", sSuffererTel1 + " " + sSuffererTel2);
                                Context context = getActivity()
                                        .getApplicationContext();
                                Toast.makeText(context,
                                        "เพิ่มข้อมูลเรียบร้อยเเล้ว",
                                        Toast.LENGTH_LONG).show();
                                saveDataAddSufferer(
                                        reportID, "",
                                        sSuffererAntecedent,
                                        sSuffererFirstName, sSuffererLastName,
                                        sSuffererAge, sSuffererTel1,
                                        sSuffererTel2, sSuffererStatus);

                                dialog.dismiss();

                            }

                        })
                        // Button Cancel
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                dialog = builder.create();


                break;
            default:
                dialog = null;
        }
        return dialog;

    }

    public class ReceiveOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {
 case R.id.spnLocatePolice:
                    //make sure the animal was already selected during the onCreate
                    if (selectedLocatePolice != null) {
                        SelectedPoliceStationID = mTypePoliceStationArray[pos][3];
                        Log.i("LocatePolice", SelectedPoliceStationID + " " + selectedItem);
                    }

                    selectedLocatePolice = selectedItem;
                    new SaveLocatePolice().execute(reportID, SelectedPoliceStationID);

                    break;

            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }


    class showScheduleInvestOfCase extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }


        @Override
        protected String[] doInBackground(String... params) {
            String[] arrData = {""};
            arrData = mDbHelper.SelectScheduleInvestDate(params[0]);
            if (arrData != null) {


                Log.i("scheduleInvestDate", String.valueOf(arrData.length) + "/" + arrData[0]+ "/" + arrData[1]);
            } else {
                arrData=null;
                Log.i("ScheduleInvestDate","nohave");
            }

            return arrData;
        }

        protected void onPostExecute(String[] arrData) {

            if (arrData != null) {
                //selectScheduleID = arrData[0];
               editScheduleInvestDate.setText(arrData[1]);
                ShowListInvestigatorSchedule(arrData[1], reportID);

            } else {
                Log.i("ScheduleInvestDate", "no have");
            }

        }
    }

    class showData extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }


        @Override
        protected String[] doInBackground(String... params) {
            String[] arrData = {""};
            arrData = mDbHelper.SelectDataCaseScene(params[0]);


            return arrData;
        }

        protected void onPostExecute(String[] arrData) {
            if (arrData != null) {


                edtUpdateDateTime2.setText("อัพเดทข้อมูลล่าสุดเมื่อวันที่ " + getDateTime.changeDateFormatToCalendar(arrData[7]) + " เวลา " + arrData[8]);


                if (arrData[31] != null) {

                        editTextPhone1.setText(arrData[31]);



                } else {


                }

                editAddrDetail.setText(arrData[6]);
                editHouseNo.setText(arrData[32]);
                editVillageNo.setText(arrData[33]);
                editVillageName.setText(arrData[34]);
                editLane.setText(arrData[35]);
                editRoad.setText(arrData[36]);
                autoCompleteDistrict.setText(arrData[37]);
                autoCompleteAmphur.setText(arrData[13]);
                autoCompleteProvince2.setText(arrData[38]);

                editCircumstanceOfCaseDetail.setText(arrData[41]);
                String zerodate = "0000-00-00";
                if (arrData[21] != null) {

                    if (arrData[21].length() == 10) {
                        if(arrData[21].equals(zerodate)){

                        }else {
                            editReceiveCaseDate.setText(getDateTime.changeDateFormatToCalendar(arrData[21]));
                            Log.i("editReceiveCaseDate1 ", getDateTime.changeDateFormatToCalendar(arrData[21]));
                        }
                    } else {
                        // editCompleteSceneDate.setText("date3");
                        editReceiveCaseDate.setText(arrData[21]);
                        Log.i("editReceiveCaseDate2 ", arrData[21]);
                    }

                }
                if(arrData[20] != null){
                if(arrData[20].length() > 1) {
                    editReceiveCaseTime.setText(arrData[20]);
                }}
                if (arrData[19] != null) {
                    if (arrData[19].length() == 10) {
                        if(arrData[19].equals(zerodate)){

                        }else {
                            editHappenCaseDate.setText(getDateTime.changeDateFormatToCalendar(arrData[19]));
                        }

                    } else {
                        // editCompleteSceneDate.setText("date3");
                        editHappenCaseDate.setText(arrData[19]);
                    }


                }
                if(arrData[18] != null){
                if(arrData[18].length() > 1) {
                    editHappenCaseTime.setText(arrData[18]);
                }}
                if (arrData[17] != null) {
                    if (arrData[17].length() == 10) {
                        if(arrData[17].equals(zerodate)){

                        }else {
                            editKnowCaseDate.setText(getDateTime.changeDateFormatToCalendar(arrData[17]));
                        }
                    } else {
                        editKnowCaseDate.setText(arrData[17]);
                    }


                }
                if(arrData[16] != null){
                if(arrData[16].length() > 1) {
                    editKnowCaseTime.setText(arrData[16]);
                }}


                if (arrData[39] != null) {
                    valueLat.setText(arrData[39]);
                }
                if (arrData[40] != null) {
                    valueLong.setText(arrData[40]);
                }

                if (arrData[22] != null) {
                    Log.i("adapterPoliceStation 1", arrData[22]);
                    if(adapterPoliceStation != null) {
                        for (int i = 0; i < adapterPoliceStation.getCount(); i++) {
                            if (arrData[22].trim().equals(adapterPoliceStation.getItem(i).toString())) {
                                spnLocatePolice.setSelection(i);
                                break;
                            }
                        }
                    }else{
                        Toast.makeText(getActivity()
                                        .getApplicationContext(),
                                "กรุณาโหลดข้อมูลทั่วไปก่อน",
                                Toast.LENGTH_LONG).show();
                        Log.i("adapterPoliceStation 2", "Null!! ");
                    }
                }
            } else {
                Log.i("Recieve ", "Null!! ");
            }
        }
    }

    class saveData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";
            Log.i("updateCaseScene ", params[3]);
            long updateCaseScene = mDbHelper.updateCaseSceneReceive(params[0], params[1], params[2],
                    params[3], params[4], params[5], params[6], params[7],
                    params[8], params[9], params[10], params[11], params[12], params[13], params[14],
                    params[15], params[16], params[17], params[18], params[19], "",
                    "", params[20], params[21], params[22]);

            if (updateCaseScene <= 0) {
                Log.i("update Receive", "error");
                arrData = "error";
            } else {
                Log.i("update Receive", "save");
                arrData = "save";
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            if (arrData == "save") {
                message = "บันทึกข้อมูลเรียบร้อยแล้ว";

            } else {
                message = "เกิดข้อผิดพลาด";

            }
            Log.i("save sum", message);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        }
    }
/*
    class SaveInspectorReceivingCase extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";

            long SaveInspector = mDbHelper.SaveReceivingCase(params[0], params[1],params[2], params[3]);
            Log.i("SaveInspector async", "save " + params[0] + " " + params[1]);
            if (SaveInspector <= 0) {

                arrData = "error";
            } else {
                arrData = "save";
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            if (arrData == "save") {
                Log.i("SaveInspector", "save");
                //String[] arrData1 = {""};
                //arrData1 = mDbHelper.SelectReceivingCase(reportID);
                //Log.i("InspectorArray",String.valueOf(arrData1.length)+"/"+arrData1[0]);

            } else {
                Log.i("SaveInspector", "error");

            }

            //Snackbar.make(fabBtn.getRootView(), message, Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();

        }
    }*/

    class SaveLocatePolice extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";

            long SaveLocatePolice = mDbHelper.SaveLocatePolice(params[0], params[1]);
            // Log.i("SaveLocatePolice",  params[1]);
            if (SaveLocatePolice <= 0) {

                arrData = "error";
            } else {
                arrData = "save";
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            if (arrData == "save") {
                Log.i("SaveLocatePolice", "save");


            } else {
                Log.i("SaveLocatePolice", "error");

            }

            //Snackbar.make(fabBtn.getRootView(), message, Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();

        }
    }


    public void onStart() {
        super.onStart();
        Log.i("Check", "onStart recieve");
        String[] curDateTime = getDateTime.getDateTimeCurrent();
        int year = Integer.parseInt(curDateTime[0])-543;
        curDateTime[0] = String.valueOf(year);
        //editReceiveCaseDate.setText(curDateTime[2] + "/" + curDateTime[1] + "/" + curDateTime[0]);
        //editReceiveCaseTime.setText(curDateTime[3] + ":" + curDateTime[4]);

       // new showData().execute(reportID);
        // new showScheduleInvestOfCase().execute(reportID);

    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause receive");


            saveAllReceiveData();


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
        //saveAllReceiveData();
    }

    public void saveDataAddSufferer(String sCaseReportID, String sSSN,
                                    String sSuffererAntecedent, String sSuffererFirstName,
                                    String sSuffererLastName, String sSuffererAge,
                                    String sSuffererTel1, String sSuffererTel2, String sSuffererStatus) {
        // TODO Auto-generated method stub
        String curDateTime[] = getDateTime.getDateTimeCurrent();
        Log.i("CurrentDateTime", curDateTime[2]  + curDateTime[1] + curDateTime[0]);
        String sSuffererID = "SUF" +"_"+ curDateTime[2]  + curDateTime[1] + curDateTime[0] + "_"+curDateTime[3]+curDateTime[4]+curDateTime[5];

        long saveStatus = mDbHelper.saveDataSufferer(sCaseReportID,
                sSuffererID, "", sSuffererAntecedent, sSuffererFirstName,
                sSuffererLastName, sSuffererAge, sSuffererTel1, sSuffererTel2,
                sSuffererStatus);
        if (saveStatus <= 0) {
            Log.i("saveDataSufferer", "Error!! ");
        } else {
            Log.i("saveDataSufferer", "OK!! ");
            linearLayoutAddSufferer.setVisibility(View.VISIBLE);
            ShowListSufferer(reportID);
        }

    }

    public void ShowListSufferer(String reportID) {
        // TODO Auto-generated method stub
        suffererList = mDbHelper.SelectDataSuffererinCase(reportID);
        if (suffererList.size() != 0) {
            linearLayoutAddSufferer.setVisibility(View.VISIBLE);
        } else {
            linearLayoutAddSufferer.setVisibility(View.GONE);
        }
        listViewSufferer.setAdapter(new SuffererAdapter(getActivity()));
    }

    public class SuffererAdapter extends BaseAdapter {
        private Context context;

        public SuffererAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return suffererList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_sufferer, null);

            }

            final String sSuffererID = suffererList.get(position).get(
                    "SuffererID");
            String[] sAntecedentSufferer = getResources().getStringArray(
                    R.array.antecedent);

            final Spinner spinnerAntecedent = (Spinner) convertView
                    .findViewById(R.id.spinnerAntecedent);
            spinnerAntecedent
                    .setSelection(Arrays.asList(sAntecedentSufferer).indexOf(
                            suffererList.get(position).get("SuffererPrename")));

            final EditText editSuffererFirstName = (EditText) convertView
                    .findViewById(R.id.editSuffererFirstName);
            editSuffererFirstName.setText(suffererList.get(position).get(
                    "SuffererFirstName"));

            final EditText editSuffererLastName = (EditText) convertView
                    .findViewById(R.id.editSuffererLastName);
            editSuffererLastName.setText(suffererList.get(position).get(
                    "SuffererLastName"));
            final EditText editSuffererAge = (EditText) convertView
                    .findViewById(R.id.editSuffererAge);
            editSuffererAge.setText(suffererList.get(position).get(
                    "SuffererAge"));
            final EditText editTextSuffererPhone1 = (EditText) convertView
                    .findViewById(R.id.editTextSuffererPhone1);

            final EditText editTextSuffererMobile1 = (EditText) convertView
                    .findViewById(R.id.editTextSuffererMobile1);

            if(suffererList.get(position).get("SuffererTelephone").length()==0){

            }else {
                editTextSuffererPhone1.setText(suffererList.get(position).get(
                        "SuffererTelephone"));
            }

            if(suffererList.get(position).get("SuffererTelMobile").length()==0){

            }else {
                editTextSuffererMobile1.setText(suffererList.get(position).get(
                        "SuffererTelMobile"));
            }

            final AutoCompleteTextView autoCompleteSuffererStatus = (AutoCompleteTextView) convertView
                    .findViewById(R.id.autoCompleteSuffererStatus);
            autoCompleteSuffererStatus.setText(suffererList.get(position).get(
                    "SuffererStatus"));
            String[] mSuffererStatusArray;
            mSuffererStatusArray = getResources().getStringArray(
                    R.array.suffererStatus);
            ArrayAdapter<String> adapterSuffererStatus = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mSuffererStatusArray);
            autoCompleteSuffererStatus.setThreshold(1);
            autoCompleteSuffererStatus.setAdapter(adapterSuffererStatus);

            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adbEdit.setTitle("แก้ไขข้อมูล");
                    adbEdit.setMessage("ยืนยันการแก้ไขข้อมูล");
                    adbEdit.setNegativeButton("Cancel", null);
                    adbEdit.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long saveStatus = mDbHelper
                                            .updateDataSufferer(
                                                    sSuffererID,
                                                    String.valueOf(spinnerAntecedent
                                                            .getSelectedItem()),
                                                    editSuffererFirstName
                                                            .getText()
                                                            .toString(),
                                                    editSuffererLastName
                                                            .getText()
                                                            .toString(),
                                                    editSuffererAge.getText()
                                                            .toString(),
                                                    editTextSuffererPhone1.getText().toString(),
                                                    editTextSuffererMobile1.getText().toString(),
                                                    autoCompleteSuffererStatus.getText().toString());
                                    if (saveStatus <= 0) {
                                        Log.i("updateDataSufferer", "Error!! ");
                                    } else {
                                        Log.i("updateDataSufferer", "ok!! ");
                                        Toast.makeText(getActivity(),
                                                "แก้ไขเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                    adbEdit.show();
                }
            });
            // imgDelete
            ImageButton imgDelete = (ImageButton) convertView
                    .findViewById(R.id.imgDelete);
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    getActivity());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // final String inspectorID =
                    // InspectorList.get(position).get(
                    // "InspectorID");
                    adb.setTitle("ลบข้อมูล");
                    adb.setMessage("ยืนยันการลบข้อมูล[ข้อมูลผู้เสียหายชื่อ  "
                            + suffererList.get(position).get(
                            "SuffererFirstName") + "]");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long flg = mDbHelper
                                            .DeleteSelectedSufferer(sSuffererID);
                                    if (flg > 0) {

                                        Toast.makeText(getActivity(),
                                                "ลบข้อมูลเรียบร้อยแล้ว",
                                                Toast.LENGTH_LONG).show();
                                        ShowListSufferer(getArguments()
                                                .getString(reportID));
                                    } else {
                                        Toast.makeText(getActivity(),
                                                "เกิดการผิดพลาด",
                                                Toast.LENGTH_LONG).show();
                                        Log.i("DeleteSelectedSufferer", "Error");
                                    }

                                }

                            });
                    adb.show();
                }
            });

            return convertView;

        }

    }

}