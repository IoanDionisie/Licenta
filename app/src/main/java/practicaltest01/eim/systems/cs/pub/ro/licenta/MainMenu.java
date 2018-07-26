package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainMenu extends Activity {
    private RelativeLayout checkWifi;
    private TextView gameDifficulty;

    private RelativeLayout mainLayout;
    private Button playGame, settings, multiplayer, guessPersonality, exitGame, okWifi, noWifi;

    private MediaPlayer startSong, back;

    private final static int PLAY_GAME_ACTIVITY_REQUEST_CODE = 2,
            SETTINGS_REQUEST_CODE = 3,
            MULTIPLAYER_REQUEST_CODE = 4,
            GUESS_PERSONALITY_REQUEST_CODE = 5,
            EXIT_GAME_REQUEST_CODE = 6;


    private EnableWifiClickListener enableWifiClickListener = new EnableWifiClickListener();
    private class EnableWifiClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch(view.getId()) {
                case R.id.yesWiFi:
                    WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    break;
                case R.id.noWiFi:
                    break;
            }
            mainLayout.setVisibility(View.VISIBLE);
            checkWifi.setVisibility(View.INVISIBLE);
        }
    }


    private MenuButtonClickListener menuButtonClickListener = new MenuButtonClickListener();
    private class MenuButtonClickListener implements View.OnClickListener {

        @Override
        public  void onClick(View view) {

            startSong.stop();
            back.start();

            switch (view.getId()) {
                case R.id.playGame:
                    Intent intent = new Intent(getApplicationContext(), PlayGame.class);
                    String gameDiff = gameDifficulty.getText().toString();
                    intent.putExtra("gameDifficulty", gameDiff);
                    intent.putExtra("firstTimeInGame", 0);
                    startActivity(intent);
                    break;
                case R.id.settings:
                    Intent intent2 = new Intent(getApplicationContext(), Settings.class);
                    startActivityForResult(intent2, SETTINGS_REQUEST_CODE);
                    break;
                case R.id.multiplayer:
                    if (isOnline() || checkWiFi()) {
                        Intent intent3 = new Intent(getApplicationContext(), Multiplayer.class);
                        startActivityForResult(intent3, MULTIPLAYER_REQUEST_CODE);
                        break;
                    } else {
                        checkWifi.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.INVISIBLE);
                        break;
                    }
                case R.id.guessPersonality:
                    Intent intent4 = new Intent(getApplicationContext(), GuessYourPersonality.class);
                    startActivityForResult(intent4, GUESS_PERSONALITY_REQUEST_CODE);
                    break;
                case R.id.exitGame:
                    Intent intent5 = new Intent(getApplicationContext(), ExitGame.class);
                    startActivityForResult(intent5, EXIT_GAME_REQUEST_CODE);
                    break;
            }
        }
    }
    /* TODO */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        checkWifi = (RelativeLayout)findViewById(R.id.checkWifi);

        playGame = (Button)findViewById(R.id.playGame);
        settings = (Button)findViewById(R.id.settings);
        multiplayer = (Button)findViewById(R.id.multiplayer);
        guessPersonality = (Button)findViewById(R.id.guessPersonality);
        exitGame = (Button)findViewById(R.id.exitGame);

        gameDifficulty = (TextView)findViewById(R.id.showGameDificulty);

        okWifi = (Button)findViewById(R.id.yesWiFi);
        noWifi = (Button)findViewById(R.id.noWiFi);

        okWifi.setOnClickListener(enableWifiClickListener);
        noWifi.setOnClickListener(enableWifiClickListener);
        playGame.setOnClickListener(menuButtonClickListener);
        settings.setOnClickListener(menuButtonClickListener);
        multiplayer.setOnClickListener(menuButtonClickListener);
        guessPersonality.setOnClickListener(menuButtonClickListener);
        exitGame.setOnClickListener(menuButtonClickListener);


        Intent menuIntent = getIntent();
        startSong = MediaPlayer.create(getApplicationContext(), R.raw.start);
        back = MediaPlayer.create(getApplicationContext(), R.raw.back);
        startSong.start();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SETTINGS_REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String gameDifficultySet = data.getStringExtra("gameDifficulty");

                    if (gameDifficultySet != null) {
                        gameDifficulty.setText(gameDifficultySet);
                    }
                }
                break;
            }
            case (EXIT_GAME_REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                    System.exit(0);
                } else {
                    Log.d("DONT_EXIT", "Didnt close the game, motherfucker");
                }
            }
        }
    }


    public boolean checkWiFi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled())
            return true;
        else
            return false;
    }

    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


