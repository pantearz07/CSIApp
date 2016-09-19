package com.scdc.csiapp.inqmain;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.scdc.csiapp.main.AcceptListFragment;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;


public class SummaryEmerTabFragment extends Fragment {
    private static final String TAG = "DEBUG-SummaryEmerTabFragment";
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    // connect sqlite

    DBHelper dbHelper;
    SQLiteDatabase db;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String officialID, noticecaseid;
    ArrayAdapter<String> adapterCaseType, adapterSubCaseType;
    EditText edtReportNo;
    FragmentManager mFragmentManager;
    AcceptListFragment acceptListFragment;
    Spinner spnCaseType, spnSubCaseType;
    TextView edtStatus, edtInvestDateTime, edtUpdateDateTime,
            txtButtonCount1, txtButtonCount2, txtButtonCount3, txtButtonCount4, txtButtonCount5;
    String[] updateDT, selectedCaseType, selectedSubCaseType;
    String message = "";
    String[][] mTypeAgencyArray, mTypeCenterArray, mCaseTypeArray, mSubCaseTypeArray;
    String[] mTypeAgencyArray2, mTypeCenterArray2, mAgencyID, mCenterID;
    ArrayAdapter<String> adapterSCDCcenter, adapterSCDCagency;
    private String selectedAgency, SelectedAgencyID, selectedCenter, SelectedCenterID = null;
    private View mViewBtnSaveServer, mViewBtnFullReport, mViewBtnTransReport, layoutButton1, layoutButton2, layoutButton3, layoutButton4, layoutButton5;
    Spinner spnSCDCcenterType, spnSCDCagencyType;
    Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);
// Permission StrictMode
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

        final Context context = viewSummaryCSI.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        acceptListFragment = new AcceptListFragment();
        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);

        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();

        dbHelper = new DBHelper(getContext());

        officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;

        cd = new ConnectionDetector(getActivity());
        String noticeCaseID = EmergencyTabFragment.tbNoticeCase.getNoticeCaseID();
        Log.i(TAG, " NoticeCaseID " + noticeCaseID);
        Log.i(TAG, " SubCaseTypeID " + EmergencyTabFragment.tbNoticeCase.getSubCaseTypeID());
