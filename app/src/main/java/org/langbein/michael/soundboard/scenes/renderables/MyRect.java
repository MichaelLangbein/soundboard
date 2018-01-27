package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by michael on 27.01.18.
 */

public class MyRect implements Renderable, Touchable {

    private Paint painter;
    private Rect rect;
    private int posX;
    private int posY;
    private int width;
    private int height;

    public MyRect(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        rect = new Rect(posX, posY, width, height);
        painter = new Paint();
        painter.setColor(Color.MAGENTA);
    }

    @Override
    public void update(long delta) {
        rect.top -= delta * 1;
        rect.bottom -= delta * 1;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, painter);
    }

    @Override
    public void onTouch(MotionEvent event) {
        rect.left   = (int) (event.getX() - (width/2));
        rect.right  = (int) (event.getX() + (width/2));
        rect.top    = (int) (event.getY() - (height/2));
        rect.bottom = (int) (event.getY() + (height/2));
    }
}
