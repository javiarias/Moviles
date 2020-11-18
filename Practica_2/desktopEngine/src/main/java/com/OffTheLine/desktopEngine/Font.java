package com.OffTheLine.desktopEngine;

import java.io.FileInputStream;
import java.io.InputStream;

public class Font implements com.OffTheLine.common.Font
{
    public Font(InputStream is, float size, boolean isBold) throws Exception
    {
        java.awt.Font baseFont;

        try {
            baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
        }
        catch(Exception e){
            System.err.println("Error cargando la fuente: " + e);
            return;
        }

        // baseFont contiene el tipo de letra base en tamaño 1. La
        // usamos como punto de partida para crear la nuestra, más
        // grande y en negrita.
        if(isBold)
            _font = baseFont.deriveFont(java.awt.Font.BOLD, size);
        else
            _font = baseFont.deriveFont(size);
    }

    public java.awt.Font getFont() { return _font; }

    java.awt.Font _font;
}
