package org.langbein.michael.soundboard.sound;

import android.media.midi.MidiDevice;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.media.midi.MidiReceiver;

import org.langbein.michael.soundboard.MidiTools.MidiPortWrapper;

class MeinMidiInputPortSelector {


    private MidiInputPort mInputPort;
    private MidiDevice mOpenDevice;

    public MeinMidiInputPortSelector(MidiManager mMidiManager, MidiWrapper midiWrapper) {

    }

    public MidiReceiver getReceiver() {
    }

    public void close() {
    }
}
