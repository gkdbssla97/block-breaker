package model.domain;

import util.Constant;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StartPanel extends JPanel {
    ImageIcon backGround = new ImageIcon(getClass().getResource("/image/sej.jpeg"));
    JLabel label1, label2, label3, labelSpace;
    private int w, h;
    private Image image;

    public StartPanel() {
        w = backGround.getIconWidth();
        h = backGround.getIconHeight();
        image = backGround.getImage();

        initLabel1();
        initLabel2();
        initLabel3();

    }

    private void initLabel1() {
        label1 = new JLabel("Java Programming");
        label1.setSize(Constant.CANVAS_WIDTH, Constant.CANVAS_HEIGHT / 2);
        label1.setVerticalAlignment(SwingConstants.CENTER);
        label1.setHorizontalAlignment(SwingConstants.CENTER);

        label1.setFont(new Font("Serif", Font.PLAIN, 40));
        label1.setForeground(Color.BLACK);
        this.add(label1);
    }

    private void initLabel2() {
        label2 = new JLabel("    Homework #5");
//        label2.setVerticalAlignment(JLabel.BOTTOM);
//        label2.setHorizontalAlignment(JLabel.BOTTOM);
        label2.setSize(80,30);
        label2.setLocation(30,30);
        label2.setFont(new Font("Serif", Font.PLAIN, 30));
        label2.setForeground(Color.BLACK);
        this.add(label2);
    }

    private void initLabel3() {
        label3 = new JLabel("  Block Breaker");
        label3.setVerticalAlignment(JLabel.CENTER);
        label3.setHorizontalAlignment(JLabel.CENTER);

        label3.setFont(new Font("Serif", Font.PLAIN, 60));
        label3.setForeground(Color.BLACK);
        this.add(label3);
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
