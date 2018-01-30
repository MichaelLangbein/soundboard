package org.langbein.michael.soundboard.scenes.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;


public class SoundOut extends Thread {

    private int sampleRate;
    private double frameTime;
    private int bufferSize;
    private int minSize;
    private AudioTrack track;
    private short[] buffer;

    public SoundOut (int sampleRate, double frameTime) {
        this.sampleRate = sampleRate;
        this.frameTime = frameTime;
        this.bufferSize = (int) (frameTime * sampleRate);

        minSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize,
                AudioTrack.MODE_STREAM);

        buffer = new short[bufferSize];


    }

    @Override
    public void run() {
        track.play();
        while(true){
            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addToBuffer(short[] data) {
        Log.d("Basic", "Data is being added to buffer");
        if(data.length >= bufferSize) { //  if input is bigger than buffer, discard rest of input
            for(int i = 0; i < bufferSize; i++) {
                buffer[i] += data[i];
            }
        } else { // if input is smaller than buffer, leave end of buffer empty
            for(int i = 0; i < data.length; i++) {
                buffer[i] += data[i];
            }
        }

    }

    /**
     * Track.write will not block.
     * For this reason, we don't need a separate thread for the sound-out class.
     */
    public void playAndEmptyBuffer(long delta) {
        track.write(buffer, 0, buffer.length);
        for(int i = 0; i < bufferSize; i++) {
            buffer[i] = 0;
        }
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getBufferSize() { return bufferSize; }
}
