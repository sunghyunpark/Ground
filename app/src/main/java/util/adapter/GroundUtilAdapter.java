package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.R;

import view.FormationActivity;
import view.dialog.GroundUtilWeatherDialog;


public class GroundUtilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final int TYPE_ITEM = 0;
    private int[] imgArray = {R.mipmap.ground_util_formation_img, R.mipmap.ground_util_weather_img, R.mipmap.ground_util_youtube_img,
    R.mipmap.ground_util_market_img};
    private String [] textArray = {"전술판", "날씨", "YouTube", "용품 마켓"};
    private Context context;
    private GroundUtilWeatherDialog groundUtilWeatherDialog;

    public GroundUtilAdapter(Context context){
        this.context = context;
        groundUtilWeatherDialog = new GroundUtilWeatherDialog(context);
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

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (position){
                        case 0:
                            goToFormationActivity();
                            break;
                        case 1:
                            goToWeatherDialog();
                            break;
                        case 2:
                            goToYouTubeAPP();
                            break;
                        case 3:
                            Toast.makeText(context,"준비중입니다.", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            });
        }
    }

    private class Util_VH extends RecyclerView.ViewHolder{
        ViewGroup item_layout;
        TextView util_tv;
        ImageView util_iv;

        private Util_VH(View itemView){
            super(itemView);
            item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
            util_iv = (ImageView)itemView.findViewById(R.id.util_iv);
            util_tv = (TextView)itemView.findViewById(R.id.util_tv);
        }
    }

    private void goToFormationActivity(){
        Intent intent = new Intent(context, FormationActivity.class);
        context.startActivity(intent);
    }

    private void goToWeatherDialog(){
        groundUtilWeatherDialog.show();
    }

    private void goToYouTubeAPP(){
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra("query", "풋살");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
