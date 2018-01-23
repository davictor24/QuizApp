package com.electroninc.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizCompletedActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "your_name";
    public static final String EXTRA_SCORE = "quiz_score";
    public static final String EXTRA_MAX_SCORE = "quiz_max_score";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_completed);

        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);
        int score = intent.getIntExtra(EXTRA_SCORE, 0);
        int maxScore = intent.getIntExtra(EXTRA_MAX_SCORE, 20);
        int percentage = score * 100 / maxScore;

        TextView nameTextView = findViewById(R.id.your_name);
         if (name.equals("")) nameTextView.setVisibility(View.GONE);
         else nameTextView.setText(name);

        TextView wellDoneTextView = findViewById(R.id.well_done);
        wellDoneTextView.setText((percentage >= 80) ? getString(R.string.well_done) : getString(R.string.do_better));

        TextView scoreTextView = findViewById(R.id.final_score);
        scoreTextView.setText(score + "/" + maxScore + " (" + percentage + "%)");

        Button btnPlayAgain = findViewById(R.id.btn_play_again);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizCompletedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
