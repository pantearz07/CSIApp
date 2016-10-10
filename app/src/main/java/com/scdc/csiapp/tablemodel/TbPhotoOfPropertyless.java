package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPhotoOfPropertyless implements Serializable {
    public String TB_PhotoOfPropertyless = "photoofpropertyless";
    // From Table photoofpropertyless field name PropertyLessID
    public String COL_PropertyLessID = "PropertyLessID";
    public String PropertyLessID = "";

    // From Table photoofpropertyless field name FileID
    public String COL_FileID = "FileID";
    public String FileID = "";

    public String getPropertyLessID() {
        return PropertyLessID;
    }

    public void setPropertyLessID(String propertyLessID) {
        PropertyLessID = propertyLessID;
    }

    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }
}
