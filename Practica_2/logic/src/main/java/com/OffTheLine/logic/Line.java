package com.OffTheLine.logic;

import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.OffTheLine.common.Vector2D;
import java.util.ArrayList;

public class Line extends GameObject {

    float _rotAngle;
    float thicc_ = 2;

    Line(float posX, float posY) {
        super(posX, posY);
    }

    Line(Vector2D pos_) {
        super(pos_);
    }

    @Override
    public void update(double delta, ArrayList<Input.TouchEvent> inputList) {
        _rotAngle += 8;
    }

    @Override
    public void lateUpdate(double delta) {

    }

    @Override
    public void render(Graphics g) {
        g.translate(pos.x, pos.y);
        //g.scale(thicc_, thicc_);
        g.rotate(_rotAngle);

        g.setColor(0xFF0000FF);

        g.drawLine(pos.x, pos.y, pos.x + 6, pos.y);
    }
}
