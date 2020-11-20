package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;

public class Item extends GameObject{

    //No todos lo tienen
    protected float radius;
    protected float speed;
    protected float angle;

    //Getters
    public float getRadius()
    {
        return radius;
    }

    public float getSpeed()
    {
        return speed;
    }

    public float getAngle()
    {
        return angle;
    }

    //Setters
    public void setRadius(float radius_)
    {
        radius = radius_;
    }
    public void setAngle(float angle_)
    {
        angle = angle_;
    }
    public void setSpeed (float speed_)
    {
        speed = speed_;
    }

    //Constructora
    Item(float posX, float posY)
    {
        super(posX, posY); //Constructora de gameObject
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void lateUpdate(double delta) {

    }

}
