package com.OffTheLine.desktopEngine;

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;

import com.OffTheLine.common.GameObject;
import com.OffTheLine.common.Logic;
import com.OffTheLine.desktopEngine.Graphics;
import com.OffTheLine.common.Input;

public class Engine implements com.OffTheLine.common.Engine {

    String _path;
    Graphics _graphics;
    Logic _logic;

    public Engine(String path) {
        _path = path;
    }

    public void init(Logic l)
    {
        _graphics = new Graphics("Paint");
        if (!_graphics.init(640, 480, _path))
            // Ooops. Ha fallado la inicialización.
            return;

        _logic = l;
    }

    public void update()
    {
        boolean _running = true;

        long lastFrameTime = System.nanoTime();

        while(_running){
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double delta = (double) nanoElapsedTime / 1.0E9;

            getLogic().update(delta);

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    _graphics.render(null);
                } while(_graphics.contentsRestored());
                _graphics.present();
            } while(_graphics.contentsLost());
        }
    }

    @Override
    public Graphics getGraphics() {
       return null;
    }

    @Override
    public Input getInput() {
        return null;
    }

    @Override
    public InputStream openInputStream(String filename) {
        return null;
    }

    @Override
    public Logic getLogic() {
        return null;
    }
}
