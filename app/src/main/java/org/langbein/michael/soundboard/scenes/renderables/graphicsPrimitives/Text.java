package org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;



public class Text {

    private int posX;
    private int posY;
    private String text;
    private int textSize;
    private Paint paint;

    public Text(int x, int y, String txt, int ts) {
        posX = x;
        posY = y;
        text = txt;
        textSize = ts;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(ts);
    }

    public void setColor(int a, int r, int g, int b) {
        paint.setARGB(a,r,g,b);
    }

    public void drawWithPosInMiddle(Canvas canvas) {
//        int startup = 0;
//        int end = 0;
//        Rect bounds = new Rect();
//        paint.getTextBounds(text, startup, end, bounds);
//        int len = bounds.right - bounds.left;
//        int hig = bounds.top - bounds.top;
//        canvas.drawText(text, (int)(posX - len/2), (int)(posY - hig/2), paint);
        int x = posX - text.length() * textSize / 2;
        int y = posY + textSize / 2;
        canvas.drawText(text, x, y, paint);
    }

    public void drawWithPosInLowerLeft(Canvas canvas) {
        canvas.drawText(text, posX, posY, paint);
    }
}
