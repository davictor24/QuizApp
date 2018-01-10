package com.electroninc.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Fade entryAnimation = new Fade();
            entryAnimation.setDuration(700);
            getWindow().setEnterTransition(entryAnimation);
        }

        String category = getIntent().getStringExtra("category");
        TextView textView = findViewById(R.id.clicked_item);
        textView.setText(category);
    }

}
