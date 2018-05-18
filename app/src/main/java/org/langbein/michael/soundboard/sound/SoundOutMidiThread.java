package org.langbein.michael.soundboard.sound;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;

import java.util.ArrayList;

public class SoundOutMidiThread extends Thread implements SoundOut {

    MidiManager midiManager;
    ArrayList<MidiDeviceInfo> devices;
    private boolean alive;

    public SoundOutMidiThread(Context context) throws Exception {

        if (! context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            throw new Exception("You need MIDI enabled to be able to use the MidiOutThread.");
        }

        midiManager = (MidiManager)context.getSystemService(Context.MIDI_SERVICE);
        MidiDeviceInfo[] infos = midiManager.getDevices();
        devices = new ArrayList<MidiDeviceInfo>();
        for(MidiDeviceInfo device : infos){
            devices.add(device);
        }

//        midiManager.registerDeviceCallback(new MidiManager.DeviceCallback() {
//            public void onDeviceAdded( MidiDeviceInfo info ) {
//                devices.add(info);
//            }
//            public void onDeviceRemoved( MidiDeviceInfo info ) {
//                devices.remove(info);
//            }
//        });

        alive = true;
    }

    @Override
    public void run(){
        while(alive){

        }
    }

    @Override
    public void soundOutStart() {
        alive = true;
        if(!isAlive()){
            start();
        }
    }

    @Override
    public void soundOutClose() {
        alive = false;
    }

    @Override
    public void addToBuffer(short[] data) {

    }

    @Override
    public int getSampleRate() {
        return 0;
    }
}
