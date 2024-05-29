package me.smartfarm.ui.main.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.smartfarm.R;
import me.smartfarm.adapters.ReservationsAdapter;
import me.smartfarm.common.Constants;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.models.Reservation;
import me.smartfarm.data.repositories.FarmRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.ReservationsRepository;
import me.smartfarm.databinding.FragmentReservationsBinding;

public class ReservationsFragment extends Fragment {

    ReservationsAdapter reservationsAdapter;
    FragmentReservationsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        initReservations();
        return binding.getRoot();
    }

    private void initReservations() {

        Map<String, String> filter = new HashMap<>();
        if (Util.getCurrentUser().getUserTypeId() == Constants.TRADER_ROLE) {

            filter.put("traderId", Util.getCurrentUser().getId());
        } else {
            filter.put("ownerId", Util.getCurrentUser().getId());
        }

        ReservationsRepository.getInstance(requireActivity()).getAllDocuments(Reservation.class, filter, object -> {
            if (object == null || object.isEmpty()) {
                binding.noReservations.setVisibility(View.VISIBLE);
            } else {
                binding.noReservations.setVisibility(View.GONE);
                reservationsAdapter = new ReservationsAdapter(object);
                binding.recyclerReservations.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerReservations.setAdapter(reservationsAdapter);
            }

        });


    }


}