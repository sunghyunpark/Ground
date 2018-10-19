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

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.adapter.AreaSearchAdapter;
import view.AreaSearchResultActivity;

public class AreaSearchDialog extends Dialog{

    private AreaSearchAdapter areaSearchAdapter;

    @BindView(R.id.area_recyclerView) RecyclerView areaRecyclerView;

    public AreaSearchDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.area_search_dialog);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        Resources res = getContext().getResources();
        String [] areaNameArray = res.getStringArray(R.array.area_search_list);

        LinearLayoutManager lL = new LinearLayoutManager(getContext());
        areaSearchAdapter = new AreaSearchAdapter(getContext(), areaNameArray);
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
                checkListStr += (i==(checkArrayList.size()-1)) ? checkArrayList.get(i)+1 : (checkArrayList.get(i)+1)+",";
            }
            Intent intent = new Intent(getContext(), AreaSearchResultActivity.class);
            intent.putExtra(GroundApplication.EXTRA_AREA_NO, checkListStr);
            intent.putExtra(GroundApplication.EXTRA_AREA_NAME, "검색");
            getContext().startActivity(intent);
            dismiss();

        }
    }
}
