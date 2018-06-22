package api.response;

import model.UserModel;

public class LoginResponse {
    private int code;
    private String message;
    private UserModel result;

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

    public UserModel getResult() {
        return result;
    }

    public void setResult(UserModel result) {
        this.result = result;
    }

}
