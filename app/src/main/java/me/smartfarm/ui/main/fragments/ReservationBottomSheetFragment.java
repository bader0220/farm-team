package me.smartfarm.ui.main.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.smartfarm.R;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.models.Reservation;
import me.smartfarm.data.repositories.FarmRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.ReservationsRepository;
import me.smartfarm.databinding.FragmentReservationBottomSheetBinding;


public class ReservationBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentReservationBottomSheetBinding binding;
    private final String farmId;

    private Farm farm;

    public ReservationBottomSheetFragment() {
        this("");
    }

    public ReservationBottomSheetFragment(String farmId) {
        this.farmId = farmId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReservationBottomSheetBinding.inflate(inflater, container, false);
        intPaymentData();
        initConfirmOrderButton();
        return binding.getRoot();
    }

    private void initConfirmOrderButton() {
        binding.btnConfirmOrder.setOnClickListener(v -> {
            prepareRegistration();
        });
    }

    private void prepareRegistration() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        double reservationAmount = binding.sliderAmount.getValue();
        double reservationPrice = farm.getUnitPrice() * reservationAmount;
        String traderId = Util.getCurrentUser().getId();
        String ownerId = farm.getOwnerId();

        Reservation.getInstance().setAmount(reservationAmount);
        Reservation.getInstance().setFarmId(farmId);
        Reservation.getInstance().setOwnerId(ownerId);
        Reservation.getInstance().setTraderId(traderId);
        Reservation.getInstance().setReservationPrice(reservationPrice);
        Reservation.getInstance().setCreationDate(System.currentTimeMillis());
        ReservationsRepository.getInstance(requireContext()).saveDocument(Reservation.getInstance(), result -> {
            farm.setAvailableAmount(farm.getAvailableAmount() - reservationAmount);
            farm.getReservations().add(Reservation.getInstance());
            FarmRepository.getInstance(requireContext()).updateDocument(farmId, farm, result1 -> {
                dismiss();
                progressDialog.dismiss();
            });
        });
    }

    private void intPaymentData() {
        FarmRepository.getInstance(requireContext()).getDocumentById(farmId, Farm.class, object -> {
            if (object != null) {
                farm = object;

                binding.sliderAmount.setValueTo((float) object.getAvailableAmount());
                initSliderOnSlide();
            }
        });
    }

    private void initSliderOnSlide() {
        binding.sliderAmount.setValue(1);
        binding.txtReservationPrice.setText(Util.formatNum(String.valueOf(farm.getUnitPrice())));
        binding.sliderAmount.addOnChangeListener((slider, value, fromUser) -> {
            double reservationPrice = farm.getUnitPrice() * value;
            binding.txtReservationPrice.setText(Util.formatNum(String.valueOf(reservationPrice)));
        });
    }
}