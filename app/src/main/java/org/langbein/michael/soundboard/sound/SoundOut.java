package org.langbein.michael.soundboard.sound;

/**
 * Created by michael on 17.02.18.
 */

public interface SoundOut {

    public void start();

    public void close();

    public void addToBuffer(short[] data);

    public int getSampleRate();

}
