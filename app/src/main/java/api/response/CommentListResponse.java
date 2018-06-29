package api.response;

import java.util.ArrayList;

import model.CommentModel;

public class CommentListResponse {
    private int code;
    private String message;
    private ArrayList<CommentModel> result;

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

    public ArrayList<CommentModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<CommentModel> result) {
        this.result = result;
    }

}
