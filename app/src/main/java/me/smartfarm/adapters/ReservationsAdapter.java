package me.smartfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.smartfarm.R;
import me.smartfarm.common.Base64Chunker;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.models.Reservation;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.FarmRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.UserRepository;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {


    List<Reservation> reservations ;

    public ReservationsAdapter(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    @NonNull
    @Override
    public ReservationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Reservation reservation = reservations.get(position);
        viewHolder.txtCreateDate.setText(Util.timestampToDateTime(reservation.getCreationDate()));
        Context context = viewHolder.itemView.getContext();
        String originalAmount = String.valueOf(reservation.getAmount());

        if (Util.getLocale(context).getLanguage().equals(new Locale("ar").getLanguage())) {
            originalAmount = Util.convertEnglishToArabicNumerals(Util.formatNum(originalAmount));
        }

        viewHolder.txtAmount.setText(Util.formatNum(originalAmount));

        FarmRepository.getInstance(context).getDocumentById(reservation.getFarmId(), Farm.class, new OnLoadingComplete<Farm>() {
            @Override
            public void onLoaded(Farm farm) {
                viewHolder.txtFarmTitle.setText(farm.getTitle());
            }
        });
        UserRepository userRepository = new UserRepository(context);
        userRepository.getDocumentById(reservation.getTraderId(), User.class, user ->
        {
            viewHolder.txtTraderName.setText(Util.getUserFullName(user.getFirstName(), user.getLastName()));
            if (user.getImage() != null) {
                Glide.with(viewHolder.itemView.getContext())
                        .load(user.getImage())
                        .into(viewHolder.imgTrader);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCreateDate;
        TextView txtTraderName;
        TextView txtAmount;
        TextView txtFarmTitle;
        CircleImageView imgTrader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCreateDate = itemView.findViewById(R.id.txtCreateDate);
            txtTraderName = itemView.findViewById(R.id.txtTraderName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtFarmTitle = itemView.findViewById(R.id.txtFarmTitle);
            imgTrader = itemView.findViewById(R.id.imgTrader);
        }
    }
}
