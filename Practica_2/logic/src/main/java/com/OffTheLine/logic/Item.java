package com.OffTheLine.logic;

import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class Item extends Square{

    /*Variables*/

    //For movement
    protected float _radius = 0;
    protected float _speed = 0;
    protected float _angle = 0;

    // For death
    float pendingDeathTime = 0.5f;
    float sizeGrow = 15f;
    boolean toDie = false;

    /*Funciones*/

    //Getters

    //Aunque no se usen, estan por si acaso
    public float getRadius() { return _radius; }
    public float getSpeed() { return _speed; }
    public float getAngle() { return _angle; }

    //Setters

    //Para el radio de movimiento
    public void setRadius(float radius_) { _radius = radius_; }

    //Para el ángulo de giro
    public void setAngle(float angle_) { _angle = angle_; }

    //Para la velocidad de movimiento
    public void setSpeed(float speed_) { _speed = speed_; }

    //Constructora
    Item(float posX, float posY)
    {
        super(posX, posY * -1,0xFFFFFF00); //Constructora de gameObject
        setScale(1);
    }

    //Constructora
    Item(Vector2D pos_)
    {
        super(pos_, 0xFFFFFF00); //Constructora de gameObject
    }

    //Update
    @Override public void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        if(_radius != 0)
        {
            pos.x = _radius * (float)cos(toRadians(_angle));
            pos.y = _radius * (float)sin(toRadians(_angle));

            _angle += _speed * delta;
            _angle = (_angle % 360);
        }

        _rotAngle += _rotSpeed * delta;
        _rotAngle = (_rotAngle % 360);

        if (toDie && pendingDeathTime >= 0) //Para la animacion de ampliar tamaño al ser recogida
        {
            pendingDeathTime -= delta;
            setScale(getScale() + (sizeGrow * (float) delta));
        }
        else if (pendingDeathTime <= 0)
            _dead = true;
    }

    @Override public void lateUpdate(double delta) { }

}
