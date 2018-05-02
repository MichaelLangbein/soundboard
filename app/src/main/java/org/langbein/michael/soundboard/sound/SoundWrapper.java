package org.langbein.michael.soundboard.sound;

import android.util.Log;

/**
 * Created by michael on 17.02.18.
 */

public class SoundWrapper {

    private SoundOut so;
    private SoundIn si;
    private short[] currentBatch;

    public static final int SOUND_SOURCE_BLANK = 1;
    public static final int SOUND_SOURCE_MIC = 2;
    public static final int SOUND_TARGET_DEFAULT = 10;

    public SoundWrapper() {
        this.si = new BlankIn();
        this.so = new SoundOutThread();
    }

    public void setSoundSource (int soundSource) {
        switch(soundSource) {
            case SOUND_SOURCE_BLANK:
                if(si.getClass() != BlankIn.class){
                    Log.d("Basic", "Changing sound in to BlankIn");
                    si.close();
                    si = new BlankIn();
                    si.start();
                }
                break;
            case SOUND_SOURCE_MIC:
                if(si.getClass() != SoundInMicThread.class){
                    Log.d("Basic", "Changing sound in to SoundInMicThread");
                    si.close();
                    si = new SoundInMicThread();
                    si.start();
                }
                break;
        }
    }


    public void setSoundTarget(int soundTarget) {
        switch(soundTarget) {
            case SOUND_TARGET_DEFAULT:
                if(so.getClass() != SoundOutThread.class) {
                    Log.d("Basic", "Changing sound out to SoundOutThread");
                    so.close();
                    so = new SoundOutThread();
                    so.start();
                }
                break;
        }
    }

    public void fetchNewBatch(long delta) {
        currentBatch = si.takeFromBuffer(delta);
    }

    public short[] getCurrentBatch() {
        return currentBatch;
    }

    public void addToCurrentBatch(short[] data) throws Exception {

        if(currentBatch == null) {
            throw new Exception("There is currently no batch of sounddata. You must first call fetchNewBatch().");
        }

        if(data.length > currentBatch.length) { // discard overhang
            for(int i = 0; i < currentBatch.length; i++) {
                currentBatch[i] += data[i];
            }
        } else { // leave last values empty
            for (int i = 0; i < data.length; i++) {
                currentBatch[i] += data[i];
            }
        }
    }

    public void flushCurrentBatch() throws Exception {

        if(currentBatch == null) {
            throw new Exception("There is currently no batch of sounddata. You must first call fetchNewBatch().");
        }

        so.addToBuffer(currentBatch);
        currentBatch = null;
    }


    public int getBufferSize() {

        if(currentBatch == null) {
            return 0;
        }

        return currentBatch.length;
    }


    public int getSampleRate() {

        if(currentBatch == null) {
            return 0;
        }

        return so.getSampleRate();
    }

    public void startThreads() {
        si.start();
        so.start();
    }

    public void stopThreads() {
        si.close();
        so.close();
    }

}
