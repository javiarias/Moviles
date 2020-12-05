package com.OffTheLine.logic;

import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Button extends Square {

    String _text;
    float _height;
    float _width;
    Font _font;

    //Constructora
    Button(float posX, float posY, String text, float width, float height, Font font, float scale)
    {
        super(posX, posY, 0xFF000000);
        _text = text;
        _height = height * scale;
        _width = width * scale;
        _font = font;
        _scale = scale;
    }

    @Override
    public void render(Graphics g)
    {
        g.translate(pos.x, pos.y);
        g.setFont(_font);
        g.setColor(0xFFFFFFFF);

        //para poder renderizar la bounding box del b0tón, en caso de querer debuggearlo
/*
        g.drawLine(0, _height, _width, _height);
        g.drawLine(_width, _height, _width, 0);
        g.drawLine(_width, 0, 0, 0);
        g.drawLine(0, 0, 0, _height);

 */

        g.scale(_scale, _scale);
        g.drawText(_text, _scale * 2, _height * 0.75f / _scale);
    }

    public boolean clicked(float x, float y)
    {
        if ( pos.x < x && x < pos.x + _width && pos.y < y && y < pos.y + _height)
            return true;
        else
            return false;
    }
}