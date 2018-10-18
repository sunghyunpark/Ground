package util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.groundmobile.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private Context context;
    private String[] areaArray;
    private ArrayList<Integer> checkedList = new ArrayList<>();

    public AreaSearchAdapter(Context context, String [] areaNameArray){
        this.context = context;
        this.areaArray = areaNameArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_area_search_item, parent, false);
            return new Area_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private String getItem(int position) {
        return areaArray[position];
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Area_VH) {
            final String currentItem = getItem(position);
            final Area_VH VHitem = (Area_VH)holder;

            VHitem.checkBox.setText(currentItem);

            VHitem.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        checkedList.add(position);
                    }
                }
            });

            if(checkedList.contains(position)){
                VHitem.checkBox.setChecked(true);
            }else{
                VHitem.checkBox.setChecked(false);
            }


        }
    }

    /**
     * 검색하기 버튼을 눌렀을 때 다이얼로그로 반환하기 위함.
     * @return
     */
    public ArrayList<Integer> getCheckList(){
        return this.checkedList;
    }

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
        return TYPE_ITEM;
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return areaArray.length;
    }
}
