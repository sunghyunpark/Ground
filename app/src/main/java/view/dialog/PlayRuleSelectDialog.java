package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.groundmobile.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayRuleSelectDialog extends Dialog {

    private playRuleSelectListener playRuleSelectListener;

    public PlayRuleSelectDialog(Context context, playRuleSelectListener playRuleSelectListener){
        super(context);
        this.playRuleSelectListener = playRuleSelectListener;
    }

    public interface playRuleSelectListener{
        void playRuleSelectEvent(int playRule);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.play_rule_select_dialog);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.vs_3_3_tv, R.id.vs_5_5_tv, R.id.vs_6_6_tv, R.id.vs_11_11_tv, R.id.vs_etc_tv})
    public void ageClick(View view){
        int playRuleStr = 5;

        switch (view.getId()){
            case R.id.vs_3_3_tv:
                playRuleStr = 3;
                break;

            case R.id.vs_5_5_tv:
                playRuleStr = 5;
                break;

            case R.id.vs_6_6_tv:
                playRuleStr = 6;
                break;

            case R.id.vs_11_11_tv:
                playRuleStr = 11;
                break;

            case R.id.vs_etc_tv:
                playRuleStr = 0;
                break;
        }
        playRuleSelectListener.playRuleSelectEvent(playRuleStr);
        dismiss();
    }
}
