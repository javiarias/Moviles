package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.awt.Color;
import java.util.ArrayList;

public class Logic implements com.OffTheLine.common.Logic {

    int _x = 0;
    int _incX = 10;

    Engine _e;
    Level _level;
    int currentLvl = 7;
    int lives;
    int maxLives = 3;
    Player _player;

    float score = 0;
    float maxScore;

    Vector2D UI_LivesPosRight;
    float UI_LivesPadding = 25;
    float UI_Y = 60;

    float umbralDistancia = 20;

    ArrayList<Square> UI_Lives;

    public boolean _centerScreen = false;

    ArrayList<Item> itemsToDestroy = new ArrayList<Item>();

    boolean lost = false;

    float delayChangeLevel = 1.0f;
    float delayDeath = 1.0f;

    //public void centerScreen(boolean bool) { _centerScreen = bool; }

    public Logic(Engine e, String path) {
        _e = e;

        _level = new Level(path, _e);

        try {
            _level.loadLevel(currentLvl - 1);
        }
        catch ( Exception E)
        {

        }

        maxScore = _level._items.size();

        _player = new Player(_level.getPaths());

        lives = maxLives;

        UI_LivesPosRight = new Vector2D(-(UI_LivesPadding * 3), UI_Y);

        Vector2D pos = UI_LivesPosRight;

        UI_Lives = new ArrayList<Square>();
        for (int i = 0; i < maxLives; i++) {
            Square s = new Square(new Vector2D(pos), 0xFF0088FF);
            s._size = 12;
            s._thicc = 2;
            UI_Lives.add(s);

            pos.x -= (UI_LivesPadding + s._size);
        }

        lostLife();
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
        if (!checkLevelCompleted(deltaTime))
        {
            //System.out.println(score);

            ArrayList<Input.TouchEvent> ls = new ArrayList(_e.getInput().getTouchEvents());

            checkPlayerCollision();

            _level.update(deltaTime, ls);
            _player.update(deltaTime, ls);

            destroyItems();

            if (lost)
                destroyPlayer(deltaTime);
        }
        else
        {
            changeLevel();
        }
    }

    @Override
    public void render(Graphics g)
    {

        g.save();
        paintUI(g);
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

    public void paintUI(Graphics g)
    {
        g.drawText(_level._name, UI_LivesPadding*3,UI_Y);
        //Valores arbitrarios de momento, ya que no puedo sacar el tamaño del texto para ajustarlo
        //Elegir fuente (la del menu y la UI no es la misma)
        //PD: Se reescala regular

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
        //Items
        for (Item i : _level.getItems())
        {
            if (Utils.distancePointPoint(_player.pos, i.pos) < umbralDistancia)
            {
                //Marcar item para destruirlo cuando acabe el update
                if (!i.toDie) {
                    score++;
                    i.toDie = true;
                    itemsToDestroy.add(i);
                }
            }
        }

        //Enemies
        for (Enemy e : _level.getEnemies())
        {
            if (Utils.distancePointPoint(_player.pos, e.pos) < umbralDistancia)
            {
                lost = true;
                //_player.die(); //Crear las lineas, sustituyendo al cuadrado en el render?
            }
        }
    }

    void destroyItems()
    {
        for (Item i : itemsToDestroy)
        {
            if (i.dead)
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

            //Reiniciar nivel
        }
    }

    public void changeLevel()
    {
        _level.getItems().clear();
        _level.getEnemies().clear();
        _level.getPaths().clear();

        currentLvl++;
        try {
            _level.loadLevel(currentLvl - 1);
        }
        catch ( Exception E)
        {

        }
        maxScore = _level._items.size();
        score = 0;
        _player = new Player(_level.getPaths());
        delayChangeLevel = 1.0f;
        delayDeath = 1.0f;
    }

    boolean checkLevelCompleted(double deltaTime)
    {
        if (score == maxScore)
        {
            if (delayChangeLevel >= 0)
                delayChangeLevel -= deltaTime;
            else
                return true;
        }
        return false;
    }

    public int getX() { return _x; }
    public void setX(int x) { _x = x; }
}