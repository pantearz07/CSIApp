package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 18/9/2559.
 */
public class TbPoliceRank implements Serializable {

    public String TB_PoliceRank = "policerank";

    // From Table policerank field name RankID
    public String COL_RankID = "RankID";
    public String RankID = "";

    // From Table policerank field name rankname
    public String COL_RankName = "RankName";
    public String RankName = "";

    // From Table policerank field name RankAbbr
    public String COL_RankAbbr = "RankAbbr";
    public String RankAbbr = "";
}
