package allstarsProject;

import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import modelUnit.ObjectUnit;
import android.graphics.Bitmap;


public class Mars {
	
	private ObjectUnit _mars;
	private ObjectUnit _reflex;
	
	private float _yAngle0   = 0;
	private float _yAngle1   = 0;
	private float _slowK     = 1.6f;
	
	
	public Mars(
		        GL10 gl, 
		        Bitmap bitmap0, 
		        InputStream inputStream0, 
		        Bitmap bitmap1, 
		        InputStream inputStream1
		        ) throws Exception {
		
		_mars = new ObjectUnit();
		_mars.init(gl, bitmap0, inputStream0);
		_reflex = new ObjectUnit();
		_reflex.init(gl, bitmap1, inputStream1);		
		
	}
	
	
	public void draw(GL10 gl, float shiftY) {
		
		gl.glLoadIdentity();
	        
//********** mars
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_LIGHT1);
        gl.glLoadIdentity();
        gl.glScalef(0.93f, 0.93f, 0.93f);
        gl.glRotatef(_yAngle0, 0f, 1f, 0f);
        _mars.draw(gl, false);
        gl.glDisable(GL10.GL_LIGHTING);
	        
//********** reflex
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_LIGHT1);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glLoadIdentity();
        gl.glScalef(0.9f, 0.9f, 0.9f);
        gl.glRotatef(_yAngle1, 0f, 1f, 0f);
        _reflex.draw(gl, false);
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_LIGHTING);
        
//********** rotate
        _yAngle0 += shiftY / _slowK;
        _yAngle1 += shiftY;
        
        if(Math.abs(_yAngle0)>=360)
        	_yAngle0 = 0;
        if(Math.abs(_yAngle1)>=360)
        	_yAngle1 = 0;
        
	}

}
