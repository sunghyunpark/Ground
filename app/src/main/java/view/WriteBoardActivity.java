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
import model.UserModel;
import presenter.WriteBoardPresenter;
import presenter.view.WriteBoardView;
import util.Util;
import view.dialog.AgeSelectDialog;

public class WriteBoardActivity extends BaseActivity implements WriteBoardView, TextWatcher {

    private static final int MATCH_MODE = 1;
    private static final int HIRE_MODE = 2;
    private static final int RECRUIT_MODE = 3;
    private int boardMode;

    private String area, boardType;
    private int areaNo;
    private String beforeStr;
    private WriteBoardPresenter writeBoardPresenter;
    private int year, month, day;

    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.board_title_et) EditText board_title_et;
    @BindView(R.id.board_contents_et) EditText board_contents_et;
    @BindView(R.id.title_length_tv) TextView title_length_tv;
    @BindView(R.id.matching_date_tv) TextView matchingDateTv;
    @BindView(R.id.matching_date_layout) ViewGroup matchingDateLayout;
    @BindView(R.id.age_tv) TextView ageTv;
    @BindView(R.id.age_layout) ViewGroup ageLayout;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;
    @BindString(R.string.write_board_default_txt) String matchDefaultStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        boardType = intent.getExtras().getString("boardType");
        area = intent.getExtras().getString("area");
        areaNo = intent.getIntExtra("areaNo",0);

        init();
    }

    private void init(){
        initMode(boardType);

        board_title_et.addTextChangedListener(this);
        writeBoardPresenter = new WriteBoardPresenter(this, getApplicationContext());
        area_tv.setText(area);
//        if(boardType.equals("match")){
//            board_contents_et.setText(matchDefaultStr);
//        }
        // 매칭 게시글 쓰기가 아니면 매칭날짜 및 연령 입력 폼 GONE 처리한다.
        if(boardMode != MATCH_MODE){
            matchingDateLayout.setVisibility(View.GONE);
            ageLayout.setVisibility(View.GONE);
        }
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;
        day = cal.get(Calendar.DATE);
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
    public void writeBoard(){
        String titleStr = board_title_et.getText().toString().trim();
        String contentsStr = board_contents_et.getText().toString().trim();
        String matchDateStr = matchingDateTv.getText().toString().trim();
        String ageStr = ageTv.getText().toString().trim();

        if(titleStr.equals("") || contentsStr.equals("") || matchDateStr.equals("") || ageStr.equals("")){
            Util.showToast(getApplicationContext(), errorNotExistInputStr);
        }else{
            String age = ageStr.replace("대", "");
            writeBoardPresenter.postBoard(areaNo, UserModel.getInstance().getUid(), titleStr, contentsStr, boardType, matchDateStr, age);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @OnClick(R.id.write_btn) void writeBtn(){
        writeBoard();
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
