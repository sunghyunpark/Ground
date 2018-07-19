package presenter.view;

import base.presenter.view.BasePresenterView;
import model.ArticleModel;

public interface DetailArticleView extends BasePresenterView{

    void favoriteClick(int state);

    void commentClick();

    void shareClick();

    void writeComment();

    void setArticleData(ArticleModel articleModel);

    void initComment(boolean hasComment);

}
