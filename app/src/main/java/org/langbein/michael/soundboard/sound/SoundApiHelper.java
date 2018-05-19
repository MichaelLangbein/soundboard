package org.langbein.michael.soundboard.sound;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;


/**
 * This class is there to ensure that adioIn and Out use compatible settings.
 * Also, to make the syntax a little easier.
 */
public class SoundApiHelper {

    private Context context;
    private final AudioManager am;
    private int audioSourceIn;
    private int audioChannelIn;
    private int audioChannelOut;
    private int audioEncodingIn;
    private int audioEncodingOut;
    private int frequency;
    private AudioRecord audioRecord;
    private AudioTrack audioTrack;

    public SoundApiHelper(Context context) {
        this.context = context;
        this.am = context.getSystemService(AudioManager.class);
    }

    public AudioTrack findWorkingAudioTrack() {
        int sampleRate = getNativeOutputSampleRate();

        this.audioChannelOut = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        this.audioEncodingOut = AudioFormat.ENCODING_PCM_16BIT;

        int minSize = AudioTrack.getMinBufferSize(sampleRate, audioChannelOut, audioEncodingOut);

        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                audioChannelOut,
                audioEncodingOut,
                minSize,
                AudioTrack.MODE_STREAM);

        this.audioTrack = track;
        return track;
    }

    public AudioRecord findWorkingAudioRecord() throws Exception {

        frequency = getNativeOutputSampleRate();

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
                            this.audioRecord = audioRecord;
                            this.audioSourceIn = audioSource;
                            this.audioChannelIn = audioChannel;
                            this.audioEncodingIn = audioEncoding;
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

    public int getBufferSizeInBytes(AudioRecord audioRecord) {
        int sampleRate = getNativeOutputSampleRate();
        int channelConfiguration = audioRecord.getChannelConfiguration();
        int audioFormat = audioRecord.getAudioFormat();
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfiguration, audioFormat);
        return minBufferSize;
    }

    public int getMinOutputBufferSize() {
        return AudioTrack.getMinBufferSize(frequency, audioChannelOut, audioEncodingOut);
    }

    public int getNativeOutputSampleRate() {
        return  AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
    }

    public AudioDeviceInfo[] getInputDeviceInfos () {
        AudioDeviceInfo[] inputDevices = am.getDevices(AudioManager.GET_DEVICES_INPUTS);
        for(AudioDeviceInfo adi : inputDevices){
            Log.d("Input Devices", "Eines unserer Input devices: " +  adi.toString() );
        }
        return inputDevices;
    }

    public AudioDeviceInfo[] getOutputDeviceInfos () {
        AudioDeviceInfo[] outputDevices = am.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for(AudioDeviceInfo adi : outputDevices){
            Log.d("Output Devices", "Eines unserer Output devices: " +  adi.toString() );
        }
        return outputDevices;
    }

    public void setPreferredDevice(AudioRecord audioRecord, AudioDeviceInfo deviceInfo) {
        audioRecord.setPreferredDevice(deviceInfo);
    }

}
