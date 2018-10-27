package view.myFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import api.response.CommentListResponse;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.MyAdapter;

/**
 * MY > 내가 쓴 글, 내가 쓴 댓글, 관심을 눌렀을 때 매칭 화면
 * type을 파라미터로 받아온다. 이때 파라미터값은 GroundApplication 의 static 값으로 전달받는다.
 * type 에 따라 분기처리된다.
 */
public class MatchFragment extends Fragment {
    private View v;
    private String type;    //내가 쓴 글, 내가 쓴 댓글, 관심 글(myArticle, myComment, myFavorite)
    private ArrayList<Object> objectArrayList;
    private MyAdapter myAdapter;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.empty_tv) TextView empty_tv;
    @BindString(R.string.empty_tv_comment) String emptyCommentStr;
    @BindString(R.string.empty_tv_article) String emptyArticleStr;
    @BindString(R.string.empty_tv_favorite_article) String emptyFavoriteStr;

    // TODO: Rename and change types and number of parameters
    public static MatchFragment newInstance(String type) {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        //임의의 아이템 클릭 시 list에서 viewCnt를 증가시키는데 다시 목록화면으로
        //돌아왔을 때 변경된 것을 갱신하기 위함.
        init();
        initUI();
        if(myAdapter != null)
            myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_match, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    private void init(){
        int typeNo = 0;
        objectArrayList = new ArrayList<>();

        switch (type){
            case Constants.MY_ARTICLE_TYPE :
                loadArticleList(0);
                typeNo = 0;
                break;
            case Constants.MY_COMMENT_TYPE :
                loadCommentList(0);
                typeNo = 1;
                break;
            case Constants.MY_FAVORITE_TYPE :
                loadFavoriteList(0);
                typeNo = 2;
                break;
        }
        myAdapter = new MyAdapter(getContext(), objectArrayList, typeNo, 3);
    }

    private void initUI(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);

        switch (type){
            case Constants.MY_ARTICLE_TYPE :
                empty_tv.setText(emptyArticleStr);
                break;
            case Constants.MY_COMMENT_TYPE :
                empty_tv.setText(emptyCommentStr);
                break;
            case Constants.MY_FAVORITE_TYPE :
                empty_tv.setText(emptyFavoriteStr);
                break;
        }
    }

    /**
     * 내가 쓴 글
     * @param no
     */
    private void loadArticleList(int no){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getMyArticleList("match", UserModel.getInstance().getUid(), no);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();

                    if(size > 0){
                        empty_tv.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        for(Object ob : articleModelListResponse.getResult()){
                            Collections.addAll(objectArrayList, ob);
                        }
                        myAdapter.notifyDataSetChanged();
                    }else{
                        empty_tv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
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

    /**
     * 내가 쓴 댓글
     * @param no
     */
    private void loadCommentList(int no){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommentListResponse> call = apiService.getMyCommentList("match", UserModel.getInstance().getUid(), no);
        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                CommentListResponse commentListResponse = response.body();
                if(commentListResponse.getCode() == 200){
                    int size = commentListResponse.getResult().size();

                    if(size > 0){
                        empty_tv.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        for(Object ob : commentListResponse.getResult()){
                            Collections.addAll(objectArrayList, ob);
                        }
                        myAdapter.notifyDataSetChanged();
                    }else{
                        empty_tv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }else{
                    Util.showToast(getContext(), "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getContext(), "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    /**
     * 관심 글
     * @param no
     */
    private void loadFavoriteList(int no){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleModelListResponse> call = apiService.getMyFavoriteArticleList("match", UserModel.getInstance().getUid(), no);
        call.enqueue(new Callback<ArticleModelListResponse>() {
            @Override
            public void onResponse(Call<ArticleModelListResponse> call, Response<ArticleModelListResponse> response) {
                ArticleModelListResponse articleModelListResponse = response.body();
                if(articleModelListResponse.getCode() == 200){
                    int size = articleModelListResponse.getResult().size();

                    if(size > 0){
                        empty_tv.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        for(Object ob : articleModelListResponse.getResult()){
                            Collections.addAll(objectArrayList, ob);
                        }
                        myAdapter.notifyDataSetChanged();
                    }else{
                        empty_tv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
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
