package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class TbInvPosition implements Serializable {
    public String TB_InvPosition = "invposition";
    // From Table invposition field name InvPosID
    public String COL_InvPosID = "InvPosID";
    public String InvPosID = "";

    // From Table invposition field name InvPosName
    public String COL_InvPosName = "InvPosName";
    public String InvPosName = "";

    // From Table invposition field name InvPosAbbr
    public String COL_InvPosAbbr = "InvPosAbbr";
    public String InvPosAbbr = "";
}