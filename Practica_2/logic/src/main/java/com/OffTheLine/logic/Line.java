package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;

public class Line extends GameObject {

    /*Variables*/
    float _rotAngle;
    float _rotSpeed = 50;
    int _color;
    Vector2D _dir;

    /*Funciones*/

    Line(float posX, float posY, int color, Vector2D dir, float angle, boolean angleDir)
    {
        super(posX, posY);
        _color = color;
        _dir = dir;
        _rotAngle = angle;

        if(!angleDir)
            _rotSpeed *= -1;
    }

    //Constructora
    Line(Vector2D pos_, int color, Vector2D dir, float angle, boolean angleDir) {
        super(pos_);
        _color = color;
        _dir = dir;
        _rotAngle = angle;

        if(!angleDir)
            _rotSpeed *= -1;

    }

    //Update
    @Override public void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        _rotAngle += (_rotSpeed * delta);
        pos = pos.add(_dir.multiply((float)delta));
    }

    @Override public void lateUpdate(double delta) { }

    //Render, Save y restore hechos fuera
    @Override public void render(Graphics g)
    {
        g.translate(pos.x, pos.y);
        g.rotate(_rotAngle);
        g.setColor(_color);
        g.drawLine(-3, 0, 3, 0);
    }
}
