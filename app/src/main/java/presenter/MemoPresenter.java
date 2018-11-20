package presenter;

import java.util.ArrayList;

import base.presenter.BasePresenter;
import database.model.MemoVO;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import presenter.view.MemoView;

public class MemoPresenter extends BasePresenter<MemoView> {
    private ArrayList<MemoVO> memoVOArrayList;
    private Realm mRealm;

    public MemoPresenter(MemoView view, ArrayList<MemoVO> memoVOArrayList, Realm mRealm){
        super(view);
        this.memoVOArrayList = memoVOArrayList;
        this.mRealm = mRealm;
    }

    public void setMemoData(boolean refresh){
        if(refresh){
            memoVOArrayList.clear();
        }

        RealmResults<MemoVO> memoVORealmResults = mRealm.where(MemoVO.class).findAll().sort("order", Sort.DESCENDING);
        int listSize = memoVORealmResults.size();

        if(listSize > 0){
            for(int i=0;i<listSize;i++){
                memoVOArrayList.add(memoVORealmResults.get(i));
            }
            getView().notifyMemoData(true);
        }else{
            getView().notifyMemoData(false);
        }
    }
}
