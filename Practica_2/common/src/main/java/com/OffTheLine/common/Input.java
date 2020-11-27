package com.OffTheLine.common;

import java.util.List;

public interface Input {

    class TouchEvent
    {
        public enum TouchType {PRESS, RELEASE, DRAG, MOVE, CLICK, ENTER, EXIT}

        public Vector2D pos;
        public TouchType type;
        public int id;

        public TouchEvent(Vector2D pos_, int id_, TouchType type_)
        {
            pos = pos_;
            type = type_;
            id = id_;
        }
    }

    public List<TouchEvent> getTouchEvents();
    public void addEvent(TouchEvent e);
}
