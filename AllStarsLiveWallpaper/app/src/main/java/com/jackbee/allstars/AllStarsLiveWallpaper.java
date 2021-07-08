package com.jackbee.allstars;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Date;
import java.util.HashMap;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.jackbee.allstars.R;
import com.jackbee.allstars.settings.PrefSettings;
import modelUnit.BufferBase;
import allstarsProject.Constellation;
import allstarsProject.Earth;
import allstarsProject.Jupiter;
import allstarsProject.Mars;
import allstarsProject.Mercury;
import allstarsProject.Moon;
import allstarsProject.Neptune;
import allstarsProject.Pluto;
import allstarsProject.Saturn;
import allstarsProject.Sun;
import allstarsProject.Universe;
import allstarsProject.Uranus;
import allstarsProject.Venus;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.Log;


public class AllStarsLiveWallpaper extends GLWallpaperService {
	//public static final String TAG0 = "AllStarsLiveWallpaper";
	
	
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
		//Log.i(TAG0,"Starting...");
		
		CosmosRenderer _renderer = new CosmosRenderer(); 
		GLEngine engine = new GLEngine();
		engine.setEGLConfigChooser(false);
		engine.setRenderer(_renderer);
		engine.setRenderMode(GLEngine.RENDERMODE_CONTINUOUSLY);
		engine.setTouchEventsEnabled(true);
		engine.setMotionCallback(new MotionProcessor(_renderer));
		
