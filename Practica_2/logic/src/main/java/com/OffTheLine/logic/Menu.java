package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.logic.Button;
import java.util.ArrayList;

public class Menu {

    ArrayList<Button> buttons = new ArrayList<Button>();
    boolean _mainMenu;
    Logic _logic;
    Menu(boolean main, Logic l)
    {
        _logic = l;
        _mainMenu = main;
    }

    void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        if (!inputList.isEmpty())
        {
            for (Input.TouchEvent tE : inputList) {
                for (Button b : buttons) {
                    if (tE.type == Input.TouchEvent.TouchType.CLICK || tE.type == Input.TouchEvent.TouchType.PRESS && b.clicked(tE.pos.x, tE.pos.y))
                    {
                        if (_mainMenu) //meter enum aqui tambien?
                        {
                            if (b == buttons.get(0))
                            {
                                //Boton 0 Main Menu
                                System.out.println("0");
                            }
                            else if (b == buttons.get(1))
                            {
                                //Boton 1 Main Menu
                                System.out.println("1");
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

    void easyMode()
    {
        _logic.loadEasyMode();
    }

    void hardMode()
    {
        _logic.loadHardMode();
    }

    void render(Graphics g)
    {
     for (Button b: buttons)
     {

         b.render(g);
     }
    }

    void addButton(float x, float y, String text, float width, float height)
    {
        Button b = new Button(x, y, text, width, height);
        buttons.add(b);
    }
}
