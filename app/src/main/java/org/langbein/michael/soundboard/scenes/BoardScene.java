package org.langbein.michael.soundboard.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;



public class BoardScene implements SceneLogic {

    private Rect lilRect;
    private final int SQUARE_SIDE_LENGTH = 200;
    private Paint painter;

    public BoardScene() {
        lilRect = new Rect(30, 30, SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH);
        painter = new Paint();
        painter.setColor(Color.MAGENTA);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        lilRect.left   = (int) (event.getX() - (SQUARE_SIDE_LENGTH/2));
        lilRect.right  = (int) (event.getX() + (SQUARE_SIDE_LENGTH/2));
        lilRect.top    = (int) (event.getY() - (SQUARE_SIDE_LENGTH/2));
        lilRect.bottom = (int) (event.getY() + (SQUARE_SIDE_LENGTH/2));
        return true;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRGB(20, 20, 20);
        canvas.drawRect(lilRect, painter);
    }
}
