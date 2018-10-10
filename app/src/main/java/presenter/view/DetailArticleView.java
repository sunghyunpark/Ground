package presenter.view;

import base.presenter.view.BasePresenterView;
import model.MatchArticleModel;

public interface DetailArticleView extends BasePresenterView{

    void favoriteClick();

    void commentClick();

    void captureClick();

    void writeComment();

    void setArticleData(MatchArticleModel matchArticleModel);

    void loadArticleData(MatchArticleModel matchArticleModel);

    void setFavoriteState(int state);

    void initComment(boolean hasComment);

    void error();

}
