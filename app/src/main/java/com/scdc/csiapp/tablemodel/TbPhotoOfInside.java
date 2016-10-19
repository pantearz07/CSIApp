package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPhotoOfInside implements Serializable {
    // From Table photoofinside field name FileID
    public String FileID = "";

    // From Table photoofinside field name FeatureInsideID
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
