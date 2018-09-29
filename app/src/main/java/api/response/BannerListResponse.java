package api.response;

import java.util.ArrayList;

import model.BannerModel;

public class BannerListResponse {
    private int code;
    private String message;
    private ArrayList<BannerModel> mainBanner;    // 상단 슬라이드 배너
    private BannerModel RBBanner;    // 최신글 하단 띠 배너
    private BannerModel TBBanner;    // 오늘의 시합 하단 띠 배너

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

    public ArrayList<BannerModel> getMainBanner() {
        return mainBanner;
    }

    public void setMainBanner(ArrayList<BannerModel> mainBanner) {
        this.mainBanner = mainBanner;
    }

    public BannerModel getRBBanner() {
        return RBBanner;
    }

    public void setRBBanner(BannerModel RBBanner) {
        this.RBBanner = RBBanner;
    }

    public BannerModel getTBBanner() {
        return TBBanner;
    }

    public void setTBBanner(BannerModel TBBanner) {
        this.TBBanner = TBBanner;
    }
}
