package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;



public interface SceneLogic {

    public boolean onTouch(MotionEvent event);

    public void update();

    public void draw(Canvas canvas);
}
