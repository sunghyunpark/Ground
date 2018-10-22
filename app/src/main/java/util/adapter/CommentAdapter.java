package util.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import model.CommentModel;
import model.UserModel;
import util.Util;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private boolean isAll;
    private ArrayList<CommentModel> listItems;
    private Context context;
    private CommentListener commentListener;

    public CommentAdapter(Context context, ArrayList<CommentModel> listItems, boolean isAll, CommentListener commentListener) {
        this.context = context;
        this.listItems = listItems;
        this.isAll = isAll;
        this.commentListener = commentListener;
    }

    public interface CommentListener{
        public void deleteCommentEvent(int commentNo);
        public void reportCommentEvent(int commentNo);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment_item, parent, false);
            return new Comment_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private CommentModel getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Comment_VH) {
            final CommentModel currentItem = getItem(position);
            final Comment_VH VHitem = (Comment_VH)holder;

            //Glide Options
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.user_profile_img);
            requestOptions.error(R.mipmap.user_profile_img);
            requestOptions.circleCrop();    //circle

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(GroundApplication.GROUND_DEV_API+currentItem.getProfile())
                    .into(VHitem.userProfile_iv);

            VHitem.nickName_tv.setText(currentItem.getNickName());

            VHitem.comment_tv.setText(currentItem.getComment());

            VHitem.createdAt_tv.setText(Util.parseTimeWithoutSec(currentItem.getCreatedAt()));

            if(hasNewArticle(position)){
                VHitem.new_iv.setVisibility(View.VISIBLE);
            }else{
                VHitem.new_iv.setVisibility(View.INVISIBLE);
            }

            if(isMyComment(position)){
                VHitem.delete_tv.setVisibility(View.VISIBLE);
            }else{
                VHitem.delete_tv.setVisibility(View.GONE);
            }

            VHitem.delete_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getRootView().getContext());
                    alert.setTitle("삭제");
                    alert.setMessage("정말 삭제 하시겠습니까?");
                    alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(isAll){
                                onItemDismiss(position);
                            }else{
                                onItemDismiss(position-1);
                            }
                            commentListener.deleteCommentEvent(currentItem.getNo());
                        }
                    });
                    alert.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.

                                }
                            });
                    alert.show();
                }
            });

            VHitem.report_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentListener.reportCommentEvent(currentItem.getNo());
                }
            });

        }
    }

    /**
     * Header 가 존재해서 position 값을 -1 한 값으로 전달한다.
     * @param position
     */
    private void onItemDismiss(int position){
        listItems.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    private boolean hasNewArticle(int position){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String todayStr = df.format(new Date());
        return getItem(position).getCreatedAt().contains(todayStr);
    }

    private boolean isMyComment(int position){
        return getItem(position).getWriterId().equals(UserModel.getInstance().getUid());
    }

    //댓글 item
    public class Comment_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.user_profile_iv) ImageView userProfile_iv;
        @BindView(R.id.nick_name_tv) TextView nickName_tv;
        @BindView(R.id.comment_tv) TextView comment_tv;
        @BindView(R.id.created_at_tv) TextView createdAt_tv;
        @BindView(R.id.report_tv) TextView report_tv;
        @BindView(R.id.new_iv) ImageView new_iv;
        @BindView(R.id.delete_btn) TextView delete_tv;

        private Comment_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
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

}
