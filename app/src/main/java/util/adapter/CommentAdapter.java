package util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yssh.ground.R;

import java.util.ArrayList;
import model.CommentModel;
import util.SessionManager;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private ArrayList<CommentModel> listItems;
    private Context context;
    private SessionManager sessionManager;

    public CommentAdapter(Context context, ArrayList<CommentModel> listItems) {
        this.context = context;
        this.listItems = listItems;
        this.sessionManager = new SessionManager(context);
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

            VHitem.nickName_tv.setText(currentItem.getNickName());

            VHitem.comment_tv.setText(currentItem.getComment());

            VHitem.createdAt_tv.setText(currentItem.getCreatedAt());

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

    private boolean isPositionHeader(int position){
        return position == 0;
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
