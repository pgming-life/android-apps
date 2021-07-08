package earthProject.free;

import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import modelUnit.ObjectUnit;
import android.graphics.Bitmap;


public class Planet {

    private ObjectUnit   _planet;
    private ObjectUnit   _clouds;
    private ObjectUnit   _atmosphere;

    private float _yAngle0 = 0;
    private float _yAngle1 = 0;
    private float _yAngle2 = 0;
    private float _slowK   = 1.7f;
    private float _slowL   = 1.02f;


    public Planet(
                GL10 gl,
                Bitmap bitmap0,
                Bitmap bitmap1,
                Bitmap bitmap2,
                InputStream inputStream0,
                InputStream inputStream1,
                InputStream inputStream2
                ) throws Exception {

        _planet     = new ObjectUnit();
        _planet.init(gl, bitmap0, inputStream0);
        _clouds     = new ObjectUnit();
        _clouds.init(gl, bitmap1, inputStream1);
        _atmosphere = new ObjectUnit();
        _atmosphere.init(gl, bitmap2, inputStream2);

    }


    public void draw(GL10 gl, float shiftY) {

        gl.glLoadIdentity();

//****** earth
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_LIGHT1);
        gl.glLoadIdentity();
        gl.glRotatef(_yAngle0, 0f, 1f, 0f);
        _planet.draw(gl, false);
        gl.glDisable(GL10.GL_LIGHTING);

//****** clouds
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_LIGHT1);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glLoadIdentity();
        gl.glScalef(0.98f, 0.98f, 0.98f);
        gl.glRotatef(_yAngle1, 0f, 1f, 0f);
        _clouds.draw(gl, false);
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_LIGHTING);

//****** atmosphere
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_LIGHT1);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glLoadIdentity();
        gl.glRotatef(_yAngle2, 0f, 1f, 0f);
        _atmosphere.draw(gl, false);
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_LIGHTING);

//****** rotate
        _yAngle0 += shiftY/_slowK;
        _yAngle1 += shiftY/_slowL;
        _yAngle2 += shiftY;

        if (Math.abs(_yAngle0)>=360)
            _yAngle0 = 0;
        if (Math.abs(_yAngle1)>=360)
            _yAngle1 = 0;
        if (Math.abs(_yAngle2)>=360)
            _yAngle2 = 0;

    }

}
