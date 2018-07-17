package presenter;

import base.presenter.BasePresenter;
import presenter.view.MyView;

public class MyPresenter extends BasePresenter<MyView>{

    public MyPresenter(MyView view){
        super(view);
    }
}
