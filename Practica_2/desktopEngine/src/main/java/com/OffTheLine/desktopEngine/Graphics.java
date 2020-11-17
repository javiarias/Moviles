package com.OffTheLine.desktopEngine;

import com.OffTheLine.common.GameObject;

import javax.swing.JFrame;
import java.io.File;

import java.awt.Color;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;


public class Graphics implements com.OffTheLine.common.Graphics {

    JFrame _window;

    public Graphics(String title) {
        _window = new JFrame(title);
    }

    Font _font;
    java.awt.image.BufferStrategy _strategy;

    Color _bgColor;
    java.awt.Graphics _graphics;

    public boolean init(int width, int height, String assetsPath) {

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
                _window.createBufferStrategy(2);
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

        _bgColor = Color.BLUE;

        return true;
    }

    public java.awt.Graphics getDrawGraphics() {
        return _strategy.getDrawGraphics();
    }

    public boolean contentsRestored() {
       return  _strategy.contentsRestored();
    }

    public boolean contentsLost(){
        return _strategy.contentsLost();
    }

    public void present(){
        _strategy.show();
    }

    @Override
    public void render(ArrayList<GameObject> objects){
        _graphics = _strategy.getDrawGraphics();

        clear(_bgColor);

        try {
            clear(Color.BLUE);
            //getGraphics().render(getLogic().getObjects());
            fillRect(0, 0, getWidth(), getHeight());

            setColor(Color.RED);
            drawLine(20, 0, 100, 100);

            // Ponemos el rótulo (si conseguimos cargar la fuente)
            if (_font != null) {
                //setColor(Color.WHITE);
                //setFont(_font);
                //drawString("RENDERIZADO ACTIVO", (int)_x, 100);
            }
        }
        finally {
            _graphics.dispose();
        }
    }

    @Override
    public Font newFont(InputStream filename, int size, boolean isBold, boolean isItalic) throws Exception {
        return null;

        /*
        Font ret = new Font(Font.createFont(Font.TRUETYPE_FONT, filename));

        int s = Font.PLAIN;
        if (isBold)
            s = s | Font.BOLD;
        if(isItalic)
            s = s | Font.ITALIC;

        ret = (Font) ret.deriveFont(s, size);

        return ret;
        */
    }


    @Override
    public void clear(Color color) {
        setColor(color);
        fillRect(0, 0, getWidth(), getHeight());
    }


    @Override
    public void translate(float x, float y) {
        _graphics.translate((int)x, (int)y);
    }


    @Override
    public void scale(float x, float y) {
        //_graphics.scale((int)x, (int)y);
    }


    @Override
    public void rotate(float angle) {
        //_graphics.rotate((int)angle);
    }



    @Override
    public void save() {

    }


    @Override
    public void restore() {

    }



    @Override
    public void setColor(Color color) {
        //_color = color;
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

        return _window.getWidth();
    }


    @Override
    public int getHeight() {

        return _window.getHeight();
    }

}
