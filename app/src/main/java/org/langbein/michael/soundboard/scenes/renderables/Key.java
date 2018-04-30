package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives.Hexagon;
import org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives.Text;
import org.langbein.michael.soundboard.utils.MusicUtils;
import org.langbein.michael.soundboard.sound.SoundWrapper;


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
    private SoundWrapper sound;
    private boolean playing;
    private double offset;

    // State

    public Key(int posX, int posY, int len, float freq, int indx, SoundWrapper sw) {

        // Graphics
        hex = new Hexagon(posX, posY, len);
        text = new Text(posX, posY, String.valueOf(indx), 20);
        a = 50;
        r =  (int)(255 * Math.random());
        g =  (int)(255 * Math.random());
        b =  (int)(255 * Math.random());

        // Sound
        sound = sw;
        frq = freq;
        playing = false;
        offset = 0;

    }

    @Override
    public void update(long delta) {
        if(playing) {
            Log.d("Basic", "Note " + frq + " now adding data to prebuffer.");

            int bufferSize, sampleRate;
            short[] data;
            try {

                bufferSize = sound.getBufferSize();
                sampleRate = sound.getSampleRate();

                data = MusicUtils.makeWave( bufferSize, frq, 1000, sampleRate, offset );
                offset = MusicUtils.calcOffset( bufferSize, frq, sampleRate, offset );

                sound.addToCurrentBatch(data);


            } catch (Exception e) {
                e.printStackTrace();
            }
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
        this.r = r;
        this.g = g;
        this.b = b;
        hex.setFillColor(a, r, g, b);
    }

    public void lightsOn(){
        setFillColor(255, r, g, b);
    }

    public void lightsOn(double degree){
        setFillColor((int)(degree * 255), r, g, b);
    }

    private void lightsOut(){
        setFillColor(25, r, g, b);
    }

    private void startPlayingNote() {
        playing = true;
    }

    private void continuePlayingNote() {
        playing = true;
    }

    private void stopPlayingNote() {
        playing = false;
        offset = 0;
    }

    public double getFrequency() {
        return frq;
    }
}
