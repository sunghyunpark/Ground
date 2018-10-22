package view;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.groundmobile.ground.R;

public class YouTubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String API_KEY = "AIzaSyDXT-E0MMC5Jz5mcoil9EsQ3bbVffSuhvQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);

        // YouTube API KEY를 설정한다

        YouTubePlayerView youtubeView = (YouTubePlayerView)findViewById(R.id.youtube_view);
        youtubeView.initialize(API_KEY, this);

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
            player.loadVideo("CZB-CaxLjyw");
            //player.cueVideo("CZB-CaxLjyw");

        }

    }
}
