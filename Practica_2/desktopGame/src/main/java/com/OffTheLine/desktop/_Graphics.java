package com.OffTheLine.desktop;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Font;

import java.awt.Color;
//import java.awt.Font;
import java.io.File;

public class _Graphics implements com.OffTheLine.common.Graphics {

    _Graphics() {

    }

    public Font newFont(File filename, int size, boolean isBold) { return null; } /*throws Exception {
        Font ret = Font.createFont(Font.TRUETYPE_FONT, filename);

        return null;
    };
    */

    public void clear(Color color) {

    }

    public void translate(float x, float y) {

    }

    public void scale(float x, float y) {

    }

    public void rotate(float angle) {

    }


    public void save() {

    }

    public void restore() {

    }


    public void setColor(Color color) {

    }


    public void drawLine(float x1, float y1, float x2, float y2) {

    }


    public void fillRect(float x1, float y1, float x2, float y2) {

    }


    public void drawText(String text, float x, float y) {

    }


    public int getWidth() {

        return 0;
    }

    public int getHeight() {

        return 0;
    }

}
