package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPhotoOfPropertyless implements Serializable {
    // From Table photoofpropertyless field name FileID
    public String FileID = "";
    // From Table photoofpropertyless field name PropertyLessID
    public String PropertyLessID = "";


    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }
    public String getPropertyLessID() {
        return PropertyLessID;
    }

    public void setPropertyLessID(String propertyLessID) {
        PropertyLessID = propertyLessID;
    }

}
