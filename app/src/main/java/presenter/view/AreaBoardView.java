package presenter.view;

import android.support.v7.widget.LinearLayoutManager;

import base.presenter.view.BasePresenterView;

public interface AreaBoardView extends BasePresenterView{
    void onWriteClick();

    void loadMoreArticle(LinearLayoutManager linearLayoutManager);
}
