package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPhotoOfEvidence implements Serializable {
    public String TB_PhotoOfEvidence = "photoofevidence";
    // From Table photoofevidence field name FileID
    public String COL_FileID = "FileID";
    public String FileID = "";
    // From Table photoofevidence field name FindEvidenceID
    public String COL_FindEvidenceID = "FindEvidenceID";
    public String FindEvidenceID = "";


    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }

    public String getFindEvidenceID() {
        return FindEvidenceID;
    }

    public void setFindEvidenceID(String findEvidenceID) {
        FindEvidenceID = findEvidenceID;
    }

}
