package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;
import java.util.Random;

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
    ArrayList<Line> deadLines = new ArrayList<Line>();

    int _currentVert = 0;
    int _nextVert = 1;
    int _currentPath = 0;
    boolean _invert = false;
    boolean _jumping = false;
    Vector2D _direction; //direccion normalizada

    protected float _speed;
    protected float _jumpSpeed;

    public float getSpeed()
    {
        return _speed;
    }
    public void setSpeed(float speed_)
    {
        _speed = speed_;
    }

    //Constructora
    Player(float speed)
    {
        super(new Vector2D(0, 0),0xFF0088FF); //Constructora de gameObject
        _size = 12;
        _speed = speed;
        _jumpSpeed = 1500f;
        _dead = false;
    }

    public void newLevel(ArrayList<Path> paths)
    {
        _paths = paths;
        pos = paths.get(0)._vertices.get(0);

        _currentVert = 0;
        _nextVert = 1;
        _jumping = false;
    }

    @Override
    public void render(Graphics g)
    {
        if (!_dead)
            super.render(g);
        else
        {
            for (Line l: deadLines)
            {

            }
        }
    }

    @Override
    public void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        if(!_jumping)
            checkInputs(inputList);

        //Siempre se hace
        _rotAngle += _rotSpeed * delta;
        _rotAngle = (_rotAngle % 360);

        if (_jumping) {
            for (Collision c : possibleCollisions)
            {
                if (Utils.distancePointPoint(pos, c.collisionPoint) <= 1) //Sin este margen, en la circunferencia explota
                {
                    _jumping = false;

                    //changeDirection(); //Esto en vez del if siguiente

                    if (!_invert)
                    {
                        _direction = _direction.PerpendicularCounterClockwise(_direction);
                        _currentVert = c._currentVert;
                        _nextVert = c._nextVert;
                    }
                    else
                    {
                        _direction = _direction.PerpendicularCounterClockwise(_direction);
                        _currentVert = c._nextVert;
                        _nextVert = c._currentVert;
                    }

                    _currentPath = c._currentPath;
                }
            }

            //Movimiento
            Vector2D add_ = new Vector2D(_direction);
            add_ = add_.multiply(_jumpSpeed * (float) delta);
            pos = pos.add(add_);
        }

        else
        {
            Path path = _paths.get(_currentPath);
            Vector2D current = path._vertices.get(_currentVert);
            Vector2D next = path._vertices.get(_nextVert);

            Vector2D aux = next.subtract(current);
            aux = aux.normalize(aux);
            _direction = aux;

            Vector2D add_ = new Vector2D(_direction);
            add_ = add_.multiply(_speed * (float) delta);
            Vector2D nuPos = pos.add(add_);

            float d1 = next.distance(current);
            float d2 = nuPos.distance(current);
            boolean past = (d1 <= d2);

            if (past) {
                pos = next;

                if (!_invert)
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
                pos = nuPos;
            }
        }
    }

    @Override
    public void lateUpdate(double delta) {}

    @Override
    public void checkInputs(ArrayList<Input.TouchEvent> inputs)
    {
        if (!inputs.isEmpty())
            for (Input.TouchEvent tE : inputs)
                if (tE.type == Input.TouchEvent.TouchType.CLICK || tE.type == Input.TouchEvent.TouchType.PRESS) //el que sea
                {
                    jump();
                    return;
                }
    }

    public void changeDirection()
    {
        if (_paths.get(_currentPath)._directions.size() == 0) {
            if (!_invert)
                _direction = _direction.PerpendicularCounterClockwise(_direction);
            else
                _direction = _direction.PerpendicularClockwise(_direction);
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

            _direction.x = x;
            _direction.y = y;
        }
    }

    public void jump()
    {
        _jumping = true;

        changeDirection();

        //Check possible collisions
        Vector2D aux = new Vector2D(pos.x, pos.y);
        Vector2D aux2 = _direction.multiply(1000);
        aux = aux.add(aux2);

        _invert = !_invert;

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

            //Crear lineas
            deadLines.add(new Line(pos.x, pos.y));

            //Mover y rotar aleatoriamente
        }
    }
}
