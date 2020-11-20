package com.OffTheLine.logic;

public abstract class GameObject implements com.OffTheLine.common.GameObject {

    protected float _X;
    protected float _Y;
    protected float _scale;

    //Getters
    public float getX()
    {
       return  _X;
    }

    public float getY()
    {
        return  _Y;
    }

    public float getScale()
    {
        return  _scale;
    }

    //Setters

    public void setX(float X)
    {
        this._X = X;
    }

    public void setY(float Y)
    {
        this._Y = Y;
    }

    public void setScale(float scale)
    {
        this._scale = scale;
    }

    //Constructora
    GameObject(float posX, float posY)
    {
        this._X = posX;
        this._Y = posY;
    }
}
