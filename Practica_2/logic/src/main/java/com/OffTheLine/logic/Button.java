package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;

public class Button extends Square {

    String _text;
    float _height;
    float _width;

    //Constructora
    Button(float posX, float posY, String text, float width, float height)
    {
        super(posX, posY, 0xFF000000);
        _text = text;
        _height = height;
        _width = width;
    }

    @Override
    public void render(Graphics g)
    {
        g.translate(pos.x, pos.y);

        g.drawText(_text, pos.x, pos.y + _height);

        g.drawLine(pos.x, pos.y, pos.x + _width, pos.y);
        g.drawLine(pos.x, pos.y, pos.x, pos.y + _height);
        g.drawLine(pos.x, pos.y + _height, pos.x + _width, pos.y + _height);
        g.drawLine(pos.x + _width, pos.y, pos.x + _width, pos.y + _height);
    }

    public boolean clicked(float x, float y)
    {
        if ( pos.x < x && x < pos.x + _width && pos.y < y && y < pos.y + _height)
            return true;
        else
            return false;
    }
}