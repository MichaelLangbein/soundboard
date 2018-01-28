package org.langbein.michael.soundboard.workers;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by michael on 28.01.18.
 */

public class SoundOutThread extends Thread {


    private int sampleRate = 44100;
    private int minSize;
    private AudioTrack track;
    private boolean running;


    public SoundOutThread () {
        minSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize,
                AudioTrack.MODE_STREAM);

        running = true;
    }

    @Override
    public void run() {
        track.play();
        while (running) {
            audioTrack.write(buffer, 0, buffer.length);
        }
    }

    public void addSoundToPlay(short[] sound) {

    }
}
