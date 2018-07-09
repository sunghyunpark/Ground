package util.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yssh.ground.R;

import java.util.ArrayList;

import model.BannerModel;

public class BannerViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BannerModel> bannerModelArrayList;
    private int bannerCount;
    private int[] bannerArray = {R.drawable.banner_test_1, R.drawable.banner_test_2, R.drawable.banner_test_3 };

    public BannerViewPagerAdapter(Context context, ArrayList<BannerModel> bannerModelArrayList, int bannerCount){
        this.context = context;
        this.bannerModelArrayList = bannerModelArrayList;
        this.bannerCount = bannerCount;
    }

    @Override
    public int getCount() {
        //return bannerModelArrayList.size();
        return bannerCount * 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == ((ViewGroup) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= bannerCount;
        final int pos = position;
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.banner_view_pager_item, container, false);
        ImageView banner_iv = (ImageView) v.findViewById(R.id.banner_iv);

        RequestOptions requestOptions = new RequestOptions();
        Drawable drawable = context.getResources().getDrawable(bannerArray[position]);
        requestOptions.placeholder(drawable);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(null)
                .into(banner_iv);

        banner_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, pos+"",Toast.LENGTH_SHORT).show();
            }
        });

        TextView cnt_tv = (TextView) v.findViewById(R.id.cnt_tv);
        cnt_tv.setText(position+"");

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
