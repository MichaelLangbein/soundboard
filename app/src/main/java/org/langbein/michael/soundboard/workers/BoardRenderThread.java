package org.langbein.michael.soundboard.workers;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import org.langbein.michael.soundboard.scenes.SceneLogic;
import org.langbein.michael.soundboard.views.BoardView;

/**
 * This render thread takes
 *   - a surface view, to get the canvas and to be notified of touch events
 *   - a scene logic, that determines exactly *what* is supposed to be drawn onto the surface
 */

public class BoardRenderThread extends Thread {

    private BoardView boardView;
    private SurfaceHolder surfaceHolder;
    private SceneLogic scene;
    private boolean running;
    private long frameTime;

    public BoardRenderThread(BoardView bv, SceneLogic sc) {
        boardView = bv;
        scene = sc;
        surfaceHolder = bv.getHolder();
        running = true;
        frameTime = 17;
    }


    public void enableRendering() {
        running = true;
    }

    public void disableRendering() {
        running = false;
    }

    @Override
    public void run() {

        long updateDuration = 0;
        long sleepDuration = 0;

        while(running) {

            // Step 0 : how long did the loop take?
            long beforeUpdateAndRender = System.nanoTime();
            long delta = sleepDuration + updateDuration;

            // Step 1 : scene logic
            scene.update(delta);

            // Step 2: scene rendering
            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas != null) {
                scene.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            // Step 3: sleep for remainder of frameTime
            updateDuration = (System.nanoTime() - beforeUpdateAndRender) / 1000000L;
            Log.d("Basic", "Render thread: update and render calls took " + updateDuration + " milisecs");
            sleepDuration = Math.max(2, frameTime - updateDuration);
            try{
                Thread.sleep(sleepDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        return scene.onTouch(event);
    }



}
