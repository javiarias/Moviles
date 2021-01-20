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

    Map _map;

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
            _boardManager.LoadLevel(_map);

            if (_levelText)
                _levelText.text = GameManager.Instance().GetPackName() + " - " + (1 + GameManager.Instance().GetLevel()).ToString();
            if (_hintText)
                _hintText.text = GameManager.Instance().GetHints().ToString();
        }
    }
}
