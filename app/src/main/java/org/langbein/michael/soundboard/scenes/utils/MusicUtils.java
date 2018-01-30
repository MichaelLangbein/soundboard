package org.langbein.michael.soundboard.scenes.utils;


public class MusicUtils {

    public static float getNthTone(float base, float n) {
        return (float) (base * Math.pow(2.0, (n/12.0)));
    }

    public static short[] makeWave(int len, double freq, double amplitude, double sampleRate) {
        short[] data = new short[len];
        for(int i = 0; i<len; i++) {
            data[i] = (short) (amplitude * Math.sin(2 * Math.PI * freq * i / sampleRate));
        }
        return data;
    }
}
