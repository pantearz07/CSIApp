package com.scdc.csiapp.main;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scdc.csiapp.R;

import java.util.List;

/**
 * Created by Pantearz07 on 15/10/2558.
 */
public class ReceivingCaseAdapter extends RecyclerView.Adapter<ReceivingCaseAdapter.ReceivingCaseViewHolder>{

    List<ReceivingCaseList> receivigCaseLists;
    OnItemClickListener mItemClickListener;
    ReceivingCaseAdapter(List<ReceivingCaseList> receivigCaseLists){
        this.receivigCaseLists = receivigCaseLists;
    }
    public class ReceivingCaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cvCSI;
        TextView positioncase;
        TextView policeStation;
        TextView typeCase;
        TextView inqInfo;
        TextView sufferrerInfo;
        TextView receiviedatetime;
        ImageView ic_CaseType;
        public ReceivingCaseViewHolder(View itemView) {
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
    public ReceivingCaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draft_cardview, viewGroup, false);
        ReceivingCaseViewHolder csivh = new ReceivingCaseViewHolder(v);
        return csivh;
    }

    @Override
    public void onBindViewHolder(ReceivingCaseViewHolder csidataholder, int position) {
         ReceivingCaseList csidata = receivigCaseLists.get(position);
        String casereportid = csidata.caseReportID;

        csidataholder.sufferrerInfo.setVisibility(View.GONE);

        csidataholder.typeCase.setText("ประเภทคดี: " + csidata.typeCase);
        String positioncase = csidata.LocaleName+" "+csidata.HouseNo+" "+csidata.VillageNo+" "+csidata.VillageName
                +" "+csidata.LaneName+" "+csidata.RoadName+" "+csidata.District+" "+csidata.Amphur+" "+csidata.Province;
        csidataholder.inqInfo.setVisibility(View.VISIBLE);
        csidataholder.positioncase.setText("เกิดที่: " + positioncase);
        csidataholder.inqInfo.setText("พงส." + csidata.Rank + " " + csidata.FirstName + " " + csidata.LastName + " "
                + "(" + csidata.AreaCodeTel + ")-" + csidata.PhoneNumber);
        if (csidata.policeStation == null) {
            csidataholder.policeStation.setText("สภ.");

        } else {
            csidataholder.policeStation.setText("สภ. " + csidata.policeStation);

        }csidataholder.receiviedatetime.setText("แจ้งเหตุ: " + csidata.datereceivie + " เวลา " + csidata.timereceivie + " น.");

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
       return receivigCaseLists.size();
        //return (null != receivigCaseLists ? receivigCaseLists.size() : 0);

    }
/*/ Clean all elements of the recycler

    public void clear() {
        Log.i("log_show","claerlist");
        receivigCaseLists.clear();

        notifyDataSetChanged();

    }*/



// Add a list of items

    public void addAll(List<ReceivingCaseList> list) {

        receivigCaseLists.addAll(list);

        notifyDataSetChanged();

    }


}
