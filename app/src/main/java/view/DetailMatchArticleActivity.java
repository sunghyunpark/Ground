package view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;
import com.groundmobile.ground.R;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CommentModel;
import model.MatchArticleModel;
import model.UserModel;
import presenter.DetailMatchArticlePresenter;
import presenter.view.DetailMatchArticleView;
import util.JMediaScanner;
import util.PowerMenuUtil;
import util.SaveImageTask;
import util.Util;
import util.adapter.CommentAdapter;
import view.dialog.ReportDialog;

public class DetailMatchArticleActivity extends BaseActivity implements DetailMatchArticleView {

    private static final int REQUEST_PERMISSIONS = 10;
    private static final int REQUEST_EDIT = 1000;
    private static final int RESULT_DELETE = 3000;

    private static final int MATCH_MODE = 1;
    private static final int HIRE_MODE = 2;
    private static final int RECRUIT_MODE = 3;
    private int boardMode;
    private PowerMenu morePowerMenu;

    private String area, uid;
    private boolean hasArticleModel;
    private String boardType;    // api 호출로 새로 불러올때 사용될 변수
    private int areaNo, articleNo;    // api 호출로 새로 불러올때 사용될 변수
    private MatchArticleModel matchArticleModel;
    private DetailMatchArticlePresenter detailMatchArticlePresenter;
    private int favoriteState = -1;    // -1 : null, 0: not like, 1:like

    @BindView(R.id.rootView) ViewGroup root_view;
    @BindView(R.id.profile_iv) ImageView user_profile_iv;
    @BindView(R.id.area_tv) TextView area_tv;
    @BindView(R.id.title_tv) TextView title_tv;
    @BindView(R.id.nick_name_tv) TextView nick_name_tv;
    @BindView(R.id.created_at_tv) TextView created_at_tv;
    @BindView(R.id.view_cnt_tv) TextView view_cnt_tv;
    @BindView(R.id.contents_tv) TextView contents_tv;
    @BindView(R.id.comment_recyclerView) RecyclerView comment_recyclerView;
    @BindView(R.id.comment_et) EditText comment_et;
    @BindView(R.id.empty_comment_tv) TextView empty_comment_tv;
    @BindView(R.id.favorite_tb) ImageView favorite_tb;
    @BindView(R.id.matching_state_btn) ToggleButton matching_state_toggle;
    @BindView(R.id.matching_date_tv) TextView match_date_tv;
    @BindView(R.id.matching_date_layout) ViewGroup match_date_layout;
    @BindView(R.id.charge_tv) TextView charge_tv;
    @BindView(R.id.charge_layout) ViewGroup charge_layout;
    @BindView(R.id.play_rule_tv) TextView play_rule_tv;
    @BindView(R.id.play_rule_layout) ViewGroup play_rule_layout;
    @BindView(R.id.age_tv) TextView age_tv;
    @BindView(R.id.age_layout) ViewGroup age_layout;
    @BindView(R.id.detail_more_btn) ImageButton detailMoreBtn;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    public void onDestroy(){
        super.onDestroy();
        matchArticleModel = null;
        detailMatchArticlePresenter = null;
    }

    @Override
    protected void onResume(){
        super.onResume();
        initUI();
    }

