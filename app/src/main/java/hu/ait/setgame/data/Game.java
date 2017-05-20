package hu.ait.setgame.data;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Game extends RealmObject {

    @PrimaryKey
    private String gameID;
    private String userName;
    private double time;

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }
}
