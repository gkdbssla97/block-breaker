package model.domain;

import model.audio.GameAudio;
import model.service.StageService;
import util.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

import static model.service.StageService.STAGE1;
import static util.Constant.*;

public class MyFrame extends JFrame {

    //audio
    static GameAudio blockCollision = new GameAudio();
    //panel
    static StartPanel startPanel = new StartPanel();
    static PlayPanel playPanel = new PlayPanel(STAGE1);
    static PlayPanel nextGamePanel;
    static EndPanel endPanel = new EndPanel();
    static GameClearPanel clearPanel = new GameClearPanel();

    //variable

    static Timer timer = null;
    static Block[][] blocks;

    static Bar bar = new Bar();
    static Ball ball = new Ball();
    static int barXTarget = bar.x; //Target Value - interpolation
    static int dir = 0; // 0: Up-Right 1: Down-right 2: Up-Left 3: Down-Left
    static int ballSpeed = 3;
    static boolean isGameFinished = false;
    static boolean isGameOver = false;
    static boolean duplicateBall = false;
    static int panelStatus = 0; // 0: startPanel, 1: playPanel, 2: endPanel
    static Thread t;
    LinkedList<MObject> balls = playPanel.getBalls();
    public MyFrame() throws HeadlessException {
        setTitle("#HomeWork5");
        setSize(600, 600);
        setResizable(false);
        setLocation(400, 200);
        setLayout(new BorderLayout());

//        startPanel.setLayout(null);
        startPanel.setBounds(0, 0, 600, 400);
        playPanel.setVisible(false);
        endPanel.setVisible(false);

        add(startPanel);
        add(playPanel);
        add(endPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameAudio.gameStartAudio();
        t = new Thread();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && panelStatus == 0) {
                    panelStatus = 2;
                    startPanel.setVisible(false);
                    playPanel.setVisible(true);
                    endPanel.setVisible(false);
                    if(LEVEL == 1)
                        blocks = initApplication();

                    setKeyListener();
                    if (LEVEL == 1) {
                        startTime(playPanel);
                    } else if (LEVEL == 2) {
//                        timer.start();
                        startTime(nextGamePanel);
                    } else if (LEVEL == 3)
                        endPanel.setVisible(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
    public Block[][] initApplication() {
        StageService stageByLevel = StageService.findStageByLevel(LEVEL);
        blocks = new Block[stageByLevel.getRows()][stageByLevel.getCols()];

        for (int i = 0; i < stageByLevel.getRows(); i++) {
            for (int j = 0; j < stageByLevel.getCols(); j++) {
                blocks[i][j] = new Block();
                blocks[i][j].x = stageByLevel.getWidth() * j + BLOCK_GAP * j;
                blocks[i][j].y = stageByLevel.getHeight() * i + BLOCK_GAP * i;
                blocks[i][j].width = stageByLevel.getWidth();
                blocks[i][j].height = stageByLevel.getHeight();
                blocks[i][j].color = initColorByRandom();
                blocks[i][j].isHidden = false;
            }
        }
        return blocks;
    }

    public void setKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    barXTarget -= 20;
                    if (bar.x < barXTarget) {
                        barXTarget = bar.x;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    barXTarget += 20;
                    if (bar.x > barXTarget) {
                        barXTarget = bar.x;
                    }
                }
                if (bar.x < 0) bar.x = 0;
                if (bar.x + BAR_WIDTH > 600) bar.x = 600 - BAR_WIDTH;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
    }

    public int initColorByRandom() {
        Random rand = new Random();
        int iValue = rand.nextInt(2);
        return iValue;
    }
    public void startTime(PlayPanel playpanel) {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                    }
                    StageService stageByLevel = StageService.findStageByLevel(Constant.LEVEL);
                    if(duplicateBall) {
                        System.out.println("SMASHED!");
                        balls.add(new Ball(ball.x, ball.y));
                        balls.add(new Ball(ball.x, ball.y));
                        balls.add(new Ball(ball.x, ball.y));

                        movementDupBall();
                        duplicateBall = false;
                        repaint();
                    }
                    movement();
                    checkCollision(); // Bar, Wall
                    if(LEVEL <= 2)
                        checkCollisionBlock(Constant.LEVEL); // Blocks
//                checkCollisionBlockDupl(LEVEL);
//                playPanel.repaint(); //Redraw
                    if(LEVEL <= 3)
                        isGameFinished(stageByLevel);

                    if (isGameFinished && LEVEL <= 2) {
                        playpanel.setVisible(false);
                        isGameFinished = false;
                        StageService nextStage = StageService.findStageByLevel(Constant.LEVEL);
                        System.out.println("LV: " + LEVEL);
                        ball = new Ball(bar.x + 5, bar.y);
//                    setKeyListener();
                        nextGamePanel = new PlayPanel(nextStage);
                        add(nextGamePanel);

                        nextGamePanel.setVisible(true);

                        nextGamePanel.repaint();
                        blocks = new Block[nextStage.getRows()][nextStage.getCols()];
                        blocks = initApplication();
                        isGameFinished = false;
                    }
//                    if (isGameOver && LEVEL == 3) {
//                        initEndPanel();
//                        endPanel.setVisible(true);
//                        repaint();
//                    }
                    repaint();
                }
            }
        });
        t.start();
    }

    public void isGameFinished(StageService stageService) {
        int count = 0;
        if (LEVEL <= 2) {
            for (int i = 0; i < stageService.getRows(); i++) {
                for (int j = 0; j < stageService.getCols(); j++) {
                    Block block = blocks[i][j];
                    if (block.isHidden) {
                        count++;
                        System.out.println("?: " + count);
                    } else System.out.println("umm...");
                }
            }
        }
        if (count == stageService.getCols() * stageService.getRows()) {
            LEVEL++;
            isGameFinished = true;
//                timer.stop();
//                collisionCount = 0;
//                return true;
        }
        if (isGameOver) {
            initEndPanel();
            add(endPanel);
            playPanel.setVisible(false);
            nextGamePanel.setVisible(false);
            endPanel.setVisible(true);
            t.interrupt();
        }
        if (LEVEL == 3) {
            LEVEL = 0;
//            timer.stop();
            initClearPanel();
            add(clearPanel);
            isGameFinished = true;
            nextGamePanel.setVisible(false);
            clearPanel.setVisible(true);
            t.interrupt();
        }
    }

    public void movement() {
        if (bar.x < barXTarget) {
            bar.x += 5;
        } else if (bar.x > barXTarget) {
            bar.x -= 5;
        }
        if (dir == 0) { // 0: Up-Right
            ball.x += ballSpeed;
            ball.y -= ballSpeed;
        } else if (dir == 1) { // 1: Down-right
            ball.x += ballSpeed;
            ball.y += ballSpeed;
        } else if (dir == 2) { // 2: Up-Left
            ball.x -= ballSpeed;
            ball.y -= ballSpeed;
        } else if (dir == 3) { // 3: Down-Left
            ball.x -= ballSpeed;
            ball.y += ballSpeed;
        }
    }

    public void movementDupBall() {
        for (var b : balls) {
            if (b instanceof Ball) {
                Ball ball = (Ball) b;
                if (dir == 0) { // 0: Up-Right
                    ball.x += ballSpeed;
                    ball.y -= ballSpeed;
                } else if (dir == 1) { // 1: Down-right
                    ball.x += ballSpeed;
                    ball.y += ballSpeed;
                } else if (dir == 2) { // 2: Up-Left
                    ball.x -= ballSpeed;
                    ball.y -= ballSpeed;
                } else if (dir == 3) { // 3: Down-Left
                    ball.x -= ballSpeed;
                    ball.y += ballSpeed;
                }
            }
        }
    }

    public boolean isCollidedBallAndBar(Rectangle ballRect, Rectangle barRect) {
        return ballRect.intersects(barRect);
    }

    public void checkCollision() {
        if (dir == 0) { // 0: Up-Right
            // Wall
            if (ball.y < 0) { // 윗벽
                dir = 1;
            }
            if (ball.x >= CANVAS_WIDTH - BALL_WIDTH) { // 오른벽
                dir = 2;
            }
        } if (dir == 1) { // 1: Down-right
            // Wall
            if (ball.y > CANVAS_HEIGHT - ball.height) { // 아랫벽
                //Game Over
//                isGameFinished = true;
                // 새로운 패널 가져오기
                isGameOver = true;
                if (HIGH_SCORE <= SCORE) {
                    HIGH_SCORE = SCORE;
                } t.interrupt();
                playPanel.setVisible(false);
//                nextGamePanel.setVisible(false);
//                endPanel.setVisible(true);
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
//                            super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_SPACE && panelStatus == 2) {
                            panelStatus = 0;
                            endPanel.setVisible(false);
                            startPanel.setVisible(true);
                            repaint();
                        }
                    }
                });

                SCORE = 0;
            }
            if (ball.x >= CANVAS_WIDTH - BALL_WIDTH) {
                dir = 3;
            }
            // Bar
            if (ball.getBottomCenter().y >= bar.y) {
                if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
                        new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
                    dir = 0;
                }
            }
        } if (dir == 2) { // 2: Up-Left
            // Wall
            if (ball.y < 0) { // 윗벽
                dir = 3;
            }
            if (ball.x < 0) { // 왼쪽벽
                dir = 0;
            }
            // Bar
        } if (dir == 3) { // 3: Down-Left
            if (ball.y > CANVAS_HEIGHT - bar.height) { // 아랫벽
                //Game Over
//                isGameFinished = true;
                // 새로운 패널 가져오기
                isGameOver = true;
                if (HIGH_SCORE <= SCORE) {
                    HIGH_SCORE = SCORE;
                } t.interrupt();
//                endPanel.setVisible(true);
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
//                            super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_SPACE && panelStatus == 2) {
                            panelStatus = 0;
                            endPanel.setVisible(false);
                            startPanel.setVisible(true);
                            isGameOver = false;
                            setKeyListener();
                            repaint();
                        }
                    }
                });

                SCORE = 0;
            }
            if (ball.x < 0) { // 왼쪽벽
                dir = 1;
            }
            //Bar
            if (ball.getBottomCenter().y >= bar.y) {
                if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
                        new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
                    dir = 2;
                }
            }
        }
    }

    private void initEndPanel() {
        endPanel.initLabel1();
        endPanel.initLabel2(HIGH_SCORE);
        endPanel.initLabel3(SCORE);
        endPanel.initLabel4();
//        playPanel.setVisible(false);
        endPanel.setVisible(true);
    }
    private void initClearPanel() {
        clearPanel.initLabel1();
        clearPanel.initLabel2(HIGH_SCORE);
        clearPanel.initLabel3(SCORE);
        clearPanel.initLabel4();
//        playPanel.setVisible(false);
        clearPanel.setVisible(true);
    }

    public void checkCollisionBlock(int level) {
        StageService stageByLevel = StageService.findStageByLevel(level);
        //0: Up-Right 1: Down-right 2: Up-Left 3: Down-Left
        for (int i = 0; i < stageByLevel.getRows(); i++) {
            for (int j = 0; j < stageByLevel.getCols(); j++) {
                Block block = blocks[i][j];
                if (!block.isHidden) {
                    if (dir == 0) {// 0: Up-Right
                        if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
                                new Rectangle(block.x, block.y, block.width, block.height))) {
                            if (ball.x > block.x + BLOCK_GAP &&
                                    ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
                                // block bottom collision
                                dir = 1;
                            } else {
                                // block left collision
                                dir = 2;
                            }
                            blockCollision.blockCollisionAudio();
                            block.isHidden = true;
                            if (block.color == 0) {
                                SCORE += 10;
                            } else if (block.color == 1) {
                                SCORE += 20;
                                duplicateBall = true;
                            }
                        }
                    } else if (dir == 1) { // 1: Down-right
                        if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
                                new Rectangle(block.x, block.y, block.width, block.height))) {
                            if (ball.x > block.x + BLOCK_GAP &&
                                    ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
                                // block top collision
                                dir = 0;
                            } else {
                                // block left collision
                                dir = 2;
                            }
                            blockCollision.blockCollisionAudio();
                            block.isHidden = true;
                            if (block.color == 0) {
                                SCORE += 10;
                            } else if (block.color == 1) {
                                SCORE += 20;
                                duplicateBall = true;
                            }
                        }
                    } else if (dir == 2) { // 2: Up-Left
                        if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
                                new Rectangle(block.x, block.y, block.width, block.height))) {
                            if (ball.x > block.x + BLOCK_GAP &&
                                    ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
                                // block bottom collision
                                dir = 3;
                            } else {
                                // block right collision
                                dir = 0;
                            }
                            blockCollision.blockCollisionAudio();
                            block.isHidden = true;
                            if (block.color == 0) {
                                SCORE += 10;
                            } else if (block.color == 1) {
                                SCORE += 20;
                                duplicateBall = true;
                            }
                        }
                    } else if (dir == 3) {// 3: Down-Left
                        if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
                                new Rectangle(block.x, block.y, block.width, block.height))) {
                            if (ball.x > block.x + BLOCK_GAP &&
                                    ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
                                // block top collision
                                dir = 2;
                            } else {
                                // block right collision
                                dir = 1;
                            }
                            blockCollision.blockCollisionAudio();
                            block.isHidden = true;
                            if (block.color == 0) {
                                SCORE += 10;
                            } else if (block.color == 1) {
                                SCORE += 20;
                                duplicateBall = true;
                            }
                        }
                    }
                }
            }
        }
    }
