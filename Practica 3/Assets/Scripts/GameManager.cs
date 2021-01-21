using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameManager : MonoBehaviour
{
    public LevelManager _levelManager;

    public int packToPlay;
    public int levelToPlay;

    int _hints = 0;

    private int[] _completedLevel;

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

            _instance.LoadNewLevel();

            DestroyImmediate(gameObject);

            return;
        }
        else
        {
            _instance = this;
            DontDestroyOnLoad(gameObject); //Persistente entre escenas

            _completedLevel = new int[_levelPacks.Length];

            for (int i = 0; i < _levelPacks.Length; i++)
            {
                _completedLevel[i] = 0;
            }
        }
    }

    // Update is called once per frame
    void Update()
    {
        if (activateRestart)
        {
            activateRestart = !activateRestart;
            LoadNewLevel();
        }
    }

    public int getLevelCompleted(int pack)
    {
        return _completedLevel[pack];
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

        if (_levelManager)
            _levelManager.Pause(_isPaused);
    }

    public void StartGame(int pack, int level)
    {
        packToPlay = pack;
        levelToPlay = level;

        SceneManager.LoadSceneAsync("GameScene");
    }

    public void BackToMenu()
    {
        if (_isPaused)
            Pause();

        SceneManager.LoadSceneAsync("MainMenu");
    }

    public void LoadNewLevel()
    {
        if (_levelManager)
        {
            _levelManager.LoadLevel(_levelPacks[packToPlay]._levels[levelToPlay].text);
        }
    }

    public void RestartLevel()
    {
        if (_levelManager)
        {
            _levelManager.RestartLevel(_levelPacks[packToPlay]._levels[levelToPlay].text);
        }
    }

    public void UseHint()
    {
        if (_hints <= 0)
        {

        }
        else
        {
            _hints--;

            if (_levelManager)
            {
                _levelManager.UseHint();
            }
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
        if (levelToPlay > _completedLevel[packToPlay])
            _completedLevel[packToPlay] = levelToPlay;

        levelToPlay++;
        if(levelToPlay >= _levelPacks[packToPlay]._levels.Length)
        {
            levelToPlay = 0;
            packToPlay = (packToPlay + 1) % _levelPacks.Length;
        }

        IEnumerator coroutine = Wait(0.5f);

        AdsManager.ShowInterstitialAd(); //No se si va antes o despues, pero eso

        StartCoroutine(coroutine);
    }

    IEnumerator Wait(float waitTime)
    {
        yield return new WaitForSeconds(waitTime);
        LoadNewLevel();
    }
}