    /**
     * MatchArticleModel 객체 생성 후 전 화면에서 넘겨받은 MatchArticleModel 을 할당한다.
     * 상세 게시글 진입 시 matchArticleModel 내의 viewCnt 를 +1 해준다.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        area = intent.getExtras().getString(Constants.EXTRA_AREA_NAME);
        hasArticleModel = intent.getExtras().getBoolean(Constants.EXTRA_EXIST_ARTICLE_MODEL);
        uid = intent.getExtras().getString(Constants.EXTRA_USER_ID);

        UserModel.getInstance().setUid(uid);    //푸시를 통해 바로 액티비티 진입 시 uid값을 새로 받아오지만 moreBtn과 같이 UserModel을 이용하는 부분도 있어서 다시 넣어준다.

        matchArticleModel = new MatchArticleModel();

        if(hasArticleModel){
            matchArticleModel = (MatchArticleModel)intent.getExtras().getSerializable(Constants.EXTRA_ARTICLE_MODEL);
            matchArticleModel.setViewCnt(matchArticleModel.getViewCnt()+1);
            initMode(matchArticleModel.getMatchBoardType());
            //showMessage("area : "+area+"\nhasArticleModel : "+hasArticleModel+"\nboardType : "+matchArticleModel.getBoardType()+"\nareaNo : "+matchArticleModel.getAreaNo()+"\narticleNo : "+matchArticleModel.getNo());
        }else{
            boardType = intent.getExtras().getString(Constants.EXTRA_MATCH_BOARD_TYPE);
            areaNo = intent.getIntExtra(Constants.EXTRA_AREA_NO, 0);
            articleNo = intent.getIntExtra(Constants.EXTRA_ARTICLE_NO, 0);
            initMode(boardType);
            //showMessage("area : "+area+"\nhasArticleModel : "+hasArticleModel+"\nboardType : "+boardType+"\nareaNo : "+areaNo+"\narticleNo : "+articleNo);
        }

        init();
    }

    /**
     * 댓글 recyclerView 초기화 및 CommentPresenter 를 초기화한다.
     * CommentAdapter 내에서 댓글 삭제 부분은 Callback 을 통해 통신한다.
     */
    private void init(){
        ArrayList<CommentModel> commentModelArrayList = new ArrayList<CommentModel>();
        LinearLayoutManager lL = new LinearLayoutManager(getApplicationContext());
        CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), commentModelArrayList, false, new CommentAdapter.CommentListener() {
            @Override
            public void deleteCommentEvent(int commentNo) {
                detailMatchArticlePresenter.deleteComment(matchArticleModel.getMatchBoardType(), commentNo, matchArticleModel.getNo(), matchArticleModel.getAreaNo());
            }
            @Override
            public void reportCommentEvent(int commentNo){
                ReportDialog reportDialog = new ReportDialog(DetailMatchArticleActivity.this, "comment", matchArticleModel.getMatchBoardType(), matchArticleModel.getNo(), commentNo);
                reportDialog.show();
            }
        });
        comment_recyclerView.setLayoutManager(lL);
        comment_recyclerView.setAdapter(commentAdapter);
        comment_recyclerView.setNestedScrollingEnabled(false);

        detailMatchArticlePresenter = new DetailMatchArticlePresenter(getApplicationContext(), this, commentModelArrayList, commentAdapter);
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

    /**
     * 상단 타이틀 명 초기화(지역명)
     * matchArticleModel 내의 데이터를 각 뷰에 넣어준다.
     * commentPresenter 를 통해 해당 게시글의 좋아요 상태를 초기화 해준다.
     * commentPresenter 를 통해 해당 게시글의 댓글 리스트들을 api 로 받아온다.
     */
    private void initUI(){
        area_tv.setText(area);
        if(hasArticleModel){
            setArticleData(matchArticleModel);
        }else{
            detailMatchArticlePresenter.loadArticleData(boardType, areaNo, articleNo, uid);
        }
    }

    /**
     * 사용자 프로필 이미지를 초기화해준다.
     * 현재는 별도의 프로필 스펙을 적용하지 않은 상태라 디폴트 이미지만 보여준다.
     * @param urlPath
     */
    private void setUserProfile(String urlPath){
        //Glide Options
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.user_profile_img);
        requestOptions.error(R.mipmap.user_profile_img);
        requestOptions.circleCrop();    //circle

        Glide.with(getApplicationContext())
                .setDefaultRequestOptions(requestOptions)
                .load(GroundApplication.GROUND_DEV_API+urlPath)
                .into(user_profile_iv);
    }

    // 평균 연령 클릭 리스너
    private OnMenuItemClickListener<PowerMenuItem> moreOnMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            morePowerMenu.setSelectedPosition(position); // change selected item
            morePowerMenu.dismiss();

            switch (item.getTitle()){
                case "수정" :
                    Intent intent = new Intent(getApplicationContext(), EditBoardActivity.class);
                    intent.putExtra(Constants.EXTRA_AREA_NAME, area);
                    intent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
                    startActivityForResult(intent, REQUEST_EDIT);
                    break;
                case "삭제" :
                    detailMatchArticlePresenter.deleteArticle((hasArticleModel) ? matchArticleModel.getMatchBoardType() : boardType, (hasArticleModel) ? matchArticleModel.getNo() : articleNo,
                            uid);
                    break;
                case "신고" :
                    ReportDialog reportDialog = new ReportDialog(DetailMatchArticleActivity.this, "article", (hasArticleModel) ? matchArticleModel.getMatchBoardType() : boardType,
                            (hasArticleModel) ? matchArticleModel.getNo() : articleNo, 0);
                    reportDialog.show();
                    break;
            }

        }
    };

    // 평균 연령 dismiss 리스너
    private OnDismissedListener onMoreMenuDismissedListener = new OnDismissedListener() {
        @Override
        public void onDismissed() {
        }
    };

    /**
     * commentPresenter 를 통해 받아온 댓글 리스트 중 commentList 의 size 를 통해
     * 댓글이 있는지 없는지 판별을 한 뒤에 initComment 를 통해 Gone 처리 여부 수행한다.
     * @param hasCommentItem
     */
    @Override
    public void initComment(boolean hasCommentItem){
        if(hasCommentItem){
            empty_comment_tv.setVisibility(View.GONE);
            comment_recyclerView.setVisibility(View.VISIBLE);
        }else{
            empty_comment_tv.setVisibility(View.VISIBLE);
            comment_recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteArticle(){
        Intent returnIntent = new Intent();
        setResult(RESULT_DELETE, returnIntent);
        finish();
    }

    /**
     * 좋아요 상태를 presenter 에서 콜백받아와 초기화 한다.
     * @param state
     */
    @Override
    public void setFavoriteState(int state){
        favoriteState = state;
        setFavorite(favoriteState);
    }

    @Override
    public void loadArticleData(MatchArticleModel articleData){
        matchArticleModel = articleData;
        setArticleData(matchArticleModel);
    }

    /**
     * matchArticleModel 을 통해 상세 게시글 내의 view 들에 데이터를 넣어준다.
     * 매칭 상태 토글 버튼 초기화
     * @param matchArticleModel
     */
    @Override
    public void setArticleData(final MatchArticleModel matchArticleModel){
        // matchArticleModel 을 다 받아오기전에 초기화를 하게되면 crash 가 발생되어, 모든 데이터들을 셋팅할 때 초기화를 한다.
        morePowerMenu = PowerMenuUtil.getDetailMatchArticleMorePowerMenu(getApplicationContext(), this, moreOnMenuItemClickListener, onMoreMenuDismissedListener, matchArticleModel);

        title_tv.setText(matchArticleModel.getTitle());
        nick_name_tv.setText(matchArticleModel.getNickName());
        created_at_tv.setText(Util.parseTimeWithoutSec(matchArticleModel.getCreatedAt()));
        view_cnt_tv.setText("조회 "+ matchArticleModel.getViewCnt());

        if(boardMode == MATCH_MODE){
            // 매치 게시글인 경우
            match_date_tv.setText(matchArticleModel.getMatchDate());
            age_tv.setText(matchArticleModel.getAverageAge()+"대");

            DecimalFormat dc = new DecimalFormat("###,###,###,###");
            String chargeStr = dc.format(matchArticleModel.getCharge());

            charge_tv.setText(chargeStr+"원");
            if(matchArticleModel.getPlayRule() == 0){
                play_rule_tv.setText("기타");
            }else{
                play_rule_tv.setText(matchArticleModel.getPlayRule()+" vs "+matchArticleModel.getPlayRule());
            }
        }else if(boardMode == HIRE_MODE){
            // 용병 게시글인 경우
            match_date_tv.setText(matchArticleModel.getMatchDate());

            age_layout.setVisibility(View.GONE);
            charge_layout.setVisibility(View.GONE);
            play_rule_layout.setVisibility(View.GONE);
        } else{
            // 모집 게시글인 경우
            match_date_layout.setVisibility(View.GONE);
            age_layout.setVisibility(View.GONE);
            charge_layout.setVisibility(View.GONE);
            play_rule_layout.setVisibility(View.GONE);
        }

        contents_tv.setText(matchArticleModel.getContents());
        setUserProfile(matchArticleModel.getProfile());
        initMatchingStateToggle();

        matching_state_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMatchingState(matching_state_toggle.isChecked() ? "Y" : "N");
                matchArticleModel.setMatchState(matching_state_toggle.isChecked() ? "Y" : "N");
                detailMatchArticlePresenter.changeMatchState(matchArticleModel.getAreaNo(), matchArticleModel.getNo(), matching_state_toggle.isChecked() ? "Y" : "N",
                        (hasArticleModel) ? matchArticleModel.getMatchBoardType() : boardType);
            }
        });

        if(detailMatchArticlePresenter != null){
            detailMatchArticlePresenter.loadFavoriteState(matchArticleModel.getMatchBoardType(), matchArticleModel.getAreaNo(), matchArticleModel.getNo(), uid);
            detailMatchArticlePresenter.loadComment(true, matchArticleModel.getNo(), 0, matchArticleModel.getAreaNo(), matchArticleModel.getMatchBoardType());
        }
    }

    /**
     * 토글 버튼 초기화
     * boardType 이 match 인 경우에만 토글버튼을 노출시킨다.
     */
    private void initMatchingStateToggle(){
        if(matchArticleModel.getMatchBoardType().equals(Constants.MATCH_OF_BOARD_TYPE_MATCH) || matchArticleModel.getMatchBoardType().equals(Constants.HIRE_OF_BOARD_TYPE_MATCH)){
            setMatchingState(matchArticleModel.getMatchState());
            if(!matchArticleModel.getWriterId().equals(uid)){
                matching_state_toggle.setEnabled(false);
            }
        }else{
            matching_state_toggle.setVisibility(View.GONE);
        }
    }

    /**
     * 매칭 상태 토글 버튼 상태를 적용한다.
     * @param state
     */
    private void setMatchingState(String state){
        if(state.equals("Y")){
            matching_state_toggle.setChecked(true);
            matching_state_toggle.setBackgroundResource(R.drawable.matching_state_on_shape);
            matching_state_toggle.setTextColor(getResources().getColor(R.color.colorMoreGray));
            matching_state_toggle.setText("완료");
        }else{
            matching_state_toggle.setChecked(false);
            matching_state_toggle.setBackgroundResource(R.drawable.matching_state_off_shape);
            matching_state_toggle.setTextColor(getResources().getColor(R.color.colorAccent));
            matching_state_toggle.setText("진행중");
        }
    }

    /**
     * 받아온 Article Data에서 관심 상태를 초기화한다.
     * @param state
     */
    private void setFavorite(int state){
        if(state == 0){
            //not like
            favorite_tb.setImageResource(R.mipmap.favorite_img);
        }else if(state == 1){
            //like
            favorite_tb.setImageResource(R.mipmap.favorite_selected_img);
        }else{
            // favoriteState is null
            favorite_tb.setImageResource(R.mipmap.favorite_img);
        }
    }

    /**
     * 공유하기 시 현재 보여지는 화면을 bitmap 으로 만들어준다.
     * @param rootView
     * @return
     */
    private Bitmap takeScreenshot(View rootView) {
        rootView.setDrawingCacheEnabled(true);
        Bitmap bit = rootView.getDrawingCache();
        Rect statusBar = new Rect();
        //this.getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBar);
        return Bitmap.createBitmap(bit, 0, statusBar.top, bit.getWidth(), bit.getHeight() - statusBar.top, null, true);
    }

    /**
     * 관심 버튼 이벤트
     */
    @Override
    public void favoriteClick(){
        if(favoriteState == 1){
            favoriteState = 0;
            setFavorite(favoriteState);
            detailMatchArticlePresenter.postFavoriteState(matchArticleModel.getNo(), uid, matchArticleModel.getMatchBoardType(), "N");
        }else if(favoriteState == 0){
            favoriteState = 1;
            setFavorite(favoriteState);
            detailMatchArticlePresenter.postFavoriteState(matchArticleModel.getNo(), uid, matchArticleModel.getMatchBoardType(), "Y");
        }else{
            // favoriteState is null
            Util.showToast(getApplicationContext(), "네트워크 연결상태를 확인해주세요.");
        }
    }

    /**
     * Error 발생 시 액티비티 종료 시킨다.
     * 현재는 게시글 삭제 후 리스트에서 바로 갱신이 되지않아
     * 다시 진입 시 error 로 종료시킨다.
     */
    @Override
    public void error(){
        finish();
    }

    /**
     * 댓글 화면 진입
     */
    @Override
    public void commentClick(){
        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        intent.putExtra(Constants.EXTRA_AREA_NO, matchArticleModel.getAreaNo());
        intent.putExtra(Constants.EXTRA_ARTICLE_NO, matchArticleModel.getNo());
        intent.putExtra(Constants.EXTRA_MATCH_BOARD_TYPE, matchArticleModel.getMatchBoardType());
        intent.putExtra(Constants.EXTRA_BOARD_TYPE, Constants.BOARD_TYPE_MATCH);
        startActivity(intent);
    }

    /**
     * 캡쳐 버튼 클릭 후 AsyncTask 로 로컬에 캡쳐한 비트맵을 저장
     * SaveImageTask 의 interface 를 통해 콜백을 받아온다.
     */
    @Override
    public void captureClick(){
        showLoading();
        SaveImageTask saveImageTask = new SaveImageTask(new SaveImageTask.callbackListener() {
            @Override
            public void openChooserCallback(String mediaPath, String timeStamp) {
                //SaveImageTask 의 interface
                hideLoading();
                showMessage("게시글을 캡쳐했습니다.");
                openShareChooser(mediaPath, timeStamp);
            }

            @Override
            public void saveImageCallback(String imgPath, String imageName){

            }
        },"share");
        saveImageTask.execute(takeScreenshot(root_view));
    }

    /**
     * 츄져 노출
     * @param mediaPath -> 이미지 경로 (SaveImageTask)
     * @param timeStamp -> 이미지 생성 시간 (SaveImageTask)
     */
    private void openShareChooser(String mediaPath, String timeStamp){
        String type = "image/*";
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.groundmobile.ground.fileProvider", media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

        JMediaScanner scanner = new JMediaScanner(getApplicationContext());
        scanner.startScan(Environment.getExternalStorageDirectory()+ "/" + Constants.STORAGE_DIRECTORY_NAME + "/" + timeStamp + Constants.IMG_NAME);

    }

    /**
     * 댓글 입력 버튼 이벤트
     */
    @Override
    public void writeComment(){
        String commentStr = comment_et.getText().toString().trim();
        if(isLogin()){
            if(commentStr.equals("")){
                Util.showToast(getApplicationContext(), errorNotExistInputStr);
            }else{
                comment_et.setText(null);
                detailMatchArticlePresenter.postComment(matchArticleModel.getAreaNo(), matchArticleModel.getNo(), uid, commentStr, matchArticleModel.getMatchBoardType());
            }
        }else{
            showMessage("로그인을 해주세요.");
        }
    }

    @OnClick({R.id.favorite_btn, R.id.write_comment_btn, R.id.comment_btn, R.id.capture_btn, R.id.back_btn, R.id.detail_more_btn}) void Click(View v){
        switch (v.getId()){
            case R.id.favorite_btn:
                favoriteClick();
                break;
            case R.id.write_comment_btn:
                writeComment();
                break;
            case R.id.comment_btn:
                commentClick();
                break;
            case R.id.capture_btn:
                if (ContextCompat.checkSelfPermission(DetailMatchArticleActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                        .checkSelfPermission(DetailMatchArticleActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (DetailMatchArticleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(DetailMatchArticleActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    } else {
                        ActivityCompat.requestPermissions(DetailMatchArticleActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    captureClick();
                }
                break;
            /*
             * 좌측 상단 뒤로가기 버튼 탭 이벤트
             * 게시글 리스트 화면으로 돌아갈때 Result 값을 반환한다.
             * 반환된 result 값을 통해 리스트 내에서 해당 item 을 갱신한다.
             */
            case R.id.back_btn:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            /*
             * 우측 상단 더보기 버튼 이벤트
             * 탭 시 DetailMoreDialog 가 노출된다.
             * 이때 DetailMoreDialog 와 상세 게시글 화면간의 통신을 위해 Callback 함수를 통해 데이터를 주고 전달받는다.
             * - 게시글 삭제
             * - 게시글 수정
             */
            case R.id.detail_more_btn:
                morePowerMenu.showAsDropDown(detailMoreBtn);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                //권한이 있는 경우
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureClick();
                }
                //권한이 없는 경우
                else {
                    showMessage("퍼미션을 허용해야 이용할 수 있습니다.");
                }
                break;
        }
    }

    /**
     * 게시글 수정을 하고 난 뒤 다시 게시글 상세 화면으로 돌아왔을 때 result 값을 통해
     * 다시 matchArticleModel 을 가지고 데이터를 갱신해준다.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT) {
            if(resultCode == Activity.RESULT_OK){
                matchArticleModel = (MatchArticleModel)data.getExtras().getSerializable(Constants.EXTRA_ARTICLE_MODEL);
                setArticleData(matchArticleModel);
            }else if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }

    /**
     * 하드웨어 back 버튼으로 뒤로가기 시 게시글 리스트 화면에 matchArticleModel 과 함께 result 값을 돌려준다.
     */
    @Override
    public void onBackPressed() {
        try{
            if(morePowerMenu.isShowing()){
                morePowerMenu.dismiss();
            }else{
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
                setResult(Activity.RESULT_OK, returnIntent);
                super.onBackPressed();
            }
        }catch (NullPointerException npe){
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.EXTRA_ARTICLE_MODEL, matchArticleModel);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

}
