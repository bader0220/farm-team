package me.smartfarm.ui.main.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import me.smartfarm.R;
import me.smartfarm.common.Base64Chunker;
import me.smartfarm.common.Constants;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.UserRepository;
import me.smartfarm.databinding.FragmentProfileBinding;
import me.smartfarm.ui.auth.AuthActivity;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initFrame();
        initLoginButton();
        initLogoutButton();
        initMyFarmsButton();
        initMyReservationButton();
        initEditProfileButton();
        return binding.getRoot();
    }

    private void initEditProfileButton() {
        binding.btnEditProfile.setOnClickListener(v->{

            NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
            navController.navigate(R.id.action_profile_to_editProfileFragment);
        });
    }

    private void initMyReservationButton() {
        Util.makeTraderView(binding.btnMyReservations);
        binding.btnMyReservations.setOnClickListener(v->{

        });
    }

    private void initMyFarmsButton() {
        Util.makeFarmerView(binding.btnMyFarms);
        binding.btnMyFarms.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("crop_type_id", -1);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
            navController.navigate(R.id.action_profile_to_farmPostFragment, bundle);
        });
    }

    private void initLogoutButton() {
        binding.btnLogout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getResources().getString(R.string.logout))
                    .setMessage(getResources().getString(R.string.logout_confirm))
                    .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        requireActivity().finish();

                    })
                    .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

        });
    }

    private void initLoginButton() {
        binding.btnToAuth.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        });
    }

    private void initFrame() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            binding.frameRequireLogin.setVisibility(View.VISIBLE);
            binding.frameProfile.setVisibility(View.GONE);
        } else {
            binding.frameRequireLogin.setVisibility(View.GONE);
            binding.frameProfile.setVisibility(View.VISIBLE);
            intiUser(Util.getCurrentUser().getId());
        }
    }

    private void intiUser(String userId) {
        UserRepository userRepository = new UserRepository(getActivity());
        userRepository.getDocumentById(userId, User.class, user -> {
            if (user != null) {
                binding.txtUserFullName.setText(Util.getUserFullName(user.getFirstName(), user.getLastName()));
                binding.txtUserBio.setText(user.getBio());
                binding.txtUserEmail.setText(user.getEmail());
                if (user.getUserTypeId() == Constants.FARMER_ROLE){
                    binding.txtAccountType.setText(getResources().getString(R.string.farmer_account));
                }else{
                    binding.txtAccountType.setText(getResources().getString(R.string.trader_account));
                }
                if (user.getImage() != null) {
                    Glide.with(requireContext())
                            .load(user.getImage())
                            .into(binding.imgUserImage);
                }

            }
        });
    }
}