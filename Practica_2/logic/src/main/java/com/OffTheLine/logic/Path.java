package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.logic.GameObject;

import java.util.ArrayList;

public class Path extends GameObject {

    ArrayList<Vector2D> _vertices = null;
    ArrayList<Vector2D> _directions = null;

    boolean _useDirections = false;

    public Path()
    {
        super(0, 0); //Constructora de gameObject

        _vertices = new ArrayList<Vector2D>();
        _directions = new ArrayList<Vector2D>();
    }

    public void useDirections() { _useDirections = true; }

    public void addVertice(float x, float y){
        _vertices.add(new Vector2D(x, y * -1));
    }

    public void addDirection(float x, float y){
        _directions.add(new Vector2D(x, y * -1));
    }

    @Override
    public void update(double delta) {

    }

    //SAVE & RESTORE DONE OUTSIDE
    @Override
    public void render(Graphics g)
    {
        g.setColor(0xFFFFFFFF);

        for (int i = 0; i < _vertices.size() - 1; i++)
        {
            g.drawLine(_vertices.get(i).x, _vertices.get(i).y, _vertices.get(i + 1).x, _vertices.get(i + 1).y);
        }

        g.drawLine(_vertices.get(_vertices.size() - 1).x, _vertices.get(_vertices.size() - 1).y, _vertices.get(0).x, _vertices.get(0).y);
    }

    @Override
    public void lateUpdate(double delta) {

    }
}
