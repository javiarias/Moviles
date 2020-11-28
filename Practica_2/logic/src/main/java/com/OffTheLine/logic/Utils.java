package com.OffTheLine.logic;

//PP la sugiere en el enunciado, asi que se hace

import com.OffTheLine.common.Vector2D;

public class Utils {

    //Distancia de un punto a un segmento
    //SP1, primer punto del segmento (origen), SP2 segundo punto del segmento (final), p el punto
    public static float distancePointSegment(Vector2D SP1, Vector2D SP2, Vector2D p)
    {
        float r = 0.0f;

        //Numerador
        float num = (SP2.x - SP1.x) * (p.y-SP1.y) - (SP2.y - SP1.y)*(p.x - SP1.x);

        //Denominador
        float den = distancePointPoint(SP1, SP2);

        r = (num/den);
        return  r;
    }

    //Distancia de un punto a un punto (igual hace falta)
    //P1, primer punto (origen), P2 segundo punto (final)
    public static float distancePointPoint(Vector2D p1, Vector2D p2)
    {
        float r = 0.0f;

        //Diferencia de x
        float xD = p2.x - p1.x;
        xD = xD * xD; //Al cuadrado

        //Diferencia de y
        float yD = p2.y - p1.y;
        yD = yD * yD; //Al cuadrado

        r = (float) Math.sqrt(xD + yD);
        return  r;
    }


    //recibe dos pares de vertices y devuelve el punto donde se cruzan (si lo hacen).
    public Vector2D pointIntersectionSegmentSegment(Vector2D S1P1, Vector2D S1P2, Vector2D S2P1, Vector2D S2P2)
    {
        Vector2D ret = null; //Por si no hay interseccion

        /*float s1_x, s1_y, s2_x, s2_y;
        s1_x = s1.p2.x - s1.p1.x;     s1_y = s1.p2.y - s1.p1.y;
        s2_x = s2.p2.x - s2.p1.x;     s2_y = s2.p2.y - s2.p1.y;

        float s, t;
        s = (-s1_y * (s1.p1.x - s2.p1.x) + s1_x * (s1.p1.y - s2.p1.y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (s1.p1.y - s2.p1.y) - s2_y * (s1.p1.y - s2.p1.y)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
        {
            // Collision detected
            p.x = s1.p1.x + (t * s1_x);
            p.y = s1.p1.y + (t * s1_y);
        }
        return p;*/

        return ret;
    }

    //sqrDistancePointSegment(...): recibe un segmento y un punto y devuelve
    //el cuadrado de la distancia del punto al segmento. Tened en cuenta que esto no es lo mismo que la distancia del punto a la recta definida por el segmento.
}
