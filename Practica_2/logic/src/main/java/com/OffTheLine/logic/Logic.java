package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.GameObject;

import java.util.ArrayList;

public class Logic implements com.OffTheLine.common.Logic {

    int _x = 0;
    int _incX = 10;
    Engine _engine;

    ArrayList<GameObject> _objects = null;

    public Logic(Engine e) {
        _engine = e;
        _objects = new ArrayList<GameObject>();
    }

    @Override
    public ArrayList<GameObject> getObjects() {
        return _objects;
    }

    @Override
    public void update(double deltaTime) {
        int maxX = _engine.getGraphics().getWidth() - 300; // 300 : longitud estimada en píxeles del rótulo

        _x += _incX * deltaTime;
        while(_x < 0 || _x > maxX) {
            // Vamos a pintar fuera de la pantalla. Rectificamos.
            if (_x < 0) {
                // Nos salimos por la izquierda. Rebotamos.
                _x = -_x;
                _incX *= -1;
            }
            else if (_x > maxX) {
                // Nos salimos por la derecha. Rebotamos
                _x = 2*maxX - _x;
                _incX *= -1;
            }
        } // while
    }
}