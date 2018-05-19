package org.langbein.michael.soundboard.sound;


import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioRecord;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SoundInCinchThread extends Thread implements SoundIn {


    private int frequency;
    private Context context;
    private SoundApiHelper sah;
    private boolean recording;
    private boolean alive;
    private AudioRecord audioRecord;
    private short[] audiodata;
    private BlockingQueue<Short> inBuffer;

    public SoundInCinchThread(Context context) throws Exception {
        Log.d("SoundThread", "Now initiating cinch thread");
        this.context = context;

        frequency =  sah.getNativeOutputSampleRate();
        audioRecord = sah.findWorkingAudioRecord();
        AudioDeviceInfo[] devices = sah.getInputDeviceInfos();
        int bufferSizeInBytes = sah.getBufferSizeInBytes(audioRecord);
        audiodata = new short[bufferSizeInBytes/4];
        inBuffer = new LinkedBlockingQueue<Short>();

        recording = true;
        alive = true;


    }


    /**
     * This method is extended from the Thread class. Don't call it directly!
     * Instead call soundInStart().
     */
    @Override
    public void run() {
        /**
         * put <------------ blocking
         * add <------------ exception
         * offer <---------- special value
         * offer(long) <---- timeout
         */
        Log.d("SoundThread", "Now starting to run cinch thread");
        Thread.currentThread().setName("SoundInCinchThread");
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        audioRecord.startRecording();
        while (alive) {
            if(recording){
                int readLength = audioRecord.read(audiodata, 0, audiodata.length);
                for(int i = 0; i < readLength; i++){
                    inBuffer.add(audiodata[i]);
                }
            }
        }
        audioRecord.stop();
        audioRecord.release();
    }


    public short[] takeFromBuffer(long delta) {
        int n = inBuffer.size();
        return takeFromBuffer(n);
    }

    public short[] takeFromBuffer(int n) {
        /**
         * take <-------- blocking
         * remove <------ exception
         * poll <-------- special value
         * poll(long) <-- timeout
         */

        int l = inBuffer.size();
        short[] data;
        if(l < n){ // If we have been asked for more data than there is, give all we have
            data = new short[l];
            for(int i = 0; i < l; i++) {
                data[i] = inBuffer.remove();
            }
        } else { // Otherwise, give just enough
            data = new short[n];
            for(int i = 0; i < n; i++) {
                data[i] = inBuffer.remove();
            }
        }

        return data;
    }

    @Override
    public void soundInStart() {
        alive = true;
        recording = true;
        if(!isAlive()){
            start(); // starts the actual thread
        }
    }

    public void soundInStop() {
        recording = false;
        alive = false;
    }


    public int getSampleRate() {
        return frequency;
    }
}
