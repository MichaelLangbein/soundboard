package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;



public class Key implements Renderable, Touchable {

    private Hexagon hex;
    private Text text;
    private float frq;

    public Key(int posX, int posY, int len, float freq, int indx) {
        frq = freq;
        hex = new Hexagon(posX, posY, len);
        text = new Text(posX, posY, String.valueOf(indx), 20);
    }

    @Override
    public void update(long delta) {

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
                    break;
                case MotionEvent.ACTION_MOVE:
                    lightsOn();
                    break;
                case MotionEvent.ACTION_UP:
                    lightsOut();
                    break;
            }
        }
    }

    public void setFillColor(int a, int r, int  g, int b){
        hex.setFillColor(a, r, g, b);
    }

    private void lightsOn(){
        setFillColor(255, (int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random()));
    }

    private void lightsOut(){
        setFillColor(123, (int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random()));
    }
}
