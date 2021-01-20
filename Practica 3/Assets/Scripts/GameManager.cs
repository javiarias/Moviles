using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameManager : MonoBehaviour
{
    public LevelManager _levelManager;

    public int packToPlay;
    public int levelToPlay;

#if UNITY_EDITOR
    public bool activateRestart = false;
#endif
    
    private static GameManager _instance;

    public static GameManager Instance()
    {
        return _instance;
    }

    public LevelPackage[] _levelPacks;

    void Start()
    {
        if (_instance != null)
        {
            _instance._levelManager = _levelManager;
            DestroyImmediate(gameObject);

            StartNewScene();

            return;
        }
        else
            _instance = this;
    }

    // Update is called once per frame
    void Update()
    {
        if (activateRestart)
        {
            activateRestart = !activateRestart;
            StartNewScene();
        }
    }

    private void StartNewScene()
    {
        if(_levelManager)
        {
            _levelManager.LoadLevel(_levelPacks[packToPlay]._levels[levelToPlay].text);
        }
    }

    public Color GetPackColor()
    {
        return _levelPacks[packToPlay]._colorScheme;
    }

    public Color GetPackHintColor()
    {
        return _levelPacks[packToPlay]._hintColorScheme;
    }
}
