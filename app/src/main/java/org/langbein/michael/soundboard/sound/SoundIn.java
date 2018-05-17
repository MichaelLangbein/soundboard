package org.langbein.michael.soundboard.sound;

/**
 * Created by michael on 17.02.18.
 */

public interface SoundIn {

    public void soundInStart();

    public void soundInStop();

    public short[] takeFromBuffer(long delta);

    public short[] takeFromBuffer(int n);

    public int getSampleRate();
}
