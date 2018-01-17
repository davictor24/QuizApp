package com.electroninc.quizapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuestionFragment extends Fragment {

    public static final String QUESTION_KEY = "question";

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(Question question) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_KEY, question);
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

        TextView questionTextView = view.findViewById(R.id.question_text_view);
        questionTextView.setText(question.getQuestionText());

        RecyclerView recyclerView = view.findViewById(R.id.quiz_options_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        QuizOptionsAdapter quizOptionsAdapter = new QuizOptionsAdapter(question.getAnswers());
        recyclerView.setAdapter(quizOptionsAdapter);
    }

}
