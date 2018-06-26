package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yssh.ground.R;

import api.ApiClient;
import api.ApiInterface;
import api.response.AboutAreaBoardListResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ArticleModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class AboutBoardActivity extends AppCompatActivity {

    private String area;
    private int no;
    private int areaNo;
    private ArticleModel articleModel;

    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.title_tv) TextView title_tv;
    @BindView(R.id.nick_name_tv) TextView nick_name_tv;
    @BindView(R.id.created_at_tv) TextView created_at_tv;
    @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
    @BindView(R.id.contents_tv) TextView contents_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString("area");
        no = intent.getIntExtra("no", 0);
        areaNo = intent.getIntExtra("areaNo", 0);

        init();

    }

    private void init(){
        articleModel = new ArticleModel();

        getAboutBoard(areaNo, no);

    }

    public void getAboutBoard(int areaNo, int no){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AboutAreaBoardListResponse> call = apiService.getAboutBoard(areaNo, no);
        call.enqueue(new Callback<AboutAreaBoardListResponse>() {
            @Override
            public void onResponse(Call<AboutAreaBoardListResponse> call, Response<AboutAreaBoardListResponse> response) {
                AboutAreaBoardListResponse aboutAreaBoardListResponse = response.body();
                if(aboutAreaBoardListResponse.getCode() == 200){
                    articleModel = aboutAreaBoardListResponse.getResult().get(0);
                    area_tv.setText(area);
                    title_tv.setText(articleModel.getTitle());
                    nick_name_tv.setText(articleModel.getNickName());
                    created_at_tv.setText(articleModel.getCreatedAt());
                    view_cnt_tv.setText("조회 "+articleModel.getViewCnt());
                    contents_tv.setText(articleModel.getContents());
                }else{
                    Util.showToast(getApplicationContext(), "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<AboutAreaBoardListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getApplicationContext(), "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    @OnClick(R.id.back_btn) void goBack(){
        finish();
    }
}
