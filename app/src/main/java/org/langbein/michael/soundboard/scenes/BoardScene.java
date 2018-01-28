package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.Board;


public class BoardScene implements SceneLogic {


    private Board board;

    public BoardScene() {
        board = new Board(220, 50);
    }

    @Override
    public void update(long delta) {
        board.update(delta);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRGB(20, 20, 20);
        board.draw(canvas);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return board.onTouch(event);
    }
}
