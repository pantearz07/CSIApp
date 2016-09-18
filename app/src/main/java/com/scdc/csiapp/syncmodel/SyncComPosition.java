package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbComPosition;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncComPosition {
    private String status;

    private List<TbComPosition> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbComPosition> getData() {
        return data;
    }

    public void setData(List<TbComPosition> data) {
        this.data = data;
    }
}
