package me.smartfarm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;

import de.hdodenhof.circleimageview.CircleImageView;
import me.smartfarm.R;
import me.smartfarm.common.Constants;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.UserRepository;

public class FarmsAdapter extends FirestoreRecyclerAdapter<Farm, FarmsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String farmId);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Farm farm, String farmId);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(String farmId);
    }

    int pageFrom;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteClickListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }


    public FarmsAdapter(@NonNull FirestoreRecyclerOptions<Farm> options, int pageFrom) {
        super(options);
        this.pageFrom = pageFrom;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Farm model) {
        if (pageFrom == Constants.SHOW_FARMER_FARMS) {
            holder.btnContainer.setVisibility(View.VISIBLE);
        } else {
            holder.btnContainer.setVisibility(View.GONE);
        }

        holder.txtCreationDate.setText(Util.timestampToDateTime(model.getCreationDate()));
        holder.txtFarmTitle.setText(model.getTitle());
        holder.txtFarmDescription.setText(model.getDescription());
        holder.txtUnitPrice.setText(Util.formatNum(String.valueOf(model.getUnitPrice())));
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(holder.itemView.getContext(), model.getImages());
        holder.imageSlider.setAdapter(sliderAdapter);
        new UserRepository(holder.itemView.getContext()).getDocumentById(model.getOwnerId(), User.class, user -> {
            holder.txtUserName.setText(user.getFirstName() + " " + user.getLastName());
            if (user.getImage() != null) {
                Glide.with(holder.itemView.getContext())
                        .load(user.getImage())
                        .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                        .into(holder.imgUserImage);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCreationDate;
        TextView txtFarmTitle;
        TextView txtFarmDescription;
        TextView txtUserName;
        TextView txtUnitPrice;
        ViewPager2 imageSlider;
        CircleImageView imgUserImage;
        MaterialCardView container;
        LinearLayout btnContainer;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmDescription = itemView.findViewById(R.id.txtFarmDescription);
            txtFarmTitle = itemView.findViewById(R.id.txtFarmTitle);
            txtCreationDate = itemView.findViewById(R.id.txtCreationDate);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            imageSlider = itemView.findViewById(R.id.imageSlider);
            imgUserImage = itemView.findViewById(R.id.imgUserImage);
            txtUnitPrice = itemView.findViewById(R.id.txtUnitPrice);
            btnContainer = itemView.findViewById(R.id.btnContainer);
            container = itemView.findViewById(R.id.container);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            container.setOnClickListener(v -> {
                if (listener != null) {
                    String id = getSnapshots().getSnapshot(getAdapterPosition()).getId();
                    listener.onItemClick(id);
                }
            });
            btnDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    Farm farm = getSnapshots().getSnapshot(getAdapterPosition()).toObject(Farm.class);
                    deleteClickListener.onDeleteClick(farm, getSnapshots().getSnapshot(getAdapterPosition()).getId());
                }
            });



        }
    }
}
