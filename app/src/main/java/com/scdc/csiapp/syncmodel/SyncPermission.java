package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbPermission;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncPermission {
    private String status;

    private List<TbPermission> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbPermission> getData() {
        return data;
    }

    public void setData(List<TbPermission> data) {
        this.data = data;
    }
}
