package com.scdc.csiapp.invmain;

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
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    Cursor mCursor;
    List<ScheduleList> scheduleLists;
    OnItemClickListener mItemClickListener;

    ScheduleAdapter(List<ScheduleList> scheduleLists) {
        this.scheduleLists = scheduleLists;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cvSchedule;
        TextView txt_schedule_date;
        TextView txt_investnamelists;
        ImageView ic_CaseType;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            cvSchedule = (CardView) itemView.findViewById(R.id.cvSchedule);
            ic_CaseType = (ImageView) itemView.findViewById(R.id.ic_CaseType);
            txt_schedule_date = (TextView) itemView.findViewById(R.id.txt_schedule_date);
            txt_investnamelists = (TextView) itemView.findViewById(R.id.txt_investnamelists);
            cvSchedule.setOnClickListener(this);

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
    public ScheduleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_cardview, viewGroup, false);
        ScheduleViewHolder csivh = new ScheduleViewHolder(v);
        mDbHelper = new SQLiteDBHelper(viewGroup.getContext());
        mDb = mDbHelper.getWritableDatabase();
        return csivh;
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder csidataholder, int position) {
        ScheduleList csidata = scheduleLists.get(position);

        csidataholder.txt_schedule_date.setText("ประจำวันที่: " + csidata.Schedule_Date);
        if (csidata.ScheduleID == null) {
            csidataholder.txt_investnamelists.setText("-");

        } else {
            String investlist ="";
           /*$scheduleofofficialSQL =mysql_query("SELECT official.OfficialID,official.Rank,official.FirstName,official.LastName,official.Position  FROM official,scheduleinvestigates,scheduleinvingroup,schedulegroup
								WHERE scheduleinvingroup.InvOfficialID = official.OfficialID AND schedulegroup.ScheduleInvestigateID = scheduleinvestigates.ScheduleInvestigateID AND scheduleinvingroup.ScheduleGroupID='".$ScheduleGroupID."'
								AND scheduleinvingroup.ScheduleGroupID = schedulegroup.ScheduleGroupID AND scheduleinvestigates.ScheduleDate='".$scheduleQuery['ScheduleDate']."' ORDER BY official.OfficialID ASC");
							*/
            String strSQL = "SELECT official.OfficialID,official.Rank,official.FirstName,official.LastName,official.Position " +
                    "FROM official,scheduleinvingroup " +
                    "WHERE scheduleinvingroup.InvOfficialID = official.OfficialID AND " +
                    "scheduleinvingroup.ScheduleGroupID='" + csidata.ScheduleGroupID + "'" +
                   // "scheduleinvingroup.ScheduleID = scheduleinvestigate.ScheduleID " +
                    "ORDER BY official.OfficialID ASC ";
            /*String strSQL = "SELECT official.OfficialID,official.Rank,official.FirstName,official.LastName,official.Position " +
                    "FROM official,scheduleinvestigate,scheduleofofficial " +
                    "WHERE scheduleofofficial.OfficialID = official.OfficialID AND " +
                    "scheduleinvestigate.ScheduleID='" + csidata.ScheduleID + "' AND " +
                    "scheduleofofficial.ScheduleID = scheduleinvestigate.ScheduleID " +
                    "ORDER BY official.OfficialID ASC ";*/
            mCursor = mDb.rawQuery(strSQL, null);
            Log.i("mCursor",String.valueOf(mCursor.getCount()));
            mCursor.moveToFirst();
            int i =mCursor.getCount();
            while (!mCursor.isAfterLast()) {

                String OfficialID = mCursor.getString(mCursor
                        .getColumnIndex(SQLiteDBHelper.COL_OfficialID));
                String Rank = mCursor.getString(mCursor
                        .getColumnIndex(SQLiteDBHelper.COL_Rank));
                String FirstName = mCursor.getString(mCursor
                        .getColumnIndex(SQLiteDBHelper.COL_FirstName));
                String LastName = mCursor.getString(mCursor
                        .getColumnIndex(SQLiteDBHelper.COL_LastName));
                String Position = mCursor.getString(mCursor
                        .getColumnIndex(SQLiteDBHelper.COL_Position));
                investlist = String.valueOf(i) + ") " + Rank + " " + FirstName + " " + LastName + " " + Position + "\n"+investlist;
                mCursor.moveToNext();
                i--;
            }
            mCursor.close();
            csidataholder.txt_investnamelists.setText(investlist);

        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return scheduleLists.size();
        //return (null != receivigCaseLists ? receivigCaseLists.size() : 0);

    }


}
