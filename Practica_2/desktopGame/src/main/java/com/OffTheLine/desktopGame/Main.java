package com.OffTheLine.desktopGame;

import com.OffTheLine.desktopEngine.Engine;
import com.OffTheLine.logic.Logic;

public class Main
{
    public static void main(String[] args)
    {
        String path = "assets/";
        Engine _engine = new Engine(path);
        Logic logic = new Logic(_engine, "assets/");
        _engine.init(logic);
        _engine.update();
        _engine.release();
    }
}
