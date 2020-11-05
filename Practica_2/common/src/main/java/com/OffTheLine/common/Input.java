package com.OffTheLine.common;

import java.util.List;

public interface Input {
    class TouchEvent {}

    List<TouchEvent> getTouchEvents();
}
