package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.groundmobile.ground.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import database.model.MemoVO;
import io.realm.Realm;
import io.realm.RealmResults;
import util.ItemTouchHelperAdapter;

public class MemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private static final int TYPE_ITEM = 0;
    private Context context;
    private Realm mRealm;
    private ArrayList<MemoVO> listItems;
    private memoAdapterListener memoAdapterListener;
    private int checkPos = -1;

    public MemoAdapter(Context context, ArrayList<MemoVO> listItems, Realm mRealm, memoAdapterListener memoAdapterListener) {
        this.listItems = listItems;
        this.context = context;
        this.mRealm = mRealm;
        this.memoAdapterListener = memoAdapterListener;
    }

    public interface memoAdapterListener{
        void verifyExistMemo();
        void goToEditMemo(int position, MemoVO memoVO);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_memo_item, parent, false);
            return new MemoViewHolder(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private MemoVO getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MemoViewHolder) {
            final MemoVO currentItem = getItem(position);
            final MemoViewHolder VHitem = (MemoViewHolder)holder;

            VHitem.memo_text_tv.setText(currentItem.getMemoText());

            VHitem.memo_item_vg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    memoAdapterListener.goToEditMemo(position, currentItem);
                }
            });

        }
    }

    class MemoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.memo_item_layout) ViewGroup memo_item_vg;
        @BindView(R.id.memo_text_tv) TextView memo_text_tv;

        private MemoViewHolder(View itemView){
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(listItems, fromPosition, toPosition);
        swapMemoOrder(getItem(fromPosition).getOrder(), getItem(toPosition).getOrder());
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        deleteMemoDB(getItem(position).getNo());
        listItems.remove(position);
        notifyItemRemoved(position);

        if(getItemCount() == 0){
            memoAdapterListener.verifyExistMemo();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    /**
     * Realm DB 에서 해당 position item 의 no 를 삭제
     * @param position
     */
    private void deleteMemoDB(int position){
        MemoVO memoVO = mRealm.where(MemoVO.class).equalTo("no",(position)).findFirst();
        mRealm.beginTransaction();
        memoVO.deleteFromRealm();
        mRealm.commitTransaction();

    }

    /**
     * no 은 Primary key 라서 order 를 추가해서 사용함
     * 단순 순서를 정하는 용도로만 사용
     * @param fromOrder
     * @param ToOrder
     */
    private void swapMemoOrder(int fromOrder, int ToOrder){
        MemoVO fromMemoVO = mRealm.where(MemoVO.class).equalTo("order",(fromOrder)).findFirst();
        MemoVO toMemoVO = mRealm.where(MemoVO.class).equalTo("order",(ToOrder)).findFirst();
        mRealm.beginTransaction();
        fromMemoVO.setOrder(ToOrder);
        toMemoVO.setOrder(fromOrder);
        mRealm.commitTransaction();
    }
}
