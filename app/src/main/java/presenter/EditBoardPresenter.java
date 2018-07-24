package presenter;

import android.content.Context;

import base.presenter.BasePresenter;
import presenter.view.EditBoardView;

public class EditBoardPresenter extends BasePresenter<EditBoardView> {

    private Context context;

    public EditBoardPresenter(EditBoardView view, Context context){
        super(view);
        this.context = context;
    }

}
