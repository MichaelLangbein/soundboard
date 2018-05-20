package org.langbein.michael.soundboard;

import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.langbein.michael.soundboard.sound.MidiWrapper;
import org.langbein.michael.soundboard.sound.OscWrapper;
import org.langbein.michael.soundboard.views.BoardView;

import java.util.ArrayList;


/**
 * Mostly modeled after this page: https://developer.android.com/guide/topics/graphics/2d-graphics.html
 * And specifically after the section "Drawing on a SurfaceView"
 *
 * The fact that the boardView has its own render-thread should not mean that we cannot use any
 * conventional views parallel to it. In fact, all fragments, menu's etc. should work just as normal.
 */
public class MainActivity extends AppCompatActivity {

    private OscWrapper mw;
    private BoardView boardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mw = new OscWrapper(this);
        boardView = new BoardView(this, mw);
        setContentView(boardView);
        boardView.startRendering();

    }


    @Override
    public void onDestroy() {
        boardView.stopRendering();
        super.onDestroy();
    }
}
