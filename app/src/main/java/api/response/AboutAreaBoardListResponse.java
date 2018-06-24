package api.response;

import java.util.ArrayList;

import model.BoardModel;

public class AboutAreaBoardListResponse {
    private int code;
    private String message;
    private ArrayList<BoardModel> result;

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

    public ArrayList<BoardModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<BoardModel> result) {
        this.result = result;
    }
}
