package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.yssh.ground.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;

public class EditProfileDialog extends Dialog{

    private Context context;
    private EditProfileDialogListener editProfileDialogListener;

    @BindView(R.id.user_name_et) EditText user_name_et;

    public EditProfileDialog(Context context, EditProfileDialogListener editProfileDialogListener){
        super(context);
        this.editProfileDialogListener = editProfileDialogListener;
    }

    public interface EditProfileDialogListener{
        public void editUserNameEvent();
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.edit_profile_dialog);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        user_name_et.setText(UserModel.getInstance().getNickName());
    }

    @OnClick(R.id.cancel_btn) void cancelBtn(){
        dismiss();
    }

    @OnClick(R.id.save_btn) void saveBtn(){
        if(user_name_et.getText().toString().length() > 10){
            Toast.makeText(getContext(), "이름(별명)은 최대 10자까지 입니다.", Toast.LENGTH_SHORT).show();
        }else{
            UserModel.getInstance().setNickName(user_name_et.getText().toString());
            editProfileDialogListener.editUserNameEvent();
            dismiss();
        }
    }
}
