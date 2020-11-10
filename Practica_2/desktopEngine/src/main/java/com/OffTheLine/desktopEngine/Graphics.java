package com.OffTheLine.desktopEngine;

import java.awt.Color;
import java.io.File;

public class Graphics implements com.OffTheLine.common.Graphics {

    Graphics() {

    }

    @Override
    public Font newFont(File filename, int size, boolean isBold) throws Exception {
        Font ret = (Font) Font.createFont(Font.TRUETYPE_FONT, filename);

        return ret;
    };


    @Override
    public void clear(Color color) {

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

        return 0;
    }


    @Override
    public int getHeight() {

        return 0;
    }

}
