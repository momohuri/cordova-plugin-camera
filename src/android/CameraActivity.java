package org.apache.cordova.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static org.apache.cordova.camera.MediaHelper.getOutputMediaFile;
import static org.apache.cordova.camera.MediaHelper.saveToFile;

/**
 * Takes a photo saves it to the SD card and returns the path of this photo to the calling Activity
 *
 * @author paul.blundell
 */
public class CameraActivity extends Activity implements PictureCallback {

    public static final String EXTRA_IMAGE_PATH = "diogoPath";

    private Camera camera;
    private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        // Camera may be in use by another activity or the system or not available at all

        initCameraPreview();

    }

    // Show the camera view on the activity
    private void initCameraPreview() {

        cameraPreview = (CameraPreview) findViewById(getApplication().getResources().getIdentifier("activity_camera.xml", "layout", getApplication().getPackageName()));
        cameraPreview.init(camera);
    }

    public void onCaptureClick(View button) {
        // Take a picture with a callback when the photo has been created
        // Here you can add callbacks if you want to give feedback when the picture is being taken
        button.setEnabled(false);
        camera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        int dimension = bitmap.getWidth();
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        data = stream.toByteArray();
        String path = savePictureToFileSystem(data);
        setResult(path);
        finish();
    }

    private static String savePictureToFileSystem(byte[] data) {
        File file = getOutputMediaFile();
        saveToFile(data, file);
        return file.getAbsolutePath();
    }

    private void setResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IMAGE_PATH, path);
        setResult(RESULT_OK, intent);
    }

    // ALWAYS remember to release the camera when you are finished
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
