package com.example.danil.androidservice;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class FirstActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    final int TASK_CODE = 1;

    public final static int STATUS_START = 100;
    public final static int STATUS_INTERMEDIATE = 150;
    public final static int STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    private TextView textViewInfo;
    private TextView textViewResult;
    private TextView textViewTime;
    private Button btnOk;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewInfo = (TextView) findViewById(R.id.textViewInfo);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        btnOk = (Button) findViewById(R.id.buttonStart);
        btnOk.setOnClickListener(onClickListener);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startService();
        }
    };

    public void startService() {
        Intent intent = new Intent(this, FirstService.class);
        PendingIntent pi = createPendingResult(TASK_CODE, intent, 0);
        intent.putExtra(PARAM_TIME, Integer.parseInt(textViewTime.getText().toString()));
        intent.putExtra(PARAM_PINTENT, pi);
        startService(intent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textViewTime.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == STATUS_START) {
            switch (requestCode) {
                case TASK_CODE:
                    textViewInfo.setText("Timer started...");
                    break;
            }
        }

        if (resultCode == STATUS_INTERMEDIATE) {
            int result = data.getIntExtra(PARAM_RESULT, 0);
            switch (requestCode) {
                case TASK_CODE:
                    textViewResult.setText("Time elapsed: " + result + " s");
                    break;
            }
        }

        if (resultCode == STATUS_FINISH) {
            int result = data.getIntExtra(PARAM_RESULT, 0);
            switch (requestCode) {
                case TASK_CODE:
                    textViewInfo.setText("Timer finish");
                    break;
            }
        }
    }
}