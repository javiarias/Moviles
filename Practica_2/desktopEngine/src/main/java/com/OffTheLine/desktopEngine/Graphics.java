package com.OffTheLine.desktopEngine;

import javax.swing.JFrame;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;


public class Graphics extends com.OffTheLine.common.CommonGraphics {

    JFrame _window;

    public Graphics(String title) {

        _window = new JFrame(title);

        //así se llama al fixaspectratio solo cuando se cambia la ventana de tamaño
        _window.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                fixAspectRatio(getTrueWidth(), getTrueHeight());
            }
        });
    }

    java.awt.image.BufferStrategy _strategy;

    Color _bgColor = Color.BLACK;
    Graphics2D _graphics;

    ArrayDeque<AffineTransform> _savedTransform;

    public boolean init(int width, int height, String assetsPath, Engine engine) {

        //sumo a height para que se vea toda la pantalla. un poco sucio pero bueno
        super.init(width, height + 100, assetsPath, engine);

        _savedTransform = new ArrayDeque<AffineTransform>();

        _window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _window.setSize(width, height);

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

    public void updateDrawGraphics() { _graphics = (Graphics2D)_strategy.getDrawGraphics(); }

    public void dispose()
    {
        restoreAll();
        _graphics.dispose();
        _graphics = null;
    }

    @Override
    public void render()
    {
        //CLEAR SIEMPRE ANTES DE TRANSLATE
        clear(_bgColor);

        translate(_xOffset, _yOffset);
        //System.out.println("xOffset: " + _xOffset + ", yOffset: " + _yOffset);

        scale(_scaleW, _scaleH);
        //System.out.println("xScale: " + _scaleW + ", yScale: " + _scaleH);

        _graphics.setStroke(new BasicStroke(2));
    }

    @Override
    public Font newFont(String path, int size, boolean isBold) throws Exception {
        InputStream is = _engine.openInputStream(path);
        Font ret = new Font(is, size, isBold);
        return ret;
    }


    @Override
    public void clear(int color) {
        setColor(color);
        fillRect(0, 0, getTrueWidth(), getTrueHeight());
    }

    void clear(Color color) {
        clear(color.getRGB());
    }


    @Override
    public void translate(float x, float y) {
        _graphics.translate((int)x, (int)y);
    }


    @Override
    public void scale(float x, float y) {
        _graphics.scale(x, y);
    }


    @Override
    public void rotate(float angle) {
        _graphics.rotate(Math.toRadians(angle));
    }



    @Override
    public void save() {
        _savedTransform.push(_graphics.getTransform());
    }


    @Override
    public void restore() {
        if(_savedTransform.size() > 0)
        {
            AffineTransform a = _savedTransform.removeLast();
            _graphics.setTransform(a);
        }
        else
            System.out.println("Empty!");
    }

    @Override
    public void restoreAll() {
        while(!_savedTransform.isEmpty()) {
            AffineTransform a = _savedTransform.removeLast();
            _graphics.setTransform(a);
        }
    }


    @Override
    public void setColor(int color) {
        _graphics.setColor(new Color(color));
    }

    void setColor(Color color) {
        setColor(color.getRGB());
    }


    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        _graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }


    @Override
    public void fillRect(float x1, float y1, float x2, float y2) {
        _graphics.fillRect((int)x1, (int)y1, (int)x2 - (int)x1, (int)y2 - (int)y1);
    }

    @Override
    public void setFont(com.OffTheLine.common.Font font)
    {
        Font f = (Font)font;
        _graphics.setFont(f.getFont());
    }

    @Override
    public void drawText(String text, float x, float y)
    {
        _graphics.drawString(text, (int) x, (int) y); //me obliga a castear a int, si no, llora
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

    //public JFrame getWindow() { return _window; }

    public void addMouseListener(MouseListener listener)
    {
        _window.addMouseListener(listener);
    }

    public void addMouseMotionListener(MouseMotionListener listener)
    {
        _window.addMouseMotionListener(listener);
    }
}
