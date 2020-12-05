package com.OffTheLine.logic;

import com.OffTheLine.common.Vector2D;

//Sugerida en el enunciado
public class Utils {

    //Distancia de un punto a un segmento
    //SP1, primer punto del segmento (origen), SP2 segundo punto del segmento (final), p el punto
    public static float distancePointSegment(Vector2D SP1, Vector2D SP2, Vector2D p)
    {
        float a = p.x - SP1.x;
        float b = p.y - SP1.y;
        float c = SP2.x - p.x;
        float d = SP2.y - p.y;

        float dot = a * c + b * d;
        float length = c * c + d * d;
        float parameter = -1;

        if (length != 0)
            parameter = dot/length;

        float auxX, auxY;

        if (parameter < 0)
        {
            auxX = SP1.x;
            auxY = SP1.y;
        }
        else if (parameter > 1)
        {
            auxX = SP2.x;
            auxY = SP2.y;
        }
        else
        {
            auxX = SP1.x + parameter * c;
            auxY = SP1.y + parameter * d;
        }

        float dx = p.x - auxX;
        float dy = p.y - auxY;

        float ret = dx * dx + dy * dy;

        return ret;
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

        r = xD + yD;
        return  r;
    }

    static boolean isInSegment(Vector2D p, Vector2D a, Vector2D b){
        Vector2D v1 = new Vector2D(b.x - a.x, b.y- a.y);
        Vector2D v2 = new Vector2D(b.x - p.x, b.y- p.y);

        //si el segmento es mayor que la distancia al pto de colisión, y ningún valor es negativo
        return v1.module() > v2.module() && v1.x * v2.x >= 0 && v1.y * v2.y >= 0;
    }

    //recibe dos pares de vertices y devuelve el punto donde se cruzan (si lo hacen).
    public static Vector2D pointIntersectionSegmentSegment(Vector2D S1P1, Vector2D S1P2, Vector2D S2P1, Vector2D S2P2)
    {
        Vector2D ret = null; //Por si no hay interseccion
        Vector2D tmp = null;

        float Ax;         float Ay;
        float Bx;         float By;
        float Cx;         float Cy;
        float Dx;         float Dy;
        float m1 = 0;     float m2 = 0;
        float x = 0;      float y = 0;

        //Comprobación de valores menores que otros
        if (S1P1.x <= S1P2.x)
        {
            Ax = S1P1.x;
            Ay = S1P1.y;
            Bx = S1P2.x;
            By = S1P2.y;
        }
        else
        {
            Ax = S1P2.x;
            Ay = S1P2.y;
            Bx = S1P1.x;
            By = S1P1.y;
        }

        if (S2P1.x <= S2P2.x)
        {
            Cx = S2P1.x;
            Cy = S2P1.y;
            Dx = S2P2.x;
            Dy = S2P2.y;
        }
        else
        {
            Cx = S2P2.x;
            Cy = S2P2.y;
            Dx = S2P1.x;
            Dy = S2P1.y;
        }

        if ((Ax - Bx) != 0)
        {
            m1 = ((Ay - By)/(Ax - Bx));
        }

        if ((Cx - Dx) != 0)
        {
            m2 = ((Cy - Dy)/(Cx - Dx));
        }

        if (Ax != Bx && Cx != Dx && m1 != m2)
        {
            x = (Cy - Cx*((Cy-Dy)/(Cx-Dx)) - (Ay - Ax*((Ay-By)/(Ax-Bx))))/((Ay-By)/(Ax-Bx)-(Cy-Dy)/(Cx-Dx));
            y = ((Ay-By)/(Ax-Bx)) * x + (Ay - Ax * ((Ay-By)/(Ax-Bx)));

            tmp = new Vector2D(x,y);
        }

        else if (Ax == Bx && Cx != Dx)
        {
            x = Ax;
            y = ((Cy-Dy)/(Cx-Dx)) * x + (Cy - Cx * ((Cy-Dy)/(Cx-Dx)));

            tmp = new Vector2D(x,y);
        }

        else if (Ax != Bx && Cx == Dx)
        {
            x = Cx;
            y = ((Ay - By) / (Ax - Bx)) * x + (Ay - Ax * ((Ay - By) / (Ax - Bx)));

            tmp = new Vector2D(x,y);
        }

        //Comprobacion de que el punto esta en los segmentos y que las pendientes sean distintas
        if (tmp != null && isInSegment(tmp, S1P1, S1P2) && isInSegment(tmp, S2P1, S2P2))
            ret = tmp;

        return ret;
    }
}
