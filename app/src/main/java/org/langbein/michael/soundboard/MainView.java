package org.langbein.michael.soundboard;


import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.langbein.michael.soundboard.scenes.SceneLogic;

/**
 * This class is there to present the SurfaceHolder as well as any touch- or system-events
 * to the RenderThread.
 */
public class MainView extends SurfaceView implements SurfaceHolder.Callback {

    private RenderThread renderThread;

    public MainView(Context context, SceneLogic sceneLogic) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        renderThread = new RenderThread(context, surfaceHolder, sceneLogic);
    }

    public void startRenderThread() {
        renderThread.start();
    }

    public void stopRenderThread() {
        renderThread.setInactive();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return renderThread.getLogic().onTouch(event);
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
