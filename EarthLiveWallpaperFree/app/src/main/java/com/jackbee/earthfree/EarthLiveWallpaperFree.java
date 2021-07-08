package com.jackbee.earthfree;

import java.io.InputStream;
import java.util.HashMap;
import com.jackbee.earthfree.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class EarthLiveWallpaperFree extends GLWallpaperService {
    //public static final String TAG = "EarthLiveWallpaper";

    private RenderInterface _renderer;


    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public Engine onCreateEngine() {
        //Log.i(TAG,"Starting...");

        //Log.d(TAG, "Reading bitmap files...");
        HashMap<String,Bitmap> textures = new HashMap<String,Bitmap>();

        //Log.d(TAG, "Reading files coordinates...");
        HashMap<String,InputStream> coordinates = new HashMap<String,InputStream>();

        // earth texture
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.earth);
        textures.put("planet", bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.clouds);
        textures.put("clouds", bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.atmosphere);
        textures.put("atmosphere", bitmap);

        // universe texture
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.milky_way0);
        textures.put("universe0", bitmap);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.milky_way1);
        textures.put("universe1", bitmap);

        // earth coordinate
        InputStream inputStream = this.getResources().openRawResource(R.raw.planet);
        coordinates.put("planet", inputStream);
        inputStream = this.getResources().openRawResource(R.raw.skinface);
        coordinates.put("clouds", inputStream);
        inputStream = this.getResources().openRawResource(R.raw.skinface);
        coordinates.put("atmosphere", inputStream);

        //Log.d(TAG, "Initializing renderer...");
        try {
            _renderer = new RenderInterface(coordinates,textures);

        }
        catch(Exception e) {
            //Log.e(TAG, "Can not initialize renderer:");
            //Log.e(TAG, e.toString());
            System.exit(1);

        }
        GLEngine engine = new GLEngine() {

            {
                setEGLConfigChooser(false);
                setRenderer(_renderer);
                setRenderMode(RENDERMODE_CONTINUOUSLY);

            }

        };

        engine.setTouchEventsEnabled(true);
        engine.setMotionCallback(new MotionProcessor(_renderer));

        return engine;

    }

}
