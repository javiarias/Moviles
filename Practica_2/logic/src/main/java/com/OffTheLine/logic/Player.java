package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.awt.Point;
import java.sql.Array;
import java.sql.Statement;
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
    Vector2D _preJumpDir; //direccion normalizada

    public boolean _shake = false;

    public float colRange = 20;

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
        _currentPath = 0;
        _nextVert = 1;
        _jumping = false;
        _invert = false;
        _dead = false;
        updateDirection();
    }

    @Override
    public void render(Graphics g)
    {
        if (!_dead)
        {
            g.save();
            super.render(g);
            g.restore();
        }
        else
        {
            for (Line l: deadLines)
            {
                g.save();
                l.render(g);
                g.restore();
            }
        }
    }

    public void checkPlayerCollisions(Level level, ArrayList<Item> itemsToDestroy, double delta)
    {
        if(_dead)
            return;

        //Movimiento
        Vector2D add_ = new Vector2D(_direction);
        add_ = add_.multiply(_jumpSpeed * (float) delta);
        Vector2D nuPos = pos.add(add_);

        for (Item i : level.getItems())
        {
            if (Utils.distancePointPoint(pos, i.pos) < colRange * colRange)
            {
                if (!i.toDie) {
                    i.toDie = true;
                    itemsToDestroy.add(i);
                }
            }
        }

        for (Enemy e : level.getEnemies())
        {
            Vector2D intersect = Utils.pointIntersectionSegmentSegment(pos, nuPos, e._vertice1.add(e.pos), e._vertice2.add(e.pos));
            Vector2D intersect2 = Utils.pointIntersectionSegmentSegment(pos, nuPos, e._vertice2.add(e.pos), e._vertice1.add(e.pos));

            if (intersect != null && intersect2 != null)
                die(); //Crear las lineas, sustituyendo al cuadrado en el render?
        }
    }

    @Override
    public void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        if(_dead)
        {
            for (Line l : deadLines)
            {
                l.update(delta, inputList);
            }
        }
        else
        {
            if(!_jumping)
                checkInputs(inputList);

            //Siempre se hace
            _rotAngle += _rotSpeed * delta;
            _rotAngle = (_rotAngle % 360);

            if (_jumping) {

                //Movimiento
                Vector2D add_ = new Vector2D(_direction);
                add_ = add_.multiply(_jumpSpeed * (float) delta);
                Vector2D nuPos = pos.add(add_);

                for (Collision c : possibleCollisions)
                {
                    float colDist = Utils.distancePointPoint(pos, c.collisionPoint);
                    float colDistPast = Utils.distancePointPoint(pos, nuPos);

                    if (colDistPast >= colDist) //Sin este margen, en la circunferencia explota
                    {
                        _jumping = false;

                        _currentPath = c._currentPath;
                        invertMovement(c);

                        nuPos = c.collisionPoint;
                        updateDirection();

                        _shake = true;
                    }
                }

                pos = nuPos;
            }

            else
            {
                updateDirection();

                Path path = _paths.get(_currentPath);
                Vector2D current = path._vertices.get(_currentVert);
                Vector2D next = path._vertices.get(_nextVert);

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
    }

    private void updateDirection()
    {
        Path path = _paths.get(_currentPath);
        Vector2D current = path._vertices.get(_currentVert);
        Vector2D next = path._vertices.get(_nextVert);

        Vector2D aux = next.subtract(current);
        aux.normalize();
        _direction = aux;
    }

    @Override
    public void lateUpdate(double delta) {}

    @Override
    public void checkInputs(ArrayList<Input.TouchEvent> inputs)
    {
        if (!inputs.isEmpty())
            for (Input.TouchEvent tE : inputs) {
                if (tE.type == Input.TouchEvent.TouchType.PRESS) {
                    jump();
                    break;
                }
            }
    }

    public void setJumpDirection()
    {
        if (_paths.get(_currentPath)._directions.size() == 0)
        {
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

        _direction.normalize();
    }

    private void invertMovement()
    {
        _invert = !_invert;

        int tmp = _currentVert;
        _currentVert = _nextVert;
        _nextVert = tmp;
    }

    private void invertMovement(Collision c)
    {
        boolean shouldInvert = null == Utils.pointIntersectionSegmentSegment(c.collisionPoint, c.collisionPoint, pos, pos.add(_preJumpDir.multiply(1000)));

        if(shouldInvert)
            _invert = !_invert;

        arrangeVerts(c);
    }

    private void arrangeVerts(Collision c)
    {
        if(_invert)
        {
            _currentVert = c._nextVert;
            _nextVert = c._currentVert;
        }
        else
        {
            _currentVert = c._currentVert;
            _nextVert = c._nextVert;
        }
    }

    public void jump()
    {
        _preJumpDir = _direction;

        possibleCollisions.clear();

        _jumping = true;

        setJumpDirection();

        Vector2D checkStart = pos.add(_direction);
        Vector2D checkEnd = pos.add(_direction.multiply(1000));

        for (int path = 0; path < _paths.size() ; path++)
        {
            for (int vert = 0; vert < _paths.get(path)._vertices.size(); vert++)
            {
                Vector2D point = null;

                int followingVert = (vert + 1) % _paths.get(path)._vertices.size();

                point = Utils.pointIntersectionSegmentSegment(checkStart, checkEnd, _paths.get(path)._vertices.get(vert), _paths.get(path)._vertices.get(followingVert));

                if (point != null)
                {
                    //System.out.print("point: " + point.x + ", " + point.y);
                    //System.out.println(" -- pos: " + pos.x + ", " + pos.y);

                    Collision col_ = new Collision(point, path, vert, followingVert);
                    possibleCollisions.add(col_);
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
        _dead = true;

        deadLines.clear();

        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < 10; i++)
        {
            Vector2D randPos = new Vector2D(pos.x,pos.y);

            Vector2D randDir = new Vector2D(r.nextFloat() * 50,r.nextFloat() * 50);
            float angle = r.nextFloat() * 360;
            boolean rotDir = r.nextFloat() >= 0.5f;

            if (r.nextFloat() >= 0.5f)
                randDir.x *= -1;
            if (r.nextFloat() >= 0.5f)
                randDir.y *= -1;

            //Crear lineas
            deadLines.add(new Line(randPos, _color, randDir, angle, rotDir));
        }
    }
}
