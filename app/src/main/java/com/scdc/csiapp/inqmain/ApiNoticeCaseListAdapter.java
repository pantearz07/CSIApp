package com.scdc.csiapp.inqmain;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

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
    GetDateTime getDateTime = new GetDateTime();


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
        ImageView iv_mode;
        TextView txt_status;
        RelativeLayout rel_status;

        public CSIDataViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            cvCSI = (CardView) itemView.findViewById(R.id.cvCSI);
            rel_status = (RelativeLayout) itemView.findViewById(R.id.rel_status);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            typeCase = (TextView) itemView.findViewById(R.id.casetype_name);
            positioncase = (TextView) itemView.findViewById(R.id.positioncase);
            policeStation = (TextView) itemView.findViewById(R.id.police_station);
            inqInfo = (TextView) itemView.findViewById(R.id.inqInfo);
            sufferrerInfo = (TextView) itemView.findViewById(R.id.sufferrerInfo);
            receiviedatetime = (TextView) itemView.findViewById(R.id.receiviedatetime);
            CaseStatus = (TextView) itemView.findViewById(R.id.txtCaseStatus);
            iv_mode = (ImageView) itemView.findViewById(R.id.iv_mode);

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
        // set icon mode
        if (apiNoticeCase.getMode() != null && apiNoticeCase.getMode().equalsIgnoreCase("online")) {
            csidataholder.iv_mode.setImageResource(R.drawable.ic_router_black_24dp);
        } else if (apiNoticeCase.getMode() != null && apiNoticeCase.getMode().equalsIgnoreCase("offline")) {
            csidataholder.iv_mode.setImageResource(R.drawable.ic_phone_android_black_24dp);
        } else {
            csidataholder.iv_mode.setImageResource(R.drawable.ic_help_black_24dp);
        }

        csidataholder.typeCase.setText("ประเภทคดี: " + apiNoticeCase.getTbCaseSceneType().CaseTypeName);
        String DISTRICT_NAME = "", AMPHUR_NAME = "", PROVINCE_NAME = "";
        if (apiNoticeCase.getTbDistrict() != null) {
            DISTRICT_NAME = apiNoticeCase.getTbDistrict().DISTRICT_NAME;
        }
        if (apiNoticeCase.getTbAmphur() != null) {
            AMPHUR_NAME = apiNoticeCase.getTbAmphur().AMPHUR_NAME;
        }
        if (apiNoticeCase.getTbProvince() != null) {
            PROVINCE_NAME = apiNoticeCase.getTbProvince().PROVINCE_NAME;
        }
        String positioncase = "";
        if (apiNoticeCase.getTbNoticeCase().LocaleName != null) {
            positioncase = apiNoticeCase.getTbNoticeCase().LocaleName + " " + DISTRICT_NAME
                    + " " + AMPHUR_NAME + " " + PROVINCE_NAME;
        } else {
            positioncase = DISTRICT_NAME + " " + AMPHUR_NAME + " " + PROVINCE_NAME;
        }
        csidataholder.positioncase.setText("เกิดที่: " + positioncase);

        if (apiNoticeCase.getTbNoticeCase().PoliceStationID.equalsIgnoreCase("")) {
            csidataholder.policeStation.setText("สภ.");
        } else {
            csidataholder.policeStation.setText("สภ. " + apiNoticeCase.getTbPoliceStation().PoliceStationName);

        }

        csidataholder.receiviedatetime.setText("แจ้งเหตุ: " +
                getDateTime.changeDateFormatToCalendar(apiNoticeCase.getTbNoticeCase().ReceivingCaseDate) +
                " เวลา " + getDateTime.changeTimeFormatToDB(apiNoticeCase.getTbNoticeCase().ReceivingCaseTime) + " น.");

        if (apiNoticeCase.getTbNoticeCase().InvestigatorOfficialID != null || apiNoticeCase.getTbNoticeCase().InvestigatorOfficialID != "") {
            if (apiNoticeCase.getTbOfficial() == null) {
                csidataholder.inqInfo.setVisibility(View.GONE);
            } else {

                csidataholder.inqInfo.setVisibility(View.VISIBLE);
                csidataholder.inqInfo.setText(apiNoticeCase.getTbOfficial().Rank + " " + apiNoticeCase.getTbOfficial().FirstName + " " + apiNoticeCase.getTbOfficial().LastName
                        + " (" + apiNoticeCase.getTbOfficial().Position
                        + ") โทร. " + apiNoticeCase.getTbOfficial().PhoneNumber);
            }

        } else {
            csidataholder.inqInfo.setVisibility(View.GONE);
        }
        String SuffererPrename = "", SuffererName = "", SuffererPhoneNum = "";
        if (apiNoticeCase.getTbNoticeCase().SuffererName == null || apiNoticeCase.getTbNoticeCase().SuffererName.equals("")) {
            csidataholder.sufferrerInfo.setVisibility(View.GONE);
        } else {
            if (apiNoticeCase.getTbNoticeCase().SuffererPrename != null) {
                SuffererPrename = apiNoticeCase.getTbNoticeCase().SuffererPrename;
            }
            if (apiNoticeCase.getTbNoticeCase().SuffererName != null) {
                SuffererName = apiNoticeCase.getTbNoticeCase().SuffererName;
            }
            if (apiNoticeCase.getTbNoticeCase().SuffererPhoneNum != null) {
                SuffererPhoneNum = apiNoticeCase.getTbNoticeCase().SuffererPhoneNum;
            }
            csidataholder.sufferrerInfo.setText("ผู้เสียหาย" + SuffererPrename + " " + SuffererName + " โทร: " + SuffererPhoneNum);
        }

        // เปลี่ยนสีเส้นตาม CaseStatus ด้วยการเปลี่ยนรูปใหม่มาใส่แทนตัวเก่า
        String CaseStatus = apiNoticeCase.getTbNoticeCase().CaseStatus;
        if (CaseStatus.equalsIgnoreCase("receive")) {
            csidataholder.rel_status.setBackgroundColor(Color.parseColor("#c9302c"));
            csidataholder.txt_status.setText(R.string.edtStatus_1);
        } else if (CaseStatus.equalsIgnoreCase("notice")) {
            csidataholder.rel_status.setBackgroundColor(Color.parseColor("#ec971f"));
            csidataholder.txt_status.setText(R.string.edtStatus_2);
        } else if (CaseStatus.equalsIgnoreCase("assign")) {
            csidataholder.rel_status.setBackgroundColor(Color.parseColor("#449d44"));
            csidataholder.txt_status.setText(R.string.edtStatus_3);
        } else if (CaseStatus.equalsIgnoreCase("accept")) {
            csidataholder.rel_status.setBackgroundColor(Color.parseColor("#31b0d5"));
            csidataholder.txt_status.setText(R.string.edtStatus_4);
        } else if (CaseStatus.equalsIgnoreCase("investigating")) {
            csidataholder.rel_status.setBackgroundColor(Color.parseColor("#286090"));
            csidataholder.txt_status.setText(R.string.edtStatus_5);
        } else if (CaseStatus.equalsIgnoreCase("investigated")) {
            csidataholder.rel_status.setBackgroundColor(Color.parseColor("#9B26AF"));
            csidataholder.txt_status.setText(R.string.edtStatus_6);
        }else if (CaseStatus.equalsIgnoreCase("reported")) {
            csidataholder.rel_status.setBackgroundColor(Color.parseColor("#9D9D9D"));
            csidataholder.txt_status.setText(R.string.edtStatus_7);
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
