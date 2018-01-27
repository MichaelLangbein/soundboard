package org.langbein.michael.soundboard.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.langbein.michael.soundboard.scenes.SceneLogic;
import org.langbein.michael.soundboard.workers.BoardRenderThread;



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
