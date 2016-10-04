package com.scdc.csiapp.apimodel;

/**
 * Created by Pantearz07 on 1/10/2559.
 */

public class ApiStatusResult {

    /**
     * status : fail
     * data : {"action":"save_noticecase","reason":"ไม่สามารถบันทึกข้อมูลได้","result":""}
     */

    private String status;
    /**
     * action : save_noticecase
     * reason : ไม่สามารถบันทึกข้อมูลได้
     * result :
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
        private String result;

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

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
