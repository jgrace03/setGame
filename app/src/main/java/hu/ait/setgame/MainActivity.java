package hu.ait.setgame;


import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import hu.ait.setgame.model.GameModel;
import hu.ait.setgame.view.GameView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        showSplash();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        final GameView gameView = (GameView) findViewById(R.id.gameView);

        showStartGameDialog(gameView);

        Button btnShuffle = (Button) findViewById(R.id.shuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gameView.resetGame();
                gameView.inval();
                GameModel.getInstance().shuffle();
            }
        });
    }

    public void openScoreBoard(){
        Intent intent = new Intent(this, ScoreBoard.class);
        startActivity(intent);
    }

    // NOTE: This method is not being used because no request codes are sent with Intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE) {
                    // TODO: Start a new game upon return from high scores
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showUserNameDialog(final GameView gameView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater myLayout = this.getLayoutInflater();
        View Username = myLayout.inflate(R.layout.username, null);

        builder.setView(Username);
        final AlertDialog alert = builder.create();
        alert.show();

        final EditText etUsername = (EditText) gameView.findViewById(R.id.etUsername);
        Button startGame = (Button) Username.findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = (EditText) gameView.findViewById(R.id.etUsername);
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