		return engine;
	
	}
	
	
	public class CosmosRenderer implements GLWallpaperService.Renderer, 
		SharedPreferences.OnSharedPreferenceChangeListener {
		//public static final String TAG1 = "CosmosRenderer";
		
		private RenderInterface _renderer;
		
		// SharedPreferences
		private SharedPreferences _prefs;
		private int   _planets;
		private int   _universes;
		private int   _starCount;
		private int   _planetSpeed;
		private int   _universeSpeed;
		private int   _consteSpeed;
		private int   _touchSense;
		
		private float _shiftYCurrent = 0f;
		
		
		public CosmosRenderer() {
			
			_prefs = AllStarsLiveWallpaper.this.getSharedPreferences(
					PrefSettings.KEY_NAME, 0);
			_prefs.registerOnSharedPreferenceChangeListener(this);
			onSharedPreferenceChanged(_prefs, null);
			
			loadPrefs();
			
		}
		
		
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			initHashMap();
			_renderer.onSurfaceCreated(gl, config);
			
		}
		
		
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			
			_renderer.onSurfaceChanged(gl, width, height);
			
		}
		
		
		public void onDrawFrame(GL10 gl) {
			
			_renderer.onDrawFrame(gl);
			
		}
		
		
		public void initHashMap() {
			
			//Log.d(TAG1, "Reading bitmap files...");
			HashMap<String,Bitmap> textures = new HashMap<String,Bitmap>();
			Bitmap b = null;
			
			//Log.d(TAG1, "Reading files coordinates...");
			HashMap<String,InputStream> coordinates = new HashMap<String,InputStream>();
			InputStream i = null;
			
	//******** _planets
			switch(_planets) {
			case 1:
				// sun texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
				textures.put("sun", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.heliosphere);
				textures.put("heliosphere", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.sunlight);
				textures.put("sunlight0", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.sunlight);
				textures.put("sunlight1", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.sunlight);
				textures.put("sunlight2", b);
				
				// sun coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("sun", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("heliosphere", i);
				
				break;
				
			case 2:
				// mercury texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.mercury);
				textures.put("mercury", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.mercury_reflex);
				textures.put("mercury_reflex", b);
				
				// mercury coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("mercury", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("mercury_reflex", i);
				
				break;
				
			case 3:
				// venus texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.venus);
				textures.put("venus", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.venus_reflex);
				textures.put("venus_reflex", b);
				
				// venus coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("venus", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("venus_reflex", i);
				
				break;
				
			case 4:
				// earth texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.earth);
				textures.put("earth", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.clouds);
				textures.put("clouds", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.atmosphere);
				textures.put("atmosphere", b);
				
				// earth coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("earth", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("clouds", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("atmosphere", i);
				
				break;
				
			case 5:
				// moon texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.moon);
				textures.put("moon", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.moon_reflex);
				textures.put("moon_reflex", b);
				
				// moon coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("moon", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("moon_reflex", i);
				
				break;
				
			case 6:
				// mars texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.mars);
				textures.put("mars", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.mars_reflex);
				textures.put("mars_reflex", b);
				
				// mars coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("mars", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("mars_reflex", i);
				
				break;
				
			case 7:
				// jupiter texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.jupiter);
				textures.put("jupiter", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.jupiter_reflex);
				textures.put("jupiter_reflex", b);
				
				// jupiter coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("jupiter", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("jupiter_reflex", i);
				
				break;
				
			case 8:
				// saturn texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.saturn);
				textures.put("saturn", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.saturn_ring);
				textures.put("saturn_ring", b);
				
				// saturn coordinate
				i = getResources().openRawResource(R.raw.saturn);
				coordinates.put("saturn", i);
				i = getResources().openRawResource(R.raw.saturn_ring);
				coordinates.put("saturn_ring", i);
				
				break;
				
			case 9:
				// uranus texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.uranus);
				textures.put("uranus", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.uranus_ring);
				textures.put("uranus_ring", b);
				
				// uranus coordinate
				i = getResources().openRawResource(R.raw.uranus);
				coordinates.put("uranus", i);
				i = getResources().openRawResource(R.raw.uranus_ring);
				coordinates.put("uranus_ring", i);
				
				break;
				
			case 10:
				// neptune texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.neptune);
				textures.put("neptune", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.neptune_reflex);
				textures.put("neptune_reflex", b);
				
				// neptune coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("neptune", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("neptune_reflex", i);
				
				break;
				
			case 11:
				// pluto texture
				b = BitmapFactory.decodeResource(getResources(), R.drawable.pluto);
				textures.put("pluto", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.pluto_reflex);
				textures.put("pluto_reflex", b);
				
				// pluto coordinate
				i = getResources().openRawResource(R.raw.planet);
				coordinates.put("pluto", i);
				i = getResources().openRawResource(R.raw.skinface);
				coordinates.put("pluto_reflex", i);
				
				break;
				
			}
			
	//******** _universes
			switch(_universes) {
			case 1:
				b = BitmapFactory.decodeResource(getResources(), R.drawable.milky_way0);
				textures.put("universe0", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.milky_way1);
				textures.put("universe1", b);
				break;
				
			case 2:
				b = BitmapFactory.decodeResource(getResources(), R.drawable.darkness);
				textures.put("universe0", b);
				b = BitmapFactory.decodeResource(getResources(), R.drawable.darkness);
				textures.put("universe1", b);
				break;
				
			}
			
			/**
			 * @params
			 * 0 > coordinates
			 * 1 > textures
			 * 2 > planetsNum
			 * 3 > starCountNum
			 * 4 > planetSpeedNum
			 * 5 > universeSpeedNum
			 * 6 > consteSpeedNum
			 * 7 > touchSenseNum
			 * 8 > touchEvent
			 * 
			 */
			//Log.d(TAG1, "Initializing renderer...");
			try {
				_renderer = new RenderInterface(
						coordinates, 
						textures, 
						_planets, 
						_starCount, 
						_planetSpeed, 
						_universeSpeed, 
						_consteSpeed, 
						_touchSense, 
						_shiftYCurrent);
				
			} 
			catch(Exception e) {
				//Log.e(TAG1, "Can not initialize renderer:");
				//Log.e(TAG1, e.toString());
				System.exit(1);
			
			}
			
		}
		
		
		public void setYshift(float yAngle) {
	    	
	    	_shiftYCurrent = yAngle;
	    
	    }
		
		
		public class RenderInterface implements GLSurfaceView.Renderer {
			//public static final String TAG2 = "RenderInterface";
			
			private boolean _initFlag        = false;
			
			private int   _shiftY        = 0;
		    private int   _motionK       = 0;
		    private long  _startTimeout = 150;
		    private float _startShiftY  = 55;
		    
		    private float _matAmbient[]    = {1.0f,1.0f,1.0f,1.0f};
		    private float _matSpecular[]    = {1.0f,1.0f,1.0f,1.0f};
		    private float _matShininess[]   = {128.0f};
		    private float _lightPosition0[]  = {-1.0f, 1.0f, 3.0f, 0.0f};
		    private float _lightPosition1[]  = {-0.56f, 0.64f, 0.0f, 0.0f};
		    private float _ambient[]        = {0.1f, 0.1f, 0.1f, 1.0f};
		    //private float _starLight[]      = {0.17254902f,0.670588235f,0.925490196f,1.0f};
		    //private float _starLight[]     = {0.90625f, 0.73828125f, 0.0390625f, 1.f};
		    private float _starLight[]     = {1.0f,1.0f,1.0f,1.f};
		    private float _universeLight[] = {1.0f,1.0f,1.0f,1.0f};
		    
		    private HashMap<String,Bitmap>      _textures;
		    private HashMap<String,InputStream> _coordinates;
		    
		    private Sun           _sun;
		    private Mercury       _mercury;
		    private Venus         _venus;
		    private Earth         _earth;
		    private Moon          _moon;
		    private Mars          _mars;
		    private Jupiter       _jupiter;
		    private Saturn        _saturn;
		    private Uranus        _uranus;
		    private Neptune       _neptune;
		    private Pluto         _pluto;
		    private Universe      _universe;
		    private Constellation _constellation;
		    
		    private int _planetsNum       = 0;
		    private int _starCountNum     = 0;
		    private int _universeSpeedNum = 0;
		    private int _consteSpeedNum   = 0;
		    
		    private long              _lastRenderTime = 0;
		    private static final long _renderTimeout  = 17;
		    
		    
		    // Appearance, Amount > Code
		    public RenderInterface(
		    		HashMap<String,InputStream> coordinates, 
		    		HashMap<String,Bitmap> textures, 
		    		int   planets, 
		    		int   starCountNum, 
		    		int   planetSpeedNum, 
		    		int   universeSpeedNum, 
		    		int   consteSpeedNum, 
		    		int   touchSenseNum, 
		    		float shiftYCurrent) {
		    	//Log.d(TAG2, "Inilializing renderer...");
		    	
		    	_coordinates      = coordinates;
		    	_textures         = textures;
		    	_planetsNum       = planets;
		    	_starCountNum     = starCountNum;
		    	_shiftY           = planetSpeedNum;
		    	_universeSpeedNum = universeSpeedNum;
		    	_consteSpeedNum   = consteSpeedNum;
		    	_motionK          = touchSenseNum;
		    	_shiftYCurrent    = shiftYCurrent;
		    	
		    }
		    
			
		    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				//Log.d(TAG2,"Preparing data for drawing...");
				
				if(_initFlag) {
					//Log.d(TAG2, "Allready initialized!");
					
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
		        	
		        //** planetsNum
		        	switch(_planetsNum) {
		        	case 1:
		        		_sun = new Sun(
		        				gl, 
		        				_textures.get("sun"), 
		        				_coordinates.get("sun"), 
		        				_textures.get("heliosphere"), 
		        				_coordinates.get("heliosphere"), 
		        				_textures.get("sunlight0"), 
		        				_textures.get("sunlight1"), 
		        				_textures.get("sunlight2")
		        				);
		        		break;
		        		
		        	case 2:
		        		_mercury = new Mercury(
		        				gl, 
		        				_textures.get("mercury"), 
		        				_coordinates.get("mercury"), 
		        				_textures.get("mercury_reflex"), 
		        				_coordinates.get("mercury_reflex")
		        				);
		        		break;
		        		
		        	case 3:
		        		_venus = new Venus(
		        				gl, 
		        				_textures.get("venus"), 
		        				_coordinates.get("venus"), 
		        				_textures.get("venus_reflex"), 
		        				_coordinates.get("venus_reflex")
		        				);
		        		break;
		        		
		        	case 4:
		        		_earth = new Earth(
		        				gl, 
		        				_textures.get("earth"), 
		        				_coordinates.get("earth"), 
		        				_textures.get("clouds"), 
		        				_coordinates.get("clouds"), 
		        				_textures.get("atmosphere"), 
		        				_coordinates.get("atmosphere")
		        				);
		        		break;
		        		
		        	case 5:
		        		_moon = new Moon(
		        				gl, 
		        				_textures.get("moon"), 
		        				_coordinates.get("moon"), 
		        				_textures.get("moon_reflex"), 
		        				_coordinates.get("moon_reflex")
		        				);
		        		break;
		        		
		        	case 6:
		        		_mars = new Mars(
		        				gl, 
		        				_textures.get("mars"), 
		        				_coordinates.get("mars"), 
		        				_textures.get("mars_reflex"), 
		        				_coordinates.get("mars_reflex")
		        				);
		        		break;
		        		
		        	case 7:
		        		_jupiter = new Jupiter(
		        				gl, 
		        				_textures.get("jupiter"), 
		        				_coordinates.get("jupiter"), 
		        				_textures.get("jupiter_reflex"), 
		        				_coordinates.get("jupiter_reflex")
		        				);
		        		break;
		        		
		        	case 8:
		        		_saturn = new Saturn(
		        				gl, 
		        				_textures.get("saturn"), 
		        				_coordinates.get("saturn"), 
		        				_textures.get("saturn_ring"), 
		        				_coordinates.get("saturn_ring")
		        				);
		        		break;
		        		
		        	case 9:
		        		_uranus = new Uranus(
		        				gl, 
		        				_textures.get("uranus"), 
		        				_coordinates.get("uranus"), 
		        				_textures.get("uranus_ring"), 
		        				_coordinates.get("uranus_ring")
		        				);
		        		break;
		        		
		        	case 10:
		        		_neptune = new Neptune(
		        				gl, 
		        				_textures.get("neptune"), 
		        				_coordinates.get("neptune"), 
		        				_textures.get("neptune_reflex"), 
		        				_coordinates.get("neptune_reflex")
		        				);
		        		break;
		        		
		        	case 11:
		        		_pluto = new Pluto(
		        				gl, 
		        				_textures.get("pluto"), 
		        				_coordinates.get("pluto"), 
		        				_textures.get("pluto_reflex"), 
		        				_coordinates.get("pluto_reflex")
		        				);
		        		break;
		        		
		        	}
		        	
		        } 
		        catch(Exception e) {
		        	//Log.e(TAG2, "Can not pars coordinates:");
		        	//Log.e(TAG2, e.toString());
		        
		        }

		        _universe      = new Universe(gl, _textures.get("universe0"), _textures.get("universe1"), _universeSpeedNum);
		        _constellation = new Constellation(gl, _starCountNum, _consteSpeedNum);

		        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		        
		        gl.glEnable(GL10.GL_CULL_FACE);
		        gl.glFrontFace(GL10.GL_CCW);
			
		    }
		    
			
		    public void onSurfaceChanged(GL10 gl, int width, int height) {
				//Log.d(TAG2, "Changing screen size...");
				
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
		        	//Log.w(TAG2, e.toString());
		        
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
		        
		//****** _planetsNum
		        switch(_planetsNum) {
		        case 1:
		        	_sun.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 2:
		        	_mercury.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 3:
		        	_venus.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 4:
		        	_earth.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 5:
		        	_moon.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 6:
		        	_mars.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 7:
		        	_jupiter.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 8:
		        	_saturn.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 9:
		        	_uranus.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 10:
		        	_neptune.draw(gl, shiftGlobalY);
		        	break;
		        	
		        case 11:
		        	_pluto.draw(gl, shiftGlobalY);
		        	break;
		        	
		        }
		        
		    }
		    
		    
		    public float getGlobalShiftY() {
		        
		    	if(Math.abs(_shiftYCurrent) <= 0.1f * _shiftY) {
		        	_shiftYCurrent = 0.1f * _shiftY;
		        } 
		    	else {
		        	_shiftYCurrent = _shiftYCurrent / (1.0f + (0.1f * _motionK));
		        
		    	}

		        return _shiftYCurrent;
			
		    }
		    
		}
		
		
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			
			loadPrefs();
			
		}
		
		
		private void loadPrefs() {
			
			// _planets
			try {
				_planets = Integer.parseInt(_prefs.getString(PrefSettings.KEY_PLANETS, "1"));
				
			}
			catch(Exception ex) {
				
			}
			
			// _universes
			try {
				_universes = Integer.parseInt(_prefs.getString(PrefSettings.KEY_UNIVERSES, "1"));
				
			}
			catch(Exception ex) {
				
			}
			
			// _starCount
			_starCount = _prefs.getInt(PrefSettings.KEY_STAR_COUNT, 67);
			
			// _planetSpeed
			_planetSpeed = _prefs.getInt(PrefSettings.KEY_PLANET_SPEED, 3);
			
			// _universeSpeed
			_universeSpeed = _prefs.getInt(PrefSettings.KEY_UNIVERSE_SPEED, 3);
			
			// _consteSpeed
			_consteSpeed = _prefs.getInt(PrefSettings.KEY_CONSTELLATION_SPEED, 5);
			
			// _touchSense
			_touchSense = _prefs.getInt(PrefSettings.KEY_PLANET_TOUCH_SENSE, 2);
			
		}
		
	}
	
}
