package org.langbein.michael.soundboard;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.langbein.michael.soundboard.sound.SoundInCinchThread;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SoundInCinchThreadTest {

    @Test
    public void testInitiation() throws Exception {

        Context appContext = InstrumentationRegistry.getTargetContext();
        SoundInCinchThread sct = new SoundInCinchThread(appContext);

        assertTrue(sct instanceof SoundInCinchThread);

        sct.soundInStart();
        Thread.sleep(100);
        short[] data = sct.takeFromBuffer(10);
        sct.soundInStop();

        Log.d("TestSoundInCinch", "Data is as such: " + data);
        assertTrue(data.length > 0);
    }
}
