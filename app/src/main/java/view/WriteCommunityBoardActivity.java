package view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.Constants;
import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import presenter.WriteCommunityBoardPresenter;
import presenter.view.WriteFreeBoardView;
import util.SaveImageTask;
import util.Util;

public class WriteCommunityBoardActivity extends BaseActivity implements WriteFreeBoardView{

    private static final int REQUEST_PERMISSIONS = 10;
    private static final int GET_PICTURE_URI = 0;

    private String typeOfCommunity;

    private WriteCommunityBoardPresenter writeCommunityBoardPresenter;
    private Bitmap bm, resized;

    @BindView(R.id.board_title_et) EditText title_et;
    @BindView(R.id.board_contents_et) EditText contents_et;
    @BindView(R.id.photo_thumb_iv) ImageView photo_thumb_iv;
    @BindString(R.string.error_not_exist_input_txt) String errorNotExistInputStr;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(bm != null){
            bm.recycle();
        }
        if(resized != null){
            resized.recycle();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_free_board);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        typeOfCommunity = intent.getExtras().getString(Constants.EXTRA_COMMUNITY_BOARD_TYPE);

        init();
    }

    private void init(){
        writeCommunityBoardPresenter = new WriteCommunityBoardPresenter(this, getApplicationContext());
    }

    /**
     * Uri 절대 경로
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    //사진가져오기
    private void goToSelectPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GET_PICTURE_URI);
    }

    @Override
    public void WriteBoard(){
        final String titleStr = title_et.getText().toString().trim();
        final String contentsStr = contents_et.getText().toString().trim();

        if(titleStr.equals("") || contentsStr.equals("")){
            showMessage(errorNotExistInputStr);
        }else{
            if(bm == null){
                writeCommunityBoardPresenter.writeFreeBoard(UserModel.getInstance().getUid(), titleStr, contentsStr, "N", "N", typeOfCommunity);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }else{
                SaveImageTask saveImageTask = new SaveImageTask(new SaveImageTask.callbackListener() {
                    @Override
                    public void openChooserCallback(String imageName, String timeStamp) {

                    }

                    @Override
                    public void saveImageCallback(String imagePath, String imageName) {
                        writeCommunityBoardPresenter.writeFreeBoard(UserModel.getInstance().getUid(), titleStr, contentsStr, imagePath, imageName, typeOfCommunity);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }, "save");
                saveImageTask.execute(bm);
            }
        }
    }

    @OnClick({R.id.add_photo_btn, R.id.write_btn, R.id.back_btn}) void Click(View v){
        switch (v.getId()){
            case R.id.add_photo_btn:
                if (ContextCompat.checkSelfPermission(WriteCommunityBoardActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                        .checkSelfPermission(WriteCommunityBoardActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (WriteCommunityBoardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(WriteCommunityBoardActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    } else {
                        ActivityCompat.requestPermissions(WriteCommunityBoardActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    goToSelectPhoto();
                }
                break;
            case R.id.write_btn:
                WriteBoard();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    /**
     * 갤러리에서 사진을 불러오면 일단 글라이드에서는 uri로 보여줌(속도때문에)
     * 그리고 저장을 누르면 파일을 로컬 폴더에 저장
     * 비트맵은 리사이징을 거친다(속도문제)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PICTURE_URI) {
            if (resultCode == RESULT_OK) {
                try {
                    //BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 2;
                    bm = BitmapFactory.decodeFile(getPath(data.getData()));
                    try{
                        ExifInterface exif = new ExifInterface(getPath(data.getData()));
                        int exifOrientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = Util.exifOrientationToDegrees(exifOrientation);
                        bm = Util.rotate(bm, exifDegree);
                    }catch (Exception e){

                    }
                    float size = 0;
                    if( bm.getHeight() >= bm.getWidth() ) {
                        size = bm.getHeight();
                    } else if( bm.getHeight() < bm.getWidth() ) {
                        size = bm.getWidth();
                    }

                    if(size > 1080){
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        //리사이징 과정에서 단말기 메모리 오류 방지
                        if( size > 4320 ){
                            options.inSampleSize = 8;
                        } else if( size > 3240 ){
                            options.inSampleSize = 4;
                        } else if( size > 2160 ){
                            options.inSampleSize = 2;
                        } else {
                            options.inSampleSize = 1;
                        }
                        bm = BitmapFactory.decodeFile(getPath(data.getData()), options);
                        bm = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), true);
                    }
                    //resized = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), true);
                    //photoPath = "file://"+localBitmapPath;
                    photo_thumb_iv.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();

                    Glide.with(getApplicationContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(data.getData())
                            .into(photo_thumb_iv);

                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                //권한이 있는 경우
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToSelectPhoto();
                }
                //권한이 없는 경우
                else {
                    showMessage("퍼미션을 허용해야 이용할 수 있습니다.");
                }
                break;
        }
    }
}
