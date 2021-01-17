using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Advertisements;

public class AdsManager : MonoBehaviour
{
    private string gameID = "3977791"; //En principio solo para Android, asi que se podría dejar valor por defecto
    private bool testMode = true;

    // Start is called before the first frame update
    void Start()
    {
        //Monetization.Initialize(gameID, testMode);

        if (Application.platform == RuntimePlatform.Android)
            gameID = "3977791";
        //else //Como somos pobres y no hay para iOS, pues eso

        Advertisement.Initialize(gameID, testMode);
    }
}
