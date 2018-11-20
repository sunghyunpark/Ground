package presenter.view;

import base.presenter.view.BasePresenterView;

public interface MatchDateAlarmView extends BasePresenterView {

    void loadComplete(boolean error);    // 서버 통신 후 데이터를 받아와 error 여부를 반환한다.

    void registerComplete(boolean error);    // 서버 통신 후 error 여부를 반환한다.

    void unregisterComplete(boolean error);    // 서버 통신 후 error 여부를 반환한다.
}
