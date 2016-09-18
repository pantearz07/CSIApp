package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbPoliceRank;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncPoliceRank {
    private String status;

    private List<TbPoliceRank> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbPoliceRank> getData() {
        return data;
    }

    public void setData(List<TbPoliceRank> data) {
        this.data = data;
    }
}
