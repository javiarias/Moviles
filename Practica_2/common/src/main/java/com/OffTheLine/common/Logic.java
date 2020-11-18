package com.OffTheLine.common;

import java.util.ArrayList;

public interface Logic {

    ArrayList<GameObject> _objects = null;

    public void update(double deltaTime);

    public ArrayList<GameObject> getObjects();
}
