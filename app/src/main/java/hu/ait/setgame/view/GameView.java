package hu.ait.setgame.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import hu.ait.setgame.R;
import hu.ait.setgame.model.Card;
import hu.ait.setgame.model.GameModel;

import static android.R.color.holo_purple;

public class GameView extends View {

    private Paint paintLine;
    private Paint paintRect;
    private Paint paintHighLight;
    private Bitmap bitmapLogo;
    private Bitmap bitmapCard;
    Context context;
    int width;
    int height;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        paintLine = new Paint();
        paintLine.setColor(getResources().getColor(holo_purple));
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintHighLight = new Paint();
        paintHighLight.setColor(Color.parseColor("#0BB5FF"));
        paintHighLight.setStyle(Paint.Style.STROKE);
        paintHighLight.setStrokeWidth(15);

        paintRect = new Paint();
        paintRect.setColor(Color.parseColor("#d3d3d3"));
        paintRect.setStyle(Paint.Style.FILL);

        bitmapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.set);

        width = GameModel.getInstance().width;
        height = GameModel.getInstance().height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmapLogo = Bitmap.createScaledBitmap(bitmapLogo, getWidth()/8, getHeight()/8, false);
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
        // three horizontal lines
        canvas.drawLine(0, getHeight() / height, getWidth(), getHeight() / height,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / height, getWidth(),
                2 * getHeight() / height, paintLine);
        canvas.drawLine(0, 3 * getHeight() / height, getWidth(),
                3 * getHeight() / height, paintLine);


        // four vertical lines
        canvas.drawLine(getWidth() / width, 0, getWidth() / width, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / width, 0, 2 * getWidth() / width, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / width, 0, 3 * getWidth() / width, getHeight(),
                paintLine);
        canvas.drawLine(4 * getWidth() / width, 0, 4 * getWidth() / width, getHeight(),
                paintLine);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (GameModel.getInstance().getSelected(i,j)) {

                    float leftX = j * getWidth() / width ;
                    float rightX = (j + 1) * getWidth() / width;
                    float topY = i * getHeight() / height;
                    float bottomY = (i + 1) * getHeight() / height;

                    canvas.drawRect(leftX, topY, rightX, bottomY, paintHighLight);
                }
            }
        }

    }

    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (GameModel.getInstance().getField(i,j) == null) {

                    // draw a circle at the center of the field

                    // X coordinate: left side of the square + half width of the square
                    float leftX = j * getWidth() / width ;
                    float rightX = (j + 1) * getWidth() / width;
                    float topY = i * getHeight() / height;
                    float bottomY = (i + 1) * getHeight() / height;

                    canvas.drawRect(leftX, topY, rightX, bottomY, paintRect);

                    canvas.drawBitmap(
                            bitmapLogo,
                            leftX + getWidth() / (width * width),
                            topY + getHeight() / (height * height), null);


                } else {
                    drawCard(GameModel.getInstance().getField(i,j), canvas);
                    float leftX = j * getWidth() / width ;
                    float rightX = (j + 1) * getWidth() / width;
                    float topY = i * getHeight() / height;
                    float bottomY = (i + 1) * getHeight() / height;

                    canvas.drawBitmap(
                            bitmapCard,
                            leftX, topY, null);

                    //canvas.drawRect(leftX, topY, rightX, bottomY, paintHighLight);
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
        bitmapCard = Bitmap.createScaledBitmap(bitmapCard, getWidth()/ width, getHeight()/height, false);
    }

    public void inval() {
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = ((int)event.getX())/ (getWidth() / width);
            int tY = ((int)event.getY())/ (getHeight() / height);

            GameModel.getInstance().select(tY,tX);
            invalidate();
        }
        return true;
    }

}