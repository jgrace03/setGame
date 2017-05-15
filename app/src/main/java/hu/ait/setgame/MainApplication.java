package hu.ait.setgame;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {

    Realm realmGame;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmGame = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmGame.close();
    }

    public Realm getRealm() {
        return realmGame;
    }


}
