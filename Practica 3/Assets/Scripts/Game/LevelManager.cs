using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LevelManager : MonoBehaviour
{
    private static LevelManager _instance;

    public static LevelManager Instance()
    {
        return _instance;
    }

    public Text _levelText;
    public Text _hintText;

    public BoardManager _boardManager;

    public GameObject pauseMenu;

    public Button[] _buttons;

    Map _map;

    int _hintsUsed = 0;

    public void Start()
    {
        if (!_instance)
            _instance = this;

        _boardManager = BoardManager.Instance();
    }

    private void Update()
    {
        if(!_boardManager)
            _boardManager = BoardManager.Instance();
    }

    public void LoadLevel(string json)
    {
        _map = Map.JSON_To_Map(json);

        if (_boardManager)
        {
            _hintsUsed = 0;

            _boardManager.LoadLevel(_map);

            if (_levelText)
                _levelText.text = GameManager.Instance().GetPackName() + " - " + (1 + GameManager.Instance().GetLevel()).ToString();
            if (_hintText)
                _hintText.text = GameManager.Instance().GetHints().ToString();
        }
    }

    public void UseHint()
    {
        if (_hintsUsed < 3)
        {
            if (_boardManager)
                _boardManager.ActivateHint(_hintsUsed);

            _hintsUsed++;

            if (_hintText)
                _hintText.text = GameManager.Instance().GetHints().ToString();
        }
    }

    public void RestartLevel(string json)
    {
        _map = Map.JSON_To_Map(json);

        if (_boardManager)
        {
            _boardManager.RestartLevel();
        }
    }

    public void Pause(bool pause)
    {
        if (pauseMenu)
            pauseMenu.SetActive(pause);

        if (_buttons != null)
            foreach (Button b in _buttons)
                b.enabled = !pause;
    }
}
