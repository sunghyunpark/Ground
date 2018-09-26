package util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groundmobile.ground.R;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.ArticleModel;


public class FreeBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private ArrayList<ArticleModel> articleModelArrayList;
    private Context context;

    public FreeBoardAdapter(Context context) {
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_free_board_item, parent, false);
            return new Article_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private ArticleModel getItem(int position) {
        return articleModelArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Article_VH) {
            final ArticleModel currentItem = getItem(position);
            final Article_VH VHitem = (Article_VH)holder;

        }
    }

    private class Article_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.new_iv) ImageView new_iv;
        @BindView(R.id.title_tv) TextView title_tv;
        @BindView(R.id.nick_name_tv) TextView nick_name_tv;
        @BindView(R.id.created_at_tv) TextView created_at_tv;
        @BindView(R.id.comment_cnt_tv) TextView comment_cnt_tv;
        @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
        @BindView(R.id.photo_thumb_iv) ImageView photo_thumb_iv;

        private Article_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return articleModelArrayList.size();
    }

}