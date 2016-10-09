package com.scdc.csiapp.syncmodel;

import com.scdc.csiapp.tablemodel.TbEvidenceType;

import java.util.List;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class SyncEvidenceType {
    private String status;

    private List<TbEvidenceType> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TbEvidenceType> getData() {
        return data;
    }

    public void setData(List<TbEvidenceType> data) {
        this.data = data;
    }
}
