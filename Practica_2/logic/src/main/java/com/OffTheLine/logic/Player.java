package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;
import java.util.Random;

public class Player extends Square {

    //Clase colision
    class Collision
    {
        /*Variables*/
        public Vector2D collisionPoint;
        public int _currentVert;
        public int _currentPath;
        public int _nextVert;

        //Constructora
        Collision(Vector2D p, int _currP, int _currV, int _nextV)
        {
            collisionPoint = p;
            _currentPath = _currP;
            _currentVert = _currV;
            _nextVert = _nextV;
        }
    }

    /*Variables*/

    //Arrays
    ArrayList<Path> _paths;
    ArrayList<Collision> possibleCollisions = new ArrayList<Collision>();
    ArrayList<Line> deadLines = new ArrayList<Line>();

    //Para vertices y segmentos
    int _currentVert = 0;
    int _nextVert = 1;
    int _currentPath = 0;

    //Para movimientos
    boolean _invert = false;
    boolean _jumping = false;
    Vector2D _direction; //Normalizada
    Vector2D _preJumpDir; //Normalizada
    protected float _speed;
    protected float _jumpSpeed;

    //Para el efecto de screenshake
    public boolean _shake = false;

    //Umbral para colision
    public float colRange = 20;

    /*Getters*/

    public float getSpeed()
    {
        return _speed;
    }

    /*Setters*/

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

    //Actualizacion de parámetros relativos al nivel
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

    //Render
    @Override public void render(Graphics g)
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

    //Comprobacion de colisiones del jugador
    public void checkPlayerCollisions(Level level, ArrayList<Item> itemsToDestroy, double delta)
    {
        if(_dead) //Si muere, no hace falta comprobar nada
            return;

        //Nueva posicion para comprobar colisiones
        Vector2D add_ = new Vector2D(_direction);
        add_ = add_.multiply(_jumpSpeed * (float) delta);
        Vector2D nuPos = pos.add(add_);

        for (Item i : level.getItems())
        {
            if (Utils.distancePointPoint(pos, i.pos) < colRange * colRange) //ColRange^2 para evitar raíces cuadradas
            {
                if (!i.toDie) {
                    i.toDie = true;
                    itemsToDestroy.add(i);
                }
            }
        }

        for (Enemy e : level.getEnemies())
        {
            //2 diferentes, para evitar que si los vértices se invierten en el movimiento haya problemas
            Vector2D intersect = Utils.pointIntersectionSegmentSegment(pos, nuPos, e._vertice1.add(e.pos), e._vertice2.add(e.pos));
            Vector2D intersect2 = Utils.pointIntersectionSegmentSegment(pos, nuPos, e._vertice2.add(e.pos), e._vertice1.add(e.pos));

            if (intersect != null && intersect2 != null)
                die();
        }
    }

