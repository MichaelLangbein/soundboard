package org.langbein.michael.soundboard.utils;


public class MusicUtils {

    public static final int TIMBRE_SINEWAVE = 0;
    public static final int TIMBRE_PIANO = 1;


    public static float getNthTone(float base, float n) {
        return (float) (base * Math.pow(2.0, (n/12.0)));
    }

    public static short[] makeWave(int len, double freq, double amplitude, double sampleRate, double offset, int timbre) {
        // @TODO:
        short[] data;
        switch (timbre){
            case TIMBRE_SINEWAVE:
                data = makeWaveSimple(len, freq, amplitude, sampleRate, offset);
                break;
            case TIMBRE_PIANO:
                short[] data0 = makeWaveSimple(len, 0.5 * freq, 0.6 * amplitude, sampleRate, offset);
                short[] data1 = makeWaveSimple(len, freq, amplitude, sampleRate, offset);
                short[] data2 = makeWaveSimple(len, 2 * freq, 0.5 * amplitude, sampleRate, offset);
                data = add(data0, data1);
                data = add(data, data2);
                break;
            default:
                data = makeWaveSimple(len, freq, amplitude, sampleRate, offset);
                break;
        }
        return data;
    }

    private static short[] add(short[] data1, short[] data2) {
        short[] out = new short[data1.length];
        for (int i = 0; i < data1.length; i++) {
            out[i] = (short) (data1[i] + data2[i]);
        }
        return out;
    }

    private static short[] makeWaveSimple(int len, double freq, double amplitude, double sampleRate, double offset) {
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
