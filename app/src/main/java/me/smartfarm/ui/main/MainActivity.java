package me.smartfarm.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.smartfarm.R;
import me.smartfarm.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.content_frame);

        // Set up Bottom Navigation View with Navigation Controller
        BottomNavigationView bottomNavigationView = findViewById(R.id.cat_bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//        // Set up Bottom Navigation View with Navigation Controller
//        BottomNavigationView bottomNavigationView = findViewById(R.id.cat_bottom_nav);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }
}