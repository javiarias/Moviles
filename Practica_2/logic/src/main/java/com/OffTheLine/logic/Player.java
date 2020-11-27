package com.OffTheLine.logic;

import java.util.ArrayList;

public class Player extends Square {

    ArrayList<Path> _paths;

    int _currentVert = 0;
    int _nextVert = 1;
    int _currentPath = 0;

    //THIS IS FOR MOVEMENT!!!!!
    protected float _speed;

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
        _speed = 5;

        _paths = paths;
    }

    @Override
    public void update(double delta) {
        _rotAngle += _rotSpeed * delta;

        _rotAngle = (_rotAngle % 360);

        Path path =  _paths.get(_currentPath);

        Vector2D current = path._vertices.get(_currentVert);
        Vector2D next = path._vertices.get(_nextVert);

        Vector2D temp = pos.add(next.subtract(current).multiply(_speed * (float)delta));

        boolean past = (current.distance(next) <= current.distance(temp));

        if(past)
        {
            pos = next;

            _currentVert = (_currentVert + 1) % path._vertices.size();
            _nextVert = (_nextVert + 1) % path._vertices.size();

            //_rotSpeed *= -1;
        }
        else {
            pos = temp;
        }
    }

    @Override
    public void lateUpdate(double delta) {

    }
}
