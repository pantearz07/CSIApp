package com.scdc.csiapp.invmain;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.apimodel.ApiStatusData;
import com.scdc.csiapp.apimodel.ApiStatusResult;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class SummaryCSITabFragment extends Fragment {
    private static final String TAG = "DEBUG-SummaryCSITabFragment";
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    // connect sqlite
    SQLiteDatabase mDb;
    DBHelper dbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    boolean statusConnect = false;
    GetDateTime getDateTime;
    EditText edtReportNo;
    private static String strSDCardPathName_Doc = "/CSIFiles/";

    TextView edtStatus, edtUpdateDateTime2, edtSceneNoticeDateTime, edtCompleteSceneDateTime, edtCompleteSceneDateTimeTitle,
            edtUpdateDateTime, txtUpdateDateTimeTitle, edtInqInfo, edtInvInfo, edtPoliceStation,
            edtInvTel, edtInqTel;
    String[] updateDT;
    String message = "";
    String[][] mCaseTypeArray, mSubCaseTypeArray;
    String[] mSubCaseTypeArray2;

    Spinner spnCaseType, spnSubCaseType;
    String selectedCaseType, selectedSubCaseType;
    String sCaseTypeID, sSubCaseTypeID = null;

    String InqTel, InvTel;
    String caseReportID, noticeCaseID;
    Snackbar snackbar;
    Button btnNoticecase, btnDownloadfile, btnAcceptCase;
    View layoutButton1, layoutButton, layoutSceneNoticeDate;
    boolean oldselectedCT, oldselectedSubCT = false;
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 10; //10 second
    boolean status_savecase = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);

        final Context context = viewSummaryCSI.getContext();

        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        dbHelper = new DBHelper(getActivity());

        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        cd = new ConnectionDetector(getActivity());
        layoutButton1 = (LinearLayout) viewSummaryCSI.findViewById(R.id.layoutButton1);
        layoutButton1.setVisibility(View.GONE);
        layoutSceneNoticeDate = (LinearLayout) viewSummaryCSI.findViewById(R.id.layoutSceneNoticeDate);
        layoutSceneNoticeDate.setVisibility(View.GONE);
        layoutButton = (LinearLayout) viewSummaryCSI.findViewById(R.id.layoutButton);
        noticeCaseID = CSIDataTabFragment.apiCaseScene.getTbNoticeCase().NoticeCaseID;
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        if (noticeCaseID.equals("null") || noticeCaseID == null || noticeCaseID.equals("")) {
            Log.i(TAG, " show noticeCaseID: ");
        } else {
            Log.i(TAG, " show noticeCaseID: " + noticeCaseID.toString());
        }

        if (caseReportID.equals("null") || caseReportID == null || caseReportID.equals("")) {
            Log.i(TAG, " show caseReportID: ");
        } else {
            Log.i(TAG, " show caseReportID: " + caseReportID);
        }


        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        // Log.i(TAG, " show edtReportNo: " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportNo);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportNo == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportNo.equals("")) {
            edtReportNo.setText("");
        } else {
            edtReportNo.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportNo.toString());
        }
        edtReportNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportNo = edtReportNo.getText().toString();
            }
        });
        edtInqInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInqInfo);
        edtInvInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInvInfo);
        edtInqTel = (TextView) viewSummaryCSI.findViewById(R.id.edtInqTel);
        edtInvTel = (TextView) viewSummaryCSI.findViewById(R.id.edtInvTel);
        edtPoliceStation = (TextView) viewSummaryCSI.findViewById(R.id.edtPoliceStation);
