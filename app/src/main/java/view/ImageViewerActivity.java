package view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewerActivity extends BaseActivity {

    @BindView(R.id.image_view_pic)
    CropView cropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String photoPath = intent.getExtras().getString(Constants.EXTRA_BOARD_PHOTO_URL);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);

        Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).asBitmap().load(GroundApplication.GROUND_DEV_API+photoPath)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        cropView.setImageBitmap(resource);
                        //할일
                    }
                });
    }
}
