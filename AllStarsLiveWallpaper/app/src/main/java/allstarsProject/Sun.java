package allstarsProject;

import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;
import modelUnit.ObjectUnit;
import modelUnit.Rectangle;


public class Sun {
	
	private ObjectUnit _sun;
	private ObjectUnit _heliosphere;
	private Rectangle  _sunlight0;
	private Rectangle  _sunlight1;
	private Rectangle  _sunlight2;
	
	private float _zAngle0 = 0;
	private float _zAngle1 = 0;
	private float _yAngle0 = 0;
	private float _yAngle1 = 0;
	private float _slowS   = 0.1f;
	private float _slowK   = 1.7f;
	
	static final float _sunWidth0  = 1.18f;
	static final float _sunHeight0 = 1.18f;
	static final float _sunWidth1  = 1.19f;
	static final float _sunHeight1 = 1.19f;
	static final float _sunWidth2  = 1.2f;
	static final float _sunHeight2 = 1.2f;
	
	
	public Sun(
			GL10        gl, 
			Bitmap      bitmap0, 
			InputStream inputStream0, 
			Bitmap      bitmap1, 
			InputStream inputStream1, 
			Bitmap      bitmap2, 
			Bitmap      bitmap3, 
			Bitmap      bitmap4
			) throws Exception {
		
		_sun         = new ObjectUnit();
		_sun.init(gl, bitmap0, inputStream0);
		_heliosphere = new ObjectUnit();
		_heliosphere.init(gl, bitmap1, inputStream1);
		_sunlight0   = new Rectangle();
		_sunlight0.init(gl, bitmap2, _sunWidth0, _sunHeight0);
		_sunlight1   = new Rectangle();
		_sunlight1.init(gl, bitmap3, _sunWidth1, _sunHeight1);
		_sunlight2   = new Rectangle();
		_sunlight2.init(gl, bitmap4, _sunWidth2, _sunHeight2);
		
	}
	
	
	public void draw(GL10 gl, float shiftY) {
		
		gl.glLoadIdentity();
		
//****** sunlight0
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -5.1f);
		gl.glRotatef(_zAngle0, 0f, 0f, 1f);
		_sunlight0.draw(gl, false);
		
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -5.0f);
		gl.glRotatef(_zAngle1, 0f, 0f, 1f);
		_sunlight1.draw(gl, false);
		gl.glDisable(GL10.GL_BLEND);
		
//****** sun
		gl.glLoadIdentity();
		gl.glRotatef(_yAngle0, 0f, 1f, 0f);
		_sun.draw(gl, false);
		
//****** helioshere
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glLoadIdentity();
		gl.glRotatef(_yAngle1, 0f, 1f, 0f);
		_heliosphere.draw(gl, false);
		gl.glDisable(GL10.GL_BLEND);
		
//****** sunlight2
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 1.0f);
		_sunlight2.draw(gl, false);
		gl.glDisable(GL10.GL_BLEND);
		
//****** rotate
		_zAngle0 += shiftY * _slowS;
		_zAngle1 -= shiftY * _slowS;
		
		_yAngle0 += shiftY / _slowK;
		_yAngle1 += shiftY;
		
		if(Math.abs(_zAngle0)>=360)
			_zAngle0 = 0;
		if(Math.abs(_zAngle1)>=360)
			_zAngle1 = 0;
		if(Math.abs(_yAngle0)>=360)
			_yAngle0 = 0;
		if(Math.abs(_yAngle1)>=360)
			_yAngle1 = 0;
		
	}
	
}
