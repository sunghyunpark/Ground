package util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import java.util.ArrayList;
import model.CommentModel;
import util.SessionManager;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean isAll;
    private ArrayList<CommentModel> listItems;
    private Context context;
    private SessionManager sessionManager;

    public CommentAdapter(Context context, ArrayList<CommentModel> listItems, boolean isAll) {
        this.context = context;
        this.listItems = listItems;
        this.sessionManager = new SessionManager(context);
        this.isAll = isAll;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment_item, parent, false);
            return new Comment_VH(v);
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment_header, parent, false);
            return new Comment_Header(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private CommentModel getItem(int position) {
        if(isAll){
            return listItems.get(position);
        }else{
            return listItems.get(position-1);
        }
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

            VHitem.createdAt_tv.setText(currentItem.getCreatedAt());

        }else if(holder instanceof Comment_Header){
            final Comment_Header VHitem = (Comment_Header)holder;

            VHitem.comment_cnt_tv.setText("댓글 "+(getItemCount()-1));

            VHitem.more_comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    //게시판 item
    private class Comment_VH extends RecyclerView.ViewHolder{
        ImageView userProfile_iv;
        TextView nickName_tv;
        TextView comment_tv;
        TextView createdAt_tv;
        TextView report_tv;

        private Comment_VH(View itemView){
            super(itemView);
            userProfile_iv = (ImageView)itemView.findViewById(R.id.user_profile_iv);
            nickName_tv = (TextView)itemView.findViewById(R.id.nick_name_tv);
            comment_tv = (TextView)itemView.findViewById(R.id.comment_tv);
            createdAt_tv = (TextView)itemView.findViewById(R.id.created_at_tv);
            report_tv = (TextView)itemView.findViewById(R.id.report_tv);


        }
    }

    private class Comment_Header extends RecyclerView.ViewHolder{
        TextView comment_cnt_tv;
        Button more_comment_btn;

        private Comment_Header(View itemView){
            super(itemView);
            comment_cnt_tv = (TextView)itemView.findViewById(R.id.comment_cnt_tv);
            more_comment_btn = (Button) itemView.findViewById(R.id.more_comment_btn);
        }
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(isAll){
            return TYPE_ITEM;
        }else{
            if(isPositionHeader(position)){
                return TYPE_HEADER;
            }else{
                return TYPE_ITEM;
            }
        }
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        if(isAll){
            return listItems.size();
        }else{
            return listItems.size()+1;
        }
    }

}