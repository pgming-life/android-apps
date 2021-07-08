package modelUnit;

import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import modelUnit.modelcode.CodeLoader;
import android.graphics.Bitmap;
import android.util.Log;


public class ObjectUnit extends BufferBase {
	
	
	public void init (GL10 gl, Bitmap bitmap, InputStream inputStream) 
			throws Exception {
		
		CodeLoader loader;
		
		try {
		    loader = new CodeLoader(inputStream);
		    loader.load();
		} 
		catch(Exception e) {
			Log.e("TEST 3D", "Can not load obj-file");
			throw new Exception ("Can not load obj-file");
		
		}
		
		float verticesCoords[] = loader.getVerticesCoords();
		float textureCoords[]  = loader.getTextureCoords();
		float normalesCoords[] = loader.getVerticesCoords();

		super.init(gl, verticesCoords, bitmap, textureCoords, normalesCoords);
    
	}

}
