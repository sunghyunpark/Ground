package util.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.groundmobile.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.MatchingDateAlarmModel;

public class MatchDateAlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 0;
    private ArrayList<MatchingDateAlarmModel> matchingDateAlarmModelArrayList;
    private MatchDateAlarmListener matchDateAlarmListener;

    public MatchDateAlarmAdapter(ArrayList<MatchingDateAlarmModel> matchingDateAlarmModelArrayList, MatchDateAlarmListener matchDateAlarmListener){
        this.matchingDateAlarmModelArrayList = matchingDateAlarmModelArrayList;
        this.matchDateAlarmListener = matchDateAlarmListener;
    }

    public interface MatchDateAlarmListener{
        void deleteAlarm(MatchingDateAlarmModel matchingDateAlarmModel);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_matching_date_alarm_item, parent, false);
            return new Alarm_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private MatchingDateAlarmModel getItem(int position) {
        return matchingDateAlarmModelArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Alarm_VH) {
            final MatchingDateAlarmModel currentItem = getItem(position);
            final Alarm_VH VHitem = (Alarm_VH)holder;

            VHitem.alarm_tv.setText(currentItem.getAreaName()+" - "+currentItem.getMatchDate());

            VHitem.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemDismiss(position);
                    matchDateAlarmListener.deleteAlarm(currentItem);
                }
            });
        }
    }

    // 알림 item
    public class Alarm_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.alarm_tv) TextView alarm_tv;
        @BindView(R.id.delete_btn) Button delete_btn;

        private Alarm_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void onItemDismiss(int position){
        matchingDateAlarmModelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return matchingDateAlarmModelArrayList.size();
    }
}
