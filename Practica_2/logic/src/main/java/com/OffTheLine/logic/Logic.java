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
    //int currentLvl = 3;
    int currentLvl = 1;
    int lives;
    int maxLives = 3;
    Player _player;

    Vector2D UI_LivesPosRight;
    float UI_LivesPadding = 5;

    float UI_Y = 40;

    ArrayList<Square> UI_Lives;

    public boolean _centerScreen = false;

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

        _player = new Player(_level.getPaths());

        lives = maxLives;

        UI_LivesPosRight = new Vector2D(-(UI_LivesPadding * 3), UI_Y);

        Vector2D pos = UI_LivesPosRight;

        UI_Lives = new ArrayList<Square>();
        for (int i = 0; i < maxLives; i++) {
            Square s = new Square(new Vector2D(pos), 0xFF0088FF);
            s._size = 12;
            UI_Lives.add(s);

            pos.x -= (UI_LivesPadding + s._size);
        }

        lostLife();
    }

    public void lostLife()
    {
        lives--;
        if(lives > 0)
        {
            Cross x = new Cross(UI_Lives.get(lives), 0x00FF0000);

            UI_Lives.set(lives, x);
        }
    }

    @Override
    public void update(double deltaTime)
    {
        ArrayList<Input.TouchEvent> ls = new ArrayList(_e.getInput().getTouchEvents());

        _level.update(deltaTime, ls);
        _player.update(deltaTime, ls);
    }

    @Override
    public void render(Graphics g)
    {
        g.save();
        //paintUI(g);
        g.restore();

        //checkPlayerCollision

        g.translate(g.getWidth() / 2.0f, g.getHeight() / 2.0f);

        g.save();
        _level.render(g);
        _player.render(g);
        g.restore();
    }

    public void paintUI(Graphics g)
    {
        g.save();
        g.drawText(_level._name, UI_LivesPadding*3,UI_Y);
        g.restore();
        //Valores arbitrarios de momento, ya que no puedo sacar el tamaño del texto para ajustarlo
        //Elegir fuente (la del menu y la UI no es la misma)
        //PD: Se reescala regular


        g.save();
        //La UI de vidas está dibujada desde la dcha
        g.translate(g.getWidth(), 0);

        for (Square s : UI_Lives)
        {
            g.save();
            s.render(g);
            g.restore();
        }
        g.restore();
    }

    public int getX() { return _x; }
    public void setX(int x) { _x = x; }
}