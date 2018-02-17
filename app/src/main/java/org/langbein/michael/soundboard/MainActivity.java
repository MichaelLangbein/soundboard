package org.langbein.michael.soundboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.langbein.michael.soundboard.sound.SoundWrapper;
import org.langbein.michael.soundboard.views.BoardView;


/**
 * Mostly modeled after this page: https://developer.android.com/guide/topics/graphics/2d-graphics.html
 * And specifically after the section "Drawing on a SurfaceView"
 *
 * The fact that the boardView has its own render-thread should not mean that we cannot use any
 * conventional views parallel to it. In fact, all fragments, menu's etc. should work just as normal.
 */
public class MainActivity extends AppCompatActivity {

    private SoundWrapper sw;
    private BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sw = new SoundWrapper();
        boardView = new BoardView(this, sw);
        setContentView(boardView);
        boardView.startRendering();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sourceToBlank:
                sw.setSoundSource(SoundWrapper.SOUND_SOURCE_BLANK);
                break;
            case R.id.sourceToMic:
                sw.setSoundSource(SoundWrapper.SOUND_SOURCE_MIC);
                break;
        }
        return true;
    }
}
