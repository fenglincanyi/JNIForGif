package com.geng.jnigif;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gif.encoder.GifEncoder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Bitmap> bitmapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }

        findViewById(R.id.dd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.mipmap.aa);
                        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.mipmap.bb);
                        Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.mipmap.cc);
                        Bitmap b4 = BitmapFactory.decodeResource(getResources(), R.mipmap.dd);
                        Bitmap b5 = BitmapFactory.decodeResource(getResources(), R.mipmap.ee);

                        bitmapList.add(b1);
                        bitmapList.add(b2);
                        bitmapList.add(b3);
                        bitmapList.add(b4);
                        bitmapList.add(b5);

                    }
                }.start();
            }
        });

        findViewById(R.id.sample_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        if (createGif()) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "GIF create success!!!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.start();
            }
        });
    }

    private boolean createGif() {
        boolean result;
        GifEncoder encoder = new GifEncoder();
        try {
            encoder.init(200, 200, Environment.getExternalStorageDirectory().getAbsolutePath() + "/12345.gif", GifEncoder.EncodingType.FAST);
            result = encoder.encodeFrame(bitmapList, 150);
            if (!result) {
                throw new Exception("encode gif fail");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
