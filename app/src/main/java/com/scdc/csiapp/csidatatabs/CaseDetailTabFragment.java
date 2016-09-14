package com.scdc.csiapp.csidatatabs;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import  com.scdc.csiapp.connecting.ConnectionDetector;
import  com.scdc.csiapp.connecting.PreferenceData;
import  com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;
import  com.scdc.csiapp.main.MapActivity;
import  com.scdc.csiapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class CaseDetailTabFragment extends Fragment {

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
    String officialID, reportID;
    ArrayAdapter<String> adapterSubCaseType;
    String[] updateDT;
    private String message = "";
    TextView spnLocatePolice;
    TextView edtUpdateDateTime2;
    TextView editTextPhone1;
    private String sAddrDetail, sHouseNo, sVillageNo, sVillageName, sLane,
            sRoad, sDistrict, sAmphur, sProvinceName, sPostalCode, sLatitude,
            sLongitude;
    private AutoCompleteTextView autoCompleteDistrict, autoCompleteAmphur, autoCompleteProvince2;
    private EditText editAddrDetail, editHouseNo, editVillageNo,
            editVillageName, editLane, editRoad, editCircumstanceOfCaseDetail, edtVehicleDetail;
    private Button btnButtonSearchMap;
    static String sInquiryOfficialID, sProvinceID, sAmphurID = "";
    // layout Sufferer 3

    private ListView listViewSufferer, listViewInvestigator;
    private View linearLayoutAddSufferer, linearLayoutInvestigator;
    private Button btnAddSufferer, btnShowInvestigator;
    private ArrayList<HashMap<String, String>> suffererList;
    // CaseDateTime การรับเเจ้งเหตุ, การเกิดเหตุ, การทราบเหตุ
    private String sReceivingCaseDate, sReceivingCaseTime, sHappenCaseDate,
            sHappenCaseTime, sKnowCaseDate, sKnowCaseTime,
            sCircumstanceOfCaseDetail;
    private TextView editReceiveCaseDate, editReceiveCaseTime, editScheduleInvestDate;
    private TextView editHappenCaseDate, editHappenCaseTime;
    private TextView editKnowCaseDate, editKnowCaseTime, valueLat, valueLong;
    // InvestDateTime ตรวจ
    //private TextView editSceneInvestDate, editSceneInvestTime;
    // ตรวจเสร็จ
    //private TextView editCompleteSceneDate, editCompleteSceneTime;

    //TextView edtInvestDateTime, edtUpdateDateTime;
    //private String selectedInspector = null;
    private String selectedLocatePolice = null;
    private String selectedTypeReceive = null;

    String[][] mTypePoliceStationArray = null;
    //String[][] mSelectDataInspectorArray = null;
    //String[] mSelectDataInspectorArray2, mSelectDataInspectorID;
    //String[] mSelectDataInspectorRank;

    String[] mTypeReceiveArray = null;
    String SelectedPoliceStationID, SelectedTypeReceiveName = "";
    String mSelected;
    ArrayList<Integer> mMultiSelected;
    ArrayList<Boolean> mMultiChecked;
    String[][] mSelectDataInvestigatorArray = null;
    private ArrayList<HashMap<String, String>> InvestigatorList;
    protected static final int DIALOG_SelectDataInvestigator = 1; // Dialog 1 ID
    protected static final int DIALOG_AddSufferer = 0; // Dialog 2 ID
    ViewGroup viewByIdaddsufferer;
    View viewReceiveCSI;
    Context context;
    Button btn_clear_txt_1, btn_clear_txt_2, btn_clear_txt_3, btn_clear_txt_4, btn_clear_txt_5,
            btn_clear_txt_6, btn_clear_txt_7, btn_clear_txt_8, btn_clear_txt_9, btn_clear_txt_10, btn_clear_txt_11, btn_clear_txt_12;
    ArrayAdapter<String> adapterTypeReceive, adapterSelectDataInspector, adapterPoliceStation;
    protected static String selectScheduleID = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        viewReceiveCSI = inflater.inflate(R.layout.casedetail_tab_layout, null);

        context = viewReceiveCSI.getContext();

        rootLayout = (CoordinatorLayout) viewReceiveCSI.findViewById(R.id.rootLayoutReceive);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        getDateTime = new GetDateTime();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();
        updateDT = getDateTime.updateDataDateTime();
        Log.i("viewReceiveCSI", reportID);
        //new showScheduleInvestOfCase().execute(reportID);
        edtUpdateDateTime2 = (TextView) viewReceiveCSI.findViewById(R.id.edtUpdateDateTime2);
        //Form
        spnLocatePolice = (TextView) viewReceiveCSI.findViewById(R.id.spnLocatePolice);


        editTextPhone1 = (TextView) viewReceiveCSI.findViewById(R.id.editTextPhone1);


        editAddrDetail = (EditText) viewReceiveCSI.findViewById(R.id.edtAddrDetail);
        editAddrDetail.setEnabled(false);


        //datetime
        editReceiveCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseDate);
        editReceiveCaseDate.setEnabled(false);
        editReceiveCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editReceiveCaseTime);
        editReceiveCaseTime.setEnabled(false);
        editHappenCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseDate);
        editHappenCaseDate.setEnabled(false);
        editHappenCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editHappenCaseTime);
        editHappenCaseTime.setEnabled(false);
        editKnowCaseDate = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseDate);
        editKnowCaseDate.setEnabled(false);
        editKnowCaseTime = (TextView) viewReceiveCSI
                .findViewById(R.id.editKnowCaseTime);
        editKnowCaseTime.setEnabled(false);


