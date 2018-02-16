package org.langbein.michael.soundboard.workers;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SoundInThread extends Thread {

    private AudioRecord audioRecord;
    private short[] audiodata;
    private BlockingQueue<Short> inBuffer;

    public SoundInThread() {

        int frequency = 44100;

        int bufferSizeInBytes = AudioRecord.getMinBufferSize(
                frequency,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                frequency,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSizeInBytes);

        audiodata = new short[bufferSizeInBytes/4];
        inBuffer = new LinkedBlockingQueue<Short>();
    }


    @Override
    public void run() {
        audioRecord.startRecording();
        while (true) {
            int readLength = audioRecord.read(audiodata, 0, audiodata.length);
            for(int i = 0; i < readLength; i++){
                inBuffer.add(audiodata[i]);
            }
        }
        //audioRecord.stop();
    }

    public short[] getData() {
        int l = inBuffer.size();
        short[] data = new short[l];
        for(int i = 0; i < l; i++) {
            data[i] = inBuffer.take();
        }
        return data;
    }
}
