package com.OffTheLine.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class CommonGraphics implements Graphics {

    protected int _initWidth = 0;
    protected int _initHeight = 0;
    protected Engine _engine;
    protected String _assetsPath;

    protected int _xOffset = 0;
    protected int _yOffset = 0;
    protected int _logicWidth = 0;
    protected int _logicHeight = 0;
    protected float _scaleW = 1.0f;
    protected float _scaleH = 1.0f;

    public void init(int width, int height, String assetsPath, Engine engine) {

        _initWidth = width;
        _initHeight = height;

        _logicWidth = width;
        _logicHeight = height;

        _engine = engine;

        _assetsPath = assetsPath;
    }

    protected void fixAspectRatio(int trueW, int trueH) {

        double tryW = ((trueH * _initWidth) / (double)_initHeight);

        double tryH = ((trueW * _initHeight) / (double)_initWidth);

        if(tryW > trueW) {
            _xOffset = 0;
            _logicWidth = trueW;

            double newH = ((tryW * _initHeight) / (double)_initWidth);
            _yOffset = (trueH - (int)tryH) / 2;
            _logicHeight = (int)tryH;
        }
        else {
            _yOffset = 0;
            _logicHeight = trueH;

            _xOffset = (trueW - (int)tryW) / 2;
            _logicWidth = (int)tryW;
        }

        _scaleW = _logicWidth / (float)_initWidth;
        _scaleH = _logicHeight / (float)_initHeight;
    }

    @Override
    public int getWidth() {
        return _initWidth;
    }

    @Override
    public int getHeight() {
        return _initHeight;
    }

}
