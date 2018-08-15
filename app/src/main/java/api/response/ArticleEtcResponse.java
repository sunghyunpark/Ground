package api.response;

public class ArticleEtcResponse {
    private int code;
    private String message;
    private int favoriteState;    //0:not like, 1: like

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

    public int getFavoriteState() {
        return favoriteState;
    }

    public void setFavoriteState(int favoriteState) {
        this.favoriteState = favoriteState;
    }
}
