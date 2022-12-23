package model.domain;

import model.service.StageService;

import javax.swing.*;
import java.awt.*;

import static util.Constant.*;

public class PlayPanel extends JPanel { // CANVAS for Drawing
    public int blockRows;
    public int blockCols;
    public int blockWidth;
    public int blockHeight;
    public StageService stageService;

    public PlayPanel(StageService stageService) {
        this.stageService = stageService;
        this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.setBackground(Color.BLACK); //그라데이션으로 수정
        this.blockRows = this.stageService.getRows();
        this.blockCols = this.stageService.getCols();
        this.blockWidth = this.stageService.getWidth();
        this.blockHeight = this.stageService.getHeight();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;

        drawUI(graphics2D);
    }

    private void drawUI(Graphics2D graphics2D) {
        //draw Blocks
        for (int i = 0; i < stageService.getRows(); i++) {
            for (int j = 0; j < stageService.getCols(); j++) {
                if (MyFrame.blocks[i][j].isHidden) {
                    continue;
                }
                if (MyFrame.blocks[i][j].color == 0) {
                    graphics2D.setColor(new Color(120, 0, 130));
                } else if (MyFrame.blocks[i][j].color == 1) {
                    graphics2D.setColor(Color.YELLOW);
                }
                graphics2D.fillRect(MyFrame.blocks[i][j].x, MyFrame.blocks[i][j].y,
                        MyFrame.blocks[i][j].width, MyFrame.blocks[i][j].height);
            }

            //draw Ball
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillOval(MyFrame.ball.x, MyFrame.ball.y, BALL_WIDTH, BALL_HEIGHT);

            //draw Bar
            graphics2D.setColor(new Color(110, 80, 60));
            graphics2D.fillRect(MyFrame.bar.x, MyFrame.bar.y, BAR_WIDTH, BALL_HEIGHT);
        }
    }
}