//        if (snackbar == null || !snackbar.isShown()) {
//            snackbar = Snackbar.make(viewSummaryCSI, "เพิ่มรายการคดีใหม่ " + noticeCaseID, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(getString(R.string.ok), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    });
//            snackbar.show();
//        }
        updateDT = getDateTime.getDateTimeNow();

        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);

        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);

        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);

        edtInvestDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtInvestDateTime);
        edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);

        TextView txtInvestDateTimeTitle = (TextView) viewSummaryCSI.findViewById(R.id.txtInvestDateTimeTitle);
        txtInvestDateTimeTitle.setText("วันเวลาแจ้งเหตุ:");

        //View linearLayoutDocfiles= viewSummaryCSI.findViewById(R.id.linearLayoutDocfiles);
        //linearLayoutDocfiles.setVisibility(View.GONE);
        mViewBtnSaveServer = viewSummaryCSI.findViewById(R.id.layoutButtonServer);
        TextView txtButtonServer1 = (TextView) viewSummaryCSI.findViewById(R.id.txtButtonServer1);
        txtButtonServer1.setText("แจ้งเหตุ");
        mViewBtnTransReport = viewSummaryCSI.findViewById(R.id.layoutButtonTransReport);
        mViewBtnTransReport.setVisibility(View.GONE);
        // mViewBtnSaveServer.setOnClickListener(new SummaryOnClickListener());
        // mViewBtnTransReport.setOnClickListener(new SummaryOnClickListener());

        //โชว์dropdown casetype
        //ดึงค่าจาก TbCaseSceneType
        final String[] selectedCaseType = new String[1];
        final String[] selectedSubCaseType = new String[1];
        mCaseTypeArray = dbHelper.SelectCaseType();
        if (mCaseTypeArray != null) {
            String[] mCaseTypeArray2 = new String[mCaseTypeArray.length];
            for (int i = 0; i < mCaseTypeArray.length; i++) {
                mCaseTypeArray2[i] = mCaseTypeArray[i][1];
                Log.i(TAG + " show mCaseTypeArray", mCaseTypeArray2[i].toString());
            }
            ArrayAdapter<String> adapterTypeCase = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mCaseTypeArray2);
            spnCaseType.setAdapter(adapterTypeCase);
        } else {
            Log.i(TAG + " show mCaseTypeArray", "null");
        }
        if (EmergencyTabFragment.tbNoticeCase.CaseTypeID != null) {
            for (int i = 0; i < mCaseTypeArray.length; i++) {
                if (EmergencyTabFragment.tbNoticeCase.CaseTypeID.trim().equals(mCaseTypeArray[i][0].toString())) {
                    spnCaseType.setSelection(i);
                    break;
                }
            }
        }

        spnCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCaseType[0] = mCaseTypeArray[position][0];
                Log.i(TAG + " show mCaseTypeArray", selectedCaseType[0]);

                //ดึงค่าจาก TbSubCaseSceneType
                mSubCaseTypeArray = dbHelper.SelectSubCaseTypeByCaseType(selectedCaseType[0]);
                if (mSubCaseTypeArray != null) {
                    String[] mSubCaseTypeArray2 = new String[mSubCaseTypeArray.length];
                    for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                        mSubCaseTypeArray2[i] = mSubCaseTypeArray[i][2];
                        Log.i(TAG + " show mSubCaseTypeArray2", mSubCaseTypeArray2[i].toString());
                    }
                    ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, mSubCaseTypeArray2);
                    spnSubCaseType.setAdapter(adapterSubCaseType);
                } else {
                    spnSubCaseType.setAdapter(null);
                    selectedSubCaseType[0] = null;
                    Log.i(TAG + " show mSubCaseTypeArray", String.valueOf(selectedSubCaseType[0]));
                }
                if (EmergencyTabFragment.tbNoticeCase.SubCaseTypeID != null) {
                    mSubCaseTypeArray = dbHelper.SelectSubCaseType();
                    for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                        if (EmergencyTabFragment.tbNoticeCase.SubCaseTypeID.trim().equals(mSubCaseTypeArray[i][0].toString())) {
                            spnSubCaseType.setSelection(i);
                            break;
                        }
                    }
                }

                spnSubCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubCaseType[0] = mSubCaseTypeArray[position][0];
                        Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType[0]);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedSubCaseType[0] = mSubCaseTypeArray[0][0];
                        Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType[0]);
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedCaseType[0] = mCaseTypeArray[0][0];
                Log.i(TAG + " show mCaseTypeArray", selectedCaseType[0]);
                //String mSubCaseTypeArray[][]= mDbHelper.SelectSubCaseTypeByCaseType(selectedCaseType[0]);
            }
        });


        if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("investigating")) {
            edtStatus.setText("กำลังดำเนินการตรวจ");
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("notice")) {
            edtStatus.setText("แจ้งเหตุแล้ว รอจ่ายงาน");
            mViewBtnSaveServer.setVisibility(View.GONE);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("receive")) {
            edtStatus.setText("ยังไม่ส่งแจ้งเหตุ");
            mViewBtnSaveServer.setVisibility(View.GONE);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("investigated")) {
            edtStatus.setText("ตรวจเสร็จแล้ว");
        }

        if (EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate != null) {

            edtInvestDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.ReceivingCaseDate) + " เวลาประมาณ " + EmergencyTabFragment.tbNoticeCase.ReceivingCaseTime + " น.");

        } else {
            edtInvestDateTime.setText("-");
        }
        if (EmergencyTabFragment.tbNoticeCase.LastUpdateDate != null) {
            edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.LastUpdateDate) + " เวลาประมาณ " + EmergencyTabFragment.tbNoticeCase.LastUpdateTime + " น.");
        } else {
            edtUpdateDateTime.setText("-");
        }


/*
        layoutButton1 = viewSummaryCSI.findViewById(R.id.layoutButton1);
        layoutButton2 = viewSummaryCSI.findViewById(R.id.layoutButton2);
        layoutButton3 = viewSummaryCSI.findViewById(R.id.layoutButton3);
        layoutButton4 = viewSummaryCSI.findViewById(R.id.layoutButton4);
        layoutButton5 = viewSummaryCSI.findViewById(R.id.layoutButton5);
        layoutButton1.setOnClickListener(new SummaryOnClickListener());
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


        return viewSummaryCSI;
    }

    public void onStart() {
        super.onStart();
        Log.i("Check", "onStartSummary");
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause sum");

    }
}