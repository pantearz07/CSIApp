package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbPoliceStation;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncPoliceStation {
    private String status;

    private List<TbPoliceStation> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbPoliceStation> getData() {
        return data;
    }

    public void setData(List<TbPoliceStation> data) {
        this.data = data;
    }
}
