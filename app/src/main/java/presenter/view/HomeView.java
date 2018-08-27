package presenter.view;

import base.presenter.view.BasePresenterView;

public interface HomeView extends BasePresenterView{
    void setBannerPager();    // 상단 슬라이드 배너

    void setRecentArticlePager();    // 최신글

    void setGroundRecyclerView();    // 그라운드 유틸 가로 리사이클러뷰

    void setTodayMatchBoard();    // 오늘의 시합

    void notifyTodayMatchArticle(boolean hasData, int listSize);    // 오늘의 시합 데이터를 받은 뒤 갱신한다.

}
