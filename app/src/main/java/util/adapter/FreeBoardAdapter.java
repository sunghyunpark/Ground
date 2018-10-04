package util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.CommunityModel;
import util.NetworkUtils;
import util.SessionManager;
import util.Util;


public class FreeBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private ArrayList<CommunityModel> articleModelArrayList;
    private Context context;
    private FreeBoardAdapterListener freeBoardAdapterListener;
    private SessionManager sessionManager;

    public FreeBoardAdapter(Context context, ArrayList<CommunityModel> communityModelArrayList, FreeBoardAdapterListener freeBoardAdapterListener) {
        this.context = context;
        this.articleModelArrayList = communityModelArrayList;
        this.freeBoardAdapterListener = freeBoardAdapterListener;
        this.sessionManager = new SessionManager(context);
    }

    public interface FreeBoardAdapterListener{
        void goToDetailArticle(int position, CommunityModel communityModel);
        void writeArticle();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_free_board_item, parent, false);
            return new Article_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private CommunityModel getItem(int position) {
        return articleModelArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Article_VH) {
            final CommunityModel currentItem = getItem(position);
            final Article_VH VHitem = (Article_VH)holder;

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!sessionManager.isLoggedIn()){
                        Util.showToast(context, "로그인을 해주세요.");
                    }else if(!NetworkUtils.isNetworkConnected(context)){
                        Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
                    }else{
                        freeBoardAdapterListener.goToDetailArticle(position, currentItem);
                    }
                }
            });

            VHitem.title_tv.setText(currentItem.getTitle());

            VHitem.nick_name_tv.setText(Util.ellipseStr(currentItem.getNickName()));

            VHitem.view_cnt_tv.setText(currentItem.getViewCnt()+"");

            VHitem.comment_cnt_tv.setText(currentItem.getCommentCnt()+"");

            VHitem.created_at_tv.setText(Util.formatTimeString(Util.getDate(currentItem.getCreatedAt())));

            if(hasNewArticle(position)){
                VHitem.new_iv.setVisibility(View.VISIBLE);
            }else{
                VHitem.new_iv.setVisibility(View.INVISIBLE);
            }

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(GroundApplication.GROUND_DEV_API+currentItem.getPhotoUrl())
                    .into(VHitem.photo_thumb_iv);

        }
    }

    public class Article_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
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

    public void onItemDismiss(int position){
        articleModelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    private boolean hasNewArticle(int position){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String todayStr = df.format(new Date());
        return Util.parseTime(getItem(position).getCreatedAt()).contains(todayStr);
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