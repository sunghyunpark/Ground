package presenter.view;

import base.presenter.view.BasePresenterView;

public interface DetailArticleView extends BasePresenterView{

    void onFavoriteClick();

    void onCommentClick();

    void onShareClick();

    void onWriteComment();

}
