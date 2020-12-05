package com.OffTheLine.desktopEngine;

import java.util.ArrayList;

public class Input implements com.OffTheLine.common.Input
{
    ArrayList<TouchEvent> _events;

    MouseListener _mouse;
    MouseMotionListener _mouseMotion;

    Input()
    {
        _mouse = new MouseListener(this);
        _mouseMotion = new MouseMotionListener(this);
        _events = new ArrayList<TouchEvent>();
    }

    public MouseListener getMouseListener() {
        return _mouse;
    }
    public MouseMotionListener getMouseMotionListener() {
        return _mouseMotion;
    }

    @Override
    synchronized public ArrayList<TouchEvent> getTouchEvents()
    {
        ArrayList<TouchEvent> tmp = new ArrayList<TouchEvent>(_events);
        _events.clear();

        return tmp;
    }

    @Override
    synchronized public void addEvent(TouchEvent e) {
        _events.add(e);
    }
}
