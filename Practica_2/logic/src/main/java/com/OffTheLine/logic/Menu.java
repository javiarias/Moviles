package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.logic.Button;
import java.util.ArrayList;

public class Menu {
    ArrayList<Button> buttons = new ArrayList<Button>();

    void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {

        //Check input
        //Do stuff if true

    }

    void render(Graphics g)
    {
     for (Button b: buttons)
     {
         b.render(g);
     }
    }
}
