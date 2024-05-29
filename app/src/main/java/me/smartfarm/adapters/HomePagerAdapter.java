package me.smartfarm.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import me.smartfarm.ui.main.fragments.FarmPostFragment;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomePagerAdapter extends FragmentStateAdapter {

    private final String[] tabTitles;

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity, String[] tabTitles) {
        super(fragmentActivity);
        this.tabTitles = tabTitles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a new fragment instance for each tab
        return FarmPostFragment.newInstance(tabTitles[position], position);
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }
}
