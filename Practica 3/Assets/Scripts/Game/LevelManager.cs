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

    /// <summary>
    /// Cargado de nivel
    /// </summary>
    /// <param name="json"></param>
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

    /// <summary>
    /// Empleo de hints, tiene en cuenta las hints usadas en un mismo nivel
    /// </summary>
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

    /// <summary>
    /// Actualizado del texto del icono de pista
    /// </summary>
    public void UpdateHintTxt()
    {
        if (_hintText)
            _hintText.text = GameManager.Instance().GetHints().ToString();
    }
    
    /// <summary>
    /// Activa/Desactiva el menú que permite ver un anuncio a cambio de usar una pista, solo si se pueden usar aún
    /// </summary>
    public void ToggleHintMenu()
    {
        if (_hintsUsed < 3)
        {
            GameManager.Instance().Pause();

            if (_hintMenu)
                _hintMenu.SetActive(!_hintMenu.activeSelf);
        }
    }

    /// <summary>
    /// Reinicio de un nivel
    /// </summary>
    public void RestartLevel()
    {
        if (_boardManager)
        {
            _boardManager.RestartLevel();
        }
    }

    /// <summary>
    /// Pausado del juego
    /// </summary>
    /// <param name="pause">Bool que indica si se ha de pausar o no</param>
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
