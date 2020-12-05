package com.OffTheLine.common;

import java.util.ArrayList;

public interface Logic {

    /*Funciones a sobreescribir en las implementaciones*/

    void update(double deltaTime);
    void render(Graphics g);
    void init();
}
