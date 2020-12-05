package com.OffTheLine.logic;

import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;

public class Button extends Square {

    /*Variables*/

    String _text;
    float _height;
    float _width;
    Font _font;

    /*Funciones*/

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

    //Render
    @Override public void render(Graphics g)
    {
        g.translate(pos.x, pos.y);
        g.setFont(_font);
        g.setColor(0xFFFFFFFF);
        g.scale(_scale, _scale);
        g.drawText(_text, _scale * 2, _height * 0.75f / _scale);
    }

    //Para ver si se ha clickado el botón
    public boolean clicked(float x, float y)
    {
        if ( pos.x < x && x < pos.x + _width && pos.y < y && y < pos.y + _height)
            return true;
        else
            return false;
    }
}