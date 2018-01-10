package com.electroninc.quizapp;

/**
 * Created by D A Victor on 05-Jan-18.
 */

public class Category {

    private int mImageResourceId;
    private String mCategoryName;
    private int mCategoryId;

    public Category(int imageResourceId, String categoryName, int categoryId) {
        mImageResourceId = imageResourceId;
        mCategoryName = categoryName;
        mCategoryId = categoryId;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

}
