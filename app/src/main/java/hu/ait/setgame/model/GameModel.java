package hu.ait.setgame.model;


import java.util.ArrayList;
import java.util.List;

import hu.ait.setgame.R;
import hu.ait.setgame.view.GameView;

public class GameModel {

    private int cards_left;
    private int num_selected;

    private static GameModel instance = null;

    private GameModel() { //private constructor makes class a singleton
        //setField(0,0, Card.RED, Card.DIAMOND, Card.OUTLINED, (short) 2);
        clearModel();
        unSelectAll();
    }

    public void startGame() {
        clearModel();
        unSelectAll();
        initCards();
        initBoard();
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

    private boolean [][] selected = {
            {false, false, false, false},
            {false, false, false, false},
            {false, false, false, false},
    };

    public boolean getSelected(int i, int j) {
        return selected[i][j];
    }

    public void select(int i, int j) {

        //if (num_selected < 0) num_selected = 0; //fixes weird random mem error

        if (selected[i][j] == true) {
            selected[i][j] = false;
            num_selected--;
        } else {
            num_selected++;
            selected[i][j] = true;
            checkSet();
        }
    }

    public void clearModel() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                model[i][j] = null;
            }
        }

        cards_left = 81;
    }

    public void unSelectAll() {

        num_selected = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                selected[i][j] = false;
            }
        }
    }

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

        if (cards_left == 0)
            return null;

        int R = (int)(Math.random() * (cards_left - 1));
        cards_left--;
        return cards.remove(R);
    }

    public void shuffle() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                cards.add(model[i][j]);
                cards_left++;
            }
        }
        unSelectAll();
        initBoard();
    }

    public void checkSet() {

        if (num_selected < 3)
            return;

        Card [] selectedCards = new Card[3];
        int [] XCoords = new int[3];
        int [] YCoords = new int[3];

        int k = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (selected[i][j] == true) {
                    selectedCards[k] = model[i][j];
                    XCoords[k] = j;
                    YCoords[k] = i;
                    k++;
                }
            }
        }

        if (checkSet(selectedCards)) {
            for (int i = 0; i < 3; i++) {
                cards.remove(selectedCards[i]);
                model[YCoords[i]][XCoords[i]] = getRandomCard();
            }
        }

        unSelectAll();
    }


    private boolean checkSet(Card [] cards) {

        if (!allSame(cards[0].getColor(), cards[1].getColor(), cards[2].getColor()) &&
                !allDifferent(cards[0].getColor(), cards[1].getColor(), cards[2].getColor()))  {
            return false;
        }

        if (!allSame(cards[0].getShape(), cards[1].getShape(), cards[2].getShape()) &&
                !allDifferent(cards[0].getShape(), cards[1].getShape(), cards[2].getShape()))  {
            return false;
        }

        if (!allSame(cards[0].getShading(), cards[1].getShading(), cards[2].getShading()) &&
                !allDifferent(cards[0].getShading(), cards[1].getShading(), cards[2].getShading()))  {
            return false;
        }

        if (!allSame(cards[0].getNumber(), cards[1].getNumber(), cards[2].getNumber()) &&
                !allDifferent(cards[0].getNumber(), cards[1].getNumber(), cards[2].getNumber()))  {
            return false;
        }

        return true;
    }

    private boolean allDifferent(short s1, short s2, short s3) {

        if (s1 != s2) {
            if (s2 != s3) {
                if (s1 != s3) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean allSame(short s1, short s2, short s3) {

        if (s1 == s2) {
            if (s2 == s3) {
                return true;
            }
        }
        return false;
    }

}
