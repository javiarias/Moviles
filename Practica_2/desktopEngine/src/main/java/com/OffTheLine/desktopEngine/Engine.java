package com.OffTheLine.desktopEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.OffTheLine.common.Logic;

public class Engine implements com.OffTheLine.common.Engine {

    /*Variables*/
    String _path;
    Graphics _graphics;
    Logic _logic;
    Input _input;

    /*Funciones*/

    /*Getters*/

    //Obtener la lógica
    @Override public Logic getLogic() { return _logic; }

    //Obtener los gráficos
    @Override public Graphics getGraphics() { return _graphics; }

    //Obtener el input
    @Override public Input getInput() { return _input; }

    //Constructora
    public Engine(String path) { _path = path; }

    //Inicializacion
    public void init(Logic l)
    {
        _graphics = new Graphics("Paint");
        if (!_graphics.init(640, 480, _path, this))
            return; // Ooops. Ha fallado la inicialización.

        _logic = l;
        _input = new Input();
        _graphics.addMouseListener(_input.getMouseListener());
        _graphics.addMouseMotionListener(_input.getMouseMotionListener());
        l.init();
    }

    //Update
    public void update()
    {
        boolean _running = true;
        long lastFrameTime = System.nanoTime();

        while(_running)
        {
            //Temas de tiempo (para el delta time)
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double delta = (double) nanoElapsedTime / 1.0E9;

            _logic.update(delta);

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    _graphics.updateDrawGraphics();

                    try
                    {
                        _graphics.render();
                        _logic.render(_graphics);
                    }
                    finally
                    {
                        _graphics.dispose();
                    }
                } while(_graphics.contentsRestored());
                _graphics.present();
            } while(_graphics.contentsLost());
        }
    }

    //Para liberar los gráficos
    @Override public void release() { _graphics.release(); }

    /*Para tratar archivos*/

    @Override public InputStream openInputStream(String path)
    {
        InputStream is;

        try {
            is = new FileInputStream(path);
        }
        catch (Exception e) {
            System.err.println("Error cargando " + path + " : " + e);
            return null;
        }

        return is;
    }

    @Override public InputStream getFile(String path) throws Exception
    {
        InputStream is;

        try {
            is = new FileInputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return is;
    }
}
