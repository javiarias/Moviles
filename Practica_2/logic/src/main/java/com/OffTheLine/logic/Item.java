package com.OffTheLine.logic;

import com.OffTheLine.common.Vector2D;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class Item extends Square{

    //THIS IS FOR MOVEMENT!!!!!
    protected float _radius = 0;
    protected float _speed = 0;
    protected float _angle = 0;

    //Getters
    public float getRadius()
    {
        return _radius;
    }
    public float getSpeed()
    {
        return _speed;
    }
    public float getAngle()
    {
        return _angle;
    }

    //Setters
    public void setRadius(float radius_)
    {
        _radius = radius_;
    }
    public void setAngle(float angle_)
    {
        _angle = angle_;
    }
    public void setSpeed(float speed_)
    {
        _speed = speed_;
    }

    //Constructora
    Item(float posX, float posY)
    {
        super(posX, posY * -1,0xFFFFFF00); //Constructora de gameObject
    }

    //Constructora
    Item(Vector2D pos_)
    {
        super(pos_, 0xFFFFFF00); //Constructora de gameObject
    }

    @Override
    public void update(double delta) {
        if(_radius != 0)
        {
            pos.x = _radius * (float)cos(toRadians(_angle));
            pos.y = _radius * (float)sin(toRadians(_angle));

            _angle += _speed * delta;
            _angle = (_angle % 360);
        }

        _rotAngle += _rotSpeed * delta;
        _rotAngle = (_rotAngle % 360);
    }

    @Override
    public void lateUpdate(double delta) {

    }

}
