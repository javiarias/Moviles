using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LevelManager : MonoBehaviour
{

    public Text _levelText;
    public Text _hintText;

    public BoardManager _boardManager;

    public GameObject _pauseMenu;
    public GameObject _winMenu;
    public GameObject _hintMenu;

    public Button[] _buttons;

    Map _map;

    int _hintsUsed = 0;

    public void LoadLevel(string json)
    {
        if (_winMenu)
            _winMenu.SetActive(false);

        _map = Map.JSON_To_Map(json);

        if (_boardManager)
        {
            _hintsUsed = 0;

            _boardManager.LoadLevel(_map);

            if (_levelText)
                _levelText.text = GameManager.Instance().GetPackName() + " - " + (1 + GameManager.Instance().GetLevel()).ToString();

            UpdateHintTxt();
        }
    }

    public void UseHint()
    {
        if (_hintsUsed < 3)
        {
            if (_boardManager)
                _boardManager.ActivateHint(_hintsUsed);

            _hintsUsed++;

            UpdateHintTxt();
        }
    }

    public void UpdateHintTxt()
    {
        if (_hintText)
            _hintText.text = GameManager.Instance().GetHints().ToString();
    }

    public void ToggleHintMenu()
    {
        if (_hintsUsed < 3)
        {
            GameManager.Instance().Pause();

            if (_hintMenu)
                _hintMenu.SetActive(!_hintMenu.activeSelf);
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
        if (_pauseMenu)
            _pauseMenu.SetActive(pause);

        if (_buttons != null)
            foreach (Button b in _buttons)
                b.enabled = !pause;
    }

    public void LevelFinished()
    {
        if (_winMenu)
            _winMenu.SetActive(true);
    }
}
