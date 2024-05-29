package me.smartfarm.ui.auth.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButtonToggleGroup;


import java.io.IOException;
import java.util.List;

import me.smartfarm.R;
import me.smartfarm.common.Base64Chunker;
import me.smartfarm.common.Constants;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.User;
import me.smartfarm.databinding.FragmentRegisterAddtionalBinding;


public class RegisterAddtionalFragment extends Fragment {
    FragmentRegisterAddtionalBinding binding;
    private static final int PROFILE_IMAGE_REQ_CODE = 100;

    private int userTypeId;
    private int cityId;
    private int neighborhoodId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterAddtionalBinding.inflate(inflater, container, false);
        initCities();
        initImageListener();
        initToggleButton();
        userTypeId = Constants.FARMER_ROLE;
        cityId = -1;
        neighborhoodId = -1;
        return binding.getRoot();
    }

    private void initToggleButton() {
        binding.toggleButton.setSelectionRequired(true);
        binding.btnFarmer.setOnClickListener(v -> {
            userTypeId = Constants.FARMER_ROLE;
        });
        binding.btnTrader.setOnClickListener(v -> {
            userTypeId = Constants.TRADER_ROLE;
        });
    }

    private void initImageListener() {
        binding.imgProfilePicture.setOnClickListener(v ->
                pickProfileImage()
        );
    }

    private void initCities() {

        String[] cities = getResources().getStringArray(R.array.jordan_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, cities);
        binding.etUserCity.setAdapter(adapter);

        binding.etUserCity.setOnItemClickListener((parent, view, position, id) -> {
            // Handle the selected item
            cityId = position;
            binding.etNeighborhoodLayout.setVisibility(View.VISIBLE);
            intiNeighborhood(position);
        });
    }

    public void pickProfileImage() {
        ImagePicker.with(this)
                // Crop Square image
                .cropSquare()
                .setImageProviderInterceptor(imageProvider -> {
                    Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.toString());
                    return null;
                }).setDismissListener(() -> Log.d("ImagePicker", "Dialog Dismiss"))
                // Image resolution will be less than 512 x 512
                .maxResultSize(200, 200)
                .start(PROFILE_IMAGE_REQ_CODE);
    }


    private void intiNeighborhood(int position) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, Util.neighborhood(requireContext(), position));
        binding.etNeighborhood.setAdapter(adapter);
        binding.etNeighborhood.setOnItemClickListener((parent, view, position1, id) -> neighborhoodId = position1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK) {
            // Image Uri will not be null for RESULT_OK

            if (data != null) {
                Uri selectedImageUri = data.getData();
                User.getInstance().setImage(String.valueOf(selectedImageUri));
                ImageView profileImageView = requireView().findViewById(R.id.imgProfilePicture);
                profileImageView.setImageURI(selectedImageUri);
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            // Handle the error
       //     User.getInstance().getImage().clear();
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
         //   User.getInstance().getImage().clear();
            // Task was cancelled
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean validUser() {

        if (userTypeId < 1) {
            Toast.makeText(requireActivity(), getResources().getString(R.string.selecte_user_type), Toast.LENGTH_SHORT).show();
            binding.toggleButton.requestFocus();
            return false;
        }
        if (cityId < 0) {
            binding.etUserCity.setError(getResources().getString(R.string.select_residence_city));
            binding.etUserCity.requestFocus();
            return false;
        }
        if (neighborhoodId < 0) {
            binding.etNeighborhood.setError(getResources().getString(R.string.select_residence_neighborhood));
            binding.etNeighborhood.requestFocus();
            return false;
        }

        User.getInstance().setResidentCityId(cityId);
        User.getInstance().setResidentNeighborhoodId(neighborhoodId);
        User.getInstance().setUserTypeId(userTypeId);
        return true;
    }
}