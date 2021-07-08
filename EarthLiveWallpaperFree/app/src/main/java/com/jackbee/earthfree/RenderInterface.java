package com.jackbee.earthfree;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Date;
import java.util.HashMap;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import earthProject.free.Planet;
import earthProject.free.Universe;
import earthProject.free.Constellation;
import modelUnit.BufferBase;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.Log;


public class RenderInterface implements GLSurfaceView.Renderer,
        GLWallpaperService.Renderer {
    //public static final String TAG = "EarthLiveWallpaper";

    private boolean _initFlag        = false;

    private float _shiftY        = 0.4f;
    private float _motionK       = 1.1f;
    private float _shiftYCurrent = 0f;

    private float _matAmbient[]    = {1.0f,1.0f,1.0f,1.0f};
    private float _matSpecular[]    = {1.0f,1.0f,1.0f,1.0f};
    private float _matShininess[]   = {128.0f};
    private float _lightPosition0[]  = {-0.15f, 0.15f, 0.4f, 0.0f};
    private float _lightPosition1[]  = {-0.56f, 0.64f, 0.0f, 0.0f};
    private float _ambient[]        = {0.1f, 0.1f, 0.1f, 1.0f};
    private float _starLight[]      = {0.17254902f,0.670588235f,0.925490196f,1.0f};
    //private float _starLight[]     = {0.90625f, 0.73828125f, 0.0390625f, 1.f};
    //private float _starLight[]     = {1.0f,1.0f,1.0f,1.f};
    private float _universeLight[] = {1.0f,1.0f,1.0f,1.0f};

    private HashMap<String,Bitmap>      _textures;
    private HashMap<String,InputStream> _coordinates;

    private Planet        _planet;
    private Universe      _universe;
    private Constellation _constellation;

    private long  _startTimeout = 150;
    private float _startShiftY  = 55;

    private long _lastRenderTime = 0;
    private static long _renderTimeout = 17;


    public RenderInterface(HashMap<String,InputStream> coordinates,
                           HashMap<String,Bitmap> textures) {
        //Log.d(TAG,"Inilializing renderer...");

        _coordinates = coordinates;
        _textures    = textures;

    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Log.d(TAG,"Preparing data for drawing...");

        if(_initFlag) {
            //Log.d(TAG, "Allready initialized!");

            return;

        }

        _initFlag = true;

        BufferBase.initTexturesArray(gl,_textures.size());

        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);

        gl.glClearColor(.0f, .0f, .0f, 1);
        gl.glColor4f(1, 1, 1, 1);

        gl.glShadeModel(GL10.GL_SMOOTH);

        ByteBuffer bb = ByteBuffer.allocateDirect(_ambient.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = bb.asFloatBuffer();
        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, floatBuffer);

        gl.glMaterialfv(GL10.GL_FRONT,GL10.GL_SPECULAR,_matSpecular,0);
        gl.glMaterialfv(GL10.GL_FRONT,GL10.GL_SHININESS,_matShininess,0);

        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_POSITION,_lightPosition0,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_DIFFUSE,_universeLight,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_SPECULAR,_universeLight,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_AMBIENT,_matAmbient,0);

        gl.glLightfv(GL10.GL_LIGHT1,GL10.GL_POSITION,_lightPosition1,0);
        gl.glLightfv(GL10.GL_LIGHT1,GL10.GL_DIFFUSE,_starLight,0);
        gl.glLightfv(GL10.GL_LIGHT1,GL10.GL_SPECULAR,_starLight,0);

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_DITHER);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        try {
            _planet = new Planet(
                    gl,
                    _textures.get("planet"),
                    _textures.get("clouds"),
                    _textures.get("atmosphere"),
                    _coordinates.get("planet"),
                    _coordinates.get("clouds"),
                    _coordinates.get("atmosphere")
            );
        }
        catch(Exception e) {
            //Log.e(TAG, "Can not pars coordinates:");
            //Log.e(TAG, e.toString());

        }

        _universe      = new Universe(gl, _textures.get("universe0"), _textures.get("universe1"));
        _constellation = new Constellation(gl);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CCW);

    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Log.d(TAG, "Changing screen size...");

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        float preparedWidth, preparedHeight;

        if(width<=height) {
            preparedWidth  = 0.6f;
            preparedHeight = 0.6f*height/width;

        }
        else {
            preparedWidth  = 0.6f*width/height;
            preparedHeight = 0.6f;

        }

        gl.glOrthof(-preparedWidth,preparedWidth,-preparedHeight,preparedHeight,-100.0f,100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        _universe.changeSize(preparedWidth > preparedHeight ? preparedWidth : preparedHeight);
        _constellation.changeSize(preparedWidth, preparedHeight);

        try {
            Thread.sleep(_startTimeout);
            _shiftYCurrent = _startShiftY;

        }
        catch(InterruptedException e) {
            //Log.w(TAG, e.toString());

        }

    }


    public void onDrawFrame(GL10 gl) {

        long currentTime = (new Date()).getTime();

        if( currentTime - _lastRenderTime < _renderTimeout ) {

            try {
                Thread.sleep(_renderTimeout - currentTime + _lastRenderTime);
            }
            catch(InterruptedException e) {

            }

        }

        _lastRenderTime = currentTime;

        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                GL10.GL_MODULATE);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);

        float shiftGlobalY = getGlobalShiftY();

        _universe.draw(gl);
        _constellation.draw(gl);
        _planet.draw(gl, shiftGlobalY);

    }


    private float getGlobalShiftY() {

        if(Math.abs(_shiftYCurrent) <= _shiftY) {
            _shiftYCurrent = _shiftY;
        }
        else {
            _shiftYCurrent = _shiftYCurrent/_motionK;

        }

        return _shiftYCurrent;

    }


    public void setYshift(float yAngle) {

        _shiftYCurrent = yAngle;

    }

}
