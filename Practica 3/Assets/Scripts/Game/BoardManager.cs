using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class BoardManager : MonoBehaviour
{
    private static BoardManager _instance;

    private void Start()
    {
        if (!_instance)
            _instance = this;
    }

    public static BoardManager Instance()
    {
        return _instance;
    }

    public int _rows = 0;
    public int _columns = 0;

    /// <summary>
    /// Prefab de las tiles
    /// </summary>
    public GameObject _tilePrefab;

    /// <summary>
    /// Prefab del jugador
    /// </summary>
    public GameObject _playerPrefab;

    /// <summary>
    /// Instancia del jugador
    /// </summary>
    GameObject _player;

    /// <summary>
    /// Array de tiles. El array no es multidimensional, porque (al parecer) son menos eficientes
    /// </summary>
    Tile[] _tiles = null;

    /// <summary>
    /// Método para obtener el array de Tiles
    /// </summary>
    /// <returns>Array de Tiles del tablero</returns>
    public Tile[] GetAllTiles()
    {
        return _tiles;
    }

    /// <summary>
    /// Método para obtener una tile específica
    /// </summary>
    /// <returns>La tile en la posición especificada</returns>
    public Tile GetTile(int x, int y)
    {
        int id_ = XY_To_Index(x, y);

        return _tiles[id_];
    }

    /// <summary>
    /// Este método prepara el tablero instanciando los objetos Tile, pero sin inicializarlos según el JSON
    /// </summary>
    void PrepareBoard()
    {
        Clean();

        //Generación inicial de tiles (Solo la clase, no el contenido)
        _tiles = new Tile[_rows * _columns];

        for (int i = 0; i < _columns; i++)
        {
            for (int j = 0; j < _rows; j++)
            {
                Tile obj = Instantiate(_tilePrefab, transform).GetComponent<Tile>();

                obj.gameObject.transform.localPosition = new Vector2(i, -j);


                int id = (i + (j * _columns));
                _tiles[id] = obj;

                obj.gameObject.name = i.ToString() + ", " + j.ToString();
            }
        }


        //Esto centra el tablero en la cámara
        Vector2 nuPos = new Vector2((_columns - 1) / -2.0f, (_rows - 1) / 2.0f);
        transform.position = nuPos;
    }

    /// <summary>
    /// Método que limpia el tablero
    /// </summary>
    void Clean()
    {
        if (_player)
            Destroy(_player);

        if (_tiles != null)
        {
            foreach (Tile t in _tiles)
                Destroy(t.gameObject);

            _tiles = null;
        }
    }
    

    /// <summary>
    /// Devuelve el índice de un array unidimensional (por filas) en función de las posiciones X e Y
    /// </summary>
    /// <param name="x">Posición X</param>
    /// <param name="y">Posición Y</param>
    /// <returns></returns>
    public int XY_To_Index(int x, int y)
    {
        int ret = (x + (Mathf.Abs(y) * _columns));

        return ret;
    }

    /// <summary>
    /// Devuelve el índice de un array unidimensional (por filas) en función de las posiciones X e Y
    /// </summary>
    /// <param name="x">Posición X</param>
    /// <param name="y">Posición Y</param>
    /// <returns></returns>
    public int JSON_XY_To_Index(int x, int y)
    {
        int ret = (x + ((_rows - Mathf.Abs(y) - 1) * _columns));

        return ret;
    }

    public void LoadLevel(Map map)
    {
        _rows = map.r;
        _columns = map.c;

        PrepareBoard();

        foreach (JSONWall w in map.w)
            MakeWall(w);

        foreach (JSONTile t in map.i)
            if(t.x >= 0 && t.y >= 0)
                _tiles[JSON_XY_To_Index(t.x, t.y)].SetIce(true);

        if (map.f.x >= 0 && map.f.y >= 0)
            _tiles[JSON_XY_To_Index(map.f.x, map.f.y)].SetGoal(true);

        foreach (JSONTile t in map.e) { }
        foreach (JSONTile t in map.t) { }

        _player = Instantiate(_playerPrefab, gameObject.transform);
        _player.transform.Translate(new Vector2(map.s.x, -(_rows - map.s.y - 1)));
    }

    void MakeWall(JSONWall w)
    {
        //al parecer algunos niveles invierten o y d, por lo que nos aseguramos de que start siempre tenga el de menor valor y end el de mayor
        int x_start = Mathf.Min(w.o.x, w.d.x);
        int y_start = Mathf.Min(w.o.y, w.d.y);
        int x_end = Mathf.Max(w.o.x, w.d.x);
        int y_end = Mathf.Max(w.o.y, w.d.y);

        //ESTO ES NECESARIO!
        //En el JSON, las tiles se miran desde abajo hacia arriba, mientras que en UNITY va al revés
        //Por comodidad, se hace ahora la transformación en vez de aplicarla constantemente
        //int y_aux = y_end;
        //y_end = _rows - y_start;
        //y_start = _rows - y_aux;

        if (x_start == x_end)
        {
            for (int y = y_start; y < y_end; y++)
            {
                if(x_start < _columns)
                    _tiles[JSON_XY_To_Index(x_start, y)].SetWall(GameUtils.Direction.LEFT, true);

                if (x_start > 0)
                    _tiles[JSON_XY_To_Index(x_start - 1, y)].SetWall(GameUtils.Direction.RIGHT, true);
            }
        }
        else if (y_start == y_end)
        {
            for (int x = x_start; x < x_end; x++)
            {
                if(y_start < _rows)
                    _tiles[JSON_XY_To_Index(x, y_start)].SetWall(GameUtils.Direction.DOWN, true);

                if (y_start > 0)
                    _tiles[JSON_XY_To_Index(x, y_start - 1)].SetWall(GameUtils.Direction.UP, true);
            }
        }
    }



    void DebugBoard()
    {
        int x = UnityEngine.Random.Range(0, _rows);
        int y = UnityEngine.Random.Range(0, _columns);

        int id_ = XY_To_Index(x, y);

        Debug.Log("Goal should be at: " + new Vector2(x, y));

        _tiles[id_].SetGoal(true);

        foreach (Tile obj in _tiles)
        {
            System.Array A = System.Enum.GetValues(typeof(GameUtils.Direction));
            GameUtils.Direction V = (GameUtils.Direction)A.GetValue(UnityEngine.Random.Range(0, A.Length));

            obj.SetWall(V, true);

            obj.SetIce(true);
        }
    }
}
