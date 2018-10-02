package api.response;

import java.util.ArrayList;

import model.CommunityArticleModel;

public class FreeArticleModelListResponse {
    private int code;
    private String message;
    private ArrayList<CommunityArticleModel> result;

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

    public ArrayList<CommunityArticleModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<CommunityArticleModel> result) {
        this.result = result;
    }
}
