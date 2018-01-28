package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.Key;
import org.langbein.michael.soundboard.scenes.renderables.MyRect;


public class BoardScene implements SceneLogic {

    private MyRect lilRect;
    private Key lilKey;

    public BoardScene() {
        lilRect = new MyRect(100, 100, 30, 30 );
        lilKey = new Key(200, 200, 200, 440);
    }

    @Override
    public void update(long delta) {
        lilRect.update(delta);
        lilKey.update(delta);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRGB(20, 20, 20);
        lilRect.draw(canvas);
        lilKey.draw(canvas);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        lilRect.onTouch(event);
        return true;
    }
}
