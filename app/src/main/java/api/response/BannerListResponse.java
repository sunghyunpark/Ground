package api.response;

import java.util.ArrayList;

import model.BannerModel;

public class BannerListResponse {
    private int code;
    private String message;
    private ArrayList<BannerModel> result;

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

    public ArrayList<BannerModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<BannerModel> result) {
        this.result = result;
    }
}
