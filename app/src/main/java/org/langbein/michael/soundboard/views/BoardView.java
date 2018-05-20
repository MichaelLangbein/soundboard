package org.langbein.michael.soundboard.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.langbein.michael.soundboard.scenes.BoardScene;
import org.langbein.michael.soundboard.scenes.SceneLogic;
import org.langbein.michael.soundboard.sound.MidiWrapper;
import org.langbein.michael.soundboard.sound.OscWrapperThread;


/**
 * Coming from a web background, I like to think of activities and views as server- and client-side.
 * An activity is like the server, in that it does the background logic and only talks when called.
 * A view is like the client side, in that it contains the render loop and calls upon the server
 * when necessary.
 *
 *
 * A surfaceView is a special kind of view
 * that can expose a surface-holder, and with that a surface, and with that a canvas.
 * This is important, because the SurfaceView typically creates a custom thread that it exposes
 * the canvas to.
 * Typically, the SurfaceView is the owner of the thread, so that it can call the threads methods
 * when a TouchEvent is registered.
 */
public class BoardView extends SurfaceView implements SurfaceHolder.Callback {

    private SceneLogic sl;
    private BoardRenderThread brt;
    private OscWrapperThread sw;


    public BoardView(Context context, OscWrapperThread sw) {
        super(context);
        this.sw = sw;
        this.sl = new BoardScene(sw);;
        this.brt = new BoardRenderThread(this, sl);
    }

    public void startRendering() {
        brt.enableRendering();
        brt.start();
    }

    public void stopRendering() {
        brt.close();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return brt.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void settings(int type, int value) {
        sl.settings(type, value);
    }
}
