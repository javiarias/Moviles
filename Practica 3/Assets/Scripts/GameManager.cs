using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameManager : MonoBehaviour
{
    public LevelManager _levelManager;

#if UNITY_EDITOR
    public int packToPlay;
    public int levelToPlay;
#endif

    static GameManager _instance;


    public LevelPackage[] _levelPacks;

    void Start()
    {
        if(_instance != null)
        {
            _instance._levelManager = _levelManager;
            DestroyImmediate(gameObject);

            StartNewScene();

            return;
        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    private void StartNewScene()
    {
        if(_levelManager)
        {
            //in-game
        }
    }
}
