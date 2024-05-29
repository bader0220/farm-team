package me.smartfarm.ui.main.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import me.smartfarm.R;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.UserRepository;
import me.smartfarm.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {

    FragmentEditProfileBinding binding;

    private int cityId;
    private int neighborhoodId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        initUserInfo();
        intiSaveButton();
        return binding.getRoot();
    }

    private void intiSaveButton() {
        binding.btnSave.setOnClickListener(v -> {
            updateUser();
        });
    }

    private void updateUser() {


        final User userTemp = Util.getCurrentUser();
        String firstName = String.valueOf(binding.etFirstName.getText()).trim();
        String lastName = String.valueOf(binding.etLastName.getText()).trim();
        String bio = String.valueOf(binding.etUserBio.getText()).trim();

        if (!userTemp.getFirstName().equals(firstName)) {
            if (firstName.isEmpty()) {
                binding.etFirstName.setError(getResources().getString(R.string.required));
                binding.etFirstName.requestFocus();
                return;
            }
            userTemp.setFirstName(firstName);
        }
        if (!userTemp.getLastName().equals(lastName)) {
            if (lastName.isEmpty()) {
                binding.etLastName.setError(getResources().getString(R.string.required));
                binding.etLastName.requestFocus();
                return;
            }
            userTemp.setLastName(lastName);
        }
        if (!userTemp.getBio().equals(bio)) {
            userTemp.setBio(bio);
        }
        userTemp.setResidentCityId(cityId);
        userTemp.setResidentNeighborhoodId(neighborhoodId);


        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle(getResources().getString(R.string.loading));
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        UserRepository userRepository = new UserRepository(requireContext());
        userRepository.updateDocument(userTemp.getId(), userTemp, object -> {
            Util.setCurrentUser(userTemp);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
            navController.navigate(R.id.action_editProfileFragment_to_profile);
            progressDialog.dismiss();
        });

    }

    private void initUserInfo() {
        cityId = Util.getCurrentUser().getResidentCityId();
        Glide.with(requireContext()).load(Util.getCurrentUser().getImage()).into(binding.imgUserImage);
        binding.etFirstName.setText(Util.getCurrentUser().getFirstName());
        binding.etLastName.setText(Util.getCurrentUser().getLastName());
        binding.etEmail.setText(Util.getCurrentUser().getEmail());
        binding.etUserBio.setText(Util.getCurrentUser().getBio());
        initCities();
    }

    private void initCities() {
        String[] cities = getResources().getStringArray(R.array.jordan_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, cities);
        binding.etUserCity.setAdapter(adapter);

        int cityId = Util.getCurrentUser().getResidentCityId();
        if (cityId >= 0 && cityId < cities.length) {
            binding.etUserCity.setText(cities[cityId], false);
        }


        int neighborhoodId = Util.getCurrentUser().getResidentNeighborhoodId();
        if (neighborhoodId >= 0 && neighborhoodId < Util.neighborhood(requireContext(), cityId).length) {
            binding.etNeighborhood.setText(Util.neighborhood(requireContext(), cityId)[neighborhoodId], false);
        }
        binding.etUserCity.setOnItemClickListener((parent, view, position, id) -> {
            this.cityId = position;
/*
            binding.etNeighborhoodLayout.setVisibility(View.VISIBLE);
*/
            intiNeighborhood(position);
        });
    }


    private void intiNeighborhood(int position) {
        String[] neighborhoods = Util.neighborhood(requireContext(), position);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, neighborhoods);
        binding.etNeighborhood.setAdapter(adapter);

        int neighborhoodId = Util.getCurrentUser().getResidentNeighborhoodId();
        if (neighborhoodId >= 0 && neighborhoodId < neighborhoods.length) {
            binding.etNeighborhood.setText(neighborhoods[neighborhoodId], false);
        }

        binding.etNeighborhood.setOnItemClickListener((parent, view, position1, id) -> this.neighborhoodId = position1);
    }


}