package com.scdc.csiapp.inqmain;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.AcceptListFragment;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;


public class SummaryEmerTabFragment extends Fragment {
    private static final String TAG = "DEBUG-SummaryEmerTabFragment";
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    DBHelper dbHelper;
    SQLiteDatabase db;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String officialID, reportID;
    ArrayAdapter<String> adapterCaseType, adapterSubCaseType;
    EditText edtReportNo;
    FragmentManager mFragmentManager;
    AcceptListFragment acceptListFragment;
    Spinner spnCaseType, spnSubCaseType;
    TextView edtStatus, edtInvestDateTime, edtUpdateDateTime,
            txtButtonCount1, txtButtonCount2, txtButtonCount3, txtButtonCount4, txtButtonCount5;
    String[] updateDT, selectedCaseType, selectedSubCaseType;
    String message = "";
    String[][] mTypeAgencyArray, mTypeCenterArray = null;
    String[] mTypeAgencyArray2, mTypeCenterArray2, mAgencyID, mCenterID = null;
    ArrayAdapter<String> adapterSCDCcenter, adapterSCDCagency;
    private String selectedAgency, SelectedAgencyID, selectedCenter, SelectedCenterID = null;
    private View mViewBtnSaveServer, mViewBtnFullReport, mViewBtnTransReport, layoutButton1, layoutButton2, layoutButton3, layoutButton4, layoutButton5;
    Spinner spnSCDCcenterType, spnSCDCagencyType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);
// Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Context context = viewSummaryCSI.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        acceptListFragment = new AcceptListFragment();
        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();

        officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;

        cd = new ConnectionDetector(getActivity());

        String noticecaseid = EmergencyTabFragment.NoticeCaseID;
        Log.i(TAG,  "noticecaseid " + noticecaseid);

        updateDT = getDateTime.getDateTimeNow();

        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);



        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        edtReportNo.setVisibility(View.GONE);

        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);
        
        edtInvestDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtInvestDateTime);
        edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);

        TextView txtInvestDateTimeTitle = (TextView) viewSummaryCSI.findViewById(R.id.txtInvestDateTimeTitle);
        txtInvestDateTimeTitle.setText("วันเวลาแจ้งเหตุ:");
        final String[] CaseType = getResources().getStringArray(R.array.casetype);
        adapterCaseType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, CaseType);
        spnCaseType.setAdapter(adapterCaseType);
        selectedCaseType = new String[1];
        spnCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCaseType[0] = CaseType[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedCaseType[0] = CaseType[0];
            }
        });

        final String[] SubCaseType = getResources().getStringArray(R.array.subcasetypeproperties);
        adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SubCaseType);
        spnSubCaseType.setAdapter(adapterSubCaseType);
        selectedSubCaseType = new String[1];
        spnSubCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubCaseType[0] = SubCaseType[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedSubCaseType[0] = SubCaseType[0];
            }
        });
        //View linearLayoutDocfiles= viewSummaryCSI.findViewById(R.id.linearLayoutDocfiles);
        //linearLayoutDocfiles.setVisibility(View.GONE);
        mViewBtnSaveServer = viewSummaryCSI.findViewById(R.id.layoutButtonServer);
        TextView txtButtonServer1 = (TextView) viewSummaryCSI.findViewById(R.id.txtButtonServer1);
        txtButtonServer1.setText("แจ้งเหตุ");
        mViewBtnTransReport = viewSummaryCSI.findViewById(R.id.layoutButtonTransReport);
        mViewBtnTransReport.setVisibility(View.GONE);

/*
        layoutButton1 = viewSummaryCSI.findViewById(R.id.layoutButton1);
        layoutButton2 = viewSummaryCSI.findViewById(R.id.layoutButton2);
        layoutButton3 = viewSummaryCSI.findViewById(R.id.layoutButton3);
        layoutButton4 = viewSummaryCSI.findViewById(R.id.layoutButton4);
        layoutButton5 = viewSummaryCSI.findViewById(R.id.layoutButton5);*/
        //  mViewBtnSaveServer.setOnClickListener(new SummaryOnClickListener());
        //  mViewBtnTransReport.setOnClickListener(new SummaryOnClickListener());
   /*     layoutButton1.setOnClickListener(new SummaryOnClickListener());
        layoutButton2.setOnClickListener(new SummaryOnClickListener());
        layoutButton3.setOnClickListener(new SummaryOnClickListener());
        layoutButton4.setOnClickListener(new SummaryOnClickListener());
        layoutButton5.setOnClickListener(new SummaryOnClickListener());
*/
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new saveData().execute(reportID,
//                        String.valueOf(edtReportNo.getText()),
//                        selectedCaseType[0],
//                        selectedSubCaseType[0],
//                        updateDT[0],
//                        updateDT[1]);


            }
        });
        // new showData().execute(reportID);
        return viewSummaryCSI;
    }

//    class showData extends AsyncTask<String, Void, String[]> {
//        @Override
//        protected void onPreExecute() {
//            // Create Show ProgressBar
//
//        }
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            String[] arrData = {""};
//            arrData = mDbHelper.SelectDataCaseScene(params[0]);
//
//
//            return arrData;
//        }
//
//        protected void onPostExecute(String[] arrData) {
//            if (arrData != null) {
//                Log.i("edtStatus", arrData[30]);
//
//                if (arrData[30].equals("investigating")) {
//                    edtStatus.setText("กำลังดำเนินการตรวจ");
//
//                } else if (arrData[30].equals("waiting")) {
//                    edtStatus.setText("ยังไม่ส่งแจ้งเหตุ");
//
//                } else if (arrData[30].equals("receiving")) {
//                    edtStatus.setText("แจ้งเหตุแล้ว รอจ่ายงาน");
//                    mViewBtnSaveServer.setVisibility(View.GONE);
//
//                } else if (arrData[30].equals("investigated")) {
//                    edtStatus.setText("ตรวจเสร็จแล้ว");
//
//                }
//                if (arrData[21] != null) {
//
//                    edtInvestDateTime.setText(getDateTime.changeDateFormatToCalendar(arrData[21]) + " เวลาประมาณ " + arrData[20]);
//
//                } else {
//                    edtInvestDateTime.setText("-");
//                }
//                if (arrData[7] != null) {
//                    edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(arrData[7]) + " เวลาประมาณ " + arrData[8]);
//                } else {
//                    edtInvestDateTime.setText("-");
//                }
//                if (arrData[0] != null) {
//                    for (int i = 0; i < adapterCaseType.getCount(); i++) {
//                        if (arrData[0].trim().equals(adapterCaseType.getItem(i).toString())) {
//                            spnCaseType.setSelection(i);
//                            break;
//                        }
//                    }
//                }
//                if (arrData[26] != null) {
//                    for (int i = 0; i < adapterSubCaseType.getCount(); i++) {
//                        if (arrData[26].trim().equals(adapterSubCaseType.getItem(i).toString())) {
//                            spnSubCaseType.setSelection(i);
//                            break;
//                        }
//                    }
//                }
//
//            }
//        }
//    }


    public void onStart() {
        super.onStart();
        Log.i("Check", "onStartSummary");
        //   new showData().execute(reportID);

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause sum");

    }
}