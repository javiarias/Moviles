using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Clase serializable para cargar un mapa de json
/// </summary>
[System.Serializable]
public class Map
{
    public static Map JSON_To_Map(string json)
    {
        return JsonUtility.FromJson<Map>(json);
    }

    public int r;
    public int c;
    public JSONTile s;
    public JSONTile f;
    public JSONTile[] h;
    public JSONWall[] w;
    public JSONTile[] i;
    public JSONTile[] e;
    public JSONTile[] t;
}


[System.Serializable]
public class JSONWall
{
    public JSONTile o;
    public JSONTile d;
}


[System.Serializable]
public class JSONTile
{
    public int x = 0;
    public int y = 0;
}
