package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.groundmobile.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargeSelectDialog extends Dialog {

    private chargeSelectListener chargeSelectListener;
    @BindView(R.id.charge_edit_box) EditText charge_et;

    public ChargeSelectDialog(Context context, chargeSelectListener chargeSelectListener){
        super(context);
        this.chargeSelectListener = chargeSelectListener;
    }

    public interface chargeSelectListener{
        void chargeSelectEvent(int charge);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.charge_select_dialog);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.ok_btn) void okBtn(){
        String chargeStr = charge_et.getText().toString();

        if(chargeStr.equals("")){
            Toast.makeText(getContext(), "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            chargeSelectListener.chargeSelectEvent(Integer.parseInt(chargeStr));
            dismiss();
        }
    }
}
