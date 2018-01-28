package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by michael on 27.01.18.
 */

public class Key implements Renderable, Touchable {

    private Hexagon hex;

    public Key(int posX, int posY, int len, float freq) {
        hex = new Hexagon(posX, posY, len);
    }

    @Override
    public void update(long delta) {

    }

    @Override
    public void draw(Canvas canvas) {
        hex.draw(canvas);
    }

    @Override
    public void onTouch(MotionEvent event) {

    }
}
