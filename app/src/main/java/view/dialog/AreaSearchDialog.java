package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.AreaModel;
import util.adapter.AreaSearchAdapter;
import view.AreaSearchResultActivity;

public class AreaSearchDialog extends Dialog{

    private AreaSearchAdapter areaSearchAdapter;
    private int currentPage;    // 게시판 화면에서 매치,용병,모집 중 어디로 부터 진입했는 지 확인하기 위한 변수
    private String boardType;    // 게시판 화면에서 매치, 용병, 모집 중 어디로부터 진입했는지 확인하기 위한 변수

    @BindView(R.id.area_recyclerView) RecyclerView areaRecyclerView;

    public AreaSearchDialog(Context context, int currentPage){
        super(context);
        this.currentPage = currentPage;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.area_search_dialog);

        ButterKnife.bind(this);

        init(currentPage);
    }

    private void init(int currentPage){
        ArrayList<AreaModel> areaModelArrayList = new ArrayList<>();

        Resources res = getContext().getResources();
        String [] areaNameArray = null;
        switch (currentPage){
            case 0 :
                areaNameArray = res.getStringArray(R.array.matching_board_list);
                boardType = Constants.MATCH_OF_BOARD_TYPE_MATCH;
                break;
            case 1:
                areaNameArray = res.getStringArray(R.array.hire_board_list);
                boardType = Constants.HIRE_OF_BOARD_TYPE_MATCH;
                break;
            case 2:
                areaNameArray = res.getStringArray(R.array.recruit_board_list);
                boardType = Constants.RECRUIT_OF_BOARD_TYPE_MATCH;
                break;
        }

        AreaModel areaModel;
        for(int i=0;i<areaNameArray.length;i++){
            areaModel = new AreaModel();
            areaModel.setAreaNo(i);
            areaModel.setAreaName(areaNameArray[i]);
            areaModelArrayList.add(areaModel);
        }

        LinearLayoutManager lL = new LinearLayoutManager(getContext());
        areaSearchAdapter = new AreaSearchAdapter(areaModelArrayList, boardType);
        areaRecyclerView.setLayoutManager(lL);
        areaRecyclerView.setAdapter(areaSearchAdapter);

    }

    @OnClick(R.id.search_btn) void searchBtn(){
        String checkListStr = "";
        ArrayList<Integer> checkArrayList = areaSearchAdapter.getCheckList();

        if(checkArrayList.isEmpty()){
            Toast.makeText(getContext(), "지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0;i<checkArrayList.size();i++){
                checkListStr += (i==(checkArrayList.size()-1)) ? checkArrayList.get(i) : (checkArrayList.get(i))+",";
            }
            Intent intent = new Intent(getContext(), AreaSearchResultActivity.class);
            intent.putExtra(Constants.EXTRA_AREA_NO, checkListStr);
            intent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, boardType);
            getContext().startActivity(intent);
            dismiss();

        }
    }
}
