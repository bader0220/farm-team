package me.smartfarm.ui.auth.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.smartfarm.R;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.User;
import me.smartfarm.databinding.FragmentRegisterUserInfoBinding;


public class RegisterUserInfoFragment extends Fragment {
    FragmentRegisterUserInfoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterUserInfoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean validUser() throws Exception {
        String firstName = String.valueOf(binding.etFirstName.getText()).trim();
        String lastName = String.valueOf(binding.etLastName.getText()).trim();
        String password = String.valueOf(binding.etPassword.getText());
        String confirmPassword = String.valueOf(binding.etConfirmPassword.getText());
        String bio = String.valueOf(binding.etUserBio.getText()).trim();
        String email = String.valueOf(binding.etEmail.getText()).trim();
        if (firstName.isEmpty()) {
            binding.etFirstName.setError(getResources().getString(R.string.required));
            binding.etFirstName.requestFocus();
            return false;
        }
        if (lastName.isEmpty()) {
            binding.etLastName.setError(getResources().getString(R.string.required));
            binding.etLastName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            binding.etEmail.setError(getResources().getString(R.string.required));
            binding.etEmail.requestFocus();
            return false;
        }else {
            if (!Util.isValidEmail(email)) {
                binding.etEmail.setError(getResources().getString(R.string.invalid_email));
                binding.etEmail.requestFocus();
                return false;
            }
        }
        if (password.isEmpty()) {
            binding.etPassword.setError(getResources().getString(R.string.required));
            binding.etPassword.requestFocus();
            return false;
        }else {
            if (password.length() < 8) {
                binding.etPassword.setError(getResources().getString(R.string.password_is_too_short));
                binding.etPassword.requestFocus();
                return false;
            }
        }


        if (!confirmPassword.equals(password)) {
            binding.etConfirmPassword.setError(getResources().getString(R.string.password_does_not_match));
            binding.etConfirmPassword.requestFocus();
            return false;
        }

        User.getInstance().setFirstName(firstName);
        User.getInstance().setLastName(lastName);
        User.getInstance().setEmail(email);
        User.getInstance().setBio(bio);
        User.getInstance().setPassword(Util.encrypt(password));
        return true;
    }
}