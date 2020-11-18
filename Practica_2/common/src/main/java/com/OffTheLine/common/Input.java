package com.OffTheLine.common;

import java.util.List;

public interface Input {

    public interface TouchEvent {}

    public List<TouchEvent> getTouchEvents();
}
