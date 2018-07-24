package util.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.R;

import java.util.ArrayList;

import model.GroundUtilModel;


public class GroundUtilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final int TYPE_ITEM = 0;
    private int[] imgArray = {R.mipmap.ground_util_formation_img, R.mipmap.ground_util_weather_img, R.mipmap.ground_util_youtube_img,
    R.mipmap.ground_util_market_img};
    private String [] textArray = {"전술판", "날씨", "영상", "용품 마켓"};
    private Context context;

    public GroundUtilAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ground_util_item, parent, false);
            return new Util_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private String getItem(int position) {
        return textArray[position];
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Util_VH) {
            final String currentItem = getItem(position);
            final Util_VH VHitem = (Util_VH)holder;

            RequestOptions requestOptions = new RequestOptions();
            Drawable drawable = context.getResources().getDrawable(imgArray[position]);
            requestOptions.placeholder(drawable);

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(null)
                    .into(VHitem.util_iv);

            VHitem.util_tv.setText(currentItem);
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
        return textArray.length;
    }
}