//สถานะคดี
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);

        edtUpdateDateTime2 = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText("อัพเดทข้อมูลเมื่อ " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate + " เวลา " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime);
        //วันเวลาที่ผู้ตรวจสถานที่เกิดเหตุออกไปตรวจ
        edtSceneNoticeDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtSceneNoticeDateTime);
        //วันเวลาที่ตรวจคดีเสร็จ
        edtCompleteSceneDateTimeTitle = (TextView) viewSummaryCSI.findViewById(R.id.edtCompleteSceneDateTimeTitle);
        edtCompleteSceneDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtCompleteSceneDateTime);
        ///วันเวลาที่แก้ไขข้อมูลล่าสุด
        txtUpdateDateTimeTitle = (TextView) viewSummaryCSI.findViewById(R.id.txtUpdateDateTimeTitle);
        edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);

        btnNoticecase = (Button) viewSummaryCSI.findViewById(R.id.btnNoticecase);
        btnDownloadfile = (Button) viewSummaryCSI.findViewById(R.id.btnDownloadfile);
        btnNoticecase.setOnClickListener(new SummaryOnClickListener());
        btnDownloadfile.setOnClickListener(new SummaryOnClickListener());
        btnNoticecase.setText("ส่งไปยังเซิร์ฟเวอร์");
        btnAcceptCase = (Button) viewSummaryCSI.findViewById(R.id.btnAcceptCase);
        btnAcceptCase.setVisibility(View.GONE);

        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        sCaseTypeID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTypeID;
        sSubCaseTypeID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().SubCaseTypeID;

        //โชว์dropdown casetype
        //ดึงค่าจาก TbCaseSceneType
        mCaseTypeArray = dbHelper.SelectCaseType();
        if (mCaseTypeArray != null) {
            String[] mCaseTypeArray2 = new String[mCaseTypeArray.length];
            for (int i = 0; i < mCaseTypeArray.length; i++) {
                mCaseTypeArray2[i] = mCaseTypeArray[i][1];
                // Log.i(TAG + " show mCaseTypeArray", mCaseTypeArray2[i].toString());
            }
            ArrayAdapter<String> adapterTypeCase = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mCaseTypeArray2);
            spnCaseType.setAdapter(adapterTypeCase);
        } else {
            Log.i(TAG + " show mCaseTypeArray", "null");
        }
        /// new
        if (sSubCaseTypeID == null || sSubCaseTypeID.equals("") || sSubCaseTypeID.equals("null")) {
            spnSubCaseType.setSelection(0);
        } else {

            String SelectCaseTypeID = dbHelper.SelectCaseTypeID(sSubCaseTypeID);
            for (int i = 0; i < mCaseTypeArray.length; i++) {
                if (SelectCaseTypeID.trim().equals(mCaseTypeArray[i][0].toString())) {
                    spnCaseType.setSelection(i);
                    oldselectedCT = true;
                    break;
                }
            }
            mSubCaseTypeArray = dbHelper.SelectSubCaseTypeByCaseType(SelectCaseTypeID);
            if (mSubCaseTypeArray != null) {
                mSubCaseTypeArray2 = new String[mSubCaseTypeArray.length];
                for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                    mSubCaseTypeArray2[i] = mSubCaseTypeArray[i][2];
                    // Log.i(TAG + " show mSubCaseTypeArray2", mSubCaseTypeArray2[i].toString());
                }
                ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, mSubCaseTypeArray2);
                spnSubCaseType.setAdapter(adapterSubCaseType);
                for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                    if (sSubCaseTypeID.trim().equals(mSubCaseTypeArray[i][0].toString())) {
                        spnSubCaseType.setSelection(i);
                        oldselectedSubCT = true;
                        break;
                    }
                }
            } else {
                spnSubCaseType.setAdapter(null);

            }

        }

        spnCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spnSubCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spnSubCaseType.setOnTouchListener(new EmerOnItemSelectedListener());
        spnCaseType.setOnTouchListener(new EmerOnItemSelectedListener());
