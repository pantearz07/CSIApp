package com.scdc.csiapp.apimodel;

import java.util.List;

/**
 * Created by Pantearz07 on 22/10/2559.
 */

public class ApiListScheduleInvestigates
{

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

        private List<ApiScheduleInvestigates> result;

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

        public List<ApiScheduleInvestigates> getResult() {
            return result;
        }

        public void setResult(List<ApiScheduleInvestigates> result) {
            this.result = result;
        }
    }

}
