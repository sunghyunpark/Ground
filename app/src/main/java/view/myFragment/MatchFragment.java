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
import api.response.AboutAreaBoardListResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.ArticleModel;
import model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.MyAdapter;

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
        }else if(type.equals(GroundApplication.MY_COMMENT_TYPE)){
            myAdapter = new MyAdapter(getContext(), objectArrayList, 1, 3);
        }else if(type.equals(GroundApplication.MY_FAVORITE_TYPE)){
            myAdapter = new MyAdapter(getContext(), objectArrayList, 2, 3);
        }

        loadArticleList(type, 0);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void loadArticleList(String type, int no){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AboutAreaBoardListResponse> call = null;

        if(type.equals(GroundApplication.MY_ARTICLE_TYPE)){
            call = apiService.getMyArticleList("match", UserModel.getInstance().getUid(), no);
        }else if(type.equals(GroundApplication.MY_COMMENT_TYPE)){
            call = apiService.getMyArticleList("match", UserModel.getInstance().getUid(), no);
        }else if(type.equals(GroundApplication.MY_FAVORITE_TYPE)){
            call = apiService.getMyArticleList("match", UserModel.getInstance().getUid(), no);
        }
        call.enqueue(new Callback<AboutAreaBoardListResponse>() {
            @Override
            public void onResponse(Call<AboutAreaBoardListResponse> call, Response<AboutAreaBoardListResponse> response) {
                AboutAreaBoardListResponse aboutAreaBoardListResponse = response.body();
                if(aboutAreaBoardListResponse.getCode() == 200){
                    int size = aboutAreaBoardListResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        objectArrayList.add(aboutAreaBoardListResponse.getResult().get(i));
                        Log.d("MyArticleList","boardData No : "+ aboutAreaBoardListResponse.getResult().get(i).getNo()+"\n"+
                                "boardData WriterId : "+aboutAreaBoardListResponse.getResult().get(i).getWriterId()+"\n"+
                                "boardData title"+aboutAreaBoardListResponse.getResult().get(i).getTitle()+"\n"+
                                "boardData contents : "+aboutAreaBoardListResponse.getResult().get(i).getContents()+"\n"+
                                "boardData created_at : "+aboutAreaBoardListResponse.getResult().get(i).getCreatedAt());
                    }
                    myAdapter.notifyDataSetChanged();
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
