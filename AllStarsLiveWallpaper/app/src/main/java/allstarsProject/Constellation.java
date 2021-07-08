package allstarsProject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;
import modelUnit.Rectangle;


public class Constellation {
   
	private Star        _stars[];
    private FloatBuffer _coordsBuffer;
    private FloatBuffer _colorsBuffer;
    
    static private int    _starsNum = 0;
    static private Random rand      = new Random();
    
    static private float _areaWidth;
    static private float _areaHeight;
    
    private float _starWidth  = 0.0017f;
    private float _starHeight = 0.0017f;
    
    private float _xMinLimit;
    
    static private float _shiftX = 0;
    
    private Rectangle _star;
    
    
    public Constellation(GL10 gl, int starCountNum, int consteSpeedNum) {
    	
    	_star = new Rectangle();
    	_star.init(gl, _starWidth, _starHeight);
    	_starsNum = starCountNum;
    	_shiftX = consteSpeedNum;
    
    }
    
    
    public void changeSize(float w, float h) {
    	
    	_areaWidth  = w;
    	_areaHeight = h;
    	
    	_xMinLimit = -(_starWidth*2)-_areaWidth;
    	
    	_stars = new Star [_starsNum];
    	
    	for(int i=0;i<_starsNum;i++) {
    		_stars[i]=new Star();
    	
    	}
    	
    	float starsCoords[] = new float[_starsNum*3];
    	float starsColors[] = new float[_starsNum*4];
    	int j,k;
    	
    	for(int i=0;i<_starsNum;i++) {
    		
    		j=i*3;
    		k=i*4;
    		
    		starsColors[k+0] = 1.0f;
    		starsColors[k+1] = 1.0f;
    		starsColors[k+2] = 1.0f;
    		starsColors[k+3] = 1.0f;
    		
    		starsCoords[j+0]= _stars[i].x;
    		starsCoords[j+1]= _stars[i].y;
    		starsCoords[j+2]= _stars[i].z;
    	
    	}
    	
    	ByteBuffer cdbb = ByteBuffer.allocateDirect(starsCoords.length * 4);
        cdbb.order(ByteOrder.nativeOrder());
        _coordsBuffer = cdbb.asFloatBuffer();
        _coordsBuffer.put(starsCoords);
        _coordsBuffer.position(0);
        
        ByteBuffer clbb = ByteBuffer.allocateDirect(starsColors.length * 4);
        clbb.order(ByteOrder.nativeOrder());
        _colorsBuffer = clbb.asFloatBuffer();
        _colorsBuffer.put(starsColors);
        _colorsBuffer.position(0);
    
    }

    
    public void draw (GL10 gl) {
    
    	for(int i=0;i<_starsNum;i++) {
    		gl.glLoadIdentity();
       		gl.glTranslatef(_stars[i].x,_stars[i].y,_stars[i].z);
       		gl.glRotatef(45, 0f, 0f, 1f);
    		_star.draw(gl,false);
    		_stars[i].x -= _stars[i].shiftX;
    		
    		if(_stars[i].x < _xMinLimit) {
    			_stars[i].y = (rand.nextFloat()*2 -1)*_areaHeight;
    			_stars[i].x = _areaWidth;
    		
    		}
    	
    	}
    
    }
    
    
    static class Star {
    	
    	float x = 0;
    	float y = 0;
    	float z = 0;
    	
    	int size = 1;
    	float shiftX = 0.0001f * _shiftX;
    	
    	long deadLine;
    	
    	
    	public Star () {
    		
    		setRandomCoords();
    	
    	}
    	
    	
    	public void setRandomCoords () {
    		
    		x = (rand.nextFloat()*2 -1)*_areaWidth;
    		y = (rand.nextFloat()*2 -1)*_areaHeight;
    		z = -8;
    	
    	}
    	
    }

}
