package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Vector2D;

public class Cross extends Square
{
    //Constructora
    Cross(float posX, float posY, int color)
    {
        super(posX, posY, color);
    }
    //Constructora
    Cross(Vector2D pos, int color)
    {
        super(pos, color);
    }
    //Constructora
    Cross(Square s, int color)
    {
        super(s.pos, color);
        _size = s._size;
    }

    @Override
    public void render(Graphics g)
    {
        g.translate(pos.x, pos.y);
        g.rotate(_rotAngle);

        g.setColor(_color);

        g.drawLine(-_size/2, -_size/2, _size/2, _size/2);
        g.drawLine(-_size/2, _size/2, _size/2, -_size/2);
    }
}
