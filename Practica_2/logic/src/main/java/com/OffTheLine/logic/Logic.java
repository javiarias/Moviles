package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;
import java.util.Random;

public class Logic implements com.OffTheLine.common.Logic
{

    //Enum para estado del juego
    public enum GameState { MAINMENU, GAME, LOST, WON }

    /*Variables*/

    //Elementos
    Engine _e;
    Level _level = null;
    Player _player = null;
    Menu _menu = null;

    //Lives
    int lives;
    int maxLives;

    //Score
    int score = 0;
    int totalScore = 0;
    int maxScore;

    //UI
    float UI_LivesPadding = 15;
    float UI_Y = 60;
    Vector2D UI_LivesPosRight = new Vector2D(0, UI_Y);
    ArrayList<Square> UI_Lives;

    //Otros
    GameState _state = GameState.MAINMENU;
    int MAX_LEVEL = 20;
    int currentLvl = 1;
    boolean lost = false;
    float delayChangeLevel = 1.0f;
    float delayDeath = 1.0f;
    ArrayList<Item> itemsToDestroy = new ArrayList<Item>();
    String _path;
    float playerSpeed;

    //Para screenshake
    float shakeTime = 0;
    int shakeX, shakeY;

    //Fuentes
    Font _gameFont = null;
    Font _menuFont = null;

    /*Funciones*/

    //Constructora
    public Logic(Engine e, String path)
    {
        _e = e;
        _path = path;
    }

    @Override public void init()
    {
        try
        {
            _gameFont = _e.getGraphics().newFont(_path + "BungeeHairline-Regular.ttf", 20, false);
            _menuFont = _e.getGraphics().newFont(_path + "Bungee-Regular.ttf", 20, false);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        _menu = new Menu(this, _menuFont);
        UI_Lives = new ArrayList<Square>();
    }

    //Inicio del juego
    public void gameStart(boolean easyMode)
    {
        if(easyMode)
            setEasyMode();
        else
            setHardMode();

        _player = new Player(playerSpeed);
        readyGameUI();
        _state = GameState.GAME;
        _level = new Level(_path + "levels.json", _e);
        currentLvl = 1;
        loadCurrentLevel();
    }

    //Para construir la interfaz
    private void readyGameUI()
    {
        UI_Lives.clear();
        Vector2D pos = new Vector2D(UI_LivesPosRight);

        for (int i = 0; i < maxLives; i++)
        {
            Square s = new Square(new Vector2D(pos), 0xFF0088FF);
            s._size = 12;
            UI_Lives.add(s);
            pos.x -= (UI_LivesPadding + s._size);
        }
    }

    //Vida perdida
    public void lostLife()
    {
        if(lives > 0)
        {
            lives--;
            Cross x = new Cross(UI_Lives.get(maxLives - lives - 1), 0xFFFF1111);
            UI_Lives.set(maxLives - lives - 1, x);
        }
    }

    //Update
    @Override public void update(double deltaTime)
    {
        ArrayList<Input.TouchEvent> ls = new ArrayList<Input.TouchEvent>(_e.getInput().getTouchEvents());

        if (_state == GameState.MAINMENU)
            _menu.update(deltaTime, ls);

        else
        {
            //Si estamos en la pantalla de victoria o la de derrota, comprobar si se hace click
            // Pero, seguimos queriendo que se actualice y renderice el nivel por debajo
            if (_state != GameState.GAME)
            {
                for (Input.TouchEvent e : ls)
                {
                    if (e.type == Input.TouchEvent.TouchType.PRESS) {
                        _state = GameState.MAINMENU;
                        return;
                    }
                }
            }

            boolean levelComplete = checkLevelCompleted(deltaTime);

            if (_state != GameState.GAME || !levelComplete)
            {
                if(shakeTime > 0)
                    shakeTime -= deltaTime;

                int i = itemsToDestroy.size();
                _player.checkPlayerCollisions(_level, itemsToDestroy, deltaTime);
                score += (itemsToDestroy.size() - i);

                lost = _player.getDead();

                if (_player.outOfBounds(_e.getGraphics().getHeight(), _e.getGraphics().getWidth()))
                    lost = true;

                _level.update(deltaTime, ls);
                _player.update(deltaTime, ls);

                if(_player._shake || (_player._dead && delayDeath >= 1))
                {
                    _player._shake = false;
                    shakeTime = 0.03F;

                    Random r = new Random(System.currentTimeMillis());

                    shakeX = (r.nextFloat() >= 0.5) ? 1 : -1;
                    shakeY = (r.nextFloat() >= 0.5) ? -1 : 1;
                }

                destroyItems();

                if (lost)
                    destroyPlayer(deltaTime);

            }
            else if(_state == GameState.GAME && levelComplete)
            {
                totalScore += score;
                changeLevel();
            }
        }
    }

    //Render
    @Override public void render(Graphics g)
    {
        if (_state == GameState.MAINMENU)
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

            if(shakeTime > 0)
                g.translate(4 * shakeX, 4 * shakeY);

            g.translate(g.getWidth() / 2.0f, g.getHeight() / 2.0f);

            _level.render(g);
            _player.render(g);

            //queremos que se renderice el nivel debajo de la pantalla de victoria
            g.save();
            if(_state == GameState.LOST)
                drawGameOver(g);
            else if(_state == GameState.WON)
                drawGameWon(g);
            g.restore();
        }
    }

