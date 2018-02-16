package org.langbein.michael.soundboard.workers;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.concurrent.BlockingQueue;

public class SoundInThread extends Thread {

    private AudioTrack audioTrack;
    private short[] audiodata;
    private BlockingQueue<Short> postInBuffer;

    public SoundInThread() {

        int frequency = 44100;

        int bufferSize = AudioTrack.getMinBufferSize(
                frequency,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audiodata = new short[bufferSize/4];

        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                frequency,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);
    }
}
