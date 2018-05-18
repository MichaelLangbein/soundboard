package org.langbein.michael.soundboard.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundApiHelper {

    public static AudioRecord findWorkingAudioRecord() throws Exception {

        //        AudioManager am = context.getSystemService(AudioManager.class);
//        AudioDeviceInfo[] inputDevices = am.getDevices(AudioManager.GET_DEVICES_INPUTS);
//        for(AudioDeviceInfo adi : inputDevices){
//            Log.d("Input Devices", "Eines unserer Input devices: " +  adi.toString() );
//        }
        //AudioDeviceInfo adi = inputDevices[0];
        //audioRecord.setPreferredDevice(adi);

        int frequency = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);

        int[] possibleAudioSources = {
                MediaRecorder.AudioSource.DEFAULT,  // das sollte es sein
                MediaRecorder.AudioSource.MIC,      // das könnte auch ...
                MediaRecorder.AudioSource.UNPROCESSED,  // klingt ganz gut ...
                MediaRecorder.AudioSource.VOICE_DOWNLINK,  // hmm, vielleicht?
                MediaRecorder.AudioSource.VOICE_CALL,   // könnte auch sein?
                MediaRecorder.AudioSource.REMOTE_SUBMIX,  // was ist das?
                MediaRecorder.AudioSource.VOICE_COMMUNICATION  // glaube ich eigentlich nicht.
        };

        int[] possibleAudioChannels = {
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.CHANNEL_IN_BACK,
                AudioFormat.CHANNEL_IN_FRONT,
                AudioFormat.CHANNEL_IN_FRONT_PROCESSED,
                AudioFormat.CHANNEL_IN_BACK_PROCESSED,
                AudioFormat.CHANNEL_IN_STEREO
        };

        int[] possibleAudioEncodings = {
                AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.ENCODING_DEFAULT,
                AudioFormat.ENCODING_PCM_8BIT,
                AudioFormat.ENCODING_AC3,
                AudioFormat.ENCODING_DOLBY_TRUEHD,
                AudioFormat.ENCODING_DTS,
                AudioFormat.ENCODING_IEC61937
        };

        for(int audioSource : possibleAudioSources) {
            for(int audioChannel : possibleAudioChannels) {
                for(int audioEncoding : possibleAudioEncodings) {

                    int bufferSizeInBytes = AudioRecord.getMinBufferSize(
                            frequency,
                            audioChannel,
                            audioEncoding);

                    Log.d("SoundApiHelper", "Trying with source "+audioSource+", channel "+audioChannel+", encoding "+audioEncoding+", bufferSize " + bufferSizeInBytes);

                    try{
                        AudioRecord audioRecord = new AudioRecord(
                                audioSource,
                                frequency,
                                audioChannel,
                                audioEncoding,
                                bufferSizeInBytes);

                        if(audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                            return audioRecord;
                        }

                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }




                }
            }
        }

        throw new Exception("Keine valide Kombination von Parametern für AudioRecord gefunden!");
    }

    public static int getBufferSizeInBytes(AudioRecord audioRecord) {
        int sampleRate = getNativeSampleRate();
        int channelConfiguration = audioRecord.getChannelConfiguration();
        int audioFormat = audioRecord.getAudioFormat();
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfiguration, audioFormat);
        return minBufferSize;
    }

    public static int getNativeSampleRate() {
        return  AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
    }
}
