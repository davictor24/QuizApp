package com.electroninc.quizapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by D A Victor on 16-Jan-18.
 */

public class Question implements Serializable {

    private String mQuestionText;
    private String mCorrectAnswer;
    private ArrayList<String> mIncorrectAnswers;
    private ArrayList<String> mAnswers;
    private int mCorrectAnswerIndex;

    Question(String questionText, String correctAnswer, ArrayList<String> incorectAnswers) {
        mQuestionText = questionText;
        mCorrectAnswer = correctAnswer;
        mIncorrectAnswers = incorectAnswers;

        mCorrectAnswerIndex = (int) (Math.random() * (mIncorrectAnswers.size() + 1));
        mAnswers = (ArrayList<String>) mIncorrectAnswers.clone();
        Collections.shuffle(mAnswers);
        mAnswers.add(mCorrectAnswerIndex, mCorrectAnswer);
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public String getCorrectAnswer() {
        return mCorrectAnswer;
    }

    public ArrayList<String> getIncorectAnswers() {
        return mIncorrectAnswers;
    }

    public ArrayList<String> getAnswers() {
        return mAnswers;
    }

    public int getCorrectAnswerIndex() {
        return mCorrectAnswerIndex;
    }

}
