package com.scdc.csiapp.inqmain;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.apimodel.ApiStatusResult;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.BusProvider;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.SnackBarAlert;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbNoticeCase;

import org.parceler.Parcels;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.LENGTH_SHORT;


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
    GetDateTime getDateTime;
    String officialID;
    EditText edtReportNo;
    FragmentManager mFragmentManager;

    Spinner spnCaseType, spnSubCaseType;
    String selectedCaseType, selectedSubCaseType, sCaseTypeID, sSubCaseTypeID;
    TextView edtUpdateDateTime2, edtStatus, edtUpdateDateTime, edtInqInfo, edtInvInfo, edtPoliceStation,
            edtInvTel, edtInqTel;
    String[] updateDT;
    String[][] mTypeCenterArray, mCaseTypeArray, mSubCaseTypeArray, mTypeAgencyArray;
    String[] mTypeCenterArray2, mTypeAgencyArray2, mSubCaseTypeArray2;
    ArrayAdapter<String> adapterSCDCcenter, adapterSCDCagency;
    boolean oldselectedCT, oldselectedSubCT = false;
    Button btnNoticecase, btnDownloadfile, btnAcceptCase;
    String noticeCaseID, sSCDCAgencyCode;
    Snackbar snackbar;
    Spinner spnSCDCcenterType, spnSCDCagencyType;
    String selectedCenter, selectedAgencyCode;
    View layoutButton1, linearLayoutReportNo, layoutSceneNoticeDate;
    NoticeCaseListFragment noticeCaseListFragment;
    String InqTel, InvTel;
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 10; //10 second
    boolean status_deletecase = false;
    boolean status_noticecase = false;
    public static final String KEY_PROFILE = "key_profile";
    public static final String KEY_CONNECT = "key_connect";
    ApiProfile apiProfile;
    ApiConnect api;

    public static SummaryEmerTabFragment newInstance() {
        return new SummaryEmerTabFragment();
    }

    public SummaryEmerTabFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BusProvider.getInstance().register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            // Fragment ถูกสร้างขึ้นมาครั้งแรก
            officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;

            Log.i(TAG, "from savedInstanceState null");
        } else {
            // Fragment ถูก Restore ขึ้นมา
            restoreInstanceState(savedInstanceState);
            Log.i(TAG, "from onActivityCreated" + WelcomeActivity.profile.getTbOfficial().OfficialID);
            officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;
            setViewData();
        }
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        apiProfile = Parcels.unwrap(savedInstanceState.getParcelable(KEY_PROFILE));
        if (WelcomeActivity.profile == null) {
            WelcomeActivity.profile = new ApiProfile();
            WelcomeActivity.profile = apiProfile;
        } else {
            WelcomeActivity.profile = apiProfile;
        }
        api = Parcels.unwrap(savedInstanceState.getParcelable(KEY_CONNECT));
        if (WelcomeActivity.api == null) {
            WelcomeActivity.api = new ApiConnect(mContext);
            WelcomeActivity.api = api;
        } else {
            WelcomeActivity.api = api;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (apiProfile == null) {
            apiProfile = new ApiProfile();
            apiProfile = WelcomeActivity.profile;
        } else {
            apiProfile = WelcomeActivity.profile;
        }
        if (api == null) {
            api = new ApiConnect(getActivity());
            api = WelcomeActivity.api;
        } else {
            api = WelcomeActivity.api;
        }
        outState.putParcelable(KEY_PROFILE, Parcels.wrap(apiProfile));
        outState.putParcelable(KEY_CONNECT, Parcels.wrap(api));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);

