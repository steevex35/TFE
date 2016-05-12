package com.obisteeves.meetuworld.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.obisteeves.meetuworld.Tabs.TabGuide;
import com.obisteeves.meetuworld.Tabs.TabHome;
import com.obisteeves.meetuworld.Tabs.TabProfil;
import com.obisteeves.meetuworld.Tabs.TabTravel;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch (position){ // if the position is 0 we are returning the First tab
            case 0:
                TabHome tabHome = new TabHome();
                return tabHome;
            case 1:
                TabTravel tabTravel = new TabTravel();
                return tabTravel;
            case 2:
                TabProfil tabProfil = new TabProfil();
                return tabProfil;
            case 3:
                TabGuide tabGuide = new TabGuide();
                return tabGuide;

        }

        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}