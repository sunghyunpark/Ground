package base.presenter;

import base.presenter.view.BasePresenterView;

public class BasePresenter<V extends BasePresenterView> {

    private V view;

    public BasePresenter(V view) {
        this.view = view;
    }

    protected V getView() {
        return view;
    }
}
