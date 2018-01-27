package org.langbein.michael.soundboard.scenes.renderables;


import android.graphics.Canvas;

public interface Renderable {
    public void update(long delta);
    public void draw(Canvas canvas);
}
