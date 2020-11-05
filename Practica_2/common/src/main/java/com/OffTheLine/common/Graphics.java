package com.OffTheLine.common;

import java.awt.Color;
import java.io.File;

public interface Graphics {
    Font newFont(File filename, int size, boolean isBold);

    void clear(Color color);

    void translate(float x, float y);
    void scale(float x, float y);
    void rotate(float angle);

    void save();
    void restore();

    void setColor(Color color);

    void drawLine(float x1, float y1, float x2, float y2);

    void fillRect(float x1, float y1, float x2, float y2);

    void drawText(String text, float x, float y);

    int getWidth();
    int getHeight();
}