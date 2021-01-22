using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Clase genérica con funciones que puedan tener los botones
/// </summary>
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

    public static void NextLevel()
    {
        GameManager.Instance().NextLevel();
    }

    /// <summary>
    /// Función para lanzar un anuncio con premio (hint), empleando un callback
    /// </summary>
    public static void RewardedVideo()
    {
        AdsManager.ShowRewardedAd(new UnityEngine.Advertisements.ShowOptions { resultCallback = GameManager.Instance().AddHintAd });
    }


    /// <summary>
    /// Función para lanzar un anuncio con premio (hint) CUANDO el jugador se queda sin hints pero desea usarlas, empleando un callback
    /// </summary>
    public static void FreeHint()
    {
        AdsManager.ShowRewardedAd(new UnityEngine.Advertisements.ShowOptions { resultCallback = GameManager.Instance().FreeHint });
    }
}
