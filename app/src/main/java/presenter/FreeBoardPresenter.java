package presenter;

import android.content.Context;

import base.presenter.BasePresenter;
import presenter.view.FreeBoardView;

public class FreeBoardPresenter extends BasePresenter<FreeBoardView> {

    private Context context;

    public FreeBoardPresenter(FreeBoardView view, Context context){
        super(view);
        this.context = context;
    }

    public void loadBoardData(){

    }
}
