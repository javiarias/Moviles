package com.OffTheLine.desktopEngine;

import com.OffTheLine.common.GameObject;

import javax.swing.JFrame;
import java.io.File;

import java.awt.Color;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;


public class Graphics extends JFrame implements com.OffTheLine.common.Graphics {

    public Graphics(String title) {
        super(title);
    }

    Font _font;
    java.awt.image.BufferStrategy _strategy;

    int _width = 0;
    int _height = 0;

    Color _color;
    java.awt.Graphics _graphics;

    public boolean init(int width, int height, String assetsPath) {

        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font baseFont;
        try (InputStream is = new FileInputStream(assetsPath + "Bangers-Regular.ttf")) {
            _font = newFont(is, 40, true, false);
        }
        catch (Exception e) {
            // Ouch. No está.
            System.err.println("Error cargando la fuente: " + e);
            return false;
        }

        // Vamos a usar renderizado activo. No queremos que Swing llame al
        // método repaint() porque el repintado es continuo en cualquier caso.
        setIgnoreRepaint(true);

        // Hacemos visible la ventana.
        setVisible(true);

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                createBufferStrategy(2);
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
        _strategy = getBufferStrategy();
        _width = width;
        _height = height;

        _color = Color.BLUE;

        return true;
    }

    @Override
    public void render(ArrayList<GameObject> g){
        // Pintamos el frame con el BufferStrategy
        do {
            do {
                _graphics = _strategy.getDrawGraphics();
                try {
                    for(GameObject o : g) {
                        o.render(this);

                        //y ahora cómo coño renderizo yo esto
                        //porque tendríamos que meternos en el GameObject, y dentro tener un render que llame de nuevo a esto y suena bastante mal pero no se?
                    }
                }
                finally {
                    _graphics.dispose();
                }
            } while(_strategy.contentsRestored());
            _strategy.show();
        } while(_strategy.contentsLost());
    }

    @Override
    public Font newFont(InputStream filename, int size, boolean isBold, boolean isItalic) throws Exception {
        Font ret = (Font) Font.createFont(Font.TRUETYPE_FONT, filename);

        int s = Font.PLAIN;
        if (isBold)
            s = s | Font.BOLD;
        if(isItalic)
            s = s | Font.ITALIC;

        ret = (Font) ret.deriveFont(s, size);

        return ret;
    }


    @Override
    public void clear(Color color) {
        setColor(color);
        fillRect(0, 0, getWidth(), getHeight());
    }


    @Override
    public void translate(float x, float y) {

    }


    @Override
    public void scale(float x, float y) {

    }


    @Override
    public void rotate(float angle) {

    }



    @Override
    public void save() {

    }


    @Override
    public void restore() {

    }



    @Override
    public void setColor(Color color) {
        _color = color;
    }



    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {

    }



    @Override
    public void fillRect(float x1, float y1, float x2, float y2) {

    }



    @Override
    public void drawText(String text, float x, float y) {

    }



    @Override
    public int getWidth() {

        return _width;
    }


    @Override
    public int getHeight() {

        return _height;
    }

}
