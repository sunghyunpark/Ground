package view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Date;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import presenter.WriteBoardPresenter;
import presenter.view.WriteBoardView;
import util.PowerMenuUtil;
import util.Util;

public class WriteMatchBoardActivity extends BaseActivity implements WriteBoardView, TextWatcher {

    private static final int MATCH_MODE = 1;
    private static final int HIRE_MODE = 2;
    private static final int RECRUIT_MODE = 3;
    private int boardMode;

    private String area, boardType;
    private int areaNo;
    private String beforeStr;
    private WriteBoardPresenter writeBoardPresenter;

    private PowerMenu agePowerMenu, playRulePowerMenu;

    @BindView(R.id.write_board_layout) ViewGroup write_board_layout;
    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.board_title_et) EditText board_title_et;
    @BindView(R.id.board_contents_et) EditText board_contents_et;
    @BindView(R.id.title_length_tv) TextView title_length_tv;
    @BindView(R.id.matching_date_tv) TextView matchingDateTv;
    @BindView(R.id.charge_et) EditText charge_et;
    @BindView(R.id.play_rule_tv) TextView play_rule_tv;
    @BindView(R.id.matching_date_layout) ViewGroup matchingDateLayout;
    @BindView(R.id.age_tv) TextView ageTv;
    @BindView(R.id.age_layout) ViewGroup ageLayout;
    @BindView(R.id.charge_layout) ViewGroup chargeLayout;
    @BindView(R.id.play_rule_layout) ViewGroup playRuleLayout;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        boardType = intent.getExtras().getString(Constants.EXTRA_MATCH_BOARD_TYPE);
        area = intent.getExtras().getString(Constants.EXTRA_AREA_NAME);
        areaNo = intent.getIntExtra(Constants.EXTRA_AREA_NO,0);

        init();
    }

    private void init(){
        initMode(boardType);

        agePowerMenu = PowerMenuUtil.getAgePowerMenu(getApplicationContext(), this, ageOnMenuItemClickListener, onAgeMenuDismissedListener);
        playRulePowerMenu = PowerMenuUtil.getPlayRulePowerMenu(getApplicationContext(), this, playRuleOnMenuItemClickListener, onPlayRuleMenuDismissedListener);

        board_title_et.addTextChangedListener(this);
        writeBoardPresenter = new WriteBoardPresenter(this, getApplicationContext());
        area_tv.setText(area);
        // 매칭 게시글 쓰기가 아니면 매칭날짜 및 연령 입력 폼 GONE 처리한다.
        if(boardMode == HIRE_MODE){
            ageLayout.setVisibility(View.GONE);
            chargeLayout.setVisibility(View.GONE);
            playRuleLayout.setVisibility(View.GONE);
        }else if(boardMode == RECRUIT_MODE){
            matchingDateLayout.setVisibility(View.GONE);
            ageLayout.setVisibility(View.GONE);
            chargeLayout.setVisibility(View.GONE);
            playRuleLayout.setVisibility(View.GONE);
        }
    }

    // boardType 에 따른 MODE 초기화
    private void initMode(String  boardType){
        if(boardType.equals(Constants.MATCH_OF_BOARD_TYPE_MATCH)){
            boardMode = MATCH_MODE;
        }else if(boardType.equals(Constants.HIRE_OF_BOARD_TYPE_MATCH)){
            boardMode = HIRE_MODE;
        }else{
            boardMode = RECRUIT_MODE;
        }
    }

    @Override
    public void writeBoard(){
        String titleStr = board_title_et.getText().toString().trim();
        String contentsStr = board_contents_et.getText().toString().trim();
        String matchDateStr = (matchingDateTv.getVisibility() == View.GONE) ? "" : matchingDateTv.getText().toString().trim();
        String ageStr = (ageTv.getVisibility() == View.GONE) ? "" : ageTv.getText().toString().trim();
        String chargeStr = (charge_et.getVisibility() == View.GONE) ? "" : charge_et.getText().toString().trim();
        String playRuleStr = (play_rule_tv.getVisibility() == View.GONE) ? "" : play_rule_tv.getText().toString().trim();

        if(titleStr.equals("") || contentsStr.equals("") || ((boardMode == MATCH_MODE) && ((matchDateStr.equals("") || ageStr.equals("") || chargeStr.equals("") || playRuleStr.equals(""))))
                || ((boardMode == HIRE_MODE) && (matchDateStr.equals("")))){
            Util.showToast(getApplicationContext(), errorNotExistInputStr);
        }else{
            String age = ageStr.replace("대", "").replace("이상", "");
            int charge;
            int playRule = 0;
            String[] playRuleArray;
            try{
                charge= Integer.parseInt(chargeStr.replace("원", ""));
                if(playRuleStr.equals("기타")){
                    playRule = 0;
                }else{
                    playRuleArray = playRuleStr.replaceAll("[^-?0-9]+", " ").split(" ");
                    playRule = Integer.parseInt(playRuleArray[0]);
                }
            }catch (NumberFormatException NFE){
                charge = 0;
            }
            writeBoardPresenter.postBoard(areaNo, UserModel.getInstance().getUid(), titleStr, contentsStr, boardType, matchDateStr, age, charge, playRule);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    // 평균 연령 클릭 리스너
    private OnMenuItemClickListener<PowerMenuItem> ageOnMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            agePowerMenu.setSelectedPosition(position); // change selected item
            agePowerMenu.dismiss();
            ageTv.setText(String.valueOf(item.getTitle()));
        }
    };

    // 평균 연령 dismiss 리스너
    private OnDismissedListener onAgeMenuDismissedListener = new OnDismissedListener() {
        @Override
        public void onDismissed() {
        }
    };

    // 경기방식 클릭 리스너
    private OnMenuItemClickListener<PowerMenuItem> playRuleOnMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            playRulePowerMenu.setSelectedPosition(position); // change selected item
            playRulePowerMenu.dismiss();
            play_rule_tv.setText(String.valueOf(item.getTitle()));
        }
    };
    // 경기방식 dismiss 리스너
    private OnDismissedListener onPlayRuleMenuDismissedListener = new OnDismissedListener() {
        @Override
        public void onDismissed() {
        }
    };

    @OnClick({R.id.write_btn, R.id.back_btn, R.id.matching_date_layout, R.id.memo_layout, R.id.age_layout, R.id.play_rule_layout}) void Click(View v){
        switch (v.getId()){
            case R.id.write_btn:
                writeBoard();
                break;
            case R.id.back_btn:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
            case R.id.matching_date_layout:
                DatePickerDialog dialog = new DatePickerDialog(this, onDateSetListener, GroundApplication.TODAY_YEAR, GroundApplication.TODAY_MONTH-1, GroundApplication.TODAY_DAY);
                dialog.show();
                break;
            case R.id.memo_layout:
                Intent memoIntent = new Intent(getApplicationContext(), MemoActivity.class);
                startActivity(memoIntent);
                break;
            case R.id.age_layout:
                agePowerMenu.showAtCenter(write_board_layout);
                break;
            case R.id.play_rule_layout:
                playRulePowerMenu.showAtCenter(write_board_layout);
                break;
        }
    }


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
            matchingDateTv.setText(strAfterFormat);
        }
    };

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() >= 50)
        {
            showMessage("50자까지 입력이 가능합니다.");
            board_title_et.setText(beforeStr);
        }
        title_length_tv.setText(s.length() + "/50");
        title_length_tv.setTextColor(getResources().getColor(R.color.colorTextHintGray));
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeStr = s.toString();

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void onBackPressed() {
        if(agePowerMenu.isShowing())
            agePowerMenu.dismiss();
        else if(playRulePowerMenu.isShowing())
            playRulePowerMenu.dismiss();
        else
            super.onBackPressed();
    }
}
