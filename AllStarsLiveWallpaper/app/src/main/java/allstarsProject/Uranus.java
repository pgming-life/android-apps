package allstarsProject;

import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;
import modelUnit.ObjectUnit;


public class Uranus {
	
	private ObjectUnit _uranus;
	private ObjectUnit _uranusRing;
	
	private float _xAngle0 = 0; // angle 10
	private float _zAngle0 = 0; // angle 39
	private float _yAngle  = 0;
	private float _slowK   = 1.6f;
	
	
	public Uranus(
			GL10        gl, 
			Bitmap      bitmap0, 
			InputStream inputStream0, 
			Bitmap      bitmap1, 
			InputStream inputStream1
			) throws Exception {
	
		_uranus     = new ObjectUnit();
		_uranus.init(gl, bitmap0, inputStream0);
		_uranusRing = new ObjectUnit();
		_uranusRing.init(gl, bitmap1, inputStream1);
		
	}
	
	
	public void draw(GL10 gl, float shiftY) {
		
		gl.glLoadIdentity();
		
//****** uranus
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glLoadIdentity();
		gl.glRotatef(_xAngle0, 1f, 0f, 0f);
		gl.glRotatef(_zAngle0, 0f, 0f, 1f);
		gl.glRotatef(_yAngle, 0f, 1f, 0f);
		_uranus.draw(gl, false);
		gl.glDisable(GL10.GL_LIGHTING);
		
//****** uranus ring
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glLoadIdentity();
		gl.glRotatef(_xAngle0, 1f, 0f, 0f);
		gl.glRotatef(_zAngle0, 0f, 0f, 1f);
		_uranusRing.draw(gl, false);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_LIGHTING);
		
//****** rotate
		_xAngle0 = 10f;
		_zAngle0 = 39f;
		_yAngle += shiftY / _slowK;
		
		if(Math.abs(_yAngle)>=360)
			_yAngle = 0;
		
	}
	
}