//โชว์ scenenoticedate & time
        mContext = viewSummaryCSI.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();

        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        dbHelper = new DBHelper(getContext());
        cd = new ConnectionDetector(getActivity());
        noticeCaseListFragment = new NoticeCaseListFragment();
        noticeCaseID = EmergencyTabFragment.tbNoticeCase.NoticeCaseID;
        Log.i(TAG, " NoticeCaseID " + noticeCaseID);

        updateDT = getDateTime.getDateTimeNow();
        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);
        edtUpdateDateTime2 = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText(getString(R.string.updatedata) + " " +
                getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.LastUpdateDate)
                + " เวลา " + getDateTime.changeTimeFormatToDB(EmergencyTabFragment.tbNoticeCase.LastUpdateTime) + " น.");

        linearLayoutReportNo = (LinearLayout) viewSummaryCSI.findViewById(R.id.linearLayoutReportNo);
        linearLayoutReportNo.setVisibility(View.GONE);
        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        edtReportNo.setVisibility(View.GONE);

        layoutButton1 = (LinearLayout) viewSummaryCSI.findViewById(R.id.layoutButton1);
//        layoutButton1.setVisibility(View.GONE);
        layoutSceneNoticeDate = (LinearLayout) viewSummaryCSI.findViewById(R.id.layoutSceneNoticeDate);
        layoutSceneNoticeDate.setVisibility(View.GONE);
        edtInqInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInqInfo);
        edtInvInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInvInfo);
        edtInqTel = (TextView) viewSummaryCSI.findViewById(R.id.edtInqTel);
        edtInvTel = (TextView) viewSummaryCSI.findViewById(R.id.edtInvTel);
        edtPoliceStation = (TextView) viewSummaryCSI.findViewById(R.id.edtPoliceStation);
//สถานะคดี
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);

        edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);
