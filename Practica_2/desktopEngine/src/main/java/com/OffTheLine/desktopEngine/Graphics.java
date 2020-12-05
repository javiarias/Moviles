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

public class Graphics extends com.OffTheLine.common.CommonGraphics {

    /*Variables*/
    JFrame _window;
    java.awt.image.BufferStrategy _strategy;
    Color _bgColor = Color.BLACK;
    Graphics2D _graphics;
    ArrayDeque<AffineTransform> _savedTransform;

    /*Funciones*/

    /*Getters*/

    //Obtener la anchura real
    public int getTrueWidth() { return _window.getWidth(); }

    //Obtener la altura real
    public int getTrueHeight() { return _window.getHeight(); }

    java.awt.Graphics getDrawGraphics() { return _strategy.getDrawGraphics(); } //No se usa, pero la hemos dejado por si acaso

    /*Setters*/

    //Para elegir color a la hora de pintar
    @Override public void setColor(int color) { _graphics.setColor(new Color(color)); }

    //Para elegir fuente
    @Override public void setFont(com.OffTheLine.common.Font font)
    {
        Font f = (Font)font;
        _graphics.setFont(f.getFont());
    }

    void setColor(Color color) { setColor(color.getRGB()); } //No se usa, pero la hemos dejado por si acaso

    //Constructora
    public Graphics(String title)
    {
        _window = new JFrame(title);

        //así se llama al fixAspectRatio solo cuando se cambia la ventana de tamaño
        _window.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt)
            {
                Component c = (Component)evt.getSource();
                fixAspectRatio(getTrueWidth(), getTrueHeight());
            }
        });
    }

    //Inicialización
    public boolean init(int width, int height, String assetsPath, Engine engine)
    {
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

        return true;
    }

    //Para comprobación
    public boolean contentsRestored() { return  _strategy.contentsRestored(); }

    //Para comprobación
    public boolean contentsLost() { return _strategy.contentsLost(); }

    //Present
    public void present() { _strategy.show(); }

    //Actualizar graficos
    public void updateDrawGraphics() { _graphics = (Graphics2D)_strategy.getDrawGraphics(); }

    //Para liberar graphics
    public void dispose()
    {
        restoreAll();
        _graphics.dispose();
        _graphics = null;
    }

    //Render
    @Override public void render()
    {
        clear(_bgColor); //CLEAR SIEMPRE ANTES DE TRANSLATE
        translate(_xOffset, _yOffset);
        scale(_scaleW, _scaleH);
        _graphics.setStroke(new BasicStroke(2));
    }

    //Para crear fuente
    @Override public Font newFont(String path, int size, boolean isBold) throws Exception
    {
        InputStream is = _engine.openInputStream(path);
        Font ret = new Font(is, size, isBold);
        return ret;
    }

    //Limpieza
    @Override public void clear(int color)
    {
        setColor(color);
        fillRect(0, 0, getTrueWidth(), getTrueHeight());
    }

    void clear(Color color) { clear(color.getRGB()); }

    //Para colocar en posicion x,y
    @Override public void translate(float x, float y) { _graphics.translate((int)x, (int)y); }

    //Para escalar en tamaño x,y
    @Override public void scale(float x, float y) { _graphics.scale(x, y); }

    //Para rotar
    @Override public void rotate(float angle) { _graphics.rotate(Math.toRadians(angle)); }

    //Save
    @Override public void save() { _savedTransform.push(_graphics.getTransform()); }

    //Restore
    @Override public void restore()
    {
        if(_savedTransform.size() > 0)
        {
            AffineTransform a = _savedTransform.removeLast();
            _graphics.setTransform(a);
        }
        else
            System.out.println("Empty!");
    }

    //Restore en caso de varios
    @Override public void restoreAll()
    {
        while(!_savedTransform.isEmpty())
        {
            AffineTransform a = _savedTransform.removeLast();
            _graphics.setTransform(a);
        }
    }

    //Para pintar una linea (desde x1,y1 hasta x2,y2)
    @Override public void drawLine(float x1, float y1, float x2, float y2) { _graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2); }

    //Para rellenar una recuadro (desde x1,y1 hasta x2,y2)
    @Override public void fillRect(float x1, float y1, float x2, float y2) { _graphics.fillRect((int)x1, (int)y1, (int)x2 - (int)x1, (int)y2 - (int)y1); }

    //Para pintar texto
    @Override public void drawText(String text, float x, float y) { _graphics.drawString(text, (int) x, (int) y); } //Obliga a castear a int

    //Para liberar el strategy
    @Override public void release() { _strategy.dispose(); }

    /*Para añadir Listeners*/
    public void addMouseListener(MouseListener listener) { _window.addMouseListener(listener); }
    public void addMouseMotionListener(MouseMotionListener listener) { _window.addMouseMotionListener(listener); }
}
