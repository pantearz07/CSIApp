package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbPoliceAgency;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncPoliceAgency {
    private String status;

    private List<TbPoliceAgency> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbPoliceAgency> getData() {
        return data;
    }

    public void setData(List<TbPoliceAgency> data) {
        this.data = data;
    }
}
