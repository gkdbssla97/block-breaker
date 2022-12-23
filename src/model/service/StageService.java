package model.service;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum StageService {

    STAGE1(1, 3, 3, 200, 80),
    STAGE2(2, 6, 6, 100, 40),
    STAGE3(3, 9, 9, 66, 33);

//    STAGE4(BLOCK_ROWS, BLOCK_COLS, BLOCK_WIDTH, BLOCK_HEIGHT);
//
//    STAGE5(BLOCK_ROWS, BLOCK_COLS, BLOCK_WIDTH, BLOCK_HEIGHT);

    private int level, rows, cols, width, height;

    StageService(int level, int rows, int cols, int width, int height) {
        this.level = level;
        this.rows = rows;
        this.cols = cols;
        this.width = width;
        this.height = height;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return level;
    }

    public static StageService findStageByLevel(int level) {
        StageService stageService = Arrays.stream(StageService.values())
                .filter(x -> x.getLevel() == (level))
                .findFirst().orElse(null);
//        System.out.println(stageService.getLevel());
        return stageService;
    }
}
