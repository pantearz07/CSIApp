package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPhotoOfOutside implements Serializable {

    public String TB_PhotoOfOutside = "photoofoutside";
    // From Table photoofoutside field name FileID
    public String COL_FileID = "FileID";
    public String FileID = "";
    // From Table photoofoutside field name CaseReportID
    public String COL_CaseReportID = "CaseReportID";
    public String CaseReportID = "";

    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }


}
