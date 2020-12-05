package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Font;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;

public class Menu {

    /*Variables*/
    ArrayList<Button> buttons = new ArrayList<Button>();
    Logic _logic;
    Engine _e;
    Font _font;

    /*Funciones*/

    //Constructora
    Menu(Logic l, Font font)
    {
        _logic = l;
        _e = l._e;
        _font = font;

        addButton( 10, 350, "EASY MODE",130, 30, font, 2);
        addButton(10, 410, "HARD MODE",135, 30, font, 2);
    }

    //Update
    void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        if (!inputList.isEmpty())
        {
            for (Input.TouchEvent tE : inputList)
            {
                for (Button b : buttons)
                {
                    if (tE.type == Input.TouchEvent.TouchType.PRESS)
                    {
                        Vector2D transPos = new Vector2D(tE.pos);
                        transPos.x -= _e.getGraphics().getXOffset();
                        transPos.y -= _e.getGraphics().getYOffset();
                        transPos.x /= _e.getGraphics().getWidthScale();
                        transPos.y /= _e.getGraphics().getHeightScale();

                        if (b.clicked(transPos.x, transPos.y))
                        {
                            if (b == buttons.get(0))
                                easyMode();
                            else if (b == buttons.get(1))
                                hardMode();
                        }
                    }
                }
            }
        }
    }

    //Accion para boton de modo fácil
    void easyMode() { _logic.gameStart(true); }

    //Accion para boton de modo difícil
    void hardMode()
    {
        _logic.gameStart(false);
    }

    //Render
    void render(Graphics g)
    {
        g.setFont(_font);
        g.setColor(0xFF0088FF);

        g.save();
        g.scale(3, 3);
        g.drawText("OFF THE LINE", 3, 40);
        g.restore();

        g.save();
        g.scale(1.5f, 1.5f);
        g.drawText("A game", 9, 110);
        g.restore();

        g.setColor(0xFF777777);
        g.save();
        g.drawText("(Slow speed, 10 lives)", 270, 394);
        g.drawText("(Fast speed, 5 lives)", 275, 454);
        g.restore();

        for (Button b: buttons)
        {
            g.save();
            b.render(g);
            g.restore();
        }
    }

    //Crear y añadir boton al menu
    void addButton(float x, float y, String text, float width, float height, Font font, float scale)
    {
        Button b = new Button(x, y, text, width, height, font, scale);
        buttons.add(b);
    }
}
