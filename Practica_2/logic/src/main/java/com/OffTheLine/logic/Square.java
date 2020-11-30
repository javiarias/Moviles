package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;

public class Square extends GameObject {

    //THIS IS FOR RENDERING
    protected float _size = 8; //pixels
    protected float _rotSpeed = 180; //per second!!
    protected float _rotAngle = 0;

    protected int _color;

    //Constructora
    Square(float posX, float posY, int color)
    {
        super(posX, posY); //Constructora de gameObject
        _color = color;
    }

    //Constructora
    Square(Vector2D pos_, int color)
    {
        super(pos_); //Constructora de gameObject
        _color = color;
    }

    //SAVE & RESTORE DONE OUTSIDE
    @Override
    public void render(Graphics g) {
        g.translate(pos.x, pos.y);
        g.rotate(_rotAngle);

        g.setColor(_color);

        g.drawLine(_scale * -_size / 2.0f, _scale * _size / 2.0f, _scale * _size / 2.0f, _scale * _size / 2.0f);
        g.drawLine(_scale * _size / 2.0f, _scale * _size / 2.0f, _scale * _size / 2.0f, _scale * -_size / 2.0f);
        g.drawLine(_scale * _size / 2.0f, _scale * -_size / 2.0f, _scale * -_size / 2.0f, _scale * -_size / 2.0f);
        g.drawLine(_scale * -_size / 2.0f, _scale * -_size / 2.0f, _scale * -_size / 2.0f, _scale * _size / 2.0f);
    }

    @Override
    public void update(double delta, ArrayList<Input.TouchEvent> inputList) {

    }

    @Override
    public void lateUpdate(double delta) {

    }
}
