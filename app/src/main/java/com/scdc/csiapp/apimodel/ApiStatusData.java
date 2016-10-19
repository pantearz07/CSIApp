package com.scdc.csiapp.apimodel;

/**
 * Created by Pantearz07 on 18/10/2559.
 */

public class ApiStatusData {

    /**
     * status : success
     * data : {"action":"saveCaseReport","reason":"บันทึกข้อมูลเรียบร้อยแล้ว"}
     */

    private String status;
    /**
     * action : saveCaseReport
     * reason : บันทึกข้อมูลเรียบร้อยแล้ว
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String action;
        private String reason;

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
    }
}
