package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbProvince;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncProvince {
    private String status;

    private List<TbProvince> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbProvince> getData() {
        return data;
    }

    public void setData(List<TbProvince> data) {
        this.data = data;
    }
}