// /layout 3 address
        editHouseNo = (EditText) viewReceiveCSI.findViewById(R.id.edtHouseNo);
        editHouseNo.setEnabled(false);

        editVillageNo = (EditText) viewReceiveCSI.findViewById(R.id.edtVillageNo);
        editVillageNo.setEnabled(false);

        editVillageName = (EditText) viewReceiveCSI
                .findViewById(R.id.edtVillageName);
        editVillageName.setEnabled(false);
        editLane = (EditText) viewReceiveCSI.findViewById(R.id.edtLane);
        editLane.setEnabled(false);
        editRoad = (EditText) viewReceiveCSI.findViewById(R.id.edtRoad);
        editRoad.setEnabled(false);
        autoCompleteDistrict = (AutoCompleteTextView) viewReceiveCSI
                .findViewById(R.id.edtDistrict);
        autoCompleteDistrict.setEnabled(false);
        autoCompleteAmphur = (AutoCompleteTextView) viewReceiveCSI
                .findViewById(R.id.edtAmphur);
        autoCompleteAmphur.setEnabled(false);
        autoCompleteProvince2 = (AutoCompleteTextView) viewReceiveCSI
                .findViewById(R.id.edtProvinceAddr);
        autoCompleteProvince2.setEnabled(false);
        btnButtonSearchMap = (Button) viewReceiveCSI.findViewById(R.id.btnButtonSearchMap);
        btnButtonSearchMap.setOnClickListener(new ReceiveOnClickListener());
        valueLat = (TextView) viewReceiveCSI.findViewById(R.id.valueLat);
        valueLong = (TextView) viewReceiveCSI.findViewById(R.id.valueLong);

        editCircumstanceOfCaseDetail = (EditText) viewReceiveCSI.findViewById(R.id.editCircumstanceOfCaseDetail);
        editCircumstanceOfCaseDetail.setEnabled(false);

        listViewSufferer = (ListView) viewReceiveCSI
                .findViewById(R.id.listViewSufferer);
        linearLayoutAddSufferer = viewReceiveCSI
                .findViewById(R.id.linearLayoutAddSufferer);
        linearLayoutAddSufferer.setVisibility(View.GONE);

        listViewSufferer.setOnTouchListener(new ListviewSetOnTouchListener());
        ShowListSufferer(reportID);


        return viewReceiveCSI;
    }


    private void ShowListInvestigatorSchedule(String sScheduleInvestDate, String reportID) {
        // TODO Auto-generated method stub

        InvestigatorList = mDbHelper.SelectInvestigatorInCase(sScheduleInvestDate, reportID);
        if (InvestigatorList.size() != 0) {
            Log.i("Investigator", "showlist");
            linearLayoutInvestigator.setVisibility(View.VISIBLE);
        } else {
            Log.i("Investigator", "no");
            linearLayoutInvestigator.setVisibility(View.GONE);
        }
        listViewInvestigator.setAdapter(new InvestigatorInCaseAdapter(getActivity()));
        // ShowListInvestigatorSchedule(reportID);

    }

    @SuppressLint("InflateParams")
    public class InvestigatorInCaseAdapter extends BaseAdapter {
        private Context context;

        public InvestigatorInCaseAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {

            // TODO Auto-generated method stub
            return InvestigatorList.size();
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
                convertView = inflater.inflate(R.layout.list_investigatorincase,
                        null);

            }

            // Colrank
            TextView txtCode = (TextView) convertView
                    .findViewById(R.id.txtInvestigatorRank);
            txtCode.setText(InvestigatorList.get(position).get("Rank"));

            // txtFName
            TextView txtInvestigatorFName = (TextView) convertView
                    .findViewById(R.id.txtInvestigatorFName);
            TextView txtInvestigatorLName = (TextView) convertView
                    .findViewById(R.id.txtInvestigatorLName);
            txtInvestigatorFName.setText(InvestigatorList.get(position).get(
                    "FirstName"));
            txtInvestigatorLName.setText(InvestigatorList.get(position).get("LastName"));
            return convertView;

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

    public class ReceiveOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v == btnButtonSearchMap) {
                String sLocaleName = "";
                String sHouseNo = "";
                String sVillageNo = "";
                String sVillageName = "";
                String sLane = "";
                String sRoad = "";
                String sDistrict = "";
                String sAmphur = "";
                String sProvince = "";

                if (editAddrDetail.getText().toString().length() != 0) {
                    sLocaleName = editAddrDetail.getText().toString();
                }
                if (editHouseNo.getText().toString().length() != 0) {
                    sHouseNo = editHouseNo.getText().toString();
                }
                if (editVillageNo.getText().toString().length() != 0) {
                    sVillageNo = editVillageNo.getText().toString();
                }
                if (editVillageName.getText().toString().length() != 0) {
                    sVillageName = editVillageName.getText().toString();
                }
                if (editLane.getText().toString().length() != 0) {
                    sLane = editLane.getText().toString();
                }
                if (editRoad.getText().toString().length() != 0) {
                    sRoad = editRoad.getText().toString();
                }
                if (autoCompleteDistrict.getText().toString().length() != 0) {
                    sDistrict = autoCompleteDistrict.getText().toString();
                }
                if (autoCompleteAmphur.getText().toString().length() != 0) {
                    sAmphur = autoCompleteAmphur.getText().toString();
                }
                if (autoCompleteProvince2.getText().toString().length() != 0) {
                    sProvince = autoCompleteProvince2.getText().toString();
                }
                if (valueLat.getText().toString().length() != 0) {
                    sLatitude = valueLat.getText().toString();
                }
                if (valueLong.getText().toString().length() != 0) {
                    sLongitude = valueLong.getText().toString();
                }
                Intent showMapActivity = new Intent(getActivity(), MapActivity.class);
                showMapActivity.putExtra("reportid", reportID);
                showMapActivity.putExtra("LocaleName", sLocaleName);
                showMapActivity.putExtra("houseNo", sHouseNo);
                showMapActivity.putExtra("villageNo", sVillageNo);
                showMapActivity.putExtra("villageName", sVillageName);
                showMapActivity.putExtra("lane", sLane);
                showMapActivity.putExtra("road", sRoad);
                showMapActivity.putExtra("District", sDistrict);
                showMapActivity.putExtra("Amphur", sAmphur);
                showMapActivity.putExtra("province", sProvince);
                showMapActivity.putExtra("Latitude", sLatitude);
                showMapActivity.putExtra("Longitude", sLongitude);

                startActivity(showMapActivity);

            }

        }
    }


    class showScheduleInvestOfCase extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }


        @Override
        protected String[] doInBackground(String... params) {
            String[] arrData = {""};
            arrData = mDbHelper.SelectScheduleInvestDate(params[0]);
            if (arrData != null) {


                Log.i("scheduleInvestDate", String.valueOf(arrData.length) + "/" + arrData[0] + "/" + arrData[1]);
            } else {
                arrData = null;
                Log.i("ScheduleInvestDate", "nohave");
            }

            return arrData;
        }

        protected void onPostExecute(String[] arrData) {

            if (arrData != null) {
                //selectScheduleID = arrData[0];
                editScheduleInvestDate.setText(arrData[1]);
                ShowListInvestigatorSchedule(arrData[1], reportID);

            } else {
                Log.i("ScheduleInvestDate", "no have");
            }

        }
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
                edtUpdateDateTime2.setText("อัพเดทข้อมูลล่าสุดเมื่อวันที่ " + getDateTime.changeDateFormatToCalendar(arrData[7]) + " เวลา " + arrData[8]);

