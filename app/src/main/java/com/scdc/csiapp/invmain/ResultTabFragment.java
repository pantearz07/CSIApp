package com.scdc.csiapp.invmain;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.apimodel.ApiStatusData;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.DateDialog;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.MainActivity;
import com.scdc.csiapp.main.SnackBarAlert;
import com.scdc.csiapp.main.TimeDialog;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbClueShown;
import com.scdc.csiapp.tablemodel.TbFindEvidence;
import com.scdc.csiapp.tablemodel.TbGatewayCriminal;
import com.scdc.csiapp.tablemodel.TbPropertyLoss;
import com.scdc.csiapp.tablemodel.TbResultScene;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by Pantearz07 on 22/9/2558.
 */
public class ResultTabFragment extends Fragment {
    FloatingActionButton fabBtn;
    private CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    DBHelper dbHelper;
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
    private ImageButton btn_clear_txt_24, btn_clear_txt_25, btn_clear_txt_26,
            btn_clear_txt_27, btn_clear_txt_28, btn_clear_txt_29, btn_clear_txt_30, btn_clear_txt_1;
    private String sMaleCriminalNum, sFemaleCriminalNum, sCriminalUsedWeapon, sPersonInvolvedDetail,
            sFullEvidencePerformed, sAnnotation, sCompleteSceneDate, sCompleteSceneTime;
    private EditText editCriminalUseWeapon,
            editPersonInvolvedDetail, editEvidencePerformed, editAnnotation;
    private Snackbar snackbar;
    private int iCriminalSumNum, iMaleCriminalNum, iFemaleCriminalNum = 0;
    String sCompleteSceneDate_New = "";
    private TextView showCriminalSumNum, editCompleteSceneDate, editCompleteSceneTime;
    private EditText editCriminalMaleNum, editCriminalFemaleNum;
    Button btnSubmitInvestigated;
    // sufferer confine
    private String sConfineSufferer;
    private EditText editConfineSufferer;

    private ViewGroup viewByIdadddialog;
    // gatewaycriminal : ทางเข้าออกของคนร้าย
    private ArrayList<HashMap<String, String>> gatewaycriminalList;
    private ListView listViewGatewayCriminal;
    private ImageButton btnAddGatewayCriminal;
    public static List<TbGatewayCriminal> tbGatewayCriminals = null;
    // clueshown : ร่องรอยที่ปรากฏ
    private ArrayList<HashMap<String, String>> clueShownList;
    private ListView listViewClueShown;
    private ImageButton btnAddClueShown;
    public static List<TbClueShown> tbClueShowns = null;
    public static List<TbResultScene> tbResultScenes = null;
    // property
    private ArrayList<HashMap<String, String>> propertylossList;
    private ListView listViewPropertyLoss;
    private ImageButton btnPropertyLoss;
    public static List<TbPropertyLoss> tbPropertyLosses = null;
    // btnAddEvidences
    private ArrayList<HashMap<String, String>> evidencesList;
    private ListView listViewEvidences;
    private ImageButton btnAddEvidences;
    public static List<TbFindEvidence> tbFindEvidences = null;
    String sRSID;
    TextView txtPhoto, txtVideo;

    private View mViewAddGatewayCriminal, mViewAddClueShown, mViewAddPropertyLoss, mViewAddEvidences;
    ImageButton btnShowHide1, btnShowHide2, btnShowHide3, btnShowHide4;
    private boolean viewGroupIsVisible = true;
    private static final String TAG = "DEBUG-ResultTabFragment";
    public static String Bundle_ID = "dataid";
    public static String Bundle_TB = "datatb";
    public static String Bundle_mode = "mode";
    public static String Bundle_Index = "position";
    public static String Bundle_RSType = "rstype";
    public static String Bundle_SceneInvestID = "SceneInvestID";
    AddFindEvidenceFragment addFindEvidenceFragment;
    AddPropertyLossFragment addPropertyLossFragment;
    AddGatewayFragment addGatewayFragment;
    AddClueShownFragment addClueShownFragment;

    public static String strSDCardPathName = "/CSIFiles/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewDetails = inflater.inflate(R.layout.result_tab_layout, null);
        mContext = viewDetails.getContext();
        rootLayout = (CoordinatorLayout) viewDetails.findViewById(R.id.rootLayoutCSI);
        mDbHelper = new SQLiteDBHelper(getActivity());
        dbHelper = new DBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        getDateTime = new GetDateTime();
        cd = new ConnectionDetector(getActivity());

        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();

        updateDT = getDateTime.updateDataDateTime();
        Log.i("page viewDetails", reportID);

        addFindEvidenceFragment = new AddFindEvidenceFragment();
        addPropertyLossFragment = new AddPropertyLossFragment();
        addGatewayFragment = new AddGatewayFragment();
        addClueShownFragment = new AddClueShownFragment();

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
//        btn_clear_txt_24 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_24);
        btn_clear_txt_25 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_25);
        btn_clear_txt_26 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_26);
        btn_clear_txt_27 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_27);
        btn_clear_txt_28 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_28);
        btn_clear_txt_29 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_29);
        btn_clear_txt_30 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_30);

        btnSubmitInvestigated = (Button) viewDetails.findViewById(R.id.btnSubmitInvestigated);
        edtUpdateDateTime3 = (TextView) viewDetails.findViewById(R.id.edtUpdateDateTime3);
        edtUpdateDateTime3.setText(getString(R.string.updatedata) + " "
                + getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate)
                + " เวลา " + getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime) + " น.");


