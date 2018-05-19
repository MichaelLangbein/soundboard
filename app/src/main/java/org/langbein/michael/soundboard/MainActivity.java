package org.langbein.michael.soundboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.langbein.michael.soundboard.scenes.renderables.Board;
import org.langbein.michael.soundboard.scenes.renderables.Key;
import org.langbein.michael.soundboard.sound.SoundWrapper;
import org.langbein.michael.soundboard.utils.MusicUtils;
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
    private final int MY_PERMISSION_RECORD_AUDIO = 0;
    private final int MY_PERMISSION_CHANGE_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askPermissions();

        sw = new SoundWrapper(this);
        boardView = new BoardView(this, sw);
        setContentView(boardView);
        boardView.startRendering();

    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSION_RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, MY_PERMISSION_CHANGE_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    Log.d("Permissions", "Permission RECORD_AUDIO was given. Nice!");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Permissions", "Permission RECORD_AUDIO was NOT given. D'oh!");
                }
                return;
            }
            case MY_PERMISSION_CHANGE_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    Log.d("Permissions", "Permission CHANGE_AUDIO was given. Nice!");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Permissions", "Permission CHANGE_AUDIO was NOT given. D'oh!");
                }
                return;
            }
        }
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
            case R.id.timbrePiano:
                boardView.settings(Board.SETTING_TIMBRE, MusicUtils.TIMBRE_PIANO);
                break;
            case R.id.timbreSineWave:
                boardView.settings(Board.SETTING_TIMBRE, MusicUtils.TIMBRE_SINEWAVE);
                break;
            case R.id.sinkToStream:
                sw.setSoundTarget(SoundWrapper.SOUND_TARGET_STREAM);
                break;
            case R.id.sinkToMidi:
                sw.setSoundTarget(SoundWrapper.SOUND_TARGET_MIDI);
                break;
        }
        return true;
    }
}
