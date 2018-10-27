package view.recentPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.MatchArticleModel;
import model.UserModel;
import presenter.view.RecentBoardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.EndlessRecyclerOnScrollListener;
import util.Util;
import util.adapter.RecentBoardAdapter;
import view.DetailMatchArticleActivity;

public class RecentMatchBoardFragment extends BaseFragment implements RecentBoardView {

    private static final int REQUEST_DETAIL = 2000;
    private static final int LOAD_MORE_DATA_COUNT = 20;
    private ArrayList<MatchArticleModel> matchArticleModelArrayList;
    private RecentBoardAdapter recentBoardAdapter;
    private int limit;    // 홈에서 보이는 최신글과 더보기를 통해 진입했을 경우 불러오는 데이터 갯수가 다르기 때문에 사용
    private boolean isMore;
    private int detailPosition;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @BindView(R.id.match_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.recent_empty_tv) TextView recentEmpty_tv;

    // TODO: Rename and change types and number of parameters
    public static RecentMatchBoardFragment newInstance(boolean isMore, int limit) {
        Bundle args = new Bundle();

        RecentMatchBoardFragment fragment = new RecentMatchBoardFragment();
        args.putInt("limit", limit);
        args.putBoolean("isMore", isMore);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(recentBoardAdapter != null)
            recentBoardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            limit = getArguments().getInt("limit");
            isMore = getArguments().getBoolean("isMore");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recent_match_board, container, false);
        ButterKnife.bind(this, v);

        init();
        initUI();

        return v;
    }

    private void init(){
        matchArticleModelArrayList = new ArrayList<>();
        recentBoardAdapter = new RecentBoardAdapter(getContext(), matchArticleModelArrayList, 2, new RecentBoardAdapter.RecentBoardAdapterListener() {
            @Override
            public void goToDetailArticle(int position, String area, MatchArticleModel matchArticleModel) {
                detailPosition = position;
                Intent intent = new Intent(getContext(), DetailMatchArticleActivity.class);
                intent.putExtra(Constants.EXTRA_AREA_NAME, area);
                intent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
                intent.putExtra(Constants.EXTRA_EXIST_ARTICLE_MODEL, true);
                intent.putExtra(Constants.EXTRA_USER_ID, UserModel.getInstance().getUid());
                startActivityForResult(intent, REQUEST_DETAIL);
            }
        });
    }

    private void initUI(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recentBoardAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        //LoadMore 리스너 등록
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_MORE_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!matchArticleModelArrayList.isEmpty()){
                    setListData(false, matchArticleModelArrayList.get(matchArticleModelArrayList.size()-1).getNo());
                }
            }
        };

        //최초 데이터를 받아온다.
        setListData(true, 0);

        if(isMore){
            // 더보기 상세 화면인 경우에만 load more 리스너를 사용한다.
            recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        }
    }

    /**
     * 홈 > 최신글 , 더보기 화면에서 상세글 진입 후 돌아왔을 때 해당 게시글 갱신하기 위함.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_DETAIL){
            if(resultCode == Activity.RESULT_OK){
                matchArticleModelArrayList.set(detailPosition, (MatchArticleModel)data.getExtras().getSerializable(Constants.EXTRA_ARTICLE_MODEL));
                recentBoardAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setListData(boolean refresh, int articleNo){
        if(refresh){
            matchArticleModelArrayList.clear();
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getRecentArticleList(Constants.MATCH_OF_BOARD_TYPE_MATCH, articleNo, limit);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();
                    if(size > 0){
                        for(MatchArticleModel am : articleModelListResponse.getResult()){
                            Collections.addAll(matchArticleModelArrayList, am);
                        }
                        recentBoardAdapter.notifyDataSetChanged();
                    }
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
