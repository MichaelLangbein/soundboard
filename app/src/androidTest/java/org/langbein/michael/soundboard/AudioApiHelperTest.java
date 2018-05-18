package org.langbein.michael.soundboard;

import android.media.AudioRecord;

import org.junit.Test;
import org.langbein.michael.soundboard.sound.SoundApiHelper;

import static junit.framework.TestCase.assertTrue;

public class AudioApiHelperTest {

    @Test
    public void testGetAudioRecord() throws Exception {
        AudioRecord ar = SoundApiHelper.findWorkingAudioRecord();
        assertTrue(ar.getState() == AudioRecord.STATE_INITIALIZED);
    }
}
