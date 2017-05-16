package hu.ait.setgame.model;


import java.util.ArrayList;
import java.util.List;

import hu.ait.setgame.R;
import hu.ait.setgame.view.GameView;

public class GameModel {

    private int cards_left = 81;

    private static GameModel instance = null;

    private GameModel() { //private constructor makes class a singleton
        //setField(0,0, Card.RED, Card.DIAMOND, Card.OUTLINED, (short) 2);
    }

    public void startGame() {
        initCards();
        initBoard();
        //GameView gameView = (GameView) findViewById(R.id.gameView);
        //gameView.startGame();
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }

        return instance;
    }

    private Card [][] model = {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
    };

    private List<Card> cards = new ArrayList<Card>();

    public Card getField(int x, int y) {
        return model[x][y];
    }

    /*public void setField(int x, int y, short color, short shape,
                         short shading, short number) {
        model[x][y] = new Card(color, shape, shading, number);
    }*/

    private void initCards() {

        for (short c = 0; c < 3; c++) {
            for (short s = 0; s < 3; s++) {
                for (short sd = 0; sd < 3; sd++) {
                    for (short n = 1; n < 4; n++) {
                        Card card = new Card(c,s,sd,n);
                        cards.add(card);
                    }
                }
            }
        }

    }

    private void initBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                model[i][j] = getRandomCard();
            }
        }
    }

    private Card getRandomCard() {
        int R = (int)(Math.random() * ((cards_left) + 1));
        cards_left--;
        return cards.remove(R);
    }

}
