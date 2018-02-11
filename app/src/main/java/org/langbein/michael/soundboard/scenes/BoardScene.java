package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.Board;
import org.langbein.michael.soundboard.scenes.utils.SoundOutWrapper;
import org.langbein.michael.soundboard.workers.SoundOutThread;


public class BoardScene implements SceneLogic {


    private Board board;
    private SoundOutThread soundOut;

    public BoardScene() {
        soundOut = new SoundOutThread();
        soundOut.start();
        int bufferSize = (int) (0.050 * soundOut.getSampleRate());
        Log.d("Basic", "bufferSize at 50ms frameRate equals " + bufferSize); // expected to be 1444 * 50/17 = 4247
        SoundOutWrapper sow = new SoundOutWrapper(soundOut, bufferSize);
        board = new Board(220, 50, sow);
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
