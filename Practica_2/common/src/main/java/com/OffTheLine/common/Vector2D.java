package com.OffTheLine.common;

//Clase vector que hemos implementado por nuestra (no nos dimos cuenta de que había una hecha)
public class Vector2D {

    /*Variables*/
    public float x;
    public float y;

    /*Funciones*/

    //Constructora a partir de puntos
    public Vector2D(float _x, float _y) { x = _x; y = _y; }

    //Constructora a partir de otro vector
    public Vector2D(Vector2D v) { x = v.x; y = v.y; }

    //Suma de vectores
    public Vector2D add(Vector2D v) { return new Vector2D(x + v.x, y + v.y); }

    //Resta de vectores (Vector - Parámetro)
    public Vector2D subtract(Vector2D v){ return new Vector2D(x - v.x, y - v.y); }

    //Suma de un número
    public Vector2D add(float v) { return new Vector2D(x + v, y + v); }

    //Resta de un número
    public Vector2D subtract(float v) { return new Vector2D(x - v, y - v); }

    //Producto por un número
    public Vector2D multiply(float v) { return new Vector2D(x * v, y * v); }

    //División por un número
    public Vector2D divide(float v) { return new Vector2D(x / v, y / v); }

    //Módulo
    public float module() { return (float) Math.sqrt((x * x) + (y * y)); }

    //Normalizar vector
    public void normalize()
    {
        float modulo = this.module();
        Vector2D aux = this.divide(modulo);

        x = aux.x;
        y = aux.y;
    }

    //Distancia a otro vector
    public float distance(Vector2D v) { return (float)(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2)); }

    //Direccion perpendicular al vector dado en el sentido de las agujas del reloj
    public Vector2D PerpendicularClockwise(Vector2D v) { return new Vector2D(v.y, -v.x); }

    //Direccion perpendicular al vector dado en el sentido contrario a las agujas del reloj
    public Vector2D PerpendicularCounterClockwise(Vector2D v) { return new Vector2D(-v.y, v.x); }

    /*No se usan*/

    public boolean equals(Vector2D v){
        return (x == v.x)  && (y == v.y);
    }

    public boolean isCloseTo(Vector2D v, float distance)
    {
        float dist = distance(v);
        return (dist <= distance);
    }

    public float dot(Vector2D v)
    {
        return (x * v.y) - (y * v.x);
    }
};
