package hu.ait.setgame.data;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Game extends RealmObject {

    @PrimaryKey
    private String gameID;
    private String userName;
    private String time;

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGameID() {
        return gameID;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

}
