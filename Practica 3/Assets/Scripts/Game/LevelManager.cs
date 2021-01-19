using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelManager : MonoBehaviour
{
    [Tooltip("pog")]
    public BoardManager _boardManager;

    public TextAsset _level;

    // Start is called before the first frame update
    void Start()
    {
        if(_level && _boardManager)
        {

        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
