package util.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.groundmobile.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.AreaModel;

public class AreaSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<AreaModel> areaModelArrayList;
    private ArrayList<Integer> checkedList = new ArrayList<>();

    public AreaSearchAdapter(ArrayList<AreaModel> areaModelArrayList){
        this.areaModelArrayList = areaModelArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_area_search_item, parent, false);
            return new Area_VH(v);
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_area_search_header, parent, false);
            return new Header_Vh(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private AreaModel getItem(int position) {
        return areaModelArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Area_VH) {
            final AreaModel currentItem = getItem(position);
            final Area_VH VHitem = (Area_VH)holder;

            VHitem.checkBox.setText(currentItem.getAreaName());

            VHitem.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        checkedList.add(position);
                    }else{
                        checkedList.remove((Integer)position);
                    }
                }
            });

            if(checkedList.contains(position)){
                VHitem.checkBox.setChecked(true);
            }else{
                VHitem.checkBox.setChecked(false);
            }


        }else if(holder instanceof Header_Vh){
            final AreaModel currentItem = getItem(position);
            final Header_Vh VHitem = (Header_Vh)holder;

            VHitem.areaName.setText(currentItem.getAreaName());

        }
    }

    /**
     * 검색하기 버튼을 눌렀을 때 다이얼로그로 반환하기 위함.
     * @return
     */
    public ArrayList<Integer> getCheckList(){
        return this.checkedList;
    }

    private boolean isPositionHeader(int position){
        return position == 0 || position == 9;
    }

    //지역 이름
    private class Header_Vh extends RecyclerView.ViewHolder{
        TextView areaName;

        private Header_Vh(View itemView){
            super(itemView);
            areaName = (TextView)itemView.findViewById(R.id.title_tv);
        }
    }

    // 상세 지역 이름
    public class Area_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
        @BindView(R.id.checkbox) CheckBox checkBox;

        private Area_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
        return areaModelArrayList.size();
    }
}
