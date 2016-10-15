package com.scdc.csiapp.policetabs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiOfficial;
import com.scdc.csiapp.connecting.SQLiteDBHelper;

import java.util.List;

/**
 * Created by Pantearz07 on 14/10/2559.
 */

public class OfficialListAdapter extends RecyclerView.Adapter<OfficialListAdapter.OfficialViewHolder> {
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    Cursor mCursor;
    List<ApiOfficial> apiOfficialList;
    OnItemClickListener mItemClickListener;

    OfficialListAdapter(List<ApiOfficial> apiOfficialList) {
        this.apiOfficialList = apiOfficialList;
    }

    public class OfficialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        CardView cv;
        TextView personName;
        TextView personPosition;

        TextView personTel;
        TextView personAgencyname;
        TextView personStationname;
        ImageView personPhoto;
        ImageView iv_mode;

        public OfficialViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personTel = (TextView) itemView.findViewById(R.id.person_tel);
            personPosition = (TextView) itemView.findViewById(R.id.person_position);
            personAgencyname = (TextView) itemView.findViewById(R.id.person_agencyname);
            personStationname = (TextView) itemView.findViewById(R.id.person_stationname);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            iv_mode = (ImageView) itemView.findViewById(R.id.iv_mode);

            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
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
    public OfficialViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layoutcardview, viewGroup, false);
        OfficialViewHolder officialvh = new OfficialViewHolder(v);
        return officialvh;
    }

    @Override
    public void onBindViewHolder(final OfficialViewHolder OfficialViewHolder, int position) {
        ApiOfficial apiOfficial = apiOfficialList.get(position);

        if (apiOfficial.getMode() != null && apiOfficial.getMode().equalsIgnoreCase("online")) {
            OfficialViewHolder.iv_mode.setImageResource(R.drawable.ic_router_black_24dp);
        } else if (apiOfficial.getMode() != null && apiOfficial.getMode().equalsIgnoreCase("offline")) {
            OfficialViewHolder.iv_mode.setImageResource(R.drawable.ic_phone_android_black_24dp);
        } else {
            OfficialViewHolder.iv_mode.setImageResource(R.drawable.ic_help_black_24dp);
        }
        if (apiOfficial.getTbOfficial() != null) {
            OfficialViewHolder.personName.setText(apiOfficial.getTbOfficial().Rank + " " + apiOfficial.getTbOfficial().FirstName + " " + apiOfficial.getTbOfficial().LastName);

            OfficialViewHolder.personPosition.setText(apiOfficial.getTbOfficial().Position);
            OfficialViewHolder.personTel.setText("เบอร์โทร " + apiOfficial.getTbOfficial().PhoneNumber);

            if (apiOfficial.getTbOfficial().AccessType.equalsIgnoreCase("investigator") || apiOfficial.getTbOfficial().AccessType.equalsIgnoreCase("investigator2")) {
                OfficialViewHolder.personAgencyname.setText(apiOfficial.getTbSCDCagency().SCDCAgencyName);
                OfficialViewHolder.personStationname.setVisibility(View.GONE);
            } else {
                OfficialViewHolder.personStationname.setText("สภ. "+apiOfficial.getTbPoliceStation().PoliceStationName);
                OfficialViewHolder.personAgencyname.setVisibility(View.GONE);
            }
        }


//        OfficialViewHolder.personPhoto.setImageResource(apiOfficial.getTbOfficial().OfficialDisplayPic);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return apiOfficialList.size();
    }


}
