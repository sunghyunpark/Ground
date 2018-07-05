package presenter.view;

import base.presenter.view.BasePresenterView;

public interface CommentView extends BasePresenterView{

    void writeComment();

    void initComment(boolean hasComment);

    void loadMoreComment();

}
