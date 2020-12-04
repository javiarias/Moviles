package com.OffTheLine.desktopEngine;

import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.awt.event.MouseEvent;
public class MouseListener implements java.awt.event.MouseListener
{
    Input _i;

    MouseListener(Input i) { _i = i; }


    //click es pulsar y soltar, press es solo pulsar

    @Override
    public void mouseClicked(MouseEvent e)
    {
        _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getID(), Input.TouchEvent.TouchType.CLICK));
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getID(), Input.TouchEvent.TouchType.PRESS));
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getID(), Input.TouchEvent.TouchType.RELEASE));
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getID(), Input.TouchEvent.TouchType.ENTER));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        _i.addEvent(new Input.TouchEvent(new Vector2D(e.getX(), e.getY()), e.getID(), Input.TouchEvent.TouchType.EXIT));
    }
}
