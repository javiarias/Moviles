using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class Tile : MonoBehaviour
{

    bool _isIce = false;
    bool _isGoal = false;
    bool _isStart = false;

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

    public void SetPlayerPath(GameUtils.Direction dir, bool path)
    {
        switch (dir)
        {
            case GameUtils.Direction.UP:
                _playerUp.enabled = path;
                break;

            case GameUtils.Direction.DOWN:
                _playerDown.enabled = path;
                break;

            case GameUtils.Direction.LEFT:
                _playerLeft.enabled = path;
                break;

            case GameUtils.Direction.RIGHT:
                _playerRight.enabled = path;
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

    private void Start()
    {
#if UNITY_EDITOR
#endif
    }
}
