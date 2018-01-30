package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.Board;
import org.langbein.michael.soundboard.scenes.utils.SoundOut;


public class BoardScene implements SceneLogic {


    private Board board;
    private SoundOut soundOut;

    public BoardScene() {
        soundOut = new SoundOut(44100, 17);
        soundOut.start();
        board = new Board(220, 50, soundOut);
    }

    @Override
    public void update(long delta) {
        Log.d("Basic", "BoardScene update is called with delta = " + delta);
        board.update(delta);
        soundOut.playAndEmptyBuffer(delta);
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