//ชื่อพนักงานสอบสวน
        if (CSIDataTabFragment.apiCaseScene.getTbNoticeCase().getInquiryOfficialID() == null) {
            edtInqInfo.setText("");
            edtInqTel.setText("");
        } else {
            String InvestigatorOfficialID = CSIDataTabFragment.apiCaseScene.getTbNoticeCase().getInquiryOfficialID();
            Log.i(TAG, "InvestigatorOfficialID : " + InvestigatorOfficialID);

            String mInvOfficialArray[] = dbHelper.SelectInvOfficial(InvestigatorOfficialID);
            if (mInvOfficialArray != null) {
                edtInqInfo.setText(mInvOfficialArray[4].toString() + " " + mInvOfficialArray[1].toString()
                        + " " + mInvOfficialArray[2].toString() + " (" + mInvOfficialArray[5].toString() + ")");
                if (mInvOfficialArray[7] != null || mInvOfficialArray[7].equals("")) {
                    edtInqTel.setText("โทร." + mInvOfficialArray[7].toString());
                    InqTel = mInvOfficialArray[7].toString();
                    edtInqTel.setOnClickListener(new SummaryOnClickListener());
                }
            } else {
                edtInqInfo.setText("");
                edtInqTel.setText("");
            }

        }


        edtInvInfo.setText(WelcomeActivity.profile.getTbOfficial().Rank + " "
                + WelcomeActivity.profile.getTbOfficial().FirstName + " "
                + WelcomeActivity.profile.getTbOfficial().LastName + " ("
                + WelcomeActivity.profile.getTbOfficial().Position + ")");
        edtInvTel.setText("โทร." + WelcomeActivity.profile.getTbOfficial().PhoneNumber.toString());
        InvTel = WelcomeActivity.profile.getTbOfficial().PhoneNumber.toString();
        edtInvTel.setOnClickListener(new SummaryOnClickListener());
        String mTypePoliceStationArray[] = dbHelper.SelectPoliceStation(CSIDataTabFragment.apiCaseScene.getTbNoticeCase().PoliceStationID);
        if (mTypePoliceStationArray != null) {
            edtPoliceStation.setText(mTypePoliceStationArray[2].toString());
        }

        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().getReportStatus().equals("investigating")) {
            edtStatus.setText("กำลังดำเนินการตรวจ");
        } else if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().getReportStatus().equals("notice")) {
            edtStatus.setText("แจ้งเหตุแล้ว รอจ่ายงาน");
            btnNoticecase.setEnabled(false);

        } else if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().getReportStatus().equals("receive")) {
            edtStatus.setText("รอส่งแจ้งเหตุ");
        } else if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().getReportStatus().equals("assign")) {
            edtStatus.setText("รอรับไปตรวจ");
        } else if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().getReportStatus().equals("accept")) {
            edtStatus.setText("รับเรื่องแล้ว");
            final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "investigating";
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "investigating";
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
            SaveCaseReport statusCase = new SaveCaseReport();
            statusCase.execute(CSIDataTabFragment.apiCaseScene);
            if (status_savecase = true) {

                Log.i(TAG, "accept to investigating");
                edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate) + " เวลาประมาณ " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime + " น.");
                edtStatus.setText("กำลังดำเนินการตรวจ");

            }
        } else if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().getReportStatus().equals("investigated")) {
            edtStatus.setText("ตรวจเสร็จแล้ว");
        }
//
        //วันเวลาที่ผู้ตรวจสถานที่เกิดเหตุออกไปตรวจ
        if (CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeDate == null || CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeDate.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeDate.equals("0000-00-00")) {
            edtSceneNoticeDateTime.setText("-");

        } else {
            edtSceneNoticeDateTime.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeDate) + " เวลาประมาณ " + CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeTime + " น.");
        }
        //วันเวลาที่ตรวจคดีเสร็จ
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("0000-00-00")) {
            edtCompleteSceneDateTime.setText("-");

        } else {
            edtCompleteSceneDateTime.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate) + " เวลาประมาณ " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime + " น.");

        }
        //วันเวลาที่แก้ไขข้อมูลล่าสุด
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate.equals("0000-00-00")) {
            edtUpdateDateTime.setText("-");

        } else {
            edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate) + " เวลาประมาณ " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime + " น.");
        }
