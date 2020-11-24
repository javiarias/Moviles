package com.OffTheLine.appEngine;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.io.InputStream;

public class Font implements com.OffTheLine.common.Font {

    public Font(AssetManager manager, String path, int size, boolean isBold) throws Exception
    {
        _font = Typeface.createFromAsset(manager, path);
        _size = size;
        _isBold = isBold;
    }

    public Typeface getFont() { return _font; }
    public int getSize() { return _size; }
    public boolean isBold() { return _isBold; }

    Typeface _font;
    int _size;
    boolean _isBold;
}
