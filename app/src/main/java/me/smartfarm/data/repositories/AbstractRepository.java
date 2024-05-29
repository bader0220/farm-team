package me.smartfarm.data.repositories;


import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractRepository {
    protected FirebaseFirestore db;
    protected CollectionReference collectionReference;
    protected Context context;


    public AbstractRepository(String collection, Context context) {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(collection);
        this.context = context;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public CollectionReference getCollectionReference() {
        return collectionReference;
    }

    public Context getContext() {
        return context;
    }


    public <T> Task<QuerySnapshot> getAllDocuments(Class<T> objectType, OnLoadingComplete<List<T>> onLoadingComplete) {
        return collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Convert QuerySnapshot to a list of objects
                    List<T> objects = task.getResult().toObjects(objectType);
                    // Pass the list of objects to the callback
                    onLoadingComplete.onLoaded(objects);
                }
            }
        });
    }

    public <T> Task<QuerySnapshot> getAllDocuments(Class<T> objectType, Map<String, String> filters, OnLoadingComplete<List<T>> onLoadingComplete) {
        Query query = collectionReference;

        // Apply filters based on the provided map
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String fieldName = entry.getKey();
            String filterValue = entry.getValue();

            // Add whereEqualTo filter for each key-value pair in the map
            query = query.whereEqualTo(fieldName, filterValue);
        }

        return query.orderBy("creationDate", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Convert QuerySnapshot to a list of objects
                List<T> objects = task.getResult().toObjects(objectType);
                // Pass the list of objects to the callback
                onLoadingComplete.onLoaded(objects);
            } else {
                // Handle errors here
                onLoadingComplete.onLoaded(new ArrayList<>());

            }
        });
    }

    public <T> Task<DocumentSnapshot> getDocumentById(String documentId, Class<T> objectType, OnLoadingComplete<T> onLoadingComplete) {
        DocumentReference docRef = collectionReference.document(documentId);
        return docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Convert DocumentSnapshot to an object
                        T object = document.toObject(objectType);
                        // Pass the object to the callback
                        onLoadingComplete.onLoaded(object);
                    } else {
                        // Document does not exist
                        onLoadingComplete.onLoaded(null);
                    }
                } else {
                    // Handle errors here
                }
            }
        });
    }

    public <T> void saveDocument(T object, OnLoadingComplete<Void> onLoadingComplete) {
        collectionReference.document().set(object).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Pass the document reference to the callback
                    onLoadingComplete.onLoaded(task.getResult());
                } else {
                    // Handle errors here
                    onLoadingComplete.onLoaded(null);
                }
            }
        });
    }

    public <T> void updateDocument(String documentId, T object, OnLoadingComplete<Void> onLoadingComplete) {
        collectionReference.document(documentId).set(object).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Pass the document reference to the callback
                onLoadingComplete.onLoaded(task.getResult());
            } else {
                // Handle errors here
                onLoadingComplete.onLoaded(null);
            }
        });
    }

    public  void deleteDocument(String documentId, OnLoadingComplete<Void> onLoadingComplete) {
        collectionReference.document(documentId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Pass the document reference to the callback
                onLoadingComplete.onLoaded(task.getResult());
            } else {
                // Handle errors here
                onLoadingComplete.onLoaded(null);
            }
        });
    }


}
