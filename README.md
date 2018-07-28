## Android Generate Gif

Create GIF By JNI Method

### Useage
project/build.gradle
```gradle
buildscript {
    repositories {
        ...
        maven { url "https://dl.bintray.com/canyifenglin/maven" }
    }
}

allprojects {
    repositories {
        ...
        maven { url "https://dl.bintray.com/canyifenglin/maven" }
    }
}
```
app/build.gradle
```gradle
implementation 'com.gif:jnigif:0.1.2'
```

GIF quality：
* FAST
* BETTER_FAST
* NORMAL
* HIGH

Encode:

a series of bitmaps or files to generate gif

delay: frames between times (ms)


```java
GifEncoder encoder = new GifEncoder();
try {
    encoder.init(200, 200, Environment.getExternalStorageDirectory().getAbsolutePath() + "/12345.gif", GifEncoder.EncodingType.FAST);
    result = encoder.encodeFrame(bitmapList, 150);
    if (!result) {
        throw new Exception("encode gif fail");
    }
} catch (Exception e) {
}
```

#### use must be in work thread

## LICENSE

Apache License 2.0