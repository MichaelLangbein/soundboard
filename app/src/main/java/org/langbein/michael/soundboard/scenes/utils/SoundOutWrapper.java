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
        bufferSize = bffSze; //844 + 600; // TODO: actually, board must calculate this, based on gamethread frequency. I already learned that 1444 is a good buffer size.
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
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public double getSampleRate() {
        return sampleRate;
    }
}
