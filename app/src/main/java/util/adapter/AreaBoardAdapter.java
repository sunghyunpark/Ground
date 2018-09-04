package util.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.groundmobile.ground.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.ArticleModel;
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
    private ArrayList<ArticleModel> listItems;
    private Context context;
    private SessionManager sessionManager;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private int bannerCount;

    private AreaBoardAdapterListener areaBoardAdapterListener;

    /* 메모리 관련 이슈때문에 잠시 주석처리
    private static final int SEND_RUNNING = 1000;
    private Util.BannerHandler handler;
    private BannerThread thread = null;
    */

    public AreaBoardAdapter(Context context, ArrayList<ArticleModel> listItems, String area, BannerViewPagerAdapter bannerViewPagerAdapter, int bannerCount, String boardType, AreaBoardAdapterListener areaBoardAdapterListener) {
        this.context = context;
        this.listItems = listItems;
        this.area = area;
        this.sessionManager = new SessionManager(context);
        this.bannerViewPagerAdapter = bannerViewPagerAdapter;
        this.bannerCount = bannerCount;
        this.boardType = boardType;
        this.areaBoardAdapterListener = areaBoardAdapterListener;
    }

    public interface AreaBoardAdapterListener{
        public void goToDetailArticle(int position, String area, ArticleModel articleModel);
        public void allSort();
        public void dateSort();
        public void writeArticle();
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

    private ArticleModel getItem(int position) {
        return listItems.get(position-1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Board_VH) {
            final ArticleModel currentItem = getItem(position);
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
                        /*
                        이 부분을 articleModel 자체를 넘겨주고자함
                        그리고 돌아올땐 articleModel 을 다시 돌려받고 notify 해주게되면
                        article 에서 변경된 모든 것들을 바로 게시글 리스트에서 반영할 수 있을 듯 함.
                        예상되는 효과로는 댓글의 갯수도 바로 갱신이 가능할듯함
                        조회수도 바로 갱신 가능.
                        상세화면 진입 시 다시 api 호출을 하지 않고 댓글 api만 호출하면된다.
                        단, 게시글 리스트를 내려주는 api 에서 상세화면에 보여질 데이터들을 모두 받아둔 상태여야한다.
                        게시글 상세화면에서 finish 처리가 되었을 때 onActivityResult 를 통해서 notify 해줘야한다.
                         */
                        areaBoardAdapterListener.goToDetailArticle(position-1, area, currentItem);
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTime(currentItem.getCreatedAt()));

            if(hasNewArticle(position)){
                VHitem.new_iv.setVisibility(View.VISIBLE);
            }else{
                VHitem.new_iv.setVisibility(View.INVISIBLE);
            }

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

        }else if(holder instanceof Header_Vh){
            final Header_Vh VHitem = (Header_Vh)holder;

            VHitem.banner_pager.setAdapter(bannerViewPagerAdapter);
            VHitem.banner_pager.setCurrentItem(bannerCount);

            VHitem.all_sort_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    areaBoardAdapterListener.allSort();
                }
            });

            VHitem.date_sort_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    areaBoardAdapterListener.dateSort();
                }
            });
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

    private class Empty_Vh extends RecyclerView.ViewHolder{
        TextView empty_tv;
        Button write_btn;
        private Empty_Vh(View itemView){
            super(itemView);
            empty_tv = (TextView) itemView.findViewById(R.id.empty_tv);
            write_btn = (Button) itemView.findViewById(R.id.write_btn);
        }
    }

    //상단 헤더
    private class Header_Vh extends RecyclerView.ViewHolder{
        ViewPager banner_pager;
        ViewGroup sort_layout;
        TextView all_sort_tv;
        TextView date_sort_tv;

        private Header_Vh(View itemView){
            super(itemView);
            banner_pager = (ViewPager) itemView.findViewById(R.id.banner_pager);
            sort_layout = (ViewGroup) itemView.findViewById(R.id.sort_layout);
            all_sort_tv = (TextView) itemView.findViewById(R.id.all_tv);
            date_sort_tv = (TextView) itemView.findViewById(R.id.date_tv);

            banner_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position < bannerCount)        //1번째 아이템에서 마지막 아이템으로 이동하면
                        banner_pager.setCurrentItem(position+bannerCount, false); //이동 애니메이션을 제거 해야 한다
                    else if(position >= bannerCount*2)     //마지막 아이템에서 1번째 아이템으로 이동하면
                        banner_pager.setCurrentItem(position - bannerCount, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if(!boardType.equals("match")){
                sort_layout.setVisibility(View.GONE);
            }

            /*메모리 관련 이슈때문에 잠시 주석처리
            handler = new Util.BannerHandler(this, banner_pager, bannerCount);
            thread = new BannerThread();
            thread.start();
            */
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

    //게시판 item
    private class Board_VH extends RecyclerView.ViewHolder{
        ViewGroup item_layout;
        ImageView new_iv;
        TextView title_tv;
        TextView nick_name_tv;
        TextView created_at_tv;
        TextView view_cnt_tv;
        TextView comment_cnt_tv;
        TextView match_state_tv;

        private Board_VH(View itemView){
            super(itemView);
            item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
            new_iv = (ImageView)itemView.findViewById(R.id.new_iv);
            title_tv = (TextView)itemView.findViewById(R.id.title_tv);
            nick_name_tv = (TextView)itemView.findViewById(R.id.nick_name_tv);
            created_at_tv = (TextView)itemView.findViewById(R.id.created_at_tv);
            view_cnt_tv = (TextView)itemView.findViewById(R.id.view_cnt_tv);
            comment_cnt_tv = (TextView)itemView.findViewById(R.id.comment_cnt_tv);
            match_state_tv = (TextView)itemView.findViewById(R.id.match_state_tv);

            if(!boardType.equals("match")){
                match_state_tv.setVisibility(View.GONE);
            }

        }
    }

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
