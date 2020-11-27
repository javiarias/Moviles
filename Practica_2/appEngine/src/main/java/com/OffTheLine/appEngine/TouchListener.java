package com.OffTheLine.appEngine;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.OffTheLine.common.Vector2D;

public class TouchListener implements View.OnTouchListener
{
    Input _i;

    TouchListener(Input i) { _i = i; }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getPointerId(0), Input.TouchEvent.TouchType.PRESS));
        return false;
    }
}
