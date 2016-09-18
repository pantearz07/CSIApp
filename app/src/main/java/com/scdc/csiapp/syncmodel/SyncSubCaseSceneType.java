package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncSubCaseSceneType {
    private String status;

    private List<TbSubcaseSceneType> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbSubcaseSceneType> getData() {
        return data;
    }

    public void setData(List<TbSubcaseSceneType> data) {
        this.data = data;
    }
}