package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbMultimediaFile implements Serializable {
    public String TB_MultimediaFile = "multimediafile";
    // From Table multimediafile field name FileID
    public String COL_FileID = "FileID";
    public String FileID = "";

    // From Table multimediafile field name CaseReportID
    public String COL_CaseReportID = "CaseReportID";
    public String CaseReportID = "";

    // From Table multimediafile field name FileType
    public String COL_FileType = "FileType";
    public String FileType = "";

    // From Table multimediafile field name FilePath
    public String COL_FilePath = "FilePath";
    public String FilePath = "";

    // From Table multimediafile field name FileDescription
    public String COL_FileDescription = "FileDescription";
    public String FileDescription = "";

    // From Table multimediafile field name Timestamp
    public String COL_Timestamp = "Timestamp";
    public String Timestamp = "";

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getFileDescription() {
        return FileDescription;
    }

    public void setFileDescription(String fileDescription) {
        FileDescription = fileDescription;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }

    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }
}
