package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import model.YouTubeModel;
import view.YouTubePlayerActivity;

public class RecommendYouTubeViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<YouTubeModel> youTubeModelArrayList;

    public RecommendYouTubeViewPagerAdapter(Context context, ArrayList<YouTubeModel> youTubeModelArrayList){
        this.context = context;
        this.youTubeModelArrayList = youTubeModelArrayList;
    }

    @Override
    public int getCount() {
        //return bannerModelArrayList.size();
        return youTubeModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == ((ViewGroup) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.recommend_youtube_view_pager_item, container, false);

        ImageView youtube_thumb = (ImageView)v.findViewById(R.id.youtube_thumb);

        TextView youtube_title_tv = (TextView)v.findViewById(R.id.youtube_title_tv);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(youTubeModelArrayList.get(position).getImgPath())
                .apply(new RequestOptions().transform(new RoundedCorners(20)))
                .into(youtube_thumb);

        youtube_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, YouTubePlayerActivity.class);
                intent.putExtra(GroundApplication.EXTRA_YOUTUBE_VIDEO_ID, youTubeModelArrayList.get(position).getVideoId());
                intent.putExtra(GroundApplication.EXTRA_YOUTUBE_TITLE, youTubeModelArrayList.get(position).getTitle());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        youtube_title_tv.setText(youTubeModelArrayList.get(position).getTitle());

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

}
