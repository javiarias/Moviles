using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class BoardManager : MonoBehaviour
{  

    public Camera _cam;
    
    public float _camAdjustment = 0.5f;

    [HideInInspector]
    public int _rows = 0;
    [HideInInspector]
    public int _columns = 0;

    /// <summary>
    /// Prefab de las tiles
    /// </summary>
    public Tile _tilePrefab;

    /// <summary>
    /// Prefab del jugador
    /// </summary>
    public PlayerMovement _playerPrefab;

    /// <summary>
    /// Instancia del jugador
    /// </summary>
    PlayerMovement _player;

    /// <summary>
    /// Array de tiles. El array no es multidimensional, porque (al parecer) son menos eficientes
    /// </summary>
    Tile[] _tiles = null;

    /// <summary>
    /// Array de direcciones de hints, comenzando desde el inicio. Como es mucho menor que el de tiles, no nos importa que sea multidimensional
    /// La primera dimensión determina qué tercio de la solución se muestra
    /// </summary>
    GameUtils.Direction[,] _hints;

    /// <summary>
    /// Vector de posición de inicio
    /// </summary>
    Vector2 _startPos;

    /// <summary>
    /// Vector de posición de la última hint activada, se asume map.s como el inicial
    /// </summary>
    Vector2 _lastHintPos;

    /// <summary>
    /// Función que arregla la posición del nivel en función de su tamaño y el tamaño de pantalla
    /// </summary>
    void FixPosition()
    {
        //Esto centra el tablero en la cámara
        Vector2 nuPos = new Vector2((_columns - 1) / -2.0f, (_rows - 1) / 2.0f);
        transform.position = nuPos;
        
        if (Screen.width > Screen.height)
        {
            float unitsPerPixel = (float)_rows / (float)Screen.height;

            float desiredHalfHeight = unitsPerPixel * Screen.width * _camAdjustment;

            _cam.orthographicSize = desiredHalfHeight;
        }
        else if (Screen.width < Screen.height)
        {
            float unitsPerPixel = (float)_rows / (float)Screen.width;

            float desiredHalfHeight = unitsPerPixel * Screen.height * _camAdjustment;

            _cam.orthographicSize = desiredHalfHeight;
        }
        else
        {
            float unitsPerPixel = (float)_rows / (float)Screen.width;

            float desiredHalfHeight = unitsPerPixel * Screen.height * _camAdjustment * 1.25f;

            _cam.orthographicSize = desiredHalfHeight;
        }
    }

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
                Tile obj = Instantiate<Tile>(_tilePrefab, transform);

                obj.gameObject.transform.localPosition = new Vector2(i, -j);


                int id = (i + (j * _columns));
                _tiles[id] = obj;

                obj.gameObject.name = i.ToString() + ", " + j.ToString();
            }
        }

        FixPosition();
    }

    /// <summary>
    /// Método que limpia el tablero
    /// </summary>
    void Clean()
    {
        if (_player)
            Destroy(_player.gameObject);

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

    /// <summary>
    /// Función de cargado de nivel
    /// </summary>
    /// <param name="map">Objeto Map del nivel</param>
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

        _player = Instantiate<PlayerMovement>(_playerPrefab, gameObject.transform);
        _startPos = new Vector2(map.s.x, -(_rows - map.s.y - 1));
        _lastHintPos = new Vector2(map.s.x, map.s.y);
        _player.transform.Translate(_startPos);
        _player._boardManager = this;

        PrepareHints(map.h, map.f);
    }

    /// <summary>
    /// Para reiniciar el nivel, pero no rehacerlo. Mantiene las hints y eso
    /// </summary>
    /// <param name="s">El tile en el que empieza el jugador</param>
    public void RestartLevel()
    {
        if (_player)
            Destroy(_player.gameObject);

        _player = Instantiate(_playerPrefab, gameObject.transform);
        _player.transform.Translate(_startPos);
        _player._boardManager = this;

        foreach (Tile t in _tiles)
            t.Reset();
    }

    /// <summary>
    /// Función para transformar el array pista que viene en Map a direcciones para poder generar mejor las pistas
    /// </summary>
    /// <param name="h">El array pista en cuestión</param>
    /// <param name="f">El tile donde se encuentra la meta, dado que no lo incluye el array</param>
    void PrepareHints(JSONTile[] h, JSONTile f)
    {
        int hintDiv = (int)Mathf.Ceil(h.Length / 3.0f) + 1;
        _hints = new GameUtils.Direction[3, hintDiv];

        Vector2 prevTile = _lastHintPos;

        int i;

        for(i = 0; i < h.Length; i++)
        {
            Vector2 dirVec = new Vector2(h[i].x, h[i].y) - prevTile;

            GameUtils.Direction dir = GameUtils.Direction.NONE;

            if (dirVec.x < 0)
                dir = GameUtils.Direction.LEFT;
            else if (dirVec.x > 0)
                dir = GameUtils.Direction.RIGHT;
            else if (dirVec.y < 0)
                dir = GameUtils.Direction.DOWN;
            else if (dirVec.y > 0)
                dir = GameUtils.Direction.UP;

            prevTile = new Vector2(h[i].x, h[i].y);

            int which = i / (hintDiv);
            int id = i % (hintDiv);

            _hints[which, id] = dir;
        }

        //se genera también la dirección a la meta, para que quede completo
        Vector2 dirVec_ = new Vector2(f.x, f.y) - prevTile;

        GameUtils.Direction dir_ = GameUtils.Direction.NONE;

        if (dirVec_.x < 0)
            dir_ = GameUtils.Direction.LEFT;
        else if (dirVec_.x > 0)
            dir_ = GameUtils.Direction.RIGHT;
        else if (dirVec_.y < 0)
            dir_ = GameUtils.Direction.DOWN;
        else if (dirVec_.y > 0)
            dir_ = GameUtils.Direction.UP;

        int which_ = i / (hintDiv);
        int id_ = i % (hintDiv);

        _hints[which_, id_] = dir_;

        _lastHintPos = _startPos;
    }

    /// <summary>
    /// Función que simplifica la generación de paredes
    /// </summary>
    /// <param name="w">Pared a generar</param>
    void MakeWall(JSONWall w)
    {
        //al parecer algunos niveles invierten o y d, por lo que nos aseguramos de que start siempre tenga el de menor valor y end el de mayor
        int x_start = Mathf.Min(w.o.x, w.d.x);
        int y_start = Mathf.Min(w.o.y, w.d.y);
        int x_end = Mathf.Max(w.o.x, w.d.x);
        int y_end = Mathf.Max(w.o.y, w.d.y);

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

    /// <summary>
    /// Función que activa las pistas
    /// </summary>
    /// <param name="h">Número de pista a activar</param>
    public void ActivateHint(int h)
    {
        if(h <= 3)
        {
            for(int i = 0; i < _hints.GetLength(1); i++)
            {
                Tile t = GetTile((int)_lastHintPos.x, (int)_lastHintPos.y);

                GameUtils.Direction dir = _hints[h, i];

                t.SetHintPath(dir, true);

                switch(dir)
                {
                    case GameUtils.Direction.UP:
                        _lastHintPos += Vector2.up;
                        break;

                    case GameUtils.Direction.DOWN:
                        _lastHintPos += Vector2.down;
                        break;

                    case GameUtils.Direction.LEFT:
                        _lastHintPos += Vector2.left;
                        break;

                    case GameUtils.Direction.RIGHT:
                        _lastHintPos += Vector2.right;
                        break;

                    default:
                        break;
                }
            }
        }
    }
}
