package com.OffTheLine.appEngine;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

public class Graphics extends com.OffTheLine.common.CommonGraphics {

    /*Variables*/
    int _bgColor = Color.BLACK;
    Canvas _canvas;
    SurfaceView _surface;
    AssetManager _manager;
    Paint _paint;
    int _savedTransform = 1;

    /*Funciones*/

    /*Getters*/
    //Para obtener la superficien
    public SurfaceView getSurface() { return _surface; }

    //Para obtener la anchura real
    public int getTrueWidth() { return _surface.getWidth(); }

    //Para obtener la altura real
    public int getTrueHeight() { return _surface.getHeight(); }

    /*Setters*/
    //Añadir listener para temas de input
    public void setTouchListener(TouchListener listener) { _surface.setOnTouchListener(listener); }

    //Elegir fuente a la hora de renderizar texto
    @Override public void setFont(com.OffTheLine.common.Font font)
    {
        Font f = (Font)font;
        _paint.setTypeface(f.getFont());
        _paint.setFakeBoldText(f.isBold());
        _paint.setTextSize(f.getSize());
    }

    //Elegir colo a la hora de renderizar elementos
    @Override public void setColor(int color) { _paint.setColor(color); }

    //Constructora de Graphics para Android
    public Graphics(Context context, AssetManager manager)
    {
        _surface = new SurfaceView(context);
        _manager = manager;
    }

    //Para corregir posibles deformaciones por distintos tamaños de pantalla
    public void fixAspectRatio() { fixAspectRatio(getTrueWidth(), getTrueHeight()); }

    //Iniciar el sistema de gráficos
    public boolean init(int width, int height, String assetsPath, Engine engine)
    {
        super.init(width, height, assetsPath, engine);
        _paint = new Paint();
        return true;
    }

    public void present() { _surface.getHolder().unlockCanvasAndPost(_canvas); }

    public void updateCanvas()
    {
        while(!_surface.getHolder().getSurface().isValid())
        ;
        _canvas = _surface.getHolder().lockCanvas();
    }

    @Override public void render()
    {
        clear(_bgColor); //CLEAR SIEMPRE ANTES DE TRANSLATE
        translate(_xOffset, _yOffset);
        scale(_scaleW, _scaleH);
    }

    //public void drawString(Font font, String text, int x, int y) { _canvas.drawText(text, x, y, _paint); }

    //Crear fuente nueva
    @Override public Font newFont(String path, int size, boolean isBold) throws Exception {
        Font ret = new Font(_manager, path, size, isBold);
        return ret;
    }

    //Limpieza
    @Override public void clear(int color) {
        setColor(color);
        fillRect(0, 0, getTrueWidth(), getTrueHeight());
    }

    //Para colocar en posición x,y
    @Override public void translate(float x, float y) { _canvas.translate((int)x, (int)y); }

    //Para escalar en tamaño x,y
    @Override public void scale(float x, float y) { _canvas.scale(x, y); }

    //Para rotar
    @Override public void rotate(float angle) { _canvas.rotate(angle); }

    //Save
    @Override public void save() { _savedTransform = _canvas.save(); }

    //Restore
    @Override public void restore() {
        if(_savedTransform > 1) {
            _canvas.restore();
            _savedTransform--;
        }
    }

    //Restore en caso de varios
    @Override public void restoreAll() {
        _canvas.restoreToCount(1);
        _savedTransform = 1;
    }

    //Para pintar una linea (desde x1,y1 hasta x2,y2)
    @Override public void drawLine(float x1, float y1, float x2, float y2)
    {
        _paint.setStrokeWidth(1);
        _canvas.drawLine(x1, y1, x2, y2, _paint);
    }

    //Para rellenar una recuadro (desde x1,y1 hasta x2,y2)
    @Override public void fillRect(float x1, float y1, float x2, float y2)
    {
        _paint.setStrokeWidth(1);
        _canvas.drawRect(x1, y1, x2, y2, _paint);
    }

    //Para pintar texto
    @Override public void drawText(String text, float x, float y) { _canvas.drawText(text, x, y, _paint); }

    //Release, vacío a propósito (se llama desde otro lado, por eso tiene que existir)
    @Override public void release() { }
}
