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
import hu.ait.setgame.model.Card;
import hu.ait.setgame.model.GameModel;

import static android.R.color.holo_purple;

public class GameView extends View {

    private Paint paintLine;
    private Paint paintRect;
    private Bitmap bitmapLogo;
    private Bitmap bitmapCard;
    Context context;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

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

        drawPlayers(canvas);
        drawGameGrid(canvas);
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


                } else {
                    drawCard(GameModel.getInstance().getField(i,j), canvas);
                    float leftX = j * getWidth() / 4 ;
                    float topY = i * getHeight() / 3;


                    canvas.drawBitmap(
                            bitmapCard,
                            leftX, topY, null);

                }
            }
        }
    }


    private void drawCard(Card card, Canvas canvas){
        short color = card.getColor();
        short shape = card.getShape();
        short shadding = card.getShading();
        short number = card.getNumber();

        String cardName = "";

        switch (color) {
            case Card.RED:
                cardName += "r";
                break;
            case Card.GREEN:
                cardName += "g";
                break;
            case Card.PURPLE:
                cardName += "p";
                break;
        }

        switch (shape) {
            case Card.DIAMOND:
                cardName += "d";
                break;
            case Card.OVAL:
                cardName += "o";
                break;
            case Card.SQUIGGLE:
                cardName += "s";
                break;
        }

        switch (shadding) {
            case Card.OUTLINED:
                cardName += "o";
                break;
            case Card.SOLID:
                cardName += "sl";
                break;
            case Card.STRIPED:
                cardName += "s";
                break;
        }

        switch (number) {
            case 1:
                cardName += "1";
                break;
            case 2:
                cardName += "2";
                break;
            case 3:
                cardName += "3";
                break;
        }

        int id = context.getResources().getIdentifier(cardName, "drawable", context.getPackageName());
        bitmapCard = BitmapFactory.decodeResource(getResources(), id);
        bitmapCard = Bitmap.createScaledBitmap(bitmapCard, getWidth()/4, getHeight()/3, false);
    }

    public void startGame() {
        invalidate();
    }
}