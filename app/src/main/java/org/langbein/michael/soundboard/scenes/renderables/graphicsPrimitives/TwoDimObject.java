package org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by michael on 28.01.18.
 */

public interface TwoDimObject {
    public void draw(Canvas canvas);
    public boolean isInsideBounds(MotionEvent me);
}
