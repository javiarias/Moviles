package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import com.OffTheLine.logic.Utils;

import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Vector;

public class Player extends Square {

    ArrayList<Path> _paths;

    int _currentVert = 0;
    int _nextVert = 1;
    int _currentPath = 0;
    boolean invert = false;
    boolean jumping = false;

    //THIS IS FOR MOVEMENT!!!!!
    protected float _speed;

    Vector2D direction; //direccion normalizada

    public float getSpeed()
    {
        return _speed;
    }

    //Setters
    public void setSpeed(float speed_)
    {
        _speed = speed_;
    }

    //Constructora
    Player(ArrayList<Path> paths)
    {
        super(paths.get(0)._vertices.get(0),0xFF0088FF); //Constructora de gameObject

        _size = 12;
        _speed = 0.01f;

        _paths = paths;
    }

    @Override
    public void update(double delta, ArrayList<Input.TouchEvent> inputList) {
        Vector2D add_ = new Vector2D(0, 0);
        Vector2D temp;
        boolean past;

        if (jumping) {
            /*
            //Comprobar colision
            for (int i = 0; i < _paths.size() ; i++) {
                for (int j = 0; j < _paths.get(i)._vertices.size(); j++) {

                    if (j != _currentVert || j != _nextVert) {

                        if (j == _paths.get(i)._vertices.size() - 1) //Primero y ultimo
                        {
                            if (Utils.distancePointSegment(_paths.get(i)._vertices.get(j), _paths.get(i)._vertices.get(0), pos) <= 5) {
                                jumping = false;
                                direction = direction.PerpendicularCounterClockwise(direction);
                            }
                        }

                        if (Utils.distancePointSegment(_paths.get(i)._vertices.get(j), _paths.get(i)._vertices.get(j + 1), pos) <= 5) //Normal

                        {
                            jumping = false;
                            direction = direction.PerpendicularCounterClockwise(direction);

                            //check de next y current?
                        }
                    }
                }
            }*/


            //Comprobar que no se sale de los márgenes

            //Movimiento
            add_ = add_.add(direction);
            add_.multiply(_speed * (float) delta); //Hay que mirarlo porque va a todo ojete
            pos = pos.add(add_);
        }

        else {
            _rotAngle += _rotSpeed * delta;

            _rotAngle = (_rotAngle % 360);

            if (!inputList.isEmpty() && !jumping) {

                for (Input.TouchEvent tE : inputList) {
                    if (tE.type == Input.TouchEvent.TouchType.CLICK || tE.type == Input.TouchEvent.TouchType.PRESS) //el que sea
                    {
                        Jump();
                        return;
                    }
                }
            }

            Path path = _paths.get(_currentPath);

            Vector2D current = path._vertices.get(_currentVert);
            Vector2D next = path._vertices.get(_nextVert);

            Vector2D aux = next.subtract(current);
            aux = aux.normalize(aux);
            direction = aux;

            add_ = add_.add(direction);
            add_.multiply(_speed * (float) delta); //Hay que mirarlo porque va a todo ojete
            if (!invert) {
                temp = pos.add(add_);
                float d1 = current.distance(next);
                float d2 = current.distance(temp);

                past = (d1 <= d2);
            } else {
                //No funciona???
                temp = pos.add(add_);
                float d1 = current.distance(next);
                float d2 = current.distance(temp);

                past = (d1 <= d2);
            }

            if (past) {
                pos = next;

                if (!invert) {
                    _currentVert = (_currentVert + 1) % path._vertices.size();
                    _nextVert = (_nextVert + 1) % path._vertices.size();
                } else {
                    _currentVert = (_currentVert - 1) % path._vertices.size();
                    _nextVert = (_nextVert - 1) % path._vertices.size();

                    if (_currentVert == -1) //Evitar valores negativos
                    {
                        _currentVert += path._vertices.size();
                    } else if (_nextVert == -1) //No pueden ser los dos negativos, de ahi el else if
                    {
                        _nextVert += path._vertices.size();
                    }
                }
                //_rotSpeed *= -1;
            } else {
                pos = temp;
            }
        }
    }

    @Override
    public void lateUpdate(double delta) {

    }

    public void Jump()
    {
        if (_paths.get(_currentPath)._directions.size() == 0) {
            direction = direction.PerpendicularCounterClockwise(direction);
            jumping = true;
        }
        else //To test
        {
            float x = 0;
            float y = 0;
            if (_paths.get(_currentPath)._directions.get(_currentPath).x != 0)
            {
                x = direction.x * _paths.get(_currentPath)._directions.get(_currentPath).x;
            }
            if (_paths.get(_currentPath)._directions.get(_currentPath).y != 0)
            {
                y = direction.y * _paths.get(_currentPath)._directions.get(_currentPath).y;
            }

            direction.x = x;
            direction.y = y;
        }

        //Check possible collisions

    }
}
