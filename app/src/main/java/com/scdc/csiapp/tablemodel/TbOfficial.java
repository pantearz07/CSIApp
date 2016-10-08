package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbOfficial implements Serializable {

    public String TB_Official = "official";

    // From Table official field name OfficialID
    public String COL_OfficialID = "OfficialID";
    public String OfficialID = "";

    // From Table official field name FirstName
    public String COL_FirstName = "FirstName";
    public String FirstName = "";

    // From Table official field name LastName
    public String COL_LastName = "LastName";
    public String LastName = "";

    // From Table official field name Alias
    public String COL_Alias = "Alias";
    public String Alias = "";

    // From Table official field name Rank
    public String COL_Rank = "Rank";
    public String Rank = "";

    // From Table official field name Position
    public String COL_Position = "Position";
    public String Position = "";

    // From Table official field name SubPossition
    public String COL_SubPossition = "SubPossition";
    public String SubPossition = "";

    // From Table official field name PhoneNumber
    public String COL_PhoneNumber = "PhoneNumber";
    public String PhoneNumber = "";

    // From Table official field name OfficialEmail
    public String COL_OfficialEmail = "OfficialEmail";
    public String OfficialEmail = "";

    // From Table official field name OfficialDisplayPic
    public String COL_OfficialDisplayPic = "OfficialDisplayPic";
    public String OfficialDisplayPic = "";

    // From Table official field name AccessType
    public String COL_AccessType = "AccessType";
    public String AccessType = "";

    // From Table official field name SCDCAgencyCode
    public String COL_SCDCAgencyCode = "SCDCAgencyCode";
    public String SCDCAgencyCode = "";

    // From Table official field name PoliceStationID
    public String COL_PoliceStationID = "PoliceStationID";
    public String PoliceStationID = "";

    // From Table official field name id_users
    public String COL_id_users = "id_users";
    public String id_users = "";

    public String getOfficialID() {
        return OfficialID;
    }

    public void setOfficialID(String officialID) {
        OfficialID = officialID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getSubPossition() {
        return SubPossition;
    }

    public void setSubPossition(String subPossition) {
        SubPossition = subPossition;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getOfficialEmail() {
        return OfficialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        OfficialEmail = officialEmail;
    }

    public String getOfficialDisplayPic() {
        return OfficialDisplayPic;
    }

    public void setOfficialDisplayPic(String officialDisplayPic) {
        OfficialDisplayPic = officialDisplayPic;
    }

    public String getAccessType() {
        return AccessType;
    }

    public void setAccessType(String accessType) {
        AccessType = accessType;
    }

    public String getSCDCAgencyCode() {
        return SCDCAgencyCode;
    }

    public void setSCDCAgencyCode(String SCDCAgencyCode) {
        this.SCDCAgencyCode = SCDCAgencyCode;
    }

    public String getPoliceStationID() {
        return PoliceStationID;
    }

    public void setPoliceStationID(String policeStationID) {
        PoliceStationID = policeStationID;
    }

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }
}
