package modelUnit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;
import android.opengl.GLUtils;


public class BufferBase {
	
	protected FloatBuffer _vertexBuffer;
	protected FloatBuffer _textureBuffer;
	protected FloatBuffer _normaleBuffer;
	
	protected int         _verticesNum = 0;
	
	static protected int  _texIndex = 0;
	static protected int  _textures[];
	
	protected int _textureId;
	
	boolean normalAvialable  = false;
	boolean textureAvialable = false;
	
	
	static public void initTexturesArray(GL10 gl, int textures_num) {
		
		_texIndex = 0;
        _textures = new int[textures_num];   
        gl.glGenTextures(textures_num, _textures, 0);
	
	}
	
	
	public void init(GL10 gl, float verticesCoords[], Bitmap bitmap, 
			float textureCoords[], float normalesCoords[])  {
		
		_verticesNum = verticesCoords.length/3;

        ByteBuffer vbb = ByteBuffer.allocateDirect(verticesCoords.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        _vertexBuffer.put(verticesCoords);
        _vertexBuffer.position(0);
        
        if(normalesCoords != null) {
        	normalAvialable = true;
            ByteBuffer nbb = ByteBuffer.allocateDirect(normalesCoords.length * 4);
            nbb.order(ByteOrder.nativeOrder());
            _normaleBuffer = nbb.asFloatBuffer();
            _normaleBuffer.put(normalesCoords);
            _normaleBuffer.position(0);
        
        }
        
        if(textureCoords != null) {
        	textureAvialable = true;

            ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
            tbb.order(ByteOrder.nativeOrder());
            _textureBuffer = tbb.asFloatBuffer();
            _textureBuffer.put(textureCoords);
            _textureBuffer.position(0);
        
            _textureId = _textures[_texIndex];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureId);
        
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_CLAMP_TO_EDGE);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                GL10.GL_REPLACE);
        
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        
            bitmap.recycle();
            _texIndex++;
        
        }
    
	}
	
	
	public void changeCoords(float verticesCoords[]) {
	    
		_verticesNum = verticesCoords.length/3;

	    ByteBuffer vbb = ByteBuffer.allocateDirect(verticesCoords.length * 4);
	    vbb.order(ByteOrder.nativeOrder());
	    _vertexBuffer = vbb.asFloatBuffer();
	        
	    _vertexBuffer.put(verticesCoords);
	    _vertexBuffer.position(0);
	
	}
	
	
	public void init(GL10 gl, float verticesCoords[], Bitmap bitmap, 
			float textureCoords[])  {
		
		init(gl, verticesCoords, bitmap, textureCoords, null);
	
	}
	
	
	public void draw(GL10 gl) {
		
		draw(gl,true);
	
	}
	
	
	public void draw(GL10 gl, boolean resetMatrix) {
		
		if(resetMatrix)
		    gl.glLoadIdentity();
		
		if(textureAvialable) {
	    	gl.glEnable(GL10.GL_TEXTURE_2D);
		    gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureId);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);
		
		}

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
		
		if(textureAvialable)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, _textureBuffer);
		
		if(normalAvialable)
			gl.glNormalPointer(GL10.GL_FLOAT, 0, _normaleBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, _verticesNum);
		
		if(textureAvialable)
			gl.glDisable(GL10.GL_TEXTURE_2D);
    
	}

}
