package org.langbein.michael.soundboard;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import org.langbein.michael.soundboard.scenes.SceneLogic;

public class RenderThread extends Thread {

    private final long frameTime = 17;   // How long (in millisec) a frame may take
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
        long updateDuration = 0;
        long sleepDuration = 0;

        while(running) {

            // Step 0 : how long did the loop take?
            long beforeUpdateRender = System.nanoTime();
            long delta = sleepDuration + updateDuration;

            // Step 1 : scene logic
            sceneLogic.update(delta);

            // Step 2: scene rendering
            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas != null) {
                sceneLogic.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            // Step 3: sleep for remainder of frameTime
            updateDuration = (System.nanoTime() - beforeUpdateRender) / 1000000L;
            sleepDuration = Math.max(2, frameTime - updateDuration);
            try{
                Thread.sleep(sleepDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public SceneLogic getLogic() {
        return sceneLogic;
    }

    public void setInactive() {
        running = false;
    }
}
