package api.response;

import java.util.ArrayList;

import model.CommunityModel;

public class CommunityModelListResponse {
    private int code;
    private String message;
    private ArrayList<CommunityModel> result;

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

    public ArrayList<CommunityModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<CommunityModel> result) {
        this.result = result;
    }
}