//วันเวลาที่ผู้ตรวจสถานที่เกิดเหตุออกไปตรวจ
        TextView edtSceneNoticeDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtSceneNoticeDateTime);
        //วันเวลาที่ตรวจคดีเสร็จ
        TextView edtCompleteSceneDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtCompleteSceneDateTime);
        //วันเวลาที่แก้ไขข้อมูลล่าสุด
        TextView edtUpdateDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);
        btnAcceptCase = (Button) viewSummaryCSI.findViewById(R.id.btnAcceptCase);
        btnNoticecase = (Button) viewSummaryCSI.findViewById(R.id.btnNoticecase);
        btnDownloadfile = (Button) viewSummaryCSI.findViewById(R.id.btnDownloadfile);
        btnAcceptCase.setOnClickListener(new SummaryOnClickListener());
        btnNoticecase.setOnClickListener(new SummaryOnClickListener());
        btnDownloadfile.setOnClickListener(new SummaryOnClickListener());
        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        spnCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spnSubCaseType.setOnItemSelectedListener(new EmerOnItemSelectedListener());
        spnSubCaseType.setOnTouchListener(new EmerOnItemSelectedListener());
        spnCaseType.setOnTouchListener(new EmerOnItemSelectedListener());
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

        //เเสดงค่าเดิม


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
        if (savedInstanceState == null) {
            setViewData();
        }
        edtInqTel.setOnClickListener(new SummaryOnClickListener());

        if (EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID == null || EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID.equals("") || EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID.equals("null")) {
            edtInvInfo.setText("");
            edtInvTel.setText("");
        } else {
            String InvestigatorOfficialID = EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID;
            Log.i(TAG, "InvestigatorOfficialID : " + InvestigatorOfficialID);

            String mInvOfficialArray[] = dbHelper.SelectInvOfficial(EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID);
            if (mInvOfficialArray != null) {
                edtInvInfo.setText(mInvOfficialArray[4].toString() + " " + mInvOfficialArray[1].toString()
                        + " " + mInvOfficialArray[2].toString() + " (" + mInvOfficialArray[5].toString() + ")");
                if (mInvOfficialArray[7] != null || mInvOfficialArray[7].equals("")) {
                    edtInvTel.setText("โทร." + mInvOfficialArray[7].toString());
                    InvTel = mInvOfficialArray[7].toString();
                    edtInvTel.setOnClickListener(new SummaryOnClickListener());
                }

            } else {
                edtInvInfo.setText("");
                edtInvTel.setText("");
            }

        }


        String mTypePoliceStationArray[] = dbHelper.SelectPoliceStation(EmergencyTabFragment.tbNoticeCase.PoliceStationID);
        if (mTypePoliceStationArray != null) {
            edtPoliceStation.setText(mTypePoliceStationArray[2].toString());
        }

        if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals(R.string.casestatus_1)) {
            edtStatus.setText(R.string.edtStatus_1);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals(R.string.casestatus_2)) {
            edtStatus.setText(R.string.edtStatus_2);
            btnNoticecase.setEnabled(false);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals(R.string.casestatus_3)) {
            edtStatus.setText(R.string.edtStatus_3);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals(R.string.casestatus_4)) {
            edtStatus.setText(R.string.edtStatus_4);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals(R.string.casestatus_5)) {
            edtStatus.setText(R.string.edtStatus_5);
        } else if (EmergencyTabFragment.tbNoticeCase.getCaseStatus().equals(R.string.casestatus_6)) {
            edtStatus.setText(R.string.edtStatus_6);
        }
        //วันเวลาที่ผู้ตรวจสถานที่เกิดเหตุออกไปตรวจ
        if (EmergencyTabFragment.tbNoticeCase.SceneNoticeDate == null || EmergencyTabFragment.tbNoticeCase.SceneNoticeDate.equals("")
                || EmergencyTabFragment.tbNoticeCase.SceneNoticeDate.equals("0000-00-00")) {
            edtSceneNoticeDateTime.setText("-");
        } else {
            edtSceneNoticeDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.SceneNoticeDate)
                    + " เวลาประมาณ " + getDateTime.changeTimeFormatToDB(EmergencyTabFragment.tbNoticeCase.SceneNoticeTime) + " น.");
        }

        //วันเวลาที่ตรวจคดีเสร็จ
        if (EmergencyTabFragment.tbNoticeCase.CompleteSceneDate == null || EmergencyTabFragment.tbNoticeCase.CompleteSceneDate.equals("")
                || EmergencyTabFragment.tbNoticeCase.CompleteSceneDate.equals("0000-00-00")) {
            edtCompleteSceneDateTime.setText("-");
        } else {
            edtCompleteSceneDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.CompleteSceneDate)
                    + " เวลาประมาณ " + getDateTime.changeTimeFormatToDB(EmergencyTabFragment.tbNoticeCase.CompleteSceneTime) + " น.");

        }
        //วันเวลาที่แก้ไขข้อมูลล่าสุด
        if (EmergencyTabFragment.tbNoticeCase.LastUpdateDate == null || EmergencyTabFragment.tbNoticeCase.LastUpdateDate.equals("")
                || EmergencyTabFragment.tbNoticeCase.LastUpdateDate.equals("0000-00-00")) {
            edtUpdateDateTime.setText("-");

        } else {
            edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(EmergencyTabFragment.tbNoticeCase.LastUpdateDate)
                    + " เวลาประมาณ " + getDateTime.changeTimeFormatToDB(EmergencyTabFragment.tbNoticeCase.LastUpdateTime) + " น.");

        }

        if (EmergencyTabFragment.mode == "view") {
            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtn.setLayoutParams(p);
            fabBtn.hide();
            fabBtn.setEnabled(false);

            spnCaseType.setEnabled(false);
            spnSubCaseType.setEnabled(false);
            edtReportNo.setEnabled(false);
            btnNoticecase.setVisibility(View.GONE);
            layoutButton1.setVisibility(View.GONE);
            btnDownloadfile.setVisibility(View.GONE);
            if (EmergencyTabFragment.tbNoticeCase.CaseStatus.equals("receive")) {
                btnNoticecase.setVisibility(View.VISIBLE);
                btnDownloadfile.setVisibility(View.VISIBLE);
                btnDownloadfile.setText(getString(R.string.delete_case));
            }
        } else if (EmergencyTabFragment.mode == "edit") {
//            btnDownloadfile.setVisibility(View.GONE);
            if (EmergencyTabFragment.tbNoticeCase.CaseStatus.equals("receive")) {
                btnNoticecase.setVisibility(View.VISIBLE);
                btnDownloadfile.setVisibility(View.VISIBLE); //ลบคดี
                btnDownloadfile.setText(getString(R.string.delete_case));
                layoutButton1.setVisibility(View.VISIBLE);
                btnAcceptCase.setText(getString(R.string.upload_case));
            }
        } else {
            //mode = new
            btnNoticecase.setVisibility(View.VISIBLE);
            btnDownloadfile.setVisibility(View.VISIBLE);
            btnDownloadfile.setText(getString(R.string.delete_case));
            layoutButton1.setVisibility(View.VISIBLE);
            btnAcceptCase.setText(getString(R.string.upload_case));
        }

        fabBtn.setOnClickListener(new SummaryOnClickListener());

        return viewSummaryCSI;
    }

    private void setViewData() {
        if (WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode != null || WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode != "") {
            EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode;
            sSCDCAgencyCode = WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode;
        }
        edtInqInfo.setText(WelcomeActivity.profile.getTbOfficial().Rank + " "
                + WelcomeActivity.profile.getTbOfficial().FirstName + " "
                + WelcomeActivity.profile.getTbOfficial().LastName + " ("
                + WelcomeActivity.profile.getTbOfficial().Position + ")");
        edtInqTel.setText("โทร." + WelcomeActivity.profile.getTbOfficial().PhoneNumber.toString());
        InqTel = WelcomeActivity.profile.getTbOfficial().PhoneNumber.toString();
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
        hiddenKeyboard();
    }

    private class SummaryOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnNoticecase) {
                hiddenKeyboard();
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
                        final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                        Log.i(TAG + " show SCDCAgencyCode", EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode);

                        EmergencyTabFragment.tbNoticeCase.CaseStatus = "notice";
                        EmergencyTabFragment.tbNoticeCase.Mobile_CaseID = EmergencyTabFragment.tbNoticeCase.NoticeCaseID;
                        EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID = null;
                        EmergencyTabFragment.tbNoticeCase.PoliceStationID = WelcomeActivity.profile.getTbOfficial().PoliceStationID;
                        EmergencyTabFragment.tbNoticeCase.LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                        EmergencyTabFragment.tbNoticeCase.LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                        EmergencyTabFragment.tbNoticeCase.SceneNoticeDate = null;
                        EmergencyTabFragment.tbNoticeCase.SceneNoticeTime = null;
                        EmergencyTabFragment.tbNoticeCase.CompleteSceneDate = null;
                        EmergencyTabFragment.tbNoticeCase.CompleteSceneTime = null;
                        if (EmergencyTabFragment.tbNoticeCase != null) {

                            boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
                            if (isSuccess) {
                                SendNewNoticeCase noticeCase = new SendNewNoticeCase();
                                noticeCase.execute(EmergencyTabFragment.tbNoticeCase);

                            } else {
                                if (snackbar == null || !snackbar.isShown()) {
                                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_LONG,
                                            getString(R.string.save_error)
                                                    + " " + EmergencyTabFragment.tbNoticeCase.NoticeCaseID.toString());
                                    snackBarAlert.createSnacbar();
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
                hiddenKeyboard();
                if (EmergencyTabFragment.tbNoticeCase.CaseStatus.equals("receive") || EmergencyTabFragment.mode == "new") {
                    Log.i(TAG, "ลบคดี");

                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, getString(R.string.delete_noticecase), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DeleteNoticeCase deleteNoticeCase = new DeleteNoticeCase();
                                        deleteNoticeCase.execute(EmergencyTabFragment.tbNoticeCase.Mobile_CaseID);

                                    }
                                });
                        snackbar.show();
                    }


                } else {
                    Log.i(TAG, "btnDownloadfile");
                }

            }
            if (v == btnAcceptCase) {
                hiddenKeyboard();
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                EmergencyTabFragment.tbNoticeCase.CaseStatus = "receive";
                EmergencyTabFragment.tbNoticeCase.Mobile_CaseID = EmergencyTabFragment.tbNoticeCase.NoticeCaseID;
                EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID = null;
                EmergencyTabFragment.tbNoticeCase.PoliceStationID = WelcomeActivity.profile.getTbOfficial().PoliceStationID;
                EmergencyTabFragment.tbNoticeCase.LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                EmergencyTabFragment.tbNoticeCase.LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                EmergencyTabFragment.tbNoticeCase.SceneNoticeDate = null;
                EmergencyTabFragment.tbNoticeCase.SceneNoticeTime = null;
                EmergencyTabFragment.tbNoticeCase.CompleteSceneDate = null;
                EmergencyTabFragment.tbNoticeCase.CompleteSceneTime = null;

                if (EmergencyTabFragment.tbNoticeCase.CaseStatus.equals("receive") || EmergencyTabFragment.mode == "new") {
                    Log.i(TAG, "ส่งข้อมูลไปยัง server");

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    builder.setMessage("ยืนยันการอัพโหลดข้อมูลไปยังเซิร์ฟเวอร์");
                    builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (EmergencyTabFragment.tbNoticeCase != null) {

                                boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
                                if (isSuccess) {
                                    UploadNoticeCaseToServer uploadNoticeCaseToServer = new UploadNoticeCaseToServer();
                                    uploadNoticeCaseToServer.execute(EmergencyTabFragment.tbNoticeCase);
                                    Log.i(TAG, "ส่งข้อมูลไปยัง server4");
                                }
                            } else {
                                if (snackbar == null || !snackbar.isShown()) {
                                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_LONG,
                                            getString(R.string.save_error)
                                                    + " " + EmergencyTabFragment.tbNoticeCase.NoticeCaseID.toString());
                                    snackBarAlert.createSnacbar();
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                        }
                    });
                    builder.show();

                } else {
                    Log.i(TAG, "btnAcceptCase");
                }

            }
            if (v == fabBtn) {
                hiddenKeyboard();
                EmergencyTabFragment.tbNoticeCase.CaseStatus = "receive";
                savedata();
            }
            if (v == edtInqTel) {
                calling(InqTel);
            }
            if (v == edtInvTel) {
                calling(InvTel);
            }
        }
    }

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

    class SendNewNoticeCase extends AsyncTask<TbNoticeCase, Void, ApiStatusResult> {
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
        protected ApiStatusResult doInBackground(TbNoticeCase... params) {
            return WelcomeActivity.api.sendNewNoticeCase(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatusResult apiStatusResult) {
            super.onPostExecute(apiStatusResult);
            progressDialog.dismiss();
            if (apiStatusResult.getStatus().equalsIgnoreCase("success")) {
                status_noticecase = true;
                EmergencyTabFragment.tbNoticeCase.NoticeCaseID = apiStatusResult.getData().getResult();
                btnNoticecase.setVisibility(View.GONE);
                btnDownloadfile.setVisibility(View.GONE);
                boolean isSuccess = dbHelper.updateNoticeCaseID(EmergencyTabFragment.tbNoticeCase);
                if (isSuccess) {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, apiStatusResult.getData().getReason()
                                        + "\nรหัสคดี " + EmergencyTabFragment.tbNoticeCase.NoticeCaseID
                                , Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mFragmentManager.popBackStack();
                                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.containerView, noticeCaseListFragment).addToBackStack(null).commit();
                                    }
                                });
                        snackbar.show();

                    }
                }
            } else {
                status_noticecase = false;
                EmergencyTabFragment.tbNoticeCase.CaseStatus = "receive";
                boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
                if (isSuccess) {
                    Toast.makeText(getActivity(),
                            getString(R.string.error_data) + " " + getString(R.string.network_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class UploadNoticeCaseToServer extends AsyncTask<TbNoticeCase, Void, ApiStatusResult> {
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
        protected ApiStatusResult doInBackground(TbNoticeCase... params) {
            return WelcomeActivity.api.sendNewNoticeCase(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatusResult apiStatusResult) {
            super.onPostExecute(apiStatusResult);
            progressDialog.dismiss();
            if (apiStatusResult.getStatus().equalsIgnoreCase("success")) {
                EmergencyTabFragment.tbNoticeCase.NoticeCaseID = apiStatusResult.getData().getResult();
                boolean isSuccess = dbHelper.updateNoticeCaseID(EmergencyTabFragment.tbNoticeCase);
                if (isSuccess) {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, "อัพโหลดข้อมูลสำเร็จเเล้ว"
                                , Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.i(TAG, "ส่งข้อมูลไปยัง server3 รหัส :" + EmergencyTabFragment.tbNoticeCase.NoticeCaseID);
                                    }
                                });
                        snackbar.show();
                    }
                }
            } else {
                Toast.makeText(getActivity(),
                        getString(R.string.error_data) + " " + getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();

            }
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
            String selectedItem = parent.getItemAtPosition(pos).toString();
            switch (parent.getId()) {
                case R.id.spnCaseType:
                    if (oldselectedCT == false) {
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
                    }
                    break;
                case R.id.spnSubCaseType:
                    if (oldselectedSubCT == false) {
                        selectedSubCaseType = mSubCaseTypeArray[pos][0];
                        EmergencyTabFragment.tbNoticeCase.SubCaseTypeID = selectedSubCaseType;
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

            //check which spinner triggered the listener
            switch (parent.getId()) {

                case R.id.spnSCDCcenterType:
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
                case R.id.spnSCDCagencyType:
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

    class DeleteNoticeCase extends AsyncTask<String, Void, ApiStatus> {
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
        protected ApiStatus doInBackground(String... strings) {
            return WelcomeActivity.api.deleteNoticeCase(strings[0]);
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            progressDialog.dismiss();
            if (apiStatus != null && apiStatus.getStatus().equalsIgnoreCase("success")) {
                status_deletecase = true;
                Toast.makeText(getActivity(),
                        apiStatus.getData().getReason().toString(),
                        Toast.LENGTH_SHORT).show();
                boolean isSuccess = dbHelper.DeleteNoticeCase(EmergencyTabFragment.tbNoticeCase.Mobile_CaseID);
                if (isSuccess) {
                    mFragmentManager.popBackStack();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, noticeCaseListFragment).addToBackStack(null).commit();
                }
            } else {
                status_deletecase = false;
                Toast.makeText(getActivity(),
                        getString(R.string.error_data) + " " + getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();
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

    private void savedata() {
        final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

        EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode = WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode;
        Log.i(TAG + " show SCDCAgencyCode", EmergencyTabFragment.tbNoticeCase.SCDCAgencyCode);

        EmergencyTabFragment.tbNoticeCase.LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        EmergencyTabFragment.tbNoticeCase.LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
        if (EmergencyTabFragment.tbNoticeCase != null) {

            boolean isSuccess = dbHelper.saveNoticeCase(EmergencyTabFragment.tbNoticeCase);
            if (isSuccess) {
                if (snackbar == null || !snackbar.isShown()) {
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                            getString(R.string.save_complete)
                                    + " " + EmergencyTabFragment.tbNoticeCase.LastUpdateDate.toString()
                                    + " " + EmergencyTabFragment.tbNoticeCase.LastUpdateTime.toString());
                    snackBarAlert.createSnacbar();
                }
            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_LONG,
                            getString(R.string.save_error)
                                    + " " + EmergencyTabFragment.tbNoticeCase.NoticeCaseID.toString());
                    snackBarAlert.createSnacbar();
                }
            }
        }
    }

}
