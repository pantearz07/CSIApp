package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 23/9/2558.
 */
public class DoneListFragment extends Fragment {

    CoordinatorLayout rootLayoutDraft;
    FloatingActionButton fabBtnDraft;
    private RecyclerView rvDraft;
    private List<CSIDataList> csiDataLists;
    private CSIDataListAdapter csiDataListAdapter;
    Context context;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    FragmentManager mFragmentManager;
    GetDateTime getDateTime;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;
    Cursor mCursor;
    String officialID;
    EmergencyTabFragment emergencyTabFragment;
    AcceptDoneTabFragment acceptDoneTabFragment;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewdraft =  inflater.inflate(R.layout.emergency_layout,null);
        Context context = getContext();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.done);
        rootLayoutDraft = (CoordinatorLayout) viewdraft.findViewById(R.id.rootLayoutDraft);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = viewdraft.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.isNetworkAvailable();
        isConnectingToInternet = cd.isConnectingToInternet();
        TextView txtUpdateDate = (TextView) viewdraft
                .findViewById(R.id.txtUpdateDate);
        txtUpdateDate.setVisibility(View.GONE);
        getDateTime = new GetDateTime();
        acceptDoneTabFragment = new AcceptDoneTabFragment();
        emergencyTabFragment = new EmergencyTabFragment();

        fabBtnDraft = (FloatingActionButton) viewdraft.findViewById(R.id.fabBtnDraft);
        fabBtnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View view1 = inflater.inflate(R.layout.dialog_add_case, null);
                dialog.setView(view1);

                final EditText edtReportNo = (EditText) view1.findViewById(R.id.edtReportNo);
                edtReportNo.setVisibility(View.GONE);
                final Spinner spnCaseType = (Spinner) view1.findViewById(R.id.spnCaseType);
                final String[] CaseType = getResources().getStringArray(R.array.casetype);
                ArrayAdapter<String> adapterCaseType = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, CaseType);
                spnCaseType.setAdapter(adapterCaseType);
                final String[] selectedCaseType = new String[1];
                spnCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedCaseType[0] = CaseType[position];
                        Log.i("selectedCaseType", "Select : " + CaseType[position] + " " + selectedCaseType[0]);


                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final Spinner spnSubCaseType = (Spinner) view1.findViewById(R.id.spnSubCaseType);
                //final String mSubCaseTypeArray[][] = mDbHelper.SelectSubCaseType();
                final String[] SubCaseType = getResources().getStringArray(R.array.subcasetypeproperties);
                ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, SubCaseType);
                spnSubCaseType.setAdapter(adapterSubCaseType);
                final String[] selectedSubCaseType = new String[1];
                spnSubCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubCaseType[0] = SubCaseType[position];
                        Log.i("spnSubCaseType", "Select : " + SubCaseType[position] + " " + selectedSubCaseType[0]);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final TextView edtYear = (TextView) view1.findViewById(R.id.edtYear);
                edtYear.setVisibility(View.GONE);
                dialog.setTitle("เพิ่มข้อมูลการตรวจสถานที่เกิดเหตุ");
                dialog.setIcon(R.drawable.ic_noti);
                dialog.setCancelable(true);
// Current Date
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

                final String saveDataTime = dateTimeCurrent[2] + dateTimeCurrent[1] + dateTimeCurrent[0] + "_" + dateTimeCurrent[3] + dateTimeCurrent[4] + dateTimeCurrent[5];
