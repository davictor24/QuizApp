package com.electroninc.quizapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by D A Victor on 05-Jan-18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private ArrayList<Category> mCategories;
    private Activity mActivity;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        mContext = context;
        mCategories = categories;
        mActivity = (Activity) context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = mCategories.get(position);
        holder.getCategoryImage().setImageResource(category.getImageResourceId());
        holder.getCategoryName().setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCategoryImage;
        private TextView mCategoryName;

        public CategoryViewHolder(View view) {
            super(view);
            mCategoryImage = view.findViewById(R.id.category_image);
            mCategoryName = view.findViewById(R.id.category_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(mContext, SetupActivity.class);
                    intent.putExtra("category", mCategories.get(pos).getCategoryName());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle();
                        mContext.startActivity(intent, bundle);
                    } else {
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        public ImageView getCategoryImage() {
            return mCategoryImage;
        }

        public TextView getCategoryName() {
            return mCategoryName;
        }
    }

}
