package hu.ait.setgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hu.ait.setgame.view.GameView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        showSplash();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GameView gameView = (GameView) findViewById(R.id.gameView);

        showStartGameDialog();
    }

    public void openScoreBoard(){
        Intent intent = new Intent(this, ScoreBoard.class);
        startActivity(intent);
    }

    public void showUserNameDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater myLayout = this.getLayoutInflater();

        View Username = myLayout.inflate(R.layout.username, null);

        builder.setView(Username);
        final AlertDialog alert = builder.create();
        alert.show();

        Button startGame = (Button) Username.findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }

    public void showSplash() {
        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
    }

    public void showStartGameDialog() {
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
                showUserNameDialog();
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
}
