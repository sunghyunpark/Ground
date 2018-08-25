package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import com.groundmobile.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 그라운드 유틸 > 날씨 다이얼로그
 */
public class GroundUtilWeatherDialog extends Dialog {

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

    @OnClick(R.id.item_1_tv) void item1Btn(){
        //네이버 날씨
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weather.naver.com/"));
        getContext().startActivity(intent);
    }

    @OnClick(R.id.item_2_tv) void item2Btn(){
        //기상청
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kma.go.kr/index.jsp"));
        getContext().startActivity(intent);
    }

    @OnClick(R.id.item_3_tv) void item3Btn(){
        //항공기상청
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://amo.kma.go.kr/new/html/main/main.jsp"));
        getContext().startActivity(intent);
    }

}
