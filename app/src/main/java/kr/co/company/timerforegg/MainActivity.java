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
    
    private long timeCountInMilliSeconds = 1 * 60000; // 디폴트 시간
    
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

        // 리스너로 반숙이 버튼 작업중

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimerValues();
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
            }
        };
        Button soft = (Button) findViewById(R.id.soft); // 버튼 불러오기
        soft.setOnClickListener(listener); // 메소드 호출하여 48줄에 만든 리스너 집어넣기



        /*soft.setOnClickListener(new Button.OnClickListener(){

            // 반숙, 완숙 버튼 이벤트
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.soft: *//*반숙 버튼 클릭 시 *//*
                        setTimerValues(); // 반숙(9분) 타이머에 세팅
                        break;
                    case R.id.hard: *//*완숙 버튼 클릭 시*//*
                        setTimerValuesHard(); // 완숙(12분) 타이머에 세팅
                        break;
                }
            }
        });*/
    }

    private void initViews(){ // 뷰 초기화
    progressBarCircle = findViewById(R.id.progressBarCircle);
    textViewTime = findViewById(R.id.textViewTime);
    imageViewReset = findViewById(R.id.imageViewReset);
    imageViewStart = findViewById(R.id.imageViewStart);
    soft = findViewById(R.id.soft);
    hard = findViewById(R.id.hard);
    }

    private void initListeners(){ // 리스너 초기화
        imageViewReset.setOnClickListener(this);
        imageViewStart.setOnClickListener(this);
    }

    // 시작, 리셋 버튼 이벤트
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
            //setTimerValues();
            setProgressBarValues();

            imageViewReset.setVisibility(View.VISIBLE);
            imageViewStart.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);

            timerStatus = TimerStatus.STARTED;

            startCountDownTimer();
        }else{
            imageViewReset.setVisibility(View.GONE);
            imageViewStart.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);

            timerStatus = TimerStatus.STOPPED;

            stopCountDownTimer();
        }
    }

    private void setTimerValues(){ // 반숙 타이머에 시간 세팅
        timeCountInMilliSeconds = 9 * 60 * 1000; // 반숙 : 9분
    }

    private void setTimerValuesHard() { // 완숙 타이머에 시간 세팅
        timeCountInMilliSeconds = 12 * 60 * 1000;
    }

    private void startCountDownTimer(){ // 카운트다운 시작 기능

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

    // 카운트다운 정지 및 초기화
    private void stopCountDownTimer(){
        countDownTimer.cancel();
    }

    // 원형 프로그레스 바에 값을 세팅
    private void setProgressBarValues(){
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int)timeCountInMilliSeconds / 1000);
    }

    // 시간 출력 포매팅
    private String hmsTimeFormatter(long milliSeconds){
        return String.format("%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(milliSeconds),
        TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
        TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }
    
}


