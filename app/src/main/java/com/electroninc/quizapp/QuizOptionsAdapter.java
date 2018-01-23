package com.electroninc.quizapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D A Victor on 16-Jan-18.
 */

public class QuizOptionsAdapter extends RecyclerView.Adapter<QuizOptionsAdapter.QuizOptionViewHolder> {

    public static final String OPTION_SELECTED_BROADCAST = "com.electroninc.quizapp.OPTION_SELECTED";
    private Context mContext;
    private Activity mActivity;
    private ArrayList<String> mOptions;
    private int mCorrectAnswerIndex;
    private List<QuizOptionsAdapter.QuizOptionViewHolder> mViewHolders;
    private QuizOptionsAdapter.QuizOptionViewHolder mCorrectView;

    public QuizOptionsAdapter(Context context, ArrayList<String> options, int correctAnswerIndex) {
        mContext = context;
        mActivity = (Activity) context;
        mOptions = options;
        mCorrectAnswerIndex = correctAnswerIndex;
        mViewHolders = new ArrayList<>();
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
        if (position == mCorrectAnswerIndex) mCorrectView = holder;
        mViewHolders.add(holder);
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    public List<QuizOptionViewHolder> getViewHolders() {
        return mViewHolders;
    }

    public QuizOptionViewHolder getCorrectView() {
        return mCorrectView;
    }

    public class QuizOptionViewHolder extends RecyclerView.ViewHolder {
        private TextView mOptionTextView;
        private CardView mOptionBackground;

        public QuizOptionViewHolder(View view) {
            super(view);
            mOptionTextView = view.findViewById(R.id.option_text);
            mOptionBackground = view.findViewById(R.id.option_background);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos == mCorrectAnswerIndex) {
                        mOptionBackground.setCardBackgroundColor(mContext.getResources().getColor(R.color.correct_option));
                        ((TextView) mOptionBackground.getChildAt(0)).setTextColor(mContext.getResources().getColor(android.R.color.white));
                        QuizActivity.score++;
                        Toast.makeText(mContext, "Correct", Toast.LENGTH_SHORT).show();
                    } else {
                        mOptionBackground.setCardBackgroundColor(mContext.getResources().getColor(R.color.wrong_option));
                        ((TextView) mOptionBackground.getChildAt(0)).setTextColor(mContext.getResources().getColor(android.R.color.white));
                        mCorrectView.getOptionBackground().setCardBackgroundColor(mContext.getResources().getColor(R.color.correct_option));
                        ((TextView) mCorrectView.getOptionBackground().getChildAt(0)).setTextColor(mContext.getResources().getColor(android.R.color.white));
                        Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
                    }

                    for (QuizOptionViewHolder viewHolder : mViewHolders)
                        viewHolder.getOptionBackground().setOnClickListener(null);

                    Intent intent = new Intent();
                    intent.setAction(OPTION_SELECTED_BROADCAST);
                    mContext.sendBroadcast(intent);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (++QuizActivity.currentPage >= QuestionFragment.numberOfQuestions) {
                                Intent intent = new Intent(mContext, QuizCompletedActivity.class);
                                intent.putExtra(QuizCompletedActivity.EXTRA_NAME, QuizActivity.name);
                                intent.putExtra(QuizCompletedActivity.EXTRA_SCORE, QuizActivity.score);
                                intent.putExtra(QuizCompletedActivity.EXTRA_MAX_SCORE, QuestionFragment.numberOfQuestions);
                                mContext.startActivity(intent);
                                mActivity.finish();
                            } else
                                ((ViewPager) mActivity.findViewById(R.id.questions_view_pager)).setCurrentItem(QuizActivity.currentPage);
                        }
                    }, 1000);
                }
            });
        }

        public TextView getOptionTextView() {
            return mOptionTextView;
        }

        public CardView getOptionBackground() {
            return mOptionBackground;
        }
    }

}
