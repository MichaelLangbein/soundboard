package org.langbein.michael.soundboard.utils;

import android.util.Log;

import org.langbein.michael.soundboard.workers.SoundInThread;
import org.langbein.michael.soundboard.workers.SoundOutThread;

/**
 * Created by michael on 17.02.18.
 *
 * @TODO: in the future, the user will be able to chose what input source to use.
 * The sound wrapper will then internally exchange the SoundInThread for a MusicFile thread, for example.
 * However, to the outside world this will not be visible. Nevermind what the current
 * input is, data will always be obtained via soundWrapper.fetchNewBatch().
 */

public class SoundWrapper {

    private SoundOutThread sot;
    private SoundInThread sit;
    private short[] currentBatch;

    public SoundWrapper() {
        this.sit = new SoundInThread();
        this.sot = new SoundOutThread();
    }

    public void fetchNewBatch(long delta) {
        currentBatch = sit.takeFromBuffer();

        for(int i = 0; i < currentBatch.length; i++) {
            currentBatch[i] = (short) (currentBatch[i] * 100);
        }
        Log.d("Basic", "Obtained " + currentBatch.length + " datapoints. " );


    }

    public void addToCurrentBatch(short[] data) throws Exception {

        Log.d("Basic", "Now adding "+data.length+" datapoints to soundbuffer.");

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

        sot.addToBuffer(currentBatch);
        currentBatch = null;
    }


    public int getBufferSize() throws Exception {

        if(currentBatch == null) {
            throw new Exception("There is currently no batch of sounddata. You must first call fetchNewBatch().");
        }

        return currentBatch.length;
    }


    public int getSampleRate() throws Exception {

        if(currentBatch == null) {
            throw new Exception("There is currently no batch of sounddata. You must first call fetchNewBatch().");
        }

        return sot.getSampleRate();
    }

    public void startThreads() {
        sit.start();
        sot.start();
    }

    public void stopThreads() {
        sit.close();
        sot.close();
    }

}
