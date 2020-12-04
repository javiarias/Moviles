package com.OffTheLine.common;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public interface Graphics {
    Font newFont(String path, int size, boolean isBold) throws Exception;

    void clear(int color);

    void translate(float x, float y);
    void scale(float x, float y);
    void rotate(float angle);

    void save();
    void restore();
    void restoreAll();

    void setColor(int color);

    void setFont(Font font);

    void drawLine(float x1, float y1, float x2, float y2);

    void fillRect(float x1, float y1, float x2, float y2);

    void drawText(String text, float x, float y);

    int getWidth();
    int getHeight();
    void setWidth(int w);
    void setHeight(int h);

    void render();

    void release();
}
