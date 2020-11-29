package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import com.OffTheLine.logic.Utils;

import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Vector;

public class Player extends Square {

    class Collision{

        Collision(Vector2D p, int _curr, int _next)
        {
            collisionPoint = p;
            _currentVert = _curr;
            _nextVert = _next;
        }

        public Vector2D collisionPoint;
        public int _currentVert;
        public int _nextVert;
    }

    ArrayList<Path> _paths;
    ArrayList<Collision> possibleCollisions = new ArrayList<Collision>();

    int _currentVert = 0;
    int _nextVert = 3;
    int _currentPath = 0;
    boolean invert = true;
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

        //Siempre se hace
        _rotAngle += _rotSpeed * delta;
        _rotAngle = (_rotAngle % 360);

        if (jumping) {
            for (Collision c : possibleCollisions)
            {
                if (Utils.distancePointPoint(pos, c.collisionPoint) == 0) //Valor de comprobación
                {
                    jumping = false;

                    if (!invert)
                        direction = direction.PerpendicularCounterClockwise(direction);
                    else
                        direction = direction.PerpendicularClockwise(direction);

                    _currentVert = c._currentVert;
                    _nextVert = c._nextVert;
                    //current y next
                }
            }

            //Comprobar que no se sale de los márgenes

            //Movimiento
            add_ = add_.add(direction);
            add_.multiply(_speed * (float) delta); //Hay que mirarlo porque va a todo ojete
            pos = pos.add(add_);
        }

        else
        {
            if (!inputList.isEmpty() && !jumping)
            {
                for (Input.TouchEvent tE : inputList)
                {
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

            if (!invert)
            {
                temp = pos.add(add_);
                float d1 = current.distance(next);
                float d2 = current.distance(temp);
                past = (d1 <= d2);
            }
            else //Sigue fallando aqui
            {
                temp = pos.add(add_);
                float d1 = current.distance(next);
                float d2 = current.distance(temp);

                past = (d1 <= d2);
            }

            if (past) {
                pos = next;

                if (!invert)
                {
                    _currentVert = (_currentVert + 1) % path._vertices.size();
                    _nextVert = (_nextVert + 1) % path._vertices.size();
                }
                else
                {
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

            if (!invert)
                direction = direction.PerpendicularCounterClockwise(direction);
            else
                direction = direction.PerpendicularClockwise(direction);

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
        Vector2D aux = new Vector2D(pos.x, pos.y);
        Vector2D aux2 = direction.multiply(1000);
        aux = aux.add(aux2);

        invert = !invert;

        for (int i = 0; i < _paths.size() ; i++)
        {
            for (int j = 0; j < _paths.get(i)._vertices.size(); j++)
            {
                Vector2D point = null;
                int k;

                if (j == _paths.get(i)._vertices.size() - 1) //Primero y ultimo
                {
                    k = 0;
                    point = Utils.pointIntersectionSegmentSegment(pos, aux, _paths.get(i)._vertices.get(j), _paths.get(i)._vertices.get(k));
                }

                else
                {
                    k = j + 1;
                    point = Utils.pointIntersectionSegmentSegment(pos, aux, _paths.get(i)._vertices.get(j), _paths.get(i)._vertices.get(k));
                }

                if (point != null)
                {
                    //Para no añadir el propio punto en el que está al saltar
                    boolean xEqual = point.x == pos.x;
                    boolean yEqual = point.y == pos.y;

                    if (!(xEqual && yEqual))
                    {
                        Collision col_ = new Collision(point, j, k);
                        possibleCollisions.add(col_);
                    }
                }
            }
        }
    }
}
