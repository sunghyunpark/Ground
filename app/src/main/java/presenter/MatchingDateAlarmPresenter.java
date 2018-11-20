package presenter;

import android.content.Context;

import base.presenter.BasePresenter;
import presenter.view.MatchingDateAlarmView;

public class MatchingDateAlarmPresenter extends BasePresenter<MatchingDateAlarmView> {

    private Context context;

    public MatchingDateAlarmPresenter(MatchingDateAlarmView view, Context context){
        super(view);
    }
}
