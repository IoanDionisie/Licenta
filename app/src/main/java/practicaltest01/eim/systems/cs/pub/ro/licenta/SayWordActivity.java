package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SayWordActivity extends AppCompatActivity {

    private TextView txvResult;
    private TextView hurryUp;
    private Button pressToSpeak, tryAgainButton, noKnow;
    private TextView fLetter, tryAgain, countTimer;
    public String gameDiff;
    public int playerPenalty, enemyPenalty;
    private int seconds;
    private MediaPlayer mp;

    private CountDownTimer myTimer;

    private NoKnowClickListener noKnowClickListener  = new NoKnowClickListener();
    private class NoKnowClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), PlayGame.class);
            intent.putExtra("playerPenalty", 1);
            setResult(Activity.RESULT_OK, intent);
            mp.stop();
            finish();
        }
    }

    private TryAgainSetOnClickListener tryAgainSetOnClickListener = new TryAgainSetOnClickListener();
    private class TryAgainSetOnClickListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent mIntent = getIntent();
            finish();
            startActivity(mIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_word);

        txvResult = (TextView) findViewById(R.id.txvResult);
        pressToSpeak = (Button)findViewById(R.id.btnSpeak);
        tryAgain = (TextView) findViewById(R.id.tryAgain);
        tryAgain.setVisibility(View.GONE);
        tryAgainButton = (Button) findViewById(R.id.tryAgainButton);
        tryAgainButton.setOnClickListener(tryAgainSetOnClickListener);
        tryAgainButton.setVisibility(View.INVISIBLE);
        hurryUp = (TextView) findViewById(R.id.hurryUp);
        noKnow = findViewById(R.id.noKnow);
        noKnow.setOnClickListener(noKnowClickListener);
        countTimer = (TextView)findViewById(R.id.countTimer);
        countTimer.setVisibility(View.VISIBLE);
        countTimer.setText(String.valueOf(seconds));

        seconds = 30;

        Intent playGame = getIntent();

        playerPenalty = playGame.getIntExtra("playerPenalty", 0);
        enemyPenalty =  playGame.getIntExtra("enemyPenalty", 0);

        /* The first word said by the player */
        if (playGame != null && playGame.hasExtra("firstLetter")) {
            String firstLetter = playGame.getStringExtra("firstLetter");
            txvResult.setText(firstLetter);
        }

        /* The rest of the words said by a player, excepting the first one */
        if (playGame != null && playGame.hasExtra("twoLetters")) {
            String twoLet = playGame.getStringExtra("twoLetters");
            txvResult.setText(twoLet);
        }

        long timeLeft = 30000;

        mp = MediaPlayer.create(getApplicationContext(), R.raw.time);
        mp.start();

        myTimer = new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                countTimer.setText(String.valueOf(millisUntilFinished / 1000));

                if (millisUntilFinished <= 10000) {
                    hurryUp.setText("Grabeste-te! Nu mai e prea mult timp!");
                }
            }

            public void onFinish() {
                noKnow.performClick();
                mp.stop();
            }
        }.start();

        myTimer.start();
    }

    /* Gets a vocal input from the player */
    public void getSpeechInput(View view) {
        mp.stop();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // intent.putExtra(RecognizerIntent.EXTRA_SECURE, RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10 || requestCode == 13) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                String lowerCaseResult = result.get(0).toLowerCase();
                String lowerCaseFletter = txvResult.getText().toString().toLowerCase();
                txvResult.setText(lowerCaseResult);

                switch (lowerCaseFletter.length()) {
                    case 1:
                        if (lowerCaseResult.charAt(0) == lowerCaseFletter.charAt(0)) {
                            myTimer.cancel();
                            Intent intent = new Intent(getApplicationContext(), PlayGame.class);
                            intent.putExtra("playerWord", lowerCaseResult);
                            intent.putExtra("playerPenalty", 0);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            tryAgain.setVisibility(View.VISIBLE);
                            tryAgainButton.setVisibility(View.VISIBLE);
                            String tryAgainText = "Cuvantul trebuie sa inceapa cu litera:  " + lowerCaseFletter.charAt(0);
                            txvResult.setText(lowerCaseResult);
                            tryAgain.setText(tryAgainText);
                            tryAgain.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        if (lowerCaseFletter.charAt(0) == lowerCaseResult.charAt(0) &&
                                lowerCaseFletter.charAt(1) == lowerCaseResult.charAt(1)) {
                            myTimer.cancel();
                            Intent intent = new Intent(getApplicationContext(), PlayGame.class);
                            intent.putExtra("playerWord", lowerCaseResult);
                            intent.putExtra("playerPenalty", 0);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            tryAgain.setVisibility(View.VISIBLE);
                            tryAgainButton.setVisibility(View.VISIBLE);
                            String tryAgainText = "Cuvantul trebuie sa inceapa cu literele:  " + lowerCaseFletter.substring(0, 2);
                            txvResult.setText(lowerCaseResult);
                            tryAgain.setText(tryAgainText);
                        }
                        break;

                    case 0:
                        break;
                }
            }
        }
    }
}