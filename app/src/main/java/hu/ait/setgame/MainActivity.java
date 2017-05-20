package hu.ait.setgame;


import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import hu.ait.setgame.model.GameModel;
import hu.ait.setgame.view.GameView;

public class MainActivity extends AppCompatActivity {

    private TextView time;
    private TextView tvNumCards;
    private double startTime;
    private double ellapsedTime;
    private Timer mainTimer = null;

    private class MyShowTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ellapsedTime = (
                            System.currentTimeMillis()- startTime)/1000.0;
                    time.setText("" + String.format("%.2f", ellapsedTime) + "s");
                }
            });

        }
    }

    public static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        showSplash();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        time = (TextView) findViewById(R.id.time);
        time.setText("0.00s");

        final GameView gameView = (GameView) findViewById(R.id.gameView);
        tvNumCards = (TextView) findViewById(R.id.tvNumCards);
        tvNumCards.setText("81");

        showStartGameDialog(gameView);

        Button btnShuffle = (Button) findViewById(R.id.shuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.inval();
                GameModel.getInstance().shuffle();
            }
        });
    }

    public void updateCardsLeft(int numCards) {
        tvNumCards.setText(String.valueOf(numCards));
    }

    public double endGame() {
        if (mainTimer != null) {
            mainTimer.cancel();
            mainTimer = null;
        }

        return ellapsedTime;
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

        final EditText etUsername = (EditText) gameView.findViewById(R.id.etUsername);
        Button startGame = (Button) Username.findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = (EditText) Username.findViewById(R.id.etUsername);
                String username = "";
                // TODO: etUsername is null
                if (etUsername != null) {
                    username = etUsername.getText().toString();
                }
                if (username.equals("")) { username = "Anonymous"; }

                startNewGame(gameView, username);
//                GameModel.getInstance().startGame();
//                gameView.inval();
                alert.dismiss();
            }
        });

    }

    public void startNewGame(GameView gameView, String username) {
        GameModel.getInstance(this,((MainApplication)getApplication()).getRealm(), username)
                .startGame();
        gameView.inval();

        startTime = System.currentTimeMillis();

        if (mainTimer == null) {
            mainTimer = new Timer();

            mainTimer.schedule(new MyShowTimerTask(),
                    0, 10);
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