// การดำเนินการเกี่ยวกับวัตถุพยาน
        editEvidencePerformed = (EditText) viewDetails
                .findViewById(R.id.editEvidencePerformed);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().FullEvidencePerformed == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().FullEvidencePerformed.equals("")) {
            editEvidencePerformed.setText("");
        } else {
            editEvidencePerformed.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().FullEvidencePerformed);
        }
        editEvidencePerformed.addTextChangedListener(new ResultTextWatcher(editEvidencePerformed));
        // หมายเหตุ
        editAnnotation = (EditText) viewDetails.findViewById(R.id.editAnnotation);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().Annotation == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().Annotation.equals("")) {
            editAnnotation.setText("");
        } else {
            editAnnotation.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().Annotation);
        }
        editAnnotation.addTextChangedListener(new ResultTextWatcher(editAnnotation));

        // ข้อมูลคนร้าย (กรณีชิงทรัพย์/ปล้นทรัพย์)
        // จำนวนคนร้าย
        editCriminalMaleNum = (EditText) viewDetails
                .findViewById(R.id.editCriminalAmountMale);
        editCriminalFemaleNum = (EditText) viewDetails
                .findViewById(R.id.editCriminalAmountFemale);
        showCriminalSumNum = (TextView) viewDetails
                .findViewById(R.id.txtCriminalAmount);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum.equals("")) {
            editCriminalMaleNum.setText("");
        } else {
            editCriminalMaleNum.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum);
            if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum.equals("")) {
                showCriminalSumNum.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum);
            } else {
                int malenum = 0, femalenum = 0, sumnum = 0;
                malenum = Integer.parseInt(CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum);
                femalenum = Integer.parseInt(CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum);
                sumnum = malenum + femalenum;
                showCriminalSumNum.setText(String.valueOf(sumnum));
            }
        }
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum.equals("")) {
            editCriminalFemaleNum.setText("");
        } else {
            editCriminalFemaleNum.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum);
            if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum.equals("")) {
                showCriminalSumNum.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum);
            } else {
                int malenum = 0, femalenum = 0, sumnum = 0;
                malenum = Integer.parseInt(CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum);
                femalenum = Integer.parseInt(CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum);
                sumnum = malenum + femalenum;
                showCriminalSumNum.setText(String.valueOf(sumnum));
            }
        }
        editCriminalMaleNum.addTextChangedListener(new ResultTextWatcher(editCriminalMaleNum));
        editCriminalFemaleNum.addTextChangedListener(new ResultTextWatcher(editCriminalFemaleNum));

        // มีการใช้อาวุธ
        editCriminalUseWeapon = (EditText) viewDetails
                .findViewById(R.id.editCriminalUseWeapon);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CriminalUsedWeapon == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CriminalUsedWeapon.equals("")) {
            editCriminalUseWeapon.setText("");
        } else {
            editCriminalUseWeapon.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CriminalUsedWeapon);
        }
        editCriminalUseWeapon.addTextChangedListener(new ResultTextWatcher(editCriminalUseWeapon));

        // การพันธนาการผู้เสียหาย
        editConfineSufferer = (EditText) viewDetails
                .findViewById(R.id.editConfineSufferer);

        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().ConfineSufferer == null || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ConfineSufferer.equals("")) {
            editConfineSufferer.setText("");
        } else {
            editConfineSufferer.setText(CSIDataTabFragment.apiCaseScene.getTbCaseScene().ConfineSufferer);
        }
        editConfineSufferer.addTextChangedListener(new ResultTextWatcher(editConfineSufferer));
        //วันเวลาตรวจเสร็จ
        String[] currentDT = getDateTime.updateDataDateTime();
        editCompleteSceneDate = (TextView) viewDetails
                .findViewById(R.id.editCompleteSceneDate);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate == null
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("0000-00-00")) {

            editCompleteSceneDate.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate = null;

        } else {
            editCompleteSceneDate.setText(getDateTime.changeDateFormatToCalendar(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate));
        }
        editCompleteSceneDate.setOnClickListener(new ResultOnClickListener());

        editCompleteSceneTime = (TextView) viewDetails
                .findViewById(R.id.editCompleteSceneTime);
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime == null
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime.equals("")
                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime.equals("00:00:00")) {

            editCompleteSceneTime.setText("");
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime = null;
        } else {
            editCompleteSceneTime.setText(getDateTime.changeTimeFormatToDB(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime));
        }
        editCompleteSceneTime.setOnClickListener(new ResultOnClickListener());
        btn_clear_txt_1 = (ImageButton) viewDetails.findViewById(R.id.btn_clear_txt_1);
        btn_clear_txt_1.setOnClickListener(new ResultOnClickListener());
// ทางเข้า-ออกคนร้าย

//        tbResultScenes = new ArrayList<>();
        if (CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals() == null) {
            tbGatewayCriminals = new ArrayList<>();
            Log.i(TAG, "getTbGatewayCriminals null");
        } else {
            tbGatewayCriminals = CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals();
            Log.i(TAG, "getTbGatewayCriminals not null");

        }

        listViewGatewayCriminal = (ListView) viewDetails
                .findViewById(R.id.listViewAddGatewayCriminal);
        listViewGatewayCriminal.setVisibility(View.GONE);
        listViewGatewayCriminal.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedGatewayCriminal();
        btnAddGatewayCriminal = (ImageButton) viewDetails.findViewById(R.id.btnAddGatewayCriminal);
        btnAddGatewayCriminal.setOnClickListener(new ResultOnClickListener());
// ร่องรอยที่ปรากฏ ตรวจพบร่องรอยการรื้อค้น/งัดแงะบริเวณ clueshown
        if (CSIDataTabFragment.apiCaseScene.getTbClueShowns() == null) {
            tbClueShowns = new ArrayList<>();

            Log.i(TAG, "getTbClueShowns null");
        } else {
            tbClueShowns = CSIDataTabFragment.apiCaseScene.getTbClueShowns();
            Log.i(TAG, "getTbClueShowns not null");

        }
        listViewClueShown = (ListView) viewDetails
                .findViewById(R.id.listViewAddClueShown);
        listViewClueShown.setVisibility(View.GONE);
        listViewClueShown.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedClueShown();
        btnAddClueShown = (ImageButton) viewDetails.findViewById(R.id.btnAddClueShown);
        btnAddClueShown.setOnClickListener(new ResultOnClickListener());

