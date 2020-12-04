package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;

public class Logic implements com.OffTheLine.common.Logic
{


    public enum GameState
    {
        MAINMENU,
        GAME
    }

    //Elementos
    Engine _e;
    Level _level = null;
    Player _player = null;
    Menu _menu = null;

    //Lives
    int lives;
    int maxLives;

    //Score
    float score = 0;
    float maxScore;

    //UI
    float UI_LivesPadding = 15;
    float UI_Y = 60;
    Vector2D UI_LivesPosRight = new Vector2D(0, UI_Y);
    ArrayList<Square> UI_Lives;

    //Otros
    GameState _state;
    float deathThreshold = 20;
    int currentLvl = 1;
    boolean lost = false;
    float delayChangeLevel = 1.0f;
    float delayDeath = 1.0f;
    ArrayList<Item> itemsToDestroy = new ArrayList<Item>();
    String _path;
    float playerSpeed;

    Font _gameFont = null;
    Font _menuFont = null;

    public Logic(Engine e, String path)
    {
        _e = e;
        _path = path;

        /*_menu = new Menu(true, this);
        _menu.addButton(0, 100, "Easy",70, 30);
        _menu.addButton(0, 180, "Hard",75, 30);*/
    }

    @Override
    public void init()
    {
        try
        {
            _gameFont = _e.getGraphics().newFont(_path + "BungeeHairline-Regular.ttf", 20, false);
            _menuFont = _e.getGraphics().newFont(_path + "Bungee-Regular.ttf", 20, false);
        }
        catch (Exception e) {}

        gameStart(true);
    }

    public void gameStart(boolean easyMode)
    {
        setEasyMode();

        _player = new Player(playerSpeed);

        readyGameUI();
        _state = GameState.GAME;
        _level = new Level(_path + "levels.json", _e);

        //currentLvl = 0;

        loadCurrentLevel();
    }

    private void readyGameUI()
    {
        Vector2D pos = UI_LivesPosRight;

        UI_Lives = new ArrayList<Square>();
        for (int i = 0; i < maxLives; i++) {
            Square s = new Square(new Vector2D(pos), 0xFF0088FF);
            s._size = 6;
            s._thickness = 2;
            UI_Lives.add(s);

            pos.x -= (UI_LivesPadding + s._size);
        }
    }

    public void lostLife()
    {
        lives--;
        if(lives >= 0)
        {
            Cross x = new Cross(UI_Lives.get(maxLives - lives - 1), 0xFFFF1111);

            UI_Lives.set(maxLives - lives - 1, x);
        }
    }

    @Override
    public void update(double deltaTime)
    {
        if (_state == GameState.MAINMENU)
        {
            ArrayList<Input.TouchEvent> ls = new ArrayList<Input.TouchEvent>(_e.getInput().getTouchEvents());

            _menu.update(deltaTime, ls);
        }
        else if (_state == GameState.GAME)
        {
            if (!checkLevelCompleted(deltaTime)) {
                ArrayList<Input.TouchEvent> ls = new ArrayList<Input.TouchEvent>(_e.getInput().getTouchEvents());

                checkPlayerCollision();

                _level.update(deltaTime, ls);
                _player.update(deltaTime, ls);

                destroyItems();

                if (lost)
                    destroyPlayer(deltaTime);
            } else {
                changeLevel();
            }
        }
        //else //if (_state == state.LOST)
    }

    @Override
    public void render(Graphics g)
    {
        if (_menu != null)
        {
            g.save();
            _menu.render(g);
            g.restore();
        }
        else
        {
            g.save();
            paintGameUI(g);
            g.restore();

            g.save();
            g.restore();

            checkPlayerCollision();

            g.translate(g.getWidth() / 2.0f, g.getHeight() / 2.0f);

            g.save();
            _level.render(g);
            g.restore();
            g.save();
            _player.render(g);
            g.restore();
        }
    }

    public void paintGameUI(Graphics g)
    {
        g.setFont(_gameFont);
        g.setColor(0xFFFFFFFF);
        g.drawText("Level " + currentLvl + " - " + _level._name, 0, UI_Y * 1.2f);

        //La UI de vidas está dibujada desde la dcha

        for (Square s : UI_Lives)
        {
            g.save();
            g.translate(g.getWidth(), 0);
            s.render(g);
            g.restore();
        }
    }

    void checkPlayerCollision()
    {
        for (Item i : _level.getItems())
        {
            if (Utils.distancePointPoint(_player.pos, i.pos) < deathThreshold)
            {
                if (!i.toDie) {
                    score++;
                    i.toDie = true;
                    itemsToDestroy.add(i);
                }
            }
        }

        for (Enemy e : _level.getEnemies())
        {
            /*if (e._length != null)
            {

            }*/
            float d = Utils.distancePointSegment(e._vertice1, e._vertice2, _player.pos);
            if (d < 10)
            {
                lost = true;
                //_player.die(); //Crear las lineas, sustituyendo al cuadrado en el render?
            }
        }

        if (_player.outOfBounds(_e.getGraphics().getHeight(), _e.getGraphics().getWidth()))
            lost = true;
    }

    void destroyItems()
    {
        for (Item i : itemsToDestroy)
        {
            if (i._dead)
            {
                _level.getItems().remove(i);
            }
        }
    }

    void destroyPlayer(double deltaTime)
    {
        if (delayDeath >= 0)
        {
            delayDeath -= deltaTime;
        }
        else
        {
            lostLife();
            _player.die(); //Animación
            changeLevel();
        }
    }

    public void changeLevel()
    {
        _level.getItems().clear();
        _level.getEnemies().clear();
        _level.getPaths().clear();

        score = 0;
        delayChangeLevel = 1.0f;
        delayDeath = 1.0f;
        lost = false;

        loadCurrentLevel();
    }

    boolean checkLevelCompleted(double deltaTime)
    {
        if (score == maxScore)
        {
            if (delayChangeLevel >= 0)
                delayChangeLevel -= deltaTime;
            else
            {
                currentLvl++;
                return true;
            }
        }
        return false;
    }

    public void loadCurrentLevel()
    {
        try { _level.loadLevel(currentLvl - 1); }
        catch (Exception E) {}

        maxScore = _level._items.size();
        _player.newLevel(_level.getPaths());
    }

    public void setEasyMode()
    {
        lives = maxLives = 10;
        playerSpeed = 250;
    }

    public void setHardMode()
    {
        lives = maxLives = 5;
        playerSpeed = 400;
    }
}