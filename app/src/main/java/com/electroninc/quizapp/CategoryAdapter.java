package com.electroninc.quizapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
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
                    Category category = mCategories.get(pos);
                    Intent intent = new Intent(mContext, SetupActivity.class);
                    intent.putExtra("category_name", category.getCategoryName());
                    intent.putExtra("category_id", category.getCategoryId());
                    intent.putExtra("category_image", category.getImageResourceId());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair<View, String> p1 = Pair.create(view.findViewById(R.id.category_image), "category_image");
                        Pair<View, String> p2 = Pair.create(view.findViewById(R.id.category_name), "category_name");
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(mActivity, p1, p2);
                        mContext.startActivity(intent, options.toBundle());
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
