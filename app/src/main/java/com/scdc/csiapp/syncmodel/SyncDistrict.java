package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbDistrict;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncDistrict {
    private String status;

    private List<TbDistrict> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbDistrict> getData() {
        return data;
    }

    public void setData(List<TbDistrict> data) {
        this.data = data;
    }
}