/*                Toast.makeText(getActivity()
                                .getApplicationContext(),
                        "อัพเดทข้อมูลล่าสุดเมื่อวันที่ "+arrData[7]+ " เวลา "+arrData[8],
                        Toast.LENGTH_LONG).show();*/
                if (arrData[31] != null) {

                        editTextPhone1.setText(arrData[31]);

                } else {


                }

                editAddrDetail.setText(arrData[6]);
                editHouseNo.setText(arrData[32]);
                editVillageNo.setText(arrData[33]);
                editVillageName.setText(arrData[34]);
                editLane.setText(arrData[35]);
                editRoad.setText(arrData[36]);
                autoCompleteDistrict.setText(arrData[37]);
                autoCompleteAmphur.setText(arrData[13]);
                autoCompleteProvince2.setText(arrData[38]);

                editCircumstanceOfCaseDetail.setText(arrData[41]);
                String zerodate = "0000-00-00";
                if (arrData[21] != null) {

                    if (arrData[21].length() == 10) {
                        if(arrData[21].equals(zerodate)){

                        }else {
                            editReceiveCaseDate.setText(getDateTime.changeDateFormatToCalendar(arrData[21]));
                        }
                    } else {
                        // editCompleteSceneDate.setText("date3");
                        editReceiveCaseDate.setText(arrData[21]);
                    }

                }
                if (arrData[20] != null) {
                    if (arrData[20].length() > 1) {
                        editReceiveCaseTime.setText(arrData[20]);
                    }
                }
                if (arrData[19] != null) {
                    if (arrData[19].length() == 10) {
                        if(arrData[19].equals(zerodate)){

                        }else {
                            editHappenCaseDate.setText(getDateTime.changeDateFormatToCalendar(arrData[19]));
                        }
                    } else {
                        // editCompleteSceneDate.setText("date3");
                        editHappenCaseDate.setText(arrData[19]);
                    }


                }
                if (arrData[18] != null) {
                    if (arrData[18].length() > 1) {
                        editHappenCaseTime.setText(arrData[18]);
                    }
                }
                if (arrData[17] != null) {
                    if (arrData[17].length() == 10) {
                        if(arrData[17].equals(zerodate)){

                        }else {
                            editKnowCaseDate.setText(getDateTime.changeDateFormatToCalendar(arrData[17]));
                        }
                    } else {
                        editKnowCaseDate.setText(arrData[17]);
                    }


                }
                if (arrData[16] != null) {
                    if (arrData[16].length() > 1) {
                        editKnowCaseTime.setText(arrData[16]);
                    }
                }

                if (arrData[39] != null) {
                    valueLat.setText(arrData[39]);
                }
                if (arrData[40] != null) {
                    valueLong.setText(arrData[40]);
                }

                if (arrData[22] != null) {
                    Log.i("adapterPoliceStation 1", arrData[22]);
                    spnLocatePolice.setText(arrData[22]);

                }
            } else {
                Log.i("Recieve ", "Null!! ");
            }
        }
    }


    public void onStart() {
        super.onStart();
        Log.i("Check", "onStart recieve");

        new showData().execute(reportID);

    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("onPause", "onPause receive");


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStop", "onStop receive");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", "onDestroyView receive");
        //saveAllReceiveData();
    }


    public void ShowListSufferer(String reportID) {
        // TODO Auto-generated method stub
        suffererList = mDbHelper.SelectDataSuffererinCase(reportID);
        if (suffererList.size() != 0) {
            linearLayoutAddSufferer.setVisibility(View.VISIBLE);
        } else {
            linearLayoutAddSufferer.setVisibility(View.GONE);
        }
        listViewSufferer.setAdapter(new SuffererAdapter(getActivity()));
    }

    public class SuffererAdapter extends BaseAdapter {
        private Context context;

        public SuffererAdapter(Context c) {
            // super( c, R.layout.activity_column, R.id.rowTextView, );
            // TODO Auto-generated method stub
            context = c;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return suffererList.size();
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
                convertView = inflater.inflate(R.layout.list_sufferer, null);

            }

            final String sSuffererID = suffererList.get(position).get(
                    "SuffererID");
            String[] sAntecedentSufferer = getResources().getStringArray(
                    R.array.antecedent);

            final Spinner spinnerAntecedent = (Spinner) convertView
                    .findViewById(R.id.spinnerAntecedent);
            spinnerAntecedent
                    .setSelection(Arrays.asList(sAntecedentSufferer).indexOf(
                            suffererList.get(position).get("SuffererPrename")));

            final EditText editSuffererFirstName = (EditText) convertView
                    .findViewById(R.id.editSuffererFirstName);
            editSuffererFirstName.setText(suffererList.get(position).get(
                    "SuffererFirstName"));

            final EditText editSuffererLastName = (EditText) convertView
                    .findViewById(R.id.editSuffererLastName);
            editSuffererLastName.setText(suffererList.get(position).get(
                    "SuffererLastName"));
            final EditText editSuffererAge = (EditText) convertView
                    .findViewById(R.id.editSuffererAge);
            editSuffererAge.setText(suffererList.get(position).get(
                    "SuffererAge"));
            final EditText editTextSuffererPhone1 = (EditText) convertView
                    .findViewById(R.id.editTextSuffererPhone1);
            editTextSuffererPhone1.setText(suffererList.get(position).get(
                    "SuffererTelephone"));
            final EditText editTextSuffererMobile1 = (EditText) convertView
                    .findViewById(R.id.editTextSuffererMobile1);
            editTextSuffererMobile1.setText(suffererList.get(position).get(
                    "SuffererTelMobile"));
            final AutoCompleteTextView autoCompleteSuffererStatus = (AutoCompleteTextView) convertView
                    .findViewById(R.id.autoCompleteSuffererStatus);
            autoCompleteSuffererStatus.setText(suffererList.get(position).get(
                    "SuffererStatus"));
            String[] mSuffererStatusArray;
            mSuffererStatusArray = getResources().getStringArray(
                    R.array.suffererStatus);
            ArrayAdapter<String> adapterSuffererStatus = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line,
                    mSuffererStatusArray);
            autoCompleteSuffererStatus.setThreshold(1);
            autoCompleteSuffererStatus.setAdapter(adapterSuffererStatus);

            // imgEdit
            ImageButton imgEdit = (ImageButton) convertView
                    .findViewById(R.id.imgEdit);
            final AlertDialog.Builder adbEdit = new AlertDialog.Builder(
                    getActivity());
            imgEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    adbEdit.setTitle("แก้ไขข้อมูล");
                    adbEdit.setMessage("ยืนยันการแก้ไขข้อมูล");
                    adbEdit.setNegativeButton("Cancel", null);
                    adbEdit.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long saveStatus = mDbHelper
                                            .updateDataSufferer(
                                                    sSuffererID,
                                                    String.valueOf(spinnerAntecedent
                                                            .getSelectedItem()),
                                                    editSuffererFirstName
                                                            .getText()
                                                            .toString(),
                                                    editSuffererLastName
                                                            .getText()
                                                            .toString(),
                                                    editSuffererAge.getText()
                                                            .toString(),
                                                    editTextSuffererPhone1
                                                            .getText()
                                                            .toString(),
                                                    editTextSuffererMobile1
                                                            .getText()
                                                            .toString(),
                                                    autoCompleteSuffererStatus
                                                            .getText()
                                                            .toString());
                                    if (saveStatus <= 0) {
                                        Log.i("updateDataSufferer", "Error!! ");
                                    } else {
                                        Log.i("updateDataSufferer", "ok!! ");
                                        Toast.makeText(getActivity(),
                                                "แก้ไขเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                    adbEdit.show();
                }
            });
            // imgDelete
            ImageButton imgDelete = (ImageButton) convertView
                    .findViewById(R.id.imgDelete);
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    getActivity());
            imgDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // final String inspectorID =
                    // InspectorList.get(position).get(
                    // "InspectorID");
                    adb.setTitle("ลบข้อมูล");
                    adb.setMessage("ยืนยันการลบข้อมูล[ข้อมูลผู้เสียหายชื่อ  "
                            + suffererList.get(position).get(
                            "SuffererFirstName") + "]");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    long flg = mDbHelper
                                            .DeleteSelectedSufferer(sSuffererID);
                                    if (flg > 0) {

                                        Toast.makeText(getActivity(),
                                                "ลบข้อมูลเรียบร้อยแล้ว",
                                                Toast.LENGTH_LONG).show();
                                        ShowListSufferer(getArguments()
                                                .getString(reportID));
                                    } else {
                                        Toast.makeText(getActivity(),
                                                "เกิดการผิดพลาด",
                                                Toast.LENGTH_LONG).show();
                                        Log.i("DeleteSelectedSufferer", "Error");
                                    }

                                }

                            });
                    adb.show();
                }
            });

            return convertView;

        }

    }

}