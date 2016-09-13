package com.scdc.csiapp.policetabs;

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
 * Created by Pantearz07 on 13/10/2558.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    List<Person> persons;
    OnItemClickListener mItemClickListener;
    RVAdapter(List<Person> persons){
        this.persons = persons;
    }
    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView personName;
        TextView personPosition;

        TextView personTel;
        TextView personAgencyname;
        TextView personStationname;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personTel = (TextView)itemView.findViewById(R.id.person_tel);
            personPosition = (TextView)itemView.findViewById(R.id.person_position);
            personAgencyname = (TextView)itemView.findViewById(R.id.person_agencyname);
            personStationname =  (TextView)itemView.findViewById(R.id.person_stationname);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            cv.setOnClickListener(this);
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
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layoutcardview, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        Person ps =persons.get(i);

        personViewHolder.personName.setText(ps.rank+" "+ps.name);
        personViewHolder.personPosition.setText(ps.position);
        personViewHolder.personTel.setText("เบอร์โทร "+ps.tel);
        personViewHolder.personAgencyname.setText(ps.agencyname);
        personViewHolder.personStationname.setText(ps.stationname);
        personViewHolder.personPhoto.setImageResource(ps.photoId);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return persons.size();
    }


}