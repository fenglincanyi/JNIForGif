#include <jni.h>
#include <string.h>
#include <wchar.h>
#include <android/bitmap.h>
#include "../GifEncoder.h"


#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL
Java_com_gif_encoder_GifEncoder_nativeInit(JNIEnv *env, jobject instance, jint dstWidth,
                                           jint dstHeight, jstring outputPath_, jint encodeType,
                                           jint threadCount) {
    GifEncoder *gifEncoder = new GifEncoder(static_cast<EncodingType>(encodeType));
    gifEncoder->setThreadCount(threadCount);
    const char *pathChars = env->GetStringUTFChars(outputPath_, 0);
    bool result = gifEncoder->init(dstWidth, dstHeight, pathChars);
    env->ReleaseStringUTFChars(outputPath_, pathChars);
    if (result) {
        return (jlong) gifEncoder;
    } else {
        delete gifEncoder;
        return 0;
    }
}

JNIEXPORT jboolean JNICALL
Java_com_gif_encoder_GifEncoder_nativeEncodeFrame(JNIEnv *env, jobject instance, jlong initFlag,
                                                  jobject jbitmap, jint delay) {
    GifEncoder *gifEncoder = (GifEncoder *) initFlag;
    void *bitmapPixels;
    if (AndroidBitmap_lockPixels(env, jbitmap, &bitmapPixels) < 0) {
        return static_cast<jboolean>(false);
    }
    uint16_t imgWidth = gifEncoder->getWidth();
    uint16_t imgHeight = gifEncoder->getHeight();
    uint32_t *tempPixels = new unsigned int[imgWidth * imgHeight];
    int stride = imgWidth * 4;
    int pixelsCount = stride * imgHeight;
    memcpy(tempPixels, bitmapPixels, pixelsCount);
    AndroidBitmap_unlockPixels(env, jbitmap);
    gifEncoder->encodeFrame(tempPixels, delay);
    delete[] tempPixels;
    return static_cast<jboolean>(true);

}

JNIEXPORT void JNICALL
Java_com_gif_encoder_GifEncoder_nativeClose(JNIEnv *env, jobject instance, jlong initFlag) {
    GifEncoder *gifEncoder = (GifEncoder *) initFlag;
    gifEncoder->release();
    delete gifEncoder;
}


#ifdef __cplusplus
}
#endif