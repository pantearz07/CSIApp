package com.scdc.csiapp.apimodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbRegistrationGCM;
import com.scdc.csiapp.tablemodel.TbUsers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiProfile implements Parcelable {

    // ตารางหลัก
    TbOfficial tbOfficial;

    // ใช้ใน tbOfficial กรณี AccessType = investigator
    String SCDCAgencyCode;

    // ใช้ใน tbOfficial กรณี AccessType = inquiryofficial
    String PoliceStationID;

    // ข้อมูล Username, Password(MD5)
    TbUsers tbUsers;

    // Token ของ User ตามอุปกรณ์แต่ละชิ้น (ยังไม่ได้ใช้งานหรอก)
    public List<TbRegistrationGCM> tbRegistrationGCMList;

    public TbOfficial getTbOfficial() {
        return tbOfficial;
    }

    public void setTbOfficial(TbOfficial tbOfficial) {
        this.tbOfficial = tbOfficial;
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

    public TbUsers getTbUsers() {
        return tbUsers;
    }

    public void setTbUsers(TbUsers tbUsers) {
        this.tbUsers = tbUsers;
    }

    public List<TbRegistrationGCM> getTbRegistrationGCMList() {
        return tbRegistrationGCMList;
    }

    public void setTbRegistrationGCMList(List<TbRegistrationGCM> tbRegistrationGCMList) {
        this.tbRegistrationGCMList = tbRegistrationGCMList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.tbOfficial);
        dest.writeString(this.SCDCAgencyCode);
        dest.writeString(this.PoliceStationID);
        dest.writeSerializable(this.tbUsers);
        dest.writeList(this.tbRegistrationGCMList);
    }

    public ApiProfile() {
    }

    protected ApiProfile(Parcel in) {
        this.tbOfficial = (TbOfficial) in.readSerializable();
        this.SCDCAgencyCode = in.readString();
        this.PoliceStationID = in.readString();
        this.tbUsers = (TbUsers) in.readSerializable();
        this.tbRegistrationGCMList = new ArrayList<TbRegistrationGCM>();
        in.readList(this.tbRegistrationGCMList, TbRegistrationGCM.class.getClassLoader());
    }

    public static final Parcelable.Creator<ApiProfile> CREATOR = new Parcelable.Creator<ApiProfile>() {
        @Override
        public ApiProfile createFromParcel(Parcel source) {
            return new ApiProfile(source);
        }

        @Override
        public ApiProfile[] newArray(int size) {
            return new ApiProfile[size];
        }
    };
}
