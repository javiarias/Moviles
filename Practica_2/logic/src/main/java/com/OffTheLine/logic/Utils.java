package com.OffTheLine.logic;

//PP la sugiere en el enunciado, asi que se hace

import com.OffTheLine.common.Vector2D;
import java.lang.*;

import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.POSITIVE_INFINITY;

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
    public static Vector2D pointIntersectionSegmentSegment(Vector2D S1P1, Vector2D S1P2, Vector2D S2P1, Vector2D S2P2)
    {
        Vector2D ret = null; //Por si no hay interseccion

        float Ax;         float Ay;
        float Bx;         float By;
        float Cx;         float Cy;
        float Dx;         float Dy;

        float m1 = 0;
        float m2 = 0;
        float x = 0;
        float y = 0;

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
            ret = new Vector2D(x,y);
        }

        else if (Ax == Bx && Cx != Dx)
        {
            x = Ax;
            y = ((Cy-Dy)/(Cx-Dx)) * x + (Cy - Cx * ((Cy-Dy)/(Cx-Dx)));
            ret = new Vector2D(x,y);
        }

        else if (Ax != Bx && Cx == Dx)
        {
            x = Cx;
            y = ((Ay-By)/(Ax-Bx)) * x + (Ay - Ax * ((Ay-By)/(Ax-Bx)));
            ret = new Vector2D(x,y);
        }

        //Comprobacion de que el punto esta en los segmentos
        /*
        if (Ax - Bx != 0 && Cx - Dx != 0)
        {
            if (m1!= m2)
            {
                if (Ax < x && x < Bx && Cx < x && x < Dx)
                {
                    ret = new Vector2D(x,y);
                }
            }
        }*/

        return ret;
    }

    //sqrDistancePointSegment(...): recibe un segmento y un punto y devuelve
    //el cuadrado de la distancia del punto al segmento. Tened en cuenta que esto no es lo mismo que la distancia del punto a la recta definida por el segmento.
}
