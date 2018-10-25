package view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YouTubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String API_KEY = "AIzaSyDXT-E0MMC5Jz5mcoil9EsQ3bbVffSuhvQ";
    private String videoId, titleStr;

    @BindView(R.id.youtube_view) YouTubePlayerView youtubeView;
    @BindView(R.id.title_tv) TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        videoId = intent.getExtras().getString(GroundApplication.EXTRA_YOUTUBE_VIDEO_ID);
        titleStr = intent.getExtras().getString(GroundApplication.EXTRA_YOUTUBE_TITLE);

        // YouTube API KEY를 설정한다
        youtubeView.initialize(API_KEY, this);

        title_tv.setText(titleStr);
    }



    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        // 초기화를 실패한 경우에 처리한다
        if (errorReason.isUserRecoverableError()) {

        } else {

        }
    }



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        // YouTube 동영상 ID를 설정한다
        if (!wasRestored) {
            player.loadVideo(videoId);
            //player.cueVideo("CZB-CaxLjyw");

        }
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
