package view.recentPager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.MatchArticleModel;
import presenter.view.RecentBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.RecentBoardAdapter;

public class RecentHireBoardFragment extends BaseFragment implements RecentBoardView {

    private ArrayList<MatchArticleModel> matchArticleModelArrayList;
    private RecentBoardAdapter recentBoardAdapter;
    private int limit;    // 홈에서 보이는 최신글과 더보기를 통해 진입했을 경우 불러오는 데이터 갯수가 다르기 때문에 사용

    @BindView(R.id.hire_recyclerView) RecyclerView recyclerView;

    // TODO: Rename and change types and number of parameters
    public static RecentHireBoardFragment newInstance(int limit) {
        Bundle args = new Bundle();

        RecentHireBoardFragment fragment = new RecentHireBoardFragment();
        args.putInt("limit", limit);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        //임의의 아이템 클릭 시 list에서 viewCnt를 증가시키는데 다시 목록화면으로
        //돌아왔을 때 변경된 것을 갱신하기 위함.
        setListData();
        if(recentBoardAdapter != null)
            recentBoardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            limit = getArguments().getInt("limit");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recent_hire_board, container, false);
        ButterKnife.bind(this, v);

        init();
        initUI();

        return v;
    }

    private void init(){
        matchArticleModelArrayList = new ArrayList<>();
        recentBoardAdapter = new RecentBoardAdapter(getContext(), matchArticleModelArrayList, 3);
    }

    private void initUI(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recentBoardAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void setListData(){
        if(!matchArticleModelArrayList.isEmpty()){
            matchArticleModelArrayList.clear();
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getRecentArticleList(GroundApplication.HIRE_OF_BOARD_TYPE_MATCH, 0, limit);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        matchArticleModelArrayList.add(articleModelListResponse.getResult().get(i));
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
