package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    private static final String KEY_STATS_ARRAY = "key_stats_array";
    private static final String CORRECT_ANSWER = "correct_answer";
    private static final String INCORRECT_ANSWER = "incorrect_answer";

    private TextView mStatsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        String[] answers = getIntent().getStringArrayExtra(KEY_STATS_ARRAY);

        int numberOfAnswers = 0;
        int numberOfCorrectAnswers = 0;
        int numberOfQuestions = answers.length;

        for (int pos = 0; pos < answers.length; pos++){
            if (answers[pos].equals(CORRECT_ANSWER) || answers[pos].equals(INCORRECT_ANSWER) ){
                numberOfAnswers++;
            }
            if (answers[pos].equals(CORRECT_ANSWER) ){
                numberOfCorrectAnswers++;
            }
        }

        mStatsTextView = (TextView) findViewById(R.id.stats_info);

        mStatsTextView.setText(String.format("Answered %d/%d questions\n " +
                "Correct answers: %d", numberOfAnswers, numberOfQuestions, numberOfCorrectAnswers));

    }

    public static Intent newIntent(Context packageContext, String[] statsArray){
        Intent intent = new Intent(packageContext, StatsActivity.class);
        intent.putExtra(KEY_STATS_ARRAY, statsArray);
        return intent;
    }
}
