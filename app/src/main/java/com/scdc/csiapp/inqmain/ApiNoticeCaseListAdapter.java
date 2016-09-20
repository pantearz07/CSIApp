package com.scdc.csiapp.inqmain;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.connecting.SQLiteDBHelper;

import java.util.List;

/**
 * Created by cbnuke on 9/19/16.
 */
public class ApiNoticeCaseListAdapter extends RecyclerView.Adapter<ApiNoticeCaseListAdapter.CSIDataViewHolder> {
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    Cursor mCursor;
    List<ApiNoticeCase> apiNoticeCases;
    OnItemClickListener mItemClickListener;

    ApiNoticeCaseListAdapter(List<ApiNoticeCase> apiNoticeCases) {
        this.apiNoticeCases = apiNoticeCases;
    }

    public class CSIDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        CardView cvCSI;
        TextView positioncase;
        TextView policeStation;
        TextView typeCase;
        TextView inqInfo;
        TextView sufferrerInfo;
        TextView receiviedatetime;
        TextView CaseStatus;
        ImageView ic_CaseType;

        public CSIDataViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            cvCSI = (CardView) itemView.findViewById(R.id.cvCSI);
            ic_CaseType = (ImageView) itemView.findViewById(R.id.ic_CaseType);
            typeCase = (TextView) itemView.findViewById(R.id.casetype_name);
            positioncase = (TextView) itemView.findViewById(R.id.positioncase);
            policeStation = (TextView) itemView.findViewById(R.id.police_station);
            inqInfo = (TextView) itemView.findViewById(R.id.inqInfo);
            sufferrerInfo = (TextView) itemView.findViewById(R.id.sufferrerInfo);
            receiviedatetime = (TextView) itemView.findViewById(R.id.receiviedatetime);
            CaseStatus = (TextView) itemView.findViewById(R.id.txtCaseStatus);

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
        return csivh;
    }

    @Override
    public void onBindViewHolder(CSIDataViewHolder csidataholder, int position) {

        ApiNoticeCase apiNoticeCase = apiNoticeCases.get(position);
        csidataholder.typeCase.setText("ประเภทคดี: " + apiNoticeCase.getTbCaseSceneType().CaseTypeName);
        String positioncase = apiNoticeCase.getTbNoticeCase().LocaleName + " " + apiNoticeCase.getTbDistrict().DISTRICT_NAME
                + " " + apiNoticeCase.getTbAmphur().AMPHUR_NAME + " " + apiNoticeCase.getTbProvince().PROVINCE_NAME;

        csidataholder.positioncase.setText("เกิดที่: " + positioncase);

        if (apiNoticeCase.getTbNoticeCase().PoliceStationID == "") {
            csidataholder.policeStation.setText("สภ.");
        } else {
            csidataholder.policeStation.setText("สภ. " + apiNoticeCase.getTbPoliceStation().PoliceStationName);

        }

        csidataholder.receiviedatetime.setText("แจ้งเหตุ: " + apiNoticeCase.getTbNoticeCase().ReceivingCaseDate + " เวลา " + apiNoticeCase.getTbNoticeCase().ReceivingCaseTime + " น.");

        if (apiNoticeCase.getTbNoticeCase().InvestigatorOfficialID != null) {
            csidataholder.inqInfo.setVisibility(View.VISIBLE);
            csidataholder.inqInfo.setText(apiNoticeCase.getTbOfficial().Rank + " " + apiNoticeCase.getTbOfficial().FirstName + " " + apiNoticeCase.getTbOfficial().LastName
                    + " (" + apiNoticeCase.getTbOfficial().Position
                    + ") โทร. " + apiNoticeCase.getTbOfficial().PhoneNumber);
        }

        if (apiNoticeCase.getTbNoticeCase().SuffererName == "") {
            csidataholder.sufferrerInfo.setVisibility(View.GONE);
        } else {
            csidataholder.sufferrerInfo.setText("ผู้เสียหาย" + apiNoticeCase.getTbNoticeCase().SuffererPrename + " "
                    + apiNoticeCase.getTbNoticeCase().SuffererName + " โทร: " + apiNoticeCase.getTbNoticeCase().SuffererPhoneNum);
        }

        // เปลี่ยนสีเส้นตาม CaseStatus ด้วยการเปลี่ยนรูปใหม่มาใส่แทนตัวเก่า
        String CaseStatus = apiNoticeCase.getTbNoticeCase().CaseStatus;
        if (CaseStatus.equalsIgnoreCase("receive")) {
            csidataholder.ic_CaseType.setImageDrawable(ContextCompat.getDrawable(csidataholder.rootView.getContext(), R.drawable.label_casestatus1));
        } else if (CaseStatus.equalsIgnoreCase("notice")) {
            csidataholder.ic_CaseType.setImageDrawable(ContextCompat.getDrawable(csidataholder.rootView.getContext(), R.drawable.label_casestatus2));
        } else if (CaseStatus.equalsIgnoreCase("investigating")) {
            csidataholder.ic_CaseType.setImageDrawable(ContextCompat.getDrawable(csidataholder.rootView.getContext(), R.drawable.label_casestatus3));
        } else if (CaseStatus.equalsIgnoreCase("investigated")) {
            csidataholder.ic_CaseType.setImageDrawable(ContextCompat.getDrawable(csidataholder.rootView.getContext(), R.drawable.label_casestatus4));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return apiNoticeCases.size();
    }
}
