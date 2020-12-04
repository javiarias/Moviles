package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class Enemy extends GameObject {

    protected float _angle;
    protected float _length;

    //No todos lo tienen
    protected float _rotSpeed = 0;
    protected Vector2D _offset;
    protected Vector2D _originalPos;
    protected float _travelTime = -1;
    protected float _waitTime = 0;

    protected Vector2D _travelSpeed = null;

    protected Vector2D _vertice1;
    protected Vector2D _vertice2;

    boolean _waiting = false;
    float _waitStart = 0;
    float _travelDir = 1;

    //Getters
    public float getAngle()
    {
        return _angle;
    }

    public float getLength()
    {
        return _length;
    }

    public float getSpeed()
    {
        return _rotSpeed;
    }

    public float getOffsetX()
    {
        return _offset.x;
    }
    public float getOffsetY()
    {
        return _offset.y;
    }

    public float getTime1()
    {
        return _travelTime;
    }

    public float getTime2()
    {
        return _waitTime;
    }

    //Setters
    public void setAngle(float angle_)
    {
        _angle = angle_;
    }

    public void setLength(float length_)
    {
        _length = length_;
    }

    public void setSpeed (float speed_)
    {
        _rotSpeed = -speed_;
    }

    public void setOffset (float x_, float y_)
    {
        _offset = new Vector2D(x_, y_ * -1);

        if(_travelTime > 0){
            calculateTravelSpeed();
        }
    }

    public void setTime1 (float time1_)
    {
        _travelTime = time1_;
        
        if(_offset != null){
            calculateTravelSpeed();
        }
    }

    public void setTime2 (float time2_)
    {
        _waitTime = time2_;
    }

    void calculateTravelSpeed()
    {
        float speedX = _offset.x / _travelTime;
        float speedY = _offset.y / _travelTime;

        _travelSpeed = new Vector2D(speedX, speedY);
        _originalPos = pos;
    }

    //Constructora
    Enemy(float posX, float posY, float angle_, float length_)
    {
        //en el json las y están al revés, y el ángulo/velocidad igual
        super(posX, posY * -1); //Constructora de gameObject
        _angle = -angle_;
        _length = length_;

        _vertice1 = new Vector2D(0, 0);
        _vertice2 = new Vector2D(0, 0);
        _offset = new Vector2D(0, 0);
    }

    @Override
    public void update(double delta, ArrayList<Input.TouchEvent> inputList) {
        _angle += _rotSpeed * delta;

        _angle = (_angle % 360);

        //se calculan los vértices directamente en vez de usar rotate, puesto que si no no sirve de nada que estén girados.
        _vertice1.x = -(_length / 2.0f) * (float)cos(toRadians(_angle));
        _vertice1.y = -(_length / 2.0f) * (float)sin(toRadians(_angle));
        _vertice2.x = (_length / 2.0f) * (float)cos(toRadians(_angle));
        _vertice2.y = (_length / 2.0f) * (float)sin(toRadians(_angle));

        if(_travelSpeed != null)
        {
            if(_waiting)
            {
                _waitStart += delta;
                _waiting = (_waitStart < _waitTime);
                if(!_waiting){
                    _travelDir *= -1;
                }
            }
            else {
                Vector2D temp = pos.add(_travelSpeed.multiply(_travelDir * (float)delta));
                Vector2D _offsetPos = _originalPos.add(_offset);

                boolean awayFromOriginal = (_originalPos.distance(_offsetPos) <= _originalPos.distance(temp));
                boolean awayFromOffset = (_offsetPos.distance(_originalPos) <= _offsetPos.distance(temp));

                if((_travelDir > 0 && awayFromOriginal) ||
                    (_travelDir < 0 && awayFromOffset))
                {
                    if(_travelDir < 0)
                        pos = _originalPos;
                    else
                        pos = _offsetPos;

                    _waiting = true;
                    _waitStart = 0;
                }
                else {
                    pos = temp;
                }
            }
        }
    }

    //SAVE & RESTORE DONE OUTSIDE
    @Override
    public void render(Graphics g)
    {
        g.setColor(0xFFFF0000);

        g.translate(pos.x, pos.y);

        g.drawLine(_vertice1.x, _vertice1.y, _vertice2.x, _vertice2.y);
    }

    @Override
    public void lateUpdate(double delta) {

    }
}
