package util.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yssh.ground.R;

public class BoardAreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String[] listItems;

    public BoardAreaAdapter(String[] listItems) {
        this.listItems = listItems;
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

            VHitem.areaName.setText(currentItem);
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
        if(isPositionHeader(position)){
            return TYPE_HEADER;
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
