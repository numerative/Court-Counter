package com.example.android.courtcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Initialize Static Variables
    static final int FOUR = 4, SIX = 6, ONE = 1, ZERO = 0, MAX_OVERS = 2, MAX_WICKETS = 3;
    //Initialize Global Variables
    int runs = 0, wickets = 0, overs = 0, totalBalls = 0, ballsInAnOver = 0, runsRequired = 0,
            teamARuns = 0, teamBRuns = 0, teamBWickets = 0, teamAWickets = 0,
            innings = 0, firstInningsRuns = 0, firstInningsWickets = 0;
    double runRate = 0, requiredRunRate = 0;
    Button fourButton, sixButton, oneButton, zeroButton, outButton, wideButton;
    TextView runsCount, wicketsCount, oversCount, runsRequiredLabel, runsRequiredCount,
            runRateCount, requiredRunRateLabel, requiredRunRateCount, battingSide, results,
            scoreCardBattingTeam, innings1Label, innings1Runs, innings1Wickets;
    View innings1Score;
    Switch switchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing all views
        findAllViews();
        //Setting up switch logic
        //Initial Check
        if (switchKey.isChecked()) {
            battingSide.setText(getResources().getString(R.string.team_is_batting, getResources().getString(R.string.team_b)));
            scoreCardBattingTeam.setText(getResources().getString(R.string.team_b));

        } else {
            battingSide.setText(getResources().getString(R.string.team_is_batting, getResources().getString(R.string.team_a)));
            scoreCardBattingTeam.setText(getResources().getString(R.string.team_a));
        }
        //Check for state change and perform accordingly
        switchKey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if ((teamARuns > 0 || teamAWickets > 0) && innings == 1) { //Ensure Team A has not played yet
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_batted, getResources().getString(R.string.team_a)), Toast.LENGTH_LONG).show();
                    } else if (innings == 2) { //When the second innings have begun and switch is pressed
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_batted, getResources().getString(R.string.team_a)), Toast.LENGTH_LONG).show();
                    } else {
                        battingSide.setText(getResources().getString(R.string.team_is_batting, getResources().getString(R.string.team_b))); //When score is zero, allow toggle.
                        scoreCardBattingTeam.setText(getResources().getString(R.string.team_b));

                    }
                } else {
                    if ((teamBRuns > 0 || teamBWickets > 0) && innings == 1) { //Ensure Team B has not played yet
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_batted, getResources().getString(R.string.team_b)), Toast.LENGTH_LONG).show();
                    } else if (innings == 2) { //When the second innings have begun and switch is pressed
                        switchKey.toggle(); //Prevent user from toggling and display a toast message
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.team_batted, getResources().getString(R.string.team_b)), Toast.LENGTH_LONG).show();
                    } else {
                        battingSide.setText(getResources().getString(R.string.team_is_batting, getResources().getString(R.string.team_a)));
                        scoreCardBattingTeam.setText(getResources().getString(R.string.team_a));
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
                updateStats();
                updateDisplay();
            }
        });
        sixButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + SIX;
                addOneBall();
                updateStats();
                updateDisplay();
            }
        });
        oneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + ONE;
                addOneBall();
                updateStats();
                updateDisplay();
            }
        });
        zeroButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + ZERO;
                addOneBall();
                updateStats();
                updateDisplay();
            }
        });

        //Setting onClickListener on the Buttons for Wickets and No Ball/ Wide Ball
        outButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                wickets = wickets + 1;
                addOneBall();
                updateStats();
                updateDisplay();
            }
        });
        wideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runs = runs + ONE;
                updateStats();
                updateDisplay();
            }
        });
    }

    //Method to Add one ball
    private void addOneBall() {
        totalBalls++;
        ballsInAnOver++;
        if (ballsInAnOver > 5) {
            ballsInAnOver = 0;
        }
        double oversWithDecimal = totalBalls / 6; //Find the number of the over being bowled
        overs = (int) oversWithDecimal; //Casting double to an int will remove anything after the decimal
    }

    //Method to refresh Display
    private void updateDisplay() {
        //Perform Team Score Updation
        scoreCheck();
        runsCount.setText(String.valueOf(runs));
        wicketsCount.setText(String.valueOf(wickets));
        oversCount.setText(String.format(getResources().
                getString(R.string.ball_count), overs, ballsInAnOver)); //This is new - using placeholders instead of concatenating
        runsRequiredCount.setText(String.valueOf(runsRequired));
        runRateCount.setText(String.valueOf(String.format(Locale.ENGLISH, "%.2f", runRate)));
        requiredRunRateCount.setText(String.valueOf(String.format(Locale.ENGLISH, "%.2f", requiredRunRate)));
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
        if ((wickets == MAX_WICKETS || overs == MAX_OVERS) && innings == 1) { //When innings change
            innings = -1; // Transition phase.
            //Store 1st Innings Scorecard
            firstInningsRuns = runs;
            firstInningsWickets = wickets;
            //Reset Counters to Zero
            runs = 0;
            wickets = 0;
            overs = 0;
            totalBalls = 0;
            ballsInAnOver = 0;
            runRate = 0;

            //Display hidden views in second innings
            runsRequiredLabel.setVisibility(View.VISIBLE);
            runsRequiredCount.setVisibility(View.VISIBLE);
            requiredRunRateLabel.setVisibility(View.VISIBLE);
            requiredRunRateCount.setVisibility(View.VISIBLE);
            innings1Score.setVisibility(View.VISIBLE);

            //Displaying Innings 1 score
            if (!switchKey.isChecked()) {
                innings1Label.setText(getResources().getString(R.string.innings_1_label,
                        getResources().getString(R.string.team_a)));
                innings1Runs.setText(String.valueOf(teamARuns));
                innings1Wickets.setText(String.valueOf(teamAWickets));
            } else {
                innings1Label.setText(getResources().getString(R.string.innings_1_label,
                        getResources().getString(R.string.team_b)));
                innings1Runs.setText(String.valueOf(teamBRuns));
                innings1Wickets.setText(String.valueOf(teamBWickets));
            }

            switchKey.toggle(); //Toggle switch and display toast
            Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.second_innings), Toast.LENGTH_LONG).show();
            innings = 2; //Finally change back the innings counter to 2.
            updateStats();
            updateDisplay();
        } else if ((wickets == MAX_WICKETS || overs == MAX_OVERS || runs > firstInningsRuns) && innings == 2) { //Finally, who won?
            innings = 3; //Signifies match has ended.
            if (teamARuns > teamBRuns) { //When Team A wins
                results.setText(getResources().getString(R.string.team_has_won, getResources().getString(R.string.team_a)));
            } else if (teamBRuns > teamARuns) { //When Team B wins
                results.setText(getResources().getString(R.string.team_has_won, getResources().getString(R.string.team_b)));
            } else {
                results.setText(getResources().getString(R.string.draw));
            }
            //Disabling all buttons
            fourButton.setEnabled(false);
            sixButton.setEnabled(false);
            oneButton.setEnabled(false);
            zeroButton.setEnabled(false);
            outButton.setEnabled(false);
            wideButton.setEnabled(false);
        }
    }

    //Update Stats
    public void updateStats() {
        if (innings == 0 && (runs > 0 || wickets > 0 || totalBalls > 0 || overs > 0)) { //Whether the first innings have begun or not.
            innings = 1; //1 means 1st innings have started
        }
        if (runs > 0 && (innings == 1 || innings == 2)) { //Update Runrate for 1st and 2nd innings, but not when innings change.
            runRate = (6.0 * runs / totalBalls); //Formula for Run Rate
        }
        if (innings == 2) { //If in second innings, calculate RRR and Runs Required
            //Runs Required to Win
            runsRequired = firstInningsRuns + 1 - runs;
            if (runsRequired < 0) { //When second team scores more than first team
                runsRequired = 0;
            }
            //Calculating Required Run Rate
            int ballsRemaining = (MAX_OVERS * 6) - totalBalls; //Calculate remaining totalBalls in an over
            if (ballsRemaining > 0) {
                requiredRunRate = 6.0 * runsRequired / ballsRemaining; // Formula for Required Run Rate
            }
        }
    }

    //Handling the screen rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) { //Save all variables to outState
        super.onSaveInstanceState(outState);
        outState.putInt("runs", runs);
        outState.putInt("wickets", wickets);
        outState.putInt("overs", overs);
        outState.putInt("totalBalls", totalBalls);
        outState.putInt("ballsInAnOver", ballsInAnOver);
        outState.putInt("runsRequired", runsRequired);
        outState.putInt("teamARuns", teamARuns);
        outState.putInt("teamBRuns", teamBRuns);
        outState.putInt("teamAWickets", teamAWickets);
        outState.putInt("teamBWickets", teamBWickets);
        outState.putInt("innings", innings);
        outState.putInt("firstInningsRuns", firstInningsRuns);
        outState.putInt("firstInningsWickets", firstInningsWickets);
        outState.putDouble("runRate", runRate);
        outState.putDouble("requiredRunRate", requiredRunRate);
        outState.putBoolean("switchState", switchKey.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { //Restore all variables
        super.onRestoreInstanceState(savedInstanceState);
        runs = savedInstanceState.getInt("runs");
        wickets = savedInstanceState.getInt("wickets");
        overs = savedInstanceState.getInt("overs");
        totalBalls = savedInstanceState.getInt("totalBalls");
        ballsInAnOver = savedInstanceState.getInt("ballsInAnOver");
        runsRequired = savedInstanceState.getInt("runsRequired");
        teamARuns = savedInstanceState.getInt("teamARuns");
        teamBRuns = savedInstanceState.getInt("teamBRuns");
        teamAWickets = savedInstanceState.getInt("teamAWickets");
        teamBWickets = savedInstanceState.getInt("teamBWickets");
        innings = savedInstanceState.getInt("innings");
        firstInningsRuns = savedInstanceState.getInt("firstInningsRuns");
        firstInningsWickets = savedInstanceState.getInt("firstInningsWickets");
        runRate = savedInstanceState.getDouble("runRate");
        requiredRunRate = savedInstanceState.getDouble("requiredRunRate");
        switchKey.setChecked(savedInstanceState.getBoolean("switchState"));
        updateDisplay(); //Update display once variables are restored
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Hide views only when game is in first innings.
        if (innings == 1 || innings == 0) {
            //Hide the runs required and run rate required fields for the first innings
            runsRequiredLabel.setVisibility(View.INVISIBLE);
            runsRequiredCount.setVisibility(View.INVISIBLE);
            requiredRunRateLabel.setVisibility(View.INVISIBLE);
            requiredRunRateCount.setVisibility(View.INVISIBLE);
            //Innings 1 score LinearLayout
            innings1Score.setVisibility(View.INVISIBLE);
        }

        //Displaying Innings 1 score
        if (switchKey.isChecked()) {
            innings1Label.setText(getResources().getString(R.string.innings_1_label,
                    getResources().getString(R.string.team_a)));
            innings1Runs.setText(String.valueOf(teamARuns));
            innings1Wickets.setText(String.valueOf(teamAWickets));
        } else {
            innings1Label.setText(getResources().getString(R.string.innings_1_label,
                    getResources().getString(R.string.team_b)));
            innings1Runs.setText(String.valueOf(teamBRuns));
            innings1Wickets.setText(String.valueOf(teamBWickets));
        }
    }

    //Inflating options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset_score, menu);
        return true;
    }

    //How menu options clicks are handled
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.reset_scores:
                //Reset the Scores
                runs = 0;
                wickets = 0;
                overs = 0;
                totalBalls = 0;
                ballsInAnOver = 0;
                runsRequired = 0;
                teamARuns = 0;
                teamBRuns = 0;
                teamAWickets = 0;
                teamBWickets = 0;
                innings = 0;
                firstInningsRuns = 0;
                firstInningsWickets = 0;
                runRate = 0;
                requiredRunRate = 0;

                //Hide the runs required and run rate required fields for the first innings
                runsRequiredLabel.setVisibility(View.INVISIBLE);
                runsRequiredCount.setVisibility(View.INVISIBLE);
                requiredRunRateLabel.setVisibility(View.INVISIBLE);
                requiredRunRateCount.setVisibility(View.INVISIBLE);
                innings1Score.setVisibility(View.INVISIBLE); //Innings 1 LinearLayout

                updateStats(); // Update the Stats
                updateDisplay(); // Update the screen

                //Enabling back all buttons
                fourButton.setEnabled(true);
                sixButton.setEnabled(true);
                oneButton.setEnabled(true);
                zeroButton.setEnabled(true);
                outButton.setEnabled(true);
                wideButton.setEnabled(true);
                //Remove results
                results.setText("");
        }
        //Return the selected item
        return super.onOptionsItemSelected(menuItem);
    }

    private void findAllViews() {
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
        runsRequiredLabel = findViewById(R.id.runs_required_label);
        runsRequiredCount = findViewById(R.id.runs_required_count);
        runRateCount = findViewById(R.id.run_rate_count);
        requiredRunRateLabel = findViewById(R.id.required_rr_label);
        requiredRunRateCount = findViewById(R.id.required_rr_count);
        battingSide = findViewById(R.id.batting_side);
        results = findViewById(R.id.results);
        scoreCardBattingTeam = findViewById(R.id.score_card_batting_team_name);
        innings1Score = findViewById(R.id.innings_1_score); //LinearLayout
        innings1Label = findViewById(R.id.innings_1_label);
        innings1Runs = findViewById(R.id.innings_1_runs);
        innings1Wickets = findViewById(R.id.innings_1_wickets);

        //Switch
        switchKey = findViewById(R.id.switch_key);
    }
}
