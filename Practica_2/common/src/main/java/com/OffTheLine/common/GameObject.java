package com.OffTheLine.common;

public interface GameObject {

    void update(double delta);

    //idk why not
    void lateUpdate(double delta);

    void render(Graphics g);

}
