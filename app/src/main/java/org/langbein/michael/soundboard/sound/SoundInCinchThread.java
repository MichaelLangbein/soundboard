package org.langbein.michael.soundboard.sound;


import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SoundInCinchThread extends Thread implements SoundIn {


    private final int frequency;
    private final Context context;
    private boolean recording;
    private boolean alive;
    private AudioRecord audioRecord;
    private short[] audiodata;
    private BlockingQueue<Short> inBuffer;

    public SoundInCinchThread(Context context) {
        Log.d("SoundThread", "Now initiating cinch thread");
        this.context = context;

        frequency = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);


        int bufferSizeInBytes = AudioRecord.getMinBufferSize(
                frequency,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                frequency,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSizeInBytes);

        AudioManager am = context.getSystemService(AudioManager.class);
        AudioDeviceInfo[] inputDevices = am.getDevices(AudioManager.GET_DEVICES_INPUTS);
        for(AudioDeviceInfo adi : inputDevices){
            Log.d("Input Devices", "Eines unserer Input devices: " +  adi.toString() );
        }
        //AudioDeviceInfo adi = inputDevices[0];
        //audioRecord.setPreferredDevice(adi);

        audiodata = new short[bufferSizeInBytes/4];
        inBuffer = new LinkedBlockingQueue<Short>();

        recording = true;
        alive = true;


    }


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
    public void startup() {
        alive = true;
        startRecording();
    }

    public void close() {
        stopRecording();
        alive = false;
    }

    public void stopRecording() {
        recording = false;
    }

    public void startRecording() {
        recording = true;
    }

    public int getSampleRate() {
        return frequency;
    }
}
