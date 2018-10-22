package presenter.view;

import java.util.ArrayList;

import base.presenter.view.BasePresenterView;
import model.BannerModel;

public interface HomeView extends BasePresenterView{
    void setBanner(ArrayList<BannerModel> bannerModelArrayList, BannerModel RBBanner, BannerModel TBBanner);    // 상단 슬라이드 배너, 띠 배너(최신글 하단, 오늘의 시합 하단)

    void setRecentArticlePager();    // 최신글

    void setGroundRecyclerView();    // 그라운드 유틸 가로 리사이클러뷰

    void setTodayMatchBoard();    // 오늘의 시합

    void notifyTodayMatchArticle(boolean hasData, int listSize);    // 오늘의 시합 데이터를 받은 뒤 갱신한다.

    void setRecommendYouTube(String state);    // 이런 영상은 어때요? state > on / off

}
