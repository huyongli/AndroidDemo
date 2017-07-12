package cn.ittiger.demo.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author: ylhu
 * @time: 17-7-12
 */

public class ScreenCapture {
    private Context mContext;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private ImageReader mImageReader;

    public static ScreenCapture with(Context context) {

        return new ScreenCapture(context);
    }

    private ScreenCapture(Context context) {

        this.mContext = context;
    }

    public void capture(Intent data, CaptureListener listener) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaProjectionManager projectionManager = (MediaProjectionManager)mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            mMediaProjection = projectionManager.getMediaProjection(Activity.RESULT_OK, data);

            buildImageReader();

            buildVirtualDisplay();
            
            capture(listener);
        }
    }

    private void buildImageReader() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mImageReader = ImageReader.newInstance(
                    getScreenWidth(),
                    getScreenHeight(),
                    PixelFormat.RGBA_8888,//此处RGB_565后面buffer处理格式一致
                    1);//取第一帧作为截图
        }
    }

    private void buildVirtualDisplay() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                    getScreenWidth(), getScreenHeight(), Resources.getSystem().getDisplayMetrics().densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void capture(final CaptureListener listener) {

        new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                Image image = mImageReader.acquireLatestImage();
                Observable.just(image)
                        .map(new Func1<Image, File>() {
                            @Override
                            public File call(Image image) {

                                int width = image.getWidth();
                                int height = image.getHeight();
                                final Image.Plane[] planes = image.getPlanes();
                                final ByteBuffer buffer = planes[0].getBuffer();
                                //每个像素的间距
                                int pixelStride = planes[0].getPixelStride();
                                //总的间距
                                int rowStride = planes[0].getRowStride();
                                int rowPadding = rowStride - pixelStride * width;
                                Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                                bitmap.copyPixelsFromBuffer(buffer);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
                                image.close();

                                String url = mContext.getExternalFilesDir("screenshot").getAbsoluteFile() + "/" + SystemClock.currentThreadTimeMillis() + ".png";
                                File file = new File(url);
                                try {
                                    if(!file.exists()) {
                                        file.createNewFile();
                                    }
                                    FileOutputStream out = new FileOutputStream(file);
                                    if (out != null) {
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                        out.flush();
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    file = null;
                                }

                                return file;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<File>() {
                            @Override
                            public void onCompleted() {

                                mContext = null;
                            }

                            @Override
                            public void onError(Throwable e) {

                                if(listener != null) {
                                    listener.onFailure(e.getMessage());
                                    mContext = null;
                                }
                            }

                            @Override
                            public void onNext(File file) {

                                if(file == null) {
                                    onError(new Throwable("can not create screenshot"));
                                    return;
                                }
                                if(listener != null) {
                                    listener.onSuccess(file);
                                }
                            }
                        });
            }
        }, 300);
    }
    
    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public interface CaptureListener {

        void onSuccess(File file);

        void onFailure(String message);
    }


}
