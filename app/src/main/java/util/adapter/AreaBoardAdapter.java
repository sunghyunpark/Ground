package util.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.MatchArticleModel;
import util.NetworkUtils;
import util.SessionManager;
import util.Util;

/**
 * 임의의 지역 > 게시판 게시글 recyclerView Adapter
 */
public class AreaBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_EMPTY = 2;
    private String area, boardType;    //지역명
    private ArrayList<MatchArticleModel> listItems;
    private Activity context;
    private SessionManager sessionManager;
    private BannerViewPagerAdapter bannerViewPagerAdapter;

    private AreaBoardAdapterListener areaBoardAdapterListener;

    /* 메모리 관련 이슈때문에 잠시 주석처리
    private static final int SEND_RUNNING = 1000;
    private Util.BannerHandler handler;
    private BannerThread thread = null;
    */

    public AreaBoardAdapter(Activity context, ArrayList<MatchArticleModel> listItems, String area, BannerViewPagerAdapter bannerViewPagerAdapter, String boardType, AreaBoardAdapterListener areaBoardAdapterListener) {
        this.context = context;
        this.listItems = listItems;
        this.area = area;
        this.sessionManager = new SessionManager(context);
        this.bannerViewPagerAdapter = bannerViewPagerAdapter;
        this.boardType = boardType;
        this.areaBoardAdapterListener = areaBoardAdapterListener;
    }

    public interface AreaBoardAdapterListener{
        void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel);
        void allSort();
        void dateSort(String matchDateStr);
        void matchStateSort();
        void writeArticle();
        void refreshList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_about_area_board_item, parent, false);
            return new Board_VH(v);
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_about_area_about_header, parent, false);
            return new Header_Vh(v);
        }else if(viewType == TYPE_EMPTY){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_about_area_board_empty_item, parent, false);
            return new Empty_Vh(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private MatchArticleModel getItem(int position) {
        return listItems.get(position-1);
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
                        areaBoardAdapterListener.goToDetailArticle(position-1, area, currentItem);
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTimeWithoutSec(currentItem.getCreatedAt()));

            if(hasNewArticle(position)){
                VHitem.new_iv.setVisibility(View.VISIBLE);
            }else{
                VHitem.new_iv.setVisibility(View.INVISIBLE);
            }

            //VHitem.matching_date_tv.setText(currentItem.getMatchDate());
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

        }else if(holder instanceof Header_Vh){
            final Header_Vh VHitem = (Header_Vh)holder;

            VHitem.banner_pager.setAdapter(bannerViewPagerAdapter);
            VHitem.banner_pager.setCurrentItem(bannerViewPagerAdapter.getCount() / 3);

        }else if(holder instanceof Empty_Vh){
            final Empty_Vh Vhitem = (Empty_Vh)holder;

            Vhitem.write_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    areaBoardAdapterListener.writeArticle();
                }
            });

        }
    }

    public class Empty_Vh extends RecyclerView.ViewHolder{
        @BindView(R.id.write_btn) Button write_btn;

        private Empty_Vh(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //상단 헤더
    public class Header_Vh extends RecyclerView.ViewHolder{
        @BindView(R.id.banner_pager) ViewPager banner_pager;
        @BindView(R.id.sort_layout) ViewGroup sort_layout;
        @BindView(R.id.all_tv) TextView all_sort_tv;
        @BindView(R.id.date_tv) TextView date_sort_tv;
        @BindView(R.id.match_date_tv) TextView match_date_tv;
        @BindView(R.id.match_state_tv) TextView match_state_tv;
        DatePickerDialog datePickerDialog;

        private Header_Vh(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            datePickerDialog = new DatePickerDialog(context, onDateSetListener, GroundApplication.TODAY_YEAR, GroundApplication.TODAY_MONTH-1, GroundApplication.TODAY_DAY);

            banner_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position < (bannerViewPagerAdapter.getCount() / 3))        //1번째 아이템에서 마지막 아이템으로 이동하면
                        banner_pager.setCurrentItem(position+(bannerViewPagerAdapter.getCount() / 3), false); //이동 애니메이션을 제거 해야 한다
                    else if(position >= (bannerViewPagerAdapter.getCount() / 3)*2)     //마지막 아이템에서 1번째 아이템으로 이동하면
                        banner_pager.setCurrentItem(position - (bannerViewPagerAdapter.getCount() / 3), false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if(boardType.equals(Constants.RECRUIT_OF_BOARD_TYPE_MATCH)){
                sort_layout.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.all_tv) void allSortBtn(){
            date_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            all_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            match_state_tv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            match_date_tv.setVisibility(View.GONE);
            areaBoardAdapterListener.allSort();
        }

        @OnClick(R.id.date_tv) void dateSortBtn(){
            datePickerDialog.show();
        }

        @OnClick(R.id.match_state_tv) void matchStateSortBtn(){
            date_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            all_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            match_state_tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            match_date_tv.setVisibility(View.GONE);
            areaBoardAdapterListener.matchStateSort();
        }

        @OnClick(R.id.refresh_btn) void refreshBtn(){
            areaBoardAdapterListener.refreshList();
        }

        private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-M-dd");
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String strBeforeFormat = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                String strAfterFormat = "";
                try {
                    Date originDate = originFormat.parse(strBeforeFormat);

                    strAfterFormat = newFormat.format(originDate);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                date_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                all_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                match_state_tv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                areaBoardAdapterListener.dateSort(strAfterFormat);
                match_date_tv.setVisibility(View.VISIBLE);
                match_date_tv.setText(strAfterFormat);
            }
        };
    }

    //게시판 item
    public class Board_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
        @BindView(R.id.new_iv) ImageView new_iv;
        @BindView(R.id.nick_name_tv) TextView nick_name_tv;
        @BindView(R.id.title_tv) TextView title_tv;
        @BindView(R.id.created_at_tv) TextView created_at_tv;
        @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
        @BindView(R.id.comment_cnt_tv) TextView comment_cnt_tv;
        @BindView(R.id.match_state_tv) TextView match_state_tv;
        //@BindView(R.id.match_state_layout) ViewGroup match_state_layout;
        //@BindView(R.id.matching_date_tv) TextView matching_date_tv;
        @BindView(R.id.play_rule_tv) TextView play_rule_tv;
        @BindView(R.id.average_age_tv) TextView average_age_tv;

        private Board_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            if(boardType.equals(Constants.HIRE_OF_BOARD_TYPE_MATCH)){
                // 용병 게시글인 경우
                play_rule_tv.setVisibility(View.GONE);
                average_age_tv.setVisibility(View.GONE);
            }else if(boardType.equals(Constants.RECRUIT_OF_BOARD_TYPE_MATCH)){
                // 모집 게시글인 경우
                match_state_tv.setVisibility(View.GONE);
                play_rule_tv.setVisibility(View.GONE);
                average_age_tv.setVisibility(View.GONE);
            }
        }
    }

    public void onItemDismiss(int position){
        listItems.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    /* 메모리 관련 이슈때문에 잠시 주석처리
    public void stopBannerThread(){
        thread.stopThread();
        this.handler.removeMessages(0);
    }

    private class BannerThread extends java.lang.Thread{
        boolean stopped = false;

        private BannerThread(){
            this.stopped = false;
        }

        private void stopThread(){
            this.stopped = true;
        }

        @Override
        public void run(){
            super.run();
            while (!stopped){
                Message message = handler.obtainMessage();
                message.what = SEND_RUNNING;
                handler.sendMessage(message);
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    */

    private boolean isMatchState(int position){
        return getItem(position).getMatchState().equals("Y");
    }

    private boolean hasNewArticle(int position){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String todayStr = df.format(new Date());
        return Util.parseTime(getItem(position).getCreatedAt()).contains(todayStr);
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }else{
            return (listItems.size() > 0) ? TYPE_ITEM : TYPE_EMPTY;
        }
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        if(listItems.size() > 0){
            return listItems.size()+1;
        }else{
            // TYPE_EMPTY = 2 > 상단 배너는 항상 노출이므로 1+1(getItemViewType 에 position 값이 들어가야하므로 +1을 더해준 값)
            return TYPE_EMPTY;
        }
    }
}
