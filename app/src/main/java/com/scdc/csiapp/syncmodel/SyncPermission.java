package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbOfficial;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncPermission {
    private String status;

    private List<TbOfficial> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbOfficial> getData() {
        return data;
    }

    public void setData(List<TbOfficial> data) {
        this.data = data;
    }
}
