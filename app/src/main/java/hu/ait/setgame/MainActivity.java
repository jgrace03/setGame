package hu.ait.setgame;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import hu.ait.setgame.model.GameModel;
import hu.ait.setgame.view.GameView;


public class MainActivity extends AppCompatActivity {

    private TextView time;
    private TextView tvNumCards;
    private Button btnShuffle;
    private double startTime;
    private double elapsedTime;
    private Timer mainTimer = null;

    private class MyShowTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    elapsedTime = (
                            System.currentTimeMillis() - startTime) / 1000.0;
                    String timeText = String.format("%.2f", elapsedTime) + getString(R.string.ss);
                    time.setText(timeText);
                }
            });

        }
    }

    public static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        showSplash();

        super.onCreate(savedInstanceState);
        ((MainApplication)getApplication()).openRealm();

        setContentView(R.layout.activity_main);
        setupUI();

        final GameView gameView = (GameView) findViewById(R.id.gameView);

        showStartGameDialog(gameView);

        btnShuffle = (Button) findViewById(R.id.shuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.inval();
                GameModel.getInstance().shuffle();
            }
        });
    }

    private void setupUI() {
        time = (TextView) findViewById(R.id.time);
        time.setText(R.string.zero_seconds);

        tvNumCards = (TextView) findViewById(R.id.tvNumCards);
        tvNumCards.setText(R.string.total_cards);

        btnShuffle = (Button) findViewById(R.id.shuffle);
    }

    public void updateCardsLeft(int numCards) {
        tvNumCards.setText(String.valueOf(numCards));
    }

    public double endGame() {
        if (mainTimer != null) {
            mainTimer.cancel();
            mainTimer = null;
        }

        return elapsedTime;
    }

    public void openScoreBoard(){
        Intent intent = new Intent(this, ScoreBoard.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_CANCELED:
                final GameView gameView = (GameView) findViewById(R.id.gameView);
                showStartGameDialog(gameView);
                break;
        }
    }

    public void showUserNameDialog(final GameView gameView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater myLayout = this.getLayoutInflater();
        final View Username = myLayout.inflate(R.layout.username, null);

        builder.setView(Username);
        final AlertDialog alert = builder.create();
        alert.show();

        Button startGame = (Button) Username.findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = (EditText) Username.findViewById(R.id.etUsername);
                String username = "";
                username = etUsername.getText().toString();
                if (username.equals("")) { username = getString(R.string.anonymous); }

                startNewGame(gameView, username);
                alert.dismiss();
            }
        });
    }

    public void startNewGame(GameView gameView, String username) {
        GameModel.getInstance(this,((MainApplication)getApplication()).getRealm(), username).startGame();
        gameView.inval();

        startTime = System.currentTimeMillis();
        if (mainTimer == null) {
            mainTimer = new Timer();
            mainTimer.schedule(new MyShowTimerTask(), 0, 100);
        }
    }

    public void showSplash() {
        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
    }

    public void showStartGameDialog(final GameView gameView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater myLayout = this.getLayoutInflater();

        View Buttons = myLayout.inflate(R.layout.buttons, null);

        builder.setView(Buttons);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnNewGame = (Button) Buttons.findViewById(R.id.newGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showUserNameDialog(gameView);
            }
        });

        Button btnViewScores = (Button) Buttons.findViewById(R.id.viewScores);
        btnViewScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                openScoreBoard();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }
}
