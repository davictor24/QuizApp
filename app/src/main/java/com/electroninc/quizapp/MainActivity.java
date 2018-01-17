package com.electroninc.quizapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private AppBarLayout mAppBarLayout;
    private ActionBar mActionBar;
    private BottomNavigationView mNavigation;
    private MenuItem mSearch;

    private ArrayList<String> mActionBarTexts;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTags;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_play:
                    switchFragment(0);
                    return true;
                case R.id.navigation_settings:
                    switchFragment(1);
                    return true;
                case R.id.navigation_about:
                    switchFragment(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mAppBarLayout = findViewById(R.id.appbarlayout);

        mNavigation = findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mNavigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade reEntryAnimation = new Fade();
            reEntryAnimation.setDuration(700);
            getWindow().setReenterTransition(reEntryAnimation);
        }

        setupFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        mSearch = menu.getItem(0);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    private void setupFragment() {
        mFragmentManager = getFragmentManager();

        mFragments = new ArrayList<>();
        mFragments.add(new PlayFragment());
        mFragments.add(new SettingsFragment());
        mFragments.add(new AboutFragment());

        mTags = new ArrayList<>();
        mTags.add("play");
        mTags.add("settings");
        mTags.add("about");

        mActionBarTexts = new ArrayList<>();
        mActionBarTexts.add(getString(R.string.title_play));
        mActionBarTexts.add(getString(R.string.title_settings));
        mActionBarTexts.add(getString(R.string.title_about));

        switchFragment(0);
    }

    private void switchFragment(int i) {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, mFragments.get(i), mTags.get(i))
                .commit();
        mActionBar.setTitle(mActionBarTexts.get(i));
        mAppBarLayout.setExpanded(true);
        if (mSearch != null) mSearch.setVisible(i == 0);
        mNavigation.clearAnimation();
        mNavigation.animate().translationY(0).setDuration(200);
    }

}
