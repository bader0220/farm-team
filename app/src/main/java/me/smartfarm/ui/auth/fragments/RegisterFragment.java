package me.smartfarm.ui.auth.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.smartfarm.R;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.AuthenticationRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.UserRepository;
import me.smartfarm.databinding.FragmentRegisterBinding;
import me.smartfarm.databinding.FragmentRegisterUserInfoBinding;
import me.smartfarm.ui.auth.AuthActivity;
import me.smartfarm.ui.main.MainActivity;


public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    Fragment registerUserInfoFragment;
    Fragment registerAddtionalFragment;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);


        registerUserInfoFragment = new RegisterUserInfoFragment();
        registerAddtionalFragment = new RegisterAddtionalFragment();
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(registerUserInfoFragment);
        fragmentList.add(registerAddtionalFragment);


        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity(), fragmentList);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtons(position);
            }
        });

        binding.prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.viewPager.getCurrentItem() > 0) {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
                }
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (binding.viewPager.getCurrentItem() < fragmentList.size() - 1) {
                    if (binding.viewPager.getCurrentItem() == 0) {
                        try {
                            if (((RegisterUserInfoFragment) registerUserInfoFragment).validUser()) {
                                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (((RegisterAddtionalFragment) registerAddtionalFragment).validUser()) {
                        try {
                            registerUserToAuth();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });


        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUserToAuth() throws Exception {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getResources().getString(R.string.loading));
        progressDialog.show();
        AuthenticationRepository authenticationRepository = new AuthenticationRepository(requireActivity());
        authenticationRepository.signup(User.getInstance().getEmail(), Util.decrypt(User.getInstance().getPassword()), taskResult -> {
            if (taskResult.isSuccessful()) {
                User.getInstance().setId(Objects.requireNonNull(taskResult.getResult().getUser()).getUid());
                registerUserToDB();
            } else {
                progressDialog.dismiss();
            }
        });
    }

    private void registerUserToDB() {
        UserRepository userRepository = new UserRepository(requireActivity());
        userRepository.registerUser(object -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
            progressDialog.dismiss();
        });
    }

    private void updateButtons(int position) {
        if (position == 0) {
            binding.prevButton.setVisibility(View.GONE);
        } else {
            binding.prevButton.setVisibility(View.VISIBLE);
        }

        if (position == Objects.requireNonNull(binding.viewPager.getAdapter()).getItemCount() - 1) {
            binding.nextButton.setText("Finish");
        } else {
            binding.nextButton.setText("Next");
        }
    }


    private static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragmentList) {
            super(fragmentActivity);
            this.fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}