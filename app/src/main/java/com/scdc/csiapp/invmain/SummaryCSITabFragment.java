package com.scdc.csiapp.invmain;


import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectServer;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.inqmain.EmergencyTabFragment;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


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
    GetDateTime getDateTime;
    EditText edtReportNo;
    TextView edtStatus, edtUpdateDateTime2,  edtSceneNoticeDateTime,edtCompleteSceneDateTime ,edtCompleteSceneDateTimeTitle,edtUpdateDateTime,txtUpdateDateTimeTitle,edtInqInfo, edtInvInfo, edtPoliceStation;
    String[] updateDT, selectedCaseType, selectedSubCaseType;
    String message = "";
    String[][] mTypeCenterArray, mCaseTypeArray, mSubCaseTypeArray;
    String[] mTypeCenterArray2;
    Spinner spnCaseType, spnSubCaseType;
    String caseReportID,noticeCaseID,sSCDCAgencyCode;
    Snackbar snackbar;
    Button btnNoticecase, btnDownloadfile;
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


       // noticeCaseID = csiDataTabFragment.tbCaseScene.NoticeCaseID;
        //caseReportID = csiDataTabFragment.tbCaseScene.caseReportID;
        edtReportNo = (EditText) viewSummaryCSI.findViewById(R.id.edtReportNo);
        spnCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnCaseType);
        spnSubCaseType = (Spinner) viewSummaryCSI.findViewById(R.id.spnSubCaseType);
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);
        edtInqInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInqInfo);
        edtInvInfo = (TextView) viewSummaryCSI.findViewById(R.id.edtInvInfo);
        edtPoliceStation = (TextView) viewSummaryCSI.findViewById(R.id.edtPoliceStation);
//สถานะคดี
        edtStatus = (TextView) viewSummaryCSI.findViewById(R.id.edtStatus);

        edtUpdateDateTime2 = (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime2);
        //วันเวลาที่ผู้ตรวจสถานที่เกิดเหตุออกไปตรวจ
        edtSceneNoticeDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtSceneNoticeDateTime);
        //วันเวลาที่ตรวจคดีเสร็จ
        edtCompleteSceneDateTimeTitle= (TextView) viewSummaryCSI.findViewById(R.id.edtCompleteSceneDateTimeTitle);
        edtCompleteSceneDateTime = (TextView) viewSummaryCSI.findViewById(R.id.edtCompleteSceneDateTime);
        ///วันเวลาที่แก้ไขข้อมูลล่าสุด
        txtUpdateDateTimeTitle= (TextView) viewSummaryCSI.findViewById(R.id.txtUpdateDateTimeTitle);
        edtUpdateDateTime= (TextView) viewSummaryCSI.findViewById(R.id.edtUpdateDateTime);

        btnNoticecase = (Button) viewSummaryCSI.findViewById(R.id.btnNoticecase);
        btnDownloadfile = (Button) viewSummaryCSI.findViewById(R.id.btnDownloadfile);
        btnNoticecase.setOnClickListener(new SummaryOnClickListener());
        btnDownloadfile.setOnClickListener(new SummaryOnClickListener());

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
                Log.i(TAG, EmergencyTabFragment.tbNoticeCase.CaseTypeID);
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
            Log.i(TAG, "InvestigatorOfficialID : " + InvestigatorOfficialID);

            String mInvOfficialArray[] = dbHelper.SelectInvOfficial(EmergencyTabFragment.tbNoticeCase.InvestigatorOfficialID);
            if (mInvOfficialArray != null) {
                edtInvInfo.setText(mInvOfficialArray[4].toString() + " " + mInvOfficialArray[1].toString()
                        + " " + mInvOfficialArray[2].toString() + " (" + mInvOfficialArray[5].toString() + ") " +
                        mInvOfficialArray[7].toString());
            } else {
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
        fabBtn = (FloatingActionButton) viewSummaryCSI.findViewById(R.id.fabBtnSum);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




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
            if (v == btnNoticecase) {

                //new saveFullReport().execute(reportID);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("อัพโหลดข้อมูลเข้าสู่เซิร์ฟเวอร์");
                dialog.setPositiveButton("บันทึกแบบร่าง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // uploadDataToServer();
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

                        Toast.makeText(getActivity(),
                                "อัพโหลดข้อมูล บันทึกเเบบสมบูรณ์ เรียบร้อย", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.create();
                dialog.show();

            }
            if (v == btnDownloadfile) {
                Log.i("mViewBtnTransReport", "mViewBtnTransReport");

            }

        }
    }

    public boolean downloadDocFile(String sReportID) {
        // TODO Auto-generated method stub
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCaseReportID", sReportID));
        String resultServer = ConnectServer.getJsonPostGet(params,
                "downloadDocFile");
        /*** Default Value ***/
        String strStatusID = "0";
        String strError = "Unknow Status!";
        JSONObject c;

        try {
            c = new JSONObject(resultServer.replace("\uFEFF", ""));
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // Prepare Save Data
        if (strStatusID.equals("0")) {
            Log.i("downloadDocFile", strError);
            return false;
        } else {
            Log.i("downloadDocFile", strError);
            new DownloadFile().execute(strError);

            return true;
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
            String root = "";
            File myDir;
            String fileoutput="";
            try {
                root = Environment.getExternalStorageDirectory().toString();
                myDir = new File(root + "/CSIFiles/Documents/");
                myDir.mkdirs();
                ConnectServer.updateIP();
                String filepath = ConnectServer.urlWebIP+"csi_files/2559/July"+".doc";
                URL url = new URL(filepath);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());

                // Get File Name from URL
                fileoutput = Environment.getExternalStorageDirectory()+"/CSIFiles/Documents/"+params[0]+".doc";
                Log.i("fileoutput",filepath+":"+fileoutput);
                OutputStream output = new FileOutputStream(fileoutput);
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

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return fileoutput;
        }
/*
        private void publishProgress(String s) {
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            dialog.setProgress(Integer.parseInt(progress[0]));
        }
*/
        protected void onPostExecute(String arrData) {
            Toast.makeText(getActivity(),
                    "ดาวน์โหลดเรียบร้อยแล้ว",
                    Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),
                    "ไฟล์อยู่ใน "+arrData,
                    Toast.LENGTH_SHORT).show();
        }
    }
}