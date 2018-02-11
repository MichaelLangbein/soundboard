package org.langbein.michael.soundboard.workers;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SoundOutThread extends Thread {

    private final int sampleRate;
    private final int minSize;
    private AudioTrack track;
    private BlockingQueue<Short> prebuffer;
    private boolean running;

    public SoundOutThread() {

        running = true;

        prebuffer = new LinkedBlockingQueue<Short>();

        sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);

        minSize = AudioTrack.getMinBufferSize(sampleRate,
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
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        track.play();
        while(running) {
            playAndEmptyBuffer();
        }
    }

    private void playAndEmptyBuffer() {
        int len = prebuffer.size();
        if(len > 0){
            short[] data = new short[len];
            for(int i = 0; i < len; i++){
                data[i] = prebuffer.poll();
            }
            Log.d("Basic", "about to write out " + len + " datapoints");
            track.write(data, 0, data.length);
        } else {
            try {
                Log.d("Basic", "SoundOutThread sleeping 10 ms");
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void addToBuffer(short[] data) {
        Log.d("Basic", "About to add " + data.length + " elements to prebuffer");
        for(int i = 0; i < data.length; i++) {
            prebuffer.add(data[i]);
        }
    }



    public int getSampleRate() { return track.getSampleRate(); }

    public void close() {
        running = false;
    }

}
