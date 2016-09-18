package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbInvPosition;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncInvPosition {
    private String status;

    private List<TbInvPosition> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbInvPosition> getData() {
        return data;
    }

    public void setData(List<TbInvPosition> data) {
        this.data = data;
    }
}
