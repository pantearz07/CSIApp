package com.scdc.csiapp.inqmain;


import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
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

    Spinner spnCaseType, spnSubCaseType;
    String selectedCaseType, selectedSubCaseType, sCaseTypeID, sSubCaseTypeID;
    TextView edtStatus, edtInvestDateTime, edtUpdateDateTime, edtInqInfo, edtInvInfo, edtPoliceStation;
    String[] updateDT;
    String message = "";
    String[][] mTypeCenterArray, mCaseTypeArray, mSubCaseTypeArray, mTypeAgencyArray;
    String[] mTypeCenterArray2, mTypeAgencyArray2, mSubCaseTypeArray2;
    ArrayAdapter<String> adapterSCDCcenter, adapterSCDCagency;

    Button btnNoticecase, btnDownloadfile;
    String noticeCaseID, sSCDCAgencyCode;
    Snackbar snackbar;
    String SelectedAgencyID, SelectedCenterID;
    Spinner spnSCDCcenterType, spnSCDCagencyType;
    String selectedCenter, selectedAgencyCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);

//โชว์ scenenoticedate & time
        final Context context = viewSummaryCSI.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();

        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        dbHelper = new DBHelper(getContext());
        officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;
        cd = new ConnectionDetector(getActivity());

        noticeCaseID = EmergencyTabFragment.tbNoticeCase.NoticeCaseID;
        Log.i(TAG, " NoticeCaseID " + noticeCaseID);

        updateDT = getDateTime.getDateTimeNow();
        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);

        View linearLayoutReportNo = (LinearLayout) viewSummaryCSI.findViewById(R.id.linearLayoutReportNo);
        linearLayoutReportNo.setVisibility(View.GONE);
        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        edtReportNo.setVisibility(View.GONE);

        edtInqInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInqInfo);
        edtInvInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInvInfo);
        edtPoliceStation = (TextView) viewSummaryCSI.findViewById(R.id.edtPoliceStation);
//สถานะคดี
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);

        edtInvestDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtInvestDateTime);
        edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);
//วันเวลาที่ผู้ตรวจสถานที่เกิดเหตุออกไปตรวจ
        TextView edtSceneNoticeDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtSceneNoticeDateTime);
        //วันเวลาที่ตรวจคดีเสร็จ
        TextView edtCompleteSceneDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtCompleteSceneDateTime);
        //วันเวลาที่แก้ไขข้อมูลล่าสุด
        TextView edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);

        if (WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode != null || WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode != "") {
            EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode;
            sSCDCAgencyCode = WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode;
        }
        btnNoticecase = (Button) viewSummaryCSI.findViewById(R.id.btnNoticecase);
        btnDownloadfile = (Button) viewSummaryCSI.findViewById(R.id.btnDownloadfile);
        btnNoticecase.setOnClickListener(new SummaryOnClickListener());
        btnDownloadfile.setOnClickListener(new SummaryOnClickListener());

        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        spnCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spnSubCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        sCaseTypeID = EmergencyTabFragment.tbNoticeCase.CaseTypeID;
        sSubCaseTypeID = EmergencyTabFragment.tbNoticeCase.SubCaseTypeID;

        //โชว์dropdown casetype
        //ดึงค่าจาก TbCaseSceneType
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
        mSubCaseTypeArray = dbHelper.SelectSubCaseType();
        Log.d(TAG, String.valueOf(mSubCaseTypeArray.length));
        if (mSubCaseTypeArray != null) {
            mSubCaseTypeArray2 = new String[mSubCaseTypeArray.length];
            for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                mSubCaseTypeArray2[i] = mSubCaseTypeArray[i][2];
                Log.i(TAG + " show mSubCaseTypeArray2", mSubCaseTypeArray2[i].toString());
            }
            ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, mSubCaseTypeArray2);
            spnSubCaseType.setAdapter(adapterSubCaseType);
        } else {
            spnSubCaseType.setAdapter(null);
            selectedSubCaseType = null;
            Log.i(TAG + " show mSubCaseTypeArray", String.valueOf(selectedSubCaseType));
        }
        //เเสดงค่าเดิม
        if (sCaseTypeID == null || sCaseTypeID.equals("") || sCaseTypeID.equals("null")) {
            spnCaseType.setSelection(0);
        } else {
            for (int i = 0; i < mCaseTypeArray.length; i++) {
                if (sCaseTypeID.trim().equals(mCaseTypeArray[i][0].toString())) {
                    spnCaseType.setSelection(i);
                    break;
                }
            }
        }
