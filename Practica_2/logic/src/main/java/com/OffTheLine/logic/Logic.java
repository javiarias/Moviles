package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Font;
import com.OffTheLine.common.GameObject;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;

import java.awt.Color;
import java.util.ArrayList;

public class Logic implements com.OffTheLine.common.Logic {

    int _x = 0;
    int _incX = 10;

    Engine _e;
    Level _level;
    //int currentLvl = 3;
    int currentLvl = 1;
    int lives = 3;
    Player _player;

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
        g.translate(g.getWidth() / 2.0f, g.getHeight() / 2.0f);

        paintUI(g);

        _level.render(g);
        _player.render(g);
    }

    public void paintUI(Graphics g)
    {
        g.drawText(_level._name, -300,-200);
        //Valores arbitrarios de momento, ya que no puedo sacar el tamaño del texto para ajustarlo
        //Elegir fuente (la del menu y la UI no es la misma)
        //PD: Se reescala regular

        for (int i = 0; i < lives; i++)
        {
            g.setColor(0xFF0000FF); //AZUL

            //Valores a toquetear
            float _size = 20;
            float _distance = 25;
            float baseX = 100;
            float baseY = -200;

            g.drawLine(baseX - _size / 2.0f + _distance*i, baseY +_size / 2.0f, baseX +_size / 2.0f + _distance*i, baseY + _size / 2.0f);
            g.drawLine(baseX + _size / 2.0f + _distance*i, baseY +_size / 2.0f, baseX +_size / 2.0f + _distance*i, baseY -_size / 2.0f);
            g.drawLine(baseX + _size / 2.0f + _distance*i, baseY -_size / 2.0f,  baseX -_size / 2.0f + _distance*i, baseY -_size / 2.0f);
            g.drawLine(baseX - _size / 2.0f + _distance*i, baseY -_size / 2.0f, baseX -_size / 2.0f + _distance*i, baseY + _size / 2.0f);
        }
    }

    public int getX() { return _x; }
    public void setX(int x) { _x = x; }
}