package com.scdc.csiapp.invmain;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scdc.csiapp.R;

import java.util.List;

/**
 * Created by Pantearz07 on 29/1/2559.
 */
public class NotelistsAdapter  extends RecyclerView.Adapter<NotelistsAdapter.CSINotelistsViewHolder>{
    List<CSINoteList> csiNoteList;
    OnItemClickListener mItemClickListener;
    NotelistsAdapter(List<CSINoteList> csiNoteList){
        this.csiNoteList = csiNoteList;
    }
    public class CSINotelistsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cvCSINote;
        TextView note_no;
        TextView csidatetime;
        public CSINotelistsViewHolder(View itemView) {
            super(itemView);
            cvCSINote= (CardView)itemView.findViewById(R.id.cvCSINote);
            csidatetime = (TextView)itemView.findViewById(R.id.csidatetime);
            note_no= (TextView)itemView.findViewById(R.id.note_no);
            cvCSINote.setOnClickListener(this);

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
    public CSINotelistsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_cardview, viewGroup, false);
        CSINotelistsViewHolder csivh = new CSINotelistsViewHolder(v);
        return csivh;
    }

    @Override
    public void onBindViewHolder(CSINotelistsViewHolder csiNotelistsholder, int position) {
        CSINoteList csidata = csiNoteList.get(position);
        csiNotelistsholder.note_no.setText(csidata.sNoteID);
        csiNotelistsholder.csidatetime.setText("แก้ไขเมื่อวันที่ " + csidata.timeStamp.substring(6,8)+"/"+csidata.timeStamp.substring(4,6)
                +"/"+csidata.timeStamp.substring(0,4)+" เวลา "+  csidata.timeStamp.substring(9,11)+":"+csidata.timeStamp.substring(11,13)+" น.");
//Str.substring(10, 15)
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return csiNoteList.size();
    }





}
