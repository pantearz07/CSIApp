package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPhotoOfResultscene implements Serializable {
    public String TB_PhotoOfResultscene = "photoofresultscene";
    // From Table photoofresultscene field name RSID
    public String COL_RSID = "RSID";
    public String RSID = "";

    // From Table photoofresultscene field name FileID
    public String COL_FileID = "FileID";
    public String FileID = "";

    public String getRSID() {
        return RSID;
    }

    public void setRSID(String RSID) {
        this.RSID = RSID;
    }

    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }
}
