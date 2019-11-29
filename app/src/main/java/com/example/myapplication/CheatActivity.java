package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends LoggingActivity {

    private static final String KEY_CORRECT_ANSWER = "key_correct_answer";
    private static final String KEY_CORRECT_ANSWER_WAS_SHOWN = "key_correct_answer_was_shown";

    private boolean answerWasShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null && savedInstanceState.getBoolean(KEY_CORRECT_ANSWER_WAS_SHOWN) == true) {
            answerWasShown = true;
            showCorrectAnswer();
        }

        Button showCorrectAnswerButton = findViewById(R.id.show_answer_button);
        showCorrectAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerWasShown = true;
                showCorrectAnswer();
            }
        });
    }

    private void showCorrectAnswer() {
        Intent intent = getIntent();
        boolean correctAnswer = intent.getBooleanExtra(KEY_CORRECT_ANSWER, false);

        TextView correctAnswerView = findViewById(R.id.correct_answer);
        correctAnswerView.setText(String.valueOf(correctAnswer));

        setResult(RESULT_OK, makeAnswerShownIntent());
    }

    private static Intent makeAnswerShownIntent() {
        Intent intent = new Intent();
        intent.putExtra(KEY_CORRECT_ANSWER_WAS_SHOWN, true);
        return intent;
    }

    public static Intent makeIntent(Context context, boolean correctAnswer) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(KEY_CORRECT_ANSWER, correctAnswer);
        return intent;
    }

    public static boolean correctAnswerWasShown(Intent intent) {
        return intent != null &&
                intent.getBooleanExtra(KEY_CORRECT_ANSWER_WAS_SHOWN, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CORRECT_ANSWER_WAS_SHOWN, answerWasShown);
    }
}

