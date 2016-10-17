package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbInvestigatorsInScene implements Serializable {
    public String TB_InvestigatorsInScene = "investigatorsinscene";
    // From Table investigatorsinscene field name CaseReportID
    public String COL_CaseReportID = "CaseReportID";
    public String CaseReportID = "";

    // From Table investigatorsinscene field name InvOfficialID
    public String COL_InvOfficialID = "InvOfficialID";
    public String InvOfficialID = "";

    // From Table investigatorsinscene field name InvType
    public String COL_InvType = "InvType";
    public String InvType = "";

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }

    public String getInvOfficialID() {
        return InvOfficialID;
    }

    public void setInvOfficialID(String invOfficialID) {
        InvOfficialID = invOfficialID;
    }

    public String getInvType() {
        return InvType;
    }

    public void setInvType(String invType) {
        InvType = invType;
    }
}
