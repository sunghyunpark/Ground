package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import database.RealmConfig;
import database.model.MemoVO;
import io.realm.Realm;

public class WriteMemoActivity extends BaseActivity {

    private Realm mRealm;

    @BindView(R.id.memo_et) EditText memo_et;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_memo);

        ButterKnife.bind(this);

        init();
    }

    public void init(){
        RealmConfig realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.MemoRealmVersion(getApplicationContext()));
    }

    @OnClick({R.id.back_btn, R.id.write_btn}) void Click(View v){
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.write_btn:
                String memoStr = memo_et.getText().toString().trim();

                if(memoStr.equals("")){
                    showMessage(errorNotExistInputStr);
                }else{
                    mRealm.beginTransaction();
                    MemoVO memoVO = new MemoVO();

                    Number maxId = mRealm.where(MemoVO.class).max("no");
                    int nextId = (maxId == null) ? 0:maxId.intValue() + 1;

                    memoVO.setNo(nextId);
                    memoVO.setOrder(nextId);
                    memoVO.setMemoText(memoStr);

                    mRealm.copyToRealmOrUpdate(memoVO);
                    mRealm.commitTransaction();

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                break;
        }
    }
}
