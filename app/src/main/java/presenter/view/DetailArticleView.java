package presenter.view;

import base.presenter.view.BasePresenterView;
import model.ArticleModel;

public interface DetailArticleView extends BasePresenterView{

    void favoriteClick();

    void commentClick();

    void shareClick();

    void writeComment();

    void setArticleData(ArticleModel articleModel);

    void loadArticleData(ArticleModel articleModel);

    void setFavoriteState(int state);

    void initComment(boolean hasComment);

    void error();

}
