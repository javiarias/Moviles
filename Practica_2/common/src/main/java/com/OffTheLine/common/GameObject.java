package com.OffTheLine.common;

import java.util.ArrayList;

public interface GameObject {

    /*Funciones a sobreescribir en las implementaciones*/

    void update(double delta, ArrayList<Input.TouchEvent> inputList);
    void render(Graphics g);
    void checkInputs(ArrayList<Input.TouchEvent> inputs);
    void lateUpdate(double delta);
}
