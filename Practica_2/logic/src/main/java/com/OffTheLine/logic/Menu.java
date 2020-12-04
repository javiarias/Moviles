package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;

public class Menu {

    ArrayList<Button> buttons = new ArrayList<Button>();
    boolean _mainMenu;
    Logic _logic;
    Engine _e;

    Menu(boolean main, Logic l)
    {
        _logic = l;
        _mainMenu = main;
        _e = l._e;
    }

    void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        if (!inputList.isEmpty())
        {
            for (Input.TouchEvent tE : inputList) {
                for (Button b : buttons) {
                    if (tE.type == Input.TouchEvent.TouchType.PRESS)
                    {
                        Vector2D transPos = new Vector2D(tE.pos);
                        transPos.x -= _e.getGraphics().getXOffset();
                        transPos.y -=  _e.getGraphics().getYOffset();
                        transPos.x /= _e.getGraphics().getWidthScale();
                        transPos.y /=  _e.getGraphics().getHeightScale();

                        //System.out.println(transPos.x + " " + transPos.y);

                        if(b.clicked(transPos.x, transPos.y))
                        {
                            if (_mainMenu) //meter enum aqui tambien?
                            {
                                if (b == buttons.get(0))
                                {
                                    easyMode();
                                    //Boton 0 Main Menu
                                    //System.out.println("0");
                                }
                                else if (b == buttons.get(1))
                                {
                                    hardMode();
                                    //Boton 1 Main Menu
                                    //System.out.println("1");
                                }
                            }
                            else
                            {
                                //Boton 0 Lost Menu
                            }
                        }
                    }
                }
            }
        }
    }

    void easyMode() { _logic.gameStart(true); }

    void hardMode()
    {
        _logic.gameStart(false);
    }

    void render(Graphics g)
    {
        for (Button b: buttons)
        {
            g.save();
            b.render(g);
            g.restore();
        }
    }

    void addButton(float x, float y, String text, float width, float height, Font font)
    {
        Button b = new Button(x, y, text, width, height, font);
        buttons.add(b);
    }
}