// รายการทรัพย์สินที่คนร้ายโจรกรรม
        if (CSIDataTabFragment.apiCaseScene.getTbPropertyLosses() == null) {
            tbPropertyLosses = new ArrayList<>();
            Log.i(TAG, "getTbPropertyLosses null");
        } else {
            tbPropertyLosses = CSIDataTabFragment.apiCaseScene.getTbPropertyLosses();
            Log.i(TAG, "getTbPropertyLosses not null");
        }
        listViewPropertyLoss = (ListView) viewDetails
                .findViewById(R.id.listViewPropertyLoss);
        listViewPropertyLoss.setVisibility(View.GONE);
        listViewPropertyLoss.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedPropertyloss();
        btnPropertyLoss = (ImageButton) viewDetails.findViewById(R.id.btnPropertyLoss);
        btnPropertyLoss.setOnClickListener(new ResultOnClickListener());


        // ตรวจเก็บลายนิ้วมือแฝง และฝามือแฝง
        if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences() == null) {
            tbFindEvidences = new ArrayList<>();
            Log.i(TAG, "getTbFindEvidences null");
        } else {
            tbFindEvidences = CSIDataTabFragment.apiCaseScene.getTbFindEvidences();
            Log.i(TAG, "getTbFindEvidences not null  " + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));
        }
        listViewEvidences = (ListView) viewDetails
                .findViewById(R.id.listViewEvidences);
        listViewEvidences.setVisibility(View.GONE);
        listViewEvidences.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowSelectedFindEvidence();
        btnAddEvidences = (ImageButton) viewDetails.findViewById(R.id.btnAddEvidences);
        btnAddEvidences.setOnClickListener(new ResultOnClickListener());


        fabBtn = (FloatingActionButton) viewDetails.findViewById(R.id.fabBtnResult);
        fabBtn.setOnClickListener(new ResultOnClickListener());

        if (CSIDataTabFragment.mode == "view") {
            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtn.setLayoutParams(p);
            fabBtn.hide();
            btnAddEvidences.setVisibility(View.GONE);
            btnPropertyLoss.setVisibility(View.GONE);
            btnAddClueShown.setVisibility(View.GONE);
            btnAddGatewayCriminal.setVisibility(View.GONE);
            editCompleteSceneDate.setEnabled(false);
            editCompleteSceneTime.setEnabled(false);
            editCriminalMaleNum.setEnabled(false);
            editCriminalFemaleNum.setEnabled(false);
            editCriminalUseWeapon.setEnabled(false);
            editAnnotation.setEnabled(false);
            editConfineSufferer.setEnabled(false);
            editEvidencePerformed.setEnabled(false);
            btn_clear_txt_25.setEnabled(false);
            btn_clear_txt_26.setEnabled(false);
            btn_clear_txt_27.setEnabled(false);
            btn_clear_txt_28.setEnabled(false);
            btn_clear_txt_29.setEnabled(false);
            btn_clear_txt_30.setEnabled(false);
            btn_clear_txt_1.setVisibility(View.GONE);
            btnSubmitInvestigated.setVisibility(View.GONE);
        }
