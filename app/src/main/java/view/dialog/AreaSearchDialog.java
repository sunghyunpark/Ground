package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.groundmobile.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.adapter.AreaSearchAdapter;

public class AreaSearchDialog extends Dialog{

    private String [] areaNameArray;

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
        areaNameArray = res.getStringArray(R.array.matching_board_list);

        LinearLayoutManager lL = new LinearLayoutManager(getContext());
        AreaSearchAdapter areaSearchAdapter = new AreaSearchAdapter(getContext(), areaNameArray);
        areaRecyclerView.setLayoutManager(lL);
        areaRecyclerView.setAdapter(areaSearchAdapter);

    }
}
