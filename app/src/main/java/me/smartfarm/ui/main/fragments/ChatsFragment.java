package me.smartfarm.ui.main.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import me.smartfarm.R;
import me.smartfarm.adapters.ConversationAdapter;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Conversation;
import me.smartfarm.data.models.Message;
import me.smartfarm.data.repositories.ConversationsRepository;
import me.smartfarm.data.repositories.OnLoadingComplete;
import me.smartfarm.databinding.FragmentChatsBinding;


public class ChatsFragment extends Fragment {

    ConversationAdapter adapter;
    FragmentChatsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        loadConversations();
        return binding.getRoot();
    }

    private void loadConversations() {
        ConversationsRepository.getInstance(requireContext()).getConversations(Conversation.class, object -> {
            if (object.isEmpty()){
                binding.noChats.setVisibility(View.VISIBLE);
            }else {
                binding.noChats.setVisibility(View.GONE);
                adapter = new ConversationAdapter(object);
                adapter.setOnItemClickListener(receiverId -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("receiverId", receiverId);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
                    navController.navigate(R.id.action_chatsFragment_to_chatFragment, bundle);
                });

                binding.recyclerViewChats.setAdapter(adapter);
                binding.recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));
            }

        });

    }

}
