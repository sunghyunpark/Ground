package view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import database.RealmConfig;
import database.model.MemoVO;
import io.realm.Realm;
import io.realm.RealmResults;
import model.MemoModel;
import presenter.MemoPresenter;
import presenter.view.MemoView;
import util.SimpleItemTouchHelperCallback;
import util.adapter.MemoAdapter;
import view.dialog.MemoHelpDialog;

public class MemoActivity extends BaseActivity implements MemoView{

    private static final int REQUEST_WRITE = 1000;
    private static final int REQUEST_DETAIL = 2000;

    private int detailPosition;

    private Realm mRealm;
    private MemoAdapter memoAdapter;
    private ArrayList<MemoVO> memoVOArrayList;
    private ItemTouchHelper mItemTouchHelper;
    private MemoPresenter memoPresenter;

    @BindView(R.id.memo_recyclerView) RecyclerView memoRecyclerView;
    @BindView(R.id.empty_memo_tv) TextView empty_memo_tv;
    @BindView(R.id.background_img_iv) ImageView background_iv;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        ButterKnife.bind(this);

        init();
    }

    public void init(){

        // Realm 초기화
        RealmConfig realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.MemoRealmVersion(getApplicationContext()));

        // 메모 리스트 초기화
        memoVOArrayList = new ArrayList<>();

        // 메모 presenter 초기화
        memoPresenter = new MemoPresenter(this, memoVOArrayList, mRealm);

        // 메모 리사이클러뷰 어뎁터 초기화
        memoAdapter = new MemoAdapter(getApplicationContext(), memoVOArrayList, mRealm, new MemoAdapter.memoAdapterListener() {
            @Override
            public void goToEditMemo(int position, MemoVO memoVO) {
                detailPosition = position;

                MemoModel memoModel = new MemoModel();
                memoModel.setNo(memoVO.getNo());
                memoModel.setOrder(memoVO.getOrder());
                memoModel.setMemoText(memoVO.getMemoText());

                Intent intent = new Intent(getApplicationContext(), EditMemoActivity.class);
                intent.putExtra("memoModel", memoModel);
                startActivityForResult(intent, REQUEST_DETAIL);
            }

            @Override
            public void verifyExistMemo(){
                notifyMemoData(false);
            }
        });

        // 메모 리사이클러뷰 셋팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        memoRecyclerView.setLayoutManager(linearLayoutManager);
        memoRecyclerView.setAdapter(memoAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(memoAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(memoRecyclerView);

        // 최초 메모 데이터를 불러온다.
        memoPresenter.setMemoData(true);

        setBackground();

    }

    private void setBackground(){
        Drawable drawable = getResources().getDrawable(R.drawable.memo_background_img1);
        //Glide Options
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.placeholder(drawable);
        //requestOptions.override(DISPLAY_WIDTH, DISPLAY_WIDTH);

        Glide.with(getApplicationContext())
                .setDefaultRequestOptions(requestOptions)
                .load(null)
                .into(background_iv);
    }

    @Override
    public void notifyMemoData(boolean hasMemo){
        if(hasMemo){
            memoAdapter.notifyDataSetChanged();
            memoRecyclerView.setVisibility(View.VISIBLE);
            empty_memo_tv.setVisibility(View.GONE);
        }else{
            empty_memo_tv.setVisibility(View.VISIBLE);
            memoRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE) {
            if(resultCode == Activity.RESULT_OK){
                // 글쓰고 난 뒤 돌아왔을 경우
                memoPresenter.setMemoData(true);
            }else if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }else if(requestCode == REQUEST_DETAIL){
            if(resultCode == Activity.RESULT_OK){
                MemoModel memoModel = (MemoModel) data.getExtras().getSerializable("memoModel");

                MemoVO memoVO = new MemoVO();
                memoVO.setNo(memoModel.getNo());
                memoVO.setOrder(memoModel.getOrder());
                memoVO.setMemoText(memoModel.getMemoText());

                memoVOArrayList.set(detailPosition, memoVO);
                memoAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Realm DB 의 모든 Data 삭제
     */
    private void deleteAllMemoDB(){
        memoVOArrayList.clear();
        RealmResults<MemoVO> memoVORealmResults = mRealm.where(MemoVO.class).findAll();
        mRealm.beginTransaction();
        memoVORealmResults.deleteAllFromRealm();
        mRealm.commitTransaction();
        notifyMemoData(false);
    }

    @OnClick({R.id.back_btn, R.id.write_btn, R.id.delete_memo_btn, R.id.help_btn}) void Click(View v){
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.write_btn:
                Intent intent = new Intent(getApplicationContext(), WriteMemoActivity.class);
                startActivityForResult(intent, REQUEST_WRITE);
                break;
            case R.id.delete_memo_btn:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("전체 삭제");
                alert.setMessage("정말 메모들을 전체 삭제하시겠습니까?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteAllMemoDB();
                    }
                });
                alert.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.

                            }
                        });
                alert.show();
                break;
            case R.id.help_btn:
                MemoHelpDialog memoHelpDialog = new MemoHelpDialog(this);
                memoHelpDialog.show();
                break;
        }
    }

}
