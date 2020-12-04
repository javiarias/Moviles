package com.OffTheLine.common;

public class Vector2D {
    public float x;
    public float y;

    public Vector2D(float _x, float _y){
        x = _x; y = _y;
    }

    public Vector2D(Vector2D v){
        x = v.x; y = v.y;
    }

    public Vector2D add(Vector2D v){
        return new Vector2D(x + v.x, y + v.y);
    }

    public Vector2D subtract(Vector2D v){
        return new Vector2D(x - v.x, y - v.y);
    }

    public Vector2D add(float v){
        return new Vector2D(x + v, y + v);
    }

    public Vector2D subtract(float v){
        return new Vector2D(x - v, y - v);
    }

    public Vector2D multiply(float v){
        return new Vector2D(x * v, y * v);
    }

    public Vector2D divide(float v){
        return new Vector2D(x / v, y / v);
    }

    public boolean equals(Vector2D v){
        return (x == v.x)  && (y == v.y);
    }

    public Vector2D normalize(Vector2D v)
    {
        Vector2D r;

        float modulo = (float) Math.sqrt((v.x * v.x) + (v.y * v.y));
        r = v.divide(modulo);

        return r;
    }

    public float distance(Vector2D v)
    {
        return (float)(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2));
    }

    public boolean isCloseTo(Vector2D v, float distance)
    {
        float dist = distance(v);
        return (dist <= distance);
    }

    public Vector2D PerpendicularClockwise(Vector2D v)
    {
        return new Vector2D(v.y, -v.x);
    }

    public Vector2D PerpendicularCounterClockwise(Vector2D v)
    {
        return new Vector2D(-v.y, v.x);
    }
};
