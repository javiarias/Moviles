package com.OffTheLine.common;

import java.io.InputStream;

public interface Engine {
    public Graphics getGraphics();

    public Input getInput();

    public InputStream openInputStream(String path);

    public Logic getLogic();

    public void release();
}
