package org.langbein.michael.soundboard.scenes.renderables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by michael on 28.01.18.
 */

public class Hexagon {

    private Paint outlinePaint;
    private Paint fillPaint;
    private Path path;

    public Hexagon(int xPos, int yPos, int len) {

        outlinePaint = new Paint();
        outlinePaint.setColor(Color.BLUE);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(10);

        fillPaint = new Paint();
        fillPaint.setColor(Color.GREEN);
        fillPaint.setStyle(Paint.Style.FILL);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(xPos, xPos - len); // top
        path.lineTo( (float) (xPos + len * Math.cos(Math.PI / 6.0)), (float) (yPos - len * Math.sin(Math.PI / 6.0)) ); // right upper
        path.lineTo( (float) (xPos + len * Math.cos(Math.PI / 6.0)), (float) (yPos + len * Math.sin(Math.PI / 6.0)) ); // right lower
        path.lineTo(xPos, yPos + len); // bottom
        path.lineTo( (float) (xPos - len * Math.cos(Math.PI / 6.0)), (float) (yPos + len * Math.sin(Math.PI / 6.0)) ); // left lower
        path.lineTo( (float) (xPos - len * Math.cos(Math.PI / 6.0)), (float) (yPos - len * Math.sin(Math.PI / 6.0)) ); // left upper
        path.lineTo(xPos, xPos - len); // top

    }

    public void draw(Canvas canvas) {
        canvas.drawPath(path, fillPaint);
        //canvas.drawPath(path, outlinePaint);
    }

    public void setOutlineColor(int a, int r, int g, int b){
        outlinePaint.setARGB(a, r, g, b);
    }

    public void setFillColor(int a, int r, int g, int b){
        fillPaint.setARGB(a, r, g, b);
    }
}
