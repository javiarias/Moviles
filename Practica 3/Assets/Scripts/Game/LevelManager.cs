using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelManager : MonoBehaviour
{
    public BoardManager _boardManager;    

    Map _map;

    public void Start()
    {
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
