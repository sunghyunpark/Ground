package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import java.util.ArrayList;

import model.ArticleModel;
import model.CommentModel;
import util.SessionManager;
import util.Util;
import view.DetailArticleActivity;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_COMMENT = 1;
    private static final int TYPE_FAVORITE = 2;

    private static final int MATCH_BOARD = 3;
    private static final int HIRE_BOARD = 4;
    private static final int RECRUIT_BOARD = 5;

    private int type, boardType;    //type > Article, Comment, Favorite   boardType > match, hire, recruit
    private Context context;
    private ArrayList<Object> objectArrayList;
    private SessionManager sessionManager;

    public MyAdapter(Context context, ArrayList<Object> objectArrayList, int type, int boardType){
        this.context = context;
        this.objectArrayList = objectArrayList;
        this.type = type;
        this.boardType = boardType;
        sessionManager = new SessionManager(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ARTICLE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_my_article_item, parent, false);
            return new ArticleVH(v);
        }else if(viewType == TYPE_COMMENT){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment_item, parent, false);
            return new CommentVH(v);
        }else if(viewType == TYPE_FAVORITE){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment_header, parent, false);
            return new FavoriteVH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private ArticleModel getArticleItem(int position){
        return (ArticleModel)objectArrayList.get(position);
    }

    private CommentModel getCommentItem(int position){
        return (CommentModel)objectArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        if (holder instanceof ArticleVH) {
            final ArticleModel currentItem = getArticleItem(position);
            final ArticleVH VHitem = (ArticleVH) holder;

            VHitem.title_tv.setText(currentItem.getTitle());
            VHitem.nick_name_tv.setText(currentItem.getNickName());
            VHitem.view_cnt_tv.setText(currentItem.getViewCnt()+"");
            VHitem.comment_cnt_tv.setText(currentItem.getCommentCnt());

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sessionManager.isLoggedIn()){
                        //login
                        Intent intent = new Intent(context, DetailArticleActivity.class);
                        intent.putExtra("area", changeToAreaName(currentItem.getAreaNo()));
                        intent.putExtra("areaNo", currentItem.getAreaNo());
                        intent.putExtra("no", currentItem.getNo());
                        intent.putExtra("boardType", currentItem.getBoardType());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //클릭 시 해당 아이템 조회수 +1
                        ((ArticleModel)objectArrayList.get(position)).setViewCnt(getArticleItem(position).getViewCnt()+1);
                    }else{
                        //not login
                        Util.showToast(context, "로그인을 해주세요.");
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTime(currentItem.getCreatedAt()));

            VHitem.area_tv.setText(changeToAreaName(currentItem.getAreaNo()));

        }else if(holder instanceof CommentVH){
            final CommentModel currentItem = getCommentItem(position);
            final CommentVH VHitem = (CommentVH) holder;

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

            VHitem.createdAt_tv.setText(Util.parseTime(currentItem.getCreatedAt()));

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sessionManager.isLoggedIn()){
                        //login
                        Intent intent = new Intent(context, DetailArticleActivity.class);
                        intent.putExtra("area", changeToAreaName(currentItem.getAreaNo()));
                        intent.putExtra("areaNo", currentItem.getAreaNo());
                        intent.putExtra("no", currentItem.getArticleNo());
                        intent.putExtra("boardType", currentItem.getBoardType());intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        //not login
                        Util.showToast(context, "로그인을 해주세요.");
                    }
                }
            });

        }else if(holder instanceof FavoriteVH){

        }
    }

    private class ArticleVH extends RecyclerView.ViewHolder{
        ViewGroup item_layout;
        TextView title_tv;
        TextView nick_name_tv;
        TextView created_at_tv;
        TextView view_cnt_tv;
        TextView comment_cnt_tv;
        TextView area_tv;

        private ArticleVH(View itemView){
            super(itemView);
            item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
            title_tv = (TextView)itemView.findViewById(R.id.title_tv);
            nick_name_tv = (TextView)itemView.findViewById(R.id.nick_name_tv);
            created_at_tv = (TextView)itemView.findViewById(R.id.created_at_tv);
            view_cnt_tv = (TextView)itemView.findViewById(R.id.view_cnt_tv);
            comment_cnt_tv = (TextView)itemView.findViewById(R.id.comment_cnt_tv);
            area_tv = (TextView)itemView.findViewById(R.id.area_tv);
        }
    }

    private class CommentVH extends RecyclerView.ViewHolder{
        ViewGroup item_layout;
        ImageView userProfile_iv;
        TextView nickName_tv;
        TextView comment_tv;
        TextView createdAt_tv;
        TextView report_tv;
        ImageView new_iv;

        private CommentVH(View itemView){
            super(itemView);
            item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
            userProfile_iv = (ImageView)itemView.findViewById(R.id.user_profile_iv);
            nickName_tv = (TextView)itemView.findViewById(R.id.nick_name_tv);
            comment_tv = (TextView)itemView.findViewById(R.id.comment_tv);
            createdAt_tv = (TextView)itemView.findViewById(R.id.created_at_tv);
            report_tv = (TextView)itemView.findViewById(R.id.report_tv);
            new_iv = (ImageView)itemView.findViewById(R.id.new_iv);
        }
    }

    private class FavoriteVH extends RecyclerView.ViewHolder{

        private FavoriteVH(View itemView){
            super(itemView);
        }
    }

    private String changeToAreaName(int areaNo){
        String[] matchArea, hireArea, recruitArea;
        Resources res = context.getResources();
        matchArea = res.getStringArray(R.array.matching_board_list);
        hireArea = res.getStringArray(R.array.hire_board_list);
        recruitArea = res.getStringArray(R.array.recruit_board_list);

        if(boardType == MATCH_BOARD){
            return matchArea[areaNo];
        }else if(boardType == HIRE_BOARD){
            return hireArea[areaNo];
        }else if(boardType == RECRUIT_BOARD){
            return recruitArea[areaNo];
        }else{
            return null;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(type == TYPE_ARTICLE){
            return TYPE_ARTICLE;
        }else if(type == TYPE_COMMENT){
            return TYPE_COMMENT;
        }else{
            return TYPE_FAVORITE;
        }
    }

    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

}