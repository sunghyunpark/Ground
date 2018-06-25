package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yssh.ground.R;

import java.util.ArrayList;

import model.BoardModel;
import view.AboutBoardActivity;

public class AboutAreaBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String area;    //지역명
    private ArrayList<BoardModel> listItems;
    private Context context;

    public AboutAreaBoardAdapter(Context context, ArrayList<BoardModel> listItems, String area) {
        this.context = context;
        this.listItems = listItems;
        this.area = area;
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

    private BoardModel getItem(int position) {
        return listItems.get(position-1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Board_VH) {
            final BoardModel currentItem = getItem(position);
            final Board_VH VHitem = (Board_VH)holder;

            VHitem.title_tv.setText(currentItem.getTitle());
            VHitem.nick_name_tv.setText(currentItem.getNickName());
            VHitem.created_at_tv.setText(currentItem.getCreatedAt());
            VHitem.view_cnt_tv.setText(currentItem.getViewCnt());

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AboutBoardActivity.class);
                    intent.putExtra("area", area);
                    intent.putExtra("areaNo", currentItem.getAreaNo());
                    context.startActivity(intent);
                }
            });

        }else if(holder instanceof Header_Vh){

        }
    }

    //상단 헤더
    private class Header_Vh extends RecyclerView.ViewHolder{
        TextView areaName;

        private Header_Vh(View itemView){
            super(itemView);
            areaName = (TextView)itemView.findViewById(R.id.title_tv);
        }
    }

    //게시판 item
    private class Board_VH extends RecyclerView.ViewHolder{
        ViewGroup item_layout;
        TextView title_tv;
        TextView nick_name_tv;
        TextView created_at_tv;
        TextView view_cnt_tv;
        TextView comment_cnt_tv;

        private Board_VH(View itemView){
            super(itemView);
            item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
            title_tv = (TextView)itemView.findViewById(R.id.title_tv);
            nick_name_tv = (TextView)itemView.findViewById(R.id.nick_name_tv);
            created_at_tv = (TextView)itemView.findViewById(R.id.created_at_tv);
            view_cnt_tv = (TextView)itemView.findViewById(R.id.view_cnt_tv);
            comment_cnt_tv = (TextView)itemView.findViewById(R.id.comment_cnt_tv);

        }
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

}
