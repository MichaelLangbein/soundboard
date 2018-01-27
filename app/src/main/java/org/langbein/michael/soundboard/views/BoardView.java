package org.langbein.michael.soundboard.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.langbein.michael.soundboard.scenes.SceneLogic;
import org.langbein.michael.soundboard.workers.BoardRenderThread;


/**
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

    public BoardView(Context context, SceneLogic sl) {
        super(context);
        this.sl = sl;
        this.brt = new BoardRenderThread(this, sl);
    }

    public void startRendering() {
        brt.enableRendering();
        brt.start();
    }

    public void stopRendering() {
        brt.disableRendering();
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
}
