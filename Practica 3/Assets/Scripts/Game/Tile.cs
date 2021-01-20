using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class Tile : MonoBehaviour
{
    public int _upCounter = 0;
    public int _downCounter = 0;
    public int _leftCounter = 0;
    public int _rightCounter = 0;

    public bool _isIce { get; private set; } = false;
    public bool _isGoal { get; private set; } = false;
    public bool _isStart { get; private set; } = false;

    [Tooltip("Sprite de hielo")]
    public SpriteRenderer _ice;

    [Tooltip("Sprite de meta")]
    public SpriteRenderer _goal;

    [Tooltip("Sprite de comienzo")]
    public SpriteRenderer _start;

    [Tooltip("Sprite de pared superior")]
    public SpriteRenderer _wallUp;

    [Tooltip("Sprite de pared inferior")]
    public SpriteRenderer _wallDown;

    [Tooltip("Sprite de pared izquierda")]
    public SpriteRenderer _wallLeft;

    [Tooltip("Sprite de pared derecha")]
    public SpriteRenderer _wallRight;

    [Tooltip("Sprite de camino de jugador hacia arriba")]
    public SpriteRenderer _playerUp;

    [Tooltip("Sprite de camino de jugador hacia abajo")]
    public SpriteRenderer _playerDown;

    [Tooltip("Sprite de camino de jugador hacia izquierda")]
    public SpriteRenderer _playerLeft;

    [Tooltip("Sprite de camino de jugador hacia derecha")]
    public SpriteRenderer _playerRight;

    [Tooltip("Sprite de camino de hint hacia arriba")]
    public SpriteRenderer _hintUp;

    [Tooltip("Sprite de camino de hint hacia abajo")]
    public SpriteRenderer _hintDown;

    [Tooltip("Sprite de camino de hint hacia izquierda")]
    public SpriteRenderer _hintLeft;

    [Tooltip("Sprite de camino de hint hacia derecha")]
    public SpriteRenderer _hintRight;

    public void SetIce(bool ice)
    {
        _isIce = ice;
        _ice.enabled = ice;
    }

    public void SetGoal(bool goal)
    {
        _isGoal = goal;
        _goal.enabled = goal;
    }

    public void SetStart(bool start)
    {
        _isStart = start;
        _start.enabled = start;
    }

    public void SetWall(GameUtils.Direction dir, bool wall)
    {
        switch (dir)
        {
            case GameUtils.Direction.UP:
                _wallUp.enabled = wall;
                break;

            case GameUtils.Direction.DOWN:
                _wallDown.enabled = wall;
                break;

            case GameUtils.Direction.LEFT:
                _wallLeft.enabled = wall;
                break;

            case GameUtils.Direction.RIGHT:
                _wallRight.enabled = wall;
                break;

            default:
                break;
        }
    }

    /// <summary>
    /// Activa o desactiva el sprite de camino del jugador, teniendo en cuenta cuántas veces se ha pasado en una dirección por ese tile
    /// </summary>
    /// <param name="dir"></param>
    /// <param name="path"></param>
    public void SetPlayerPath(GameUtils.Direction dir, bool path)
    {
        switch (dir)
        {
            case GameUtils.Direction.UP:
                if (!path)
                {
                    _upCounter--;

                    if (_upCounter < 0)
                        _upCounter = 0;
                }

                if (_upCounter == 0)
                    _playerUp.enabled = path;

                if (path)
                {
                    _upCounter++;
                }
                break;

            case GameUtils.Direction.DOWN:
                if (!path)
                {
                    _downCounter--;

                    if (_downCounter < 0)
                        _downCounter = 0;
                }

                if (_downCounter == 0)
                    _playerDown.enabled = path;

                if (path)
                {
                    _downCounter++;
                }
                break;

            case GameUtils.Direction.LEFT:
                if (!path)
                {
                    _leftCounter--;

                    if (_leftCounter < 0)
                        _leftCounter = 0;
                }

                if (_leftCounter == 0)
                    _playerLeft.enabled = path;

                if (path)
                {
                    _leftCounter++;
                }
                break;

            case GameUtils.Direction.RIGHT:
                if (!path)
                {
                    _rightCounter--;

                    if(_rightCounter < 0)
                        _rightCounter = 0;
                }

                if (_rightCounter == 0)
                    _playerRight.enabled = path;

                if (path)
                {
                    _rightCounter++;
                }
                break;

            default:
                break;
        }
    }

    public void SetHintPath(GameUtils.Direction dir, bool path)
    {
        switch (dir)
        {
            case GameUtils.Direction.UP:
                _hintUp.enabled = path;
                break;

            case GameUtils.Direction.DOWN:
                _hintDown.enabled = path;
                break;

            case GameUtils.Direction.LEFT:
                _hintLeft.enabled = path;
                break;

            case GameUtils.Direction.RIGHT:
                _hintRight.enabled = path;
                break;

            default:
                break;
        }
    }

    /// <summary>
    /// Función que devuelve la cantidad de paredes en una tile
    /// </summary>
    /// <returns>El número de paredes en un tile</returns>
    public int GetWalls()
    {
        int w = 0;

        w += _wallUp.enabled ? 1 : 0;
        w += _wallDown.enabled ? 1 : 0;
        w += _wallLeft.enabled ? 1 : 0;
        w += _wallRight.enabled ? 1 : 0;

        return w;
    }

    public List<GameUtils.Direction> GetDirections()
    {
        List<GameUtils.Direction> dirs = new List<GameUtils.Direction>();

        if (!_wallUp.enabled)
            dirs.Add(GameUtils.Direction.UP);
        if (!_wallDown.enabled)
            dirs.Add(GameUtils.Direction.DOWN);
        if (!_wallLeft.enabled)
            dirs.Add(GameUtils.Direction.LEFT);
        if (!_wallRight.enabled)
            dirs.Add(GameUtils.Direction.RIGHT);

        return dirs;
    }

    /// <summary>
    /// Comprueba si puedes moverte dentro de una Tile en la dirección dada.
    /// </summary>
    /// <param name="dir">Dirección en la cual se desea moverse</param>
    /// <returns></returns>
    public bool CheckDirectionMovement(GameUtils.Direction dir)
    {
        bool ret = false;

        switch(dir)
        {
            case GameUtils.Direction.UP:
                ret = !_wallUp.enabled;
                break;

            case GameUtils.Direction.DOWN:
                ret = !_wallDown.enabled;
                break;

            case GameUtils.Direction.LEFT:
                ret = !_wallLeft.enabled;
                break;

            case GameUtils.Direction.RIGHT:
                ret = !_wallRight.enabled;
                break;

            default:
                break;
        }

        return ret;
    }

    private void Start()
    {
        Color c = GameManager.Instance().GetPackColor();

        _playerUp.color = c;
        _playerDown.color = c;
        _playerLeft.color = c;
        _playerRight.color = c;
        _goal.color = c;

        c = GameManager.Instance().GetPackHintColor();

        _hintUp.color = c;
        _hintDown.color = c;
        _hintLeft.color = c;
        _hintRight.color = c;
    }
}
