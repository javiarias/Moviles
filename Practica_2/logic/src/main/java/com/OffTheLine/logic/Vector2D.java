package com.OffTheLine.logic;

public class Vector2D {
    float x;
    float y;

    public Vector2D(float _x, float _y){
        x = _x; y = _y;
    }

    Vector2D add(Vector2D v){
        return new Vector2D(x + v.x, y + v.y);
    }

    Vector2D subtract(Vector2D v){
        return new Vector2D(x - v.x, y - v.y);
    }

    Vector2D add(float v){
        return new Vector2D(x + v, y + v);
    }

    Vector2D subtract(float v){
        return new Vector2D(x - v, y - v);
    }

    Vector2D multiply(float v){
        return new Vector2D(x * v, y * v);
    }

    Vector2D divide(float v){
        return new Vector2D(x / v, y / v);
    }

    boolean equals(Vector2D v){
        return (x == v.x)  && (y == v.y);
    }

    float distance(Vector2D v)
    {
        return (float)Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2));
    }

    boolean isCloseTo(Vector2D v, float distance)
    {
        float dist = distance(v);
        return (dist <= distance);
    }
};
