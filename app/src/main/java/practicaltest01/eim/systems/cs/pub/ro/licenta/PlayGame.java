package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.prefs.Preferences;

public class PlayGame extends AppCompatActivity {

    TextView gameDiff, letterCounterText, opponentWord, inchis;
    String gameDifficulty;
    Button goToLetterCounter, goToSayWord;
    RelativeLayout opponentThinking, opponentPicks;
    TextView letterSelected, firstPlayerWord;

    MediaPlayer mp, mpLost;

    TextView F1, A1, A21, Z1, N1, F2, A12, A22, Z2, N2;

    RandomPicker picker;

    public SharedPreferences.Editor mEdit;
    public SharedPreferences preferences;

    public final int letterCounterRequestCode = 10;
    public final int sayWordPlayerRequestCode = 13;
    public int firstTimeInGame = 0;
    public String firstTwoLetters;
    int playerPenalty = 0, enemyPenalty = 0;

    private GoToSayWordClickListener goToSayWordClickListener = new GoToSayWordClickListener();
    private class GoToSayWordClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SayWordActivity.class);

            if (letterSelected.getText() != null) {
                String firstLetter = letterSelected.getText().toString();
                intent.putExtra("firstLetter", firstLetter);
            }

            if (firstTwoLetters != null) {
                intent.putExtra("twoLetters", firstTwoLetters);
            }
            startActivityForResult(intent, sayWordPlayerRequestCode);
        }
    }

    private  GoToLetterCounterClickListener goToLetterCounterClickListener = new GoToLetterCounterClickListener();
    private class GoToLetterCounterClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), LetterCounterActivity.class);
            intent.putExtra("extra", "myExtra");
            startActivityForResult(intent, letterCounterRequestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_play_game);

        gameDiff = (TextView) findViewById(R.id.playGameGameDiff);
        letterCounterText = (TextView) findViewById(R.id.letterCounter);
        goToLetterCounter = (Button) findViewById(R.id.goToLetterCounter);
        goToLetterCounter.setOnClickListener(goToLetterCounterClickListener);
        letterSelected = (TextView) findViewById(R.id.letterSelected);
        firstPlayerWord = (TextView) findViewById(R.id.firstPlayerWord);
        opponentThinking = (RelativeLayout) findViewById(R.id.opponentThinking);
        opponentThinking.setVisibility(View.INVISIBLE);
        opponentWord = (TextView) findViewById(R.id.opponentWord);
        goToSayWord = (Button) findViewById(R.id.goToSayWord);
        goToSayWord.setOnClickListener(goToSayWordClickListener);
        opponentPicks = (RelativeLayout) findViewById(R.id.opponentPicks);
        opponentPicks.setVisibility(View.INVISIBLE);
        inchis = findViewById(R.id.inchis);
        inchis.setVisibility(View.INVISIBLE);

        F1 = findViewById(R.id.F1);
        A1 = findViewById(R.id.A1);
        Z1 = findViewById(R.id.Z1);
        A21 = findViewById(R.id.A12);
        N1 = findViewById(R.id.N1);

        F2 = findViewById(R.id.F2);
        A12 = findViewById(R.id.A21);
        A22 = findViewById(R.id.A22);
        Z2 = findViewById(R.id.Z2);
        N2 = findViewById(R.id.N2);

        Intent newIntent = getIntent();
        gameDiff.setText(newIntent.getStringExtra("gameDifficulty"));

        preferences =  PreferenceManager.getDefaultSharedPreferences(this);

        if (newIntent.hasExtra("firstTimeInGame")) {
            if (newIntent.getIntExtra("firstTimeInGame", 5) == 0)
                firstTimeInGame = 1;
            else
                firstTimeInGame = 1000;
        }

        if (firstTimeInGame == 1) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            mEdit = preferences.edit();
            mEdit.clear();
            mEdit.commit();
            mEdit.apply();
        }

        mp = MediaPlayer.create(getApplicationContext(), R.raw.wonround);
        mpLost = MediaPlayer.create(getApplicationContext(), R.raw.lostround);

        mEdit = preferences.edit();

        if (preferences.contains("turn")) {
            if (preferences.getInt("turn", 0) % 2 == 1) {
                opponentPicks.setVisibility(View.VISIBLE);
                goToLetterCounter.setVisibility(View.INVISIBLE);

                new CountDownTimer(5000, 5000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Random r = new Random();
                        char c = (char) (r.nextInt(26) + 'a');
                        if (c == 'w' || c == 'y')
                            c = (char) (r.nextInt(26) + 'a');

                        letterSelected.setText(String.valueOf(c));
                        goToSayWord.setVisibility(View.VISIBLE);
                        opponentPicks.setVisibility(View.INVISIBLE);
                        opponentWord.setText(String.valueOf(c));
                    }
                }.start();
            }
        }

        if (preferences.contains("playerPenalty"))
             playerPenalty = preferences.getInt("playerPenalty", 0);
        else
            playerPenalty = 0;

        if (preferences.contains("enemyPenalty"))
             enemyPenalty = preferences.getInt("enemyPenalty", 0);
        else
            enemyPenalty = 0;

        if (playerPenalty >= 1)
            F1.setBackgroundResource(R.drawable.redbutton);
        if (playerPenalty >= 2)
            A1.setBackgroundResource(R.drawable.redbutton);
        if (playerPenalty >= 3)
            Z1.setBackgroundResource(R.drawable.redbutton);
        if (playerPenalty >= 4)
            A12.setBackgroundResource(R.drawable.redbutton);
        if (playerPenalty == 5)
            N1.setBackgroundResource(R.drawable.redbutton);

        if (enemyPenalty >= 1)
            F2.setBackgroundResource(R.drawable.redbutton);
        if (enemyPenalty >= 2)
            A21.setBackgroundResource(R.drawable.redbutton);
        if (enemyPenalty >= 3)
            Z2.setBackgroundResource(R.drawable.redbutton);
        if (enemyPenalty >= 4)
            A22.setBackgroundResource(R.drawable.redbutton);
        if (enemyPenalty == 5)
            N2.setBackgroundResource(R.drawable.redbutton);

        if (playerPenalty == 5) {
            Intent gameOver = new Intent(getApplicationContext(), GameOver.class);
            gameOver.putExtra("won", false);
            finish();
            startActivity(gameOver);
        } else if (enemyPenalty == 5) {
            Intent gameOver = new Intent(getApplicationContext(), GameOver.class);
            gameOver.putExtra("won", true);
            finish();
            startActivity(gameOver);
        }
    }

    public char GetLetter(int value) {
        return (char) ('A' - 1 + value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED && data != null ) {
            if (resultCode == Activity.RESULT_OK && data.hasExtra("letterResult")) {
                letterSelected.setText(data.getStringExtra("letterResult"));
                goToSayWord.performClick();
            }

            if (data != null && data.hasExtra("playerPenalty") && data.getIntExtra("playerPenalty", 0) == 1) {
                Intent intent = getIntent();
                mEdit = preferences.edit();
                playerPenalty++;
                mpLost.start();

                int turn = 0;
                if (preferences.contains("turn")) {
                    turn = preferences.getInt("turn", 0) + 1;
                    mEdit.putInt("turn", turn);
                } else {
                    mEdit.putInt("turn", 1);
                }

                mEdit.putInt("playerPenalty", playerPenalty);
                mEdit.putInt("enemyPenalty", enemyPenalty);
                mEdit.apply();
                intent.putExtra("firstTimeInGame", 100);
                finish();
                startActivity(intent);
            } else if (data != null && data.hasExtra("playerWord")) {
                String str = data.getStringExtra("playerWord");
                firstPlayerWord.setText(str);
                goToLetterCounter.setVisibility(View.INVISIBLE);

                try {
                    opponentPicksWord(findAWord(str));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void opponentPicksWord(String myWord) {

        final String word =  myWord.toLowerCase();
        final String lastTwoLetters = myWord.substring(word.length() - 2, word.length());
        final String playerWord = lastTwoLetters + "_ _ _ _ _";
        opponentThinking.setVisibility(View.VISIBLE);
        goToLetterCounter.setVisibility(View.INVISIBLE);

        if (myWord.equals("notFound")) {
            goToSayWord.setVisibility(View.INVISIBLE);
            new CountDownTimer(2000, 2000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    opponentThinking.setVisibility(View.INVISIBLE);
                    opponentWord.setText(" ");

                    inchis.setVisibility(View.VISIBLE);
                    String myText = "L-ai inchis cu: " + firstPlayerWord.getText().toString();

                    mp.start();
                    inchis.setText(myText);
                }
            }.start();

            new CountDownTimer(5000, 5000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    inchis.setVisibility(View.INVISIBLE);
                    Intent intent = getIntent();
                    mEdit = preferences.edit();
                    enemyPenalty ++;
                    int turn = 0;
                    if (preferences.contains("turn")) {
                        turn = preferences.getInt("turn", 0) + 1;
                        mEdit.putInt("turn", turn);
                    } else {
                        mEdit.putInt("turn", 1);
                    }
                    mEdit.putInt("enemyPenalty", enemyPenalty);
                    mEdit.putInt("playerPenalty", playerPenalty);
                    mEdit.apply();
                    intent.putExtra("firstTimeInGame", 100);
                    finish();
                    startActivity(intent);
                }
            }.start();

        }

        new CountDownTimer(5000, 5000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                opponentThinking.setVisibility(View.INVISIBLE);
                goToSayWord.setVisibility(View.VISIBLE);
                opponentWord.setText(word);
                firstPlayerWord.setText(playerWord);
                String opponent = opponentWord.getText().toString();
                firstTwoLetters = opponent.substring(opponent.length() - 2, opponent.length());
            }
        }.start();
    }

    public String findAWord(String word) throws IOException {
        WordFinder finder = new WordFinder(lastTwoLetterS(word));

        gameDifficulty = gameDiff.getText().toString();
        picker = new RandomPicker(gameDifficulty);

        if (picker.selectChoice()) {
            return finder.pickToWin(finder.findWord());
        } else {
            return finder.pickToContinue(finder.findWord());
        }
    }

    public String lastTwoLetterS(String word) {
        return  word.substring(word.length() - 2, word.length());
    }

}
