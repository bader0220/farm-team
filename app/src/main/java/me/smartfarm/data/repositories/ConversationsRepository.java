package me.smartfarm.data.repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.smartfarm.common.Util;
import me.smartfarm.data.models.Conversation;
import me.smartfarm.data.models.Message;

public class ConversationsRepository extends AbstractRepository {

    @SuppressLint("StaticFieldLeak")
    private static ConversationsRepository instance;

    public ConversationsRepository(Context context) {
        super("conversations", context);
    }

    public static ConversationsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ConversationsRepository(context);
        }
        return instance;
    }


    public void getConversation(String chatId, OnLoadingComplete<Conversation> onLoadingComplete) {
        collectionReference.document(chatId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    Conversation conversation = documentSnapshot.toObject(Conversation.class);
                    if (conversation != null) {
                        onLoadingComplete.onLoaded(conversation);
                    } else {
                        // Handle the case where the document exists but conversion to Conversation failed
                        Log.e("FirestoreError", "Document exists but conversion to Conversation returned null.");
                        onLoadingComplete.onLoaded(null);  // Or handle this error as needed
                    }
                } else {
                    // Handle the case where the document does not exist
                    Log.e("FirestoreError", "Document does not exist.");
                    onLoadingComplete.onLoaded(null);  // Or handle this error as needed
                }
            } else {
                // Handle the error
                Log.e("FirestoreError", "Error getting document: ", task.getException());
                onLoadingComplete.onLoaded(null);  // Or handle this error as needed
            }
        });
    }



    public <T> void getConversations(Class<T> objectType, OnLoadingComplete<List<T>> onLoadingComplete) {

        FirebaseFirestore.getInstance().collection("conversations")
                .whereArrayContains("users", Util.getCurrentUser().getId())
                .orderBy("updateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (!task.getResult().isEmpty()){
                                List<T> objects = task.getResult().toObjects(objectType);
                                onLoadingComplete.onLoaded(objects);
                            }else{
                                onLoadingComplete.onLoaded(new ArrayList<>());
                            }

                        }
                    }
                });

    }


}
