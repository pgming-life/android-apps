package modelUnit;

import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;


public class Rectangle extends BufferBase {


    public void init(GL10 gl, Bitmap bitmap, float width, float height) {

        float verticesCoords[] = {
                -width,-height,0,width,-height,0,width,height,0,
                -width,height,0,-width,-height,0,width,height,0
        };

        float textureCoords[]  = {
                1,1,0,1,0,0,
                1,0,1,1,0,0
        };

        super.init(gl, verticesCoords, bitmap, textureCoords);

    }


    public void init(GL10 gl, float width, float height) {

        float verticesCoords[] = {
                -width,-height,0,width,-height,0,width,height,0,
                -width,height,0,-width,-height,0,width,height,0
        };

        super.init(gl, verticesCoords, null, null, null);

    }


    public void changeSize(float width, float height) {

        float verticesCoords[] = {
                -width,-height,0,width,-height,0,width,height,0,
                -width,height,0,-width,-height,0,width,height,0
        };

        super.changeCoords(verticesCoords);

    }

}
