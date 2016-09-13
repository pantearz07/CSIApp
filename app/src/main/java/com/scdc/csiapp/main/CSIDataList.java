package com.scdc.csiapp.main;

/**
 * Created by Pantearz07 on 15/10/2558.
 */
public class CSIDataList {
    String caseReportID;
    String typeCase;

    String LocaleName;
    String HouseNo;
    String VillageNo;
    String VillageName;
    String LaneName;
    String RoadName;
    String District;
    String Amphur;
    String Province;
    String policeStation;
    String datereceivie;
    String timereceivie;

    String OfficialID;

    CSIDataList(    String caseReportID,
            String typeCase,
                    String LocaleName,
                            String HouseNo,
                            String VillageNo,
                            String VillageName,
                            String LaneName,
                            String RoadName,
                            String District,
                            String Amphur,
                            String Province,
            String policeStation,
                    String datereceivie,
                    String timereceivie,
            String OfficialID

           ) {
        this.caseReportID= caseReportID;
        this.typeCase = typeCase;
        this.LocaleName = LocaleName;
        this.HouseNo = HouseNo;
        this.VillageNo = VillageNo;
        this.VillageName = VillageName;
        this.LocaleName = LocaleName;
        this.LaneName = LaneName;
        this.RoadName = RoadName;
        this.District = District;
        this.Amphur = Amphur;
        this.Province = Province;
        this.policeStation = policeStation;
        this.OfficialID = OfficialID;
        this.datereceivie = datereceivie;
        this.timereceivie = timereceivie;
    }
}
