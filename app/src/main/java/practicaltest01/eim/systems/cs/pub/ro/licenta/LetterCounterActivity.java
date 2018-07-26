package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;

public class LetterCounterActivity extends AppCompatActivity {



    private TextView textView;
    private boolean stopCounting;
    Handler handler;

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private MediaPlayer validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_counter);

        textView = (TextView)findViewById(R.id.letterCounter);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        try {
            generateLetter();
            validate = MediaPlayer.create(getApplicationContext(), R.raw.validate);
            validate.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public char pickLetter() {
        return textView.getText().charAt(0);
    }

    public String takeNextLetter () {
        String value = textView.getText().toString();
        int charValue = value.charAt(0);

        return String.valueOf( (char) (charValue + 1));
    }

    public void generateLetter () throws InterruptedException {
        stopCounting = false;
        textView.setText("A");

        handler = new Handler();
        final long startTime = System.currentTimeMillis();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
                    @Override
                    public void onShake() {
                        Intent resultIntent = new Intent();
                        String letterResult = textView.getText().toString();
                        resultIntent.putExtra("letterResult", letterResult);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });

                if (textView.getText().toString().equals("Z")) {
                    textView.setText("A");
                    Log.d("tag", textView.getText().toString());
                } else {
                    textView.setText(takeNextLetter());
                    Log.d("tag", textView.getText().toString());
                }
                handler.postDelayed(this, 250);

                long stopTime = System.currentTimeMillis();
                if (stopTime - startTime > 15000) {
                    Intent resultIntent = new Intent();
                    String letterResult = textView.getText().toString();

                    resultIntent.putExtra("letterResult", letterResult);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    handler.removeCallbacks(this);
                }
            }
        };
        runnable.run();
    }

    public boolean getStopCounting() {
        return stopCounting;
    }

    public void setStopCounting(boolean myBool) {
        stopCounting = myBool;
    }
}
