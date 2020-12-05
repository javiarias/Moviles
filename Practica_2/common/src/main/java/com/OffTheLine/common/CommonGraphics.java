package com.OffTheLine.common;

//Para cosas comunes al resto
public abstract class CommonGraphics implements Graphics {

    /*Variables*/
    protected Engine _engine;
    protected String _assetsPath;
    protected float _xOffset = 0;
    protected float _yOffset = 0;
    protected float _logicWidth = 0;
    protected float _logicHeight = 0;
    protected float _scaleW = 1.0f;
    protected float _scaleH = 1.0f;

    /*Funciones*/

    //Inicializacion
    public void init(int width, int height, String assetsPath, Engine engine)
    {
        _logicWidth = width;
        _logicHeight = height;
        _engine = engine;
        _assetsPath = assetsPath;
    }

    //Para corregir posibles deformaciones de la proporción de pantalla
    protected void fixAspectRatio(int trueW, int trueH)
    {
        float tryW = ((trueH * _logicWidth) / _logicHeight);
        float tryH = ((trueW * _logicHeight) / _logicWidth);

        float trueWidth = trueW;
        float trueHeight = trueH;

        if (tryW > trueW)
        {
            _xOffset = 0;
            _yOffset = (trueHeight - tryH) / 2.0f;
            trueHeight = tryH;
        }
        else
        {
            _yOffset = 0;
            _xOffset = (trueWidth - tryW) / 2.0f;
            trueWidth = tryW;
        }

        _scaleW = trueWidth / _logicWidth;
        _scaleH = trueHeight / _logicHeight;
    }

    /*Getters*/

    //Obtener anchura lógica
    @Override public int getWidth() { return (int)_logicWidth; }

    //Obtener altura lógica
    @Override public int getHeight() { return (int)_logicHeight; }

    //Obtener escala de la anchura
    @Override public float getWidthScale() { return _scaleW; }

    //Obtener escala de la altura
    @Override public float getHeightScale() { return _scaleH; }

    //Obtener offset en X
    @Override public float getXOffset() { return _xOffset; }

    //Obtener offset en Y
    @Override public float getYOffset() { return _yOffset; }

    /*Setters*/

    //Fijar la anchura lógica
    @Override public void setWidth(int w) { _logicWidth = w; }

    //Fijar la altura lógica
    @Override public void setHeight(int h) { _logicHeight = h; }
}
