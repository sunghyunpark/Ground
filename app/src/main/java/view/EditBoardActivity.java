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
import com.groundmobile.ground.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.MatchArticleModel;
import presenter.EditBoardPresenter;
import presenter.view.EditBoardView;
import util.Util;
import view.dialog.AgeSelectDialog;
import view.dialog.ChargeSelectDialog;
import view.dialog.PlayRuleSelectDialog;

public class EditBoardActivity extends BaseActivity implements EditBoardView, TextWatcher {

    private static final int MATCH_MODE = 1;
    private static final int HIRE_MODE = 2;
    private static final int RECRUIT_MODE = 3;
    private int boardMode;

    private String area;
    private MatchArticleModel matchArticleModel;
    private String beforeStr;
    private EditBoardPresenter editBoardPresenter;

    private int year, month, day;

    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.board_title_et) EditText board_title_et;
    @BindView(R.id.board_contents_et) EditText board_contents_et;
    @BindView(R.id.title_length_tv) TextView title_length_tv;
    @BindView(R.id.matching_date_tv) TextView matchingDateTv;
    @BindView(R.id.matching_date_layout) ViewGroup matchingDateLayout;
    @BindView(R.id.charge_tv) TextView charge_tv;
    @BindView(R.id.charge_layout) ViewGroup chargeLayout;
    @BindView(R.id.play_rule_tv) TextView play_rule_tv;
    @BindView(R.id.play_rule_layout) ViewGroup playRuleLayout;
    @BindView(R.id.age_tv) TextView ageTv;
    @BindView(R.id.age_layout) ViewGroup ageLayout;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString(Constants.EXTRA_AREA_NAME);
        matchArticleModel = new MatchArticleModel();
        matchArticleModel = (MatchArticleModel)intent.getExtras().getSerializable(Constants.EXTRA_ARTICLE_MODEL);

        init();
    }

    private void init(){
        initMode(matchArticleModel.getMatchBoardType());

        editBoardPresenter = new EditBoardPresenter(this, getApplicationContext());
        board_title_et.addTextChangedListener(this);
        area_tv.setText(area);
        board_title_et.setText(matchArticleModel.getTitle());
        board_contents_et.setText(matchArticleModel.getContents());

        // 매칭 게시글 쓰기가 아니면 매칭날짜 및 연령 입력 폼 GONE 처리한다.
        if(boardMode == MATCH_MODE){
            matchingDateTv.setText(matchArticleModel.getMatchDate());
            ageTv.setText(matchArticleModel.getAverageAge()+"대");
            charge_tv.setText(matchArticleModel.getCharge()+"원");

            if(matchArticleModel.getPlayRule() == 0){
                play_rule_tv.setText("기타");
            }else{
                play_rule_tv.setText(matchArticleModel.getPlayRule()+" VS "+matchArticleModel.getPlayRule());
            }

            String [] matchDateArray;
            try{
                matchDateArray= matchArticleModel.getMatchDate().split("-");
                year = Integer.parseInt(matchDateArray[0]);
                month = Integer.parseInt(matchDateArray[1]);
                day = Integer.parseInt(matchDateArray[2]);
            }catch (NumberFormatException e){
                //matchArticleModel 에 데이터가 없어 포맷 exception 이 발생하는 경우
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH)+1;
                day = cal.get(Calendar.DATE);
            }
        }else{
            matchingDateLayout.setVisibility(View.GONE);
            ageLayout.setVisibility(View.GONE);
            chargeLayout.setVisibility(View.GONE);
            playRuleLayout.setVisibility(View.GONE);
        }
    }

    // boardType 에 따른 MODE 초기화
    private void initMode(String  boardType){
        if(boardType.equals("match")){
            boardMode = MATCH_MODE;
        }else if(boardType.equals("hire")){
            boardMode = HIRE_MODE;
        }else{
            boardMode = RECRUIT_MODE;
        }
    }

    @Override
    public void EditBoard(){
        String titleStr = board_title_et.getText().toString().trim();
        String contentsStr = board_contents_et.getText().toString().trim();
        String matchDateStr = (matchingDateTv.getVisibility() == View.GONE) ? "" : matchingDateTv.getText().toString().trim();
        String ageStr = (ageTv.getVisibility() == View.GONE) ? "" : ageTv.getText().toString().trim();
        String chargeStr = (charge_tv.getVisibility() == View.GONE) ? "" : charge_tv.getText().toString().trim();
        String playRuleStr = (play_rule_tv.getVisibility() == View.GONE) ? "" : play_rule_tv.getText().toString().trim();

        if(titleStr.equals("") || contentsStr.equals("") || ((boardMode == MATCH_MODE) && ((matchDateStr.equals("") || ageStr.equals("") || chargeStr.equals("") || playRuleStr.equals(""))))){
            Util.showToast(getApplicationContext(), errorNotExistInputStr);
        }else{
            matchArticleModel.setTitle(titleStr);
            matchArticleModel.setContents(contentsStr);
            if(boardMode == MATCH_MODE){
                matchArticleModel.setMatchDate(matchingDateTv.getText().toString().trim());
                matchArticleModel.setAverageAge(ageTv.getText().toString().trim().replace("대", ""));

                int charge = Integer.parseInt(chargeStr.replace("원", ""));
                matchArticleModel.setCharge(charge);

                int playRule;
                String[] playRuleArray;
                if(playRuleStr.equals("기타")){
                    playRule = 0;
                }else{
                    playRuleArray = playRuleStr.replaceAll("[^-?0-9]+", " ").split(" ");
                    playRule = Integer.parseInt(playRuleArray[0]);
                }
                matchArticleModel.setPlayRule(playRule);
            }
            editBoardPresenter.EditBoard(matchArticleModel);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    @OnClick(R.id.write_btn) void writeBtn(){
        EditBoard();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @OnClick(R.id.matching_date_layout) void matchingDateClick(){
        DatePickerDialog dialog = new DatePickerDialog(this, onDateSetListener, year, month-1, day);

        dialog.show();
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

    @OnClick(R.id.age_layout) void ageClick(){
        AgeSelectDialog ageSelectDialog = new AgeSelectDialog(this, new AgeSelectDialog.ageSelectDialogListener() {
            @Override
            public void ageSelectEvent(int age) {
                ageTv.setText(String.valueOf(age)+"대");
            }
        });
        ageSelectDialog.show();
    }

    @OnClick(R.id.charge_layout) void chargeClick(){
        ChargeSelectDialog chargeSelectDialog = new ChargeSelectDialog(this, new ChargeSelectDialog.chargeSelectListener() {
            @Override
            public void chargeSelectEvent(int charge) {
                charge_tv.setText(charge+"원");
            }
        });
        chargeSelectDialog.show();
    }

    @OnClick(R.id.play_rule_layout) void playRuleClick(){
        PlayRuleSelectDialog playRuleSelectDialog = new PlayRuleSelectDialog(this, new PlayRuleSelectDialog.playRuleSelectListener() {
            @Override
            public void playRuleSelectEvent(int playRule) {
                if(playRule == 0){
                    play_rule_tv.setText("기타");
                }else{
                    play_rule_tv.setText(playRule+" VS "+playRule);
                }
            }
        });
        playRuleSelectDialog.show();
    }

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
}
