package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class TbPolicePosition implements Serializable {
    public String TB_PolicePosition = "policeposition";
    // From Table policeposition field name PolicePosID
    public String COL_PolicePosID = "PolicePosID";
    public String PolicePosID = "";

    // From Table policeposition field name PoliceName
    public String COL_PoliceName = "PoliceName";
    public String PoliceName = "";
    // From Table policeposition field name PoliceAbbr
    public String COL_PoliceAbbr = "PoliceAbbr";
    public String PoliceAbbr = "";

}
