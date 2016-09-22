package com.scdc.csiapp.apimodel;

import java.util.List;

/**
 * Created by cbnuke on 9/19/16.
 */
public class ApiListCaseScene {

    private String status;

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

        private List<ApiCaseScene> result;

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

        public List<ApiCaseScene> getResult() {
            return result;
        }

        public void setResult(List<ApiCaseScene> result) {
            this.result = result;
        }
    }
}
