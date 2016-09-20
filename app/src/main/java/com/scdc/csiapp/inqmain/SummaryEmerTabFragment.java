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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView edtStatus, edtInvestDateTime, edtUpdateDateTime, edtInqInfo, edtInvInfo, edtPoliceStation,
            txtButtonCount1, txtButtonCount2, txtButtonCount3, txtButtonCount4, txtButtonCount5;
    String[] updateDT, selectedCaseType, selectedSubCaseType;
    String message = "";
    String[][] mTypeAgencyArray, mTypeCenterArray, mCaseTypeArray, mSubCaseTypeArray;
    String[] mTypeAgencyArray2, mTypeCenterArray2, mAgencyID, mCenterID;
    ArrayAdapter<String> adapterSCDCcenter, adapterSCDCagency;
    private String selectedAgency, SelectedAgencyID, selectedCenter, SelectedCenterID = null;
    private View mViewBtnSaveServer, mViewBtnFullReport, mViewBtnTransReport, layoutButton1, layoutButton2, layoutButton3, layoutButton4, layoutButton5;
    String noticeCaseID;
    Snackbar snackbar;
    EmergencyTabFragment emergencyTabFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);

//โชว์ scenenoticedate & time
        final Context context = viewSummaryCSI.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        acceptListFragment = new AcceptListFragment();
        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        dbHelper = new DBHelper(getContext());
        officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;
        cd = new ConnectionDetector(getActivity());
        emergencyTabFragment = new EmergencyTabFragment();
        noticeCaseID = EmergencyTabFragment.tbNoticeCase.NoticeCaseID;
        Log.i(TAG, " NoticeCaseID " + noticeCaseID);

        updateDT = getDateTime.getDateTimeNow();
        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);


        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
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
                EmergencyTabFragment.tbNoticeCase.CaseTypeID = selectedCaseType[0];
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
                    EmergencyTabFragment.tbNoticeCase.SubCaseTypeID = selectedSubCaseType[0];
                    Log.i(TAG + " show mSubCaseTypeArray", String.valueOf(selectedSubCaseType[0]));
                }
                Log.i(TAG ,EmergencyTabFragment.tbNoticeCase.CaseTypeID);
                if (EmergencyTabFragment.tbNoticeCase.SubCaseTypeID != null) {
                    mSubCaseTypeArray = dbHelper.SelectSubCaseTypeByCaseType(EmergencyTabFragment.tbNoticeCase.CaseTypeID);
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
                        EmergencyTabFragment.tbNoticeCase.SubCaseTypeID = selectedSubCaseType[0];
                        Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType[0]);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedSubCaseType[0] = mSubCaseTypeArray[0][0];
                        EmergencyTabFragment.tbNoticeCase.SubCaseTypeID = selectedSubCaseType[0];
                        Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType[0]);
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedCaseType[0] = mCaseTypeArray[0][0];
                EmergencyTabFragment.tbNoticeCase.CaseTypeID = selectedCaseType[0];
                Log.i(TAG + " show mCaseTypeArray", selectedCaseType[0]);
            }
        });

        edtInqInfo.setText(WelcomeActivity.profile.getTbOfficial().Rank + " "
                + WelcomeActivity.profile.getTbOfficial().FirstName + " "
                + WelcomeActivity.profile.getTbOfficial().LastName + " ("
                + WelcomeActivity.profile.getTbOfficial().Position + ") โทร." +
                "" + WelcomeActivity.profile.getTbOfficial().PhoneNumber + " ");

        if (EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID != null) {
            String InvestigatorOfficialID = EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID;
            Log.i(TAG,"InvestigatorOfficialID : "+InvestigatorOfficialID );

            String mInvOfficialArray[] = dbHelper.SelectInvOfficial(EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID);
            if (mInvOfficialArray != null) {
                edtInvInfo.setText(mInvOfficialArray[4].toString() + " " + mInvOfficialArray[1].toString()
                        + " " + mInvOfficialArray[2].toString() + " (" + mInvOfficialArray[5].toString() + ") " +
                        mInvOfficialArray[7].toString());
            }else{
                 edtInvInfo.setText("");
            }

        }
        edtInvInfo.setText("");

        String mTypePoliceStationArray[] = dbHelper.SelectPoliceStation(EmergencyTabFragment.tbNoticeCase.PoliceStationID);
        if (mTypePoliceStationArray != null) {
            edtPoliceStation.setText(mTypePoliceStationArray[2].toString());
        }

        if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("investigating")) {
            edtStatus.setText("กำลังดำเนินการตรวจ");
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals("notice")) {
            edtStatus.setText("แจ้งเหตุแล้ว รอจ่ายงาน");
            mViewBtnSaveServer.setVisibility(View.GONE);
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

        if(emergencyTabFragment.mode =="view") {
            fabBtn.setVisibility(View.GONE);
            spnCaseType.setEnabled(false);
            spnSubCaseType.setEnabled(false);
            edtReportNo.setEnabled(false);

        }

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = null;
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
    private class SummaryOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (v == mViewBtnSaveServer) {

                //new saveFullReport().execute(reportID);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("อัพโหลดข้อมูลเข้าสู่เซิร์ฟเวอร์");
                dialog.setPositiveButton("บันทึกแบบร่าง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //uploadDataToServer();
                        Toast.makeText(getActivity(),
                                "อัพโหลดข้อมูล บันทึกแบบร่าง เรียบร้อย", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setCancelable(true);
                dialog.setNeutralButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("บันทึกเเบบสมบูรณ์", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //uploadDataToServer();
                       // new saveFullReport().execute(reportID);
                        Toast.makeText(getActivity(),
                                "อัพโหลดข้อมูล บันทึกเเบบสมบูรณ์ เรียบร้อย", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.create();
                dialog.show();

            }
            if (v == mViewBtnTransReport) {
                Log.i("mViewBtnTransReport", "mViewBtnTransReport");
               // new DownloadFileAsync().execute(reportID);
               //
            }

        }
    }
}