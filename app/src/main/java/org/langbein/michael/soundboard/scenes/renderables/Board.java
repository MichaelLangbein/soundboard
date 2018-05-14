package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.sound.SoundWrapper;
import org.langbein.michael.soundboard.utils.FrequencyAnalysis;
import org.langbein.michael.soundboard.utils.Vec2;
import org.langbein.michael.soundboard.utils.GridStateMachine;
import org.langbein.michael.soundboard.utils.MusicUtils;

import static java.lang.Math.random;

/**
 * Created by michael on 28.01.18.
 */

public class Board {

    private SoundWrapper sw;
    private int nKeys;
    private float[] keyFrequencies;
    private Key[] keys;
    private int l;
    private int deltaX;
    private int deltaY;
    private int height;
    private int width;

    // Exposed settings
    public static final int SETTING_TIMBRE = 0;


    public Board(float baseFreq, int sideLength, SoundWrapper sw){

        this.sw = sw;

        nKeys = 49;

        l = sideLength;
        deltaX = (int) (l * Math.cos(Math.PI / 6));
        deltaY = (int) (1.5 * l);
        height = 7 * deltaY;
        width = 14 * deltaX;

        keyFrequencies = new float[nKeys];
        for(int k = 0; k < nKeys; k++) {
            keyFrequencies[k] = MusicUtils.getNthTone(baseFreq, k);
        }

        keys = new Key[nKeys];
        for(int k = 0; k < nKeys; k++) {
            Vec2<Integer> pos = getPos(k);
            Key key = new Key(pos.x, pos.y, l, keyFrequencies[k], k, sw);
            key.setFillColor(123, (int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random()));
            keys[k] = key;
        }
    }

    public void update(long delta) {

        // Step 1: get data from mic
        sw.fetchNewBatch(delta);

        // Step 2: analyse current batch and highlight keys accordingly
        short[] currentBatch = sw.getCurrentBatch();
        int sampleRate = sw.getSampleRate();
        int thinning = 100;
        if(currentBatch.length > 0) {
            short[] smallerBatch = FrequencyAnalysis.downsample(currentBatch, thinning);
            double[] lights = FrequencyAnalysis.analyseInputOnKeys(smallerBatch, keyFrequencies, sampleRate/thinning);
            double[] lightsP = FrequencyAnalysis.postprocess(lights);
            for(int k = 0; k < nKeys; k++) {
                keys[k].lightsOn(lightsP[k] / 30.0);
            }
        }

        // Step 3: allow keys to modify data
        for(int k = 0; k < nKeys; k++) {
            keys[k].update(delta);
        }

        // Step 4: send data to amp
        try {
            sw.flushCurrentBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void setTimbre(int timbre) {
        for(int k = 0; k < nKeys; k++){
            keys[k].setTimbre(timbre);
        }
    }
}
