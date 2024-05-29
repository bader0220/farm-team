package me.smartfarm.ui.auth.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.smartfarm.R;
import me.smartfarm.data.repositories.AuthenticationRepository;
import me.smartfarm.databinding.FragmentLoginBinding;
import me.smartfarm.ui.main.MainActivity;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.btnLogin.setOnClickListener(v -> {
            loginUser();
        });

        return binding.getRoot();
    }

    private void loginUser() {
        String email = String.valueOf(binding.etEmail.getText()).trim();
        String password = String.valueOf(binding.etPassword.getText());

        if (email.isEmpty()) {
            binding.etEmail.setError(getResources().getString(R.string.required));
            binding.etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError(getResources().getString(R.string.required));
            binding.etPassword.requestFocus();
            return;
        } else {
            if (password.length() < 8) {
                binding.etPassword.setError(getResources().getString(R.string.password_is_too_short));
                binding.etPassword.requestFocus();
                return;
            }
        }
        AuthenticationRepository authenticationRepository = new AuthenticationRepository(requireActivity());
        authenticationRepository.login(email, password, MainActivity.class);
    }
}