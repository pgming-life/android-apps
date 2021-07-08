package allstarsProject;

import javax.microedition.khronos.opengles.GL10;
import modelUnit.Rectangle;
import android.graphics.Bitmap;


public class Universe {
	
	private float _widthTotal;
	private float _zPosition = -9;
    private float _shiftX    = 0;
	private float _currentX1 = 0.0f;
	private float _currentX2;
	
	private Rectangle _universePart0;
	private Rectangle _universePart1;
	
	private Bitmap _bitmap0;
	private Bitmap _bitmap1;

    
	public Universe(GL10 gl, Bitmap bitmap0, Bitmap bitmap1, int universeSpeedNum) {
    	
		_bitmap0 = bitmap0;
    	_bitmap1 = bitmap1;
    	_universePart0 = new Rectangle();
    	_universePart1 = new Rectangle();
    	_universePart0.init(gl,Bitmap.createBitmap(_bitmap0),0,0);
	    _universePart1.init(gl,Bitmap.createBitmap(_bitmap1),0,0);
	    _shiftX = universeSpeedNum;
    
	}
    
    
	public void changeSize(float size) {
  		
		_universePart0.changeSize(size,size);
   	    _universePart1.changeSize(size,size);
    	_widthTotal = 2 * size;
    	_currentX1 = 0.0f;
    	_currentX2  = _widthTotal;
    
	}
    
    
	public void draw(GL10 gl) {
    	
		gl.glLoadIdentity();
		gl.glTranslatef(_currentX1,0,_zPosition);
		_universePart0.draw(gl, false);
    	
    	gl.glLoadIdentity();
		gl.glTranslatef(_currentX2,0,_zPosition);
		_universePart1.draw(gl, false);
    	
    	_currentX1 -= 0.0001f * _shiftX;
    	_currentX2 -= 0.0001f * _shiftX;
    	
    	if(_currentX1 <=-_widthTotal) {
    		_currentX1 = _widthTotal;
    		_currentX2 = 0;
    	
    	}
    	else if(_currentX2 <=-_widthTotal) {
    		_currentX1 = 0;
    		_currentX2 = _widthTotal;
    	
    	}
    
	}

}
