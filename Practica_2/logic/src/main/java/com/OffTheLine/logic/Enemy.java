package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;

public class Enemy extends GameObject {

    protected float angle;
    protected float length;

    //No todos lo tienen
    protected float speed;
    protected float offset;
    protected float time1;
    protected float time2;

    //Getters
    public float getAngle()
    {
        return angle;
    }

    public float getLength()
    {
        return length;
    }

    public float getSpeed()
    {
        return speed;
    }

    public float getOffset()
    {
        return offset;
    }

    public float getTime1()
    {
        return time1;
    }

    public float getTime2()
    {
        return time2;
    }

    //Setters
    public void setAngle(float angle_)
    {
        angle = angle_;
    }

    public void setLength(float length_)
    {
        length = length_;
    }

    public void setSpeed (float speed_)
    {
        speed = speed_;
    }

    public void setOffset (float offset_)
    {
        offset = offset_;
    }

    public void setTime1 (float time1_)
    {
        time1 = time1_;
    }

    public void setTime2 (float time2_)
    {
        time2 = time2_;
    }

    //Constructora
    Enemy(float posX, float posY, float angle_, float length_)
    {
        super(posX, posY); //Constructora de gameObject
        this.angle = angle_;
        this.length = length_;
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
