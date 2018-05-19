package org.langbein.michael.soundboard.sound;

import android.content.pm.PackageManager;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.content.Context;

import org.langbein.michael.soundboard.MidiTools.MidiConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MIDI_SERVICE;

/**
 * Created by michael on 17.02.18.
 */

public class MidiWrapper extends MidiManager.DeviceCallback {

    private Context context;
    private MidiManager midiManager;
    private ArrayList<MidiDeviceInfo> deviceInfos;
    private MidiDevice midiReceiverDevice;
    private MidiInputPort midiReceiverPort;


    public MidiWrapper(Context context) throws Exception {
        this.context = context;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            throw new Exception("Midi not supported!");
        }

        midiManager = (MidiManager) context.getSystemService(MIDI_SERVICE);

        MidiDeviceInfo[] infos = midiManager.getDevices();
        deviceInfos = new ArrayList<MidiDeviceInfo>(Arrays.asList(infos));

        midiManager.registerDeviceCallback(this, new Handler(Looper.getMainLooper()));
    }

    public ArrayList<MidiDeviceInfo> getDeviceInfos() {
        return deviceInfos;
    }

    public void choseMidiDevice(MidiDeviceInfo mdi, final int newPort) {
        midiManager.openDevice(mdi, new MidiManager.OnDeviceOpenedListener() {
            @Override
            public void onDeviceOpened(MidiDevice device) {
                if (device == null) {
                    Log.e("MidiWrapper", "could not open device " + device.toString());
                } else {
                    midiReceiverDevice = device;
                    midiReceiverPort = midiReceiverDevice.openInputPort(newPort);
                }
            }
        }, new Handler(Looper.getMainLooper()));
    }


    public void noteOff(int channel, double frequency, int velocity) {
        int pitch = convertFrequencyToPitch(frequency);
        midiCommand(MidiConstants.STATUS_NOTE_OFF + channel, pitch, velocity);
    }

    public void noteOn(int channel, double frequency, int velocity) {
        int pitch = convertFrequencyToPitch(frequency);
        midiCommand(MidiConstants.STATUS_NOTE_ON + channel, pitch, velocity);
    }

    private int convertFrequencyToPitch(double frq) {
        return 45 + 12 * (int) (Math.log(frq / 440) / Math.log(2));
    }

    private void midiCommand(int status, int data1, int data2) {
        byte[] byteBuffer = new byte[3];
        byteBuffer[0] = (byte) status;
        byteBuffer[1] = (byte) data1;
        byteBuffer[2] = (byte) data2;
        long now = System.nanoTime();
        midiSend(byteBuffer, 3, now);
    }

    private void midiCommand(int status, int data1) {
        byte[] byteBuffer = new byte[3];
        byteBuffer[0] = (byte) status;
        byteBuffer[1] = (byte) data1;
        long now = System.nanoTime();
        midiSend(byteBuffer, 2, now);
    }

    private void midiSend(byte[] buffer, int count, long timestamp) {
        if(midiReceiverPort != null){
            try {
                midiReceiverPort.send(buffer, 0, count, timestamp);
            } catch (IOException e) {
                Log.e("MidiWrapper", "mKeyboardReceiverSelector.send() failed " + e);
            }
        }
    }

    public void onDeviceAdded( MidiDeviceInfo info ) {
        deviceInfos.add(info);
    }
    public void onDeviceRemoved( MidiDeviceInfo info ) {
        deviceInfos.remove(info);
    }

}
