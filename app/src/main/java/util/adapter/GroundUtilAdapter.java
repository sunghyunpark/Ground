package util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yssh.ground.R;

import java.util.ArrayList;

import model.GroundUtilModel;


public class GroundUtilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final int TYPE_ITEM = 0;
    private ArrayList<GroundUtilModel> listItems;
    private Context context;

    public GroundUtilAdapter(Context context, ArrayList<GroundUtilModel> groundUtilModelArrayList){
        this.context = context;
        this.listItems = groundUtilModelArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ground_util_item, parent, false);
            return new Util_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private GroundUtilModel getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Util_VH) {
            final GroundUtilModel currentItem = getItem(position);
            final Util_VH VHitem = (Util_VH)holder;

            VHitem.util_tv.setText(currentItem.getText());
        }
    }

    private class Util_VH extends RecyclerView.ViewHolder{
        TextView util_tv;
        ImageView util_iv;

        private Util_VH(View itemView){
            super(itemView);
            util_iv = (ImageView)itemView.findViewById(R.id.util_iv);
            util_tv = (TextView)itemView.findViewById(R.id.util_tv);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
