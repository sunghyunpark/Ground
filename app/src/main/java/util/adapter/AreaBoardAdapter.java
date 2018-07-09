package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yssh.ground.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import model.ArticleModel;
import util.SessionManager;
import util.Util;
import view.DetailArticleActivity;

/**
 * 임의의 지역 > 게시판 게시글 recyclerView Adapter
 */
public class AreaBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String area;    //지역명
    private ArrayList<ArticleModel> listItems;
    private Context context;
    private SessionManager sessionManager;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private int bannerCount;

    public AreaBoardAdapter(Context context, ArrayList<ArticleModel> listItems, String area, BannerViewPagerAdapter bannerViewPagerAdapter, int bannerCount) {
        this.context = context;
        this.listItems = listItems;
        this.area = area;
        this.sessionManager = new SessionManager(context);
        this.bannerViewPagerAdapter = bannerViewPagerAdapter;
        this.bannerCount = bannerCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_about_area_board_item, parent, false);
            return new Board_VH(v);
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_about_area_about_header, parent, false);
            return new Header_Vh(v);
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
            VHitem.nick_name_tv.setText(currentItem.getNickName());
            VHitem.view_cnt_tv.setText(currentItem.getViewCnt()+"");
            VHitem.comment_cnt_tv.setText(currentItem.getCommentCnt());

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sessionManager.isLoggedIn()){
                        //login
                        Intent intent = new Intent(context, DetailArticleActivity.class);
                        intent.putExtra("area", area);
                        intent.putExtra("areaNo", currentItem.getAreaNo());
                        intent.putExtra("no", currentItem.getNo());
                        intent.putExtra("boardType", currentItem.getBoardType());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //클릭 시 해당 아이템 조회수 +1
                        listItems.get(position-1).setViewCnt(getItem(position).getViewCnt()+1);
                    }else{
                        //not login
                        Util.showToast(context, "로그인을 해주세요.");
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTime(currentItem.getCreatedAt()));

            if(hasNewArticle(position)){
                VHitem.new_iv.setVisibility(View.VISIBLE);
            }else{
                VHitem.new_iv.setVisibility(View.INVISIBLE);
            }

        }else if(holder instanceof Header_Vh){
            final Header_Vh VHitem = (Header_Vh)holder;

            VHitem.banner_pager.setAdapter(bannerViewPagerAdapter);
            VHitem.banner_pager.setCurrentItem(bannerCount);
        }
    }

    //상단 헤더
    private class Header_Vh extends RecyclerView.ViewHolder{
        ViewPager banner_pager;

        private Header_Vh(View itemView){
            super(itemView);
            banner_pager = (ViewPager) itemView.findViewById(R.id.banner_pager);

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
        }
    }

    //게시판 item
    private class Board_VH extends RecyclerView.ViewHolder{
        ViewGroup item_layout;
        ImageView new_iv;
        TextView title_tv;
        TextView nick_name_tv;
        TextView created_at_tv;
        TextView view_cnt_tv;
        TextView comment_cnt_tv;

        private Board_VH(View itemView){
            super(itemView);
            item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
            new_iv = (ImageView)itemView.findViewById(R.id.new_iv);
            title_tv = (TextView)itemView.findViewById(R.id.title_tv);
            nick_name_tv = (TextView)itemView.findViewById(R.id.nick_name_tv);
            created_at_tv = (TextView)itemView.findViewById(R.id.created_at_tv);
            view_cnt_tv = (TextView)itemView.findViewById(R.id.view_cnt_tv);
            comment_cnt_tv = (TextView)itemView.findViewById(R.id.comment_cnt_tv);

        }
    }

    private boolean hasNewArticle(int position){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String todayStr = df.format(new Date());
        return getItem(position).getCreatedAt().contains(todayStr);
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size()+1;
    }
    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
