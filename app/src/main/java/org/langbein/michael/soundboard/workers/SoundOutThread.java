package org.langbein.michael.soundboard.workers;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * @TODO: Instead of accepting an arbitrary frame-time, accept a number of sound-chunks-per-frame value.
 */
public class SoundOutThread extends Thread {

    private int bufferSize;
    private AudioTrack track;
    private short[] prebuffer;
    private short[] buffer;
    private long frameTimeInMs;

    public SoundOutThread(long frameTimeInMs) {

        int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        this.frameTimeInMs = frameTimeInMs;
        double frameTimeInS = (frameTimeInMs * 0.001);
        bufferSize = (int) (sampleRate * frameTimeInS);
        buffer = new short[bufferSize]; // <--- To be sent to the sound hardware. May not be edited from outside
        prebuffer = new short[bufferSize]; // <--- To replace prebuffer. May be edited from outside.

        // minSize is in bytes. the size of one chunk of data that the audioplayer consumes.
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
        // Step 1: write buffer to sound out
        track.write(buffer, 0, buffer.length);
        for(int i = 0; i < bufferSize; i++) {
            // Step 2: replace buffer with prebuffer
            buffer[i] = prebuffer[i];
            // Step 3: empty prebuffer
            prebuffer[i] = 0;
        }
    }

    /**
     * data may only be added to prebuffer, nut to actual buffer.
     * This we we avoid the buffer being edited while it is on its way to the hardware.
     * @param data
     */
    public void addToBuffer(short[] data) {
        if(data.length >= bufferSize) { //  if input is bigger than buffer, discard rest of input
            for(int i = 0; i < bufferSize; i++) {
                prebuffer[i] += data[i];
            }
        } else { // if input is smaller than buffer, leave end of buffer empty.
            for(int i = 0; i < data.length; i++) {
                prebuffer[i] += data[i];
            }
        }

    }


    public int getBufferSize() { return bufferSize; }

    public int getSampleRate() { return track.getSampleRate(); }
}
