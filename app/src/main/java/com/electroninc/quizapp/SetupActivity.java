package com.electroninc.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {

    private Spinner mNumberOfQuestionsSpinner;
    private Spinner mTimePerQuestionSpinner;
    private Spinner mDifficultySpinner;
    private String mNumberOfQuestions;
    private String mTimePerQuestion;
    private String mDifficulty;
    private String[] mNumberOfQuestionsValues;
    private String[] mTimePerQuestionValues;
    private String[] mDifficultyValues;
    private EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        String categoryName = getIntent().getStringExtra("category_name");
        TextView categoryTextView = findViewById(R.id.quiz_category);
        categoryTextView.setText(categoryName);

        final String categoryId = String.valueOf(getIntent().getIntExtra("category_id", 0));

        int categoryImage = getIntent().getIntExtra("category_image", 0);
        ImageView categoryImageView = findViewById(R.id.quiz_image);
        categoryImageView.setImageResource(categoryImage);

        Button startQuizButton = findViewById(R.id.btn_start_quiz);
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, QuizActivity.class);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("name", mNameEditText.getText().toString());
                intent.putExtra("number_of_questions", mNumberOfQuestions);
                intent.putExtra("time_per_question", mTimePerQuestion);
                intent.putExtra("difficulty", mDifficulty);

                startActivity(intent);
            }
        });

        mNameEditText = findViewById(R.id.name_edit_text);
        mNumberOfQuestionsSpinner = findViewById(R.id.number_of_questions_spinner);
        mTimePerQuestionSpinner = findViewById(R.id.time_per_question_spinner);
        mDifficultySpinner = findViewById(R.id.difficulty_spinner);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mNumberOfQuestions = sharedPrefs.getString(
                getString(R.string.settings_num_of_questions_key),
                getString(R.string.settings_num_of_questions_default));
        mTimePerQuestion = sharedPrefs.getString(
                getString(R.string.settings_time_per_question_key),
                getString(R.string.settings_time_per_question_default));
        mDifficulty = sharedPrefs.getString(
                getString(R.string.settings_difficulty_key),
                getString(R.string.settings_difficulty_default));

        mNumberOfQuestionsValues = getResources().getStringArray(R.array.settings_num_of_questions_values);
        mTimePerQuestionValues = getResources().getStringArray(R.array.settings_time_per_question_values);
        mDifficultyValues = getResources().getStringArray(R.array.settings_difficulty_values);

        setupNumOfQuestionsSpinner();
        setupTimePerQuestionSpinner();
        setupDifficultySpinner();

        mNumberOfQuestionsSpinner.setSelection(getNumberOfQuestionsIndex(mNumberOfQuestions));
        mTimePerQuestionSpinner.setSelection(getTimePerQuestionIndex(mTimePerQuestion));
        mDifficultySpinner.setSelection(getDifficultyIndex(mDifficulty));
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        mNameEditText.requestFocus();
    }

    private void setupNumOfQuestionsSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter numOfQuestionsSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.settings_num_of_questions_labels, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        numOfQuestionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mNumberOfQuestionsSpinner.setAdapter(numOfQuestionsSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mNumberOfQuestionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mNumberOfQuestions = mNumberOfQuestionsValues[position];
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupTimePerQuestionSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter timePerQuestionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.settings_time_per_question_labels, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        timePerQuestionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTimePerQuestionSpinner.setAdapter(timePerQuestionSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTimePerQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTimePerQuestion = mTimePerQuestionValues[position];
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupDifficultySpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter difficultySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.settings_difficulty_labels, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        difficultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mDifficultySpinner.setAdapter(difficultySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDifficulty = mDifficultyValues[position];
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getNumberOfQuestionsIndex(String value) {
        for (int i = 0; i < mNumberOfQuestionsValues.length; i++) {
            if (value.equals(mNumberOfQuestionsValues[i])) return i;
        }
        return 0;
    }

    private int getTimePerQuestionIndex(String value) {
        for (int i = 0; i < mTimePerQuestionValues.length; i++) {
            if (value.equals(mTimePerQuestionValues[i])) return i;
        }
        return 0;
    }

    private int getDifficultyIndex(String value) {
        for (int i = 0; i < mDifficultyValues.length; i++) {
            if (value.equals(mDifficultyValues[i])) return i;
        }
        return 0;
    }

}
