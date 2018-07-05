package view.boardPager;

import android.content.res.Resources;
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
import api.response.UpdateTimeResponse;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.AreaModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;
import util.adapter.BoardAreaAdapter;

public class MatchBoardFragment extends BaseFragment {

    private ArrayList<AreaModel> areaModelArrayList;
    private String[] areaNameArray;
    @BindView(R.id.area_recyclerView) RecyclerView areaRecyclerView;

    public static MatchBoardFragment newInstance(){
        Bundle args = new Bundle();

        MatchBoardFragment fragment = new MatchBoardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_match, container, false);
        ButterKnife.bind(this, v);

        init();

        return v;
    }

    /**
     * init
     */
    private void init(){
        Resources res = getResources();
        areaNameArray = res.getStringArray(R.array.matching_board_list);
        areaModelArrayList = new ArrayList<>();

        loadUpdateList();
    }

    private void loadUpdateList(){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<UpdateTimeResponse> call = apiService.getUpdateTimeList();
        call.enqueue(new Callback<UpdateTimeResponse>() {
            @Override
            public void onResponse(Call<UpdateTimeResponse> call, Response<UpdateTimeResponse> response) {
                UpdateTimeResponse updateTimeResponse = response.body();
                if(updateTimeResponse.getCode() == 200){
                    int size = updateTimeResponse.getResult().size();
                    for(int i=0;i<size;i++){
                        updateTimeResponse.getResult().get(i).setAreaName(areaNameArray[i]);
                        areaModelArrayList.add(updateTimeResponse.getResult().get(i));
                        Log.d("updateTime", updateTimeResponse.getResult().get(i).getUpdatedAt());
                    }
                    setRecyclerView(areaModelArrayList);
                }else{
                    Util.showToast(getActivity(), "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                    setErrorList();
                }
            }

            @Override
            public void onFailure(Call<UpdateTimeResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getActivity(), "네트워크 연결상태를 확인해주세요.");
                setErrorList();
            }
        });
    }

    private void setErrorList(){
        int size = areaNameArray.length;
        AreaModel areaModel;
        for(int i=0;i<size;i++){
            areaModel = new AreaModel();
            areaModel.setAreaNo(i);
            areaModel.setAreaName(areaNameArray[i]);
            areaModel.setUpdatedAt("0000-00-00");

            areaModelArrayList.add(areaModel);
        }
        setRecyclerView(areaModelArrayList);
    }

    private void setRecyclerView(ArrayList<AreaModel> areaModelArrayList){
        LinearLayoutManager lL = new LinearLayoutManager(getActivity());
        BoardAreaAdapter areaOfAdapter = new BoardAreaAdapter(getActivity(), 2, areaModelArrayList);
        areaRecyclerView.setLayoutManager(lL);
        areaRecyclerView.setAdapter(areaOfAdapter);
        areaOfAdapter.notifyDataSetChanged();
    }
}
