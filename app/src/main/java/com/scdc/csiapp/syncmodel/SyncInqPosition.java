package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbInqPosition;

import java.util.List;

/**
 * Created by Amnart on 18/9/2559.
 */
public class SyncInqPosition {
    private String status;

    private List<TbInqPosition> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbInqPosition> getData() {
        return data;
    }

    public void setData(List<TbInqPosition> data) {
        this.data = data;
    }

}
