package me.smartfarm.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import me.smartfarm.R;
import me.smartfarm.adapters.AuthPagerAdapter;
import me.smartfarm.databinding.ActivityAuthBinding;
import me.smartfarm.ui.main.MainActivity;

public class AuthActivity extends AppCompatActivity {

    ActivityAuthBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthPagerAdapter adapter = new AuthPagerAdapter(getSupportFragmentManager() , AuthActivity.this);
        binding.viewPager.setAdapter(adapter);

        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }


    private void showToast(String message) {
        Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}