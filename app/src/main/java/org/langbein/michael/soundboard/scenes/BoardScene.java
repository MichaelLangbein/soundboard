package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.Key;
import org.langbein.michael.soundboard.sound.OscWrapperThread;
import org.langbein.michael.soundboard.utils.GridStateMachine;
import org.langbein.michael.soundboard.utils.MusicUtils;
import org.langbein.michael.soundboard.utils.Vec2;


public class BoardScene implements SceneLogic {


    private OscWrapperThread sw;
    private int nKeys;
    private float[] keyFrequencies;
    private Key[] keys;
    private int l;
    private int deltaX;
    private int deltaY;
    private int height;
    private int width;

    public BoardScene(OscWrapperThread sw) {
        this.sw = sw;

        nKeys = 49;

        l = 50;
        deltaX = (int) (l * Math.cos(Math.PI / 6));
        deltaY = (int) (1.5 * l);
        height = 7 * deltaY;
        width = 14 * deltaX;

        keyFrequencies = new float[nKeys];
        for(int k = 0; k < nKeys; k++) {
            keyFrequencies[k] = MusicUtils.getNthTone(220, k);
        }

        keys = new Key[nKeys];
        for(int k = 0; k < nKeys; k++) {
            Vec2<Integer> pos = getPos(k);
            Key key = new Key(pos.x, pos.y, l, keyFrequencies[k], k, sw);
            key.setFillColor(123, (int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random()));
            keys[k] = key;
        }
    }

    @Override
    public void update(long delta) {

        // allow keys to do their thing
        for(int k = 0; k < nKeys; k++) {
            keys[k].update(delta);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRGB(20, 20, 20);
        for(int k = 0; k < nKeys; k++){
            keys[k].draw(canvas);
        }
    }

    @Override
    public void settings(int settingType, int settingValue) {

    }

    @Override
    public boolean onTouch(MotionEvent event) {
        for(int k = 0; k < nKeys; k++) {
            keys[k].onTouch(event);
        }
        return true;
    }

    private Vec2<Integer> getPos(int nr) {
        GridStateMachine gsm = new GridStateMachine();
        for(int i = 0; i < nr; i++) {
            gsm.oneStepFurther();
        }
        Vec2<Integer> indxPos = gsm.getCurrentPos(); // (row -> y, col -> x)

        int x = width - (14 - indxPos.y) * deltaX;
        int y = height - (indxPos.x) * deltaY;

        int offsetFromLeft = 4*l;
        int offsetFromTop = l;

        return new Vec2<Integer>(x + offsetFromLeft, y + offsetFromTop);
    }
}
