package com.OffTheLine.common;

import java.util.ArrayList;

public interface GameObject {

    void update(double delta, ArrayList<Input.TouchEvent> inputList);

    //idk why not
    void lateUpdate(double delta);

    void render(Graphics g);

    void checkInputs(ArrayList<Input.TouchEvent> inputs);

}
