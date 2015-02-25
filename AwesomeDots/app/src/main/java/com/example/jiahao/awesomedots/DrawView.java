package com.example.jiahao.awesomedots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by JiaHao on 11/2/15.
 */
public class DrawView extends View {

    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(3);
        canvas.drawRect(30,30,80,80, paint);
        canvas.drawRoundRect();

        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);

        canvas.drawRect(33, 60, 77, 77, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(33, 33, 77, 60, paint);

    }
}
