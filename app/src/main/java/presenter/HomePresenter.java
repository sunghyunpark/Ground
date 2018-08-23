package presenter;

import android.content.Context;
import android.util.Log;

import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import base.presenter.BasePresenter;
import model.ArticleModel;
import presenter.view.HomeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class HomePresenter extends BasePresenter<HomeView> {

    private Context context;
    private ApiInterface apiService;

    public HomePresenter(HomeView view, Context context){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public void loadTodayMatchList(){

    }

}
