package com.electroninc.quizapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Question>> {

    private static final int LOADER_ID = 0;
    private static final String BASE_URI = "https://opentdb.com/api.php";
    private String mRequestUri;
    private LinearLayout mQuizLoading;
    private LinearLayout mEmptyStateView;
    private ViewPager mQuestionsViewPager;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkStateChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuizLoading = findViewById(R.id.quiz_loading);
        mEmptyStateView = findViewById(R.id.empty_state_view);
        mQuestionsViewPager = findViewById(R.id.questions_view_pager);

//        String name = getIntent().getStringExtra("name");
//        String timePerQuestions = getIntent().getStringExtra("time_per_questions");

        String categoryId = getIntent().getStringExtra("category_id");
        String numberOfQuestions = getIntent().getStringExtra("number_of_questions");
        String difficulty = getIntent().getStringExtra("difficulty");

        Uri baseUri = Uri.parse(BASE_URI);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("category", categoryId);
        uriBuilder.appendQueryParameter("amount", numberOfQuestions);
        uriBuilder.appendQueryParameter("difficulty", difficulty);

        mRequestUri = uriBuilder.toString();

        Toast.makeText(this, mRequestUri, Toast.LENGTH_LONG).show();

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        networkStateChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public Loader<List<Question>> onCreateLoader(int id, Bundle args) {
        return new QuestionsLoader(this, mRequestUri);
    }

    @Override
    public void onLoadFinished(Loader<List<Question>> loader, List<Question> data) {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException ignored) {
        }

        mQuizLoading.setVisibility(View.GONE);
        mEmptyStateView.setVisibility(View.GONE);
        mQuestionsViewPager.setVisibility(View.VISIBLE);

        ArrayList<QuestionFragment> questionFragments = new ArrayList<>();
        for (Question datum : data)
            questionFragments.add(QuestionFragment.newInstance(datum));

        QuizzesAdapter quizzesAdapter = new QuizzesAdapter(getSupportFragmentManager(), questionFragments);
        mQuestionsViewPager.setAdapter(quizzesAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Question>> loader) {
        finish();
    }

    private void networkStateChanged() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (!isConnected) {
            mQuizLoading.setVisibility(View.GONE);
            mEmptyStateView.setVisibility(View.VISIBLE);
        } else {
            mQuizLoading.setVisibility(View.VISIBLE);
            mEmptyStateView.setVisibility(View.GONE);
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

}
