package me.smartfarm.ui.main.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.smartfarm.R;
import me.smartfarm.adapters.MessageAdapter;
import me.smartfarm.common.TimeUtil;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Conversation;
import me.smartfarm.data.models.Message;
import me.smartfarm.data.models.User;
import me.smartfarm.data.repositories.ConversationsRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.data.repositories.UserRepository;
import me.smartfarm.databinding.FragmentChatBinding;


public class ChatFragment extends Fragment {
    private MessageAdapter adapter;
    private List<Message> messageList;

    private String receiverId;
    private String conversationId;
    Conversation conversation;
    FragmentChatBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        loadConversation();

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return binding.getRoot();
    }

    private void loadConversation() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            receiverId = arguments.getString("receiverId");
        }
        if (receiverId == null) {
            Toast.makeText(getContext(), "Error loading messages.", Toast.LENGTH_SHORT).show();
            return;
        }

        new UserRepository(requireContext()).getDocumentById(receiverId, User.class, object -> {
            binding.txtUserName.setText(Util.getUserFullName(object.getFirstName(), object.getLastName()));
            Glide.with(requireContext())
                    .load(object.getImage())
                    .into(binding.imgUserImage);
        });


        ConversationsRepository.getInstance(requireContext()).getConversation(Util.getCurrentUser().getId() + "_" + receiverId, object -> {
            if (object != null) {
                conversationId = Util.getCurrentUser().getId() + "_" + receiverId;
                conversation = object;
                loadMessages();
            } else {
                ConversationsRepository.getInstance(requireContext()).getConversation(receiverId + "_" + Util.getCurrentUser().getId(), object1 -> {
                    if (object1 != null) {
                        conversationId = receiverId + "_" + Util.getCurrentUser().getId();
                        conversation = object1;
                        loadMessages();
                    } else {
                        conversation = new Conversation();
                        List<String> usersInChat = new ArrayList<>();
                        usersInChat.add(receiverId);
                        usersInChat.add(Util.getCurrentUser().getId());
                        conversation.setUsers(usersInChat);
                    }
                });
            }
        });
    }

    private void loadMessages() {
        FirebaseFirestore.getInstance().collection("conversations").document(conversationId).collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Error getting messages.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    assert snapshots != null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Message message = dc.getDocument().toObject(Message.class);
                                messageList.add(message);
                                adapter.notifyItemInserted(messageList.size() - 1);
                                binding.recyclerView.scrollToPosition(messageList.size() - 1);
                                break;
                            case MODIFIED:
                                // Handle modified case
                                break;
                            case REMOVED:
                                // Handle removed case
                                break;
                        }
                    }
                });
    }

    private void sendMessage() {
        String messageText = binding.messageEditText.getText().toString();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(getContext(), "Message cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.messageEditText.setText("");
        Message message = new Message();
        message.setText(messageText);
        message.setSender(Util.getCurrentUser().getId());
        message.setTimestamp(TimeUtil.getNetworkTime());


        if (conversationId == null) {
            conversationId = Util.getCurrentUser().getId() + "_" + receiverId;
        }

        if (conversation.getUpdateTime() == 0) {
            conversation.setUpdateTime(TimeUtil.getNetworkTime());
            binding.messageEditText.setText("");
            FirebaseFirestore.getInstance().collection("conversations").document(conversationId).set(conversation).addOnCompleteListener(task -> {
                uploadMessage(message);
                loadMessages();
            });
        } else {
            uploadMessage(message);
        }


    }

    private void uploadMessage(Message message) {
        conversation.setUpdateTime(TimeUtil.getNetworkTime());
        FirebaseFirestore.getInstance().collection("conversations").document(conversationId).update("updateTime", TimeUtil.getNetworkTime());
        FirebaseFirestore.getInstance().collection("conversations").document(conversationId).collection("messages").add(message)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {

                    } else {
                        Toast.makeText(getContext(), "Error sending message.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}