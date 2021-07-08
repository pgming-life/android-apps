package com.jackbee.earthfree;

import com.jackbee.earthfree.RenderInterface;
import android.view.MotionEvent;
import interfaces.MotionCallback;


public class MotionProcessor implements MotionCallback{

    private float _x = 0;

    private RenderInterface _renderer;

    public MotionProcessor (RenderInterface renderer) {

        _renderer = renderer;

    }


    public void runMotionCallback(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            _x = event.getX();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float xdiff = (event.getX() - _x);
            _renderer.setYshift(xdiff);
            _x = event.getX();

        }

    }

}
