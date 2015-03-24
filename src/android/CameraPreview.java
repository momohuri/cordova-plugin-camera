package org.apache.cordova.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author paul.blundell
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceHolder holder;

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraPreview(Context context) {
        super(context);
    }

    public void init(Camera camera) {
        setWillNotDraw(false);
        this.camera = camera;
        Camera.Parameters a = camera.getParameters();
        a.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        a.setRotation(90);
        Camera.Size maxSize = a.getSupportedPictureSizes().get(0);
        a.setPictureSize(maxSize.width,maxSize.height);
        camera.setDisplayOrientation(90);
        camera.setParameters(a);
        initSurfaceHolder();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int h = getHeight();
        int w = getWidth();
        int sq = Math.min(h, w);
        int l = (w - sq) / 2;
        int t = (h - sq) / 2;
        int r = l + sq;
        int b = t + sq;
        Rect innerRect = new Rect(l, t, r, b);
        Rect outerRect = new Rect(0, 0, w, h);

        Rect above = new Rect(outerRect.left, outerRect.top, innerRect.right, innerRect.top);
        Rect bottom = new Rect(outerRect.left, innerRect.bottom, outerRect.right, outerRect.bottom);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        paint.setAlpha(200);
        canvas.drawRect(above, paint);
        canvas.drawRect(bottom, paint);
    }

    @SuppressWarnings("deprecation") // needed for < 3.0
    private void initSurfaceHolder() {
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera(holder);
    }

    private void initCamera(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}