package com.electroninc.quizapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by D A Victor on 16-Jan-18.
 */

public class QuestionsLoader extends AsyncTaskLoader<List<Question>> {

    private String mUrl;

    public QuestionsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Question> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl.equals("")) {
            return null;
        }

        return QueryUtils.fetchQuestions(getContext(), mUrl);
    }

}
