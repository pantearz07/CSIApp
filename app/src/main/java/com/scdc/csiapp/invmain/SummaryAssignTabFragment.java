package com.scdc.csiapp.invmain;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.DateDialog;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.TimeDialog;
import com.scdc.csiapp.main.WelcomeActivity;


public class SummaryAssignTabFragment extends Fragment {
    private static final String TAG = "DEBUG-SummaryAssignTabFragment";
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    // connect sqlite

    DBHelper dbHelper;
    SQLiteDatabase db;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Handler mHandler = new Handler();
    boolean statusConnect = false;
    private final static int INTERVAL = 1000 * 10; //10 second
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String officialID, noticecaseid;
    ArrayAdapter<String> adapterCaseType, adapterSubCaseType;
    EditText edtReportNo;
    FragmentManager mFragmentManager;

    Spinner spnCaseType, spnSubCaseType;
    String selectedCaseType, selectedSubCaseType, sCaseTypeID, sSubCaseTypeID;
    TextView edtUpdateDateTime2, edtStatus, edtInvestDateTime, edtUpdateDateTime, edtInqInfo, edtInvInfo,
            edtInvTel, edtInqTel, edtPoliceStation;
    String[] updateDT;
    String message = "";
    String[][] mTypeCenterArray, mCaseTypeArray, mSubCaseTypeArray, mTypeAgencyArray;
    String[] mTypeCenterArray2, mTypeAgencyArray2, mSubCaseTypeArray2;
    ArrayAdapter<String> adapterSCDCcenter, adapterSCDCagency;
    boolean oldSelectedCenter = false;
    Button btnAcceptCase, btnNoticecase, btnDownloadfile;
    String noticeCaseID, sSCDCAgencyCode;
    Snackbar snackbar;
    String SelectedAgencyID, SelectedCenterID;
    Spinner spnSCDCcenterType, spnSCDCagencyType;
    String selectedCenter, selectedAgencyCode;
    View layoutButton, layoutButton1, linearLayoutReportNo, layoutSceneNoticeDate;
    CSIDataTabFragment csiDataTabFragment;
    TextView editSceneNoticeDate, editSceneNoticeTime;
    String InqTel, InvTel;
    boolean status_updatecase = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewSummaryCSI = inflater.inflate(R.layout.summarycsi_tab_layout, null);

//โชว์ scenenoticedate & time
        final Context context = viewSummaryCSI.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        csiDataTabFragment = new CSIDataTabFragment();
        rootLayout = (CoordinatorLayout) viewSummaryCSI.findViewById(R.id.rootLayout);
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        dbHelper = new DBHelper(getContext());
        officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;
        cd = new ConnectionDetector(getActivity());

        noticeCaseID = AssignTabFragment.apiCaseScene.getTbNoticeCase().NoticeCaseID;
        Log.i(TAG, " NoticeCaseID " + noticeCaseID);

