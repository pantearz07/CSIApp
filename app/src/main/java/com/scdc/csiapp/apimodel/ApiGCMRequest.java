package com.scdc.csiapp.apimodel;

/**
 * Created by Pantearz07 on 14/9/2559.
 */
public class ApiGCMRequest {
    private String Registration_id = "";
    private String RegisOfficialID ="";
    private String Username = "";
    private String Password ="";

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRegistration_id() {
        return Registration_id;
    }

    public void setRegistration_id(String registration_id) {
        Registration_id = registration_id;
    }

    public String getRegisOfficialID() {
        return RegisOfficialID;
    }

    public void setRegisOfficialID(String regisOfficialID) {
        RegisOfficialID = regisOfficialID;
    }
}
