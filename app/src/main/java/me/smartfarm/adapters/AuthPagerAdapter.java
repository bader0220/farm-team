package me.smartfarm.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import me.smartfarm.R;
import me.smartfarm.ui.auth.fragments.LoginFragment;
import me.smartfarm.ui.auth.fragments.RegisterFragment;
import me.smartfarm.ui.auth.fragments.RegisterUserInfoFragment;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AuthPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public AuthPagerAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new RegisterFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2; // Number of tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Set tab titles from resources
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_login);
            case 1:
                return mContext.getString(R.string.tab_register);
            default:
                return null;
        }
    }
}