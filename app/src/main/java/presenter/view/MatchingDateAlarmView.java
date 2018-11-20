package presenter.view;

import base.presenter.view.BasePresenterView;

public interface MatchingDateAlarmView extends BasePresenterView {

    void loadComplete(boolean error);

    void registerComplete(boolean error);    // 서버 통신 후 error 여부를 반환한다.
}
