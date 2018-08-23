package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yssh.ground.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgeSelectDialog extends Dialog {

    private ageSelectDialogListener ageSelectDialogListener;

    public AgeSelectDialog(Context context, ageSelectDialogListener ageSelectDialogListener){
        super(context);
        this.ageSelectDialogListener = ageSelectDialogListener;
    }

    public interface ageSelectDialogListener{
        public void ageSelectEvent(int age);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.age_select_dialog);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.age_10_tv, R.id.age_20_tv, R.id.age_30_tv, R.id.age_40_tv, R.id.age_50_tv})
    public void ageClick(View view){
        int age = 10;
        switch (view.getId()){
            case R.id.age_10_tv:
                age = 10;
                break;

            case R.id.age_20_tv:
                age = 20;
                break;

            case R.id.age_30_tv:
                age = 30;
                break;

            case R.id.age_40_tv:
                age = 40;
                break;

            case R.id.age_50_tv:
                age = 50;
                break;
        }
        ageSelectDialogListener.ageSelectEvent(age);
        dismiss();
    }
}
