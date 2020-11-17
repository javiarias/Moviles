package com.OffTheLine.common;

import java.util.ArrayList;

public interface Logic {

    ArrayList<GameObject> _objects = null;

    public void update(double deltaTime, Engine e);

    public ArrayList<GameObject> getObjects();
}
