using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameManager : MonoBehaviour
{
    public LevelManager _levelManager;

    public int packToPlay;
    public int levelToPlay;

    int _hints = 0;

#if UNITY_EDITOR
    public bool activateRestart = true;
#endif

    bool _isPaused = false;
    
    private static GameManager _instance;

    public static GameManager Instance()
    {
        return _instance;
    }

    public bool IsPaused()
    {
        return _isPaused;
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

    public void Pause()
    {
        _isPaused = !_isPaused;

        if (_isPaused)
        {
            Time.timeScale = 0;
        }
        else
        {
            Time.timeScale = 1;
        }
    }

    public void StartNewScene()
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

    public string GetPackName()
    {
        return _levelPacks[packToPlay]._packName;
    }

    public int GetLevel()
    {
        return levelToPlay;
    }

    public int GetHints()
    {
        return _hints;
    }

    public void LevelFinished()
    {
        levelToPlay++;
        if(levelToPlay >= _levelPacks[packToPlay]._levels.Length)
        {
            levelToPlay = 0;
            packToPlay = (packToPlay + 1) % _levelPacks.Length;
        }

        IEnumerator coroutine = Wait(0.5f);
        StartCoroutine(coroutine);
    }

    IEnumerator Wait(float waitTime)
    {
        yield return new WaitForSeconds(waitTime);
        StartNewScene();
    }
}
