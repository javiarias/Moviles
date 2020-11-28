package com.OffTheLine.appEngine;

import java.util.ArrayList;
import java.util.List;

public class Input implements com.OffTheLine.common.Input
{
    ArrayList<TouchEvent> _events;

    public TouchListener getTouchListener() {
        return _touch;
    }

    TouchListener _touch;

    Input()
    {
        _touch = new TouchListener(this);
        _events = new ArrayList<TouchEvent>();
    }

    @Override
    synchronized public ArrayList<TouchEvent> getTouchEvents()
    {
        ArrayList<TouchEvent> tmp = new ArrayList<TouchEvent>(_events);
        _events.clear();

        return tmp;
    }

    @Override
    synchronized public void addEvent(TouchEvent e)
    {
        _events.add(e);
    }
}