        updateDT = getDateTime.getDateTimeNow();
        Log.i("updateDataDateTime", updateDT[0] + " " + updateDT[1]);
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);
        edtUpdateDateTime2 = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime2);
        edtUpdateDateTime2.setText("อัพเดทข้อมูลเมื่อ " + getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate) + " เวลา " + AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime);

        linearLayoutReportNo = (LinearLayout) viewSummaryCSI.findViewById(R.id.linearLayoutReportNo);
        linearLayoutReportNo.setVisibility(View.GONE);
        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        edtReportNo.setVisibility(View.GONE);

        layoutButton1 = (LinearLayout) viewSummaryCSI.findViewById(R.id.layoutButton1);
        //layoutButton1.setVisibility(View.GONE);
        layoutButton = (LinearLayout) viewSummaryCSI.findViewById(R.id.layoutButton);
        layoutButton.setVisibility(View.GONE);
        edtInqInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInqInfo);
        edtInvInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInvInfo);
        edtInqTel = (TextView) viewSummaryCSI.findViewById(R.id.edtInqTel);
        edtInvTel = (TextView) viewSummaryCSI.findViewById(R.id.edtInvTel);
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
            AssignTabFragment.apiCaseScene.getTbNoticeCase().SCDCAgencyCode = WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode;
            sSCDCAgencyCode = WelcomeActivity.profile.getTbOfficial().SCDCAgencyCode;
        }
        btnNoticecase = (Button) viewSummaryCSI.findViewById(R.id.btnNoticecase);
        //btnDownloadfile = (Button) viewSummaryCSI.findViewById(R.id.btnDownloadfile);
        btnNoticecase.setOnClickListener(new SummaryOnClickListener());
        //btnDownloadfile.setOnClickListener(new SummaryOnClickListener());
        btnAcceptCase = (Button) viewSummaryCSI.findViewById(R.id.btnAcceptCase);
        btnAcceptCase.setOnClickListener(new SummaryOnClickListener());

        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        sCaseTypeID = AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseTypeID;
        sSubCaseTypeID = AssignTabFragment.apiCaseScene.getTbNoticeCase().SubCaseTypeID;

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
                        break;
                    }
                }
            } else {
                spnSubCaseType.setAdapter(null);

            }
        }

        edtInvInfo.setText(WelcomeActivity.profile.getTbOfficial().Rank + " "
                + WelcomeActivity.profile.getTbOfficial().FirstName + " "
                + WelcomeActivity.profile.getTbOfficial().LastName + " ("
                + WelcomeActivity.profile.getTbOfficial().Position + ")");
        edtInvTel.setText("โทร." + WelcomeActivity.profile.getTbOfficial().PhoneNumber.toString());
        InvTel = WelcomeActivity.profile.getTbOfficial().PhoneNumber.toString();
        edtInvTel.setOnClickListener(new SummaryOnClickListener());

        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().InquiryOfficialID == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().InquiryOfficialID.equals("") || AssignTabFragment.apiCaseScene.getTbNoticeCase().InquiryOfficialID.equals("null")) {
            edtInqInfo.setText("");
            edtInqTel.setText("");
        } else {
            String InquiryOfficialID = AssignTabFragment.apiCaseScene.getTbNoticeCase().InquiryOfficialID;
            Log.i(TAG, "InquiryOfficialID : " + InquiryOfficialID);

            String mInvOfficialArray[] = dbHelper.SelectInvOfficial(InquiryOfficialID);
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


        String mTypePoliceStationArray[] = dbHelper.SelectPoliceStation(AssignTabFragment.apiCaseScene.getTbNoticeCase().PoliceStationID);
        if (mTypePoliceStationArray != null) {
            edtPoliceStation.setText(mTypePoliceStationArray[2].toString());
        }

        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().getCaseStatus().equals("investigating")) {
            edtStatus.setText("กำลังดำเนินการตรวจ");
        } else if (AssignTabFragment.apiCaseScene.getTbNoticeCase().getCaseStatus().equals("notice")) {
            edtStatus.setText("แจ้งเหตุแล้ว รอจ่ายงาน");
            btnNoticecase.setEnabled(false);
        } else if (AssignTabFragment.apiCaseScene.getTbNoticeCase().getCaseStatus().equals("receive")) {
            edtStatus.setText("รอส่งแจ้งเหตุ");
        } else if (AssignTabFragment.apiCaseScene.getTbNoticeCase().getCaseStatus().equals("assign")) {
            edtStatus.setText("รอรับไปตรวจ");
        } else if (AssignTabFragment.apiCaseScene.getTbNoticeCase().getCaseStatus().equals("accept")) {
            edtStatus.setText("รับเรื่องแล้ว");
        } else if (AssignTabFragment.apiCaseScene.getTbNoticeCase().getCaseStatus().equals("investigated")) {
            edtStatus.setText("ตรวจเสร็จแล้ว");
        }
        TextView txtSceneNoticeDateTime = (TextView) viewSummaryCSI.findViewById(R.id.txtSceneNoticeDateTime);
        txtSceneNoticeDateTime.setText("วันเวลาที่จ่ายงาน");
        //ววันเวลาที่จ่ายงานห
        if (AssignTabFragment.apiCaseScene.getTbCaseScene().AssignmentDate == null || AssignTabFragment.apiCaseScene.getTbCaseScene().AssignmentDate.equals("")
                || AssignTabFragment.apiCaseScene.getTbCaseScene().AssignmentDate.equals("0000-00-00")) {
            edtSceneNoticeDateTime.setText("-");

        } else {
            edtSceneNoticeDateTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbCaseScene().AssignmentDate) + " เวลาประมาณ " + AssignTabFragment.apiCaseScene.getTbCaseScene().AssignmentDate + " น.");
        }

        edtCompleteSceneDateTime.setVisibility(View.GONE);
        //วันเวลาที่ จะออกไปตรวจ
        TextView edtCompleteSceneDateTimeTitle = (TextView) viewSummaryCSI.findViewById(R.id.edtCompleteSceneDateTimeTitle);
        edtCompleteSceneDateTimeTitle.setText("กำหนดวันเวลาทีออกไปตรวจ");
        editSceneNoticeDate = (TextView) viewSummaryCSI.findViewById(R.id.editSceneNoticeDate);
        editSceneNoticeTime = (TextView) viewSummaryCSI.findViewById(R.id.editSceneNoticeTime);
        editSceneNoticeDate.setOnClickListener(new SummaryOnClickListener());
        editSceneNoticeTime.setOnClickListener(new SummaryOnClickListener());
        //วันเวลาที่แก้ไขข้อมูลล่าสุด
        if (AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate == null || AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate.equals("")
                || AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate.equals("0000-00-00")) {

            edtUpdateDateTime.setText("-");

        } else {
            edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate) + " เวลาประมาณ " + AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime + " น.");

        }

        if (AssignTabFragment.mode == "view") {
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
            btnNoticecase.setEnabled(false);
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

    class UpdateStatusCase extends AsyncTask<ApiCaseScene, Void, ApiStatus> {
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
        protected ApiStatus doInBackground(ApiCaseScene... params) {
            return WelcomeActivity.api.updateStatusCase(params[0]);
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            progressDialog.dismiss();
            if (apiStatus.getStatus().equalsIgnoreCase("success")) {
                status_updatecase = true;
                Log.d(TAG, apiStatus.getData().getReason());
                boolean isSuccess = dbHelper.updateAlldataCase(AssignTabFragment.apiCaseScene);
                if (isSuccess) {
                    //แก้ไขตารางในเซิฟก่อน แล้วเด้งไปหน้าใหม่
                    edtUpdateDateTime.setText(getDateTime.changeDateFormatToCalendar(AssignTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate) + " เวลาประมาณ " + AssignTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime + " น.");
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, getString(R.string.save_complete) + " " + AssignTabFragment.apiCaseScene.getTbCaseScene().CaseReportID.toString(), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.edit), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle i = new Bundle();
                                        i.putSerializable(csiDataTabFragment.Bundle_Key, AssignTabFragment.apiCaseScene);
                                        i.putString(csiDataTabFragment.Bundle_mode, "edit");
                                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                                        csiDataTabFragment.setArguments(i);
                                        fragmentTransaction.replace(R.id.containerView, csiDataTabFragment).addToBackStack(null).commit();

                                    }
                                });
                        snackbar.show();
                    }
                } else {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(rootLayout, getString(R.string.save_error) + " " + AssignTabFragment.apiCaseScene.getTbCaseScene().CaseReportID.toString(), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                    }
                                });
                        snackbar.show();
                    }
                }

            } else {
                status_updatecase = false;
                Toast.makeText(getActivity(),
                        getString(R.string.error_data) + " " + getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private class SummaryOnClickListener implements View.OnClickListener {
        public void onClick(View v) {

            if (v == btnAcceptCase) {

                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                AssignTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "accept";

                if (editSceneNoticeDate.getText().toString().isEmpty()) {
                    AssignTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeDate = "";
                } else {
                    AssignTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeDate = getDateTime.changeDateFormatToDB(editSceneNoticeDate.getText().toString());
                }
                if (editSceneNoticeTime.getText().toString().isEmpty()) {
                    AssignTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeTime = "";
                } else {
                    AssignTabFragment.apiCaseScene.getTbNoticeCase().SceneNoticeTime = editSceneNoticeTime.getText().toString();

                }
                AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                AssignTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                AssignTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "accept";
                AssignTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                AssignTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                //save ลงมือถือ
                UpdateStatusCase statusCase = new UpdateStatusCase();
                statusCase.execute(AssignTabFragment.apiCaseScene);

            }

            if (v == editSceneNoticeDate) {
                Log.i("Click SceneNoticeDate", editSceneNoticeDate.getText().toString());
                DateDialog dialogKnowCaseDate = new DateDialog(v);
                dialogKnowCaseDate.show(getActivity().getFragmentManager(), "Date Picker");
            }
            if (v == editSceneNoticeTime) {
                Log.i("Click SceneNoticeTime", editSceneNoticeTime.getText().toString());
                TimeDialog dialogKnowCaseTime = new TimeDialog(v);
                dialogKnowCaseTime.show(getActivity().getFragmentManager(), "Time Picker");
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


}
