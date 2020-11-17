package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.GameObject;

import java.util.ArrayList;

public class Logic implements com.OffTheLine.common.Logic {

    public Logic(Engine e) {
        _engine = e;
    }


    int _x = 0;
    int _incX = 10;
    Engine _engine;

    ArrayList<GameObject> _objects;


    @Override
    public void update() {
        boolean _running = true;

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        while(_running){
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double delta = (double) nanoElapsedTime / 1.0E9;

            updateLogic(delta);

            // Informe de FPS
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }

            ++frames;

            _engine.getGraphics().render(_objects);
        }

    }

    @Override
    public void updateLogic(double deltaTime) {
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
    }  // update

}