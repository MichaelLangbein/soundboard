package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.sound.SoundWrapper;
import org.langbein.michael.soundboard.utils.Complex;
import org.langbein.michael.soundboard.utils.FFT;
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
    private Key[] keys;
    private int l;
    private int deltaX;
    private int deltaY;
    private int height;
    private int width;



    public Board(float baseFreq, int sideLength, SoundWrapper sw){

        this.sw = sw;

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
            Key key = new Key(pos.x, pos.y, l, freq, k, sw);
            key.setFillColor(123, (int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random()));
            keys[k] = key;
        }
    }

    public void update(long delta) {

        // Step 1: get data from mic
        sw.fetchNewBatch(delta);

        // Step 2: analyse current batch and highlight keys accordingly
        short[] currentBatch = sw.getCurrentBatch();
        // analyseInputWithFFT(currentBatch, delta); // <---- too slow!
        // analyseInputOnKeys(currentBatch); // <---- better, but still too slow!


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


    private void analyseInputOnKeys(short[] currentBatch) {

        int batchSize = currentBatch.length;
        double[] rawAmps = new double[keys.length];
        if(batchSize > 0){

            double ampTotal = 0.0;
            for(int k = 0; k < keys.length; k++) {
                double amp = keys[k].ownAmplitude(currentBatch);
                rawAmps[k] = amp;
                ampTotal += amp;
            }

            double ampNormed;
            for(int k = 0; k < keys.length; k++) {
                ampNormed = 0;
                if(ampTotal > 0.0){
                    ampNormed = rawAmps[k] / ampTotal;
                }
                keys[k].lightsOn(ampNormed);
                Log.d("update loop", "Key with frequency "+ keys[k].getFrequency() +" has amplitude "+ ampNormed);
            }
        }
    }

    private void analyseInputWithFFT(short[] currentBatch, long delta) {

        int batchSize = currentBatch.length;
        double[] rawAmps = new double[keys.length];

        if(batchSize>0){
            Complex[] currentBatchComplex = Complex.transformToComplex(currentBatch);
            Log.d("update loop", "Just did transform to complex");
            Complex[] currentBatchAmplitudes = FFT.paddedFft(currentBatchComplex); // This is what takes too long. Needs 500MB memory, but only has 60 initially. Eventually causes IllegalStateException: Queue full
            Log.d("update loop", "Just did fft");
            double[] currentBatchFrequencies = FFT.getFrequencies(batchSize, delta/batchSize);
            double[] currentBatchIntensity = associateAmplitudeWithKeys(currentBatchAmplitudes, currentBatchFrequencies);
            for(int k = 0; k<nKeys; k++){
                keys[k].lightsOn(currentBatchIntensity[k]);
            }
        }
    }

    /*
     * @TODO: Bisher ist Zuweisung f < key.getF . Besser: f < key.getF + delta
     */
    private double[] associateAmplitudeWithKeys(Complex[] currentBatchAmplitudes, double[] currentBatchFrequencies) {
        double[] keyAmplitudes = new double[keys.length];
        double totalSum = 0;
        int F = currentBatchFrequencies.length;

        // Filling key's amplitudes by all proximate fourier-slots.
        int currentKeyIndex = 0;
        Key currentKey = keys[currentKeyIndex];
        for(int f = 0; f<F; f++){
            double currentFrequency = currentBatchFrequencies[f];
            while(currentFrequency < currentKey.getFrequency() && currentKeyIndex < keys.length - 1){
                currentKeyIndex += 1;
                currentKey = keys[currentKeyIndex];
            }
            double power = currentBatchAmplitudes[f].getPower();
            keyAmplitudes[currentKeyIndex] += power;
            totalSum += power;
        }

        // Normalizing
        for(int k = 0; k<keyAmplitudes.length; k++) {
            keyAmplitudes[k] = keyAmplitudes[k]/totalSum;
        }

        return keyAmplitudes;
    }
}
