package hu.ait.setgame.model;


public class GameModel {


    private static GameModel instance = null;

    private GameModel() { //private constructor makes class a singleton
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

    public Card getField(int x, int y) {
        return model[x][y];
    }
    public void setField(int x, int y, short color, short shape,
                         short shading) {
        model[x][y] = new Card(color, shape, shading);
    }

}
