package com.OffTheLine.appEngine;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

import com.OffTheLine.common.GameObject;

import java.util.ArrayList;

public class Graphics extends com.OffTheLine.common.CommonGraphics {

    public Graphics(Context context, AssetManager manager) {
        _surface = new SurfaceView(context);
        _manager = manager;
    }

    public void setTouchListener(TouchListener listener) {
        _surface.setOnTouchListener(listener);
    }

    int _bgColor = Color.BLACK;
    Canvas _canvas;
    SurfaceView _surface;
    AssetManager _manager;
    Paint _paint;

    int _savedTransform = 1;

    public void fixAspectRatio(){

        fixAspectRatio(getTrueWidth(), getTrueHeight());

    }

    public boolean init(int width, int height, String assetsPath, Engine engine) {

        super.init(width, height, assetsPath, engine);

        //_surface.getHolder().setFixedSize(width, height);

        //_savedTransform = new LinkedList<>();

        _paint = new Paint();

        return true;
    }

    public SurfaceView getSurface() {
        return _surface;
    }

    public void present() {
        _surface.getHolder().unlockCanvasAndPost(_canvas);
    }

    public void updateCanvas()
    {
        while(!_surface.getHolder().getSurface().isValid())
        ;

        _canvas = _surface.getHolder().lockCanvas();
    }

    @Override
    public void render(){

        //CLEAR SIEMPRE ANTES DE TRANSLATE
        clear(_bgColor);

        translate(_xOffset, _yOffset);
        //System.out.println("xOffset: " + _xOffset + ", yOffset: " + _yOffset);

        scale(_scaleW, _scaleH);
        //System.out.println("xScale: " + _scaleW + ", yScale: " + _scaleH);
    }

    public void drawString(Font font, String text, int x, int y)
    {
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
    public void setFont(com.OffTheLine.common.Font font)
    {
        Font f = (Font)font;

        _paint.setTypeface(f.getFont());
        _paint.setFakeBoldText(f.isBold());
        _paint.setTextSize(f.getSize());
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
    public void drawText(String text, float x, float y)
    {
        _canvas.drawText(text, x, y, _paint);
    }

    public int getTrueWidth() {
        return _surface.getWidth();
    }

    public int getTrueHeight() {
        return _surface.getHeight();
    }

    @Override
    public void release(){

    }

}
