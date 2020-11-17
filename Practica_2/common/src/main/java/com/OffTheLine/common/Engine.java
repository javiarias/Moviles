package com.OffTheLine.common;

import java.io.InputStream;

public interface Engine {
    public Graphics getGraphics();

    public Input getInput();

    public InputStream openInputStream(String filename);

    public Logic getLogic();
}
