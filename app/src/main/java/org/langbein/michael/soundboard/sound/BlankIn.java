package org.langbein.michael.soundboard.sound;

import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by michael on 17.02.18.
 */

public class BlankIn implements SoundIn {

    private final int sampleRate;

    public BlankIn() {
        sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void start() {

    }

    @Override
    public void close() {

    }

    @Override
    public short[] takeFromBuffer(long delta) {
        int n = (int)(sampleRate * delta / 1000);
        return new short[n];
    }

    @Override
    public short[] takeFromBuffer(int n) {
        return new short[n];
    }

    @Override
    public int getSampleRate() {
        return sampleRate;
    }
}
