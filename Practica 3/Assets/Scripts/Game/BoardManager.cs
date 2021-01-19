using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class BoardManager : MonoBehaviour
{
    public int _rows = 0;
    public int _columns = 0;

    /// <summary>
    /// Prefab de las tiles
    /// </summary>
    public GameObject _tilePrefab;

    /// <summary>
    /// Array de tiles. El array no es multidimensional, porque (al parecer) son menos eficientes
    /// </summary>
    Tile[] _tiles = null;

    public bool activateClean = false;
    public bool activateRestart = false;

    // Start is called before the first frame update
    void Start()
    {
        PrepareBoard(_rows, _columns);
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

    /// <summary>
    /// Este método prepara el tablero instanciando los objetos Tile, pero sin inicializarlos según el JSON
    /// </summary>
    /// <param name="rows">Número de filas del tablero</param>
    /// <param name="columns">Número de columnas del tablero</param>
    void PrepareBoard(int rows, int columns)
    {

        //Esto centra el tablero en la cámara
        transform.position = Vector3.zero;
        transform.Translate(new Vector2((rows - 1) / -2.0f, (columns - 1) / -2.0f));


        //Generación inicial de tiles (Solo la clase, no el contenido)
        _tiles = new Tile[rows * columns];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                Tile obj = Instantiate(_tilePrefab, transform).GetComponent<Tile>();

                obj.gameObject.transform.localPosition = new Vector2(i, j);


                int id = XY_To_Index(i, j);
                _tiles[id] = obj;

                obj.gameObject.name = i.ToString() + ", " + j.ToString();
            }
        }


        DebugBoard();
    }

    /// <summary>
    /// Método que limpia el tablero
    /// </summary>
    void Clean()
    {
        foreach (Tile t in _tiles)
            Destroy(t.gameObject);

        _tiles = null;
    }

    
    void Update()
    {
        if (activateClean)
        {
            activateClean = !activateClean;
            Clean();
        }

        if (activateRestart)
        {
            activateRestart = !activateRestart;
            if (_tiles != null)
                Clean();
            PrepareBoard(_rows, _columns);
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
        return (x + y * _rows);
    }

    public void LoadLevel()//JsonUtility json)
    {

    }
}
