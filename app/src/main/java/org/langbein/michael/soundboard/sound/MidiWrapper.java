package org.langbein.michael.soundboard.sound;

import android.content.pm.PackageManager;
import android.media.midi.MidiManager;
import android.media.midi.MidiReceiver;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import org.langbein.michael.soundboard.MidiTools.MidiConstants;
import org.langbein.michael.soundboard.MidiTools.MidiInputPortSelector;
import org.langbein.michael.soundboard.MidiTools.MusicKeyboardView;
import org.langbein.michael.soundboard.R;
import org.langbein.michael.soundboard.utils.MusicUtils;

import java.io.IOException;

import static android.content.Context.MIDI_SERVICE;

/**
 * Created by michael on 17.02.18.
 */

public class MidiWrapper {

    private final Context context;
    private MidiManager mMidiManager;
    private MeinMidiInputPortSelector midiReceiverSelector;
    private byte[] mByteBuffer = new byte[3];


    public MidiWrapper(Context context) {
        this.context = context;
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            setupMidi();
        } else {
            Toast.makeText(context, "MIDI not supported!", Toast.LENGTH_LONG).show();
        }
    }

    private void setupMidi() {
        mMidiManager = (MidiManager) context.getSystemService(MIDI_SERVICE);
        if (mMidiManager == null) {
            Toast.makeText(context, "MidiManager is null!", Toast.LENGTH_LONG).show();
            return;
        }
        midiReceiverSelector = new MeinMidiInputPortSelector(mMidiManager,this);
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
        mByteBuffer[0] = (byte) status;
        mByteBuffer[1] = (byte) data1;
        mByteBuffer[2] = (byte) data2;
        long now = System.nanoTime();
        midiSend(mByteBuffer, 3, now);
    }

    private void midiCommand(int status, int data1) {
        mByteBuffer[0] = (byte) status;
        mByteBuffer[1] = (byte) data1;
        long now = System.nanoTime();
        midiSend(mByteBuffer, 2, now);
    }

    private void midiSend(byte[] buffer, int count, long timestamp) {
        if (midiReceiverSelector != null) {
            try {
                // send event immediately
                MidiReceiver receiver = midiReceiverSelector.getReceiver();
                if (receiver != null) {
                    receiver.send(buffer, 0, count, timestamp);
                }
            } catch (IOException e) {
                Log.e("MidiWrapper", "mKeyboardReceiverSelector.send() failed " + e);
            }
        }
    }

    private void closeSynthResources() {
        if (midiReceiverSelector != null) {
            midiReceiverSelector.close();
        }
    }

    public void onDestroy() {
        closeSynthResources();
    }
}
