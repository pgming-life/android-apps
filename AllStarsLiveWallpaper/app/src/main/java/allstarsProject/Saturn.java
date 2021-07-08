package allstarsProject;

import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;
import modelUnit.ObjectUnit;


public class Saturn {
	
	private ObjectUnit _saturn;
	private ObjectUnit _saturnRing;
	
	private float _xzAngle0 = 0; // angle 16
	private float _yAngle   = 0;
	private float _slowK    = 1.4f;
	
	
	public Saturn(
			GL10        gl, 
			Bitmap      bitmap0, 
			InputStream inputStream0, 
			Bitmap      bitmap1, 
			InputStream inputStream1
			) throws Exception {
		
		_saturn     = new ObjectUnit();
		_saturn.init(gl, bitmap0, inputStream0);
		_saturnRing = new ObjectUnit();
		_saturnRing.init(gl, bitmap1, inputStream1);
		
	}
	
	
	public void draw(GL10 gl, float shiftY) {
		
		gl.glLoadIdentity();
		
//****** saturn
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glLoadIdentity();
		gl.glRotatef(_xzAngle0, 1f, 0f, 1f);
		gl.glRotatef(_yAngle, 0f, 1f, 0f);
		_saturn.draw(gl, false);
		gl.glDisable(GL10.GL_LIGHTING);
		
//****** saturn ring
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glLoadIdentity();
		gl.glRotatef(_xzAngle0, 1f, 0f, 1f);
		_saturnRing.draw(gl, false);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_LIGHTING);
		
//****** rotate
		_xzAngle0 = 16f;
		_yAngle += shiftY / _slowK;
		
		if(Math.abs(_yAngle)>=360)
			_yAngle = 0;
		
	}
	
}
