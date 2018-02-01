package org.langbein.michael.soundboard.workers;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;


public class SoundOutThread extends Thread {

    private int minSize;
    private AudioTrack track;
    private short[] buffer;
    private int loopTime;

    public SoundOutThread(int sampleRate, double frameTime) {

        loopTime = 17;

        minSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize,
                AudioTrack.MODE_STREAM);

        buffer = new short[minSize];


    }

    @Override
    public void run() {
        track.play();

        while(true) {
            playAndEmptyBuffer();
            try {
                Thread.sleep(loopTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void playAndEmptyBuffer() {
        track.write(buffer, 0, buffer.length);
        for(int i = 0; i < minSize; i++) {
            buffer[i] = 0;
        }
    }

    public void addToBuffer(short[] data) {
        if(data.length >= minSize) { //  if input is bigger than buffer, discard rest of input
            for(int i = 0; i < minSize; i++) {
                buffer[i] += data[i];
            }
        } else { // if input is smaller than buffer, leave end of buffer empty.
            for(int i = 0; i < data.length; i++) {
                buffer[i] += data[i];
            }
        }

    }


    public int getBufferSize() { return minSize; }

    public int getSampleRate() { return track.getSampleRate(); }
}
