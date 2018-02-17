package org.langbein.michael.soundboard.sound;

/**
 * Created by michael on 17.02.18.
 * @TODO: stil needs to be implemented. See this:
 * https://stackoverflow.com/questions/12314635/reading-wav-wave-file-into-short-array
 */

public class SoundFileIn implements SoundIn {
    @Override
    public void start() {

    }

    @Override
    public void close() {

    }

    @Override
    public short[] takeFromBuffer(long delta) {
        return new short[0];
    }

    @Override
    public short[] takeFromBuffer(int n) {
        return new short[0];
    }

    @Override
    public int getSampleRate() {
        return 0;
    }
}
