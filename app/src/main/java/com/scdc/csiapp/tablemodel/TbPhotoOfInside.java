package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPhotoOfInside implements Serializable {
    public String TB_PhotoOfInside = "photoofinside";
    // From Table photoofinside field name FileID
    public String COL_FileID = "FileID";
    public String FileID = "";

    // From Table photoofinside field name FeatureInsideID
    public String COL_FeatureInsideID = "FeatureInsideID";
    public String FeatureInsideID = "";

    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }

    public String getFeatureInsideID() {
        return FeatureInsideID;
    }

    public void setFeatureInsideID(String featureInsideID) {
        FeatureInsideID = featureInsideID;
    }
}
