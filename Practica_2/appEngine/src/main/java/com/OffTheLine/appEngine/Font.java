package com.OffTheLine.appEngine;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Font implements com.OffTheLine.common.Font {

    /*Variables*/
    Typeface _font;
    int _size;
    boolean _isBold;

    /*Funciones*/

    //Constructora
    public Font(AssetManager manager, String path, int size, boolean isBold) throws Exception
    {
        _font = Typeface.createFromAsset(manager, path);
        _size = size;
        _isBold = isBold;
    }

    /*Getters*/
    public Typeface getFont() { return _font; }
    public int getSize() { return _size; }

    //¿Es negrita?
    public boolean isBold() { return _isBold; }
}
