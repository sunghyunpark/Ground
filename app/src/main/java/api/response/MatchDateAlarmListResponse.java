package api.response;

import java.util.ArrayList;

import model.MatchingDateAlarmModel;

public class MatchDateAlarmListResponse {
    private int code;
    private String message;
    private ArrayList<MatchingDateAlarmModel> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<MatchingDateAlarmModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<MatchingDateAlarmModel> result) {
        this.result = result;
    }
}
