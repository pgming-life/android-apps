package com.jackbee.glassboxfree;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLU;
import android.service.wallpaper.WallpaperService;
//import android.util.Log;
import android.view.SurfaceHolder;


public class CrystalLiveWallpaperFree extends WallpaperService {
    //public static final String TAG0 = "CrystalLiveWallpaperFree";


    @Override
    public Engine onCreateEngine() {
        //Log.d(TAG0, "onCreateEngine()");

        return new GLEngine();

    }


    public class GLEngine extends Engine {
        //private static final String TAG1 = "GLEngine";

        private GLEngineSurface gl = null;


        public GLEngine() {

        }


        /**
         * @param surfaceHolder
         */
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            //Log.d(TAG1, "onCreate()");

            super.onCreate(surfaceHolder);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);

        }


        @Override
        public void onDestroy() {
            //Log.d(TAG1, "onDestroy()");

            super.onDestroy();

        }


        /**
         * @param visible
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            //Log.d(TAG1, "onVisibilityChanged() : " + visible);

            super.onVisibilityChanged(visible);

            if(visible) {
                gl.onResume();

            }
            else {
                gl.onPause();

            }

        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            //Log.d(TAG1, "onSurfaceCreated()");

            super.onSurfaceCreated(holder);

            gl = new GLEngineSurface(getSurfaceHolder());
            gl.start();

        }


        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //Log.d(TAG1, "onSurfaceChanged()");
            //Log.d(TAG1, "" + width + " x " + height);

            super.onSurfaceChanged(holder, format, width, height);

            gl.windowWidth = width;
            gl.windowHeight = height;

        }


        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            //Log.d(TAG1, "onSurfaceDestroyed()");

            super.onSurfaceDestroyed(holder);

            if(gl != null) {
                gl.onDestroy();;
                gl = null;

            }

        }




    }


    public class GLEngineSurface extends Thread {
        //public static final String TAG2 = "GLEngineSurface";


        public GLEngineSurface(SurfaceHolder holder) {

            this.holder = holder;

        }

        private boolean destroy = false;
        private boolean pause = false;

        /**
         * drawing holder
         */
        private SurfaceHolder holder;

        /**
         * EGL interface
         */
        private EGL10 egl;

        /**
         * GL context
         */
        private EGLContext eglContext = null;

        /**
         * display
         */
        private EGLDisplay eglDisplay = null;

        /**
         * surface
         */
        private EGLSurface eglSurface = null;

        /**
         * configure information
         */
        private EGLConfig eglConfig = null;

        /**
         * GL interface
         */
        protected GL10 gl10 = null;

        /**
         * width / height of drawing surface
         */
        private int windowWidth = -1, windowHeight = -1;

        /**
         * start processing of GL
         */
        private void initialize() {
            //Log.d(TAG2, "initialize()");

            egl = (EGL10) EGLContext.getEGL();
            eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);


            //! initialization of EGL
            {
                int[] version = { -1, -1 };
                if(!egl.eglInitialize(eglDisplay, version)) {
                    //Log.d(TAG2, "!eglInitialize");

                    return;

                }

            }


            //! acquisition of configure
            {
                EGLConfig[] configs = new EGLConfig[1];

                int[] num = new int[1];
                int[] spec = {
                        EGL10.EGL_NONE

                };

                if(!egl.eglChooseConfig(eglDisplay, spec, configs, 1, num)) {
                    //Log.d(TAG2, "!eglChooseConfig");

                    return;

                }

                eglConfig = configs[0];

            }


            //! creating of rendering context
            {
                eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, null);

                if(eglContext == EGL10.EGL_NO_CONTEXT) {
                    //Log.d(TAG2, "glContext == EGL10.EGL_NO_CONTEXT");

                    return;

                }

            }

            //! creating of drawing surface
            {
                //! link to surface holder
                egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, holder, null);

                if(eglSurface == EGL10.EGL_NO_SURFACE) {
                    //Log.d(TAG2, "glSurface == EGL10.EGL_NO_SURFACE");

                    return;

                }

            }


            //! acquisition of GLES interface
            {
                gl10 = (GL10) eglContext.getGL();

            }


            //! link to surface and context
            {
                if(!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                    //Log.d(TAG2, "!eglMakeCurrent");

                    return;

                }

            }

        }


        /**
         * end processing of GL
         */
        private void dispose() {
            //Log.d(TAG2, "dispose()");

            // destruction of surface
            if(eglSurface != null) {
                egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                egl.eglDestroySurface(eglDisplay, eglSurface);
                eglSurface = null;

            }


            // destruction of rendering context
            if(eglContext != null) {
                egl.eglDestroyContext(eglDisplay, eglContext);
                eglContext = null;

            }

            // destruction of display connection
            if(eglDisplay != null) {
                egl.eglTerminate(eglDisplay);
                eglDisplay = null;

            }

        }


        @Override
        public void run() {
            //Log.d(TAG2, "run()");

            RenderInterface render = new RenderInterface();
            initialize();
            render.onSurfaceChanged(gl10, windowWidth, windowHeight);

            while(!destroy) {

                if(!pause) {
                    render.windowHeight = windowHeight;
                    render.windowWidth = windowWidth;
                    gl10.glViewport(0, 0, windowWidth, windowHeight);
                    render.onDrawFrame(gl10);
                    egl.eglSwapBuffers(eglDisplay, eglSurface);

                }
                else {

                    try {
                        Thread.sleep(10);

                    }
                    catch(Exception e) {

                    }

                }

            }

            dispose();

        }


        public void onPause() {
            //Log.d(TAG2, "onPause()");

            pause = true;

        }


        public void onResume() {
            //Log.d(TAG2, "onResume()");

            pause = false;

        }


        /**
         * end processing and stop the thread
         */
        public void onDestroy() {
            //Log.d(TAG2, "onDestroy()");

            synchronized(this) {
                // put out a termination request
                destroy = true;

            }

            try {
                // wait for the thread end
                join();

            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();

            }

        }

    }


    public class RenderInterface {
        //public static final String TAG3 = "RenderInterface";

        int windowWidth;
        int windowHeight;

        float aspect = 0.0f;

        int vertices = 0;
        int indices = 0;
        int positionLength = 0;
        int normalLength = 0;
        int indicesLength = 0;

        /**
         * The Surface is created/init()
         */
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //Log.d(TAG3, "onSurfaceCreated()");

            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background

        }


        /**
         * If the surface changes, reset the view
         */
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //Log.d(TAG3, "onSurfaceChanged()");

            aspect = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);	//Reset The Current Viewport

            windowWidth = width;
            windowHeight = height;

            gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
            gl.glLoadIdentity(); 					//Reset The Projection Matrix

            GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
            GLU.gluLookAt(gl, 0, 0f, 10.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);

            gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
            gl.glLoadIdentity(); 					//Reset The Modelview Matrix


            gl.glEnable(GL10.GL_DEPTH_TEST);

            GL11 gl11 = (GL11) gl;


            {
                int[] buffers = new int[2];
                gl11.glGenBuffers(2, buffers, 0);

                vertices = buffers[0];
                indices = buffers[1];

            }


            {
                //    v6----- v5
                //   /|      /|
                //  v1------v0|
                //  | |     | |
                //  | |v7---|-|v4
                //  |/      |/
                //  v2------v3
                final float[] vertices = new float[] {
                        // x, y, z
                        1.63299316f,   0.577350269f,   0.0f,                    0.81649658f,  -0.577350269f,   1.414213562f,           0.0f,         -1.732050808f,   0.0f,                  0.81649658f,  -0.577350269f,  -1.414213562f,     // v0-v1-v2-v3 front
                        1.63299316f,   0.577350269f,   0.0f,                    0.81649658f,  -0.577350269f,  -1.414213562f,          -0.81649658f,   0.577350269f,  -1.414213562f,          0.0f,          1.732050808f,   0.0f,             // v0-v3-v4-v5 right
                        1.63299316f,   0.577350269f,   0.0f,                    0.0f,          1.732050808f,   0.0f,                  -0.81649658f,   0.577350269f,   1.414213562f,          0.81649658f,  -0.577350269f,   1.414213562f,     // v0-v5-v6-v1 top
                        0.81649658f,  -0.577350269f,   1.414213562f,           -0.81649658f,   0.577350269f,   1.414213562f,          -1.63299316f,  -0.577350269f,   0.0f,                  0.0f,         -1.732050808f,   0.0f,             // v1-v6-v7-v2 left
                        -1.63299316f,  -0.577350269f,   0.0f,                   -0.81649658f,   0.577350269f,  -1.414213562f,           0.81649658f,  -0.577350269f,  -1.414213562f,          0.0f,         -1.732050808f,   0.0f,             // v7-v4-v3-v2 bottom
                        -0.81649658f,   0.577350269f,  -1.414213562f,           -1.63299316f,  -0.577350269f,   0.0f,                  -0.81649658f,   0.577350269f,   1.414213562f,          0.0f,          1.732050808f,   0.0f,             // v4-v7-v6-v5 back

                };

                float[] normals = new float[] {
                        // x, y, z
                        0.81649658f,  -0.57735027f,   0.0f,                   0.81649658f,  -0.57735027f,   0.0f,                   0.81649658f,  -0.57735027f,   0.0f,                   0.81649658f,  -0.57735027f,   0.0f,            // v0-v1-v2-v3 front
                        0.57735027f,   0.81649658f,  -0.70710678f,            0.57735027f,   0.81649658f,  -0.70710678f,            0.57735027f,   0.81649658f,  -0.70710678f,            0.57735027f,   0.81649658f,  -0.70710678f,     // v0-v3-v4-v5 right
                        0.57735027f,   0.81649658f,   0.70710678f,            0.57735027f,   0.81649658f,   0.70710678f,            0.57735027f,   0.81649658f,   0.70710678f,            0.57735027f,   0.81649658f,   0.70710678f,     // v0-v5-v6-v1 top
                        -0.57735027f,  -0.81649658f,   0.70710678f,           -0.57735027f,  -0.81649658f,   0.70710678f,           -0.57735027f,  -0.81649658f,   0.70710678f,           -0.57735027f,  -0.81649658f,   0.70710678f,     // v1-v6-v7-v2 left
                        -0.57735027f,  -0.81649658f,  -0.70710678f,           -0.57735027f,  -0.81649658f,  -0.70710678f,           -0.57735027f,  -0.81649658f,  -0.70710678f,           -0.57735027f,  -0.81649658f,  -0.70710678f,     // v7-v4-v3-v2 bottom
                        -0.81649658f,   0.57735027f,   0.0f,                  -0.81649658f,   0.57735027f,   0.0f,                  -0.81649658f,   0.57735027f,   0.0f,                  -0.81649658f,   0.57735027f,   0.0f,            // v4-v7-v6-v5 back

                };

                final byte[] indices = new byte[] {
                        0, 1, 2,   0, 2, 3,    // front
                        4, 5, 6,   4, 6, 7,    // right
                        8, 9, 10,  8, 10,11,   // top
                        12,13,14,  12,14,15,   // left
                        16,17,18,  16,18,19,   // bottom
                        20,21,22,  20,22,23    // back

                };

                // vertices and normals
                positionLength = vertices.length;
                normalLength = normals.length;

                FloatBuffer fb = ByteBuffer.allocateDirect((vertices.length + normals.length) * 4)
                        .order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.put(normals);
                fb.position(0);

                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, this.vertices);
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, fb.capacity() * 4, fb, GL11.GL_STATIC_DRAW);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

                // indices
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                indicesLength = bb.capacity();
                bb.position(0);

                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, this.indices);
                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, bb.capacity(), bb, GL11.GL_STATIC_DRAW);

            }


            {
                gl.glEnable(GL10.GL_BLEND);
                gl.glEnable(GL10.GL_ALPHA);
                gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

            }


            {
                float[] LightPos = {
                        2.0f,   // x
                        4.0f,   // y
                        5.0f,   // z
                        1.0f    // w

                };

                gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, LightPos, 0);

            }


            {
                float[] LightCol = {
                        0.98f,   //!< r
                        0.98f,   //!< g
                        0.98f,   //!< b
                        0.98f    //!< a

                };

                gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, LightCol, 0);

            }


            {
                gl.glEnable(GL10.GL_LIGHTING);
                gl.glEnable(GL10.GL_LIGHT0);

            }


        }


        /**
         * Here we do our drawing
         */
        public void onDrawFrame(GL10 gl) {
            //Log.d(TAG3, "onDrawFrame()");

            //Clear Screen And Depth Buffer
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();


            {
                plate0.posY = -2.0f;
                plate0.posZ = 2.3f;

                plate0.rotateY += 0.2f;

                plate0.scaleX = 1.3f;
                plate0.scaleY = 0.0f;
                plate0.scaleZ = 1.3f;

                plate0.drawObj(gl);

            }


            {
                plate1.posX =  2.1f;
                plate1.posY = -2.0f;
                plate1.posZ = -1.75f;

                plate1.rotateX = 180.0f;
                plate1.rotateY -= 0.2f;
                plate1.rotateZ = 180.0f;

                plate1.scaleX = 1.3f;
                plate1.scaleY = 0.0f;
                plate1.scaleZ = 1.3f;

                plate1.drawObj(gl);

            }


            {
                plate2.posX = -2.1f;
                plate2.posY = -2.0f;
                plate2.posZ = -1.75f;

                plate2.rotateY += 0.2f;
                plate2.rotateZ = 180.0f;

                plate2.scaleX = 1.3f;
                plate2.scaleY = 0.0f;
                plate2.scaleZ = 1.3f;

                plate2.drawObj(gl);

            }


            {
                cube0.rotateY += 0.2f;

                cube0.drawObj(gl);

            }


            {
                cube1.rotateX += 0.8f;
                cube1.rotateY -= 0.5f;

                cube1.scaleX = 0.5f;
                cube1.scaleY = 0.5f;
                cube1.scaleZ = 0.5f;

                cube1.drawObj(gl);

            }


            {
                cube2.rotateY += 2.0f;
                cube2.rotateZ -= 1.8f;

                cube2.scaleX = 0.25f;
                cube2.scaleY = 0.25f;
                cube2.scaleZ = 0.25f;

                cube2.drawObj(gl);

            }


        }


        class Object3D {
            //public static final String TAG4 = "Object3D";

            public float posX = 0.0f;
            public float posY = 0.0f;
            public float posZ = 0.0f;
            public float rotateX = 0.0f;
            public float rotateY = 0.0f;
            public float rotateZ = 0.0f;
            public float scaleX = 1.0f;
            public float scaleY = 1.0f;
            public float scaleZ = 1.0f;
            public float colR = 1.0f;
            public float colG = 1.0f;
            public float colB = 1.0f;
            public float colA = 1.0f;


            /**
             * The Cube constructor.
             *
             * Initiate the buffers.
             */
            public void drawObj(GL10 gl) {
                //Log.d(TAG4, "drawObj()");

                GL11 gl11 = (GL11) gl;

                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();

                gl.glTranslatef(posX, posY, posZ);
                gl.glRotatef(rotateX, 1, 0, 0);
                gl.glRotatef(rotateY, 0, 1, 0);
                gl.glRotatef(rotateZ, 0, 0, 1);
                gl.glScalef(scaleX, scaleY, scaleZ);
                gl.glColor4f(colR, colG, colB, colA);

                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertices);
                gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
                gl11.glNormalPointer(GL10.GL_FLOAT, 0, positionLength * 3);

                gl11.glDrawElements(GL10.GL_TRIANGLES, indicesLength, GL10.GL_UNSIGNED_BYTE, 0);


            }


        }


        Object3D plate0 = new Object3D();
        Object3D plate1 = new Object3D();
        Object3D plate2 = new Object3D();
        Object3D cube0 = new Object3D();
        Object3D cube1 = new Object3D();
        Object3D cube2 = new Object3D();

    }

}
