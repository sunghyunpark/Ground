package api.response;

import java.util.ArrayList;

import model.FreeArticleModel;

public class FreeArticleModelListResponse {
    private int code;
    private String message;
    private ArrayList<FreeArticleModel> result;

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

    public ArrayList<FreeArticleModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<FreeArticleModel> result) {
        this.result = result;
    }
}
