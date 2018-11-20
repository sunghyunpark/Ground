package view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.MatchingDateAlarmModel;
import presenter.MatchingDateAlarmPresenter;
import presenter.view.MatchingDateAlarmView;
import util.PowerMenuUtil;
import util.adapter.MatchingDateAlarmAdapter;

public class MatchingDateAlarmActivity extends BaseActivity implements MatchingDateAlarmView{

    private String type;    // match / hire 알림인지 판별하기 위한 변수
    private int areaNo = -1;    //지역 번호를 알기 위한 변수 초기값은 -1
    private MatchingDateAlarmPresenter matchingDateAlarmPresenter;
    private PowerMenu selectAreaPowerMenu;
    private MatchingDateAlarmAdapter matchingDateAlarmAdapter;
    private ArrayList<MatchingDateAlarmModel> matchingDateAlarmModelArrayList;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.matching_date_tv) TextView matching_date_tv;
    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.alarm_recyclerView) RecyclerView alarmRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_date_alarm);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        type = intent.getExtras().getString(Constants.EXTRA_MATCH_BOARD_TYPE);

        init(type);
    }

    private void init(String type){
        matchingDateAlarmPresenter = new MatchingDateAlarmPresenter(this, getApplicationContext());
        selectAreaPowerMenu = PowerMenuUtil.getAreaListMenu(getApplicationContext(), this, areaOnMenuItemClickListener, onMoreMenuDismissedListener, type);
        matchingDateAlarmModelArrayList = new ArrayList<>();
        matchingDateAlarmAdapter = new MatchingDateAlarmAdapter(matchingDateAlarmModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        alarmRecyclerview.setLayoutManager(linearLayoutManager);
        alarmRecyclerview.setAdapter(matchingDateAlarmAdapter);
    }

    /**
     * 설정 조건들 초기화
     */
    private void initSetting(){
        areaNo = -1;
        area_tv.setText(null);
        matching_date_tv.setText(null);
    }

    // 지역 선택 리스너
    private OnMenuItemClickListener<PowerMenuItem> areaOnMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            //selectAreaPowerMenu.setSelectedPosition(position); // change selected item
            selectAreaPowerMenu.dismiss();
            area_tv.setText(item.getTitle());

            Resources res = getResources();
            String[] matchAreaNameArray = res.getStringArray(R.array.matching_board_list);
            String[] hireAreaNameArray = res.getStringArray(R.array.hire_board_list);

            if(type.equals(Constants.MATCH_OF_BOARD_TYPE_MATCH)){
                for(int i=0;i<matchAreaNameArray.length;i++){
                    if(item.getTitle().equals(matchAreaNameArray[i])){
                        areaNo = i;
                        break;
                    }
                }
            }else if(type.equals(Constants.HIRE_OF_BOARD_TYPE_MATCH)){
                for(int i=0;i<hireAreaNameArray.length;i++){
                    if(item.getTitle().equals(hireAreaNameArray[i])){
                        areaNo = i;
                        break;
                    }
                }
            }

        }
    };

    // 지역 dismiss 리스너
    private OnDismissedListener onMoreMenuDismissedListener = new OnDismissedListener() {
        @Override
        public void onDismissed() {
        }
    };

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-M-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String strBeforeFormat = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            String strAfterFormat = "";
            try {
                Date originDate = originFormat.parse(strBeforeFormat);

                strAfterFormat = newFormat.format(originDate);
            }catch (ParseException e){
                e.printStackTrace();
            }
            matching_date_tv.setText(strAfterFormat);
        }
    };

    @OnClick({R.id.back_btn, R.id.area_layout, R.id.matching_date_layout, R.id.add_btn}) void onClick(View v){
        switch (v.getId()){

            case R.id.back_btn:    // 뒤로가기
                finish();
                break;

            case R.id.area_layout:    // 지역 선택 탭
                selectAreaPowerMenu.showAtCenter(area_tv);
                break;

            case R.id.matching_date_layout:    // 시합날짜 선택 탭
                DatePickerDialog dialog = new DatePickerDialog(this, onDateSetListener, GroundApplication.TODAY_YEAR, GroundApplication.TODAY_MONTH-1, GroundApplication.TODAY_DAY);
                dialog.show();
                break;

            case R.id.add_btn:    // 추가하기 탭
                if((areaNo < 0) || (area_tv.getText().toString().equals("")) || matching_date_tv.getText().toString().equals("")){
                    showMessage("정보를 입력해주세요.");
                }else{
                    MatchingDateAlarmModel matchingDateAlarmModel = new MatchingDateAlarmModel();
                    matchingDateAlarmModel.setAreaNo(areaNo);
                    matchingDateAlarmModel.setAreaName(area_tv.getText().toString());
                    matchingDateAlarmModel.setMatchingDate(matching_date_tv.getText().toString());

                    matchingDateAlarmModelArrayList.add(matchingDateAlarmModel);
                    matchingDateAlarmAdapter.notifyDataSetChanged();
                    initSetting();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(selectAreaPowerMenu.isShowing())
            selectAreaPowerMenu.dismiss();
        else
            super.onBackPressed();
    }
}
