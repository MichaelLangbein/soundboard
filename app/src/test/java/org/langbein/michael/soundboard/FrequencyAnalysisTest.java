package org.langbein.michael.soundboard;

import android.media.AudioManager;
import android.media.AudioTrack;

import org.junit.Test;
import org.langbein.michael.soundboard.utils.FrequencyAnalysis;
import org.langbein.michael.soundboard.utils.MusicUtils;

import static org.junit.Assert.assertTrue;


public class FrequencyAnalysisTest {

    @Test
    public void testSimple() {
        float[] frequencies = createFrequencies(220, 49);
        int sampleRate = 48000;

        short[] lowerASample = MusicUtils.makeWave(sampleRate / 2, 220, Short.MAX_VALUE / 2.0, sampleRate, 0);
        double[] lights = FrequencyAnalysis.analyseInputOnKeys(lowerASample, frequencies, sampleRate);
        int maxIndex = getIndexMaximum(lights);
        assertTrue("Oh oh! MaxIndex für lowerA ist nicht 0, sondern " + maxIndex, maxIndex == 0);
    }

    @Test
    public void testOctaveHigher() {
        float[] frequencies = createFrequencies(220, 49);
        int sampleRate = 48000;

        short[] middleASample = MusicUtils.makeWave(sampleRate/2, 440, Short.MAX_VALUE/2.0, sampleRate, 0);
        double[] lights = FrequencyAnalysis.analyseInputOnKeys(middleASample, frequencies, sampleRate);
        int maxIndex = getIndexMaximum(lights);
        assertTrue("Oh oh! MaxIndex für midA ist nicht 12, sondern " + maxIndex, maxIndex == 12);
        }

    @Test
    public void testOffset() {
        float[] frequencies = createFrequencies(220, 49);
        int sampleRate = 48000;

        short[] middleASampleOffset = MusicUtils.makeWave(sampleRate/2, 440, Short.MAX_VALUE/2.0, sampleRate, 1.1);
        double[] lights = FrequencyAnalysis.analyseInputOnKeys(middleASampleOffset, frequencies, sampleRate);
        int maxIndex = getIndexMaximum(lights);
        assertTrue("Oh oh! MaxIndex für midAoffset ist nicht 12, sondern " + maxIndex, maxIndex == 12);
    }

    @Test
    public void testBluenote() {
        float[] frequencies = createFrequencies(220, 49);
        int sampleRate = 48000;

        short[] middleALittleOffSample = MusicUtils.makeWave(sampleRate/2, 451, Short.MAX_VALUE/2.0, sampleRate, 0);
        double[] lights = FrequencyAnalysis.analyseInputOnKeys(middleALittleOffSample, frequencies, sampleRate);
        int maxIndex = getIndexMaximum(lights);
        assertTrue("Oh oh! MaxIndex für middleALittleOffSample ist nicht 12, sondern " + maxIndex, maxIndex == 12);
    }

    @Test
    public void testChangeDuringSample() {
        float[] frequencies = createFrequencies(220, 49);
        int sampleRate = 48000;

        short[] midASample = MusicUtils.makeWave(3*sampleRate/4, 440.00, Short.MAX_VALUE/2.0, sampleRate, 0);
        short[] midHSample = MusicUtils.makeWave(1*sampleRate/4, 493.88, Short.MAX_VALUE/2.0, sampleRate, 0);
        short[] sample = concatArrays(midASample, midHSample);

        double[] lights = FrequencyAnalysis.analyseInputOnKeys(sample, frequencies, sampleRate);
        int indxMax = getIndexMaximum(lights);
        int indxSecond = getIndexMaximum(spliceOutIndex(lights, indxMax));
        assertTrue("Oh oh! Longer tone is not 12, but " + indxMax, indxMax == 12);
        assertTrue("Oh oh! Shorter tone is not 14, but " + indxSecond, indxSecond == 14);
    }

    @Test
    public void testChord() {
        float[] frequencies = createFrequencies(220, 49);
        int sampleRate = 48000;

        short[] grund = MusicUtils.makeWave(sampleRate/2, 440.00, Short.MAX_VALUE/2.0, sampleRate, 0);
        short[] terz  = MusicUtils.makeWave(sampleRate/2, 523.25, Short.MAX_VALUE/2.0, sampleRate, 0);
        short[] quint = MusicUtils.makeWave(sampleRate/2, 587.33, Short.MAX_VALUE/2.0, sampleRate, 0);
        short[] all = addArrays(grund, terz);
        all = addArrays(all, quint);

        double[] lights = FrequencyAnalysis.analyseInputOnKeys(all, frequencies, sampleRate);
        printArray(lights);
    }

    @Test
    public void testMultiple() {
        // TODO
    }

    private double[] spliceOutIndex(double[] array, int index) {
        double[] out = new double[array.length - 1];

        int outIndex = 0;
        for(int inIndex = 0; inIndex < array.length; inIndex++) {
            if(inIndex == index) continue;
            out[outIndex] = array[inIndex];
        }

        return out;
    }

    private short[] addArrays(short[] arr1, short[] arr2) {
        int len = Math.max(arr1.length, arr2.length);
        short[] out = new short[len];

        for(int k = 0; k<len; k++) {
            short a = k < arr1.length ? arr1[k] : 0;
            short b = k < arr2.length ? arr2[k] : 0;
            out[k] = (short) (a + b);
        }

        return out;
    }

    private short[] concatArrays(short[] arr1, short[] arr2) {
        int len = arr1.length + arr2.length;
        short[] out = new short[len];

        int indxOut = 0;

        for(int indx1 = 0; indx1 < arr1.length; indx1++) {
            out[indxOut] = arr1[indx1];
            indxOut += 1;
        }

        for(int indx2 = 0; indx2 < arr2.length; indx2++) {
            out[indxOut] = arr2[indx2];
            indxOut += 1;
        }

        return out;
    }

    private int getIndexMaximum(double[] array) {
        int indexMaximum = 0;
        double currentMaximum = 0;
        for(int i = 0; i<array.length; i++) {
            if(array[i] > currentMaximum) {
                indexMaximum = i;
                currentMaximum = array[i];
            }
        }
        return indexMaximum;
    }

    private float[] createFrequencies(float baseFreq, int nKeys) {
        float[] keyFrequencies = new float[nKeys];
        for(int k = 0; k < nKeys; k++) {
            keyFrequencies[k] = MusicUtils.getNthTone(baseFreq, k);
        }
        return keyFrequencies;
    }

    private void printArray(double[] array) {
        for(int k = 0; k < array.length; k++){
            System.out.println(k + ": " + array[k]);
        }
    }
}
