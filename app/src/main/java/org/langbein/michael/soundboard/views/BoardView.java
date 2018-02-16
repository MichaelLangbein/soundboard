package org.langbein.michael.soundboard.views;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.langbein.michael.soundboard.scenes.BoardScene;
import org.langbein.michael.soundboard.scenes.SceneLogic;
import org.langbein.michael.soundboard.scenes.utils.SoundInWrapper;
import org.langbein.michael.soundboard.scenes.utils.SoundOutWrapper;
import org.langbein.michael.soundboard.workers.BoardRenderThread;
import org.langbein.michael.soundboard.workers.SoundInThread;
import org.langbein.michael.soundboard.workers.SoundOutThread;


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
    private SoundInThread sit;
    private SoundOutThread sot;

    public BoardView(Context context) {
        super(context);

        sit = new SoundInThread();
        SoundInWrapper siw = new SoundInWrapper(sit);

        sot = new SoundOutThread();
        int bufferSize = (int) (0.050 * sot.getSampleRate());
        SoundOutWrapper sow = new SoundOutWrapper(sot, bufferSize);

        SceneLogic boardScene = new BoardScene(siw, sow);
        this.sl = boardScene;
        this.brt = new BoardRenderThread(this, sl);
    }

    public void startRendering() {
        sot.start();
        brt.enableRendering();
        brt.start();
    }

    public void stopRendering() {
        brt.disableRendering();
        sot.close();
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
