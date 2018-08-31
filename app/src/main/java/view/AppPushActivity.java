package view;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.SessionManager;

public class AppPushActivity extends BaseActivity {

    private SessionManager sessionManager;

    @BindView(R.id.comment_push_toggle) ToggleButton commentPushToggle;
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

        setToggleState(commentPushToggle, sessionManager.isCommentPushOn());
        setToggleState(matchPushToggle, sessionManager.isMatchPushOn());
        setToggleState(eventPushToggle, sessionManager.isEventPushOn());

    }

    private void setToggleState(ToggleButton toggleButton, boolean state){
        if(state){
            toggleButton.setChecked(true);
            toggleButton.setBackgroundResource(R.mipmap.checked_img);
        }else{
            toggleButton.setChecked(false);
            toggleButton.setBackgroundResource(R.mipmap.not_checked_img);
        }
    }

    @OnClick({R.id.event_push_toggle, R.id.comment_push_toggle, R.id.match_push_toggle}) void toggleBtn(View v){
        switch (v.getId()){
            case R.id.comment_push_toggle:
                setToggleState(commentPushToggle, commentPushToggle.isChecked());
                sessionManager.setCommentPush(commentPushToggle.isChecked());
                break;

            case R.id.match_push_toggle:
                setToggleState(matchPushToggle, matchPushToggle.isChecked());
                sessionManager.setMatchPush(matchPushToggle.isChecked());
                break;

            case R.id.event_push_toggle:
                setToggleState(eventPushToggle, eventPushToggle.isChecked());
                sessionManager.setEventPush(eventPushToggle.isChecked());
                break;
        }
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }
}
