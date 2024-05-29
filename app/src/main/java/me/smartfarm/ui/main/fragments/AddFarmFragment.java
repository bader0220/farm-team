package me.smartfarm.ui.main.fragments;

import static me.smartfarm.common.Constants.dateFormat;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gun0912.tedimagepicker.builder.TedImagePicker;
import me.smartfarm.R;
import me.smartfarm.adapters.RecyclerImageFromGalleryAdapter;
import me.smartfarm.common.Base64Chunker;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.models.ImageWrapper;
import me.smartfarm.data.repositories.FarmRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.StorageRepository;
import me.smartfarm.databinding.FragmentAddFarmBinding;

public class AddFarmFragment extends Fragment {
    FragmentAddFarmBinding binding;

    private Set<Uri> selectedImages;

    private int corpTypeId;

    private int cityId;
    private int neighborhoodId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddFarmBinding.inflate(inflater, container, false);
        selectedImages = new HashSet<>();
        corpTypeId = -1;
        cityId = -1;
        neighborhoodId = -1;
        // Inflate the layout for this fragment
        initDateTo();
        initDateFrom();
        intiImagePicker();
        initSaveButton();
        intiCorpsList();
        initCities();
        return binding.getRoot();
    }

    private void intiCorpsList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.crops));
        binding.etCorpType.setAdapter(adapter);
        binding.etCorpType.setOnItemClickListener((parent, view, position, id) -> corpTypeId = position);
    }

    private void initCities() {

        String[] cities = getResources().getStringArray(R.array.jordan_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, cities);
        binding.etCity.setAdapter(adapter);

        binding.etCity.setOnItemClickListener((parent, view, position, id) -> {
            // Handle the selected item
            cityId = position;
            binding.etNeighborhoodLayout.setVisibility(View.VISIBLE);
            intiNeighborhood(position);
        });
    }

    private void intiNeighborhood(int position) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, Util.neighborhood(requireActivity(), position));
        binding.etNeighborhood.setAdapter(adapter);
        binding.etNeighborhood.setOnItemClickListener((parent, view, position1, id) -> neighborhoodId = position1);
    }


    private void initSaveButton() {
        binding.btnSaveFarm.setOnClickListener(v -> {
            v.setEnabled(false);
            saveFarm();
        });

    }

    private void saveFarm() {
        String title = String.valueOf(binding.etFarmTitle.getText()).trim();
        String description = String.valueOf(binding.etFarmDescription.getText()).trim();
        String harvestDateFrom = String.valueOf(binding.etDateFrom.getText()).trim();
        String harvestDateTo = String.valueOf(binding.etDateTo.getText()).trim();
        String amountStr = String.valueOf(binding.etAvailableAmount.getText()).trim();
        String priceStr = String.valueOf(binding.etPrice.getText()).trim();

        if (title.isEmpty()) {
            binding.etFarmTitle.setError(getResources().getString(R.string.required));
            binding.etFarmTitle.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            binding.etFarmDescription.setError(getResources().getString(R.string.required));
            binding.etFarmDescription.requestFocus();
            return;
        }

        if (harvestDateFrom.isEmpty()) {
            binding.etDateFrom.setError(getResources().getString(R.string.required));
            binding.etDateFrom.requestFocus();
            return;
        }

        if (harvestDateTo.isEmpty()) {
            binding.etDateTo.setError(getResources().getString(R.string.required));
            binding.etDateTo.requestFocus();
            return;
        }

        if (amountStr.isEmpty()) {
            binding.etAvailableAmount.setError(getResources().getString(R.string.required));
            binding.etAvailableAmount.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            binding.etPrice.setError(getResources().getString(R.string.required));
            binding.etPrice.requestFocus();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                binding.etAvailableAmount.setError(getResources().getString(R.string.invalid_amount));
                binding.etAvailableAmount.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            binding.etAvailableAmount.setError(getResources().getString(R.string.invalid_number));
            binding.etAvailableAmount.requestFocus();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price <= 0) {
                binding.etPrice.setError(getResources().getString(R.string.invalid_price));
                binding.etPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            binding.etPrice.setError(getResources().getString(R.string.invalid_number));
            binding.etPrice.requestFocus();
            return;
        }

        if (cityId < 0) {
            binding.etCity.setError(getResources().getString(R.string.select_residence_city));
            binding.etCity.requestFocus();
            return;
        }
        if (neighborhoodId < 0) {
            binding.etNeighborhood.setError(getResources().getString(R.string.select_residence_neighborhood));
            binding.etNeighborhood.requestFocus();
            return;
        }
        if (isDateToAfterDateFrom(harvestDateFrom, harvestDateTo)) {
            binding.etDateTo.setError(getResources().getString(R.string.date_to_after_date_from));
            binding.etDateTo.requestFocus();
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setTitle(getResources().getString(R.string.loading));
        progressDialog.show();
        Farm farm = new Farm();

//        for (Uri imageUri : selectedImages) {
//            Bitmap imageBitmap = null;
//            try {
//                imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            String imageBase64 = Util.encodeToBase64(imageBitmap);
//            List<String> imageChunks = Base64Chunker.chunkBase64String(imageBase64);
//            farm.getImages().add(new ImageWrapper(imageChunks));
//        }

        farm.setTitle(title);
        farm.setDescription(description);
        farm.setCorpTypeId(corpTypeId);
        farm.setCityId(cityId);
        farm.setNeighborhoodId(neighborhoodId);
        farm.setHarvestDateFrom(harvestDateFrom);
        farm.setHarvestDateTo(harvestDateTo);
        farm.setAvailableAmount(Double.parseDouble(amountStr));
        farm.setTotalAmount(Double.parseDouble(amountStr));
        farm.setUnitPrice(Double.parseDouble(priceStr));
        farm.setCreationDate(System.currentTimeMillis());
        farm.setOwnerId(Util.getCurrentUser().getId());
        StorageRepository.getInstance("farms").uploadImages(selectedImages, title)
                .addOnSuccessListener(images -> {
                    farm.setImages(images);
                    FarmRepository.getInstance(requireContext()).saveDocument(farm, object -> {
                        progressDialog.dismiss();
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
                        navController.navigate(R.id.action_addFarmFragment_to_home);
                    });
                });

    }


    private boolean isDateToAfterDateFrom(String dateFrom, String dateTo) {
        try {
            Date fromDate = dateFormat.parse(dateFrom);
            Date toDate = dateFormat.parse(dateTo);
            return toDate.after(fromDate);
        } catch (ParseException e) {
            return false;
        }
    }

    private void intiImagePicker() {
        binding.btnPickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedImagePicker.with(requireContext())
                        .startMultiImage(uriList -> {
                            selectedImages.addAll(uriList);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                updateImagesList();
                            }
                        });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private void updateImagesList() {
        RecyclerImageFromGalleryAdapter recyclerImageFromGalleryAdapter = new RecyclerImageFromGalleryAdapter(new ArrayList<>(selectedImages));
        recyclerImageFromGalleryAdapter.setOnDeleteClickListener(uri -> {
            selectedImages.remove(uri);
            updateImagesList();
        });
        binding.recyclerImagesFromGallery.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerImagesFromGallery.setAdapter(recyclerImageFromGalleryAdapter);
    }

    private void initDateTo() {
        binding.etDateTo.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select a Date")
                    .build();

            // Show the date picker
            datePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");

            // Set the selected date to the EditText
            datePicker.addOnPositiveButtonClickListener(selection -> {
                // The selection is in milliseconds
                binding.etDateTo.setText(datePicker.getHeaderText());
            });
        });
    }

    private void initDateFrom() {
        binding.etDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select a Date")
                        .build();

                // Show the date picker
                datePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");

                // Set the selected date to the EditText
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    // The selection is in milliseconds
                    binding.etDateFrom.setText(datePicker.getHeaderText());
                });
            }
        });
    }


}