package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.yssh.ground.R;

import butterknife.ButterKnife;

/**
 * 그라운드 유틸 > 날씨 다이얼로그
 */
public class GroundUtilWeatherDialog extends Dialog {
    private Context context;

    public GroundUtilWeatherDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.ground_util_weather_dialog);

        ButterKnife.bind(this);
    }


}
