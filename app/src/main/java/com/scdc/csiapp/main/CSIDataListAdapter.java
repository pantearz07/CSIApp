package com.scdc.csiapp.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.SQLiteDBHelper;

import java.util.List;

/**
 * Created by Pantearz07 on 15/10/2558.
 */
public class CSIDataListAdapter extends RecyclerView.Adapter<CSIDataListAdapter.CSIDataViewHolder>{
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    Cursor mCursor;
    List<CSIDataList> csiDataLists;
    OnItemClickListener mItemClickListener;
    CSIDataListAdapter(List<CSIDataList> csiDataLists){
        this.csiDataLists = csiDataLists;
    }
    public class CSIDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cvCSI;
        TextView positioncase;
        TextView policeStation;
        TextView typeCase;
        TextView inqInfo;
        TextView sufferrerInfo;
        TextView receiviedatetime;
        ImageView ic_CaseType;
        public CSIDataViewHolder(View itemView) {
            super(itemView);
            cvCSI= (CardView)itemView.findViewById(R.id.cvCSI);
            ic_CaseType = (ImageView)itemView.findViewById(R.id.ic_CaseType);
            typeCase= (TextView)itemView.findViewById(R.id.casetype_name);
            positioncase= (TextView)itemView.findViewById(R.id.positioncase);
            policeStation= (TextView)itemView.findViewById(R.id.police_station);
            inqInfo= (TextView)itemView.findViewById(R.id.inqInfo);
            sufferrerInfo= (TextView)itemView.findViewById(R.id.sufferrerInfo);
            receiviedatetime= (TextView)itemView.findViewById(R.id.receiviedatetime);

            cvCSI.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    @Override
    public CSIDataViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draft_cardview, viewGroup, false);
        CSIDataViewHolder csivh = new CSIDataViewHolder(v);
        mDbHelper = new SQLiteDBHelper(viewGroup.getContext());
        mDb = mDbHelper.getReadableDatabase();
        return csivh;
    }

    @Override
    public void onBindViewHolder(CSIDataViewHolder csidataholder, int position) {
        CSIDataList csidata = csiDataLists.get(position);
        String casereportid = csidata.caseReportID;
        csidataholder.typeCase.setText("ประเภทคดี: " + csidata.typeCase);
        String positioncase = csidata.LocaleName+" "+csidata.HouseNo+" "+csidata.VillageNo+" "+csidata.VillageName
                +" "+csidata.LaneName+" "+csidata.RoadName+" "+csidata.District+" "+csidata.Amphur+" "+csidata.Province;

        csidataholder.positioncase.setText("เกิดที่: " + positioncase);

        if (csidata.policeStation == null) {
            csidataholder.policeStation.setText("สภ.");

        } else {
            csidataholder.policeStation.setText("สภ. " + csidata.policeStation);

        }

        csidataholder.receiviedatetime.setText("แจ้งเหตุ: " + csidata.datereceivie + " เวลา " + csidata.timereceivie + " น.");

        String OfficialID = csidata.OfficialID;
        Log.i("OfficialID", csidata.OfficialID);
        String[] arrData1 = null;
        arrData1 = mDbHelper.SelectDataOfficial(OfficialID);
        if (arrData1 != null) {
            csidataholder.inqInfo.setVisibility(View.VISIBLE);
            csidataholder.inqInfo.setText(arrData1[4] + " " + arrData1[6] + " " + arrData1[7]+ " (" + arrData1[8]
                    + ") โทร. (" + arrData1[2] + ")-" + arrData1[1]);
        }
        /*String strSQLInq = "SELECT * FROM official WHERE 1 AND OfficialID ='"+OfficialID+"'";
        Cursor mCursorinq = mDb.rawQuery(strSQLInq, null);
        if (mCursorinq != null) {
            if (mCursorinq.moveToFirst()) {
                String Rank = mCursorinq.getString(mCursorinq
                        .getColumnIndex(SQLiteDBHelper.COL_Rank));

                String FirstName = mCursorinq.getString(mCursorinq
                        .getColumnIndex(SQLiteDBHelper.COL_FirstName));
                String LastName = mCursorinq.getString(mCursorinq
                        .getColumnIndex(SQLiteDBHelper.COL_LastName));
            String Position = mCursorinq.getString(mCursorinq
                    .getColumnIndex(SQLiteDBHelper.COL_Position));

            String AreaCodeTel = mCursorinq.getString(mCursorinq
                    .getColumnIndex(SQLiteDBHelper.COL_AreaCodeTel));
            String PhoneNumber = mCursorinq.getString(mCursorinq
                    .getColumnIndex(SQLiteDBHelper.COL_PhoneNumber));
                csidataholder.inqInfo.setVisibility(View.VISIBLE);
            csidataholder.inqInfo.setText( Rank + " " + FirstName + " " + LastName + " (" + Position
                    + ") โทร. (" + AreaCodeTel + ")-" + PhoneNumber);

            }

        }
        mCursorinq.close();*/
        String[][] sufferrerlist =null;
        sufferrerlist = mDbHelper.SelectDataSufferer(casereportid);
        if(sufferrerlist == null){
            csidataholder.sufferrerInfo.setVisibility(View.GONE);
        }else {
            String sufferrerinfo="";
            for(int i = 0; i < sufferrerlist.length; i++ ){
                sufferrerinfo =  sufferrerinfo+"\n"+String.valueOf(i+1) + ") " + sufferrerlist[i][5] + " " + sufferrerlist[i][6]
                        + " โทร. " +  sufferrerlist[i][1] ;

            }
            csidataholder.sufferrerInfo.setText("ผู้เสียหาย" + sufferrerinfo);
        }
        /*
        String strSQL = "SELECT * FROM sufferer WHERE 1 AND CaseReportID ='"+casereportid+"'";
        mCursor = mDb.rawQuery(strSQL, null);
        Log.i("mCursor",String.valueOf(mCursor.getCount()));
        mCursor.moveToFirst();
        int i =mCursor.getCount();
        while (!mCursor.isAfterLast()) {

            String SuffererID = mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_SuffererID));
            String SuffererFirstName = mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_SuffererFirstName));
            String SuffererLastName = mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_SuffererLastName));
            String SuffererTelephone = mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_SuffererTelephone));
            String SuffererTelMobile = mCursor.getString(mCursor
                    .getColumnIndex(SQLiteDBHelper.COL_SuffererTelMobile));
            sufferrerlist =  "\n"+String.valueOf(i) + ") " + SuffererFirstName + " " + SuffererLastName + " โทร. " + SuffererTelMobile  +sufferrerlist;
            mCursor.moveToNext();
            i--;
        }
        mCursor.close();
        mDb.close();
        if(mCursor.getCount()==0){
            csidataholder.sufferrerInfo.setVisibility(View.GONE);
        }else {
            csidataholder.sufferrerInfo.setText("ผู้เสียหาย" + sufferrerlist);
        }*/
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return csiDataLists.size();
    }
/*
    public void refreshList(){
        this.notifyDataSetChanged();
        Log.i("refreshList", "refreshList");
    }*/



}