    //Update
    @Override public void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        if(_dead)
        {
            for (Line l : deadLines) //Animacion al morir
            {
                l.update(delta, inputList);
            }
        }
        else
        {
            if(!_jumping) //Si ya está saltando, no se comprueban los inputs
                checkInputs(inputList);

            //Siempre se hace
            _rotAngle += _rotSpeed * delta;
            _rotAngle = (_rotAngle % 360);

            if (_jumping) {

                //Nueva posicion
                Vector2D add_ = new Vector2D(_direction);
                add_ = add_.multiply(_jumpSpeed * (float) delta);
                Vector2D nuPos = pos.add(add_);

                for (Collision c : possibleCollisions)
                {
                    float colDist = Utils.distancePointPoint(pos, c.collisionPoint);
                    float colDistPast = Utils.distancePointPoint(pos, nuPos);

                    if (colDistPast >= colDist) //Sin este margen, en el nivel de la circunferencia explota todo
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
            else //Si no está en salto, se hace el movimiento "normal"
            {
                updateDirection();

                Path path = _paths.get(_currentPath);
                Vector2D current = path._vertices.get(_currentVert);
                Vector2D next = path._vertices.get(_nextVert);

                //No se saca fuera porque _speed y _jumpSpeed son diferentes
                Vector2D add_ = new Vector2D(_direction);
                add_ = add_.multiply(_speed * (float) delta);
                Vector2D nuPos = pos.add(add_);

                float d1 = next.distance(current);
                float d2 = nuPos.distance(current);
                boolean past = (d1 <= d2);

                if (past) //Comprobación de si se ha pasado del vertice y toca cambiar el sentido del movimiento
                {
                    pos = next;
                    if (!_invert) //Si esta en sentido inverso al habitual
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
                }
                else
                {
                    pos = nuPos;
                }
            }
        }
    }

    //Actualizar la direccion
    private void updateDirection()
    {
        Path path = _paths.get(_currentPath);
        Vector2D current = path._vertices.get(_currentVert);
        Vector2D next = path._vertices.get(_nextVert);

        Vector2D aux = next.subtract(current);
        aux.normalize();
        _direction = aux;
    }

    @Override public void lateUpdate(double delta) {}

    //Comprobación del input
    @Override public void checkInputs(ArrayList<Input.TouchEvent> inputs)
    {
        if (!inputs.isEmpty())
            for (Input.TouchEvent tE : inputs)
            {
                if (tE.type == Input.TouchEvent.TouchType.PRESS)
                {
                    jump();
                    break;
                }
            }
    }

    //Fijar direccion cuando va a saltar
    public void setJumpDirection()
    {
        if (_paths.get(_currentPath)._directions.size() == 0)
        {
            if (!_invert)
                _direction = _direction.PerpendicularCounterClockwise(_direction);
            else
                _direction = _direction.PerpendicularClockwise(_direction);
        }
        else
            _direction = new Vector2D(_paths.get(_currentPath)._directions.get(_currentVert));

        _direction.normalize();
    }

    //Inversion del movimiento
    private void invertMovement()
    {
        _invert = !_invert;
        int tmp = _currentVert;
        _currentVert = _nextVert;
        _nextVert = tmp;
    }

    //Inversion del movimiento
    private void invertMovement(Collision c)
    {
        boolean shouldInvert = null == Utils.pointIntersectionSegmentSegment(c.collisionPoint, c.collisionPoint, pos, pos.add(_preJumpDir.multiply(1000)));

        if(shouldInvert)
            _invert = !_invert;

        arrangeVerts(c);
    }

    //Cambio de vertices
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

    //Salto
    public void jump()
    {
        _preJumpDir = _direction;
        possibleCollisions.clear();
        _jumping = true;

        setJumpDirection();

        Vector2D checkStart = pos.add(_direction);
        Vector2D checkEnd = pos.add(_direction.multiply(1000));

        //Se comprueban todos los posibles, y las posibles colisiones se añaden a un vector para ahorrar comprobaciones más tarde
        for (int path = 0; path < _paths.size() ; path++)
        {
            for (int vert = 0; vert < _paths.get(path)._vertices.size(); vert++)
            {
                Vector2D point = null;

                int followingVert = (vert + 1) % _paths.get(path)._vertices.size();

                point = Utils.pointIntersectionSegmentSegment(checkStart, checkEnd, _paths.get(path)._vertices.get(vert), _paths.get(path)._vertices.get(followingVert));

                if (point != null)
                {
                    Collision col_ = new Collision(point, path, vert, followingVert);
                    possibleCollisions.add(col_);
                }
            }
        }
    }

    //Comprobacion de si esta fuera de los limites de la pantalla
    public boolean outOfBounds(float Height, float Width)
    {
        boolean topY = (pos.y > Height/2);
        boolean botY = (pos.y < - (Height/2));
        boolean leftX = (pos.x < - (Width/2));
        boolean rightX = (pos.x > (Width/2));

        return (topY || botY || leftX || rightX);
    }

    //Muerte
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

            deadLines.add(new Line(randPos, _color, randDir, angle, rotDir));
        }
    }
}
