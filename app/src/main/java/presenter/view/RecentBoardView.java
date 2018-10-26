package presenter.view;

import base.presenter.view.BasePresenterView;

public interface RecentBoardView extends BasePresenterView{
    void setListData(boolean refresh, int articleNo);

    void notifyRecentArticle(boolean hasData, int listSize);
}
