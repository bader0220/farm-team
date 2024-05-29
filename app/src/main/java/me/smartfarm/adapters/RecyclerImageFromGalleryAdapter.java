package me.smartfarm.adapters;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import me.smartfarm.R;

public class RecyclerImageFromGalleryAdapter extends RecyclerView.Adapter<RecyclerImageFromGalleryAdapter.ViewHolder> {

    private ArrayList<Uri> uriArrayList;

    public interface OnDeleteClickListener {
        public void onDeleteClickListener(Uri uri);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public RecyclerImageFromGalleryAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View imageView = layoutInflater.inflate(R.layout.item_image_create_new_farm, parent, false);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(uriArrayList.get(position))
                .into(holder.imgSelected);
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgSelected;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSelected = itemView.findViewById(R.id.imgSelected);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClickListener(uriArrayList.get(getAdapterPosition()));
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), getItemCount());
                    }
                }
            });
        }
    }
}
