package com.gif.encoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjiarong
 * on 2018/7/26.
 */
public class GifEncoder {

    static {
        System.loadLibrary("gifencoder");
    }


    private long initFlag;
    private int mWidth;
    private int mHeight;


    /**
     * GIF生成质量
     */
    public enum EncodingType {
        FAST,
        BETTER_FAST,
        NORMAL,
        HIGH
    }

    /**
     * 默认: EncodingType.FAST
     */
    public void init(int dstWidth, int dstHeight, String outputPath) throws FileNotFoundException {
        init(dstWidth, dstHeight, outputPath, EncodingType.FAST);
    }

    /**
     * @param dstWidth     gif的宽
     * @param dstHeight    gif的高
     * @param outputPath   gif路径
     * @param encodingType gif质量类型
     * @see EncodingType
     */
    public void init(int dstWidth, int dstHeight, String outputPath, EncodingType encodingType) throws FileNotFoundException {
        this.mWidth = dstWidth;
        this.mHeight = dstHeight;
        initFlag = nativeInit(dstWidth, dstHeight, outputPath, encodingType.ordinal(), 1);
        if (0 == initFlag) {
            throw new FileNotFoundException();
        }
    }

    /**
     * encode 多个bitmap
     *
     * @param bitmaps bitmap list
     * @param delay   frame 之间的间隔
     * @return true: encode success
     */
    public boolean encodeFrame(List<Bitmap> bitmaps, int delay) throws Exception {
        checkThread();
        boolean result;
        for (Bitmap bitmap : bitmaps) {
            Bitmap tmp = bitmap;
            if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                throw new IllegalArgumentException("bitmap is Illegal:  width: " + bitmap.getWidth() + ", height: " + bitmap.getHeight());
            }
            if (bitmap.getWidth() != mWidth || bitmap.getHeight() != mHeight) {
                tmp = scaleBitmap(bitmap, mWidth, mHeight);
            }
            result = encodeFrame(tmp, delay);
            if (!result) {
                return false;
            }
        }

        close();
        return true;
    }

    /**
     * @see #encodeFrame(List, int)
     */
    public boolean encodeFrame(File[] files, int delay) throws Exception {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (File file : files) {
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file.getAbsolutePath()));
                if (bitmap != null) {
                    bitmaps.add(bitmap);
                }
            } catch (FileNotFoundException e) {
                return false;
            }
        }

        return !bitmaps.isEmpty() && encodeFrame(bitmaps, delay);
    }

    private void checkThread() throws Exception {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new Exception("encodeFrame must in workThread");
        }
    }

    /**
     * 缩放不符合尺寸的bitmap
     *
     * @param sourceBitmap 源bitmap
     * @param dstWidth     dstWidth
     * @param dstHeight    dstWidth
     * @return 缩放后的Bitmap
     */
    private Bitmap scaleBitmap(Bitmap sourceBitmap, int dstWidth, int dstHeight) {
        return Bitmap.createScaledBitmap(sourceBitmap, dstWidth, dstHeight, true);
    }

    private boolean encodeFrame(Bitmap bitmap, int delay) {
        return nativeEncodeFrame(initFlag, bitmap, delay);
    }

    private void close() {
        nativeClose(initFlag);
    }


    // ============================================ jni 接口 ============================================


    private native long nativeInit(int dstWidth, int dstHeight, String outputPath, int encodeType, int threadCount);

    private native boolean nativeEncodeFrame(long initFlag, Bitmap bitmap, int delay);

    private native void nativeClose(long initFlag);
}
