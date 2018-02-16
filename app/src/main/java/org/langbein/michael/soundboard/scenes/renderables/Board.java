package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.utils.SoundInWrapper;
import org.langbein.michael.soundboard.scenes.utils.SoundOutWrapper;
import org.langbein.michael.soundboard.workers.SoundOutThread;
import org.langbein.michael.soundboard.scenes.utils.Vec2;
import org.langbein.michael.soundboard.scenes.utils.GridStateMachine;
import org.langbein.michael.soundboard.scenes.utils.MusicUtils;

import static java.lang.Math.random;

/**
 * Created by michael on 28.01.18.
 */

public class Board {

    private int nKeys;
    private Key[] keys;
    private int l;
    private int deltaX;
    private int deltaY;
    private int height;
    private int width;
    private SoundInWrapper siw;
    private SoundOutWrapper sow;


    public Board(float baseFreq, int sideLength, SoundInWrapper siw, SoundOutWrapper sow){

        this.siw = siw;
        this.sow = sow;

        nKeys = 49;

        l = sideLength;
        deltaX = (int) (l * Math.cos(Math.PI / 6));
        deltaY = (int) (1.5 * l);
        height = 7 * deltaY;
        width = 14 * deltaX;


        keys = new Key[nKeys];
        for(int k = 0; k < nKeys; k++) {
            Vec2<Integer> pos = getPos(k);
            float freq = MusicUtils.getNthTone(baseFreq, k);
            Key key = new Key(pos.x, pos.y, l, freq, k, siw, sow);
            key.setFillColor(123, (int)(255 * random()), (int)(255 * random()), (int)(255 * random()));
            keys[k] = key;
        }
    }

    public void update(long delta) {
        for(int k = 0; k < nKeys; k++) {
            keys[k].update(delta);
        }
        sow.flush();
    }

    public boolean onTouch(MotionEvent event) {
        for(int k = 0; k < nKeys; k++) {
            keys[k].onTouch(event);
        }
        return true;
    }

    public void draw(Canvas canvas) {
        for(int k = 0; k < nKeys; k++){
            keys[k].draw(canvas);
        }
    }


    public Vec2<Integer> getPos(int nr) {
        GridStateMachine gsm = new GridStateMachine();
        for(int i = 0; i < nr; i++) {
            gsm.oneStepFurther();
        }
        Vec2<Integer> indxPos = gsm.getCurrentPos(); // (row -> y, col -> x)

        int x = width - (14 - indxPos.y) * deltaX;
        int y = height - (indxPos.x) * deltaY;

        return new Vec2<Integer>(x + 2*l, y + 4*l);
    }
}
