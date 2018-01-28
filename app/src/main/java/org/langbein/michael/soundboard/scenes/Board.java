package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.Key;

/**
 * Created by michael on 28.01.18.
 */

public class Board {

    private int nKeys;
    private float baseFreq;
    private Key[] keys;

    public Board(int nKeys, float baseFreq){

        this.nKeys = nKeys;
        this.baseFreq = baseFreq;

        keys = new Key[nKeys];
        for(int k = 0; k < nKeys; k++) {
            Vec2<Integer> pos = getPos(k);
            float freq = MusicUtils.getNthTone(baseFreq, k);
            keys[k] = new Key(pos.x, pos.y, 30, freq);
        }
    }

    public static Vec2<Integer> getRowsAndCols() {

    }

    public static Vec2<Integer> getPos(int nr) {

    }

    public void update(long delta) {
    }

    public boolean onTouch(MotionEvent event) {
        return true;
    }

    public void draw(Canvas canvas) {
        for(int k = 0; k < nKeys; k++){
            keys[k].draw(canvas);
        }
    }
}
