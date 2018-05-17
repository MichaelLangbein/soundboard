package org.langbein.michael.soundboard.sound;

/**
 * Created by michael on 17.02.18.
 */

public interface SoundOut {

    public void soundOutStart();

    public void soundOutClose();

    public void addToBuffer(short[] data);

    public int getSampleRate();

}
