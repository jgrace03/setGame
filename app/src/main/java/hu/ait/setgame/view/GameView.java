package hu.ait.setgame.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import hu.ait.setgame.R;

import static android.R.color.holo_purple;

public class GameView extends View {

    private Paint paintLine;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintLine = new Paint();
        paintLine.setColor(getResources().getColor(holo_purple));
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        /*canvas.drawBitmap(
                bitmapBg,
                0, 0, null);*/

        //canvas.drawText("6", 0, getHeight()/3, paintText);

        drawGameGrid(canvas);

        //drawPlayers(canvas);
    }


    private void drawGameGrid(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // two horizontal lines
        canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 3, getWidth(),
                2 * getHeight() / 3, paintLine);


        // two vertical lines
        canvas.drawLine(getWidth() / 4, 0, getWidth() / 4, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 4, 0, 2 * getWidth() / 4, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / 4, 0, 3 * getWidth() / 4, getHeight(),
                paintLine);

    }
}