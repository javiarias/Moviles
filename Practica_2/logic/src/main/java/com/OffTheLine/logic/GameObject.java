package com.OffTheLine.logic;

public abstract class GameObject implements com.OffTheLine.common.GameObject {

    protected Vector2D pos;
    protected float _scale;

    //Getters
    public float getX()
    {
       return  pos.x;
    }

    public float getY()
    {
        return  pos.y;
    }

    public float getScale()
    {
        return  _scale;
    }

    //Setters

    public void setX(float X)
    {
        pos.x = X;
    }

    public void setY(float Y)
    {
        pos.y = Y;
    }

    public void setScale(float scale)
    {
        _scale = scale;
    }

    //Constructora
    GameObject(float posX, float posY)
    {
        pos = new Vector2D(posX, posY);
    }

    //Constructora
    GameObject(Vector2D pos_)
    {
        pos = pos_;
    }
}
