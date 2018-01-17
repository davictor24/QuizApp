package com.electroninc.quizapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by D A Victor on 16-Jan-18.
 */

public class QuizOptionsAdapter extends RecyclerView.Adapter<QuizOptionsAdapter.QuizOptionViewHolder> {

    private ArrayList<String> mOptions;

    public QuizOptionsAdapter(ArrayList<String> options) {
        mOptions = options;
    }

    @Override
    public QuizOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_option_view, parent, false);
        return new QuizOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizOptionViewHolder holder, int position) {
        String option = mOptions.get(position);
        holder.getOptionTextView().setText(option);
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    public class QuizOptionViewHolder extends RecyclerView.ViewHolder {
        private TextView mOptionTextView;

        public QuizOptionViewHolder(View view) {
            super(view);
            mOptionTextView = view.findViewById(R.id.option_text);
        }

        public TextView getOptionTextView() {
            return mOptionTextView;
        }
    }

}
