using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Advertisements;

[RequireComponent(typeof(Button))]

public class showRewardedVideoButton : MonoBehaviour, IUnityAdsListener
{
    string myPlacementId = "rewardedVideo";
    Button myButton;

    // Start is called before the first frame update
    void Start()
    {
        myButton = GetComponent<Button>();
        myButton.interactable = Advertisement.IsReady(myPlacementId); // Set interactivity to be dependent on the Placement’s status:
        
        if (myButton) // Map the ShowRewardedVideo function to the button’s click listener:
            myButton.onClick.AddListener(ShowRewardedVideo); 

        Advertisement.AddListener(this); // Initialize the Ads listener

        //Advertisement.Initialize(gameId, true); //Esto no hace falta porque se hace en otro lado
    }

    public void ShowRewardedVideo()
    {
        // Check if UnityAds ready before calling Show method:
        if (Advertisement.IsReady(myPlacementId))
        {
            Advertisement.Show(myPlacementId);
        }
        else
        {
            Debug.Log("Rewarded video is not ready at the moment! Please try again later!");
        }
    }

    // When the object that subscribes to ad events is destroyed, remove the listener:
    public void OnDestroy()
    {
        Advertisement.RemoveListener(this);
    }

    public void OnUnityAdsDidError(string message)
    {
        //throw new System.NotImplementedException();

        // Log the error.
    }

    public void OnUnityAdsDidFinish(string placementId, ShowResult showResult)
    {
        // Define conditional logic for each ad completion status:
        if (showResult == ShowResult.Finished)
        {
            // Reward the user for watching the ad to completion.
        }
        else if (showResult == ShowResult.Skipped)
        {
            // Do not reward the user for skipping the ad.
        }
        else if (showResult == ShowResult.Failed)
        {
            Debug.LogWarning("The ad did not finish due to an error.");
        }
    }

    public void OnUnityAdsDidStart(string placementId)
    {
        //throw new System.NotImplementedException();

        // Optional actions to take when the end-users triggers an ad.
    }

    public void OnUnityAdsReady(string placementId)
    {
        // If the ready Placement is rewarded, show the ad:
        if (placementId == myPlacementId)
        {
            // Optional actions to take when the placement becomes ready(For example, enable the rewarded ads button)
            myButton.interactable = true;
        }
    }
}
