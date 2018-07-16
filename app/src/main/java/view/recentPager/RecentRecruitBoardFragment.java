package view.recentPager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.R;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.AboutAreaBoardListResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.ArticleModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.RecentBoardAdapter;

public class RecentRecruitBoardFragment extends Fragment {

    private View v;
    private ArrayList<ArticleModel> articleModelArrayList;
    private RecentBoardAdapter recentBoardAdapter;

    @BindView(R.id.recruit_recyclerView) RecyclerView recyclerView;

    // TODO: Rename and change types and number of parameters
    public static RecentRecruitBoardFragment newInstance() {
        Bundle args = new Bundle();

        RecentRecruitBoardFragment fragment = new RecentRecruitBoardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        //임의의 아이템 클릭 시 list에서 viewCnt를 증가시키는데 다시 목록화면으로
        //돌아왔을 때 변경된 것을 갱신하기 위함.
        if(recentBoardAdapter != null)
            recentBoardAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recent_recruit_board, container, false);
        ButterKnife.bind(this, v);

        init();
        return v;
    }

    private void init(){
        articleModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recentBoardAdapter = new RecentBoardAdapter(getContext(), articleModelArrayList, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recentBoardAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        loadArticleList();
    }

    private void loadArticleList(){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AboutAreaBoardListResponse> call = apiService.getRecentArticleList("recruit");
        call.enqueue(new Callback<AboutAreaBoardListResponse>() {
            @Override
            public void onResponse(Call<AboutAreaBoardListResponse> call, Response<AboutAreaBoardListResponse> response) {
                AboutAreaBoardListResponse aboutAreaBoardListResponse = response.body();
                if(aboutAreaBoardListResponse.getCode() == 200){
                    int size = aboutAreaBoardListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        articleModelArrayList.add(aboutAreaBoardListResponse.getResult().get(i));
                        Log.d("ArticleLIst","boardData No : "+ aboutAreaBoardListResponse.getResult().get(i).getNo()+"\n"+
                                "boardData WriterId : "+aboutAreaBoardListResponse.getResult().get(i).getWriterId()+"\n"+
                                "boardData title"+aboutAreaBoardListResponse.getResult().get(i).getTitle()+"\n"+
                                "boardData contents : "+aboutAreaBoardListResponse.getResult().get(i).getContents()+"\n"+
                                "boardData created_at : "+aboutAreaBoardListResponse.getResult().get(i).getCreatedAt());
                    }
                    recentBoardAdapter.notifyDataSetChanged();
                }else{
                    Util.showToast(getContext(), "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<AboutAreaBoardListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getContext(), "네트워크 연결상태를 확인해주세요.");
            }
        });
    }


}