//    public void checkCollisionBlockDupl(int level) {
//        for (Ball ball : balls) {
//            StageService stageByLevel = StageService.findStageByLevel(level);
//            //0: Up-Right 1: Down-right 2: Up-Left 3: Down-Left
//            for (int i = 0; i < stageByLevel.getRows(); i++) {
//                for (int j = 0; j < stageByLevel.getCols(); j++) {
//                    Block block = blocks[i][j];
//                    if (!block.isHidden) {
//                        if (dir == 0) {// 0: Up-Right
//                            if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
//                                    new Rectangle(block.x, block.y, block.width, block.height))) {
//                                if (ball.x > block.x + BLOCK_GAP &&
//                                        ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
//                                    // block bottom collision
//                                    dir = 1;
//                                } else {
//                                    // block left collision
//                                    dir = 2;
//                                }
//                                block.isHidden = true;
//                                if (block.color == 0) {
//                                    SCORE += 10;
//                                } else if (block.color == 1) {
//                                    SCORE += 20;
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    movementDupBall();
//                                }
//                            }
//                        } else if (dir == 1) { // 1: Down-right
//                            if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
//                                    new Rectangle(block.x, block.y, block.width, block.height))) {
//                                if (ball.x > block.x + BLOCK_GAP &&
//                                        ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
//                                    // block top collision
//                                    dir = 0;
//                                } else {
//                                    // block left collision
//                                    dir = 2;
//                                }
//                                block.isHidden = true;
//                                if (block.color == 0) {
//                                    SCORE += 10;
//                                } else if (block.color == 1) {
//                                    SCORE += 20;
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    movementDupBall();
//                                }
//                            }
//                        } else if (dir == 2) { // 2: Up-Left
//                            if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
//                                    new Rectangle(block.x, block.y, block.width, block.height))) {
//                                if (ball.x > block.x + BLOCK_GAP &&
//                                        ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
//                                    // block bottom collision
//                                    dir = 3;
//                                } else {
//                                    // block right collision
//                                    dir = 0;
//                                }
//                                block.isHidden = true;
//                                if (block.color == 0) {
//                                    SCORE += 10;
//                                } else if (block.color == 1) {
//                                    SCORE += 20;
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    movementDupBall();
//                                }
//                            }
//                        } else if (dir == 3) {// 3: Down-Left
//                            if (isCollidedBallAndBar(new Rectangle(ball.x, ball.y, ball.width, ball.height),
//                                    new Rectangle(block.x, block.y, block.width, block.height))) {
//                                if (ball.x > block.x + BLOCK_GAP &&
//                                        ball.getRightCenter().x <= block.x + block.width - BLOCK_GAP) {
//                                    // block top collision
//                                    dir = 2;
//                                } else {
//                                    // block right collision
//                                    dir = 1;
//                                }
//                                block.isHidden = true;
//                                if (block.color == 0) {
//                                    SCORE += 10;
//                                } else if (block.color == 1) {
//                                    SCORE += 20;
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    balls.add(new Ball(ball.x, ball.y));
//                                    movementDupBall();
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
}