    //Para renderizar pantalla de derrota
    private void drawGameOver(Graphics g)
    {
        g.setColor(0xFF444444);
        g.fillRect(-g.getWidth() / 2.0f, -160, g.getWidth() / 2.0f, 0);

        g.setFont(_menuFont);
        g.setColor(0xFFFF0000);

        g.save();
        g.scale(2, 2);
        g.drawText("GAME OVER", -60, -60);
        g.restore();

        g.scale(1, 1);
        g.setColor(0xFFFFFFFF);

        String txt = "EASY MODE";
        if(maxLives == 5)
            txt = "HARD MODE";

        g.drawText(txt, -60, -70);
        g.drawText("SCORE: " + totalScore, -50, -40);
        g.drawText("CLICK TO QUIT TO MAIN MENU", -160, -10);
    }

    //Para renderizar pantalla de victoria
    private void drawGameWon(Graphics g)
    {
        g.setColor(0xFF444444);
        g.fillRect(-g.getWidth() / 2.0f, -160, g.getWidth() / 2.0f, -20);

        g.setFont(_menuFont);
        g.setColor(0xFF0088FF);

        g.save();
        g.scale(2, 2);
        g.drawText("CONGRATULATIONS", -110, -60);
        g.restore();

        g.scale(1, 1);
        g.setColor(0xFFFFFFFF);

        String txt = "EASY MODE";
        if(maxLives == 5)
            txt = "HARD MODE";

        g.drawText(txt + " COMPLETE", -120, -70);
        g.drawText("CLICK TO QUIT TO MAIN MENU", -160, -40);
    }

    //Para renderizar la interfaz
    public void paintGameUI(Graphics g)
    {
        g.setFont(_gameFont);
        g.setColor(0xFFFFFFFF);
        g.drawText("Level " + currentLvl + " - " + _level._name, 0, UI_Y * 1.2f);

        //La UI de vidas se dibuja desde la derecha

        for (Square s : UI_Lives)
        {
            g.save();
            g.translate(g.getWidth(), 0);
            s.render(g);
            g.restore();
        }
    }

    //Destruccion de monedas, se utiliza una lista adicional (itemsToDestroy) para evitar problemas de borrado en medio de un bucle
    void destroyItems()
    {
        for (Item i : itemsToDestroy)
        {
            if (i._dead) //Si está marcado como muerto (se utiliza para la animacion de ampliar su tamaño)
            {
                _level.getItems().remove(i);
            }
        }
    }

    //Destruccion del jugador (a nivel lógico)
    void destroyPlayer(double deltaTime)
    {
        if (delayDeath >= 0)
            delayDeath -= deltaTime;
        else
        {
            lostLife();

            if (lives == 0)
                _state = GameState.LOST;
            else
                changeLevel();
        }
    }

    //Cambio de nivel
    public void changeLevel()
    {
        //Limpieza de los elementos del nivel que se quita
        _level.getItems().clear();
        _level.getEnemies().clear();
        _level.getPaths().clear();

        //Se restauran las variables del nivel
        score = 0;
        delayChangeLevel = 1.0f;
        delayDeath = 1.0f;
        lost = false;

        loadCurrentLevel();
    }

    //Comprobación de que se ha completado el nivel
    boolean checkLevelCompleted(double deltaTime)
    {
        if (!lost && score == maxScore)
        {
            if (delayChangeLevel >= 0) //Margen de paso de un nivel a otro
                delayChangeLevel -= deltaTime;
            else
            {
                if(currentLvl >= MAX_LEVEL)
                    _state = GameState.WON;
                else
                    currentLvl++;
                return true;
            }
        }

        return false;
    }

    //Carga de nivel
    public void loadCurrentLevel()
    {
        try { _level.loadLevel(currentLvl - 1); }
        catch (Exception E) {}

        maxScore = _level._items.size();
        _player.newLevel(_level.getPaths());
    }

    //Variables para el modo fácil
    public void setEasyMode()
    {
        lives = maxLives = 10;
        playerSpeed = 250;
    }

    //Variables para el modo difícil
    public void setHardMode()
    {
        lives = maxLives = 5;
        playerSpeed = 400;
    }
}