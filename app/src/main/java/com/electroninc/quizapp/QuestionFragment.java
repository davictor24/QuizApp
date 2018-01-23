package com.electroninc.quizapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class QuestionFragment extends Fragment {

    private static final String QUESTION_KEY = "question";
    private static final String NUM_OF_QUESTIONS_KEY = "number_of_questions";
    private static final String TIME_PER_QUESTION_KEY = "time_per_question";
    public static int numberOfQuestions;
    private int mTimePerQuestion;
    private int mTimeLeft;
    private QuizOptionsAdapter mQuizOptionsAdapter = null;
    private ProgressBar mProgressBar;
    private TextView mTimeLeftTextView;
    private Handler mHandler;
    private boolean mTimeStopped = false;
    private Runnable mUpdateTime = new Runnable() {
        @Override
        public void run() {
            try {
                if (mTimeLeft >= 0) {
                    mTimeStopped = false;
                    updateDisplay();
                    mTimeLeft--;
                } else {
                    mHandler.removeCallbacks(mUpdateTime);
                    mTimeStopped = true;

                    List<QuizOptionsAdapter.QuizOptionViewHolder> viewHolders = mQuizOptionsAdapter.getViewHolders();
                    for (QuizOptionsAdapter.QuizOptionViewHolder viewHolder : viewHolders) {
                        viewHolder.getOptionBackground().setOnClickListener(null);
                        viewHolder.getOptionBackground().setCardBackgroundColor(getContext().getResources().getColor(R.color.wrong_option));
                        ((TextView) viewHolder.getOptionBackground().getChildAt(0)).setTextColor(getContext().getResources().getColor(android.R.color.white));
                    }
                    mQuizOptionsAdapter.getCorrectView().getOptionBackground().setCardBackgroundColor(getContext().getResources().getColor(R.color.correct_option));

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (++QuizActivity.currentPage >= numberOfQuestions) {
                                Intent intent = new Intent(getActivity(), QuizCompletedActivity.class);
                                intent.putExtra(QuizCompletedActivity.EXTRA_NAME, QuizActivity.name);
                                intent.putExtra(QuizCompletedActivity.EXTRA_SCORE, QuizActivity.score);
                                intent.putExtra(QuizCompletedActivity.EXTRA_MAX_SCORE, numberOfQuestions);
                                startActivity(intent);
                                getActivity().finish();
                            } else
                                ((ViewPager) getActivity().findViewById(R.id.questions_view_pager)).setCurrentItem(QuizActivity.currentPage);
                        }
                    }, 1000);
                }
            } finally {
                if (!mTimeStopped) mHandler.postDelayed(mUpdateTime, 1000);
            }
        }
    };

    private BroadcastReceiver optionSelectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                mHandler.removeCallbacks(mUpdateTime);
            } catch (Exception ignored) {
            }
            mTimeStopped = true;
        }
    };

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(Question question, String numberOfQuestions, String timePerQuestion) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_KEY, question);
        args.putString(NUM_OF_QUESTIONS_KEY, numberOfQuestions);
        args.putString(TIME_PER_QUESTION_KEY, timePerQuestion);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Question question = (Question) getArguments().getSerializable(QUESTION_KEY);
        numberOfQuestions = Integer.parseInt(getArguments().getString(NUM_OF_QUESTIONS_KEY));
        mTimePerQuestion = Integer.parseInt(getArguments().getString(TIME_PER_QUESTION_KEY));
        mTimeLeft = mTimePerQuestion;

        TextView questionTextView = view.findViewById(R.id.question_text_view);
        questionTextView.setText(question != null ? question.getQuestionText() : null);

        RecyclerView recyclerView = view.findViewById(R.id.quiz_options_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (question != null) {
            mQuizOptionsAdapter = new QuizOptionsAdapter(getActivity(),
                    question.getAnswers(),
                    question.getCorrectAnswerIndex());
        }
        recyclerView.setAdapter(mQuizOptionsAdapter);

        mProgressBar = getView().findViewById(R.id.time_left_progress);
        mTimeLeftTextView = getView().findViewById(R.id.time_left);
        updateDisplay();

        getActivity().registerReceiver(optionSelectedReceiver, new IntentFilter(QuizOptionsAdapter.OPTION_SELECTED_BROADCAST));
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            mHandler = new Handler();

            try {
                mUpdateTime.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mHandler.removeCallbacks(mUpdateTime);
        } catch (Exception ignored) {
        }
        mTimeStopped = true;
    }

    @SuppressLint("SetTextI18n")
    private void updateDisplay() {
        mProgressBar.setProgress((mTimeLeft * 100) / mTimePerQuestion);
        mTimeLeftTextView.setText("00:" + ((mTimeLeft < 10) ? "0" : "") + String.valueOf(mTimeLeft));
    }

}
