package org.langbein.michael.soundboard.sound;

import android.util.Log;
import android.content.Context;

/**
 * Created by michael on 17.02.18.
 */

public class SoundWrapper {

    private final Context context;
    private SoundOut so;
    private SoundIn si;
    private short[] currentBatch;

    public static final int SOUND_SOURCE_BLANK = 1;
    public static final int SOUND_SOURCE_MIC = 2;
    public static final int SOUND_TARGET_STREAM = 3;
    public static final int SOUND_TARGET_MIDI = 4;

    public SoundWrapper(Context context) {
        this.context = context;
        this.si = new BlankInThread(context);
        this.so = new SoundOutThread(context);
    }

    public void setSoundSource (int soundSource) {
        switch(soundSource) {
            case SOUND_SOURCE_BLANK:
                if(si.getClass() != BlankInThread.class){
                    Log.d("Basic", "Changing sound in to BlankInThread");
                    si.soundInStop();
                    si = new BlankInThread(context);
                    si.soundInStart();
                }
                break;
            case SOUND_SOURCE_MIC:
                if(si.getClass() != SoundInCinchThread.class){
                    Log.d("Basic", "Changing sound in to SoundInCinchThread");
                    si.soundInStop();
                    try {
                        si = new SoundInCinchThread(context);
                    } catch (Exception e) {
                        Log.e("SoundWrapper", "Konnte SoundInCinchThread nicht initiieren.");
                        e.printStackTrace();
                    }
                    si.soundInStart();
                }
                break;
        }
    }


    public void setSoundTarget(int soundTarget) {
        switch(soundTarget) {
            case SOUND_TARGET_STREAM:
                if(so.getClass() != SoundOutThread.class) {
                    Log.d("Basic", "Changing sound out to SoundOutThread");
                    so.soundOutClose();
                    so = new SoundOutThread(context);
                    so.soundOutStart();
                }
                break;
            case SOUND_TARGET_MIDI:
                if(so.getClass() != SoundOutMidiThread.class) {
                    if(si.getClass() == SoundInCinchThread.class){
                        Log.d("Basic", "Cannot use Cinch Thread at same time as Midi-Out. Setting to blank");
                        setSoundSource(SOUND_SOURCE_BLANK);
                    }
                    Log.d("Basic", "Changing sound out to SoundOutMidiThread");
                    so.soundOutClose();
                    so = new SoundOutMidiThread(context);
                    so.soundOutStart();
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
        si.soundInStart();
        so.soundOutStart();
    }

    public void stopThreads() {
        si.soundInStop();
        so.soundOutClose();
    }

}
