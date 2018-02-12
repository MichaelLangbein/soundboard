package org.langbein.michael.soundboard.scenes.renderables.graphicsPrimitives;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import org.langbein.michael.soundboard.scenes.utils.Vec2;

/**
 * Created by michael on 28.01.18.
 */

public class Hexagon implements TwoDimObject {

    private Paint outlinePaint;
    private Paint fillPaint;
    private Path path;
    private Vec2<Integer> center;
    private int sideLength;

    public Hexagon(int xPos, int yPos, int len) {

        center = new Vec2<Integer>(xPos, yPos);
        sideLength = len;

        outlinePaint = new Paint();
        outlinePaint.setColor(Color.BLUE);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(10);

        fillPaint = new Paint();
        fillPaint.setColor(Color.GREEN);
        fillPaint.setStyle(Paint.Style.FILL);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(xPos, yPos - len); // top
        path.lineTo( (float) (xPos + len * Math.cos(Math.PI / 6.0)), (float) (yPos - len * Math.sin(Math.PI / 6.0)) ); // right upper
        path.lineTo( (float) (xPos + len * Math.cos(Math.PI / 6.0)), (float) (yPos + len * Math.sin(Math.PI / 6.0)) ); // right lower
        path.lineTo(xPos, yPos + len); // bottom
        path.lineTo( (float) (xPos - len * Math.cos(Math.PI / 6.0)), (float) (yPos + len * Math.sin(Math.PI / 6.0)) ); // left lower
        path.lineTo( (float) (xPos - len * Math.cos(Math.PI / 6.0)), (float) (yPos - len * Math.sin(Math.PI / 6.0)) ); // left upper
        path.lineTo(xPos, yPos - len); // top

    }

    public void draw(Canvas canvas) {
        canvas.drawPath(path, fillPaint);
        //canvas.drawPath(path, outlinePaint);
    }

    public boolean isInsideBounds(MotionEvent me) {
        // @TODO: das geht genauer!
        boolean inside = false;

        float x = me.getX();
        float y = me.getY();
        double dist = Math.sqrt(Math.pow((x - center.x), 2) + Math.pow((y - center.y), 2));

        if(dist < 1.3*sideLength){
            inside = true;
        }

        return inside;
    }

    public void setOutlineColor(int a, int r, int g, int b){
        outlinePaint.setARGB(a, r, g, b);
    }

    public void setFillColor(int a, int r, int g, int b){
        fillPaint.setARGB(a, r, g, b);
    }
}
