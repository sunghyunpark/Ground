package api.response;

import java.util.ArrayList;

import model.MatchArticleModel;

/**
 * Article Model List Response
 */
public class ArticleModelListResponse {
    private int code;
    private String message;
    private ArrayList<MatchArticleModel> result;

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

    public ArrayList<MatchArticleModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<MatchArticleModel> result) {
        this.result = result;
    }
}
