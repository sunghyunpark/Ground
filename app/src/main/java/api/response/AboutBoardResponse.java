package api.response;

import model.BoardModel;

public class AboutBoardResponse {
    private int code;
    private String message;
    private BoardModel result;

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

    public BoardModel getResult() {
        return result;
    }

    public void setResult(BoardModel result) {
        this.result = result;
    }
}
