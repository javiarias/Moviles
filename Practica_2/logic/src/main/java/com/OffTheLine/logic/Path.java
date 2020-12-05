package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;

import java.util.ArrayList;

public class Path extends GameObject {

    /*Variables*/

    ArrayList<Vector2D> _vertices = null;
    ArrayList<Vector2D> _directions = null;
    boolean _useDirections = false;

    /*Funciones*/

    //Constructora
    public Path()
    {
        super(0, 0); //Constructora de gameObject

        _vertices = new ArrayList<Vector2D>();
        _directions = new ArrayList<Vector2D>();
    }

    //¿Se usa componente dirección?
    public void useDirections() { _useDirections = true; }

    //Añadir vértice
    public void addVertice(float x, float y){
        _vertices.add(new Vector2D(x, y * -1));
    }

    //Añadir dirección
    public void addDirection(float x, float y){
        _directions.add(new Vector2D(x, y * -1));
    }

    //Update
    @Override public void update(double delta, ArrayList<Input.TouchEvent> inputList) { }

    //Render, Save y restore hechos fuera
    @Override public void render(Graphics g)
    {
        g.setColor(0xFFFFFFFF);

        for (int i = 0; i < _vertices.size() - 1; i++)
        {
            g.drawLine(_vertices.get(i).x, _vertices.get(i).y, _vertices.get(i + 1).x, _vertices.get(i + 1).y);
        }

        g.drawLine(_vertices.get(_vertices.size() - 1).x, _vertices.get(_vertices.size() - 1).y, _vertices.get(0).x, _vertices.get(0).y);
    }

    @Override public void lateUpdate(double delta) { }
}
