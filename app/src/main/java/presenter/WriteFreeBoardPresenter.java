package presenter;

import android.content.Context;

import base.presenter.BasePresenter;
import presenter.view.WriteFreeBoardView;

public class WriteFreeBoardPresenter extends BasePresenter<WriteFreeBoardView> {

    private Context context;

    public WriteFreeBoardPresenter(WriteFreeBoardView view, Context context){
        super(view);
        this.context = context;
    }
}
