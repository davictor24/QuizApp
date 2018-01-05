package com.electroninc.quizapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String FRAGMENT_PLAY_TAG = "play";
    private final String FRAGMENT_SETTINGS_TAG = "settings";
    private final String FRAGMENT_ABOUT_TAG = "help";
    private FragmentManager mFragmentManager;
    private ActionBar mActionBar;
    private String mPlayString;
    private String mSettingsString;
    private String mAboutString;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_play:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, new PlayFragment(), FRAGMENT_PLAY_TAG)
                            .commit();
                    mActionBar.setTitle(mPlayString);
                    return true;
                case R.id.navigation_settings:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, new SettingsFragment(), FRAGMENT_SETTINGS_TAG)
                            .commit();
                    mActionBar.setTitle(mSettingsString);
                    return true;
                case R.id.navigation_about:
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, new AboutFragment(), FRAGMENT_ABOUT_TAG)
                            .commit();
                    mActionBar.setTitle(mAboutString);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        mActionBar = getSupportActionBar();

        mPlayString = getString(R.string.title_play);
        mSettingsString = getString(R.string.title_settings);
        mAboutString = getString(R.string.title_about);

        if (mFragmentManager.findFragmentByTag(FRAGMENT_PLAY_TAG) == null
                && mFragmentManager.findFragmentByTag(FRAGMENT_SETTINGS_TAG) == null
                && mFragmentManager.findFragmentByTag(FRAGMENT_ABOUT_TAG) == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.container, new PlayFragment(), FRAGMENT_PLAY_TAG)
                    .commit();
            mActionBar.setTitle(mPlayString);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
