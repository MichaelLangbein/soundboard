package org.langbein.michael.soundboard.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by michael on 17.02.18.
 */

public class BlankInThread extends Thread implements SoundIn {

    private final int sampleRate;
    private boolean alive;

    public BlankInThread(Context context) {
        sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        alive = true;
    }

    @Override
    public void run() {
        Log.d("Blank In Thread", "Blank in thread now running.");
        while(alive){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void soundInStart() {
        alive = true;
        if(!isAlive()) {
            start();
        }
    }

    @Override
    public void soundInStop() {
        alive = false;
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
