package com.example.dell.timer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    final int MAX = 600000; // 10 Minutes
    final int INITIAL = 90000; // 1.5 Minutes

    SeekBar sb;
    TextView timer;
    Button trigger;
    CountDownTimer cdt;
    AudioManager am;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        am = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.alert);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);


        timer = (TextView) findViewById(R.id.timer);

        sb = (SeekBar) findViewById(R.id.seekBar);
        trigger = (Button) findViewById(R.id.goButton);

        setupTrigger();
        resetSeekBar();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Date date = new Date(progress);
                    DateFormat df = new SimpleDateFormat("mm:ss");
                    timer.setText(df.format(date));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void resetSeekBar() {

        sb.setEnabled(true);

        if (mp.isPlaying()) {
            mp.pause();
        }
        mp.seekTo(0);

        sb.setMax(MAX);
        sb.setProgress(INITIAL);

        Date date = new Date(INITIAL);
        DateFormat df = new SimpleDateFormat("mm:ss");
        timer.setText(df.format(date));

    }

    void setupTrigger() {
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (trigger.getText().equals(getString(R.string.start_timer))) {
                    sb.setEnabled(false);
                    trigger.setText(getString(R.string.stop_timer));
                    cdt = new CountDownTimer(sb.getProgress(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Date date = new Date(millisUntilFinished);
                            DateFormat df = new SimpleDateFormat("mm:ss");
                            timer.setText(df.format(date));
                        }

                        @Override
                        public void onFinish() {
                            Date date = new Date(0);
                            DateFormat df = new SimpleDateFormat("mm:ss");
                            timer.setText(df.format(date));
                            am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                            mp.start();

                            Toast.makeText(getApplicationContext(), "Time is up!", Toast.LENGTH_LONG).show();

                            timer.setKeepScreenOn(false);

                        }
                    };

                    timer.setKeepScreenOn(true);
                    cdt.start();

                } else {


                    timer.setKeepScreenOn(false);
                    cdt.cancel();

                    trigger.setText(getString(R.string.start_timer));

                    resetSeekBar();
                }
            }
        });

    }


}
