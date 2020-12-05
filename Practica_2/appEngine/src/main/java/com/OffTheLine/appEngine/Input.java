package com.OffTheLine.appEngine;

import java.util.ArrayList;

public class Input implements com.OffTheLine.common.Input
{
    /*Variables*/
    ArrayList<TouchEvent> _events;
    TouchListener _touch;

    /*Funciones*/
    public TouchListener getTouchListener() { return _touch; }

    //Constructora
    Input()
    {
        _touch = new TouchListener(this);
        _events = new ArrayList<TouchEvent>();
    }

    //Para conseguir la lista de eventos
    @Override synchronized public ArrayList<TouchEvent> getTouchEvents()
    {
        ArrayList<TouchEvent> tmp = new ArrayList<TouchEvent>(_events); //new porque todo se pasa por referencia
        _events.clear(); //Se borra la original
        return tmp;
    }

    //Para añadir eventos
    @Override synchronized public void addEvent(TouchEvent e) { _events.add(e); }
}
