package org.langbein.michael.soundboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.langbein.michael.soundboard.scenes.BoardScene;
import org.langbein.michael.soundboard.scenes.SceneLogic;
import org.langbein.michael.soundboard.utils.SoundWrapper;
import org.langbein.michael.soundboard.views.BoardView;
import org.langbein.michael.soundboard.workers.BoardRenderThread;
import org.langbein.michael.soundboard.workers.SoundInThread;
import org.langbein.michael.soundboard.workers.SoundOutThread;


/**
 * Mostly modeled after this page: https://developer.android.com/guide/topics/graphics/2d-graphics.html
 * And specifically after the section "Drawing on a SurfaceView"
 *
 * The fact that the boardView has its own render-thread should not mean that we cannot use any
 * conventional views parallel to it. In fact, all fragments, menu's etc. should work just as normal.
 */
public class MainActivity extends AppCompatActivity {

    BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SoundWrapper sw = new SoundWrapper();
        boardView = new BoardView(this, sw);
        setContentView(boardView);
        boardView.startRendering();
    }
}
