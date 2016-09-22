package com.scdc.csiapp.apimodel;

/**
 * Created by Pantearz07 on 12/9/2559.
 */
public class ApiLoginStatus {

    /**
     * status : success
     * data : {"action":"login","reason":"เข้าระบบสำเร็จ","result":{"users":{"id_users":"inq1","id_permission":"4","pass":"81dc9bdb52d04dc20036dbd8313ed055","id_system":"25","title":"พ.ต.ท.","name":"ภูมี","surname":"อีคะละ","position":"พงส.","picture":"","last_login":""},"official":{"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"}}}
     */

    private String status;
    /**
     * action : login
     * reason : เข้าระบบสำเร็จ
     * result : {"users":{"id_users":"inq1","id_permission":"4","pass":"81dc9bdb52d04dc20036dbd8313ed055","id_system":"25","title":"พ.ต.ท.","name":"ภูมี","surname":"อีคะละ","position":"พงส.","picture":"","last_login":""},"official":{"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"}}
     */

    private DataEntity data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public class DataEntity {
        private String action;
        private String reason;
        /**
         * users : {"id_users":"inq1","id_permission":"4","pass":"81dc9bdb52d04dc20036dbd8313ed055","id_system":"25","title":"พ.ต.ท.","name":"ภูมี","surname":"อีคะละ","position":"พงส.","picture":"","last_login":""}
         * official : {"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"}
         */

        private ResultEntity result;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public ResultEntity getResult() {
            return result;
        }

        public void setResult(ResultEntity result) {
            this.result = result;
        }

        public class ResultEntity {
            /**
             * id_users : inq1
             * id_permission : 4
             * pass : 81dc9bdb52d04dc20036dbd8313ed055
             * id_system : 25
             * title : พ.ต.ท.
             * name : ภูมี
             * surname : อีคะละ
             * position : พงส.
             * picture :
             * last_login :
             */

            private UsersEntity users;
            /**
             * OfficialID : INQ_PC04_1
             * FirstName : ภูมี
             * LastName : อีคะละ
             * Alias :
             * Rank : พ.ต.ท.
             * Position : พงส.
             * SubPossition :
             * PhoneNumber :
             * OfficialEmail :
             * OfficialDisplayPic :
             * AccessType : inquiryofficial
             * SCDCAgencyCode :
             * PoliceStationID : 40001
             * id_users : inq1
             */

            private OfficialEntity official;

            public UsersEntity getUsers() {
                return users;
            }

            public void setUsers(UsersEntity users) {
                this.users = users;
            }

            public OfficialEntity getOfficial() {
                return official;
            }

            public void setOfficial(OfficialEntity official) {
                this.official = official;
            }

            public class UsersEntity {
                private String id_users;
                private String id_permission;
                private String pass;
                private String id_system;
                private String title;
                private String name;
                private String surname;
                private String position;
                private String picture;
                private String last_login;

                public String getId_users() {
                    return id_users;
                }

                public void setId_users(String id_users) {
                    this.id_users = id_users;
                }

                public String getId_permission() {
                    return id_permission;
                }

                public void setId_permission(String id_permission) {
                    this.id_permission = id_permission;
                }

                public String getPass() {
                    return pass;
                }

                public void setPass(String pass) {
                    this.pass = pass;
                }

                public String getId_system() {
                    return id_system;
                }

                public void setId_system(String id_system) {
                    this.id_system = id_system;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSurname() {
                    return surname;
                }

                public void setSurname(String surname) {
                    this.surname = surname;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }

                public String getLast_login() {
                    return last_login;
                }

                public void setLast_login(String last_login) {
                    this.last_login = last_login;
                }
            }

            public class OfficialEntity {
                private String OfficialID;
                private String FirstName;
                private String LastName;
                private String Alias;
                private String Rank;
                private String Position;
                private String SubPossition;
                private String PhoneNumber;
                private String OfficialEmail;
                private String OfficialDisplayPic;
                private String AccessType;
                private String SCDCAgencyCode;
                private String PoliceStationID;
                private String id_users;

                public String getOfficialID() {
                    return OfficialID;
                }

                public void setOfficialID(String OfficialID) {
                    this.OfficialID = OfficialID;
                }

                public String getFirstName() {
                    return FirstName;
                }

                public void setFirstName(String FirstName) {
                    this.FirstName = FirstName;
                }

                public String getLastName() {
                    return LastName;
                }

                public void setLastName(String LastName) {
                    this.LastName = LastName;
                }

                public String getAlias() {
                    return Alias;
                }

                public void setAlias(String Alias) {
                    this.Alias = Alias;
                }

                public String getRank() {
                    return Rank;
                }

                public void setRank(String Rank) {
                    this.Rank = Rank;
                }

                public String getPosition() {
                    return Position;
                }

                public void setPosition(String Position) {
                    this.Position = Position;
                }

                public String getSubPossition() {
                    return SubPossition;
                }

                public void setSubPossition(String SubPossition) {
                    this.SubPossition = SubPossition;
                }

                public String getPhoneNumber() {
                    return PhoneNumber;
                }

                public void setPhoneNumber(String PhoneNumber) {
                    this.PhoneNumber = PhoneNumber;
                }

                public String getOfficialEmail() {
                    return OfficialEmail;
                }

                public void setOfficialEmail(String OfficialEmail) {
                    this.OfficialEmail = OfficialEmail;
                }

                public String getOfficialDisplayPic() {
                    return OfficialDisplayPic;
                }

                public void setOfficialDisplayPic(String OfficialDisplayPic) {
                    this.OfficialDisplayPic = OfficialDisplayPic;
                }

                public String getAccessType() {
                    return AccessType;
                }

                public void setAccessType(String AccessType) {
                    this.AccessType = AccessType;
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

                public void setPoliceStationID(String PoliceStationID) {
                    this.PoliceStationID = PoliceStationID;
                }

                public String getId_users() {
                    return id_users;
                }

                public void setId_users(String id_users) {
                    this.id_users = id_users;
                }
            }
        }
    }
}
