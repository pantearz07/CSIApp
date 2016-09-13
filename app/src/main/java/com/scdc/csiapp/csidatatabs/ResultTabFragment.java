package com.scdc.csiapp.csidatatabs;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pantearz07 on 22/9/2558.
 */
public class ResultTabFragment extends Fragment {
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String[] updateDT;
    String officialID, reportID;
    protected static final int DIALOG_AddGatewayCriminal = 0;
    protected static final int DIALOG_AddClueShown = 1;
    protected static final int DIALOG_AddPropertyLoss = 2;
    protected static final int DIALOG_AddEvidences = 3;
    TextView edtUpdateDateTime3;
    private ImageButton btn_clear_txt_24,btn_clear_txt_25,btn_clear_txt_26,btn_clear_txt_27,btn_clear_txt_28,btn_clear_txt_29,btn_clear_txt_30;
    private String sMaleCriminalNum, sFemaleCriminalNum, sCriminalUsedWeapon, sPersonInvolvedDetail,
            sFullEvidencePerformed, sAnnotation, sCompleteSceneDate, sCompleteSceneTime;
    private EditText editCircumstanceOfCaseDetail, editCriminalUseWeapon,
            editPersonInvolvedDetail, editEvidencePerformed, editAnnotation;

    private int iCriminalSumNum, iMaleCriminalNum, iFemaleCriminalNum = 0;
    String sCompleteSceneDate_New="";
    private TextView showCriminalSumNum, editCompleteSceneDate, editCompleteSceneTime;
    private EditText editCriminalMaleNum, editCriminalFemaleNum;

    // sufferer confine
    private String sConfineSufferer;
    private EditText editConfineSufferer;

    private ViewGroup viewByIdadddialog;
    // gatewaycriminal : ทางเข้าออกของคนร้าย

    private ArrayList<HashMap<String, String>> gatewaycriminalList;
    private ListView listViewGatewayCriminal;
    private Button btnAddGatewayCriminal;

    // clueshown : ร่องรอยที่ปรากฏ
    private ArrayList<HashMap<String, String>> clueShownList;
    private ListView listViewClueShown;
    private Button btnAddClueShown;

    // property
    private ArrayList<HashMap<String, String>> propertylossList;
    private ListView listViewPropertyLoss;
    private Button btnPropertyLoss;

    // btnAddEvidences
    private ArrayList<HashMap<String, String>> evidencesList;
    private ListView listViewEvidences;
    private Button btnAddEvidences;
    String sRSID;
    TextView txtPhoto,txtVideo;

    private View mViewAddGatewayCriminal,mViewAddClueShown,mViewAddPropertyLoss,mViewAddEvidences;
    ImageButton btnShowHide1,btnShowHide2,btnShowHide3,btnShowHide4;
    private boolean viewGroupIsVisible = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewDetails = inflater.inflate(R.layout.result_tab_layout, null);
        mContext = viewDetails.getContext();
        rootLayout = (CoordinatorLayout) viewDetails.findViewById(R.id.rootLayout);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.networkConnectivity();
        isConnectingToInternet = cd.isConnectingToInternet();
        updateDT = getDateTime.updateDataDateTime();
        Log.i("page viewDetails", reportID);
        edtUpdateDateTime3 = (TextView) viewDetails.findViewById(R.id.edtUpdateDateTime3);
         sRSID = null;
        mViewAddGatewayCriminal = viewDetails.findViewById(R.id.tableRowAddGatewayCriminal);
        mViewAddClueShown = viewDetails.findViewById(R.id.tableRowAddClueShown);
        mViewAddPropertyLoss = viewDetails.findViewById(R.id.tableRowPropertyLoss);
        mViewAddEvidences = viewDetails.findViewById(R.id.tableRowAddEvidences);
        mViewAddGatewayCriminal.setVisibility(View.VISIBLE);
        mViewAddClueShown.setVisibility(View.VISIBLE);
        mViewAddPropertyLoss.setVisibility(View.VISIBLE);
        mViewAddEvidences.setVisibility(View.VISIBLE);
        btnShowHide1 = (ImageButton) viewDetails
                .findViewById(R.id.btnShowHide1);
        btnShowHide2 = (ImageButton) viewDetails
                .findViewById(R.id.btnShowHide2);
        btnShowHide3 = (ImageButton) viewDetails
                .findViewById(R.id.btnShowHide3);
        btnShowHide4 = (ImageButton) viewDetails
                .findViewById(R.id.btnShowHide4);
        btnShowHide1.setOnClickListener(new ResultOnClickListener());
        btnShowHide2.setOnClickListener(new ResultOnClickListener());
        btnShowHide3.setOnClickListener(new ResultOnClickListener());
        btnShowHide4.setOnClickListener(new ResultOnClickListener());
        btn_clear_txt_24 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_24);
        btn_clear_txt_25 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_25);
        btn_clear_txt_26 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_26);
        btn_clear_txt_27 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_27);
        btn_clear_txt_28 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_28);
        btn_clear_txt_29 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_29);
        btn_clear_txt_30 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_30);

        editPersonInvolvedDetail = (EditText) viewDetails
                .findViewById(R.id.editMorePeople);
        editPersonInvolvedDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_24.setVisibility(View.VISIBLE);
                btn_clear_txt_24.setOnClickListener(new ResultOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
// การดำเนินการเกี่ยวกับวัตถุพยาน
        editEvidencePerformed = (EditText) viewDetails
                .findViewById(R.id.editEvidencePerformed);
        editEvidencePerformed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_25.setVisibility(View.VISIBLE);
                btn_clear_txt_25.setOnClickListener(new ResultOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // หมายเหตุ
        editAnnotation = (EditText) viewDetails.findViewById(R.id.editAnnotation);
        editAnnotation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_26.setVisibility(View.VISIBLE);
                btn_clear_txt_26.setOnClickListener(new ResultOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // ข้อมูลคนร้าย (กรณีชิงทรัพย์/ปล้นทรัพย์)
        // จำนวนคนร้าย
        editCriminalMaleNum = (EditText) viewDetails
                .findViewById(R.id.editCriminalAmountMale);
        editCriminalFemaleNum = (EditText) viewDetails
                .findViewById(R.id.editCriminalAmountFemale);
        showCriminalSumNum = (TextView) viewDetails
                .findViewById(R.id.txtCriminalAmount);
        editCriminalMaleNum.addTextChangedListener(new TextWatcher() {
            // criminalSumAmount , maleCriminalAmount , femaleCriminalAmount
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                sMaleCriminalNum = s.toString();
                Log.i("male", sMaleCriminalNum);
                /**/
                if (sMaleCriminalNum.length() == 0) {
                    iMaleCriminalNum = 0;
                } else {

                    iMaleCriminalNum = Integer.parseInt(sMaleCriminalNum);
                }

                showCriminalSumNum.setText(String.valueOf(sMaleCriminalNum
                        + sFemaleCriminalNum));
                btn_clear_txt_27.setVisibility(View.VISIBLE);
                btn_clear_txt_27.setOnClickListener(new ResultOnClickListener());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                sMaleCriminalNum = s.toString();
                Log.i("male", sMaleCriminalNum);
				/**/
                if (sMaleCriminalNum.length() == 0) {
                    iMaleCriminalNum = 0;
                } else {

                    iMaleCriminalNum = Integer.parseInt(sMaleCriminalNum);
                }

                showCriminalSumNum.setText(String.valueOf(iMaleCriminalNum
                        + iFemaleCriminalNum));
            }
        });
        editCriminalFemaleNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                sFemaleCriminalNum = s.toString();
                Log.i("female", sFemaleCriminalNum);
				/**/
                if (sFemaleCriminalNum.length() == 0) {
                    iFemaleCriminalNum = 0;
                } else {
                    iFemaleCriminalNum = Integer.parseInt(sFemaleCriminalNum);

                }
                btn_clear_txt_28.setVisibility(View.VISIBLE);
                btn_clear_txt_28.setOnClickListener(new ResultOnClickListener());
                showCriminalSumNum.setText(String.valueOf(iMaleCriminalNum
                        + iFemaleCriminalNum));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                sFemaleCriminalNum = s.toString();
                Log.i("female", sFemaleCriminalNum);
				/**/
                if (sFemaleCriminalNum.length() == 0) {
                    iFemaleCriminalNum = 0;
                } else {
                    iFemaleCriminalNum = Integer.parseInt(sFemaleCriminalNum);

                }

                showCriminalSumNum.setText(String.valueOf(iMaleCriminalNum
                        + iFemaleCriminalNum));

            }
        });

        // มีการใช้อาวุธ
        editCriminalUseWeapon = (EditText) viewDetails
                .findViewById(R.id.editCriminalUseWeapon);
        editCriminalUseWeapon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_29.setVisibility(View.VISIBLE);
                btn_clear_txt_29.setOnClickListener(new ResultOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // การพันธนาการผู้เสียหาย
        editConfineSufferer = (EditText) viewDetails
                .findViewById(R.id.editConfineSufferer);
        editConfineSufferer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_clear_txt_30.setVisibility(View.VISIBLE);
                btn_clear_txt_30.setOnClickListener(new ResultOnClickListener());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editCompleteSceneDate = (TextView) viewDetails
                .findViewById(R.id.editCompleteSceneDate);
        editCompleteSceneDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickCompleteSceneDate", "null");
                DateDialog dialogCompleteSceneDate = new DateDialog(view);
                dialogCompleteSceneDate.show(getActivity().getFragmentManager(), "Date Picker");

            }

        });
        editCompleteSceneTime = (TextView) viewDetails
                .findViewById(R.id.editCompleteSceneTime);
        editCompleteSceneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ClickCompleteSceneTime", "null");
                TimeDialog dialogCompleteSceneTime = new TimeDialog(view);
                dialogCompleteSceneTime.show(getActivity().getFragmentManager(), "Time Picker");
            }

        });
