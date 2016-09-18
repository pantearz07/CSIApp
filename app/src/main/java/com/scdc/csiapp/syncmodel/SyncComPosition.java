package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbCaseSceneType;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncComPosition {
    private String status;

    private List<TbCaseSceneType> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbCaseSceneType> getData() {
        return data;
    }

    public void setData(List<TbCaseSceneType> data) {
        this.data = data;
    }
}
