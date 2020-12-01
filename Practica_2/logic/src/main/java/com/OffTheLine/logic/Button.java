package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;

public class Button extends Square {

    String _text;

    //Constructora
    Button(float posX, float posY, String text)
    {
        super(posX, posY, 0xFF000000);
        _text = text;
    }

    @Override
    public void render(Graphics g)
    {
        g.translate(pos.x, pos.y);

        g.drawText(_text, pos.x, pos.y);

        g.drawLine(-_size/2, -_size/2, _size/2, _size/2);
        g.drawLine(-_size/2, _size/2, _size/2, -_size/2);
    }

    public boolean clicked(float width, float height)
    {
        float x = 0; //Poner valor del input, habra que pasarlo como argumento, y sacar width y height de algun lado
        float y = 0;

        if ( -width/2 < x && x < width/2 && -height/2 < y && y < height/2)
            return true;
        else
            return false;
    }
}