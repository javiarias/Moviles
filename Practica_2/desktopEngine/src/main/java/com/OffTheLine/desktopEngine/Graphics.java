package com.OffTheLine.desktopEngine;

import com.OffTheLine.common.GameObject;

import javax.swing.JFrame;

import java.awt.Dimension;

import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;


public class Graphics extends com.OffTheLine.common.CommonGraphics {

    JFrame _window;

    public Graphics(String title) {
        _window = new JFrame(title);
    }

    Font _font = null;
    java.awt.image.BufferStrategy _strategy;

    Color _bgColor = Color.BLACK;
    java.awt.Graphics _graphics;

    Deque<AffineTransform> _savedTransform;

    public boolean init(int width, int height, String assetsPath, Engine engine) {

        super.init(width, height, assetsPath, engine);

        _savedTransform = new LinkedList<>();

        _window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _window.setSize(width, height);

        /*
        Dimension d = new Dimension(width,height);
        _window.getContentPane().setPreferredSize(d);
        _window.pack();

         */

        // Vamos a usar renderizado activo. No queremos que Swing llame al
        // método repaint() porque el repintado es continuo en cualquier caso.
        _window.setIgnoreRepaint(true);

        // Hacemos visible la ventana.
        _window.setVisible(true);

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                _window.createBufferStrategy(4);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return false;
        }
        else {
            // En "modo debug" podríamos querer escribir esto.
            //System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }

        // Obtenemos el Buffer Strategy que se supone que acaba de crearse.
        _strategy = _window.getBufferStrategy();
        //_width = width;
        //_height = height;

        try
        {
            _font = newFont(_engine.openInputStream(_assetsPath + "Bangers-Regular.ttf"), 40, false);
        }
        catch(Exception e)
        {
            return false;
        }

        return true;
    }

    java.awt.Graphics getDrawGraphics() {
        return _strategy.getDrawGraphics();
    }

    public boolean contentsRestored() {
       return  _strategy.contentsRestored();
    }

    public boolean contentsLost() {
        return _strategy.contentsLost();
    }

    public void present() {
        _strategy.show();
    }

    public void updateDrawGraphics() { _graphics = _strategy.getDrawGraphics(); }

    public void dispose() { _graphics.dispose(); }

    @Override
    public void render(ArrayList<GameObject> objects){

        //con tal de que al menos el contenido se vea dentro de la ventana,
        //trasladamos lo suficiente para que (0, 0) se halle dentro de la ventana visible

        fixAspectRatio(getTrueWidth(), getTrueHeight());

        //CLEAR SIEMPRE ANTES DE TRANSLATE
        clear(_bgColor);

        translate(_xOffset, _yOffset);
        //System.out.println("xOffset: " + _xOffset + ", yOffset: " + _yOffset);

        scale(_scaleW, _scaleH);
        //System.out.println("xScale: " + _scaleW + ", yScale: " + _scaleH);

        //getGraphics().render(getLogic().getObjects());

        setColor(Color.RED);
        fillRect(0, 0, getWidth(),getHeight());


        setColor(Color.CYAN);
        //drawLine(0, 1, getWidth(), 1);

        /*
        setColor(Color.RED);
        fillRect(0, 0, getWidth(), getHeight() / 3.0f);

        setColor(Color.YELLOW);
        fillRect(0, (getHeight()) / 3.0f, getWidth(), (2 * getHeight()) / 3.0f);

        setColor(Color.MAGENTA);
        fillRect(0, (2 * getHeight()) / 3.0f, getWidth(), (3 * getHeight()) / 3.0f);

         */

        // Ponemos el rótulo (si conseguimos cargar la fuente)
        if (_font != null) {
            save();

            translate(getWidth() / 2, getHeight() / 2);
            setColor(Color.WHITE);
            _graphics.setFont(_font.getFont());
            _graphics.drawString("no voy a quitar la\n Lupa, ignacio", 0, 0);

            save();

            translate(-getWidth() / 2, 0);
            rotate(45);
            setColor(Color.BLUE);
            _graphics.setFont(_font.getFont());
            _graphics.drawString("arriba", 0, 0);

            restoreAll();
            restore();
            setColor(Color.MAGENTA);
            _graphics.setFont(_font.getFont());
            _graphics.drawString("abajo", 0, 0);
        }

        //setColor(Color.RED);
        //drawLine(0, 0, getWidth(), getHeight());

        translate(-_xOffset, -_yOffset);
    }

    @Override
    public Font newFont(InputStream is, int size, boolean isBold) throws Exception {
        Font ret = new Font(is, size, isBold);
        return ret;
    }


    @Override
    public void clear(Color color) {
        setColor(color);
        fillRect(0, 0, getTrueWidth(), getTrueHeight());
    }


    @Override
    public void translate(float x, float y) {
        _graphics.translate((int)x, (int)y);
    }


    @Override
    public void scale(float x, float y) {
        Graphics2D g2d = (Graphics2D)_graphics;
        g2d.scale(x, y);
    }


    @Override
    public void rotate(float angle) {
        Graphics2D g2d = (Graphics2D)_graphics;
        g2d.rotate(Math.toRadians(angle));
    }



    @Override
    public void save() {
        Graphics2D g2d = (Graphics2D)_graphics;
        AffineTransform a = g2d.getTransform();
        _savedTransform.addFirst(a);
    }


    @Override
    public void restore() {
        if(!_savedTransform.isEmpty()) {
            Graphics2D g2d = (Graphics2D)_graphics;

            AffineTransform a = _savedTransform.removeLast();
            g2d.setTransform(a);
        }
    }

    @Override
    public void restoreAll() {
        Graphics2D g2d = (Graphics2D)_graphics;

        while(!_savedTransform.isEmpty()) {
            AffineTransform a = _savedTransform.removeLast();
            g2d.setTransform(a);
        }
    }


    @Override
    public void setColor(Color color) {
        _graphics.setColor(color);
    }


    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        _graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }



    @Override
    public void fillRect(float x1, float y1, float x2, float y2) {
        _graphics.fillRect((int)x1, (int)y1, (int)x2, (int)y2);
    }



    @Override
    public void drawText(String text, float x, float y) {

    }

    @Override
    public int getWidth() {
        return _initWidth;
    }

    @Override
    public int getHeight() {
        return _initHeight;
    }

    public int getTrueWidth() {
        return _window.getWidth();
    }

    public int getTrueHeight() {
        return _window.getHeight();
    }

    @Override
    public void release(){
        _strategy.dispose();
    }
}
