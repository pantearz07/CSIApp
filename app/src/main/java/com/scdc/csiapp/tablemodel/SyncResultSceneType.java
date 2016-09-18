package com.scdc.csiapp.tablemodel;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncResultSceneType {
    private String status;

    private List<TbResultSceneType> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbResultSceneType> getData() {
        return data;
    }

    public void setData(List<TbResultSceneType> data) {
        this.data = data;
    }
}
