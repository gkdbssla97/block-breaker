package model.domain;

import java.awt.*;

import static util.Constant.*;

public class Ball {
    int x = (CANVAS_WIDTH - 50) / 2;
    int y = CANVAS_HEIGHT - 100;
    int width = BALL_WIDTH;
    int height = BALL_HEIGHT;

    public Ball() {
    }

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point getCenter() {
        return new Point(x + (BALL_WIDTH / 2), y + BALL_HEIGHT / 2);
    }

    Point getBottomCenter() {
        return new Point(x + (BALL_WIDTH / 2), y + BALL_HEIGHT);
    }

    Point getTopCenter() {
        return new Point(x + (BALL_WIDTH / 2), y);
    }

    Point getLeftCenter() {
        return new Point(x, y + (BALL_HEIGHT / 2));
    }

    Point getRightCenter() {
        return new Point(x + BALL_WIDTH, y + (BALL_HEIGHT / 2));
    }
}
