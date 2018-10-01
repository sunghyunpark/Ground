package view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.groundmobile.ground.R;

import base.BaseActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.UserModel;
import presenter.WriteFreeBoardPresenter;
import presenter.view.WriteFreeBoardView;
import util.ShareArticleTask;

public class WriteFreeBoardActivity extends BaseActivity implements WriteFreeBoardView{

    private static final int REQUEST_PERMISSIONS = 10;
    private static final int GET_PICTURE_URI = 0;

    private WriteFreeBoardPresenter writeFreeBoardPresenter;
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

        init();
    }

    private void init(){
        writeFreeBoardPresenter = new WriteFreeBoardPresenter(this, getApplicationContext());
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
            if(resized == null){
                writeFreeBoardPresenter.writeFreeBoard(UserModel.getInstance().getUid(), titleStr, contentsStr, "N", "N");
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }else{
                ShareArticleTask shareArticleTask = new ShareArticleTask(new ShareArticleTask.callbackListener() {
                    @Override
                    public void openChooserCallback(String imageName, String timeStamp) {

                    }

                    @Override
                    public void saveImageCallback(String imagePath) {
                        writeFreeBoardPresenter.writeFreeBoard(UserModel.getInstance().getUid(), titleStr, contentsStr, "N", "N");
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }, "save");
                shareArticleTask.execute(resized);
            }
        }
    }

    @OnClick(R.id.add_photo_btn) void addPhotoBtn(){
        if (ContextCompat.checkSelfPermission(WriteFreeBoardActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(WriteFreeBoardActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (WriteFreeBoardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(WriteFreeBoardActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            } else {
                ActivityCompat.requestPermissions(WriteFreeBoardActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            goToSelectPhoto();
        }
    }

    @OnClick(R.id.write_btn) void writeBtn(){
        WriteBoard();
    }

    @OnClick(R.id.back_btn) void backBtn(){
        finish();
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
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bm = BitmapFactory.decodeFile(getPath(data.getData()), options);
                    resized = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), true);
                    //photoPath = "file://"+localBitmapPath;
                    photo_thumb_iv.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();

                    Glide.with(getApplicationContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(data.getData())
                            .into(photo_thumb_iv);

                } catch (Exception e) {
                    Log.e("test", e.getMessage());
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
