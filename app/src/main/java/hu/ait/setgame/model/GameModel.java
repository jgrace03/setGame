package hu.ait.setgame.model;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import hu.ait.setgame.MainActivity;
import hu.ait.setgame.R;
import hu.ait.setgame.data.Game;
import hu.ait.setgame.view.GameView;
import io.realm.Realm;


public class GameModel {

    private MainActivity mainActivity;
    private Realm realmGame;
    private static GameModel instance = null;

    private String username;
    public int cards_left;
    private int num_selected;
    public int width = 5;
    public int height = 4;

    public GameModel(MainActivity activity, Realm realmGame, String username) {
        this.mainActivity = activity;
        this.realmGame = realmGame;
        this.username = username;

        clearModel();
        unSelectAll();
    }

    public static GameModel getInstance(MainActivity activity, Realm realmGame, String username) {
        if (instance == null) {
            instance = new GameModel(activity, realmGame, username);
        } else {
            instance.mainActivity = activity;
            instance.realmGame = realmGame;
            instance.username = username;
        }
        return instance;
    }


    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }

        return instance;
    }

    private GameModel() {
        clearModel();
        unSelectAll();
    }

    public void startGame() {
        clearModel();
        unSelectAll();
        initCards();
        initBoard();
    }

    private void endGame(double time) {
        saveGame(time);

        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        LayoutInflater myLayout = mainActivity.getLayoutInflater();

        View viewGameOver = myLayout.inflate(R.layout.game_over, null);
        builder.setView(viewGameOver);
        final AlertDialog alert = builder.create();
        alert.show();

        Button btnRestartGame = (Button) viewGameOver.findViewById(R.id.btnNewGame);
        btnRestartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                GameView gameView = (GameView) mainActivity.findViewById(R.id.gameView);
                mainActivity.showUserNameDialog(gameView);
                alert.dismiss();
            }
        });

        Button btnViewHighScores = (Button) viewGameOver.findViewById(R.id.btnHighScores);
        btnViewHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                mainActivity.openScoreBoard();
                alert.dismiss();
            }
        });
    }

    private void saveGame(double time) {
        Game game = new Game();
        game.setGameID(UUID.randomUUID().toString());
        game.setUserName(username);
        game.setTime(time);

        if (!realmGame.isInTransaction()) { realmGame.beginTransaction(); }
        realmGame.copyToRealm(game);
        realmGame.commitTransaction();
    }

    private Card [][] model = {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
    };

    private boolean [][] selected = {
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
    };

    public boolean getSelected(int i, int j) {
        return selected[i][j];
    }

    public void select(int i, int j) {
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
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                model[i][j] = null;
            }
        }

        cards_left = 81;
    }

    public void unSelectAll() {

        num_selected = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                selected[i][j] = false;
            }
        }
    }

    private List<Card> cards = new ArrayList<Card>();

    public Card getField(int x, int y) {
        return model[x][y];
    }

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
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                model[i][j] = getRandomCard();
            }
        }

        mainActivity.updateCardsLeft(cards_left);
    }

    private Card getRandomCard() {

        if (cards_left == 0)
            return null;

        int R = (int)(Math.random() * (cards_left - 1));
        cards_left--;
        return cards.remove(R);
    }

    public void shuffle() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (selected[i][j] == true) {
                    if (model[i][j] != null) {
                        selectedCards[k] = model[i][j];
                        XCoords[k] = j;
                        YCoords[k] = i;
                        k++;
                    } else {
                        unSelectAll();
                        return;
                    }
                }
            }
        }

        if (checkSet(selectedCards)) {
            for (int i = 0; i < 3; i++) {
                cards.remove(selectedCards[i]);
                model[YCoords[i]][XCoords[i]] = getRandomCard();
            }

            mainActivity.updateCardsLeft(cards_left);

            if (cards_left == 0) {
                endGame(mainActivity.endGame());
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
