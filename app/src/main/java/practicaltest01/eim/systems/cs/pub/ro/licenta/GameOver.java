package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionValues;

public class GameOver extends AppCompatActivity {

    Button playAgain, goToMainMenu;

    TextView youWon, youLost;
    RelativeLayout gameOver;

    MediaPlayer mp, back;

    private BackToPlayGameClickListener backToPlayGameClickListener = new BackToPlayGameClickListener();
    private class BackToPlayGameClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch(view.getId()) {
                case R.id.playAgain:
                    back = MediaPlayer.create(getApplicationContext(), R.raw.validate);
                    back.start();
                    Intent playAgain = new Intent(getApplicationContext(), PlayGame.class);
                    playAgain.putExtra("firstTimeInGame", 0);
                    finish();
                    startActivity(playAgain);
                    break;
                case R.id.goToMainMenu:
                    back = MediaPlayer.create(getApplicationContext(), R.raw.back);
                    back.start();
                    Intent backToMenu = new Intent(getApplicationContext(), MainMenu.class);
                    finish();
                    startActivity(backToMenu);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        youWon = findViewById(R.id.youWon);
        youLost = findViewById(R.id.youLost);
        gameOver = findViewById(R.id.gameOverLayout);

        playAgain = findViewById(R.id.playAgain);
        goToMainMenu = findViewById(R.id.goToMainMenu);

        playAgain.setOnClickListener(backToPlayGameClickListener);
        goToMainMenu.setOnClickListener(backToPlayGameClickListener);

        Intent myIntent = getIntent();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.wongame);

        if (myIntent.hasExtra("won")) {
            if (myIntent.getBooleanExtra("won", false)) {
                youWon.setVisibility(View.VISIBLE);
                youLost.setVisibility(View.INVISIBLE);
                mp.start();
            } else {
                youLost.setVisibility(View.VISIBLE);
                youWon.setVisibility(View.INVISIBLE);
            }
        }
    }


}
