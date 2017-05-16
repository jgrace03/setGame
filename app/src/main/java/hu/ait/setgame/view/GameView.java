package hu.ait.setgame.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import hu.ait.setgame.R;
import hu.ait.setgame.model.GameModel;

import static android.R.color.holo_purple;

public class GameView extends View {

    private Paint paintLine;
    private Paint paintRect;
    private Bitmap bitmapLogo;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintLine = new Paint();
        paintLine.setColor(getResources().getColor(holo_purple));
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintRect = new Paint();
        paintRect.setColor(Color.parseColor("#d3d3d3"));
        paintRect.setStyle(Paint.Style.FILL);

        bitmapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.set);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmapLogo = Bitmap.createScaledBitmap(bitmapLogo, getWidth()/8, getHeight()/8, false);

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
        drawPlayers(canvas);
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

    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (GameModel.getInstance().getField(i,j) == null) {

                    // draw a circle at the center of the field

                    // X coordinate: left side of the square + half width of the square
                    float leftX = j * getWidth() / 4 ;
                    float rightX = (j + 1) * getWidth() /4;
                    float topY = i * getHeight() / 3;
                    float bottomY = (i + 1) * getHeight() / 3;

                    canvas.drawRect(leftX, topY, rightX, bottomY, paintRect);

                    canvas.drawBitmap(
                            bitmapLogo,
                            leftX + getWidth() / 16, topY + getHeight() / 9, null);


                } /*else if (TicTacToeModel.getInstance().getField(i,j) == TicTacToeModel.CROSS) {
                    canvas.drawLine(i * getWidth() / 3, j * getHeight() / 3,
                            (i + 1) * getWidth() / 3,
                            (j + 1) * getHeight() / 3, paintCross);

                    canvas.drawLine((i + 1) * getWidth() / 3, j * getHeight() / 3,
                            i * getWidth() / 3, (j + 1) * getHeight() / 3, paintCross);
                } */
            }
        }
    }
}