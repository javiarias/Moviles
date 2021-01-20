using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMovement : MonoBehaviour
{
    bool _swiping = false;
    bool _swipeEnd = false;
    Vector2 _lastPos;

    bool _isMoving = false;
    bool _isReturning = false;
    bool _isOnIce = false;
    GameUtils.Direction _iceDir = GameUtils.Direction.NONE;

    bool _isLerping = false;
    Vector2 _initLerp;
    Vector2 _endLerp;
    float _lerpTime = 0;
    public float _lerpTotal = 0.1f;

    Stack<GameUtils.Direction> _movementQueue;

    public SpriteRenderer _playerUp;

    public SpriteRenderer _playerDown;

    public SpriteRenderer _playerLeft;

    public SpriteRenderer _playerRight;

    public SpriteRenderer _player;

    /// <summary>
    /// Límite que determina cuánto has de deslizar el dedo para que se reconozca como movimiento
    /// </summary>
    public float swipeThreshold = 0;

    private void Start()
    {
        _movementQueue = new Stack<GameUtils.Direction>();


        Color c = GameManager.Instance().GetPackColor();

        _playerUp.color = c;
        _playerDown.color = c;
        _playerLeft.color = c;
        _playerRight.color = c;

        _player.color = c;

        Tile t = BoardManager.Instance().GetTile((int)transform.localPosition.x, (int)transform.localPosition.y);

        List<GameUtils.Direction> dirs = t.GetDirections();

        ShowDirections(dirs);
    }

    void Update()
    {
        if (!_isMoving)
        {
            //Queremos que el movimiento solo se registre una vez por cada desliz del dedo, por lo que empleamos _swipeEnd para comprobar si ya se ha realizado un movimiento
            if (Input.touchCount >= 1 && !_swipeEnd)
                HandleInput(Input.GetTouch(0));

            //Y para ahorrar tiempo, solo ejecutamos esto si es absolutamente necesario
            else if (Input.touchCount == 0 && (_swiping || _swipeEnd))
            {
                _swiping = false;
                _swipeEnd = false;
            }
        }
        else if(!_isLerping)
        {
            HandleMovement();
        }
        else
        {
            if (_lerpTime < _lerpTotal)
            {
                transform.localPosition = Vector2.Lerp(_initLerp, _endLerp, _lerpTime / _lerpTotal);

                _lerpTime += Time.deltaTime;
            }
            else
            {
                _lerpTime = 0;
                _isLerping = false;
                transform.localPosition = _endLerp;
            }
            
        }
    }

    void ShowDirections(List<GameUtils.Direction> dirs)
    {
        foreach(GameUtils.Direction d in dirs)
        {
            switch(d)
            {
                case GameUtils.Direction.UP:
                    _playerUp.enabled = true;
                    break;

                case GameUtils.Direction.DOWN:
                    _playerDown.enabled = true;
                    break;

                case GameUtils.Direction.LEFT:
                    _playerLeft.enabled = true;
                    break;

                case GameUtils.Direction.RIGHT:
                    _playerRight.enabled = true;
                    break;

                default:
                    break;
            }
        }
    }

    void HideDirections()
    {
        _playerUp.enabled = false;

        _playerDown.enabled = false;

        _playerLeft.enabled = false;

        _playerRight.enabled = false;
    }

    void HandleMovement()
    {

        Tile t = BoardManager.Instance().GetTile((int)transform.localPosition.x, (int)transform.localPosition.y);

        if (_isOnIce)
        {
            if (_isReturning)
            {
                GameUtils.Direction dir = _movementQueue.Peek();

                if((int)_iceDir + (int)_movementQueue.Peek() == 0)
                    HandleDir(_iceDir);
                else
                {
                    _isReturning = false;
                    _isMoving = false;
                    _isOnIce = false;

                    List<GameUtils.Direction> dirs = t.GetDirections();

                    ShowDirections(dirs);
                }
            }
            else
            {
                HandleDir(_iceDir);
            }
        }
        else
        {

            int walls = t.GetWalls();

            //Si la tile en la que se halla tiene 2 paredes, significa que debe seguir avanzando
            if (walls == 2)
            {
                if (_isReturning)
                {
                    if (_movementQueue.Count > 0)
                    {
                        GameUtils.Direction dir = _movementQueue.Peek();

                        HandleDir((GameUtils.Direction)(-(int)dir));
                    }
                    else
                    {
                        _isReturning = false;
                        _isMoving = false;
                        _isOnIce = false;

                        List<GameUtils.Direction> dirs = t.GetDirections();

                        ShowDirections(dirs);
                    }
                }
                else
                {
                    List<GameUtils.Direction> dirs = t.GetDirections();

                    GameUtils.Direction dir = GameUtils.Direction.NONE;

                    if ((int)dirs[0] + (int)_movementQueue.Peek() == 0)
                        dir = dirs[1];
                    else
                        dir = dirs[0];

                    HandleDir(dir);
                }
            }
            //Si no, es que o ha alcanzado un callejón sin salida o es que ha alcanzado un cruce
            else
            {
                List<GameUtils.Direction> dirs = t.GetDirections();

                ShowDirections(dirs);

                _isMoving = false;
                _isReturning = false;
            }
        }
    }

    /// <summary>
    /// Función que gestiona la dirección obtenida del input, moviendo al jugador en consecuencia
    /// </summary>
    /// <param name="dir">Dirección en la que moverse</param>
    void HandleDir(GameUtils.Direction dir)
    {
        Vector2 translate = Vector2.zero;

        Tile t = BoardManager.Instance().GetTile((int)transform.localPosition.x, (int)transform.localPosition.y);

        if (t.CheckDirectionMovement(dir))
        {
            if (_isReturning || (_movementQueue.Count > 0 && (int)dir + (int)_movementQueue.Peek() == 0))
            {
                _movementQueue.Pop();
                _isReturning = true;
            }
            else
            {
                _movementQueue.Push(dir);
                t.SetPlayerPath(dir, true);
            }

            switch (dir)
            {
                case GameUtils.Direction.UP:
                    translate = new Vector2(0, 1);
                    break;

                case GameUtils.Direction.DOWN:
                    translate = new Vector2(0, -1);
                    break;

                case GameUtils.Direction.LEFT:
                    translate = new Vector2(-1, 0);
                    break;

                case GameUtils.Direction.RIGHT:
                    translate = new Vector2(1, 0);
                    break;

                default:
                    break;
            }

            _isLerping = true;
            _lerpTime = 0;
            _initLerp = transform.localPosition;
            _endLerp = _initLerp + translate;
            //transform.Translate(translate);

            t = BoardManager.Instance().GetTile((int)_endLerp.x, (int)_endLerp.y);

            if (_isReturning)
                t.SetPlayerPath((GameUtils.Direction)(-(int)dir), false);

            if (t._isGoal)
            { 
                Debug.Log("GANASTE");
                _isMoving = false;
                _isLerping = false;
                return;
            }

            _isOnIce = t._isIce;

            if (_isOnIce)
                _iceDir = dir;
        }
        else
        {
            _isReturning = false;
            _isMoving = false;
            _isOnIce = false;
            _iceDir = GameUtils.Direction.NONE;

            List<GameUtils.Direction> dirs = t.GetDirections();

            ShowDirections(dirs);
        }
    }


    /// <summary>
    /// Función sencilla que recibe el Input.touch y gestiona el movimiento
    /// </summary>
    /// <param name="input">El input a gestionar</param>
    void HandleInput(Touch input)
    {
        if (input.deltaPosition.sqrMagnitude != 0)
        {
            if (!_swiping)
            {
                _swiping = true;
                _lastPos = input.position;
            }
            else
            {

                Vector2 dirVect = input.position - _lastPos;

                if (dirVect.sqrMagnitude > swipeThreshold)
                {
                    dirVect.Normalize();

                    _lastPos = input.position;

                    GameUtils.Direction dir = GameUtils.GetDir(dirVect);

                    _swipeEnd = true;
                    _isMoving = true;

                    HandleDir(dir);

                    HideDirections();
                }
            }
        }
        else
        {
            _swiping = false;
        }
    }

}
