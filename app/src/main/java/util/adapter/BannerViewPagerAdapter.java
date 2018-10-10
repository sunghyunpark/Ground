package util.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import model.BannerModel;

public class BannerViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BannerModel> bannerModelArrayList;

    public BannerViewPagerAdapter(Context context, ArrayList<BannerModel> bannerModelArrayList){
        this.context = context;
        this.bannerModelArrayList = bannerModelArrayList;
    }

    @Override
    public int getCount() {
        //return bannerModelArrayList.size();
        return bannerModelArrayList.size() * 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == ((ViewGroup) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= bannerModelArrayList.size();
        final int pos = position;
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.banner_view_pager_item, container, false);
        ImageView banner_iv = (ImageView) v.findViewById(R.id.banner_iv);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(GroundApplication.GROUND_DEV_API+bannerModelArrayList.get(position).getImgPath())
                .into(banner_iv);

        banner_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(bannerModelArrayList.get(pos).getUrl()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        TextView banner_cnt_tv = (TextView) v.findViewById(R.id.count_tv);
        banner_cnt_tv.setText((position+1)+"/"+bannerModelArrayList.size());

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
