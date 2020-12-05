package com.OffTheLine.common;

import java.io.InputStream;

public interface Engine {

    /*Funciones a sobreescribir en las implementaciones*/

    void release();

    /*Getters*/
    Graphics getGraphics();
    Input getInput();
    Logic getLogic();

    /*Para tratar archivos*/
    InputStream openInputStream(String path);
    InputStream getFile(String path)  throws Exception;
}
