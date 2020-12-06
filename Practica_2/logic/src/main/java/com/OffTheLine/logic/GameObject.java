package com.OffTheLine.logic;

import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;

public abstract class GameObject implements com.OffTheLine.common.GameObject {

    /*Variables*/

    protected Vector2D pos;
    protected float _scale;
    boolean _dead = false;

    /*Funciones*/

    /*Getters*/
    //Obtener componente x de la posicion
    public float getX() { return  pos.x; }

    //Obtener componente y de la posicion
    public float getY() { return  pos.y; }

    //Obtener la escala
    public float getScale() { return  _scale; }

    //Obtener si esta muerto
    boolean getDead() { return _dead; }

    /*Setters*/

    //Fijar componente x de la posicion
    public void setX(float X) { pos.x = X; }

    //Fijar componente y de la posicion
    public void setY(float Y) { pos.y = Y; }

    //Fijar escala
    public void setScale(float scale) { _scale = scale; }

    //Constructora
    GameObject(float posX, float posY)
    {
        pos = new Vector2D(posX, posY);
        _scale = 1;
    }

    //Constructora
    GameObject(Vector2D pos_)
    {
        pos = pos_;
        _scale = 1;
    }

    @Override public void checkInputs(ArrayList<Input.TouchEvent> inputs) { }
}
