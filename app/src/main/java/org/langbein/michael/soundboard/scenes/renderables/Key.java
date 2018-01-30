package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives.Hexagon;
import org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives.Text;
import org.langbein.michael.soundboard.scenes.utils.MusicUtils;
import org.langbein.michael.soundboard.scenes.utils.SoundOut;


public class Key implements Renderable, Touchable {

    // Graphics
    private Hexagon hex;
    private Text text;
    private int a;
    private int r;
    private int g;
    private int b;

    // Sound
    private float frq;
    private SoundOut soundOut;
    private boolean playing;

    public Key(int posX, int posY, int len, float freq, int indx, SoundOut so) {

        // Graphics
        hex = new Hexagon(posX, posY, len);
        text = new Text(posX, posY, String.valueOf(indx), 20);
        a = 125;
        r =  (int)(255 * Math.random());
        g =  (int)(255 * Math.random());
        b =  (int)(255 * Math.random());

        // Sound
        soundOut = so;
        frq = freq;
        playing = false;

    }

    @Override
    public void update(long delta) {
        if(playing) {
            Log.d("Basic", "key " + frq + " now sending a sound to soundout");
            short[] data = MusicUtils.makeWave( soundOut.getBufferSize(), frq, 5000, soundOut.getSampleRate());
            soundOut.addToBuffer(data);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        hex.draw(canvas);
        text.drawWithPosInMiddle(canvas);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if(hex.isInsideBounds(event)) {
            Log.d("Basic", "key " + frq + " is touched");
            switch(event.getFlags()){
                case MotionEvent.ACTION_DOWN:
                    lightsOn();
                    startPlayingNote();
                    break;
                case MotionEvent.ACTION_MOVE:
                    lightsOn();
                    continuePlayingNote();
                    break;
                case MotionEvent.ACTION_UP:
                    lightsOut();
                    stopPlayingNote();
                    break;
            }
        } else {
            lightsOut();
            stopPlayingNote();
        }
    }

    public void setFillColor(int a, int r, int  g, int b){
        hex.setFillColor(a, r, g, b);
    }

    private void lightsOn(){
        Log.d("Basic", "ligths on on key " + frq);
        setFillColor(255, r, g, b);
    }

    private void lightsOut(){
        Log.d("Basic", "ligths out on key " + frq);
        setFillColor(123, r, g, b);
    }

    private void startPlayingNote() {
        Log.d("Basic", "now setting play active on key " + frq);
        playing = true;
    }

    private void continuePlayingNote() {
        Log.d("Basic", "now setting play active on key " + frq);
        playing = true;
    }

    private void stopPlayingNote() {
        Log.d("Basic", "now setting play inactive on key " + frq);
        playing = false;
    }
}
