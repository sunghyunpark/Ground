package presenter.view;

import base.presenter.view.BasePresenterView;
import model.CommunityModel;

public interface DetailCommunityView extends BasePresenterView {
    void favoriteClick();

    void commentClick();

    void captureClick();

    void writeComment();

    void setArticleData(CommunityModel communityModel);

    void loadArticleData(CommunityModel communityModel);

    void setFavoriteState(int state);

    void initComment(boolean hasComment);

}
