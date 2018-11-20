package view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.SessionManager;

public class AppPushActivity extends BaseActivity {

    private SessionManager sessionManager;

    @BindView(R.id.match_comment_push_toggle) ToggleButton matchCommentPushToggle;
    @BindView(R.id.free_comment_push_toggle) ToggleButton freeCommentPushToggle;
    @BindView(R.id.match_push_toggle) ToggleButton matchPushToggle;
    @BindView(R.id.event_push_toggle) ToggleButton eventPushToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_push);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        sessionManager = new SessionManager(getApplicationContext());

        setToggleState(matchCommentPushToggle, sessionManager.isPushCommentOfMatch());
        setToggleState(freeCommentPushToggle, sessionManager.isPushCommentOfFree());
        setToggleState(matchPushToggle, sessionManager.isMyFavoriteArticleMatchedOn());
        setToggleState(eventPushToggle, sessionManager.isEventPushOn());

    }

    /**
     * 푸시 토글 버튼들을 초기화한다.
     * @param toggleButton
     * @param state
     */
    private void setToggleState(ToggleButton toggleButton, boolean state){
        if(state){
            toggleButton.setChecked(true);
            toggleButton.setBackgroundResource(R.mipmap.checked_img);
        }else{
            toggleButton.setChecked(false);
            toggleButton.setBackgroundResource(R.mipmap.not_checked_img);
        }
    }

    @OnClick({R.id.event_push_toggle, R.id.match_comment_push_toggle, R.id.free_comment_push_toggle, R.id.match_push_toggle}) void toggleBtn(View v){
        switch (v.getId()){
            case R.id.match_comment_push_toggle:
                setToggleState(matchCommentPushToggle, matchCommentPushToggle.isChecked());
                sessionManager.setPushCommentOfMatch(matchCommentPushToggle.isChecked());
                break;

            case R.id.match_push_toggle:
                setToggleState(matchPushToggle, matchPushToggle.isChecked());
                sessionManager.setPushMyFavoriteArticleMatched(matchPushToggle.isChecked());
                break;

            case R.id.free_comment_push_toggle:
                setToggleState(freeCommentPushToggle, freeCommentPushToggle.isChecked());
                sessionManager.setPushCommentOfFree(freeCommentPushToggle.isChecked());
                break;

            case R.id.event_push_toggle:
                setToggleState(eventPushToggle, eventPushToggle.isChecked());
                sessionManager.setEventPush(eventPushToggle.isChecked());
                break;

        }
    }

    @OnClick({R.id.matching_date_of_match_push_btn, R.id.matching_date_of_hire_push_btn}) void matchingDateClick(View v){
        Intent intent = new Intent(getApplicationContext(), MatchingDateAlarmActivity.class);
        switch (v.getId()){
            case R.id.matching_date_of_match_push_btn:
                intent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, Constants.MATCH_OF_BOARD_TYPE_MATCH);
                startActivity(intent);
                break;

            case R.id.matching_date_of_hire_push_btn:
                intent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, Constants.HIRE_OF_BOARD_TYPE_MATCH);
                startActivity(intent);
                break;

        }
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
