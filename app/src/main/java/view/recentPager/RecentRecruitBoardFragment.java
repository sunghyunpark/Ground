package view.recentPager;

import android.os.Bundle;
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
import api.response.ArticleModelListResponse;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.ArticleModel;
import presenter.view.RecentBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.RecentBoardAdapter;

public class RecentRecruitBoardFragment extends BaseFragment implements RecentBoardView{

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
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recent_recruit_board, container, false);
        ButterKnife.bind(this, v);

        initUI();
        return v;
    }

    private void init(){
        articleModelArrayList = new ArrayList<>();
        recentBoardAdapter = new RecentBoardAdapter(getContext(), articleModelArrayList, 2);
        setListData();
    }

    private void initUI(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recentBoardAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void setListData(){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getRecentArticleList("recruit");
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        articleModelArrayList.add(articleModelListResponse.getResult().get(i));
                        Log.d("ArticleLIst","boardData No : "+ articleModelListResponse.getResult().get(i).getNo()+"\n"+
                                "boardData WriterId : "+ articleModelListResponse.getResult().get(i).getWriterId()+"\n"+
                                "boardData title"+ articleModelListResponse.getResult().get(i).getTitle()+"\n"+
                                "boardData contents : "+ articleModelListResponse.getResult().get(i).getContents()+"\n"+
                                "boardData created_at : "+ articleModelListResponse.getResult().get(i).getCreatedAt());
                    }
                    recentBoardAdapter.notifyDataSetChanged();
                }else{
                    Util.showToast(getContext(), "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<ArticleModelListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getContext(), "네트워크 연결상태를 확인해주세요.");
            }
        });
    }
}
