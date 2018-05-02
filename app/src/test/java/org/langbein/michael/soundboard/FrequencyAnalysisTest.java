package org.langbein.michael.soundboard;

import android.media.AudioManager;
import android.media.AudioTrack;

import org.junit.Test;
import org.langbein.michael.soundboard.utils.FrequencyAnalysis;
import org.langbein.michael.soundboard.utils.MusicUtils;

import static org.junit.Assert.assertTrue;


public class FrequencyAnalysisTest {

    @Test
    public void testAnalyseInputOnKeys() {
        float[] frequencies = createFrequencies(220, 49);

        int sampleRate = 48000;
        short[] lowerASample = MusicUtils.makeWave(sampleRate/2, 220, Short.MAX_VALUE/2.0, sampleRate, Math.PI / 4.0);

        double[] lights = FrequencyAnalysis.analyseInputOnKeys(lowerASample, frequencies);
        int maxIndex = getIndexMaximum(lights);

        assertTrue("Oh oh! MaxIndex ist nicht 0, sondern " + maxIndex, maxIndex == 0);
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
