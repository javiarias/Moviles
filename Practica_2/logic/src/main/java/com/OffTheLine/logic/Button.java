package com.OffTheLine.logic;

import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;

public class Button extends Square {

    String _text;
    float _height;
    float _width;
    Font _font;

    //Constructora
    Button(float posX, float posY, String text, float width, float height, Font font)
    {
        super(posX, posY, 0xFF000000);
        _text = text;
        _height = height;
        _width = width;
        _font = font;
    }

    @Override
    public void render(Graphics g)
    {
        g.translate(pos.x, pos.y);
        g.setFont(_font);
        g.setColor(0xFFFFFFFF);

        g.drawText(_text, -_width * 0.4f, _height * 0.2f);

        /*
        g.drawLine(-_width / 2.0f, _height / 2.0f, _width / 2.0f, _height / 2.0f);
        g.drawLine(_width / 2.0f, _height / 2.0f, _width / 2.0f, -_height / 2.0f);
        g.drawLine(_width / 2.0f, -_height / 2.0f, -_width / 2.0f, -_height / 2.0f);
        g.drawLine(-_width / 2.0f, -_height / 2.0f, -_width / 2.0f, _height / 2.0f);

         */
    }

    public boolean clicked(float x, float y)
    {
        if ( pos.x - _width / 2.0f < x && x < pos.x + _width / 2.0f && pos.y - _height / 2.0f < y && y < pos.y + _height / 2.0f)
            return true;
        else
            return false;
    }
}