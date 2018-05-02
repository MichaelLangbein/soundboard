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

        short[] middleALittleOffSample = MusicUtils.makeWave(sampleRate/2, 451, Short.MAX_VALUE/2.0, sampleRate, 1.1);
        double[] lights = FrequencyAnalysis.analyseInputOnKeys(middleALittleOffSample, frequencies, sampleRate);
        int maxIndex = getIndexMaximum(lights);
        assertTrue("Oh oh! MaxIndex für middleALittleOffSample ist nicht 12, sondern " + maxIndex, maxIndex == 12);
    }

    @Test
    public void testChangeDuringSample() {
        // TODO
    }

    @Test
    public void testChord() {
        // TODO
    }

    @Test
    public void testMultiple() {
        // TODO
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
}
