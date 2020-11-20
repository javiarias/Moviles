package com.OffTheLine.logic;

//import com.OffTheLine.common.Graphics;
import com.OffTheLine.logic.Vertice;

public class Path {

    public Vertice vertices[];
    public Direction directions[];

    Path(int size)
    {
        this.vertices = new Vertice[size];
        //super(posX, posY); //Constructora de gameObject
    }

    /*
    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void lateUpdate(double delta) {

    }*/
}
