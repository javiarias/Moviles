using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameUtils
{
    //Está hecho así para que comprobar si dos direcciones son opuestas sea solo sumar
    public enum Direction { UP = 1, DOWN = -1, RIGHT = 2, LEFT = -2 }

    public static Direction GetDir(Vector2 dir)
    {
        if (Mathf.Abs(dir.x) > Mathf.Abs(dir.y))
        {
            if (dir.x > 0)
                return Direction.RIGHT;
            else
                return Direction.LEFT;
        }
        else
        {
            if (dir.y > 0)
                return Direction.UP;
            else
                return Direction.DOWN;
        }
    }
}
