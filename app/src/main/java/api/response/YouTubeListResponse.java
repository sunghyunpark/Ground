package api.response;

import java.util.ArrayList;

import model.YouTubeModel;

public class YouTubeListResponse {
    private int code;
    private String message;
    private String state;
    private ArrayList<YouTubeModel> youtubeList;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<YouTubeModel> getYoutubeList() {
        return youtubeList;
    }

    public void setYoutubeList(ArrayList<YouTubeModel> youtubeList) {
        this.youtubeList = youtubeList;
    }
}
