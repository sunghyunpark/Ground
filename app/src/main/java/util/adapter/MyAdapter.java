package util.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.MatchArticleModel;
import model.CommentModel;
import model.UserModel;
import util.SessionManager;
import util.Util;
import view.DetailMatchArticleActivity;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_COMMENT = 1;
    private static final int TYPE_FAVORITE = 2;

    // 내가 작성한 댓글에 필요한 변수
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
        if (viewType == TYPE_ARTICLE || viewType == TYPE_FAVORITE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_my_article_item, parent, false);
            return new ArticleVH(v);
        }else if(viewType == TYPE_COMMENT){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment_item, parent, false);
            return new CommentVH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private MatchArticleModel getArticleItem(int position){
        return (MatchArticleModel)objectArrayList.get(position);
    }

    private CommentModel getCommentItem(int position){
        return (CommentModel)objectArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        //내가 작성한 글 or 관심있는 글
        if (holder instanceof ArticleVH) {
            final MatchArticleModel currentItem = getArticleItem(position);
            final ArticleVH VHitem = (ArticleVH) holder;

            VHitem.title_tv.setText(currentItem.getTitle());
            VHitem.nick_name_tv.setText(Util.ellipseStr(currentItem.getNickName()));
            VHitem.view_cnt_tv.setText(currentItem.getViewCnt()+"");
            VHitem.comment_cnt_tv.setText(currentItem.getCommentCnt());

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sessionManager.isLoggedIn()){
                        //login
                        Intent intent = new Intent(context, DetailMatchArticleActivity.class);
                        intent.putExtra(GroundApplication.EXTRA_AREA_NAME, changeToAreaName(currentItem.getAreaNo()));
                        intent.putExtra(GroundApplication.EXTRA_ARTICLE_MODEL, currentItem);
                        intent.putExtra(GroundApplication.EXTRA_EXIST_ARTICLE_MODEL, true);
                        intent.putExtra(GroundApplication.EXTRA_USER_ID, UserModel.getInstance().getUid());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //클릭 시 해당 아이템 조회수 +1
                        ((MatchArticleModel)objectArrayList.get(position)).setViewCnt(getArticleItem(position).getViewCnt()+1);
                    }else{
                        //not login
                        Util.showToast(context, "로그인을 해주세요.");
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTime(currentItem.getCreatedAt()));

            VHitem.area_tv.setText(changeToAreaName(currentItem.getAreaNo()));

            if(isMatchState(position)){
                // 매칭 완료
                VHitem.match_state_tv.setText("완료");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.matching_state_on_shape);
            }else{
                // 진행중
                VHitem.match_state_tv.setText("진행중");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorMoreGray));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.matching_state_off_shape);
            }

        }else if(holder instanceof CommentVH){
            //내가 작성한 댓글
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
                        Intent intent = new Intent(context, DetailMatchArticleActivity.class);
                        intent.putExtra(GroundApplication.EXTRA_AREA_NAME, changeToAreaName(currentItem.getAreaNo()));
                        intent.putExtra(GroundApplication.EXTRA_USER_ID, UserModel.getInstance().getUid());
                        intent.putExtra(GroundApplication.EXTRA_EXIST_ARTICLE_MODEL, false);
                        intent.putExtra(GroundApplication.EXTRA_AREA_NO, currentItem.getAreaNo());
                        intent.putExtra(GroundApplication.EXTRA_ARTICLE_NO, currentItem.getArticleNo());
                        intent.putExtra(GroundApplication.EXTRA_BOARD_TYPE, currentItem.getBoardType());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        //not login
                        Util.showToast(context, "로그인을 해주세요.");
                    }
                }
            });

            VHitem.delete_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getRootView().getContext());
                    alert.setTitle("삭제");
                    alert.setMessage("정말 삭제 하시겠습니까?");
                    alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            onItemDismiss(position);
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
        }
    }

    private void onItemDismiss(int position){
        objectArrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ArticleVH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
        @BindView(R.id.title_tv) TextView title_tv;
        @BindView(R.id.nick_name_tv) TextView nick_name_tv;
        @BindView(R.id.created_at_tv) TextView created_at_tv;
        @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
        @BindView(R.id.comment_cnt_tv) TextView comment_cnt_tv;
        @BindView(R.id.area_tv) TextView area_tv;
        @BindView(R.id.match_state_tv) TextView match_state_tv;

        private ArticleVH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            if(boardType != MATCH_BOARD){
                match_state_tv.setVisibility(View.GONE);
            }
        }
    }

    private boolean isMatchState(int position){
        return getArticleItem(position).getMatchState().equals("Y");
    }

    public class CommentVH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
        @BindView(R.id.user_profile_iv) ImageView userProfile_iv;
        @BindView(R.id.nick_name_tv) TextView nickName_tv;
        @BindView(R.id.comment_tv) TextView comment_tv;
        @BindView(R.id.created_at_tv) TextView createdAt_tv;
        @BindView(R.id.report_tv) TextView report_tv;
        @BindView(R.id.new_iv) ImageView new_iv;
        @BindView(R.id.delete_btn) TextView delete_tv;

        private CommentVH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            report_tv.setVisibility(View.GONE);
            new_iv.setVisibility(View.GONE);
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
