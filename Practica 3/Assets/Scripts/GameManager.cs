using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameManager : MonoBehaviour
{
    public LevelManager _levelManager;

    public int packToPlay;
    public int levelToPlay;

    public int _hints = 0;

    private int[] _completedLevel;

    bool _isPaused = false;
    
    private static GameManager _instance;

    private gameSaveData gameSaveData_;

    public static GameManager Instance()
    {
        return _instance;
    }

    public bool IsPaused()
    {
        return _isPaused;
    }

    private void Awake()
    {
        QualitySettings.vSyncCount = 0;   // Deshabilitamos el vSync
        Application.targetFrameRate = 60; // Forzamos un máximo de 60 fps.
    }

    public LevelPackage[] _levelPacks;

    void Start()
    {
        if (_instance != null)
        {
            if (_levelManager)
            {
                _instance._levelManager = _levelManager;

                _instance.LoadNewLevel();

                DestroyImmediate(gameObject);

                return;
            }
        }
        else
        {
            _instance = this;
            DontDestroyOnLoad(gameObject); //Persistente entre escenas

            _completedLevel = new int[_levelPacks.Length];
            /*
            for (int i = 0; i < _levelPacks.Length; i++)
            {
                _completedLevel[i] = 0;
            }
            */

            loadData();
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
        if (_isPaused)
            Pause();

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
            if (_levelManager)
            {
                _levelManager.ToggleHintMenu();
            }
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
        levelToPlay++;

        if (levelToPlay > _completedLevel[packToPlay])
            _completedLevel[packToPlay] = levelToPlay;

        if (levelToPlay >= _levelPacks[packToPlay]._levels.Length)
        {
            BackToMenu();
        }
        else
        {
            Pause();

            saveData(); //Tampoco se si va aqui

            _levelManager.LevelFinished();
        }
    }

    public void NextLevel()
    {
        AdsManager.ShowInterstitialAd(new UnityEngine.Advertisements.ShowOptions { resultCallback = LoadNewLevelAd }); //No se si va antes o despues, pero eso
    }

    public void LoadNewLevelAd(UnityEngine.Advertisements.ShowResult show)
    {
        LoadNewLevel();
    }

    public void AddHintAd(UnityEngine.Advertisements.ShowResult show)
    {
        AddHint();
        saveData();
    }

    public void FreeHint(UnityEngine.Advertisements.ShowResult show)
    {
        UseHint();
        AddHint();
        UseHint();
    }

    public void AddHint()
    {
        _hints++;
    }

    //SAVE DATA STUFF
    [Serializable]
    protected class gameSaveData
    {
        public int hints;
        public int[] packs = new int[50]; //Es un valor arbitrario, pero siendo packs, no se qué valor poner para no quedarnos cortos
    }

    protected class gameSaveDataHASH
    {
        public int hashCode;
        public string json;
    }

    public void saveData()
    {
        gameSaveData_.hints = GameManager._instance._hints;
        gameSaveData_.packs = GameManager._instance._completedLevel;

        string stringSaveData = JsonUtility.ToJson(gameSaveData_);
        gameSaveDataHASH hashData = new gameSaveDataHASH();
        hashData.json = stringSaveData;
        hashData.hashCode = stringSaveData.GetHashCode();

        string aux = JsonUtility.ToJson(hashData);
        PlayerPrefs.SetString("SaveData", aux);
    }

    public void loadData()
    {
        string codeToVerify = PlayerPrefs.GetString("SaveData");
        gameSaveDataHASH hashData = JsonUtility.FromJson<gameSaveDataHASH>(codeToVerify);

        if (hashData != null)
        {
            if (hashData.hashCode == hashData.json.GetHashCode())
            {
                gameSaveData gameSaveData = JsonUtility.FromJson<gameSaveData>(hashData.json);
                GameManager._instance._hints = gameSaveData.hints;
                GameManager._instance._completedLevel = gameSaveData.packs;
            }
        }
        else //Si haces trampas o no hay nada
        {
            GameManager._instance._hints = 0;

            for (int i = 0; i < GameManager._instance._completedLevel.Length; i++)
            {
                GameManager._instance._completedLevel[i] = 1; //No se que valor es por defecto, si 1 o 0
            }
        }
    }
}
