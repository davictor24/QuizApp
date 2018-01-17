package com.electroninc.quizapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by D A Victor on 17-Jan-18.
 */

public class QuizzesAdapter extends FragmentPagerAdapter {

    private ArrayList<QuestionFragment> mQuestionFragments;

    public QuizzesAdapter(FragmentManager fm, ArrayList<QuestionFragment> questionFragments) {
        super(fm);
        mQuestionFragments = questionFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mQuestionFragments.get(position);
    }

    @Override
    public int getCount() {
        return mQuestionFragments.size();
    }

}
