package util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.yssh.ground.GroundApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareArticleTask extends AsyncTask<Bitmap, Bitmap, Bitmap> {

    private String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    private String imageName = Environment.getExternalStorageDirectory() + "/" + GroundApplication.STORAGE_DIRECTORY_NAME + "/" + timeStamp + GroundApplication.IMG_NAME;
    private File folder_path = new File(Environment.getExternalStorageDirectory() + "/" + GroundApplication.STORAGE_DIRECTORY_NAME + "/");

    private callbackListener callbackListener;

    public ShareArticleTask(callbackListener callbackListener){
        this.callbackListener = callbackListener;
    }

    public interface callbackListener{
        public void openChooserCallback(String imageName, String timeStamp);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Bitmap... objects) {
        if (!folder_path.exists()) {
            folder_path.mkdir();
        }
        //로컬에 저장
        OutputStream outStream = null;
        File file = new File(imageName);

        try {
            outStream = new FileOutputStream(file);
            objects[0].compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Bitmap res) {
        if (res != null) {
            res.recycle();
        }
        callbackListener.openChooserCallback(imageName, timeStamp);
    }

}
