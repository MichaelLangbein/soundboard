package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;



public interface SceneLogic {

    public boolean onTouch(MotionEvent event);

    public void update(long delta);

    public void draw(Canvas canvas);
}
