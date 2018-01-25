package org.langbein.michael.soundboard;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import org.langbein.michael.soundboard.scenes.SceneLogic;

public class RenderThread extends Thread {

    private SceneLogic sceneLogic;
    private Context context;
    private SurfaceHolder surfaceHolder;
    private boolean running;

    public RenderThread(Context context, SurfaceHolder surfaceHolder, SceneLogic sceneLogic) {
        this.context = context;
        this.surfaceHolder = surfaceHolder;
        this.running = true;
        this.sceneLogic = sceneLogic;
    }

    @Override
    public void run() {
        while(running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            sceneLogic.draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public SceneLogic getLogic() {
        return sceneLogic;
    }

    public void setInactive() {
        running = false;
    }
}