//RC_07042016_034747
                final String sEmergencyDate = dateTimeCurrent[2] + "/" + dateTimeCurrent[1] + "/" + dateTimeCurrent[0];
                final String sEmergencyTime =dateTimeCurrent[3]+ ":" + dateTimeCurrent[4];
                dialog.setPositiveButton("ถัดไป", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String reportNo = "";

                        String reportID = "RC_" + saveDataTime;

                        // save new Report
                        long saveStatus1 = mDbHelper.saveReportID(
                                reportID, reportNo, officialID, "", selectedCaseType[0], selectedSubCaseType[0], "waiting");
                        Log.i("save report", selectedCaseType[0] + " " + selectedSubCaseType[0]);
                        if (saveStatus1 <= 0) {
                            Log.i("save report", "Error!! ");
                        } else {
                            Log.i("save report", reportID + " " + selectedSubCaseType[0]
                                    + " " + officialID);
                            long SaveReceivingCase = mDbHelper.SaveReceivingCase(reportID, officialID, "receiving", sEmergencyDate,sEmergencyTime);
                            if (SaveReceivingCase == -1) {
                                Log.i("log_show draft", "SaveReceivingCase  Error!! ");
                                // status = "0";
                            } else {
                                Log.i("log_show draft", " SaveReceivingCase ok" + officialID);

                            }
                        }
                        //save preference reportID
                        mManager.setPreferenceData(mManager.PREF_REPORTID, reportID);


                         FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                         fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();


                    }

                });

                dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                dialog.create();
                dialog.show();
            }
        });

        rvDraft = (RecyclerView) viewdraft.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvDraft.setLayoutManager(llm);
        rvDraft.setHasFixedSize(true);
        //relistData();
        initializeData();
        csiDataListAdapter = new CSIDataListAdapter(csiDataLists);
        rvDraft.setAdapter(csiDataListAdapter);
        csiDataListAdapter.setOnItemClickListener(onItemClickListener);

        return viewdraft;
    }

    CSIDataListAdapter.OnItemClickListener onItemClickListener = new CSIDataListAdapter.OnItemClickListener(){

        @Override
        public void onItemClick(View view, int position) {

            final CSIDataList csidata = csiDataLists.get(position);
            final String caserepTD = csidata.caseReportID;

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage("ดูข้อมูลการตรวจนี้ " +caserepTD);
            builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Clicked " + caserepTD, Toast.LENGTH_SHORT).show();

                    mManager.setPreferenceData(mManager.PREF_REPORTID, caserepTD);

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, acceptDoneTabFragment).addToBackStack(null).commit();
                }
            });
            builder.setNeutralButton("ปิดงาน", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_CASESCENE.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_receivingcase.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_otherofficialinscene.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_SUFFERER.toString());
                    Toast.makeText(getActivity(), "ลบ " + caserepTD +" แล้ว ", Toast.LENGTH_SHORT).show();
                    //relistData();
                    Log.i("log_show full 1", " delete " + caserepTD);
                    initializeData();
                    csiDataListAdapter = new CSIDataListAdapter(csiDataLists);
                    rvDraft.setAdapter(csiDataListAdapter);
                }
            });

            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

            //Snackbar.make(view, "Clicked " + csidata.caseReportID, Snackbar.LENGTH_LONG)
            //       .setAction("Action", null).show();



        }
    };
    private void relistData() {
     //
        initializeData();
        csiDataListAdapter = new CSIDataListAdapter(csiDataLists);
        rvDraft.setAdapter(csiDataListAdapter);

    }
    private void initializeData() {
        mDb = mDbHelper.getReadableDatabase();

        String strSQL = "SELECT C.*,R.InquiryOfficialID,substr(C.ReceivingCaseDate,1,2)AS days,substr(C.ReceivingCaseDate,4,2) AS months, "+
                "substr(C.ReceivingCaseDate,7,4) AS years  FROM casescene AS C,receivingcase AS R " +
                "WHERE R.InquiryOfficialID ='"+ officialID+"'  AND C.CaseReportID = R.CaseReportID " +
                "AND C.ReportStatus = 'investigated' ORDER BY years DESC, months DESC, days DESC, C.ReceivingCaseTime DESC";

        mCursor = mDb.rawQuery(strSQL, null);

        Log.i("log_show", "Show data All investigating have " + mCursor.getCount());

        csiDataLists = new ArrayList<>();

        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            csiDataLists.add(new CSIDataList(mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_CaseReportID)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_SubCaseTypeName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_LocaleName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_HouseNo)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_VillageNo)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_VillageName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_LaneName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_RoadName)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_District)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Amphur)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Province)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_PoliceStation)),
                    getDateTime.changeDateFormatToCalendar(mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_ReceivingCaseDate))),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_ReceivingCaseTime)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_InvestigatorOfficialID))
            ));

            mCursor.moveToNext();
        }
        mCursor.close();
        mDb.close();
        mDbHelper.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("log_show full", " onAttach ");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("log_show full", " onActivityCreated ");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("log_show full", " onResume ");
        //csiDataListAdapter.setOnItemClickListener(onItemClickListener);
        csiDataListAdapter.notifyDataSetChanged();
    }
}