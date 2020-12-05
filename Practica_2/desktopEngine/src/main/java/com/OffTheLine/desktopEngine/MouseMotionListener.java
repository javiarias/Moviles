package com.OffTheLine.desktopEngine;

import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.awt.event.MouseEvent;

public class MouseMotionListener implements java.awt.event.MouseMotionListener
{
    /*Variables*/
    Input _i;
    MouseMotionListener(Input i) { _i = i; }

    /*Funciones*/

    //Para arrastrado del ratón
    @Override public void mouseDragged(MouseEvent e) { _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getID(), Input.TouchEvent.TouchType.DRAG)); }

    //Para movimiento del ratón
    @Override public void mouseMoved(MouseEvent e) { _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getID(), Input.TouchEvent.TouchType.MOVE)); }
}
