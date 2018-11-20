package presenter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import api.ApiClient;
import api.ApiInterface;
import api.response.CommonResponse;
import api.response.MatchDateAlarmListResponse;
import base.presenter.BasePresenter;
import model.MatchingDateAlarmModel;
import model.UserModel;
import presenter.view.MatchingDateAlarmView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class MatchingDateAlarmPresenter extends BasePresenter<MatchingDateAlarmView> {

    private Context context;
    private ApiInterface apiService;
    private Resources res;
    private String[] matchAreaNameArray;
    private String[] hireAreaNameArray;

    public MatchingDateAlarmPresenter(MatchingDateAlarmView view, Context context){
        super(view);
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
        this.res = context.getResources();
        this.matchAreaNameArray = res.getStringArray(R.array.matching_board_list);
        this.hireAreaNameArray = res.getStringArray(R.array.hire_board_list);
    }

    public void loadAlarmList(final String type, final ArrayList<MatchingDateAlarmModel> matchingDateAlarmModelArrayList){
        Call<MatchDateAlarmListResponse> call = apiService.getMatchDateAlarmList(UserModel.getInstance().getUid(), type);
        call.enqueue(new Callback<MatchDateAlarmListResponse>() {
            @Override
            public void onResponse(Call<MatchDateAlarmListResponse> call, Response<MatchDateAlarmListResponse> response) {
                MatchDateAlarmListResponse matchDateAlarmListResponse = response.body();
                if(matchDateAlarmListResponse.getResult().size() > 0) {
                    for(int i=0;i<matchDateAlarmListResponse.getResult().size();i++){
                        if(type.equals(Constants.MATCH_OF_BOARD_TYPE_MATCH)){
                            matchDateAlarmListResponse.getResult().get(i).setAreaName(matchAreaNameArray[matchDateAlarmListResponse.getResult().get(i).getAreaNo()]);
                        }else if(type.equals(Constants.HIRE_OF_BOARD_TYPE_MATCH)){
                            matchDateAlarmListResponse.getResult().get(i).setAreaName(hireAreaNameArray[matchDateAlarmListResponse.getResult().get(i).getAreaNo()]);
                        }
                        matchingDateAlarmModelArrayList.add(matchDateAlarmListResponse.getResult().get(i));
                    }
                    getView().loadComplete(false);
                }else{
                    getView().loadComplete(true);
                }
            }

            @Override
            public void onFailure(Call<MatchDateAlarmListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
                getView().loadComplete(true);
            }
        });
    }

    public void registerAlarm(String type, MatchingDateAlarmModel matchingDateAlarmModel){
        Call<CommonResponse> call = apiService.registerMatchDateAlarm(UserModel.getInstance().getUid(), type, matchingDateAlarmModel.getAreaNo(), matchingDateAlarmModel.getMatchDate());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "알림이 등록되었습니다.");
                    getView().registerComplete(false);
                }else{
                    Util.showToast(context, "에러가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                    getView().registerComplete(true);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
                getView().registerComplete(true);
            }
        });
    }

    public void unregisterAlarm(String type, MatchingDateAlarmModel matchingDateAlarmModel){

    }

}
