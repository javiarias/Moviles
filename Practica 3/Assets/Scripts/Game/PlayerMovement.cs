using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMovement : MonoBehaviour
{
    bool swipe = false;

    Vector2 lastPos;

    /// <summary>
    /// Límite que determina cuánto has de deslizar el dedo para que se reconozca como movimiento
    /// </summary>
    public float swipeThreshold = 0;


    void Update()
    {
        if (Input.touchCount >= 1)
            HandleInput(Input.GetTouch(0));
        else if(swipe)
            swipe = false;
    }

    /// <summary>
    /// Función sencilla que recibe el Input.touch y gestiona el movimiento
    /// </summary>
    /// <param name="input">El input a gestionar</param>
    void HandleInput(Touch input)
    {
        if (input.deltaPosition.sqrMagnitude != 0)
        {
            if (!swipe)
            {
                swipe = true;
                lastPos = input.position;
            }
            else
            {

                Vector2 dirVect = input.position - lastPos;

                if (dirVect.sqrMagnitude > swipeThreshold)
                {
                    //Debug.Log("Swiping!");
                    Debug.Log(dirVect.sqrMagnitude);

                    dirVect.Normalize();

                    lastPos = input.position;

                    GameUtils.Direction dir = GameUtils.GetDir(dirVect);

                    HandleDir(dir);
                }
            }
        }
        else
        {
            swipe = false;
        }
    }


    /// <summary>
    /// Función que gestiona la dirección obtenida del input, moviendo al jugador en consecuencia
    /// </summary>
    /// <param name="dir">Dirección en la que moverse</param>
    void HandleDir(GameUtils.Direction dir)
    {
        Vector2 translate = Vector2.zero;

        switch (dir)
        {
            case GameUtils.Direction.UP:
                translate = new Vector2(0, -1);
                break;

            case GameUtils.Direction.DOWN:
                translate = new Vector2(0, 1);
                break;

            case GameUtils.Direction.LEFT:
                translate = new Vector2(-1, 0);
                break;

            case GameUtils.Direction.RIGHT:
                translate = new Vector2(1, 0);
                break;

            default:
                break;
        }

        transform.Translate(translate);
    }

}
