package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbGeography;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncGeography {
    private String status;

    private List<TbGeography> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbGeography> getData() {
        return data;
    }

    public void setData(List<TbGeography> data) {
        this.data = data;
    }
}
