package com.sarthaksrinivas.tanvas;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import co.tanvas.haptics.service.adapter.*;
import co.tanvas.haptics.service.app.HapticApplication;
import co.tanvas.haptics.service.model.*;

public class MainActivity extends AppCompatActivity implements iHapticServiceAdapterEventHandler {
    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoview = (VideoView) findViewById(R.id.vid);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.vid);

        videoview.setVideoURI(uri);

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoview.start();
    }


    @Override
    public void serviceAdapterWasCreated(Intent intent) {

    }

    @Override
    public void serviceAdapterIsConnecting(Intent intent) {

    }

    @Override
    public void serviceAdapterIsDisconnecting(Intent intent) {

    }

    @Override
    public void serviceAdapterIsDisconnected(Intent intent) {

    }

    @Override
    public void serviceAdapterIsConnected(Intent intent) {
        try {
            // Get the service adapter
            HapticServiceAdapter serviceAdapter =
                    HapticApplication.getHapticServiceAdapter();
            // Create a haptic view and activate it
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();
            // Set the orientation of the haptic view
            Display display = ((WindowManager)
                    getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation =
                    HapticView.getOrientationFromAndroidDisplayRotation(rotation);
            mHapticView.setOrientation(orientation);
            // Retrieve texture data from the bitmap
            Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.noise_texture);
            byte[] textureData =
                    HapticTexture.createTextureDataFromBitmap(hapticBitmap);
            // Create a haptic texture with the retrieved texture data
            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();



            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);
            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);
            // Create a haptic sprite with the hapti)c material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);
            // Set the size and position of the haptic sprite to correspond to the view we created
            View view = findViewById(R.id.vid);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            mHapticSprite.setSize(view.getWidth(), view.getHeight());
            mHapticSprite.setPosition(location[0], location[1]);
            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Register the activity as an event handler
            HapticServiceAdapterEventListener listener =
                    HapticServiceAdapterEventListener.obtain(this);
            listener.addHandler(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mHapticView.deactivate();
            HapticServiceAdapterEventListener.obtain(this).removeHandler(this);
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }
}
