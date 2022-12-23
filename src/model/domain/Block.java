package model.domain;

public class Block {

    int x;
    int y;
    int width;
    int height;
    int color = 0; // 0: purple 1: yellow
    boolean isHidden = false; //after collision, block will be hidden.
}
