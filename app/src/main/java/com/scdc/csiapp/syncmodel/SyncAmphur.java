package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbAmphur;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncAmphur {
    private String status;

    private List<TbAmphur> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbAmphur> getData() {
        return data;
    }

    public void setData(List<TbAmphur> data) {
        this.data = data;
    }
}
