package org.langbein.michael.soundboard.scenes.utils;

import org.langbein.michael.soundboard.workers.SoundOutThread;


public class SoundOutWrapper {

    private double sampleRate;
    private int bufferSize;
    private final SoundOutThread so;
    private short[] preBuffer;

    public SoundOutWrapper(SoundOutThread soundOutThread, int bffSze) {
        so = soundOutThread;
        sampleRate = so.getSampleRate();
        bufferSize = bffSze;
        preBuffer = new short[bufferSize];
    }

    public void addToPrebuffer(short[] data){
        if(data.length > preBuffer.length) { // discard overhang
            for(int i = 0; i < preBuffer.length; i++) {
                preBuffer[i] += data[i];
            }
        } else { // leave last values empty
            for (int i = 0; i < data.length; i++) {
                preBuffer[i] = data[i];
            }
        }

    }

    public void flush() {
        so.addToBuffer(preBuffer);
        for(int i = 0; i < preBuffer.length; i++) {
            preBuffer[i] = 0;
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public double getSampleRate() {
        return sampleRate;
    }
}
