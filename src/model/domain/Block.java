package model.domain;

import java.awt.*;

public class Block extends MObject{

    int x;
    int y;
    int width;
    int height;
    int color = 0; // 0: purple 1: yellow
    boolean isHidden = false; //after collision, block will be hidden.

    @Override
    void draw(Graphics g) {

    }
}