// ทางเข้า-ออกคนร้าย
        listViewGatewayCriminal = (ListView) viewDetails
                .findViewById(R.id.listViewAddGatewayCriminal);
        listViewGatewayCriminal.setVisibility(View.GONE);
        listViewGatewayCriminal.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedGatewayCriminal(reportID, "gatewaycriminal");
        btnAddGatewayCriminal = (Button) viewDetails.findViewById(R.id.btnAddGatewayCriminal);
        btnAddGatewayCriminal.setOnClickListener(new ResultOnClickListener());
// ร่องรอยที่ปรากฏ ตรวจพบร่องรอยการรื้อค้น/งัดแงะบริเวณ clueshown
        listViewClueShown = (ListView) viewDetails
                .findViewById(R.id.listViewAddClueShown);
        listViewClueShown.setVisibility(View.GONE);
        listViewClueShown.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedClueShown(reportID, "clueshown");
        btnAddClueShown = (Button) viewDetails.findViewById(R.id.btnAddClueShown);
        btnAddClueShown.setOnClickListener(new ResultOnClickListener());

// รายการทรัพย์สินที่คนร้ายโจรกรรม
        listViewPropertyLoss = (ListView) viewDetails
                .findViewById(R.id.listViewPropertyLoss);
        listViewPropertyLoss.setVisibility(View.GONE);
        listViewPropertyLoss.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedPropertyloss(reportID);
        btnPropertyLoss = (Button) viewDetails.findViewById(R.id.btnPropertyLoss);
        btnPropertyLoss.setOnClickListener(new ResultOnClickListener());


        // ตรวจเก็บลายนิ้วมือแฝง และฝามือแฝง
        listViewEvidences = (ListView) viewDetails
                .findViewById(R.id.listViewEvidences);
        listViewEvidences.setVisibility(View.GONE);
        listViewEvidences.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedFindEvidence(reportID);
        btnAddEvidences = (Button) viewDetails.findViewById(R.id.btnAddEvidences);
        btnAddEvidences.setOnClickListener(new ResultOnClickListener());


        fabBtn = (FloatingActionButton) viewDetails.findViewById(R.id.fabBtnResult);
        fabBtn.setOnClickListener(new ResultOnClickListener());

        return viewDetails;
    }

    public class ResultOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v == fabBtn) {
                saveAllResultData();
            }
            if (v == btn_clear_txt_24) {
                editPersonInvolvedDetail.setText("");
            }
            if (v == btn_clear_txt_25) {
                editEvidencePerformed.setText("");
            }
            if (v == btn_clear_txt_26) {
                editAnnotation.setText("");
            }
            if (v == btn_clear_txt_27) {
                editCriminalMaleNum.setText("");
            }
            if (v == btn_clear_txt_28) {
                editCriminalFemaleNum.setText("");
            }
            if (v == btn_clear_txt_29) {
                editCriminalUseWeapon.setText("");
            }
            if (v == btn_clear_txt_30) {
                editConfineSufferer.setText("");
            }
            if (v == btnAddGatewayCriminal) {
                Log.i("GatewayCriminal", "showlist");
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();

                sRSID = "GC_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
                Intent showActivity = new Intent(getActivity(), GatewayCriminalActivity.class);
                showActivity.putExtra("sGatewayCriminalID", sRSID);
                showActivity.putExtra("type", "new");
                startActivity(showActivity);
               // ShowSelectedGatewayCriminal(reportID, "gatewaycriminal");
               //viewByIdadddialog = (ViewGroup) v.findViewById(R.id.layout_gatewaycriminal_dialog);

              //  createdDialog(DIALOG_AddGatewayCriminal).show();
            }
            if (v == btnAddClueShown) {
                Log.i("ClueShown", "showlist");
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sRSID = "CS_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
                Intent showActivity = new Intent(getActivity(), ClueShownActivity.class);
                showActivity.putExtra("sClueShownID", sRSID);
                showActivity.putExtra("type", "new");
                startActivity(showActivity);
                /*viewByIdadddialog = (ViewGroup) v.findViewById(R.id.layout_clueshown_dialog);

                createdDialog(DIALOG_AddClueShown).show();*/
            }
            if (v == btnPropertyLoss) {
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sRSID = "PL_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
                Intent showActivity = new Intent(getActivity(), PropertyLossActivity.class);
                showActivity.putExtra("sPropertyLossID", sRSID);
                showActivity.putExtra("type", "new");
                startActivity(showActivity);

               // viewByIdadddialog = (ViewGroup) v.findViewById(R.id.layout_propertyloss_dialog);

               // createdDialog(DIALOG_AddPropertyLoss).show();
            }
            if (v == btnAddEvidences) {
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sRSID = "EV_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
                Intent showActivity = new Intent(getActivity(), EvidencesActivity.class);
                showActivity.putExtra("sEvidencesID", sRSID);
                showActivity.putExtra("type", "new");
                startActivity(showActivity);

                //viewByIdadddialog = (ViewGroup) v.findViewById(R.id.layout_evidences_dialog);

                //createdDialog(DIALOG_AddEvidences).show();
            }
            if(v == btnShowHide1){
                if (viewGroupIsVisible) {
                    mViewAddGatewayCriminal.setVisibility(View.VISIBLE);
                    btnShowHide1.setImageResource(R.drawable.ic_maxlayout);
                } else {
                    mViewAddGatewayCriminal.setVisibility(View.GONE);
                    btnShowHide1.setImageResource(R.drawable.ic_minlayout);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if(v == btnShowHide2){
                if (viewGroupIsVisible) {
                    mViewAddClueShown.setVisibility(View.VISIBLE);
                    btnShowHide2.setImageResource(R.drawable.ic_maxlayout);
                } else {
                    mViewAddClueShown.setVisibility(View.GONE);
                    btnShowHide2.setImageResource(R.drawable.ic_minlayout);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if(v == btnShowHide3){
                if (viewGroupIsVisible) {
                    mViewAddPropertyLoss.setVisibility(View.VISIBLE);
                    btnShowHide3.setImageResource(R.drawable.ic_maxlayout);
                } else {
                    mViewAddPropertyLoss.setVisibility(View.GONE);
                    btnShowHide3.setImageResource(R.drawable.ic_minlayout);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if(v == btnShowHide4){
                if (viewGroupIsVisible) {
                    mViewAddEvidences.setVisibility(View.VISIBLE);
                    btnShowHide4.setImageResource(R.drawable.ic_maxlayout);
                } else {
                    mViewAddEvidences.setVisibility(View.GONE);
                    btnShowHide4.setImageResource(R.drawable.ic_minlayout);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
        }
    }

    private boolean saveAllResultData() {
        sCompleteSceneDate = editCompleteSceneDate.getText().toString();

        if(sCompleteSceneDate.length() == 10) {

            sCompleteSceneDate_New = getDateTime.changeDateFormatToDB(sCompleteSceneDate);
            Log.i("sCompleteSceneDate_New", sCompleteSceneDate_New);
        }else{
            sCompleteSceneDate_New = "";
        }
        sCompleteSceneTime = editCompleteSceneTime.getText().toString();
        if (editCriminalMaleNum.getText().toString().length() != 0) {
            iMaleCriminalNum = Integer.parseInt(editCriminalMaleNum.getText()
                    .toString());

        }

        if (editCriminalFemaleNum.getText().toString().length() != 0) {
            iFemaleCriminalNum = Integer.parseInt(editCriminalFemaleNum
                    .getText().toString());

        }
        sCriminalUsedWeapon = "";
        if (editCriminalUseWeapon.getText().toString().length() != 0) {
            sCriminalUsedWeapon = editCriminalUseWeapon.getText().toString();

        }
        sPersonInvolvedDetail = "";
        if (editPersonInvolvedDetail.getText().toString().length() != 0) {
            sPersonInvolvedDetail = editPersonInvolvedDetail.getText()
                    .toString();

        }

        sFullEvidencePerformed = "";
        if (editEvidencePerformed.getText().toString().length() != 0) {
            sFullEvidencePerformed = editEvidencePerformed.getText().toString();

        }
        // หมายเหตุ
        sAnnotation = "";
        if (editAnnotation.getText().toString().length() != 0) {
            sAnnotation = editAnnotation.getText().toString();

        }
        sConfineSufferer = "";
        if (editConfineSufferer.getText().toString().length() != 0) {
            sConfineSufferer = editConfineSufferer.getText().toString();

        }
        new saveResultsData().execute(reportID, sCompleteSceneDate_New,
                sCompleteSceneTime, String.valueOf(iMaleCriminalNum), String.valueOf(iFemaleCriminalNum), sCriminalUsedWeapon,
                sPersonInvolvedDetail, sConfineSufferer,sFullEvidencePerformed, sAnnotation, updateDT[0],
                updateDT[1]);
        return true;
    }
    class saveResultsData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";

            long updateCaseSceneResults = mDbHelper.updateCaseSceneResults(params[0], params[1], params[2],
                    params[3], params[4], params[5], params[6], params[7],
                    params[8], params[9], params[10],params[11]);

            if (updateCaseSceneResults <= 0) {
                Log.i("update Results", "error");
                arrData = "error";
            } else {
                Log.i("update Results", "save");
                arrData = "save";
            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
           String message;
            if (arrData == "save") {
                message = "บันทึกข้อมูลเรียบร้อยแล้ว";

            } else {
                message = "เกิดข้อผิดพลาด";

            }
            Log.i("save Results", message);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

        }
    }


    protected Dialog createdDialog(int id) {
        Dialog dialog = null;
        final AlertDialog.Builder builderDialog1,builderDialog2;
        final LayoutInflater inflaterDialog;
        View Viewlayout;

        builderDialog1 = new AlertDialog.Builder(getActivity());
        builderDialog2 = new AlertDialog.Builder(getActivity());
        inflaterDialog = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (id) {
            case DIALOG_AddGatewayCriminal:

                Viewlayout = inflaterDialog
                        .inflate(
                                R.layout.add_gatewaycriminal_dialog,viewByIdadddialog);
                builderDialog1
                        .setIcon(android.R.drawable.btn_star_big_on);
                builderDialog1
                        .setTitle("เพิ่มร่องรอยทางเข้า-ออกที่พบ");
                builderDialog1.setView(Viewlayout);
                final AutoCompleteTextView editGatewayCriminalDetail = (AutoCompleteTextView) Viewlayout
                        .findViewById(R.id.editGatewayCriminalDetail);
                String[] mGateClueArray;
                mGateClueArray = getResources().getStringArray(
                        R.array.gate_clue);
                ArrayAdapter<String> adapterGateClue = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        mGateClueArray);

                editGatewayCriminalDetail.setThreshold(1);
                editGatewayCriminalDetail.setAdapter(adapterGateClue);

                editGatewayCriminalDetail.setHint("อธิบายการงัด, การเจาะ ");

                // Button OK
                builderDialog1.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                                if (editGatewayCriminalDetail.getText()
                                        .toString().equals("")) {

                                    builderDialog2
                                            .setIcon(android.R.drawable.btn_star_big_on);
                                    builderDialog2
                                            .setTitle("กรุณากรอกข้อมูลวิธีการเข้า-ออกที่พบ!");
                                    builderDialog2.setPositiveButton(
                                            "OK", null);
                                    builderDialog2.show();

                                } else {

                                    String sRSTypeID, sRSDetail;
                                    sRSTypeID = "gatewaycriminal";
                                    sRSDetail = "";
                                    if (editGatewayCriminalDetail.getText()
                                            .toString().length() != 0) {
                                        sRSDetail = editGatewayCriminalDetail
                                                .getText().toString();
                                    }
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    Toast.makeText(context,
                                            "เพิ่มข้อมูลเรียบร้อยเเล้ว",
                                            Toast.LENGTH_LONG).show();
                                    Log.i("show sRSTypeID", sRSTypeID);
                                    saveDataGatewayCriminal(reportID,sRSTypeID, sRSDetail);

                                }
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

                dialog = builderDialog1.create();
                break;
            case DIALOG_AddClueShown:

                Viewlayout = inflaterDialog
                        .inflate(
                                R.layout.add_clueshown_dialog, viewByIdadddialog);
                builderDialog1
                        .setIcon(android.R.drawable.btn_star_big_on);
                builderDialog1.setTitle("เพิ่มร่อยรอยรื้อค้น /งัดแงะ");
                builderDialog1.setView(Viewlayout);
                final EditText editClueShownPosition = (EditText) Viewlayout
                        .findViewById(R.id.editClueShownPosition);
                builderDialog1.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                                if (editClueShownPosition.getText().toString()
                                        .equals("")) {

                                    builderDialog2
                                            .setIcon(android.R.drawable.btn_star_big_on);
                                    builderDialog2
                                            .setTitle("กรุณากรอกข้อมูลร่อยรอยรื้อค้น /งัดแงะ");
                                    builderDialog2.setPositiveButton("OK",
                                            null);
                                    builderDialog2.show();

                                } else {

                                    String sRSTypeID, sRSDetail;
                                    sRSTypeID = "clueshown";
                                    sRSDetail = "";
                                    if (editClueShownPosition.getText()
                                            .toString().length() != 0) {
                                        sRSDetail = editClueShownPosition
                                                .getText().toString();
                                    }
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    Toast.makeText(context,
                                            "เพิ่มข้อมูลเรียบร้อยเเล้ว",
                                            Toast.LENGTH_LONG).show();
                                    Log.i("show sRSTypeID", sRSTypeID);
                                    saveDataGatewayCriminal(reportID,
                                            sRSTypeID, sRSDetail);

                                }
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

                dialog = builderDialog1.create();
                break;
            case DIALOG_AddPropertyLoss:

                Viewlayout = inflaterDialog
                        .inflate(
                                R.layout.add_propertyloss_dialog, viewByIdadddialog);
                builderDialog1
                        .setIcon(android.R.drawable.btn_star_big_on);
                builderDialog1.setTitle("เพิ่มทรัพย์สินที่หายไป");
                builderDialog1.setView(Viewlayout);
                final EditText editPropertyLossName = (EditText) Viewlayout
                        .findViewById(R.id.editPropertyLossName);
                final TextView editNoti = (TextView) Viewlayout
                        .findViewById(R.id.editNoti);
                editPropertyLossName.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        // TODO Auto-generated method stub
                        editNoti.setVisibility(View.GONE);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        // TODO Auto-generated method stub
                        editNoti.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        if (s.toString().length() != 0) {
                            editNoti.setVisibility(View.GONE);

                        } else {
                            editNoti.setVisibility(View.VISIBLE);
                        }
                    }
                });
                final EditText editPropertyLossAmount = (EditText) Viewlayout
                        .findViewById(R.id.editPropertyLossAmount);
                final AutoCompleteTextView autoPropertyLossUnit = (AutoCompleteTextView) Viewlayout
                        .findViewById(R.id.autoPropertyLossUnit);
                String[] mUnitArray;
                mUnitArray = getResources().getStringArray(
                        R.array.property_unit);
                ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_dropdown_item_1line, mUnitArray);

                autoPropertyLossUnit.setThreshold(1);
                autoPropertyLossUnit.setAdapter(adapterUnit);
                final EditText editPropertyLossPosition = (EditText) Viewlayout
                        .findViewById(R.id.editPropertyLossPosition);

                final EditText editPropertyInsurance = (EditText) Viewlayout
                        .findViewById(R.id.editPropertyInsurance);

                // Button OK
                builderDialog1.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (editPropertyLossName.getText().toString()
                                        .equals("")) {

                                    builderDialog2
                                            .setIcon(android.R.drawable.btn_star_big_on);
                                    builderDialog2
                                            .setTitle("กรุณากรอกชื่อทรัพย์สินที่หายไป!");
                                    builderDialog2.setPositiveButton("OK",
                                            null);
                                    builderDialog2.show();

                                } else {
                                    String sPropertyLossName, sPropertyLossAmount, sPropertyLossUnit, sPropertyLossPosition, sPropInsurance;
                                    sPropertyLossName = "";
                                    if (editPropertyLossName.getText()
                                            .toString().length() != 0) {
                                        sPropertyLossName = editPropertyLossName
                                                .getText().toString();
                                    }
                                    sPropertyLossAmount = "";
                                    if (editPropertyLossAmount.getText()
                                            .toString().length() != 0) {
                                        sPropertyLossAmount = editPropertyLossAmount
                                                .getText().toString();
                                    }
                                    sPropertyLossUnit = "";
                                    if (autoPropertyLossUnit.getText()
                                            .toString().length() != 0) {
                                        sPropertyLossUnit = autoPropertyLossUnit
                                                .getText().toString();
                                    }
                                    sPropertyLossPosition = "";
                                    if (editPropertyLossPosition.getText()
                                            .toString().length() != 0) {
                                        sPropertyLossPosition = editPropertyLossPosition
                                                .getText().toString();
                                    }
                                    sPropInsurance = "";
                                    if (editPropertyInsurance.getText()
                                            .toString().length() != 0) {
                                        sPropInsurance = editPropertyInsurance
                                                .getText().toString();
                                    }
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    Toast.makeText(context,
                                            "เพิ่มข้อมูลเรียบร้อยเเล้ว",
                                            Toast.LENGTH_LONG).show();
                                    saveDataPropertyloss(reportID,
                                            sPropertyLossName,
                                            sPropertyLossAmount,
                                            sPropertyLossUnit,
                                            sPropertyLossPosition,
                                            sPropInsurance);
                                    dialog.dismiss();
                                }
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
                dialog = builderDialog1.create();
                break;
            case DIALOG_AddEvidences:
                Viewlayout = inflaterDialog
                        .inflate(
                                R.layout.add_evidences_dialog, viewByIdadddialog);
                builderDialog1
                        .setIcon(android.R.drawable.btn_star_big_on);
                builderDialog1.setTitle("เพิ่มข้อมูลวัตถุพยานและตำแหน่งที่ตรวจพบ");
                builderDialog1.setView(Viewlayout);
                final Spinner evidenceType = (Spinner) Viewlayout
                        .findViewById(R.id.spinnerEvidenceType);
                final EditText editEvidenceType = (EditText) Viewlayout
                        .findViewById(R.id.editEvidenceType);
                editEvidenceType.setVisibility(View.GONE);

                evidenceType
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            public void onItemSelected(
                                    AdapterView<?> adapterView, View view,
                                    int i, long l) {
                                // TODO Auto-generated method stub

                                if (String.valueOf(
                                        evidenceType.getSelectedItem()).equals(
                                        "อื่นๆ")) {
                                    editEvidenceType
                                            .setVisibility(View.VISIBLE);
                                }

                            }

                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getActivity(),
                                        String.valueOf("กรุณาเลือก"),
                                        Toast.LENGTH_SHORT).show();

                            }

                        });
                final EditText editEvidenceNumber = (EditText) Viewlayout
                        .findViewById(R.id.editEvidenceNumber);
                final EditText editFindEvidenceZone = (EditText) Viewlayout
                        .findViewById(R.id.editFindEvidenceZone);
                final EditText editMarking = (EditText) Viewlayout
                        .findViewById(R.id.editMarking);
                final EditText editParceling = (EditText) Viewlayout
                        .findViewById(R.id.editParceling);
                final EditText editEvidencePerformed = (EditText) Viewlayout
                        .findViewById(R.id.editEvidencePerformed);

                // Button OK
                builderDialog1.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialog.dismiss();

								/**/
                                String sEvidenceTypeName, sEvidenceNumber, sFindEvidenceZone, sEvidencePerformed, sMarking, sParceling;
                                sEvidenceTypeName = "";
                                if (String.valueOf(evidenceType
                                        .getSelectedItem()) != "อื่นๆ") {
                                    sEvidenceTypeName = String
                                            .valueOf(evidenceType
                                                    .getSelectedItem());
                                }
                                if (editEvidenceType.getText().toString()
                                        .length() != 0) {
                                    sEvidenceTypeName = editEvidenceType
                                            .getText().toString();
                                }
                                sEvidenceNumber = "";
                                if (editEvidenceNumber.getText().toString()
                                        .length() != 0) {
                                    sEvidenceNumber = editEvidenceNumber
                                            .getText().toString();
                                }
                                sFindEvidenceZone = "";
                                if (editFindEvidenceZone.getText().toString()
                                        .length() != 0) {
                                    sFindEvidenceZone = editFindEvidenceZone
                                            .getText().toString();
                                }
                                sMarking = "";
                                if (editMarking.getText().toString().length() != 0) {
                                    sMarking = editMarking.getText().toString();
                                }
                                sParceling = "";
                                if (editParceling.getText().toString().length() != 0) {
                                    sParceling = editParceling.getText()
                                            .toString();
                                }
                                sEvidencePerformed = "";
                                if (editEvidencePerformed.getText().toString()
                                        .length() != 0) {
                                    sEvidencePerformed = editEvidencePerformed
                                            .getText().toString();
                                }

                                Context context = getActivity()
                                        .getApplicationContext();

                                Toast.makeText(context,
                                        "เพิ่มข้อมูลเรียบร้อยเเล้ว",
                                        Toast.LENGTH_LONG).show();
                                saveDataFindEvidence(reportID,
                                        sEvidenceTypeName, sEvidenceNumber,
                                        sFindEvidenceZone, sMarking,
                                        sParceling, sEvidencePerformed);

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


                dialog = builderDialog1.create();
                break;
            default:
                dialog = null;
        }
        return dialog;
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

    public void onStart() {
        super.onStart();
        Log.i("Check", "onStart Result");

        new showData().execute(reportID);
        ShowSelectedGatewayCriminal(reportID, "gatewaycriminal");
        ShowSelectedClueShown(reportID, "clueshown");
        ShowSelectedPropertyloss(reportID);
        ShowSelectedFindEvidence(reportID);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause Result");
        saveAllResultData();


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
                edtUpdateDateTime3.setText("อัพเดทข้อมูลล่าสุดเมื่อวันที่ " + getDateTime.changeDateFormatToCalendar(arrData[7]) + " เวลา " + arrData[8]);
/*
                Toast.makeText(getActivity()
                                .getApplicationContext(),
                        "อัพเดทข้อมูลล่าสุดเมื่อวันที่ "+arrData[7]+ " เวลา "+arrData[8],
                        Toast.LENGTH_LONG).show();*/
                Log.i("SelectResultTab", arrData[14]+" "+arrData[42]+" "+arrData[43]+" "+ arrData[45]+" "+arrData[46]);
                String zerodate = "0000-00-00";
                if(arrData[15].length() == 10) {
                    if(arrData[15].equals(zerodate)){

                    }else {
                        editCompleteSceneDate.setText(getDateTime.changeDateFormatToCalendar(arrData[15]));
                    }
                }else{
                   // editCompleteSceneDate.setText("date3");
                    editCompleteSceneDate.setText(arrData[15]);
                }

               // editCompleteSceneDate.setText("");
                if(arrData[14].length() != 0) {
                    editCompleteSceneTime.setText(arrData[14]);
                }
                editCriminalMaleNum.setText(arrData[45]);
                editCriminalFemaleNum.setText(arrData[46]);

                editCriminalUseWeapon.setText(arrData[9]);
                editPersonInvolvedDetail.setText(arrData[42]);
                editEvidencePerformed.setText(arrData[43]);
                editAnnotation.setText(arrData[44]);
                editConfineSufferer.setText(arrData[3]);

            }else{
                Log.i("SelectDataCaseScene", "Null!! ");
            }
        }
    }
    public void saveDataGatewayCriminal(String sReportID, String sRSTypeID,
                                        String sRSDetail) {
        // TODO Auto-generated method stub
        String sRSID = null;
        String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();

        if (sRSTypeID == "clueshown") {
            sRSID = "CS_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];

        }
        if (sRSTypeID == "gatewaycriminal") {
            sRSID = "GC_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
        }

        long saveStatus = mDbHelper.SaveResultScene(sReportID, sRSID,
                sRSTypeID, sRSDetail);
        if (saveStatus <= 0) {
            Log.i("Recieve", "Error!! ");
        } else {
            if (sRSTypeID == "clueshown") {

                listViewClueShown.setVisibility(View.VISIBLE);
                ShowSelectedClueShown(reportID, sRSTypeID);

            }
            if (sRSTypeID == "gatewaycriminal") {
                listViewGatewayCriminal.setVisibility(View.VISIBLE);
                ShowSelectedGatewayCriminal(reportID, sRSTypeID);
            }
            Log.i("saveData ResultScene", sRSID + " " + sRSTypeID + " "
                    + sRSDetail);
        }

    }

    public void ShowSelectedGatewayCriminal(String sReportID, String sRSTypeID) {
        // TODO Auto-generated method stub
        Log.i("show sRSTypeID", sReportID + " " + sRSTypeID);
        gatewaycriminalList = mDbHelper.SelectAllResultScene(sReportID,
                sRSTypeID);


        if (gatewaycriminalList != null) {
            setListViewHeightBasedOnItems(listViewGatewayCriminal);
            Log.i("show sRSTypeID", String.valueOf(gatewaycriminalList.size()));
            listViewGatewayCriminal.setVisibility(View.VISIBLE);
            listViewGatewayCriminal.setAdapter(new GatewayFoundClueAdapter(
                    getActivity()));
        } else {


            listViewGatewayCriminal.setVisibility(View.GONE);
        }

    }

    public class GatewayFoundClueAdapter extends BaseAdapter {
        private Context context;

        public GatewayFoundClueAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return gatewaycriminalList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("InflateParams")
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_resultscene_data, null);

            }

            final String sRSTypeID = gatewaycriminalList.get(position).get(
                    "RSTypeID");
            final String sRSID = gatewaycriminalList.get(position).get("RSID");
            final String sRSDetail = gatewaycriminalList.get(position).get(
                    "RSDetail");
            Log.i("sRSDetail", sRSID + " " + sRSTypeID + " " + sRSDetail);

            // ColCode
            TextView txtGatewayCriminalDetails = (TextView) convertView.findViewById(R.id.txtDetails);
            txtGatewayCriminalDetails.setText(gatewaycriminalList.get(position)
                    .get("RSDetail"));
            /*final AutoCompleteTextView editGatewayCriminalDetail = (AutoCompleteTextView) convertView
                    .findViewById(R.id.editGatewayCriminalDetail);

            String[] mGateClueArray;
            mGateClueArray = getResources().getStringArray(R.array.gate_clue);
            ArrayAdapter<String> adapterGateClue = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mGateClueArray);

            editGatewayCriminalDetail.setThreshold(1);
            editGatewayCriminalDetail.setAdapter(adapterGateClue);
            if (sRSTypeID == "clueshown") {
                editGatewayCriminalDetail.setHint("อธิบายการรื้อค้น/งัดเเงะ");
            }*/
            txtPhoto = (TextView)convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView)convertView.findViewById(R.id.txtVideo);

            final String[][] arrDataPhoto2 = mDbHelper.SelectDataPhotoOfEachResultScene(reportID, sRSID, "photo");

            if (arrDataPhoto2 != null) {
                Log.i("arrDataPhoto_gateway", sRSID + " " + String.valueOf(arrDataPhoto2.length));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(arrDataPhoto2.length) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i("photo_gateway", sRSID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent showActivity = new Intent(getActivity(), GatewayCriminalActivity.class);
                    showActivity.putExtra("sGatewayCriminalID", sRSID);
                    showActivity.putExtra("type", "update");
                    startActivity(showActivity);
/*
                    adbEdit.setTitle("ยืนยันการเเก้ไขข้อมูล");
                    adbEdit.setNegativeButton("Cancel", null);
                    adbEdit.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long saveStatus = mDbHelper
                                            .updateDataSelectedResultScene(
                                                    sRSID, reportID,
                                                    sRSTypeID,
                                                    editGatewayCriminalDetail
                                                            .getText()
                                                            .toString());
                                    if (saveStatus <= 0) {
                                        Log.i("Recieve GatewayCriminal",
                                                "Error!! ");
                                    } else {
                                        Log.i("Recieve GatewayCriminal", "OK");
                                        Toast.makeText(getActivity(),
                                                "แก้ไขเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                    adbEdit.show();*/
                }
            });
            // imgDelete
            ImageButton imgDelete = (ImageButton) convertView
                    .findViewById(R.id.imgDelete);
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    getActivity());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adb.setTitle("ลบข้อมูล");
                    adb.setMessage("ยืนยันการลบข้อมูล");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long flg = mDbHelper
                                            .DeleteSelectedResultScene(gatewaycriminalList
                                                    .get(position).get("RSID"));
                                    if (flg > 0) {
                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllResultScene(sRSID);
                                        if (saveStatus2 <= 0) {
                                            Log.i("DeletePhotoOf gateway", "Cannot delete!! ");

                                        } else {
                                            Toast.makeText(getActivity(),
                                                    "ลบข้อมูลเรียบรอยแล้ว",
                                                    Toast.LENGTH_LONG).show();
                                            ShowSelectedGatewayCriminal(reportID,
                                                    sRSTypeID);
                                        }

                                    } else {
                                        Toast.makeText(getActivity(),
                                                "เกิดการผิดพลาด",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                            });
                    adb.show();
                }
            });

            return convertView;

        }

    }

    public void ShowSelectedClueShown(String sReportID, String sRSTypeID) {
        // TODO Auto-generated method stub
        Log.i("show sRSTypeID", sReportID + " " + sRSTypeID);
        clueShownList = mDbHelper.SelectAllResultScene(sReportID, sRSTypeID);



        if (clueShownList != null) {
            setListViewHeightBasedOnItems(listViewClueShown);
            Log.i("show sRSTypeID", String.valueOf(clueShownList.size()));
            listViewClueShown.setVisibility(View.VISIBLE);
            listViewClueShown.setAdapter(new ClueShownAdapter(getActivity()));
        } else {
            listViewClueShown.setVisibility(View.GONE);
        }

    }

    public class ClueShownAdapter extends BaseAdapter {
        private Context context;

        public ClueShownAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return clueShownList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("InflateParams")
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_resultscene_data, null);

            }

            final String sRSTypeID = clueShownList.get(position)
                    .get("RSTypeID");
            final String sRSID = clueShownList.get(position).get("RSID");
            final String sRSDetail = clueShownList.get(position)
                    .get("RSDetail");
            Log.i("sRSDetail", sRSID + " " + sRSTypeID + " " + sRSDetail);
            TextView txtDetails = (TextView) convertView.findViewById(R.id.txtDetails);
            txtDetails.setText(clueShownList.get(position)
                    .get("RSDetail"));
            /*/ ColCode
            final AutoCompleteTextView editGatewayCriminalDetail = (AutoCompleteTextView) convertView
                    .findViewById(R.id.editGatewayCriminalDetail);
            editGatewayCriminalDetail.setText(clueShownList.get(position).get(
                    "RSDetail"));
            String[] mGateClueArray;
            mGateClueArray = getResources().getStringArray(R.array.gate_clue);
            ArrayAdapter<String> adapterGateClue = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mGateClueArray);

            editGatewayCriminalDetail.setThreshold(1);
            editGatewayCriminalDetail.setAdapter(adapterGateClue);

            editGatewayCriminalDetail.setHint("อธิบายการรื้อค้น/งัดเเงะ");
*/
            txtPhoto = (TextView)convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView)convertView.findViewById(R.id.txtVideo);

            final String[][] arrDataPhoto2 = mDbHelper.SelectDataPhotoOfEachResultScene(reportID, sRSID, "photo");

            if (arrDataPhoto2 != null) {
                Log.i("arrDataPhoto_clueShown", sRSID + " " + String.valueOf(arrDataPhoto2.length));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(arrDataPhoto2.length) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i("Recieve_clueShown", sRSID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
           //final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
             //       getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent showActivity = new Intent(getActivity(), ClueShownActivity.class);
                    showActivity.putExtra("sClueShownID", sRSID);
                    showActivity.putExtra("type", "update");
                    startActivity(showActivity);
/*
                    adbEdit.setTitle("ยืนยันการเเก้ไขข้อมูล");
                    adbEdit.setNegativeButton("Cancel", null);
                    adbEdit.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long saveStatus = mDbHelper
                                            .updateDataSelectedResultScene(
                                                    sRSID, reportID,
                                                    sRSTypeID,
                                                    editGatewayCriminalDetail
                                                            .getText()
                                                            .toString());
                                    if (saveStatus <= 0) {
                                        Log.i("Recieve clueShown ", "Error!! ");
                                    } else {
                                        Log.i("Recieve clueShown", "OK");
                                        Toast.makeText(getActivity(),
                                                "แก้ไขเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                    adbEdit.show();*/
                }
            });
            // imgDelete
            ImageButton imgDelete = (ImageButton) convertView
                    .findViewById(R.id.imgDelete);
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    getActivity());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adb.setTitle("ลบข้อมูล");
                    adb.setMessage("ยืนยันการลบข้อมูล");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long flg = mDbHelper
                                            .DeleteSelectedResultScene(sRSID);
                                    if (flg > 0) {
                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllResultScene(sRSID);
                                        if (saveStatus2 <= 0) {
                                            Log.i("DeletePhotoOf ClueShown", "Cannot delete!! ");

                                        } else {
                                            Toast.makeText(getActivity(),
                                                    "ลบข้อมูลเรียบรอยแล้ว",
                                                    Toast.LENGTH_LONG).show();
                                            ShowSelectedClueShown(reportID,
                                                    sRSTypeID);
                                        }

                                    } else {
                                        Toast.makeText(getActivity(),
                                                "เกิดการผิดพลาด",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                            });
                    adb.show();
                }
            });

            return convertView;

        }

    }
    public void saveDataPropertyloss(String sReportID,
                                     String sPropertyLossName, String sPropertyLossAmount,
                                     String sPropertyLossUnit, String sPropertyLossPosition,
                                     String sPropInsurance) {
        // TODO Auto-generated method stub
        String sPropertyLossID;
        String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();

        sPropertyLossID = "PL_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
        long saveStatus = mDbHelper.saveDataPropertyloss(sReportID,
                sPropertyLossID, sPropertyLossName, sPropertyLossAmount,
                sPropertyLossUnit, sPropertyLossPosition, sPropInsurance);
        if (saveStatus <= 0) {
            Log.i("Recieve", "Error!! ");
        } else {
            listViewPropertyLoss.setVisibility(View.VISIBLE);
            ShowSelectedPropertyloss(reportID);
        }
        Log.i("saveData Propertyloss", sPropertyLossID + " "
                + sPropertyLossName + " " + sPropertyLossAmount + " "
                + sPropertyLossPosition);

    }
    public void ShowSelectedPropertyloss(String sREPORTID) {
        // TODO Auto-generated method stub
        propertylossList = mDbHelper.SelectAllPropertyloss(sREPORTID);
        if (propertylossList != null) {
            setListViewHeightBasedOnItems(listViewPropertyLoss);
            listViewPropertyLoss.setVisibility(View.VISIBLE);
            listViewPropertyLoss.setAdapter(new PropertylossAdapter(getActivity()));
        } else {
            listViewPropertyLoss.setVisibility(View.GONE);
        }

    }

    public class PropertylossAdapter extends BaseAdapter {
        private Context context;

        public PropertylossAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return propertylossList.size();
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
                convertView = inflater
                        .inflate(R.layout.list_propertyloss_data, null);

            }
            final String sPropertyLossID = propertylossList.get(position).get("PropertyLossID");
            final TextView txtPropertyLossName = (TextView) convertView
                    .findViewById(R.id.txtPropertyLossName);
            txtPropertyLossName.setText(propertylossList.get(position).get(
                    "PropertyLossName"));
            final TextView txtPropertyLossAmount = (TextView) convertView
                    .findViewById(R.id.txtPropertyLossAmount);
            txtPropertyLossAmount.setText(propertylossList.get(position).get(
                    "PropertyLossNumber"));
            final TextView txtPropertyLossUnit = (TextView) convertView
                    .findViewById(R.id.txtPropertyLossUnit);
            txtPropertyLossUnit.setText(propertylossList.get(position).get(
                    "PropertyLossUnit"));
            final TextView txtPropertyLossPosition = (TextView) convertView
                    .findViewById(R.id.txtPropertyLossPosition);
            txtPropertyLossPosition.setText(propertylossList.get(position).get(
                    "PropertyLossPosition"));
            final TextView txtPropertyInsurance = (TextView) convertView
                    .findViewById(R.id.txtPropertyInsurance);
            txtPropertyInsurance.setText(propertylossList.get(position).get(
                    "PropInsurance"));
