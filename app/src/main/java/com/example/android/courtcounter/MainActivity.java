package com.example.android.courtcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static final int FOUR = 4, SIX = 6, ONE = 1, ZERO = 0;
    //Initialize Global Variables
    int runs = 0, wickets = 0, overs = 0, balls = 0, runsRequired = 0, runRate = 0,
            requiredRunRate = 0;
    Button fourButton, sixButton, oneButton, zeroButton, outButton, wideButton;
    TextView runsCount, wicketsCount, oversCount, runsRequiredCount, runRateCount, requiredRunRateCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Finding buttons and switches
        fourButton = findViewById(R.id.four);
        sixButton = findViewById(R.id.six);
        oneButton = findViewById(R.id.one);
        zeroButton = findViewById(R.id.zero);
        outButton = findViewById(R.id.out);
        wideButton = findViewById(R.id.wide);

        //Finding TextViews
        runsCount = findViewById(R.id.runs);
        wicketsCount = findViewById(R.id.wickets);
        oversCount = findViewById(R.id.overs_count);
        runsRequiredCount = findViewById(R.id.runs_required_count);
        runRateCount = findViewById(R.id.run_rate_count);
        requiredRunRateCount = findViewById(R.id.required_rr_count);


        //Setting onClickListener on the Buttons for Runs
        fourButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + FOUR;
                addOneBall();
                updateDisplay();
            }
        });
        sixButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + SIX;
                addOneBall();
                updateDisplay();
            }
        });
        oneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + ONE;
                addOneBall();
                updateDisplay();
            }
        });
        zeroButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + ZERO;
                addOneBall();
                updateDisplay();
            }
        });

        //Setting onClickListener on the Buttons for Wickets and No Ball/ Wide Ball
        outButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                wickets = wickets + 1;
                addOneBall();
                updateDisplay();
            }
        });
        wideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + ONE;
                updateDisplay();
            }
        });
    }

    //Method to Add one ball
    private void addOneBall() {
        balls++;
        if (balls == 6) {
            balls = 0;
            overs++;
        }
    }

    //Method to refresh Display
    private void updateDisplay() {
        runsCount.setText(String.valueOf(runs));
        wicketsCount.setText(String.valueOf(wickets));
        oversCount.setText(String.format(getResources().
                getString(R.string.ball_count), overs, balls)); //This is new - using placeholders instead of concatenating
        runsRequiredCount.setText(String.valueOf(runsRequired));
        runRateCount.setText(String.valueOf(runsRequired));
        requiredRunRateCount.setText(String.valueOf(requiredRunRate));
    }
}
