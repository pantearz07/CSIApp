package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbResultScene implements Serializable {
    public String TB_ResultScene = "resultscene";
    // From Table resultscene field name RSID
    public String COL_RSID = "RSID";
    public String RSID = "";

    // From Table resultscene field name RSTypeID
    public String COL_RSTypeID = "RSTypeID";
    public String RSTypeID = "";

    // From Table resultscene field name CaseReportID
    public String COL_CaseReportID = "CaseReportID";
    public String CaseReportID = "";

    // From Table resultscene field name RSDetail
    public String COL_RSDetail = "RSDetail";
    public String RSDetail = "";

    public String getRSID() {
        return RSID;
    }

    public void setRSID(String RSID) {
        this.RSID = RSID;
    }

    public String getRSTypeID() {
        return RSTypeID;
    }

    public void setRSTypeID(String RSTypeID) {
        this.RSTypeID = RSTypeID;
    }

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }

    public String getRSDetail() {
        return RSDetail;
    }

    public void setRSDetail(String RSDetail) {
        this.RSDetail = RSDetail;
    }
}
