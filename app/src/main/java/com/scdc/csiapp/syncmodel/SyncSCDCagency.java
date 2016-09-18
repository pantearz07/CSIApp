package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbSCDCagency;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncSCDCAgency {
    private String status;

    private List<TbSCDCagency> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbSCDCagency> getData() {
        return data;
    }

    public void setData(List<TbSCDCagency> data) {
        this.data = data;
    }
}
