package model.domain;

import java.awt.*;

abstract class MObject {
    int x;
    int y;
    int width;
    int height;

    MObject() {
    }

    abstract void draw(Graphics g);
}
