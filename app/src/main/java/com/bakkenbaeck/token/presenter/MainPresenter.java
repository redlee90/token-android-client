package com.bakkenbaeck.token.presenter;


import android.support.v4.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.bakkenbaeck.token.R;
import com.bakkenbaeck.token.view.activity.MainActivity;
import com.bakkenbaeck.token.view.adapter.NavigationAdapter;

public class MainPresenter implements Presenter<MainActivity> {

    private MainActivity activity;
    private boolean firstTimeAttached = true;
    private NavigationAdapter adapter;

    private final AHBottomNavigation.OnTabSelectedListener tabListener = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(final int position, final boolean wasSelected) {
            if (wasSelected) {
                return true;
            }

            final AHBottomNavigationViewPager viewPager = activity.getBinding().viewPager;
            viewPager.setCurrentItem(position, false);
            return true;
        }
    };

    @Override
    public void onViewAttached(final MainActivity activity) {
        this.activity = activity;

        if (this.firstTimeAttached) {
            this.firstTimeAttached = false;
            this.adapter = new NavigationAdapter(this.activity.getSupportFragmentManager());
        }
        initNavBar();
    }

    private void initNavBar() {
        final AHBottomNavigation navBar = this.activity.getBinding().navBar;
        final AHBottomNavigationViewPager viewPager = this.activity.getBinding().viewPager;
        final AHBottomNavigationAdapter menuInflater = new AHBottomNavigationAdapter(this.activity, R.menu.navigation);
        menuInflater.setupWithBottomNavigation(navBar);
        navBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        navBar.setAccentColor(ContextCompat.getColor(this.activity, R.color.colorPrimary));
        navBar.setOnTabSelectedListener(this.tabListener);

        viewPager.setAdapter(this.adapter);
    }

    @Override
    public void onViewDetached() {
        this.activity = null;
    }

    @Override
    public void onViewDestroyed() {
        this.activity = null;
    }
}
