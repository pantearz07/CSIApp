package com.scdc.csiapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
public class FullListFragment  extends Fragment {

    CoordinatorLayout rootLayoutDraft;

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

    CSIDataTabFragment fCSIDataTabFragment;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewdraft =  inflater.inflate(R.layout.draft_layout,null);
        Context context = getContext();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fullcsidata);
        rootLayoutDraft = (CoordinatorLayout) viewdraft.findViewById(R.id.rootLayoutDraft);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = viewdraft.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        cd = new ConnectionDetector(getActivity());
        networkConnectivity = cd.networkConnectivity();
        isConnectingToInternet = cd.isConnectingToInternet();
        TextView txtUpdateDate = (TextView) viewdraft
                .findViewById(R.id.txtUpdateDate);
        txtUpdateDate.setVisibility(View.GONE);
        getDateTime = new GetDateTime();
        fCSIDataTabFragment = new CSIDataTabFragment();


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
                    fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();
                }
            });
            builder.setNeutralButton("ปิดงาน", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_CASESCENE.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_FEATUREOUT.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_FEATUREIN.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_receivingcase.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_otherofficialinscene.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_SUFFERER.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_PROPERTYLOSS.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_FindEevidence.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_resultscene.toString());
                    mDbHelper.DeleteReport1(caserepTD,
                            mDbHelper.TABLE_multimediafile.toString());
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

        String strSQL = "SELECT C.*,R.InquiryOfficialID,substr(C.ReceivingCaseDate,1,2)AS days,substr(C.ReceivingCaseDate,4,2) AS months, " +
                "substr(C.ReceivingCaseDate,7,4) AS years  FROM casescene AS C,receivingcase AS R " +
                "WHERE C.InvestigatorOfficialID ='"+ officialID+"'  AND C.CaseReportID = R.CaseReportID " +
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