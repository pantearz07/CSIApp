package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbPoliceCenter;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncPoliceCenter {
    private String status;

    private List<TbPoliceCenter> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbPoliceCenter> getData() {
        return data;
    }

    public void setData(List<TbPoliceCenter> data) {
        this.data = data;
    }
}
