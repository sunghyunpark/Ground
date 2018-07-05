package api.response;

import java.util.ArrayList;

import model.AreaModel;

public class UpdateTimeResponse {
    private int code;
    private String message;
    private ArrayList<AreaModel> result;

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

    public ArrayList<AreaModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<AreaModel> result) {
        this.result = result;
    }
}
