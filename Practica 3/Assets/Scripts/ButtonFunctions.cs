using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ButtonFunctions : MonoBehaviour
{
    public static void Pause()
    {
        GameManager.Instance().Pause();
    }

    public static void Hint()
    {
        GameManager.Instance().UseHint();
    }

    public static void Reload()
    {
        GameManager.Instance().RestartLevel();
    }

    public static void Home()
    {
        GameManager.Instance().BackToMenu();
    }
}
