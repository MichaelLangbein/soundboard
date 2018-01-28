package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;



public class Key implements Renderable, Touchable {

    private Hexagon hex;
    private Text text;

    public Key(int posX, int posY, int len, float freq) {
        hex = new Hexagon(posX, posY, len);
        text = new Text(posX, posY, String.valueOf(freq), 20);
    }

    @Override
    public void update(long delta) {

    }

    @Override
    public void draw(Canvas canvas) {
        hex.draw(canvas);
        text.drawWithPosInMiddle(canvas);
    }

    @Override
    public void onTouch(MotionEvent event) {

    }
}
