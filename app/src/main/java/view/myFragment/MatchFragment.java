package view.myFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yssh.ground.GroundApplication;
import com.yssh.ground.R;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.ArticleModelListResponse;
import api.response.CommentListResponse;
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
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

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

        init(type);

        return v;
    }

    private void init(String type){
        objectArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());

        if(type.equals(GroundApplication.MY_ARTICLE_TYPE)){
            myAdapter = new MyAdapter(getContext(), objectArrayList, 0, 3);
            loadArticleList(0);
        }else if(type.equals(GroundApplication.MY_COMMENT_TYPE)){
            myAdapter = new MyAdapter(getContext(), objectArrayList, 1, 3);
            loadCommentList(0);
        }else if(type.equals(GroundApplication.MY_FAVORITE_TYPE)){
            myAdapter = new MyAdapter(getContext(), objectArrayList, 2, 3);
            loadFavoriteList(0);
        }

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
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
                    for(int i=0;i<size;i++){
                        objectArrayList.add(articleModelListResponse.getResult().get(i));
                        Log.d("MyArticleList","boardData No : "+ articleModelListResponse.getResult().get(i).getNo()+"\n"+
                                "boardData WriterId : "+ articleModelListResponse.getResult().get(i).getWriterId()+"\n"+
                                "boardData title"+ articleModelListResponse.getResult().get(i).getTitle()+"\n"+
                                "boardData contents : "+ articleModelListResponse.getResult().get(i).getContents()+"\n"+
                                "boardData created_at : "+ articleModelListResponse.getResult().get(i).getCreatedAt());
                    }
                    myAdapter.notifyDataSetChanged();
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
                    for(int i=0;i<size;i++){
                        objectArrayList.add(commentListResponse.getResult().get(i));
                    }
                    myAdapter.notifyDataSetChanged();
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
                    for(int i=0;i<size;i++){
                        objectArrayList.add(articleModelListResponse.getResult().get(i));
                        Log.d("MyArticleList","boardData No : "+ articleModelListResponse.getResult().get(i).getNo()+"\n"+
                                "boardData WriterId : "+ articleModelListResponse.getResult().get(i).getWriterId()+"\n"+
                                "boardData title"+ articleModelListResponse.getResult().get(i).getTitle()+"\n"+
                                "boardData contents : "+ articleModelListResponse.getResult().get(i).getContents()+"\n"+
                                "boardData created_at : "+ articleModelListResponse.getResult().get(i).getCreatedAt());
                    }
                    myAdapter.notifyDataSetChanged();
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
