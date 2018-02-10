package org.langbein.michael.soundboard.scenes.utils;

import org.langbein.michael.soundboard.workers.SoundOutThread;

/**
 * Created by michael on 10.02.18.
 */

public class SoundOutWrapper {

    private double sampleRate;
    private int bufferSize;
    private final SoundOutThread so;
    private short[] prebuffer;

    public SoundOutWrapper(SoundOutThread soundOutThread) {
        so = soundOutThread;
        sampleRate = so.getSampleRate();
        bufferSize = 844; // TODO: actually, board must calculate this, based on gamethread frequency.
        prebuffer = new short[bufferSize];
    }

    public void addToPrebuffer(short[] data){
        if(data.length > prebuffer.length) { // discard overhang
            for(int i = 0; i < prebuffer.length; i++) {
                prebuffer[i] += data[i];
            }
        } else { // leave last values empty
            for (int i = 0; i < data.length; i++) {
                prebuffer[i] = data[i];
            }
        }

    }

    public void flush() {
        so.addToBuffer(prebuffer);
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public double getSampleRate() {
        return sampleRate;
    }
}
