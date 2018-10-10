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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.Util;
import view.FormationActivity;
import view.CommunityBoardActivity;
import view.dialog.GroundUtilWeatherDialog;


public class GroundUtilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final int TYPE_ITEM = 0;
    private int[] imgArray = {R.mipmap.comment_img2, R.mipmap.ground_util_formation_img, R.mipmap.ground_util_weather_img, R.mipmap.ground_util_youtube_img};
    private String [] textArray = {"자유게시판", "전술판", "날씨", "YouTube"};
    private ArrayList<String> groundUtilUpdateList;
    private Context context;
    private GroundUtilWeatherDialog groundUtilWeatherDialog;

    public GroundUtilAdapter(Context context, ArrayList<String> groundUtilUpdateList){
        this.context = context;
        groundUtilWeatherDialog = new GroundUtilWeatherDialog(context);
        this.groundUtilUpdateList = groundUtilUpdateList;
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

            if(hasNewArticle(position)){
                VHitem.new_layout.setVisibility(View.VISIBLE);
            }else{
                VHitem.new_layout.setVisibility(View.GONE);
            }

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (position){
                        case 0:
                            goToFreeBoardActivity();
                            break;
                        case 1:
                            goToFormationActivity();
                            break;
                        case 2:
                            goToWeatherDialog();
                            break;
                        case 3:
                            goToYouTubeAPP();
                            break;

                    }
                }
            });
        }
    }

    public class Util_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
        @BindView(R.id.new_layout) ViewGroup new_layout;
        @BindView(R.id.util_tv) TextView util_tv;
        @BindView(R.id.util_iv) ImageView util_iv;

        private Util_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private boolean hasNewArticle(int position){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String todayStr = df.format(new Date());
        return Util.parseTime(groundUtilUpdateList.get(position)).contains(todayStr);
    }

    private void goToFreeBoardActivity(){
        Intent intent = new Intent(context, CommunityBoardActivity.class);
        intent.putExtra(GroundApplication.EXTRA_COMMUNITY_BOARD_TYPE, GroundApplication.FREE_OF_BOARD_TYPE_COMMUNITY);
        context.startActivity(intent);
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
