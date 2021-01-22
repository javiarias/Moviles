using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Advertisements;

public class AdsManager : MonoBehaviour, IUnityAdsListener
{

    //Variables
    private bool testMode = true;
    public static AdsManager instance; //Para singleton
    private static bool noAds = false;

    //IDs
    private string gameID = "3977791"; //En principio solo para Android, asi que se podría dejar valor por defecto
    private static string video_ID = "video";
    private static string rewarded_ID = "rewardedVideo";
    private static string banner_ID = "Banner";
    
    //Metodos de la interfaz IUnityAdsListener
    public void OnUnityAdsReady(string placementId)
    {
        //throw new System.NotImplementedException();
    }

    public void OnUnityAdsDidError(string message)
    {
        //throw new System.NotImplementedException();
    }

    public void OnUnityAdsDidStart(string placementId)
    {
        //throw new System.NotImplementedException();
    }

    public void OnUnityAdsDidFinish(string placementId, ShowResult showResult)
    {
    }

    //Métodos generales

    private void Start()
    {
        //Estructura de singleton
        if (instance != null)
        {
            Destroy(gameObject);
            return;
        }
        else
        {
            instance = this;
            DontDestroyOnLoad(gameObject); //Persistente entre escenas

            Advertisement.AddListener(this); //Para temas de anuncios
            Advertisement.Initialize(gameID, testMode); //Inicialización de los anuncios   
        }
    }

    public static void ShowVideo(ShowOptions opt)
    {
        if (Advertisement.IsReady(video_ID) && !noAds)
        {
            Advertisement.Show(video_ID, opt);
        }
        else
        {
            Debug.Log("Video ad not ready at the moment! Please try again later!");
        }
    }

    public static void ShowInterstitialAd(ShowOptions opt)
    {
        if (Advertisement.IsReady() && !noAds)
        {
            Advertisement.Show(opt);
        }
        else
        {
            Debug.Log("Interstitial ad not ready at the moment! Please try again later!");
        }
    }

    public static void ShowBanner()
    {
        if (noAds)
        {
            HideBanner();
        }
        else
        {
            ShowBannerInternally();
        }
    }

    public static void HideBanner()
    {
        Advertisement.Banner.Hide();
    }

    private static void ShowBannerInternally()
    {
        if (noAds)
        {
            HideBanner();
        }
        else
        {
            Advertisement.Banner.SetPosition(BannerPosition.BOTTOM_CENTER);
            Advertisement.Banner.Show(banner_ID);
        }
    }

    public static void ShowRewardedAd(ShowOptions opt)
    {
        if (Advertisement.IsReady(rewarded_ID))
        {
            Advertisement.Show(rewarded_ID, opt);
        }
        else
        {
            Debug.Log("Rewarded video ad not ready at the moment! Please try again later!");
        }
    }

    public static void DisableAds()
    {
        noAds = true;

        //Stuff de gameManager para desactivar anuncios
    }
}
