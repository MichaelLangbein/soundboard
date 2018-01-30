package org.langbein.michael.soundboard.scenes.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;


public class SoundOut {

    private int sampleRate;
    private int minSize;
    private AudioTrack track;
    private short[] buffer;

    public SoundOut (int sampleRate) {
        this.sampleRate = sampleRate;

        minSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize,
                AudioTrack.MODE_STREAM);

        buffer = new short[100];
    }

    public void addToBuffer(short[] data) {
        for(int i = 0; i < data.length; i++) {
            buffer[i] += data[i];
        }
    }

    public void playAndEmptyBuffer(long delta) {
        track.write(buffer, 0, buffer.length);
    }
}
