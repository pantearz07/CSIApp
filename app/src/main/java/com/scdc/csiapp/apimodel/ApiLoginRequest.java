package com.scdc.csiapp.apimodel;

/**
 * Created by Pantearz07 on 12/9/2559.
 */
public class ApiLoginRequest {
    private String Username = "";
    private String Password ="";

    public ApiLoginRequest(String Username, String Password) {
        this.Username = Username;
        this.Password = Password;
    }

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
}
