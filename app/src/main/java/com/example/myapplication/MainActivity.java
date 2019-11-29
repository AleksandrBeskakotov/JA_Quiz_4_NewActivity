package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends LoggingActivity {

    private static final int REQUEST_CODE_CHEAT = 1;

    private static final String KEY_CURRENT_INDEX = "key_current_index";
    private static final String KEY_IS_CHEATER = "key_is_cheater";
    private static final String KEY_ARRAY_ANSWERS = "key_array_answers";
    private static final String NO_ANSWER = "no_answer";
    private static final String CORRECT_ANSWER = "correct_answer";
    private static final String INCORRECT_ANSWER = "incorrect_answer";
    private static final String CHEAT_ANSWER = "cheat_answer";

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    private boolean[] questionIsCheat = new boolean[mQuestionBank.length];

    private String[] answers = new String[mQuestionBank.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int pos = 0; pos < mQuestionBank.length; pos++ ){
            answers[pos] = NO_ANSWER;
        }


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
            questionIsCheat = savedInstanceState.getBooleanArray(KEY_IS_CHEATER);
            answers = savedInstanceState.getStringArray(KEY_ARRAY_ANSWERS);
        }

        final TextView questionString = findViewById(R.id.question_string);
        final Question currentQuestion = mQuestionBank[mCurrentIndex];
        questionString.setText(currentQuestion.getQuestionResId());

        Button trueButton = findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(true);
            }
        });

        Button falseButton = findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(false);
            }
        });

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                final Question currentQuestion = mQuestionBank[mCurrentIndex];
                questionString.setText(currentQuestion.getQuestionResId());

            }
        });

        Button cheatButton = findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Question currentQuestion = mQuestionBank[mCurrentIndex];
                Intent intent =
                        CheatActivity.makeIntent(MainActivity.this, currentQuestion.isCorrectAnswer());
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        Button statsButton = findViewById(R.id.stats_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StatsActivity.newIntent(MainActivity.this, answers);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (resultCode == RESULT_OK && CheatActivity.correctAnswerWasShown(data)) {
                questionIsCheat[mCurrentIndex] = true;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_INDEX, mCurrentIndex);
        outState.putBooleanArray(KEY_IS_CHEATER, questionIsCheat);
        outState.putStringArray(KEY_ARRAY_ANSWERS,answers);
    }

    private void onButtonClicked(boolean answer) {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        int toastMessage;

        if (questionIsCheat[mCurrentIndex]) {
            toastMessage = R.string.judgment_toast;
        } else {
            toastMessage = (currentQuestion.isCorrectAnswer() == answer) ?
                    R.string.correct_toast :
                    R.string.incorrect_toast;
        }

        Toast.makeText(
                MainActivity.this,
                toastMessage,
                Toast.LENGTH_SHORT
        ).show();

        if (currentQuestion.isCorrectAnswer() == answer && questionIsCheat[mCurrentIndex] == false){
            answers[mCurrentIndex] = CORRECT_ANSWER;
        }
        if (currentQuestion.isCorrectAnswer() != answer){
            answers[mCurrentIndex] = INCORRECT_ANSWER;
        }
        if (questionIsCheat[mCurrentIndex] == true){
            answers[mCurrentIndex] = CHEAT_ANSWER;
        }
    }
}