//        if (sSubCaseTypeID == null || sSubCaseTypeID.equals("") || sSubCaseTypeID.equals("null")) {
//            spnSubCaseType.setSelection(0);
//        } else {
//            for (int i = 0; i < mSubCaseTypeArray.length; i++) {
//                if (sSubCaseTypeID.trim().equals(mSubCaseTypeArray[i][0].toString())) {
//                    spnSubCaseType.setSelection(i);
//                    break;
//                }
//            }
//        }

        edtInqInfo.setText(WelcomeActivity.profile.getTbOfficial().Rank + " "
                + WelcomeActivity.profile.getTbOfficial().FirstName + " "
                + WelcomeActivity.profile.getTbOfficial().LastName + " ("
                + WelcomeActivity.profile.getTbOfficial().Position + ") โทร." +
                "" + WelcomeActivity.profile.getTbOfficial().PhoneNumber + " ");

//        if (EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID != null) {
//            String InvestigatorOfficialID = EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID;
//            Log.i(TAG, "InvestigatorOfficialID : " + InvestigatorOfficialID);
//
//            String mInvOfficialArray[] = dbHelper.SelectInvOfficial(EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID);
//            if (mInvOfficialArray != null) {
//                edtInvInfo.setText(mInvOfficialArray[4].toString() + " " + mInvOfficialArray[1].toString()
//                        + " " + mInvOfficialArray[2].toString() + " (" + mInvOfficialArray[5].toString() + ") " +
//                        mInvOfficialArray[7].toString());
//            } else {
//                edtInvInfo.setText("");
//            }
//
//        }
        edtInvInfo.setText("");

        String mTypePoliceStationArray[] = dbHelper.SelectPoliceStation(EmergencyTabFragment.tbNoticeCase.PoliceStationID);
        if (mTypePoliceStationArray != null) {
            edtPoliceStation.setText(mTypePoliceStationArray[2].toString());
        }

        if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("investigating")) {
            edtStatus.setText("กำลังดำเนินการตรวจ");
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("notice")) {
            edtStatus.setText("แจ้งเหตุแล้ว รอจ่ายงาน");
            btnNoticecase.setEnabled(false);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("receive")) {
            edtStatus.setText("รอส่งแจ้งเหตุ");
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("assign")) {
            edtStatus.setText("รอรับไปตรวจ");
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("accept")) {
            edtStatus.setText("รับเรื่องแล้ว");
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("investigated")) {
            edtStatus.setText("ตรวจเสร็จแล้ว");
        }

        //วันเวลาที่ผู้ตรวจสถานที่เกิดเหตุออกไปตรวจ
        if (EmergencyTabFragment.tbNoticeCase.SceneNoticeDate != "") {

            edtSceneNoticeDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.SceneNoticeDate) + " เวลาประมาณ " + EmergencyTabFragment.tbNoticeCase.SceneNoticeTime + " น.");

        } else {
            edtSceneNoticeDateTime.setText("-");
        }
        //วันเวลาที่ตรวจคดีเสร็จ
        if (EmergencyTabFragment.tbNoticeCase.CompleteSceneDate != "") {

            edtCompleteSceneDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.CompleteSceneDate) + " เวลาประมาณ " + EmergencyTabFragment.tbNoticeCase.CompleteSceneTime + " น.");

        } else {
            edtCompleteSceneDateTime.setText("-");
        }
        //วันเวลาที่แก้ไขข้อมูลล่าสุด
        if (EmergencyTabFragment.tbNoticeCase.LastUpdateDate != "") {
            edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.LastUpdateDate) + " เวลาประมาณ " + EmergencyTabFragment.tbNoticeCase.LastUpdateTime + " น.");
        } else {
            edtUpdateDateTime.setText("-");
        }

        if (EmergencyTabFragment.mode == "view") {
            fabBtn.setVisibility(View.GONE);
            spnCaseType.setEnabled(false);
            spnSubCaseType.setEnabled(false);
            edtReportNo.setEnabled(false);
            btnNoticecase.setEnabled(false);
        } else if (EmergencyTabFragment.mode == "edit") {
            btnDownloadfile.setEnabled(false);
        }

        fabBtn.setOnClickListener(new SummaryOnClickListener());

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

    private class SummaryOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnNoticecase) {
                Log.i(TAG, "btnNoticecase " + WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View view1 = inflater.inflate(R.layout.emergency_send_case, null);
                dialog.setMessage(getString(R.string.send_noticecase));
                dialog.setView(view1);

                spnSCDCcenterType = (Spinner) view1.findViewById(R.id.spnSCDCcenterType);
                spnSCDCagencyType = (Spinner) view1.findViewById(R.id.spnSCDCagencyType);
                //เช็คว่ามีค่า SCDCAgencyCode

                mTypeCenterArray = dbHelper.SelectAllSCDCCenter();

                if (mTypeCenterArray != null) {

                    mTypeCenterArray2 = new String[mTypeCenterArray.length];
                    for (int i = 0; i < mTypeCenterArray.length; i++) {
                        mTypeCenterArray2[i] = mTypeCenterArray[i][1] + " " + mTypeCenterArray[i][2];

                        Log.i("SelectAllSCDCCenter",
                                mTypeCenterArray2[i].toString());
                    }
                    adapterSCDCcenter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_dropdown_item_1line,
                            mTypeCenterArray2);
                    //set the view for the Drop down list
                    adapterSCDCcenter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnSCDCcenterType.setAdapter(adapterSCDCcenter);

                    if (sSCDCAgencyCode != null) {
                        String SelectSCDCCenterID[] = dbHelper.SelectSCDCCenterID(sSCDCAgencyCode);
                        Log.i("SelectSCDCCenterID", sSCDCAgencyCode + " " + SelectSCDCCenterID[0]);
                        if (SelectSCDCCenterID[0] != null) {
                            for (int i = 0; i < mTypeCenterArray.length; i++) {
                                if (SelectSCDCCenterID[0].trim().equals(mTypeCenterArray[i][0].toString())) {
                                    spnSCDCcenterType.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    Log.i(TAG, "mTypeCenterArray null");
                }
                spnSCDCcenterType.setOnItemSelectedListener(new NoticeOnItemSelectedListener());
                spnSCDCagencyType.setOnItemSelectedListener(new NoticeOnItemSelectedListener());

                dialog.setMessage("แจ้งเหตุ");
                dialog.setPositiveButton("ส่ง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // uploadDataToServer();
                        if (EmergencyTabFragment.tbNoticeCase != null) {
                            boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
                            if (isSuccess) {
                                if (snackbar == null || !snackbar.isShown()) {
                                    snackbar = Snackbar.make(rootLayout, "ส่งแจ้งเหตุเรียบร้อย"
                                                    + " " + EmergencyTabFragment.tbNoticeCase.LastUpdateDate
                                                    + " " + EmergencyTabFragment.tbNoticeCase.NoticeCaseID
                                                    + " " + EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode
                                            , Snackbar.LENGTH_INDEFINITE)
                                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            });
                                    snackbar.show();
                                }
                            }
                        }

                        //FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        //fragmentTransaction.replace(R.id.containerView, acceptListFragment).addToBackStack(null).commit();
                    }
                });
                dialog.setCancelable(true);
                dialog.setNeutralButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                dialog.create();
                dialog.show();
            }
            if (v == btnDownloadfile) {
                Log.i(TAG, "btnDownloadfile");
                // new DownloadFileAsync().execute(reportID);
                //
            }
            if (v == fabBtn) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = WelcomeActivity.profile.getSCDCAgencyCode();
                Log.i(TAG + " show SCDCAgencyCode", EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode);
                EmergencyTabFragment.tbNoticeCase.CaseStatus = "receive";
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
        }
    }

    public class EmerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selectedItem = parent.getItemAtPosition(pos).toString();
            switch (parent.getId()) {
//                case R.id.spnSCDCcenterType:
//                    selectedCenter = mTypeCenterArray[pos][0];
//                    Log.i(TAG + " show selectedCenter", selectedCenter);
//                    //
//                    mTypeAgencyArray = dbHelper.SelectSCDCAgency(selectedCenter);
//                    if (mTypeAgencyArray != null) {
//                        mTypeAgencyArray2 = new String[mTypeAgencyArray.length];
//                        for (int i = 0; i < mTypeAgencyArray.length; i++) {
//                            mTypeAgencyArray2[i] = mTypeAgencyArray[i][2];
//                            Log.i(TAG + " show mDistrictArray2", mTypeAgencyArray2[i].toString());
//                        }
//                        adapterSCDCagency = new ArrayAdapter<String>(getActivity(),
//                                android.R.layout.simple_dropdown_item_1line, mTypeAgencyArray2);
//                        spnSCDCagencyType.setAdapter(adapterSCDCagency);
//                    } else {
//                        spnSCDCagencyType.setAdapter(null);
//                        selectedAgencyCode = null;
//                        EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = selectedAgencyCode;
//                        Log.i(TAG + " show selectedAgencyCode", String.valueOf(selectedAgencyCode));
//                    }
//                    spnSCDCagencyType.setOnItemClickListener((AdapterView.OnItemClickListener) new EmerOnItemSelectedListener());
//                    break;
//                case R.id.spnSCDCagencyType:
//                    //ค่า SCDCAgencyCode
//                    selectedAgencyCode = mTypeAgencyArray[pos][0];
//
//                    EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = selectedAgencyCode;
//                    Log.i(TAG + " show selectedAgencyCode", selectedAgencyCode + " " + EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode);
//                    break;
                case R.id.spnCaseType:
                    selectedCaseType = mCaseTypeArray[pos][0];
                    Log.i(TAG + " show mCaseTypeArray", selectedCaseType);
                    EmergencyTabFragment.tbNoticeCase.CaseTypeID = selectedCaseType;
                    //ดึงค่าจาก TbSubCaseSceneType
                    mSubCaseTypeArray = dbHelper.SelectSubCaseTypeByCaseType(selectedCaseType);
                    if (mSubCaseTypeArray != null) {
                        mSubCaseTypeArray2 = new String[mSubCaseTypeArray.length];
                        for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                            mSubCaseTypeArray2[i] = mSubCaseTypeArray[i][2];
                            Log.i(TAG + " show mSubCaseTypeArray2", mSubCaseTypeArray2[i].toString());
                        }
                        ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_dropdown_item_1line, mSubCaseTypeArray2);
                        spnSubCaseType.setAdapter(adapterSubCaseType);
                    } else {
                        spnSubCaseType.setAdapter(null);
                        selectedSubCaseType = null;
                        EmergencyTabFragment.tbNoticeCase.SubCaseTypeID = selectedSubCaseType;
                        Log.i(TAG + " show mSubCaseTypeArray", String.valueOf(selectedSubCaseType));
                    }
                    Log.i(TAG, EmergencyTabFragment.tbNoticeCase.CaseTypeID);
                    spnSubCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());

                    break;
                case R.id.spnSubCaseType:
                    selectedSubCaseType = mSubCaseTypeArray[pos][0];
                    EmergencyTabFragment.tbNoticeCase.SubCaseTypeID = selectedSubCaseType;
                    Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType);
                    if (sSubCaseTypeID == null || sSubCaseTypeID.equals("") || sSubCaseTypeID.equals("null")) {
                        spnSubCaseType.setSelection(0);
                    } else {
                        for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                            if (sSubCaseTypeID.trim().equals(mSubCaseTypeArray[i][0].toString())) {
                                spnSubCaseType.setSelection(i);
                                break;
                            }
                        }
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            switch (parent.getId()) {


                case R.id.spnCaseType:
                    selectedCaseType = mCaseTypeArray[0][0];
                    Log.i(TAG + " show mCaseTypeArray", selectedCaseType);
                    EmergencyTabFragment.tbNoticeCase.CaseTypeID = selectedCaseType;
                    break;
                case R.id.spnSubCaseType:
                    selectedSubCaseType = mSubCaseTypeArray[0][0];
                    EmergencyTabFragment.tbNoticeCase.SubCaseTypeID = selectedSubCaseType;
                    Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType);
                    break;
            }
        }
    }

    public class NoticeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {

                case R.id.spnSCDCagencyType:
                    //make sure the animal was already selected during the onCreate
                    selectedCenter = mTypeCenterArray[pos][0];
                    Log.i(TAG + " show selectedCenter", selectedCenter);
                    //
                    mTypeAgencyArray = dbHelper.SelectSCDCAgency(selectedCenter);
                    if (mTypeAgencyArray != null) {
                        mTypeAgencyArray2 = new String[mTypeAgencyArray.length];
                        for (int i = 0; i < mTypeAgencyArray.length; i++) {
                            mTypeAgencyArray2[i] = mTypeAgencyArray[i][2];
                            Log.i(TAG + " show mDistrictArray2", mTypeAgencyArray2[i].toString());
                        }
                        adapterSCDCagency = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_dropdown_item_1line, mTypeAgencyArray2);
                        spnSCDCagencyType.setAdapter(adapterSCDCagency);
                    } else {
                        spnSCDCagencyType.setAdapter(null);
                        selectedAgencyCode = null;
                        EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = selectedAgencyCode;
                        Log.i(TAG + " show selectedAgencyCode", String.valueOf(selectedAgencyCode));
                    }
                    spnSCDCagencyType.setOnItemSelectedListener(new NoticeOnItemSelectedListener());
                    break;
                case R.id.spnSCDCcenterType:
                    //make sure the animal was already selected during the onCreate
                    //ค่า SCDCAgencyCode
                    selectedAgencyCode = mTypeAgencyArray[pos][0];

                    EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = selectedAgencyCode;
                    Log.i(TAG + " show selectedAgencyCode", selectedAgencyCode + " " + EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode);
                    break;
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
            //check which spinner triggered the listener
            switch (parent.getId()) {

                case R.id.spnSCDCagencyType:
                    selectedAgencyCode = mTypeAgencyArray[0][0];

                    EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = selectedAgencyCode;

                    break;
            }
        }
    }
}
