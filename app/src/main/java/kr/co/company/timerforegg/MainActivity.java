package kr.co.company.timerforegg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    
    private long timeCountInMilliSeconds = 1 * 60000;
    
    private enum TimerStatus{
        STARTED,
        STOPPED
    }
    
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStart;
    private CountDownTimer countDownTimer;
    private Button soft;
    private Button hard;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();

        soft.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.soft: /*반숙 버튼 클릭 시 */
                        setTimerValues();
                        break;
                    case R.id.hard: /*완숙 버튼 클릭 시*/
                        setTimerValuesHard();
                        break;
                }
            }
        });
    }

    private void initViews(){
    progressBarCircle = findViewById(R.id.progressBarCircle);
    textViewTime = findViewById(R.id.textViewTime);
    imageViewReset = findViewById(R.id.imageViewReset);
    imageViewStart = findViewById(R.id.imageViewStart);
    soft = findViewById(R.id.soft);
    hard = findViewById(R.id.hard);
    }

    private void initListeners(){
        imageViewReset.setOnClickListener(this);
        imageViewStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStart:
                start();
                break;
        }
    }


    private void reset(){
        stopCountDownTimer();
        startCountDownTimer();
    }

    private void start(){
        if(timerStatus == TimerStatus.STOPPED){
            setTimerValues();
            setProgressBarValues();

            imageViewReset.setVisibility(View.VISIBLE);
            imageViewStart.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);

            timerStatus = TimerStatus.STARTED;

            //startCountDownTimer();
        }else{
            imageViewReset.setVisibility(View.GONE);
            imageViewStart.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);

            timerStatus = TimerStatus.STOPPED;

            stopCountDownTimer();
        }
    }

    private void setTimerValues(){
        timeCountInMilliSeconds = 9 * 60 * 1000;
        startCountDownTimer();
    }

    private void setTimerValuesHard() {
        timeCountInMilliSeconds = 12 * 60 * 1000;
        startCountDownTimer();
    }

    private void startCountDownTimer(){

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int)(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();
                setTimerValues();
                imageViewReset.setVisibility(View.GONE);
                imageViewStart.setVisibility(View.VISIBLE);
                timerStatus = TimerStatus.STOPPED;
            }
        }.start();
        countDownTimer.start();
    }

    private void stopCountDownTimer(){
        countDownTimer.cancel();
    }

    private void setProgressBarValues(){
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int)timeCountInMilliSeconds / 1000);
    }

    private String hmsTimeFormatter(long milliSeconds){
        return String.format("%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(milliSeconds),
        TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
        TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }
    
}


