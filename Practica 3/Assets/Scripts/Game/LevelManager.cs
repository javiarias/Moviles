using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelManager : MonoBehaviour
{
    private static LevelManager _instance;

    public static LevelManager Instance()
    {
        return _instance;
    }

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
        }
    }
}
