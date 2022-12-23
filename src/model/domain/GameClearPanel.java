package model.domain;

import util.Constant;

import javax.swing.*;
import java.awt.*;

public class GameClearPanel extends JPanel{
    ImageIcon backGround = new ImageIcon(getClass().getResource("/sourceFile/gameClear.jpeg"));
    JLabel label1, label2, label3, label4;
    private int w, h;
    private Image image;
    private int highScore;
    private int score;

    public GameClearPanel() {
        w = backGround.getIconWidth();
        h = backGround.getIconHeight();
        image = backGround.getImage();
    }

    public void initLabel1() {
        label1 = new JLabel("Game Clear");
        label1.setSize(Constant.CANVAS_WIDTH, Constant.CANVAS_HEIGHT / 2);
        label1.setVerticalAlignment(SwingConstants.CENTER);
        label1.setHorizontalAlignment(SwingConstants.CENTER);

        label1.setFont(new Font("Serif", Font.PLAIN, 80));
        label1.setForeground(Color.WHITE);
        this.add(label1);
    }

    public void initLabel2(int highScore) {
        label2 = new JLabel("High Score: " + highScore);
//        label2.setVerticalAlignment(JLabel.BOTTOM);
//        label2.setHorizontalAlignment(JLabel.BOTTOM);
        label2.setSize(80, 30);
        label2.setLocation(30, 30);
        label2.setFont(new Font("Serif", Font.PLAIN, 50));
        label2.setForeground(Color.WHITE);
        this.add(label2);
    }

    public void initLabel3(int score) {
        label3 = new JLabel(" Your Score: " + score);
        label3.setVerticalAlignment(JLabel.CENTER);
        label3.setHorizontalAlignment(JLabel.CENTER);

        label3.setFont(new Font("Serif", Font.PLAIN, 50));
        label3.setForeground(Color.WHITE);
        this.add(label3);
    }

    public void initLabel4() {
        label4 = new JLabel("Press SpaceBar!");
        label4.setSize(Constant.CANVAS_WIDTH, Constant.CANVAS_HEIGHT / 2);
        label4.setVerticalAlignment(SwingConstants.CENTER);
        label4.setHorizontalAlignment(SwingConstants.CENTER);

        label4.setFont(new Font("Serif", Font.PLAIN, 70));
        label4.setForeground(Color.RED);
        this.add(label4);
    }

//    public void paint(Graphics g) {
//        g.drawImage(image, 10, 10, w, h, this);
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 10, 10, w, h, this);
    }
}
