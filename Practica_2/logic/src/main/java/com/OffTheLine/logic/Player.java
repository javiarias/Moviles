package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import com.OffTheLine.logic.Utils;

import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Player extends Square {

    class Collision{

        Collision(Vector2D p, int _currP, int _currV, int _nextV)
        {
            collisionPoint = p;
            _currentPath = _currP;
            _currentVert = _currV;
            _nextVert = _nextV;
        }

        public Vector2D collisionPoint;
        public int _currentVert;
        public int _currentPath;
        public int _nextVert;
    }

    ArrayList<Path> _paths;
    ArrayList<Collision> possibleCollisions = new ArrayList<Collision>();

    int _currentVert = 0;
    int _nextVert = 1;
    int _currentPath = 0;
    boolean invert = false;
    boolean jumping = false;
    Vector2D direction; //direccion normalizada

    protected float _speed;

    public float getSpeed()
    {
        return _speed;
    }
    public void setSpeed(float speed_)
    {
        _speed = speed_;
    }

    //Constructora
    Player(ArrayList<Path> paths)
    {
        super(paths.get(0)._vertices.get(0),0xFF0088FF); //Constructora de gameObject
        _size = 12;
        _speed = 0.02f;
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
                if (Utils.distancePointPoint(pos, c.collisionPoint) <= 1) //Sin este margen, en la circunferencia explota
                {
                    jumping = false;

                    //changeDirection(); //Esto en vez del if siguiente

                    if (!invert)
                    {
                        direction = direction.PerpendicularCounterClockwise(direction);
                        _currentVert = c._currentVert;
                        _nextVert = c._nextVert;
                    }
                    else
                    {
                        direction = direction.PerpendicularCounterClockwise(direction);
                        _currentVert = c._nextVert;
                        _nextVert = c._currentVert;
                    }

                    _currentPath = c._currentPath;
                }
            }

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

            temp = pos.add(add_);
            float d1 = next.distance(current);
            float d2 = temp.distance(current);
            past = (d1 <= d2);

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
                    }
                    else if (_nextVert == -1) //No pueden ser los dos negativos, de ahi el else if
                    {
                        _nextVert += path._vertices.size();
                    }
                }
            } else {
                pos = temp;
            }
        }
    }

    @Override
    public void lateUpdate(double delta) {

    }

    public void changeDirection()
    {
        if (_paths.get(_currentPath)._directions.size() == 0) {
            if (!invert)
                direction = direction.PerpendicularCounterClockwise(direction);
            else
                direction = direction.PerpendicularClockwise(direction);
        }
        else //LOS 0 HAY QUE MIRARLOS
        {
            float x = 0;
            float y = 0;
            if (_paths.get(_currentPath)._directions.get(0).x != 0)
            {
                x = _paths.get(_currentPath)._directions.get(0).x;
            }
            if (_paths.get(_currentPath)._directions.get(0).y != 0)
            {
                y = _paths.get(_currentPath)._directions.get(0).y;
            }

            direction.x = x;
            direction.y = y;
        }
    }

    public void Jump()
    {
        jumping = true;

        changeDirection();

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
                    if (!(point.x == pos.x && point.y == pos.y) && !(j == _currentVert && k == _nextVert))
                    {
                        Collision col_ = new Collision(point, i, j, k);
                        possibleCollisions.add(col_);
                    }
                }
            }
        }
    }

    public boolean outOfBounds(float Height, float Width)
    {
        boolean topY = (pos.y > Height/2);
        boolean botY = (pos.y < - (Height/2));
        boolean leftX = (pos.x < - (Width/2));
        boolean rightX = (pos.x > (Width/2));

        return (topY || botY || leftX || rightX);
    }

    public void die()
    {
        for (int i = 0; i < 10; i++)
        {
            Random r = new Random();

            //Crear lineas de 6 de longitud

            //Mover y rotar aleatoriamente
        }
    }
}
