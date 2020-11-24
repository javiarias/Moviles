package com.OffTheLine.appEngine;

import android.content.Context;
import android.content.res.AssetManager;

import com.OffTheLine.common.Logic;

import java.io.FileInputStream;
import java.io.InputStream;

public class Engine implements com.OffTheLine.common.Engine, Runnable {

    String _path;
    Graphics _graphics;
    Logic _logic;
    Input _input;
    AssetManager _manager;
    Context _context;

    public Engine(String path, AssetManager manager, Context context) {
        _path = path;
        _manager = manager;
        _context = context;
    }

    public void init(Logic l)
    {
        _graphics = new Graphics(_context, _manager);
        if (!_graphics.init(640, 480, _path, this))
            // Ooops. Ha fallado la inicialización.
            return;

        _logic = l;
    }

    public void run() {

        if (_renderThread != Thread.currentThread()) {
            // ¿¿Quién es el tuercebotas que está llamando al
            // run() directamente?? Programación defensiva
            // otra vez, con excepción, por merluzo.
            throw new RuntimeException("run() should not be called directly");
        }

        System.out.println("me cago en ppp");

        // Antes de saltar a la simulación, confirmamos que tenemos
        // un tamaño mayor que 0. Si la hebra se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while (_running && _graphics.getSurface().getWidth() == 0)
            // Espera activa. Sería más elegante al menos dormir un poco.
            ;

        // Bucle principal.
        while (_running) {
            update();
        }
    }

    public void update()
    {

        long lastFrameTime = System.nanoTime();

        long currentTime = System.nanoTime();
        long nanoElapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;
        double delta = (double) nanoElapsedTime / 1.0E9;

        _logic.update(delta);

        _graphics.updateCanvas();

        _graphics.render(_logic.getObjects());

        _graphics.present();
    }

    /**
     * Método llamado para solicitar que se continue con el
     * active rendering. El "juego" se vuelve a poner en marcha
     * (o se pone en marcha por primera vez).
     */
    public void resume() {

        if (!_running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva, nunca se sabe quién va a
            // usarnos...)
            _running = true;
            // Lanzamos la ejecución de nuestro método run()
            // en una hebra nueva.
            _renderThread = new Thread(this);
            _renderThread.start();
        } // if (!_running)

    } // resume

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Método llamado cuando el active rendering debe ser detenido.
     * Puede tardar un pequeño instante en volver, porque espera a que
     * se termine de generar el frame en curso.
     *
     * Se hace así intencionadamente, para bloquear la hebra de UI
     * temporalmente y evitar potenciales situaciones de carrera (como
     * por ejemplo que Android llame a resume() antes de que el último
     * frame haya terminado de generarse).
     */
    public void pause() {

        if (_running) {
            _running = false;
            while (true) {
                try {
                    _renderThread.join();
                    _renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            } // while(true)
        } // if (_running)

    } // pause

    @Override
    public Graphics getGraphics() { return _graphics; }

    @Override
    public Input getInput() {
        return _input;
    }

    @Override
    public InputStream openInputStream(String path) {
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

    @Override
    public Logic getLogic() {
        return _logic;
    }

    @Override
    public void release(){
        _graphics.release();
    }


    Thread _renderThread;
    volatile boolean _running = false;
}
