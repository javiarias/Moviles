package com.OffTheLine.appEngine;

import android.view.MotionEvent;
import android.view.View;
import com.OffTheLine.common.Vector2D;

public class TouchListener implements View.OnTouchListener
{
    /*Variables*/
    Input _i;
    TouchListener(Input i) { _i = i; }

    /*Funciones*/
    @Override public boolean onTouch(View v, MotionEvent e) {
        //Vector 2D para posicion, 0 por ser para un único dedo, y PRESS por ser único tipo de interacción
        _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getPointerId(0), Input.TouchEvent.TouchType.PRESS));
        return false;
    }
}
