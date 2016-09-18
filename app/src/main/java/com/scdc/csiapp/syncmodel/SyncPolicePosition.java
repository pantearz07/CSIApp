package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbPolicePosition;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncPolicePosition {
    private String status;

    private List<TbPolicePosition> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbPolicePosition> getData() {
        return data;
    }

    public void setData(List<TbPolicePosition> data) {
        this.data = data;
    }
}
