package util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.groundmobile.ground.Constants;
import com.groundmobile.ground.GroundApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveImageTask extends AsyncTask<Bitmap, Bitmap, Bitmap> {

    private String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    private String imageName = timeStamp + Constants.IMG_NAME;
    private String imageFullPath = Environment.getExternalStorageDirectory() + "/" + Constants.STORAGE_DIRECTORY_NAME + "/" + imageName;
    private File folder_path = new File(Environment.getExternalStorageDirectory() + "/" + Constants.STORAGE_DIRECTORY_NAME + "/");

    private callbackListener callbackListener;
    private String type;    // share / save

    public SaveImageTask(callbackListener callbackListener, String type){
        this.callbackListener = callbackListener;
        this.type = type;
    }

    public interface callbackListener{
        public void openChooserCallback(String imageName, String timeStamp);
        public void saveImageCallback(String imageFullPath, String imageName);
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
        OutputStream outStream;
        File file = new File(imageFullPath);

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
        if(type.equals("share")){
            callbackListener.openChooserCallback(imageFullPath, timeStamp);
        }else if(type.equals("save")){
            callbackListener.saveImageCallback(imageFullPath, imageName);
        }
    }

}
