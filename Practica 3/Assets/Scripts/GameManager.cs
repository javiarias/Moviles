using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

/// <summary>
/// Clase GameManager
/// </summary>
public class GameManager : MonoBehaviour
{
    public LevelManager _levelManager;

    public int _currentPack;
    public int _currentLevel;

    public int _hints = 0;

    private int[] _completedLevel;

    bool _isPaused = false;
    
    private static GameManager _instance;

    private SaveDataFull _gameSave;

    public static GameManager Instance()
    {
        return _instance;
    }

    public bool IsPaused()
    {
        return _isPaused;
    }

    /// <summary>
    /// Aquí se realiza toda la gestión de instancias, cargado de partida y puesta a punto de las opciones de rendimiento. Se realiza en Awake para anteponerse a todo
    /// </summary>
    private void Awake()
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

            LoadData();

            QualitySettings.vSyncCount = 0;   // Deshabilitamos el vSync
            Application.targetFrameRate = 60; // Forzamos un máximo de 60 fps.
        }
    }

    public LevelPackage[] _levelPacks;

    public int GetLevelCompleted(int pack)
    {
        return _completedLevel[pack];
    }

    public int GetLevelTotal(int pack)
    {
        return _levelPacks[pack]._levels.Length;
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
        _currentPack = pack;
        _currentLevel = level;

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
            _levelManager.LoadLevel(_levelPacks[_currentPack]._levels[_currentLevel].text);
        }
    }

    public void RestartLevel()
    {
        if (_levelManager)
        {
            _levelManager.RestartLevel();
        }
    }

    /// <summary>
    /// Función que usa una hint, empleado solo durante el juego. Si no hay hints, se activa un menú que permite ver un anuncio
    /// </summary>
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
        return _levelPacks[_currentPack]._colorScheme;
    }

    public Color GetPackHintColor()
    {
        return _levelPacks[_currentPack]._hintColorScheme;
    }

    public string GetPackName()
    {
        return _levelPacks[_currentPack]._packName;
    }

    public int GetLevel()
    {
        return _currentLevel;
    }

    public int GetHints()
    {
        return _hints;
    }

    /// <summary>
    /// Función que se llama al acabar un nivel. Si es el último, te envía al menú principal. Si tiene que actualizar el último nivel completado, también guarda los datos
    /// </summary>
    public void LevelFinished()
    {
        _currentLevel++;

        if (_currentLevel > _completedLevel[_currentPack])
        {
            _completedLevel[_currentPack] = _currentLevel;
            SaveData();
        }

        if (_currentLevel >= _levelPacks[_currentPack]._levels.Length)
        {
            BackToMenu();
        }
        else
        {
            Pause();

            _levelManager.LevelFinished();
        }
    }

    /// <summary>
    /// Función que genera el anuncio entre niveles, empleando un callback para cargar el siguiente al acabar el anuncio
    /// </summary>
    public void NextLevel()
    {
        AdsManager.ShowInterstitialAd(new UnityEngine.Advertisements.ShowOptions { resultCallback = LoadNewLevelAd }); //No se si va antes o despues, pero eso
    }

    /// <summary>
    /// Función callback que se llama cuando acaba el anuncio que hay tras completar un nivel
    /// </summary>
    /// <param name="show">Enum con el resultado del anuncio, necesario para el callback</param>
    public void LoadNewLevelAd(UnityEngine.Advertisements.ShowResult show)
    {
        LoadNewLevel();
    }

    /// <summary>
    /// Función callback que se llama cuando acaba el anuncio con premio del menú principal, guarda los datos para que se guarde la hint.
    /// </summary>
    /// <param name="show">Enum con el resultado del anuncio, necesario para el callback</param>
    public void AddHintAd(UnityEngine.Advertisements.ShowResult show)
    {
        AddHint();
        SaveData();
    }

    /// <summary>
    /// Función callback que se llama cuando acaba el anuncio con premio al tratar de usar una hint sin tener más hints.
    /// Se realiza UseHint() dos veces para ocultar el menú
    /// </summary>
    /// <param name="show">Enum con el resultado del anuncio, necesario para el callback</param>
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

    /// <summary>
    /// Clase que contiene los datos de guardado
    /// </summary>
    [Serializable]
    protected class SaveDataFull
    {
        public int hints;
        public int[] packs = new int[50]; //Es un valor arbitrario, pero siendo packs, no se qué valor poner para no quedarnos cortos
    }

    /// <summary>
    /// Clase que contiene los datos de guardado, pero serializados
    /// </summary>
    protected class SaveDataHash
    {
        public int hashCode;
        public string json;
    }

    /// <summary>
    /// Función de guardado de datos
    /// </summary>
    public void SaveData()
    {
        _gameSave.hints = GameManager._instance._hints;
        _gameSave.packs = GameManager._instance._completedLevel;

        string jsonified = JsonUtility.ToJson(_gameSave);
        SaveDataHash hash = new SaveDataHash();
        hash.json = jsonified;
        hash.hashCode = jsonified.GetHashCode();

        string aux = JsonUtility.ToJson(hash);
        PlayerPrefs.SetString("SaveData", aux);
    }

    /// <summary>
    /// Función de cargado de datos
    /// </summary>
    public void LoadData()
    {
        string verification = PlayerPrefs.GetString("SaveData");
        SaveDataHash hash = JsonUtility.FromJson<SaveDataHash>(verification);

        if (hash != null && hash.hashCode == hash.json.GetHashCode())
        {
            _gameSave = JsonUtility.FromJson<SaveDataFull>(hash.json);
            GameManager.Instance()._hints = _gameSave.hints;
            GameManager.Instance()._completedLevel = _gameSave.packs;
        }
        else //Si haces trampas o no hay nada
        {
            GameManager._instance._hints = 0;

            for (int i = 0; i < GameManager._instance._completedLevel.Length; i++)
            {
                GameManager.Instance()._completedLevel[i] = 0;
            }

            _gameSave = new SaveDataFull();
        }
    }
}
