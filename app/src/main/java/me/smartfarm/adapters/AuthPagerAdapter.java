package me.smartfarm.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import me.smartfarm.ui.auth.fragments.LoginFragment;
import me.smartfarm.ui.auth.fragments.RegisterFragment;

public class AuthPagerAdapter extends FragmentPagerAdapter {
    public AuthPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public AuthPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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
        // Set tab titles (optional)
        switch (position) {
            case 0:
                return "تسجيل الدخول";
            case 1:
                return "انشاء حساب";
            default:
                return null;
        }
    }
}
