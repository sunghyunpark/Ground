package util.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.R;

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
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.banner_view_pager_item, container, false);
        ImageView banner_iv = (ImageView) v.findViewById(R.id.banner_iv);

        RequestOptions requestOptions = new RequestOptions();
        Drawable drawable = context.getResources().getDrawable(R.drawable.banner_test_img);
        requestOptions.placeholder(drawable);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(null)
                .into(banner_iv);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
