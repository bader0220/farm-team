package me.smartfarm.ui.main.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.smartfarm.R;
import me.smartfarm.adapters.FarmsAdapter;
import me.smartfarm.common.Constants;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.models.Reservation;
import me.smartfarm.data.repositories.FarmRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.ReservationsRepository;
import me.smartfarm.databinding.FragmentFarmPostBinding;
import me.smartfarm.ui.auth.AuthActivity;


public class FarmPostFragment extends Fragment {

    FragmentFarmPostBinding binding;
    private static final String ARG_TAB_TITLE = "tab_title";
    private static final String ARG_CROP_TYPE_ID = "crop_type_id";
    FarmsAdapter farmsAdapter;

    public static FarmPostFragment newInstance(String tabTitle, int cropTypeId) {
        FarmPostFragment fragment = new FarmPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAB_TITLE, tabTitle);
        args.putInt(ARG_CROP_TYPE_ID, cropTypeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_farm_post, container, false);
//        // Use getArguments().getString(ARG_TAB_TITLE) to get the tab title if needed
//        // Use getArguments().getInt(ARG_CROP_TYPE_ID) to get the crop type ID if needed
        binding = FragmentFarmPostBinding.inflate(inflater, container, false);
        initRecentlyAdded();
        return binding.getRoot();
    }

    private void initRecentlyAdded() {
        assert getArguments() != null;
        int corpTypeId = getArguments().getInt(ARG_CROP_TYPE_ID, 0);
        int pageFrom = 0;
        Query query;
        if (corpTypeId == -1) {
            pageFrom = Constants.SHOW_FARMER_FARMS;
            query = FarmRepository.getInstance(requireActivity()).getCollectionReference().whereEqualTo("ownerId", Util.getCurrentUser().getId());
        } else {
            query = FarmRepository.getInstance(requireActivity()).getCollectionReference()
                    .whereEqualTo("corpTypeId", corpTypeId);
            pageFrom = Constants.SHOW_ALL_FARMS;

        }

        FirestoreRecyclerOptions<Farm> options = new FirestoreRecyclerOptions.Builder<Farm>()
                .setQuery(query, Farm.class)
                .build();

        farmsAdapter = new FarmsAdapter(options, pageFrom);
        initOnDeleteListener();
        farmsAdapter.setOnItemClickListener(farmId -> {
            if (corpTypeId != -1) {
                Bundle bundle = new Bundle();
                bundle.putString("FARM_ID", farmId);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
                navController.navigate(R.id.action_home_to_farmDetailsFragment, bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("FARM_ID", farmId);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
                navController.navigate(R.id.action_farmPostFragment_to_farmDetailsFragment, bundle);
            }

        });
        binding.recyclerFarms.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFarms.setAdapter(farmsAdapter);
        farmsAdapter.startListening();
    }


    private void initOnDeleteListener() {
        farmsAdapter.setOnDeleteClickListener((farm, farmId) -> {
            if (farm.getReservations().isEmpty()) {

                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.delete_confirm))
                        .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                            Map<String, String> filter = new HashMap<>();
                            filter.put("farmId", farmId);
                            ReservationsRepository.getInstance(requireActivity()).getAllDocuments(Reservation.class, filter, object -> {
                                if (object.isEmpty()) {
                                    FarmRepository.getInstance(requireActivity()).deleteDocument(farmId, object1 -> {
                                        farmsAdapter.startListening();
                                    });

                                } else {
                                    new MaterialAlertDialogBuilder(requireContext())
                                            .setTitle(getResources().getString(R.string.cannot_delete))
                                            .setMessage(getResources().getString(R.string.cannot_delete_message))
                                            .create();
                                }
                            });
                        })
                        .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getResources().getString(R.string.cannot_delete))
                        .setMessage(getResources().getString(R.string.cannot_delete_message))
                        .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        farmsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        farmsAdapter.startListening();
    }


}