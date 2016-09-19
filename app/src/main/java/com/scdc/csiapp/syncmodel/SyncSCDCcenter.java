package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbSCDCcenter;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncSCDCcenter {
    private String status;

    private List<TbSCDCcenter> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbSCDCcenter> getData() {
        return data;
    }

    public void setData(List<TbSCDCcenter> data) {
        this.data = data;
    }

}
