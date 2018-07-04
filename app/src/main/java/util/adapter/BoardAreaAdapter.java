package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yssh.ground.R;

import view.AreaBoardActivity;

/**
 * 게시판 recyclerView Adapter
 * 서울(도봉/노원/강북/중랑.....) 등등 지역별 노출되는 영역
 */
public class BoardAreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_MATCH = 2;
    private static final int TYPE_HIRE = 3;
    private static final int TYPE_RECRUIT = 4;
    private int type;
    private String[] listItems;
    private Context context;

    public BoardAreaAdapter(Context context, String[] listItems, int type) {
        this.context = context;
        this.listItems = listItems;
        if(type == TYPE_MATCH){
            this.type = TYPE_MATCH;
        }else if(type == TYPE_HIRE){
            this.type = TYPE_HIRE;
        }else{
            this.type = TYPE_RECRUIT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_area_item, parent, false);
            return new Area_VH(v);
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_area_header, parent, false);
            return new Header_Vh(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private String getItem(int position) {
        return listItems[position];
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Area_VH) {
            final String currentItem = getItem(position);
            final Area_VH VHitem = (Area_VH)holder;

            VHitem.areaName.setText(" - "+currentItem);
            VHitem.areaName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AreaBoardActivity.class);
                    intent.putExtra("area", currentItem);
                    intent.putExtra("areaNo", position);
                    context.startActivity(intent);
                }
            });
        }else if(holder instanceof Header_Vh){
            final String currentItem = getItem(position);
            final Header_Vh VHitem = (Header_Vh)holder;

            VHitem.areaName.setText(currentItem);

        }
    }

    //지역 이름
    private class Header_Vh extends RecyclerView.ViewHolder{
        TextView areaName;

        private Header_Vh(View itemView){
            super(itemView);
            areaName = (TextView)itemView.findViewById(R.id.title_tv);
        }
    }

    //상세 지역 이름
    private class Area_VH extends RecyclerView.ViewHolder{
        TextView areaName;

        private Area_VH(View itemView){
            super(itemView);
            areaName = (TextView)itemView.findViewById(R.id.area_name_tv);
        }
    }

    private boolean isPositionHeader(int position){
        return position == 0 || position == 9;
    }

    @Override
    public int getItemViewType(int position) {
        if(type == TYPE_MATCH){
            if(isPositionHeader(position)){
                return TYPE_HEADER;
            }else{
                return TYPE_ITEM;
            }
        }else{
            return TYPE_ITEM;
        }
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.length;
    }

}
