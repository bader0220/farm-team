package me.smartfarm.ui.main.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.smartfarm.R;
import me.smartfarm.adapters.ImageSliderAdapter;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.FarmRepository;
import me.smartfarm.data.repositories.UserRepository;
import me.smartfarm.databinding.FragmentFarmDetailsBinding;

public class FarmDetailsFragment extends Fragment implements OnMapReadyCallback {
    FragmentFarmDetailsBinding binding;

    Farm farm;
    String farmId;

    public FarmDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFarmDetailsBinding.inflate(inflater, container, false);
        intiFarm();
        intiMakeOrderButton();
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);
        return binding.getRoot();
    }

    private void initChatButton() {
        binding.btnChat.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("receiverId", farm.getOwnerId());
            NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
            navController.navigate(R.id.action_farmDetailsFragment_to_chatFragment, bundle);
        });
    }



    private void intiMakeOrderButton() {
        binding.btnOrder.setOnClickListener(v -> {
            ReservationBottomSheetFragment bottomSheetFragment = new ReservationBottomSheetFragment(farmId);
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    private void intiFarm() {
        binding.loadingFrame.setVisibility(View.VISIBLE);
        binding.mainFrame.setVisibility(View.GONE);
        Bundle arguments = getArguments();
        Util.makeTraderView(binding.btnOrder);
        if (arguments != null) {
            farmId = arguments.getString("FARM_ID");
            FarmRepository.getInstance(requireContext()).getDocumentById(farmId, Farm.class, farm -> {
                this.farm = farm;
                initFarmImages();
                initChatButton();

                if (farm.getAvailableAmount() <= 1) {
                    binding.btnOrder.setText(getResources().getString(R.string.totaly_reserved));
                    binding.btnOrder.setEnabled(false);
                }
                binding.txtFarmName.setText(farm.getTitle());
                binding.txtFarmDescription.setText(farm.getDescription());
                binding.txtCityName.setText(getResources().getStringArray(R.array.jordan_cities)[farm.getCityId()]);
                binding.txtHarvestDateFrom.setText(farm.getHarvestDateFrom());
                binding.txtHarvestDateTo.setText(farm.getHarvestDateTo());
                String originalAmount = String.valueOf(farm.getAvailableAmount());

                Locale currentLocale = getResources().getConfiguration().locale;
                if (currentLocale.getLanguage().equals(new Locale("ar").getLanguage())) {
                    originalAmount = Util.convertEnglishToArabicNumerals(originalAmount);
                }

                binding.txtAvailableAmount.setText(Util.formatNum(originalAmount));
                UserRepository userRepository = new UserRepository(requireContext());
                userRepository.getDocumentById(farm.getOwnerId(), User.class, user -> {
                    binding.loadingFrame.setVisibility(View.GONE);
                    binding.mainFrame.setVisibility(View.VISIBLE);
                    binding.txtUserName.setText(Util.getUserFullName(user.getFirstName(), user.getLastName()));
                    if (user.getImage() != null) {
                        Glide.with(requireContext())
                                .load(Uri.parse(user.getImage()))
                                .into(binding.imgUserImage);
                        binding.imgUserImage.setOnClickListener(v -> {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("USER_ID", user.getId());
//                        Navigation.findNavController(v).navigate(R.id.action_farmDetailsFragment_to_userProfileFragment, bundle);
                        });
                    }

                });
            });
        }

    }

    private void initFarmImages() {
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(requireContext(), farm.getImages());
        binding.imageSlider.setAdapter(sliderAdapter);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Set a marker at a specific location
        LatLng location = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }
}