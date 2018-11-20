package view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;
import com.skydoves.powermenu.PowerMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.MatchingDateAlarmPresenter;
import presenter.view.MatchingDateAlarmView;

public class MatchingDateAlarmActivity extends BaseActivity implements MatchingDateAlarmView{

    private MatchingDateAlarmPresenter matchingDateAlarmPresenter;
    private PowerMenu selectAreaPowerMenu;

    @BindView(R.id.matching_date_tv) TextView matching_date_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_date_alarm);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        matchingDateAlarmPresenter = new MatchingDateAlarmPresenter(this, getApplicationContext());

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
            matching_date_tv.setText(strAfterFormat);
        }
    };

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
    }

    @OnClick(R.id.area_layout) void areaLayoutClick(){

    }

    @OnClick(R.id.matching_date_layout) void matchingDateLayoutClick(){
        DatePickerDialog dialog = new DatePickerDialog(this, onDateSetListener, GroundApplication.TODAY_YEAR, GroundApplication.TODAY_MONTH-1, GroundApplication.TODAY_DAY);
        dialog.show();
    }

    @OnClick(R.id.add_btn) void addBtn(){

    }
}
