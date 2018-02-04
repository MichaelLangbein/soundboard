package org.langbein.michael.soundboard.workers;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;


public class SoundOutThread extends Thread {

    private int bufferSize;
    private AudioTrack track;
    private short[] buffer;
    private long frameTimeInMs;

    public SoundOutThread(int sampleRate, long frameTimeInMs) {

        this.frameTimeInMs = frameTimeInMs;

        // Say we have a loopTime of 17 ms. At a frameRate of 44100 Hz and Mono that makes 750 samples.
        double frameTimeInS = (frameTimeInMs * 0.001);
        bufferSize = (int) (sampleRate * frameTimeInS) + 1;
        buffer = new short[bufferSize];

        // minSize is in bytes. Is the size of one chunk of data the audioplayer consumes.
        // because we loop every 17 ms, we always feed the player a buffer large enough for several chunks.
        int minSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize,
                AudioTrack.MODE_STREAM);




    }

    @Override
    public void run() {
        track.play();

        long start, end, workTime, sleepTime;
        while(true) {
            start = System.currentTimeMillis();
            playAndEmptyBuffer();
            end = System.currentTimeMillis();
            workTime = end - start;
            sleepTime = frameTimeInMs - workTime;
            if(sleepTime > 0){
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void playAndEmptyBuffer() {
        track.write(buffer, 0, buffer.length);
        for(int i = 0; i < bufferSize; i++) {
            buffer[i] = 0;
        }
    }

    public void addToBuffer(short[] data) {
        if(data.length >= bufferSize) { //  if input is bigger than buffer, discard rest of input
            for(int i = 0; i < bufferSize; i++) {
                buffer[i] += data[i];
            }
        } else { // if input is smaller than buffer, leave end of buffer empty.
            for(int i = 0; i < data.length; i++) {
                buffer[i] += data[i];
            }
        }

    }


    public int getBufferSize() { return bufferSize; }

    public int getSampleRate() { return track.getSampleRate(); }
}
