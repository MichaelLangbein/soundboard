package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.Board;
import org.langbein.michael.soundboard.workers.SoundOutThread;


public class BoardScene implements SceneLogic {


    private Board board;
    private SoundOutThread soundOut;

    public BoardScene() {
        soundOut = new SoundOutThread(44100, 17);
        soundOut.start();
        board = new Board(220, 50, soundOut);
    }

    @Override
    public void update(long delta) {
        board.update(delta);
        // soundOut.playAndEmptyBuffer(delta); <-- this is not done. Will be done from soundthread.
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
