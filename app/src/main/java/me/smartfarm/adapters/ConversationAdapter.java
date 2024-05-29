package me.smartfarm.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.smartfarm.R;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Conversation;
import me.smartfarm.data.models.Message;
import me.smartfarm.data.models.Reservation;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.UserRepository;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    String userId = Util.getCurrentUser().getId();
    List<Conversation> conversations;
    String receiverId;

    public ConversationAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> users = conversations.get(position).getUsers();
        userId = Util.getCurrentUser().getId();
        receiverId = users.get(0).equals(userId) ? users.get(1) : users.get(0);
        String conversationId = userId + "_" + receiverId;
        UserRepository userRepository = new UserRepository(holder.itemView.getContext());
        userRepository.getDocumentById(receiverId, User.class, object -> {
            if (object != null) {
                holder.userName.setText(Util.getUserFullName(object.getFirstName(), object.getLastName()));
                Glide.with(holder.itemView.getContext()).load(object.getImage()).into(holder.userImage);
            }
        });
        holder.updateTime.setText(Util.timestampToDateTime(conversations.get(position).getUpdateTime()));
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }


    public interface OnItemClickListener {
        void onItemClick(String receiverId);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private String getUserId(String id) {
        String[] ids = id.split("_");
        return ids[0].equals(Util.getCurrentUser().getId()) ? ids[1] : ids[0];
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView updateTime;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.imgUserImage);
            userName = itemView.findViewById(R.id.txtUserName);
            updateTime = itemView.findViewById(R.id.updateTime);
            container = itemView.findViewById(R.id.container);

            container.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    String receiverId = conversations.get(getAdapterPosition()).getUsers().get(0).equals(userId) ? conversations.get(getAdapterPosition()).getUsers().get(1) : conversations.get(getAdapterPosition()).getUsers().get(0);
                    onItemClickListener.onItemClick(receiverId);
                }
            });
        }
    }


}
