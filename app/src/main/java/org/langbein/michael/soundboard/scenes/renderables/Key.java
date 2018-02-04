package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives.Hexagon;
import org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives.Text;
import org.langbein.michael.soundboard.scenes.utils.MusicUtils;
import org.langbein.michael.soundboard.workers.SoundOutThread;


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
    private SoundOutThread soundOut;
    private boolean playing;
    private double offset;

    // State

    public Key(int posX, int posY, int len, float freq, int indx, SoundOutThread so) {

        // Graphics
        hex = new Hexagon(posX, posY, len);
        text = new Text(posX, posY, String.valueOf(indx), 20);
        a = 123;
        r =  (int)(255 * Math.random());
        g =  (int)(255 * Math.random());
        b =  (int)(255 * Math.random());

        // Sound
        soundOut = so;
        frq = freq;
        playing = false;
        offset = 0;

    }

    @Override
    public void update(long delta) {
        if(playing) {
            short[] data = MusicUtils.makeWave( soundOut.getBufferSize(), frq, 5000, soundOut.getSampleRate(), offset );
            offset = MusicUtils.calcOffset(  soundOut.getBufferSize(), frq, soundOut.getSampleRate(), offset );
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
            switch(event.getFlags()){
                case MotionEvent.ACTION_DOWN:
                    lightsOn();
                    startPlayingNote();
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
        setFillColor(255, r, g, b);
    }

    private void lightsOut(){
        setFillColor(123, r, g, b);
    }

    private void startPlayingNote() {
        playing = true;
    }

    private void continuePlayingNote() {
        playing = true;
    }

    private void stopPlayingNote() {
        playing = false;
    }
}