//        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus.equals(R.string.casestatus_6)
//                || CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus.equals(R.string.casestatus_7)) {
//            btnSubmitInvestigated.setVisibility(View.GONE);
//        }
//        btnSubmitInvestigated.setOnClickListener(new ResultOnClickListener());
        btnSubmitInvestigated.setVisibility(View.GONE);
        return viewDetails;
    }

    public static List<TbGatewayCriminal> cloneList(List<TbGatewayCriminal> tbResultScenesList) {
        List<TbGatewayCriminal> cloneList = new ArrayList<TbGatewayCriminal>(tbResultScenesList.size());
        for (TbGatewayCriminal tbGatewayCriminal : tbResultScenesList) {
            cloneList.add(new TbGatewayCriminal());
        }
        return cloneList;
    }

    private void updateData() {
        final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
        CSIDataTabFragment.apiCaseScene.getTbNoticeCase().LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
        if (editCompleteSceneDate.getText().toString() == null || editCompleteSceneDate.getText().toString().equals("")) {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate = null;
        } else {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate = getDateTime.changeDateFormatToDB(editCompleteSceneDate.getText().toString());
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CompleteSceneDate = getDateTime.changeDateFormatToDB(editCompleteSceneDate.getText().toString());
        }
        if (editCompleteSceneTime.getText().toString() == null || editCompleteSceneTime.getText().toString().equals("")) {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime = null;
        } else {
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneTime = editCompleteSceneTime.getText().toString();
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CompleteSceneTime = editCompleteSceneTime.getText().toString();
        }
    }

    private void savedata() {
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate == null ||
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("0000-00-00") ||
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("")) {
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "investigating";
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "investigating";
        } else {
            CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "investigated";
            CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "investigated";
        }
        if (CSIDataTabFragment.apiCaseScene.getTbCaseScene() != null) {
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                if (snackbar == null || !snackbar.isShown()) {
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                            getString(R.string.save_complete)
                                    + "\n" + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateDate
                                    + " " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().LastUpdateTime);
                    snackBarAlert.createSnacbar();
                }
            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                            getString(R.string.save_error));
                    snackBarAlert.createSnacbar();
                }
            }
        }
    }

    public class ResultOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v == fabBtn) {
                hiddenKeyboard();
                updateData();
                savedata();
            }
            if (v == btnSubmitInvestigated) {
                hiddenKeyboard();
                updateData();
                if (CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate == null ||
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("0000-00-00") ||
                        CSIDataTabFragment.apiCaseScene.getTbCaseScene().CompleteSceneDate.equals("")) {

                    if (snackbar == null || !snackbar.isShown()) {

                        SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_LONG,
                                "ยังไม่ระบุวันเวลาที่ตรวจเสร็จ");
                        snackBarAlert.createSnacbar();
                    }
                } else {
                    CSIDataTabFragment.apiCaseScene.getTbNoticeCase().CaseStatus = "investigated";
                    CSIDataTabFragment.apiCaseScene.getTbCaseScene().ReportStatus = "investigated";
                    SaveCaseReport statusCase = new SaveCaseReport();
                    statusCase.execute(CSIDataTabFragment.apiCaseScene);
                }
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
            if (v == btn_clear_txt_1) {
                editCompleteSceneDate.setText("");
                editCompleteSceneTime.setText("");
            }
            if (v == btnAddGatewayCriminal) {
                hiddenKeyboard();
//                Log.i(TAG, "btnAddGatewayCriminal");
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                String sRSID = "GC_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                Bundle i = new Bundle();
                i.putString(Bundle_ID, sRSID);
                i.putString(Bundle_mode, "new");
                i.putString(Bundle_RSType, "GC");
                addGatewayFragment.setArguments(i);
                MainActivity.setFragment(addGatewayFragment, 1);
            }
            if (v == btnAddClueShown) {
                hiddenKeyboard();
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                String sRSID = "CS_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                Bundle i = new Bundle();
                i.putString(Bundle_ID, sRSID);
                i.putString(Bundle_mode, "new");
                i.putString(Bundle_RSType, "CS");
                addClueShownFragment.setArguments(i);
                MainActivity.setFragment(addClueShownFragment, 1);
            }
            if (v == btnPropertyLoss) {
                hiddenKeyboard();
//                Log.i(TAG, "btnPropertyLoss");
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                String sPLID = "PL_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                Bundle i = new Bundle();
                i.putString(Bundle_ID, sPLID);
                i.putString(Bundle_mode, "new");
                addPropertyLossFragment.setArguments(i);
                MainActivity.setFragment(addPropertyLossFragment, 1);
            }
            if (v == btnAddEvidences) {
                hiddenKeyboard();
                String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                sRSID = "EV_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
                int sceneinvestsize = 0;
                String sSceneInvestID = null;
                if (CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations() != null) {
                    sceneinvestsize = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().size();
                    if (sceneinvestsize == 0) {
                        if (snackbar == null || !snackbar.isShown()) {
                            SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                                    "กรุณาระบุวันเวลาออกตรวจสถานที่เกิดเหตุ");
                            snackBarAlert.createSnacbar();
                        }
                    } else {

                        sSceneInvestID = CSIDataTabFragment.apiCaseScene.getTbSceneInvestigations().get(sceneinvestsize - 1).getSceneInvestID();
                        Log.i(TAG, "sSceneInvestID " + sSceneInvestID);
                        Bundle i = new Bundle();
                        i.putString(Bundle_ID, sRSID);
                        i.putString(Bundle_mode, "new");
                        i.putString(Bundle_SceneInvestID, sSceneInvestID);
                        addFindEvidenceFragment.setArguments(i);
                        MainActivity.setFragment(addFindEvidenceFragment, 1);
                    }
                } else {
                    if (snackbar == null || !snackbar.isShown()) {
                        SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                                "กรุณาระบุวันเวลาออกตรวจสถานที่เกิดเหตุ");
                        snackBarAlert.createSnacbar();

                    }
                }
            }
            if (v == btnShowHide1) {
                if (viewGroupIsVisible) {
                    mViewAddGatewayCriminal.setVisibility(View.VISIBLE);
                    btnShowHide1.setImageResource(R.drawable.ic_expand_up);
                } else {
                    mViewAddGatewayCriminal.setVisibility(View.GONE);
                    btnShowHide1.setImageResource(R.drawable.ic_expand_down);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if (v == btnShowHide2) {
                if (viewGroupIsVisible) {
                    mViewAddClueShown.setVisibility(View.VISIBLE);
                    btnShowHide2.setImageResource(R.drawable.ic_expand_up);
                } else {
                    mViewAddClueShown.setVisibility(View.GONE);
                    btnShowHide2.setImageResource(R.drawable.ic_expand_down);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if (v == btnShowHide3) {
                if (viewGroupIsVisible) {
                    mViewAddPropertyLoss.setVisibility(View.VISIBLE);
                    btnShowHide3.setImageResource(R.drawable.ic_expand_up);
                } else {
                    mViewAddPropertyLoss.setVisibility(View.GONE);
                    btnShowHide3.setImageResource(R.drawable.ic_expand_down);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if (v == btnShowHide4) {
                if (viewGroupIsVisible) {
                    mViewAddEvidences.setVisibility(View.VISIBLE);
                    btnShowHide4.setImageResource(R.drawable.ic_expand_up);
                } else {
                    mViewAddEvidences.setVisibility(View.GONE);
                    btnShowHide4.setImageResource(R.drawable.ic_expand_down);
                }
                viewGroupIsVisible = !viewGroupIsVisible;
            }
            if (v == editCompleteSceneDate) {
                Log.i("ClickCompleteSceneDate", "null");
                DateDialog dialogCompleteSceneDate = new DateDialog(v);
                dialogCompleteSceneDate.show(getActivity().getFragmentManager(), "Date Picker");

            }
            if (v == editCompleteSceneTime) {
                Log.i("ClickCompleteSceneTime", "null");
                TimeDialog dialogCompleteSceneTime = new TimeDialog(v);
                dialogCompleteSceneTime.show(getActivity().getFragmentManager(), "Time Picker");
            }
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

    public void onStart() {
        super.onStart();
        Log.i("Check", "onStart Result");


        ShowSelectedGatewayCriminal();
        ShowSelectedClueShown();
        ShowSelectedPropertyloss();
        ShowSelectedFindEvidence();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause Result");
        updateData();
        savedata();
        hiddenKeyboard();
    }


    public void ShowSelectedGatewayCriminal() {
        // TODO Auto-generated method stub


        if (CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals() != null) {
            setListViewHeightBasedOnItems(listViewGatewayCriminal);
            Log.i(TAG, "show getTbGatewayCriminals" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));
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
            return CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size();
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

            final String sRSTypeID = CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().get(position).getRSTypeID();
            final String sRSID = CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().get(position).getRSID();
            final String sRSDetail = CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().get(position).getRSDetail();
            Log.i("sRSDetail", sRSID + " " + sRSTypeID + " " + sRSDetail);

            // ColCode
            TextView txtGatewayCriminalDetails = (TextView) convertView.findViewById(R.id.txtDetails);
            txtGatewayCriminalDetails.setText(String.valueOf(position + 1) + ") " + CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().get(position).getRSDetail());

            txtPhoto = (TextView) convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView) convertView.findViewById(R.id.txtVideo);
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                if (cd.isNetworkAvailable()) {
                    ApiMultimedia apiMultimedia = new ApiMultimedia();
                    for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene() != null) {
                                if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().RSID.equals(sRSID)) {
                                    apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                    apiMultimedia.setTbPhotoOfResultscene(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene());
                                    apiMultimediaList.add(apiMultimedia);
                                }
                            }
                        }
                    }
                    Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
                } else {
                    apiMultimediaList = dbHelper.SelectDataPhotoOfResultscene(sRSID, "photo");
                }
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfResultscene(sRSID, "photo");
                Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            }
            txtVideo.setVisibility(View.GONE);
            if (apiMultimediaList != null) {
                Log.i(TAG, "apiMultimediaList GatewayCriminals " + sRSID + " " + String.valueOf(apiMultimediaList.size()));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(apiMultimediaList.size()) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i(TAG, "apiMultimediaList GatewayCriminals " + sRSID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, " sRSID " + sRSID);
                    Log.i(TAG, " sRSTypeID " + sRSTypeID);
                    final TbGatewayCriminal tbGatewayCriminal = CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().get(position);
                    Bundle i = new Bundle();
                    i.putString(Bundle_ID, sRSID);
                    i.putString(Bundle_mode, "edit");
                    i.putInt(Bundle_Index, position);
                    i.putString(Bundle_RSType, sRSTypeID);
                    i.putSerializable(Bundle_TB, tbGatewayCriminal);
                    addGatewayFragment.setArguments(i);
                    MainActivity.setFragment(addGatewayFragment, 1);
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
                    adb.setNegativeButton(R.string.cancel, null);
                    adb.setPositiveButton(R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {


                                    CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().remove(position);
                                    long flg = dbHelper.DeleteSelectedData("resultscene", "RSID", sRSID);
//
                                    if (flg > 0) {
                                        Log.i(TAG, "resultscene getTbGatewayCriminals" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbGatewayCriminals().size()));
                                        ShowSelectedGatewayCriminal();
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.delete_complete)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedGatewayCriminal();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
//                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllResultScene(sRSID);
//                                        if (saveStatus2 <= 0) {
//                                            Log.i("DeletePhotoOf gateway", "Cannot delete!! ");
//
//                                        } else {
//                                            Toast.makeText(getActivity(),
//                                                    "ลบข้อมูลเรียบรอยแล้ว",
//                                                    Toast.LENGTH_LONG).show();
////                                            ShowSelectedGatewayCriminal(reportID,
////                                                    sRSTypeID);
//                                        }


                                    } else {
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedGatewayCriminal();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    }
                                }

                            });
                    adb.show();
                }
            });
            if (CSIDataTabFragment.mode == "view") {
                imgDelete.setVisibility(View.GONE);
            }
            return convertView;

        }

    }

    public void ShowSelectedClueShown() {
        // TODO Auto-generated method stub
//        Log.i("show sRSTypeID", sReportID + " " + sRSTypeID);
//        clueShownList = mDbHelper.SelectAllResultScene(sReportID, sRSTypeID);


        if (CSIDataTabFragment.apiCaseScene.getTbClueShowns() != null) {
            setListViewHeightBasedOnItems(listViewClueShown);
            Log.i(TAG, "show getTbClueShowns " + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));
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
            return CSIDataTabFragment.apiCaseScene.getTbClueShowns().size();
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

            final String sRSTypeID = CSIDataTabFragment.apiCaseScene.getTbClueShowns().get(position).getRSTypeID();
            final String sRSID = CSIDataTabFragment.apiCaseScene.getTbClueShowns().get(position).getRSID();
            final String sRSDetail = CSIDataTabFragment.apiCaseScene.getTbClueShowns().get(position).getRSDetail();
            Log.i("sRSDetail", sRSID + " " + sRSTypeID + " " + sRSDetail);
            TextView txtDetails = (TextView) convertView.findViewById(R.id.txtDetails);
            txtDetails.setText(String.valueOf(position + 1) + ") " + CSIDataTabFragment.apiCaseScene.getTbClueShowns().get(position).getRSDetail());

            txtPhoto = (TextView) convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView) convertView.findViewById(R.id.txtVideo);
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                if (cd.isNetworkAvailable()) {
                    ApiMultimedia apiMultimedia = new ApiMultimedia();
                    for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene() != null) {
                                if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene().RSID.equals(sRSID)) {
                                    apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                    apiMultimedia.setTbPhotoOfResultscene(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfResultscene());
                                    apiMultimediaList.add(apiMultimedia);
                                }
                            }
                        }
                    }
                    Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
                } else {
                    apiMultimediaList = dbHelper.SelectDataPhotoOfResultscene(sRSID, "photo");
                }
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfResultscene(sRSID, "photo");
                Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            }

            txtVideo.setVisibility(View.GONE);
            if (apiMultimediaList != null) {
                Log.i(TAG, "apiMultimediaList ClueShown " + sRSID + " " + String.valueOf(apiMultimediaList.size()));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(apiMultimediaList.size()) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i(TAG, "apiMultimediaList ClueShown " + sRSID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            //final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
            //       getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, " sRSID " + sRSID);
                    Log.i(TAG, " sRSTypeID " + sRSTypeID);
                    final TbClueShown tbClueShown = CSIDataTabFragment.apiCaseScene.getTbClueShowns().get(position);
                    Bundle i = new Bundle();
                    i.putString(Bundle_ID, sRSID);
                    i.putString(Bundle_mode, "edit");
                    i.putString(Bundle_RSType, sRSTypeID);
                    i.putInt(Bundle_Index, position);
                    i.putSerializable(Bundle_TB, tbClueShown);
                    addClueShownFragment.setArguments(i);
                    MainActivity.setFragment(addClueShownFragment, 1);
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
                    adb.setNegativeButton(R.string.cancel, null);
                    adb.setPositiveButton(R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    CSIDataTabFragment.apiCaseScene.getTbClueShowns().remove(position);
                                    long flg = dbHelper.DeleteSelectedData("resultscene", "RSID", sRSID);
//
                                    if (flg > 0) {
                                        Log.i(TAG, "resultscene getTbClueShowns" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbClueShowns().size()));
                                        ShowSelectedClueShown();
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.delete_complete)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedClueShown();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    } else {
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedClueShown();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    }

                                }

                            });
                    adb.show();
                }
            });
            if (CSIDataTabFragment.mode == "view") {
                imgDelete.setVisibility(View.GONE);
            }
            return convertView;

        }

    }


    public void ShowSelectedPropertyloss() {
        // TODO Auto-generated method stub
//        propertylossList = mDbHelper.SelectAllPropertyloss(sREPORTID);

        if (CSIDataTabFragment.apiCaseScene.getTbPropertyLosses() != null) {
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
            return CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().size();
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
            final String sPropertyLossID = CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossID();
            final String sCaseReportID = CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getCaseReportID();


            final TextView txtPropertyLossName = (TextView) convertView
                    .findViewById(R.id.txtPropertyLossName);
            final TextView txtPropertyLossPosition = (TextView) convertView
                    .findViewById(R.id.txtPropertyLossPosition);
            final TextView txtPropertyInsurance = (TextView) convertView
                    .findViewById(R.id.txtPropertyInsurance);
            final TextView txtPropertyLossAmount = (TextView) convertView
                    .findViewById(R.id.txtPropertyLossAmount);
            txtPropertyLossName.setText(String.valueOf(position + 1) + ") " + CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossName());

            if (CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossNumber() == null
                    || CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossNumber().equals("")) {
                txtPropertyLossAmount.setVisibility(View.GONE);
            } else {
                txtPropertyLossAmount.setText("จำนวน " + CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossNumber()
                        + " " + CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossUnit());
            }

            if (CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossPosition() == null
                    || CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossPosition().equals("")) {
                txtPropertyLossPosition.setVisibility(View.GONE);
            } else {
                txtPropertyLossPosition.setText("บริเวณ: " + CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropertyLossPosition());
            }

            if (CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropInsurance() == null
                    || CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropInsurance().equals("")) {
                txtPropertyInsurance.setVisibility(View.GONE);
            } else {
                txtPropertyInsurance.setText("การประกันทรัพย์สิน: " + CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position).getPropInsurance());
            }
            txtPhoto = (TextView) convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView) convertView.findViewById(R.id.txtVideo);
            txtVideo.setVisibility(View.GONE);
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                if (cd.isNetworkAvailable()) {
                    ApiMultimedia apiMultimedia = new ApiMultimedia();
                    for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless() != null) {
                                if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless().PropertyLessID.equals(sPropertyLossID)) {
                                    apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                    apiMultimedia.setTbPhotoOfPropertyless(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfPropertyless());
                                    apiMultimediaList.add(apiMultimedia);
                                }
                            }
                        }
                    }
                    Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
                } else {
                    apiMultimediaList = dbHelper.SelectDataPhotoOfPropertyLoss(sPropertyLossID, "photo");
                }
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfPropertyLoss(sPropertyLossID, "photo");
                Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            }
            if (apiMultimediaList != null) {
                Log.i(TAG, "apiMultimediaList propertyloss " + sPropertyLossID + " " + String.valueOf(apiMultimediaList.size()));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(apiMultimediaList.size()) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i(TAG, "apiMultimediaList propertyloss " + sPropertyLossID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.i(TAG, " sPropertyLossID " + sPropertyLossID);
                    Log.i(TAG, " sCaseReportID " + sCaseReportID);
                    final TbPropertyLoss tbPropertyLoss = CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().get(position);
                    Bundle i = new Bundle();
                    i.putString(Bundle_ID, sPropertyLossID);
                    i.putString(Bundle_mode, "edit");
                    i.putInt(Bundle_Index, position);
                    i.putSerializable(Bundle_TB, tbPropertyLoss);
                    addPropertyLossFragment.setArguments(i);
                    MainActivity.setFragment(addPropertyLossFragment, 1);

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
                    adb.setNegativeButton(R.string.cancel, null);
                    adb.setPositiveButton(R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().remove(position);
                                    long flg = dbHelper.DeleteSelectedData("propertyloss", "PropertyLossID", sPropertyLossID);
//
                                    if (flg > 0) {
                                        Log.i(TAG, "propertyloss " + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbPropertyLosses().size()));
                                        ShowSelectedPropertyloss();
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.delete_complete)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedPropertyloss();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
//                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllPropertyLoss(sPropertyLossID);
//                                        if (saveStatus2 <= 0) {
//                                            Log.i("DeletePhoto Property", "Cannot delete!! ");
//
//                                        } else {
//                                            Toast.makeText(getActivity(),
//                                                    "ลบข้อมูลเรียบรอยแล้ว",
//                                                    Toast.LENGTH_LONG).show();
//                                            ShowSelectedPropertyloss(reportID);
//                                        }


                                    } else {
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedPropertyloss();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    }

                                }

                            });
                    adb.show();
                }
            });
            if (CSIDataTabFragment.mode == "view") {
                imgDelete.setVisibility(View.GONE);
            }
            return convertView;

        }

    }

    public void ShowSelectedFindEvidence() {
        // TODO Auto-generated method stub
//        evidencesList = mDbHelper.SelectAllEvidences(sReportID);
        if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences() != null) {
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
            return CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size();
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
                convertView = inflater.inflate(R.layout.list_evidences, null);

            }
            final String sFindEvidenceID = CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getFindEvidenceID();
            final String sCaseReportID = CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getCaseReportID();
            final String sSceneInvestID = CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getSceneInvestID();
            final TextView txtEvidenceType = (TextView) convertView
                    .findViewById(R.id.txtEvidenceType);
            if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidenceTypeID() != null) {
                Log.i(TAG, "getEvidenceTypeID" + CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidenceTypeID());
                String[][] evidenceTypeArray = dbHelper.SelectAllEvidenceType();
                if (evidenceTypeArray != null) {

                    for (int i = 0; i < evidenceTypeArray.length; i++) {
                        if (String.valueOf(evidenceTypeArray[i][0]).equals(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).EvidenceTypeID)) {
                            if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).FindEvidencecol == null || CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).FindEvidencecol.equals("")) {
                                txtEvidenceType.setText(String.valueOf(position + 1) + ") " + String.valueOf(evidenceTypeArray[i][2]));
                            } else {
                                txtEvidenceType.setText(String.valueOf(position + 1) + ") " + String.valueOf(evidenceTypeArray[i][2]) + " " + CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).FindEvidencecol);

                            }
                            break;
                        }
                    }


                } else {
                    Log.i(TAG + " show evidenceTypeArray", "null");
                }
            }

            final TextView txtEvidenceNumber = (TextView) convertView
                    .findViewById(R.id.txtEvidenceNumber);
            final TextView txtFindEvidenceZone = (TextView) convertView
                    .findViewById(R.id.txtFindEvidenceZone);
            final TextView txtMarking = (TextView) convertView
                    .findViewById(R.id.txtMarking);
            final TextView txtParceling = (TextView) convertView
                    .findViewById(R.id.txtParceling);
            final TextView txtEvidencePerformed = (TextView) convertView
                    .findViewById(R.id.txtEvidencePerformed);
            if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidenceNumber() == null
                    || CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidenceNumber().equals("")) {
                txtEvidenceNumber.setVisibility(View.GONE);
            } else {
                txtEvidenceNumber.setText("พบจำนวน " + CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidenceNumber() + "แผ่น");

            }
            if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getFindEvidenceZone() == null
                    || CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getFindEvidenceZone().equals("")) {
                txtFindEvidenceZone.setVisibility(View.GONE);
            } else {
                txtFindEvidenceZone.setText("บริเวณที่พบ: " + CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getFindEvidenceZone());

            }
            if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getMarking() == null
                    || CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getMarking().equals("")) {
                txtMarking.setVisibility(View.GONE);
            } else {
                txtMarking.setText("การทำเครื่องหมาย: " + CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getMarking());

            }
            if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getParceling() == null
                    || CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getParceling().equals("")) {
                txtParceling.setVisibility(View.GONE);
            } else {
                txtParceling.setText("การบรรจุหีบห่อ: " + CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getParceling());

            }
            if (CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidencePerformed() == null
                    || CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidencePerformed().equals("")) {
                txtEvidencePerformed.setVisibility(View.GONE);
            } else {
                txtEvidencePerformed.setText(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position).getEvidencePerformed());
            }

            txtPhoto = (TextView) convertView.findViewById(R.id.txtPhoto);
            txtVideo = (TextView) convertView.findViewById(R.id.txtVideo);
            txtVideo.setVisibility(View.GONE);
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                if (cd.isNetworkAvailable()) {
                    ApiMultimedia apiMultimedia = new ApiMultimedia();
                    for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence() != null) {
                                if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence().FindEvidenceID.equals(sFindEvidenceID)) {
                                    apiMultimedia.setTbMultimediaFile(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                                    apiMultimedia.setTbPhotoOfEvidence(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbPhotoOfEvidence());
                                    apiMultimediaList.add(apiMultimedia);
                                }
                            }
                        }
                    }
                    Log.i(TAG, "apiMultimediaList " + String.valueOf(apiMultimediaList.size()));
                } else {
                    apiMultimediaList = dbHelper.SelectDataPhotoOfEvidence(sFindEvidenceID, "photo");
                }
            } else {
                apiMultimediaList = dbHelper.SelectDataPhotoOfEvidence(sFindEvidenceID, "photo");
                Log.i(TAG, "apiMultimediaList offline " + String.valueOf(apiMultimediaList.size()));
            }
            if (apiMultimediaList != null) {
                Log.i(TAG, "apiMultimediaList Evidence " + sFindEvidenceID + " " + String.valueOf(apiMultimediaList.size()));
                txtPhoto.setText("รูปภาพ  (" + String.valueOf(apiMultimediaList.size()) + ")");

            } else {

                txtPhoto.setText("รูปภาพ (0)");

                Log.i(TAG, "apiMultimediaList Evidence " + sFindEvidenceID + " Null!! ");

            }
            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.i(TAG, " sEvidencesID " + sFindEvidenceID);
                    Log.i(TAG, " sCaseReportID " + sCaseReportID);
                    final TbFindEvidence tbFindEvidence = CSIDataTabFragment.apiCaseScene.getTbFindEvidences().get(position);
                    Bundle i = new Bundle();
                    i.putString(Bundle_ID, sFindEvidenceID);
                    i.putString(Bundle_mode, "edit");
                    i.putInt(Bundle_Index, position);
                    i.putSerializable(Bundle_TB, tbFindEvidence);
                    addFindEvidenceFragment.setArguments(i);
                    MainActivity.setFragment(addFindEvidenceFragment, 1);
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
                    adb.setNegativeButton(R.string.cancel, null);
                    adb.setPositiveButton(R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    CSIDataTabFragment.apiCaseScene.getTbFindEvidences().remove(position);
                                    long flg = dbHelper.DeleteSelectedData("findevidence", "FindEvidenceID", sFindEvidenceID);

                                    if (flg > 0) {
                                        Log.i(TAG, "findevidence " + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbFindEvidences().size()));
                                        ShowSelectedFindEvidence();
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.delete_complete)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedFindEvidence();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
//                                        long saveStatus2 = mDbHelper.DeletePhotoOfAllEvidence(sFindEvidenceID);
//                                        if (saveStatus2 <= 0) {
//                                            Log.i("DeletePhotoOf Evidence", "Cannot delete!! ");
//
//                                        } else {
//                                            Toast.makeText(getActivity(),
//                                                    "ลบข้อมูลเรียบรอยแล้ว",
//                                                    Toast.LENGTH_LONG).show();
//                                            ShowSelectedFindEvidence();
//                                        }
                                    } else {
                                        if (snackbar == null || !snackbar.isShown()) {
                                            snackbar = Snackbar.make(rootLayout, getString(R.string.save_error)
                                                    , Snackbar.LENGTH_INDEFINITE)
                                                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            ShowSelectedFindEvidence();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    }

                                }

                            });
                    adb.show();
                }
            });
            if (CSIDataTabFragment.mode == "view") {
                imgDelete.setVisibility(View.GONE);
            }
            return convertView;

        }

    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();
            Log.i("inside", String.valueOf(numberOfItems));
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                Log.i("inside getHeight ", String.valueOf(itemPos) + " - " + String.valueOf(item.getMeasuredHeight()));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.

            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            int totalHeight = totalItemsHeight + totalDividersHeight;
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            //params.height = (int) (totalItemsHeight-(totalItemsHeight/1.5));
            params.height = totalHeight;
            Log.i("inside totalHeight", String.valueOf(totalHeight));
            //  Log.i("inside getDividerHeight", String.valueOf(totalItemsHeight) + " " + String.valueOf(totalItemsHeight - (totalItemsHeight / 1.5)));
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    private class ResultTextWatcher implements TextWatcher {
        private EditText mEditText;

        public ResultTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText == editEvidencePerformed) {
                btn_clear_txt_25.setVisibility(View.VISIBLE);
                btn_clear_txt_25.setOnClickListener(new ResultOnClickListener());
            }
            if (mEditText == editAnnotation) {
                btn_clear_txt_26.setVisibility(View.VISIBLE);
                btn_clear_txt_26.setOnClickListener(new ResultOnClickListener());
            }
            if (mEditText == editCriminalMaleNum) {
                btn_clear_txt_27.setVisibility(View.VISIBLE);
                btn_clear_txt_27.setOnClickListener(new ResultOnClickListener());

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
            if (mEditText == editCriminalFemaleNum) {
                btn_clear_txt_28.setVisibility(View.VISIBLE);
                btn_clear_txt_28.setOnClickListener(new ResultOnClickListener());

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
            if (mEditText == editCriminalUseWeapon) {
                btn_clear_txt_29.setVisibility(View.VISIBLE);
                btn_clear_txt_29.setOnClickListener(new ResultOnClickListener());

            }
            if (mEditText == editConfineSufferer) {
                btn_clear_txt_30.setVisibility(View.VISIBLE);
                btn_clear_txt_30.setOnClickListener(new ResultOnClickListener());

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s == editEvidencePerformed.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().FullEvidencePerformed = s.toString();
                Log.i(TAG, "FullEvidencePerformed " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().FullEvidencePerformed);
            }
            if (s == editAnnotation.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().Annotation = s.toString();
                Log.i(TAG, "Annotation " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().Annotation);
            }
            if (s == editCriminalMaleNum.getEditableText()) {
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

                CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum = s.toString();
                Log.i(TAG, "MaleCriminalNum " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().MaleCriminalNum);
            }
            if (s == editCriminalFemaleNum.getEditableText()) {
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

                CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum = s.toString();
                Log.i(TAG, "FemaleCriminalNum " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().FemaleCriminalNum);
            }
            if (s == editCriminalUseWeapon.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().CriminalUsedWeapon = s.toString();
                Log.i(TAG, "CriminalUsedWeapon " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CriminalUsedWeapon);
            }
            if (s == editConfineSufferer.getEditableText()) {
                CSIDataTabFragment.apiCaseScene.getTbCaseScene().ConfineSufferer = s.toString();
                Log.i(TAG, "ConfineSufferer " + CSIDataTabFragment.apiCaseScene.getTbCaseScene().ConfineSufferer);
            }
        }
    }

    public static void createFolder() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), strSDCardPathName);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
                Log.i(TAG, "mkdir" + folder.getAbsolutePath());
            } else {
                Log.i(TAG, "folder.exists");

            }
        } catch (Exception ex) {
        }

    }

    public void hiddenKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            if (apiStatus != null) {
                if (apiStatus.getStatus().equalsIgnoreCase("success")) {
//                Log.d(TAG, apiStatus.getData().getReason());
                    boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                    if (isSuccess) {
                        if (snackbar == null || !snackbar.isShown()) {
                            SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_LONG,
                                    "ยืนยันตรวจเสร็จเรียบร้อย\n" + apiStatus.getData().getReason().toString());
                            snackBarAlert.createSnacbar();
                        }
                    }
                } else {

                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_LONG,
                            getString(R.string.error_data) + " " + getString(R.string.network_error));
                    snackBarAlert.createSnacbar();
                }
            } else {
                SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_LONG,
                        getString(R.string.error_data) + " " + getString(R.string.network_error));
                snackBarAlert.createSnacbar();
            }
        }
    }
}