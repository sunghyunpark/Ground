package api.response;

import java.util.ArrayList;

import model.ArticleModel;

public class AboutAreaBoardListResponse {
    private int code;
    private String message;
    private ArrayList<ArticleModel> result;

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

    public ArrayList<ArticleModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<ArticleModel> result) {
        this.result = result;
    }
}
