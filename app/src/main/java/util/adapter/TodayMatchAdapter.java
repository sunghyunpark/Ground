package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yssh.ground.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.ArticleModel;
import util.NetworkUtils;
import util.SessionManager;
import util.Util;
import view.DetailArticleActivity;

public class TodayMatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private ArrayList<ArticleModel> listItems;
    private Context context;
    private SessionManager sessionManager;

    private String[] matchArea;

    public TodayMatchAdapter(Context context, ArrayList<ArticleModel> listItems) {
        this.context = context;
        this.listItems = listItems;
        this.sessionManager = new SessionManager(context);
        Resources res = context.getResources();

        matchArea = res.getStringArray(R.array.matching_board_list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_today_match_board_item, parent, false);
            return new Board_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private ArticleModel getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Board_VH) {
            final ArticleModel currentItem = getItem(position);
            final Board_VH VHitem = (Board_VH)holder;

            VHitem.title_tv.setText(currentItem.getTitle());
            VHitem.nick_name_tv.setText(currentItem.getNickName());
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
                        Intent intent = new Intent(context, DetailArticleActivity.class);
                        intent.putExtra("area", changeToAreaName(currentItem.getAreaNo()));
                        intent.putExtra("articleModel", currentItem);
                        intent.putExtra("hasArticleModel", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //클릭 시 해당 아이템 조회수 +1
                        listItems.get(position).setViewCnt(getItem(position).getViewCnt()+1);
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTime(currentItem.getCreatedAt()));

            VHitem.area_tv.setText(changeToAreaName(currentItem.getAreaNo()));

            if(isMatchState(position)){
                // 매칭 완료
                VHitem.match_state_tv.setText("완료");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorCyan));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.today_matching_state_on_shape);
            }else{
                // 진행중
                VHitem.match_state_tv.setText("진행중");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorMoreGray));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.matching_state_off_shape);
            }
        }
    }

    private String changeToAreaName(int areaNo){
        return matchArea[areaNo];
    }

    //게시판 item
    private class Board_VH extends RecyclerView.ViewHolder{
        ViewGroup item_layout;
        TextView title_tv;
        TextView nick_name_tv;
        TextView created_at_tv;
        TextView view_cnt_tv;
        TextView comment_cnt_tv;
        TextView area_tv;
        TextView match_state_tv;

        private Board_VH(View itemView){
            super(itemView);
            item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
            title_tv = (TextView)itemView.findViewById(R.id.title_tv);
            nick_name_tv = (TextView)itemView.findViewById(R.id.nick_name_tv);
            created_at_tv = (TextView)itemView.findViewById(R.id.created_at_tv);
            view_cnt_tv = (TextView)itemView.findViewById(R.id.view_cnt_tv);
            comment_cnt_tv = (TextView)itemView.findViewById(R.id.comment_cnt_tv);
            area_tv = (TextView)itemView.findViewById(R.id.area_tv);
            match_state_tv = (TextView)itemView.findViewById(R.id.match_state_tv);
        }
    }

    private boolean isMatchState(int position){
        return getItem(position).getMatchState().equals("Y");
    }

    private boolean hasNewArticle(int position){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String todayStr = df.format(new Date());
        return getItem(position).getCreatedAt().contains(todayStr);
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