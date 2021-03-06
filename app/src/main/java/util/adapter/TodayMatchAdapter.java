package util.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groundmobile.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.MatchArticleModel;
import util.NetworkUtils;
import util.SessionManager;
import util.Util;

public class TodayMatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1000;
    private static final int TYPE_ITEM_ISMORE = 1100;
    private ArrayList<MatchArticleModel> listItems;
    private Context context;
    private SessionManager sessionManager;
    private todayMatchListener todayMatchListener;
    private boolean isMore;

    private String[] matchArea;

    public TodayMatchAdapter(Context context, ArrayList<MatchArticleModel> listItems, boolean isMore, todayMatchListener todayMatchListener) {
        this.context = context;
        this.listItems = listItems;
        this.sessionManager = new SessionManager(context);
        this.todayMatchListener = todayMatchListener;
        this.isMore = isMore;
        Resources res = context.getResources();

        matchArea = res.getStringArray(R.array.matching_board_list);
    }

    public interface todayMatchListener{
        void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_today_match_board_item, parent, false);
            return new Board_VH(v);
        } else if(viewType == TYPE_ITEM_ISMORE){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_today_match_board_ismore_item, parent, false);
            return new Board_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private MatchArticleModel getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Board_VH) {
            final MatchArticleModel currentItem = getItem(position);
            final Board_VH VHitem = (Board_VH)holder;

            VHitem.title_tv.setText(currentItem.getTitle());
            VHitem.nick_name_tv.setText(Util.ellipseStr(currentItem.getNickName()));
            VHitem.view_cnt_tv.setText(currentItem.getViewCnt()+"");
            VHitem.comment_cnt_tv.setText(currentItem.getCommentCnt());

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!sessionManager.isLoggedIn()){
                        Util.showToast(context, "로그인을 해주세요.");
                    }else if(!NetworkUtils.isNetworkConnected(context)){
                        Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
                    }else{
                        todayMatchListener.goToDetailArticle(position, changeToAreaName(currentItem.getAreaNo()), currentItem);
                        //클릭 시 해당 아이템 조회수 +1
                        listItems.get(position).setViewCnt(getItem(position).getViewCnt()+1);
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTimeWithoutSec(currentItem.getCreatedAt()));

            VHitem.area_tv.setText(changeToAreaName(currentItem.getAreaNo()));

            if(currentItem.getPlayRule() == 0){
                VHitem.play_rule_tv.setText("경기방식 미정");
            }else{
                VHitem.play_rule_tv.setText(currentItem.getPlayRule()+" vs "+currentItem.getPlayRule());
            }

            VHitem.average_age_tv.setText(currentItem.getAverageAge()+"대");

            if(isMatchState(position)){
                // 매칭 완료
                VHitem.match_state_tv.setText("완료");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorMoreGray));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.matching_state_on_shape2);
            }else{
                // 진행중
                VHitem.match_state_tv.setText("진행중");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.matching_state_off_shape2);
            }
        }
    }

    private String changeToAreaName(int areaNo){
        return matchArea[areaNo];
    }

    public void onItemDismiss(int position){
        listItems.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    //게시판 item
    public class Board_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
        @BindView(R.id.title_tv) TextView title_tv;
        @BindView(R.id.nick_name_tv) TextView nick_name_tv;
        @BindView(R.id.created_at_tv) TextView created_at_tv;
        @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
        @BindView(R.id.comment_cnt_tv) TextView comment_cnt_tv;
        @BindView(R.id.area_tv) TextView area_tv;
        @BindView(R.id.match_state_tv) TextView match_state_tv;
        @BindView(R.id.play_rule_tv) TextView play_rule_tv;
        @BindView(R.id.average_age_tv) TextView average_age_tv;

        private Board_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private boolean isMatchState(int position){
        return getItem(position).getMatchState().equals("Y");
    }

    @Override
    public int getItemViewType(int position) {
        if(isMore){
            return TYPE_ITEM_ISMORE;
        }else{
            return TYPE_ITEM;
        }
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
