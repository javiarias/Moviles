package com.OffTheLine.appEngine;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

import com.OffTheLine.common.GameObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Graphics extends com.OffTheLine.common.CommonGraphics {

    public Graphics(Context context, AssetManager manager) {
        _surface = new SurfaceView(context);
        _manager = manager;
    }

    Font _font = null;

    int _bgColor = Color.BLACK;
    Canvas _canvas;
    SurfaceView _surface;
    AssetManager _manager;
    Paint _paint;

    int _savedTransform = 1;

    public boolean init(int width, int height, String assetsPath, Engine engine) {

        super.init(width, height, assetsPath, engine);

        _surface.getHolder().setFixedSize(width, height);

        //_savedTransform = new LinkedList<>();

        try
        {
            _font = newFont(_assetsPath + "Bangers-Regular.ttf", 40, false);
        }
        catch(Exception e)
        {
            return false;
        }

        _paint = new Paint();

        return true;
    }

    public SurfaceView getSurface() {
        return _surface;
    }

    public void present() {
        _surface.getHolder().unlockCanvasAndPost(_canvas);
    }

    public void updateCanvas() {
        while(!_surface.getHolder().getSurface().isValid())
        ;

        _canvas = _surface.getHolder().lockCanvas();
    }

    @Override
    public void render(ArrayList<GameObject> objects){



        //con tal de que al menos el contenido se vea dentro de la ventana,
        //trasladamos lo suficiente para que (0, 0) se halle dentro de la ventana visible

        //fixAspectRatio(getTrueWidth(), getTrueHeight());

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
            drawString(_font,"no voy a quitar la\n Lupa, ignacio", 0, 0);

            save();

            translate(-getWidth() / 2, 0);
            rotate(45);
            setColor(Color.BLUE);
            drawString(_font,"arriba", 0, 0);

            //restoreAll();
            restore();
            setColor(Color.MAGENTA);
            drawString(_font,"abajo", 0, 0);
        }

        //setColor(Color.RED);
        //drawLine(0, 0, getWidth(), getHeight());

        translate(-_xOffset, -_yOffset);
    }

    public void drawString(Font font, String text, int x, int y){
        _paint.setTypeface(_font.getFont());
        _paint.setFakeBoldText(_font.isBold());
        _paint.setTextSize(_font.getSize());

        _canvas.drawText(text, x, y, _paint);
    }

    @Override
    public Font newFont(String path, int size, boolean isBold) throws Exception {
        Font ret = new Font(_manager, path, size, isBold);
        return ret;
    }


    @Override
    public void clear(int color) {
        setColor(color);
        fillRect(0, 0, getTrueWidth(), getTrueHeight());
    }


    @Override
    public void translate(float x, float y) {
        _canvas.translate((int)x, (int)y);
    }


    @Override
    public void scale(float x, float y) {
        _canvas.scale(x, y);
    }


    @Override
    public void rotate(float angle) {
        _canvas.rotate(angle);
    }



    @Override
    public void save() {
        _savedTransform = _canvas.save();
    }


    @Override
    public void restore() {
        if(_savedTransform > 1) {
            _canvas.restore();
            _savedTransform--;
        }
    }

    @Override
    public void restoreAll() {
        _canvas.restoreToCount(1);
        _savedTransform = 1;
    }


    @Override
    public void setColor(int color) {
        _paint.setColor(color);
    }


    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        _paint.setStrokeWidth(1);
        _canvas.drawLine(x1, y1, x2, y2, _paint);
    }



    @Override
    public void fillRect(float x1, float y1, float x2, float y2) {
        _paint.setStrokeWidth(1);
        _canvas.drawRect(x1, y1, x2, y2, _paint);
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
        return _surface.getWidth();
    }

    public int getTrueHeight() {
        return _surface.getWidth();
    }

    @Override
    public void release(){

    }

}
