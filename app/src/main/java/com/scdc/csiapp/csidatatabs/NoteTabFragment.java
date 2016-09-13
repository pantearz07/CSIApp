package com.scdc.csiapp.csidatatabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 29/1/2559.
 */

    public class NoteTabFragment extends Fragment {

    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    private PreferenceData mManager;
    String officialID, reportID;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    private RecyclerView notelist;
    private List<CSINoteList> csiNotelists;
    private NotelistsAdapter notelistsAdapter;
    GetDateTime getDateTime;
    Context context;
    Cursor mCursor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);

        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        Context context = getContext();
        getDateTime = new GetDateTime();

        View viewNoteTab = inflater.inflate(R.layout.note_tab_layout, container, false);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        fabBtn = (FloatingActionButton) viewNoteTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent showNoteActivity = new Intent(getActivity(), NoteActivity.class);
                showNoteActivity.putExtra("fileid", "");
                startActivity(showNoteActivity);
            }
        });

        notelist = (RecyclerView) viewNoteTab.findViewById(R.id.notelist);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        notelist.setLayoutManager(llm);
        notelist.setHasFixedSize(true);
        relistData();
        notelistsAdapter.setOnItemClickListener(onItemClickListener);
        return viewNoteTab;

    }
    NotelistsAdapter.OnItemClickListener onItemClickListener = new NotelistsAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final CSINoteList csidata = csiNotelists.get(position);
            final String FileID = csidata.sNoteID;
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage("ดูโน้ตย่อ "+FileID);
            builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Clicked " + FileID, Toast.LENGTH_SHORT).show();
                    Intent showNoteActivity = new Intent(getActivity(), NoteActivity.class);
                    showNoteActivity.putExtra("fileid", FileID);
                    startActivity(showNoteActivity);
                }
            });
            builder.setNeutralButton("ลบ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "ลบ " + FileID, Toast.LENGTH_SHORT).show();
                    mDbHelper.DeleteMediaFile(reportID, FileID);

                    relistData();
                }
            });

            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();



        }
    };
    private void relistData() {
        initializeData();
        notelistsAdapter = new NotelistsAdapter(csiNotelists);
        notelist.setAdapter(notelistsAdapter);

    }
    private void initializeData() {

        mDb = mDbHelper.getReadableDatabase();
        String strSQL = "SELECT * FROM " + SQLiteDBHelper.TABLE_multimediafile + " WHERE "
                + SQLiteDBHelper.COL_CaseReportID + " = '" + reportID + "' AND "
                + SQLiteDBHelper.COL_FileType + " =  'note' ORDER BY "
                + SQLiteDBHelper.COL_FileID + " DESC";

        mCursor = mDb.rawQuery(strSQL, null);

        Log.i("log_show", "Show note" + mCursor.getCount());

        csiNotelists = new ArrayList<>();

        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            csiNotelists.add(new CSINoteList(mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_FileID)),
                    mCursor.getString(mCursor
                            .getColumnIndex(SQLiteDBHelper.COL_Timestamp))

            ));

            mCursor.moveToNext();
        }
        mCursor.close();

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i("log_show", " onResume ");
        relistData();
    }
}
