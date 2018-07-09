package presenter;

import android.content.Context;

import base.presenter.BasePresenter;
import presenter.view.HomeView;

public class HomePresenter extends BasePresenter<HomeView> {

    private Context context;

    public HomePresenter(HomeView view, Context context){
        super(view);
        this.context = context;
    }


}
