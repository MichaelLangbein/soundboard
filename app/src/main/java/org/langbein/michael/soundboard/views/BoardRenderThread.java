package org.langbein.michael.soundboard.views;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import org.langbein.michael.soundboard.scenes.SceneLogic;

/**
 * This render thread takes
 *   - a surface view, to get the canvas and to be notified of touch events
 *   - a scene logic, that determines exactly *what* is supposed to be drawn onto the surface
 */

public class BoardRenderThread extends Thread {

    private BoardView boardView;
    private SurfaceHolder surfaceHolder;
    private SceneLogic scene;
    private boolean rendering;
    private boolean alive;
    private long frameTime;

    public BoardRenderThread(BoardView bv, SceneLogic sc) {
        boardView = bv;
        scene = sc;
        surfaceHolder = bv.getHolder();
        rendering = true;
        alive = true;
        frameTime = 50;
    }

    public void close() {
        disableRendering();
        alive = false;
    }

    public void enableRendering() {
        rendering = true;
    }

    public void disableRendering() {
        rendering = false;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("BoardRenderThread");

        long updateDuration = 0;
        long sleepDuration = 0;

        while(alive) {
            if(rendering) {


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
                sleepDuration = Math.max(2, frameTime - updateDuration);
                try{
                    Thread.sleep(sleepDuration);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        return scene.onTouch(event);
    }



}
