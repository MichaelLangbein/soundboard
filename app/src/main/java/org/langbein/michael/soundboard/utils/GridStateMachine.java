package org.langbein.michael.soundboard.utils;

/**
 * Created by michael on 28.01.18.
 */

public class GridStateMachine {

    private boolean nowInAShortCol;
    private int row;
    private int col;
    private int cellsInColFilled;

    public GridStateMachine () {
        nowInAShortCol = true;
        row = 1;
        col = 0;
        cellsInColFilled = 1;
    }

    public Vec2<Integer> getCurrentPos() {
        return new Vec2<Integer>(row, col);
    }

    public void oneStepFurther() {
        cellsInColFilled += 1;
        if(nowInAShortCol) {
            if(cellsInColFilled >= 4) {
                col += 1;
                row = 0;
                nowInAShortCol = false;
                cellsInColFilled = 1;
            } else {
                row += 2;
            }
        } else {
            if(cellsInColFilled >= 5) {
                col += 1;
                row = 1;
                nowInAShortCol = true;
                cellsInColFilled = 1;
            } else {
                row += 2;
            }
        }
    }
}
