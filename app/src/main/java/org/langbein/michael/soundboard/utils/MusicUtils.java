package org.langbein.michael.soundboard.utils;


public class MusicUtils {


    public static float getNthTone(float base, float n) {
        return (float) (base * Math.pow(2.0, (n/12.0)));
    }

    public static short[] makeWave(int len, double freq, double amplitude, double sampleRate, double offset) {
        short[] data = new short[len];

        float angular_frequency = (float)(2 * Math.PI * freq / sampleRate );
        double angle = offset;
        for(int i = 0; i<len; i++) {
            data[i] = (short)( amplitude * ((float) Math.sin(angle)));
            angle += angular_frequency;
        }
        return data;
    }

    public static double calcOffset(int len, double freq, double sampleRate, double lastOffset) {
        double angle = len * (2 * Math.PI * freq / sampleRate) + lastOffset;
        double offset = angle % (2*Math.PI);
        return offset;
    }


}