/*
            final EditText txtPropertyLossName = (EditText) convertView
                    .findViewById(R.id.editPropertyLossName);
            txtPropertyLossName.setText(propertylossList.get(position).get(
                    "PropertyLossName"));

            final EditText txtPropertyLossAmount = (EditText) convertView
                    .findViewById(R.id.editPropertyLossAmount);
            txtPropertyLossAmount.setText(propertylossList.get(position).get(
                    "PropertyLossAmount"));
            final AutoCompleteTextView autoPropertyLossUnit = (AutoCompleteTextView) convertView
                    .findViewById(R.id.autoPropertyLossUnit);
            String[] mUnitArray;
            mUnitArray = getResources().getStringArray(R.array.property_unit);
            ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mUnitArray);

            autoPropertyLossUnit.setThreshold(1);
            autoPropertyLossUnit.setAdapter(adapterUnit);

            autoPropertyLossUnit.setText(propertylossList.get(position).get(
                    "PropertyLossUnit"));
            final EditText txtPropertyLossPosition = (EditText) convertView
                    .findViewById(R.id.editPropertyLossPosition);
            txtPropertyLossPosition.setText(propertylossList.get(position).get(
                    "PropertyLossPosition"));
            final EditText editPropertyInsurance = (EditText) convertView
                    .findViewById(R.id.editPropertyInsurance);
            editPropertyInsurance.setText(propertylossList.get(position).get(
                    "PropInsurance"));
*/
            txtPhoto = (TextView)convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView)convertView.findViewById(R.id.txtVideo);

            final String[][] arrDataPhoto2 = mDbHelper.SelectDataPhotoOfEachPropertyloss(reportID, sPropertyLossID, "photo");

            if (arrDataPhoto2 != null) {
                Log.i("Photo_Propertyloss", sPropertyLossID + " " + String.valueOf(arrDataPhoto2.length));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(arrDataPhoto2.length) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i("Recieve_Propertyloss", sPropertyLossID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent showActivity = new Intent(getActivity(), PropertyLossActivity.class);
                    showActivity.putExtra("sPropertyLossID", sPropertyLossID);
                    showActivity.putExtra("type", "update");
                    startActivity(showActivity);
/*
                    adbEdit.setTitle("ยืนยันการเเก้ไขข้อมูล");
                    adbEdit.setNegativeButton("Cancel", null);
                    adbEdit.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long saveStatus = mDbHelper
                                            .updateDataSelectedPropertyLoss(
                                                    propertylossList.get(
                                                            position).get(
                                                            "PropertyLossID"),
                                                    txtPropertyLossName
                                                            .getText()
                                                            .toString(),
                                                    txtPropertyLossAmount
                                                            .getText()
                                                            .toString(),
                                                    autoPropertyLossUnit
                                                            .getText()
                                                            .toString(),
                                                    txtPropertyLossPosition
                                                            .getText()
                                                            .toString(),
                                                    editPropertyInsurance
                                                            .getText()
                                                            .toString());
                                    if (saveStatus <= 0) {
                                        Log.i("Recieve PropertyLoss",
                                                "Error!! ");
                                    } else {
                                        Log.i("updateData PropertyLoss", "OK");
                                        Toast.makeText(getActivity(),
                                                "แก้ไขเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                    adbEdit.show();
               */
                }
            });
            // imgDelete
            ImageButton imgDelete = (ImageButton) convertView
                    .findViewById(R.id.imgDelete);
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    getActivity());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adb.setTitle("ลบข้อมูล");
                    adb.setMessage("ยืนยันการลบข้อมูล ["
                            + propertylossList.get(position).get(
                            "PropertyLossName") + "]");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long flg = mDbHelper
                                            .DeleteSelectedPropertyLoss(sPropertyLossID);
                                    if (flg > 0) {
                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllPropertyLoss(sPropertyLossID);
                                        if (saveStatus2 <= 0) {
                                            Log.i("DeletePhoto Property", "Cannot delete!! ");

                                        } else {
                                            Toast.makeText(getActivity(),
                                                    "ลบข้อมูลเรียบรอยแล้ว",
                                                    Toast.LENGTH_LONG).show();
                                            ShowSelectedPropertyloss(reportID);
                                        }


                                    } else {
                                        Toast.makeText(getActivity(),
                                                "เกิดข้อผิดพลาด",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                            });
                    adb.show();
                }
            });

            return convertView;

        }

    }
    public void saveDataFindEvidence(String sReportID,
                                     String sEvidenceTypeName, String sEvidenceNumber,
                                     String sFindEvidenceZone, String sMarking, String sParceling,
                                     String sEvidencePerformed) {
        // TODO Auto-generated method stub
        String sFindEvidenceID;
        String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
        sFindEvidenceID = "FE_" +CurrentDate_ID[2]+CurrentDate_ID[1]+CurrentDate_ID[0]+"_"+CurrentDate_ID[3]+CurrentDate_ID[4]+CurrentDate_ID[5];
        long saveStatus = mDbHelper.saveDataFindEvidence(sReportID,
                sFindEvidenceID, sEvidenceTypeName, sEvidenceNumber,
                sFindEvidenceZone, sMarking, sParceling, sEvidencePerformed);
        if (saveStatus <= 0) {
            Log.i("Recieve", "Error!! ");
        } else {
            listViewEvidences.setVisibility(View.VISIBLE);
            ShowSelectedFindEvidence(reportID);
        }

    }
    public void ShowSelectedFindEvidence(String sReportID) {
        // TODO Auto-generated method stub
        evidencesList = mDbHelper.SelectAllEvidences(sReportID);
        if (evidencesList != null) {
            setListViewHeightBasedOnItems(listViewEvidences);
            listViewEvidences.setVisibility(View.VISIBLE);
            listViewEvidences.setAdapter(new EvidencesAdapter(getActivity()));
        } else {
            listViewEvidences.setVisibility(View.GONE);
        }

    }

    public class EvidencesAdapter extends BaseAdapter {
        private Context context;

        public EvidencesAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return evidencesList.size();
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
                convertView = inflater.inflate(R.layout.list_evidences_data, null);

            }
            final String sFindEvidenceID = evidencesList.get(position).get(
                    "FindEvidenceID");
            final TextView txtEvidenceType = (TextView) convertView
                    .findViewById(R.id.txtEvidenceType);
            txtEvidenceType.setText(evidencesList.get(position).get(
                    "EvidenceTypeName"));
            final TextView txtEvidenceNumber = (TextView) convertView
                    .findViewById(R.id.txtEvidenceNumber);
            txtEvidenceNumber.setText(evidencesList.get(position).get(
                    "EvidenceNumber"));
            final TextView txtFindEvidenceZone = (TextView) convertView
                    .findViewById(R.id.txtFindEvidenceZone);
            txtFindEvidenceZone.setText(evidencesList.get(position).get(
                    "FindEvidenceZone"));
            final TextView txtMarking = (TextView) convertView
                    .findViewById(R.id.txtMarking);
            txtMarking.setText(evidencesList.get(position).get(
                    "Marking"));
            final TextView txtParceling = (TextView) convertView
                    .findViewById(R.id.txtParceling);
            txtParceling.setText(evidencesList.get(position).get(
                    "Parceling"));
            final TextView txtEvidencePerformed = (TextView) convertView
                    .findViewById(R.id.txtEvidencePerformed);
            txtEvidencePerformed.setText(evidencesList.get(position).get(
                    "EvidencePerformed"));
            /*
            final AutoCompleteTextView autoEvidenceType = (AutoCompleteTextView) convertView
                    .findViewById(R.id.autoEvidenceType);
            String[] mEvidenceTypeArray;
            mEvidenceTypeArray = getResources().getStringArray(
                    R.array.type_evidence);
            ArrayAdapter<String> adapterEvidenceType = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mEvidenceTypeArray);

            autoEvidenceType.setThreshold(1);
            autoEvidenceType.setAdapter(adapterEvidenceType);
            autoEvidenceType.setText(evidencesList.get(position).get(
                    "EvidenceTypeName"));

            final EditText editEvidenceNumber = (EditText) convertView
                    .findViewById(R.id.editEvidenceNumber);
            editEvidenceNumber.setText(evidencesList.get(position).get(
                    "EvidenceNumber"));
            final EditText editFindEvidenceZone = (EditText) convertView
                    .findViewById(R.id.editFindEvidenceZone);
            editFindEvidenceZone.setText(evidencesList.get(position).get(
                    "FindEvidenceZone"));
            final EditText editMarking = (EditText) convertView
                    .findViewById(R.id.editMarking);
            editMarking.setText(evidencesList.get(position).get("Marking"));
            final EditText editParceling = (EditText) convertView
                    .findViewById(R.id.editParceling);
            editParceling.setText(evidencesList.get(position).get("Parceling"));
            final EditText editEvidencePerformed = (EditText) convertView
                    .findViewById(R.id.editEvidencePerformed);
            editEvidencePerformed.setText(evidencesList.get(position).get(
                    "EvidencePerformed"));
*/
            txtPhoto = (TextView)convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView)convertView.findViewById(R.id.txtVideo);

            final String[][] arrDataPhoto2 = mDbHelper.SelectDataPhotoOfEachEvidence(reportID, sFindEvidenceID, "photo");

            if (arrDataPhoto2 != null) {
                Log.i("Photo_Evidence", sFindEvidenceID + " " + String.valueOf(arrDataPhoto2.length));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(arrDataPhoto2.length) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i("Recieve_Evidence", sFindEvidenceID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent showActivity = new Intent(getActivity(), EvidencesActivity.class);
                    showActivity.putExtra("sEvidencesID", sFindEvidenceID);
                    showActivity.putExtra("type", "update");
                    startActivity(showActivity);
/*
                    adbEdit.setTitle("ยืนยันการเเก้ไขข้อมูล");
                    adbEdit.setNegativeButton("Cancel", null);
                    adbEdit.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long saveStatus = mDbHelper
                                            .updateDataSelectedFindEvidence(
                                                    sFindEvidenceID,
                                                    autoEvidenceType.getText()
                                                            .toString(),
                                                    editEvidenceNumber
                                                            .getText()
                                                            .toString(),
                                                    editFindEvidenceZone
                                                            .getText()
                                                            .toString(),
                                                    editMarking.getText()
                                                            .toString(),
                                                    editParceling.getText()
                                                            .toString(),

                                                    editEvidencePerformed
                                                            .getText()
                                                            .toString());
                                    if (saveStatus <= 0) {
                                        Log.i("Recieve FindEvidence",
                                                "Error!! ");
                                    } else {
                                        Toast.makeText(getActivity(),
                                                "แก้ไขเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                    adbEdit.show();*/
                }
            });
            // imgDelete
            ImageButton imgDelete = (ImageButton) convertView
                    .findViewById(R.id.imgDelete);
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    getActivity());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adb.setTitle("ลบข้อมูล");
                    adb.setMessage("ยืนยันการลบข้อมูล ["
                            + evidencesList.get(position).get(
                            "EvidenceTypeName") + "]");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long flg = mDbHelper
                                            .DeleteSelectedEvidences(sFindEvidenceID);
                                    if (flg > 0) {
                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllEvidence(sFindEvidenceID);
                                        if (saveStatus2 <= 0) {
                                            Log.i("DeletePhotoOf Evidence", "Cannot delete!! ");

                                        } else {
                                            Toast.makeText(getActivity(),
                                                    "ลบข้อมูลเรียบรอยแล้ว",
                                                    Toast.LENGTH_LONG).show();
                                            ShowSelectedFindEvidence(reportID);
                                        }
                                    } else {
                                        Toast.makeText(getActivity(),
                                                "เกิดข้อผิดพลาด",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                            });
                    adb.show();
                }
            });

            return convertView;

        }

    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();
            Log.i("inside",String.valueOf(numberOfItems));
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                Log.i("inside getHeight", String.valueOf(item.getMeasuredHeight()));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.

            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            int totalHeight = totalItemsHeight + totalDividersHeight ;
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            //params.height = (int) (totalItemsHeight-(totalItemsHeight/1.5));
            params.height =totalHeight;
            Log.i("inside totalHeight",String.valueOf(totalHeight));
            //  Log.i("inside getDividerHeight", String.valueOf(totalItemsHeight) + " " + String.valueOf(totalItemsHeight - (totalItemsHeight / 1.5)));
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}