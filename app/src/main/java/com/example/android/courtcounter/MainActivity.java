package com.example.android.courtcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Initialize Static Variables
    static final int FOUR = 4, SIX = 6, ONE = 1, ZERO = 0, MAX_OVERS = 2, MAX_WICKETS = 3;
    //Initialize Global Variables
    int runs = 0, wickets = 0, overs = 0, balls = 0, runsRequired = 0, runRate = 0,
            requiredRunRate = 0, teamARuns = 0, teamBRuns = 0, teamBWickets = 0, teamAWickets = 0,
            innings = 0;
    Button fourButton, sixButton, oneButton, zeroButton, outButton, wideButton;
    TextView runsCount, wicketsCount, oversCount, runsRequiredCount, runRateCount,
            requiredRunRateCount, battingSide;
    Switch switchKey;

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
        battingSide = findViewById(R.id.batting_side);

        //Switch
        switchKey = findViewById(R.id.switch_key);
        //Setting up switch logic
        //Initial Check
        if (switchKey.isChecked()) {
            battingSide.setText("Team B is Batting");
        } else {
            battingSide.setText("Team A is Batting");
        }
        //Check for state change and perform accordingly
        switchKey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if ((teamARuns > 0 || teamAWickets > 0) && innings == 1) { //Ensure Team A has not played yet
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_a_batted), Toast.LENGTH_LONG).show();
                        return;
                    } else if (innings == 2) { //When the second innings have begun and switch is pressed
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_a_batted), Toast.LENGTH_LONG).show();
                    } else {
                        battingSide.setText("Team B is Batting"); //When score is zero, allow toggle.

                    }
                } else {
                    if ((teamBRuns > 0 || teamBWickets > 0) && innings == 1) { //Ensure Team B has not played yet
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_b_batted), Toast.LENGTH_LONG).show();
                        return;
                    } else if (innings == 2) { //When the second innings have begun and switch is pressed
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_a_batted), Toast.LENGTH_LONG).show();
                    } else {
                        battingSide.setText("Team A is Batting");
                    }
                }
            }
        });


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
        runRateCount.setText(String.valueOf(runRate));
        requiredRunRateCount.setText(String.valueOf(requiredRunRate));

        if (innings == 0 && (runs > 0 || wickets > 0 || balls > 0 || overs > 0)) { //Whether the first innings have begun or not.
            innings = 1; //1 means 1st innings have started
        }
        //Perform Team Score Updation
        scoreCheck();
    }

    //Method that will perform all checks for keeping the scores
    private void scoreCheck() {
        //With the help of if statement, update the correct Team's score.
        if (!switchKey.isChecked()) {
            teamARuns = runs;
            teamAWickets = wickets;
        } else {
            teamBRuns = runs;
            teamBWickets = wickets;
        }
        //Check for change of innings condition
        if ((wickets == MAX_WICKETS || overs == MAX_OVERS) && innings == 1) { //Condition True when game is in 1st innings
            innings = -1; // Transition phase.
            //Reset Counters to Zero
            runs = 0;
            wickets = 0;
            overs = 0;
            balls = 0;
            updateDisplay();

            switchKey.toggle(); //Toggle switch and display toast
            Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.second_innings), Toast.LENGTH_LONG).show();
            innings = 2; //Finally change back the innings counter to 2.
        } else if ((wickets == MAX_WICKETS || overs == MAX_OVERS) && innings == 2) {
            //TODO: Declare who won
        }
    }
}
