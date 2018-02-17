package org.langbein.michael.soundboard.utils;

import android.util.Log;

import org.langbein.michael.soundboard.workers.SoundInThread;
import org.langbein.michael.soundboard.workers.SoundOutThread;

/**
 * Created by michael on 17.02.18.
 */

public class SoundWrapper {

    private SoundOutThread sot;
    private SoundInThread sit;
    private short[] currentBatch;

    public SoundWrapper(SoundInThread sit, SoundOutThread sot) {
        this.sit = sit;
        this.sot = sot;
    }

    public void fetchNewBatch() {
        currentBatch = sit.takeFromBuffer();

        // This block is only for debugging.
//            long sum = 0;
//            for(int i = 0; i < currentBatch.length; i++) {
//                sum += Math.abs(currentBatch[i]);
//            }
//            Log.d("Basic", "Obtained " + currentBatch.length + " datapoints. Sum: " + sum );


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
                currentBatch[i] = data[i];
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
