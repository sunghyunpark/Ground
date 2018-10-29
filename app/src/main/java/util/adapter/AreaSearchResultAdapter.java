package util.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
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

public class AreaSearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_EMPTY = 2;
    private ArrayList<MatchArticleModel> listItems;
    private ArrayList<TextView> areaTextViewList;
    private Activity context;
    private SessionManager sessionManager;
    private String[] matchArea;
    private String areaArrayStr;

    private AreaSearchResultAdapterListener areaSearchResultAdapterListener;


    public AreaSearchResultAdapter(Activity context, ArrayList<MatchArticleModel> listItems, String areaArrayStr, AreaSearchResultAdapterListener areaSearchResultAdapterListener) {
        this.context = context;
        this.listItems = listItems;
        this.sessionManager = new SessionManager(context);
        this.areaSearchResultAdapterListener = areaSearchResultAdapterListener;
        this.areaTextViewList = new ArrayList<>();
        this.areaArrayStr = areaArrayStr;

        Resources res = context.getResources();
        matchArea = res.getStringArray(R.array.matching_board_list);
    }

    public interface AreaSearchResultAdapterListener{
        void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel);
        void allSort();
        void dateSort(String matchDateStr);
        void matchStateSort();
        void writeArticle();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_area_search_result_item, parent, false);
            return new Board_VH(v);
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_area_search_result_header, parent, false);
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
                        areaSearchResultAdapterListener.goToDetailArticle(position-1, changeToAreaName(currentItem.getAreaNo()), currentItem);
                    }
                }
            });
            VHitem.created_at_tv.setText(Util.parseTimeWithoutSec(currentItem.getCreatedAt()));

            VHitem.area_tv.setText(changeToAreaName(currentItem.getAreaNo()));

            if(hasNewArticle(position)){
                VHitem.new_iv.setVisibility(View.VISIBLE);
            }else{
                VHitem.new_iv.setVisibility(View.INVISIBLE);
            }

            if(isMatchState(position)){
                // 매칭 완료
                VHitem.match_state_tv.setText("완료");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorRed));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.matching_state_on_shape);
            }else{
                // 진행중
                VHitem.match_state_tv.setText("진행중");
                VHitem.match_state_tv.setTextColor(context.getResources().getColor(R.color.colorMoreGray));
                VHitem.match_state_tv.setBackgroundResource(R.drawable.matching_state_off_shape);
            }

        }else if(holder instanceof Header_Vh){

        }else if(holder instanceof Empty_Vh){
            final Empty_Vh Vhitem = (Empty_Vh)holder;

            Vhitem.write_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    areaSearchResultAdapterListener.writeArticle();
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
        @BindView(R.id.sort_layout) ViewGroup sort_layout;
        @BindView(R.id.all_tv) TextView all_sort_tv;
        @BindView(R.id.date_tv) TextView date_sort_tv;
        @BindView(R.id.match_date_tv) TextView match_date_tv;
        @BindView(R.id.match_state_tv) TextView match_state_tv;
        @BindView(R.id.area_list_layout) FlexboxLayout area_list_layout;

        TextView area_tv;
        DatePickerDialog datePickerDialog;

        private Header_Vh(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            datePickerDialog = new DatePickerDialog(context, onDateSetListener, GroundApplication.TODAY_YEAR, GroundApplication.TODAY_MONTH-1, GroundApplication.TODAY_DAY);

            String[] areaNoStrArray = areaArrayStr.split(",");
            area_list_layout.setFlexWrap(4);

            for(int i=0;i<areaNoStrArray.length;i++){
                area_list_layout.addView(getTextView(changeToAreaName(Integer.parseInt(areaNoStrArray[i]))));
            }
        }

        private TextView getTextView(String areaName){
            area_tv = new TextView(context);
            area_tv.setBackgroundResource(R.drawable.area_search_result_shape);
            area_tv.setText(areaName);
            area_tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT) ;
            params.setMargins(10, 0, 10, 10 );
            area_tv.setLayoutParams( params );

            return area_tv;
        }

        @OnClick(R.id.all_tv) void allSortBtn(){
            date_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorMoreGray));
            all_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            match_state_tv.setTextColor(ContextCompat.getColor(context, R.color.colorMoreGray));
            match_date_tv.setVisibility(View.GONE);
            areaSearchResultAdapterListener.allSort();
        }

        @OnClick(R.id.date_tv) void dateSortBtn(){
            datePickerDialog.show();
        }

        @OnClick(R.id.match_state_tv) void matchStateSortBtn(){
            date_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorMoreGray));
            all_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorMoreGray));
            match_state_tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            match_date_tv.setVisibility(View.GONE);
            areaSearchResultAdapterListener.matchStateSort();
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
                all_sort_tv.setTextColor(ContextCompat.getColor(context, R.color.colorMoreGray));
                match_state_tv.setTextColor(ContextCompat.getColor(context, R.color.colorMoreGray));
                areaSearchResultAdapterListener.dateSort(strAfterFormat);
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
        @BindView(R.id.area_tv) TextView area_tv;

        private Board_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void onItemDismiss(int position){
        listItems.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    private String changeToAreaName(int areaNo){
        return matchArea[areaNo];

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
