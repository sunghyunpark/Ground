package presenter.view;

import base.presenter.view.BasePresenterView;

public interface HomeView extends BasePresenterView{
    void setBannerPager();    // 상단 슬라이드 배너

    void setRecentArticlePager();    // 최신글

    void setGroundRecyclerView();    // 그라운드 유틸 가로 리사이클러뷰

}
