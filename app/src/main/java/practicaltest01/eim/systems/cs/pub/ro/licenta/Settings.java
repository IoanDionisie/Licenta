package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    Button goBackToMainMenu;
    Spinner gameDifficulty;
    AudioManager audioManager;
    SeekBar seekBar;

    MediaPlayer back;

    private BackToMainMenuClickListener backToMainMenuClickListener = new BackToMainMenuClickListener();
    private class BackToMainMenuClickListener implements View.OnClickListener {

        String difficulty;

        @Override
        public void onClick(View view) {  // TODO MORE
            difficulty = gameDifficulty.getSelectedItem().toString();
            Intent resultIntent = new Intent();
            back = MediaPlayer.create(getApplicationContext(), R.raw.back);
            back.start();
            resultIntent.putExtra("gameDifficulty", difficulty);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        goBackToMainMenu = (Button)findViewById(R.id.backToMainMenu);
        goBackToMainMenu.setOnClickListener(backToMainMenuClickListener);
        gameDifficulty = (Spinner)findViewById(R.id.gameDifficulty);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        seekBar = (SeekBar)findViewById(R.id.seekBarSound);

        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b){
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setFadingEdgeLength(8);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gameDifficulties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameDifficulty.setAdapter(adapter);
    }
}