//
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);
        fabBtn.setOnClickListener(new SummaryOnClickListener());

        if (CSIDataTabFragment.mode == "view") {
            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtn.setLayoutParams(p);
            fabBtn.hide();
            edtReportNo.setEnabled(false);
            spnCaseType.setEnabled(false);
            spnSubCaseType.setEnabled(false);
            // btnNoticecase.setEnabled(false);
            layoutButton1.setVisibility(View.GONE);
            btnNoticecase.setVisibility(View.GONE);

        } else if (CSIDataTabFragment.mode == "edit") {
            layoutButton1.setVisibility(View.GONE);
        }
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
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("อัพโหลดข้อมูลเข้าสู่เซิร์ฟเวอร์");
                dialog.setPositiveButton("บันทึกแบบร่าง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // uploadDataToServer();
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "investigating";
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "investigating";
                        SaveCaseReport statusCase = new SaveCaseReport();
                        statusCase.execute(CSIDataTabFragment.apiCaseScene);
                        if (status_savecase = true) {

                            edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate)
                                    + " เวลาประมาณ " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime + " น.");

                        }
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
                        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("0000-00-00") || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("")) {
                            dialog.dismiss();
                            if (snackbar == null || !snackbar.isShown()) {
                                snackbar = Snackbar.make(rootLayout, "ยังไม่ระบุวันเวลาที่ตรวจเสร็จ", Snackbar.LENGTH_INDEFINITE)
                                        .setAction(getString(R.string.ok), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {


                                            }
                                        });
                                snackbar.show();
                            }
                        } else {
                            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "investigated";
                            CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "investigated";
                            SaveCaseReport statusCase = new SaveCaseReport();
                            statusCase.execute(CSIDataTabFragment.apiCaseScene);
                            if (status_savecase = true) {

                                edtCompleteSceneDateTime.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate) + " เวลาประมาณ " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime + " น.");
                                edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate) + " เวลาประมาณ " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime + " น.");

                            }
                        }

                    }
                });
                dialog.create();
                dialog.show();
            }
            if (v == btnDownloadfile) {
                Log.i(TAG, "btnDownloadfile");
                DownloadDocFile downloadDocFile = new DownloadDocFile();
                downloadDocFile.execute(CSIDataTabFragment.apiCaseScene);
            }

            if (v == fabBtn) {
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "investigating";
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "investigating";
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                if (CSIDataTabFragment.apiCaseScene != null) {

                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        if (snackbar == null || !snackbar.isShown()) {
                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_complete) + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID.toString(), Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                        }
                                    });
                            snackbar.show();
                        }
                    } else {
                        if (snackbar == null || !snackbar.isShown()) {
                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error) + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID.toString(), Snackbar.LENGTH_INDEFINITE)
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
            if (v == edtInqTel) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + InqTel));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
            if (v == edtInvTel) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + InvTel));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        }
    }

    class DownloadFile extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar

        }

        @Override
        protected String doInBackground(String... params) {
            int count;

            File myDir;
            String fileoutput = "";
            try {

                myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), strSDCardPathName_Doc);
                myDir.mkdirs();
                //http://localhost/mCSI/assets/csifiles/CR04_000002/docs/
                String defaultIP = "180.183.251.32/mcsi";
                SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
                defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
                String filepath = "http://" + defaultIP + "/assets/csifiles/" + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/docs/" + params[0] + ".docx";
                Log.i(TAG, "docs: " + filepath);

                URL url = new URL(filepath);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d(TAG, "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());

                // Get File Name from URL
                fileoutput = strSDCardPathName_Doc + params[0] + ".docx";
                File fileDoc = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileoutput);
                if (fileDoc.exists()) {
                    fileDoc.delete();
                }
                fileDoc.createNewFile();
                Log.i(TAG, "fileoutput : " + fileDoc.getPath());
                OutputStream output = new FileOutputStream(fileDoc);
                //OutputStream output = new FileOutputStream("/sdcard/Download/"+fileName+".doc");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return params[0];
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return "error";
            }


        }

        protected void onPostExecute(final String arrData) {
            final String CurrentDate_ID[] = getDateTime.getDateTimeCurrent();
            String timestamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];

            ApiMultimedia apiMultimedia = new ApiMultimedia();
            TbMultimediaFile tbMultimediaFile = new TbMultimediaFile();

            tbMultimediaFile.setCaseReportID(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID);
            tbMultimediaFile.setFileID(arrData);
            tbMultimediaFile.setFilePath(arrData + ".docx");
            tbMultimediaFile.setFileType("document");
            tbMultimediaFile.setFileDescription("เลขที่รายงาน " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportNo);
            tbMultimediaFile.setTimestamp(timestamp);
            apiMultimedia.setTbMultimediaFile(tbMultimediaFile);
            CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
            Log.i(TAG, String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                if (snackbar == null || !snackbar.isShown()) {
                    snackbar = Snackbar.make(rootLayout, getString(R.string.download_complete) + "\n" + arrData.toString() + ".docx", Snackbar.LENGTH_INDEFINITE)
                            .setAction("เปิดดู", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (arrData.toString() != "error") {
                                        openDocument(arrData.toString());
                                    }
                                }
                            });
                    snackbar.show();
                }
            }
        }
    }

    public void openDocument(String name) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), strSDCardPathName_Doc + name + ".docx");
        if (file.exists()) {
            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (extension.equalsIgnoreCase("") || mimetype == null) {
                // if there is no extension or there is no definite mimetype, still try to open the file
                intent.setDataAndType(Uri.fromFile(file), "text/*");
            } else {
                intent.setDataAndType(Uri.fromFile(file), mimetype);
            }
            // custom message for the intent
            startActivity(Intent.createChooser(intent, "Choose an Application:"));
        } else {
            Toast.makeText(getActivity(),
                    "ไม่มีไฟล์เอกสาร",
                    Toast.LENGTH_LONG).show();
        }
    }

    public class EmerOnItemSelectedListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            if (v == spnCaseType) {

                oldselectedCT = false;
            }
            if (v == spnSubCaseType) {

                oldselectedSubCT = false;
            }

            return false;
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            //String selectedItem = parent.getItemAtPosition(pos).toString();

            switch (parent.getId()) {

                case R.id.spnCaseType:
                    if (oldselectedCT == false) {
                        selectedCaseType = mCaseTypeArray[pos][0];
                        Log.i(TAG + " show mCaseTypeArray", selectedCaseType);
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseTypeID = selectedCaseType;
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTypeID = selectedCaseType;
                        //ดึงค่าจาก TbSubCaseSceneType
                        mSubCaseTypeArray = dbHelper.SelectSubCaseTypeByCaseType(selectedCaseType);
                        if (mSubCaseTypeArray != null) {
                            mSubCaseTypeArray2 = new String[mSubCaseTypeArray.length];
                            for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                                mSubCaseTypeArray2[i] = mSubCaseTypeArray[i][2];
                                // Log.i(TAG + " show mSubCaseTypeArray2", mSubCaseTypeArray2[i].toString());
                            }
                            ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, mSubCaseTypeArray2);
                            spnSubCaseType.setAdapter(adapterSubCaseType);
                        } else {
                            spnSubCaseType.setAdapter(null);
                            selectedSubCaseType = null;
                            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SubCaseTypeID = selectedSubCaseType;
                            CSIDataTabFragment.apiCaseScene.getTbCaseScene().SubCaseTypeID = selectedSubCaseType;
                            Log.i(TAG + " show mSubCaseTypeArray", String.valueOf(selectedSubCaseType));
                        }
                        Log.i(TAG, CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTypeID);
                        spnSubCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
                    }
                    break;

                case R.id.spnSubCaseType:
                    if (oldselectedSubCT == false) {
                        selectedSubCaseType = mSubCaseTypeArray[pos][0];
                        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SubCaseTypeID = selectedSubCaseType;
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().SubCaseTypeID = selectedSubCaseType;
                        Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType);
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
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseTypeID = selectedCaseType;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseTypeID = selectedCaseType;
                    break;
                case R.id.spnSubCaseType:
                    selectedSubCaseType = mSubCaseTypeArray[0][0];
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().SubCaseTypeID = selectedSubCaseType;
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().SubCaseTypeID = selectedSubCaseType;
                    Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType);
                    break;
            }
        }


    }

    class SaveCaseReport extends AsyncTask<ApiCaseScene, Void, ApiStatusData> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*
            * สร้าง dialog popup ขึ้นมาแสดงตอนกำลัง login
            */
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.upload_progress));
            progressDialog.show();
        }

        @Override
        protected ApiStatusData doInBackground(ApiCaseScene... params) {
            return WelcomeActivity.api.saveCaseReport(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatusData apiStatus) {
            super.onPostExecute(apiStatus);
            progressDialog.dismiss();
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatus.getData().getReason());
                status_savecase = true;
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, apiStatus.getData().getReason().toString(), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                        snackbar.show();
                    }
                }
            } else {
                status_savecase = false;
                Toast.makeText(getActivity(),
                        getString(R.string.error_data) + " " + getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    class DownloadDocFile extends AsyncTask<ApiCaseScene, Void, ApiStatusResult> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*
            * สร้าง dialog popup ขึ้นมาแสดงตอนกำลัง login
            */
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.show();
        }

        @Override
        protected ApiStatusResult doInBackground(ApiCaseScene... params) {
            return WelcomeActivity.api.saveDocFile(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatusResult apiStatusResult) {
            super.onPostExecute(apiStatusResult);
            progressDialog.dismiss();
            if (apiStatusResult.getStatus().equalsIgnoreCase("success")) {
                Log.d(TAG, apiStatusResult.getStatus().toString());
                Log.d(TAG, apiStatusResult.getData().getResult().toString());

                new DownloadFile().execute(apiStatusResult.getData().getResult().toString());

            } else {
                Log.d(TAG, "error");
                Toast.makeText(getActivity(),
                        getString(R.string.error_data) + " " + getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


}